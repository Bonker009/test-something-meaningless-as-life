package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.jwt.JwtService;
import com.kshrd.devconnect_springboot.model.dto.request.*;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.AuthResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import com.kshrd.devconnect_springboot.model.mapper.AppUserMapper;
import com.kshrd.devconnect_springboot.respository.AppUserRepository;
import com.kshrd.devconnect_springboot.respository.AuthRepository;
import com.kshrd.devconnect_springboot.respository.DeveloperProfilesRepository;
import com.kshrd.devconnect_springboot.respository.RecruiterRepository;
import com.kshrd.devconnect_springboot.service.AppUserService;
import com.kshrd.devconnect_springboot.service.AuthService;
import com.kshrd.devconnect_springboot.service.EmailSenderService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import com.kshrd.devconnect_springboot.utils.RandomOtp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final JwtService jwtService;
    private final AuthRepository authRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    private final EmailSenderService emailSenderService;
    private final RedisTemplate<String, String> redisTemplate;
    private final RecruiterRepository recruiterRepository;
    private final DeveloperProfilesRepository developerProfilesRepository;

    private void authenticate(String email, String password) {
        try {
            AppUser appUser = authRepository.getUserByEmail(email);

            if (appUser == null) {
                throw new NotFoundException("Invalid email");
            }
            if (!passwordEncoder.matches(password, appUser.getPassword())) {
                throw new NotFoundException("Invalid Password");
            }
            if(!appUser.getIsVerified()) {
                throw new BadRequestException("Your account is not verified");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        } catch (DisabledException e) {
            throw new BadRequestException("USER_DISABLED" + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new BadRequestException("INVALID_CREDENTIALS" + e.getMessage());
        }
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        String email = authRequest.getEmail().toLowerCase();
        AppUser appUser = authRepository.getUserByEmail(email);
        if(appUser == null) throw new BadRequestException("User is not registered");
        if(!appUser.getIsVerified()) throw new BadRequestException("User needs to verify before login");
        authenticate(authRequest.getEmail().toLowerCase(), authRequest.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(authRequest.getEmail().toLowerCase());
        final String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }

    @SneakyThrows
    @Override
    public AppUserResponse register(AppUserRequest appUserRequest) {
        if (authRepository.getUserByEmail(appUserRequest.getEmail().toLowerCase(Locale.ROOT)) != null)
            throw new BadRequestException("User already exists");

        appUserRequest.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));
        AppUser appUser = authRepository.register(appUserRequest);
        System.out.println("AppUser: " + appUser);
        String otp = new RandomOtp().generateOtp();
        while (redisTemplate.opsForValue().get(otp) != null) {
            otp = new RandomOtp().generateOtp();
        }
            redisTemplate.opsForValue().set(appUser.getEmail(), otp, Duration.ofMinutes(5));
            emailSenderService.sendEmail(appUser.getEmail(), otp);
        AppUserResponse appUserResponse = appUserMapper.toResponse(appUser);
        if (appUser.getIsRecruiter()) {
            log.info("Creating recruiter profile for user: {}", appUser.getUserId());
            recruiterRepository.createRecruiterProfile(appUser.getUserId());
        }
        if(!appUser.getIsRecruiter()) {

            developerProfilesRepository.insertDeveloperProfiles(appUser.getUserId());
        }
        return  appUserResponse;
    }

    @Override
    public void verify(String emailRequest, String otpCode) {
        String email = emailRequest.toLowerCase();
        AppUser appUser = authRepository.getUserByEmail(email);
        if (appUser == null) throw new NotFoundException("User doesn't exist");
        if (appUser.getIsVerified()) throw new BadRequestException("User already verified");

        String storedOTP = redisTemplate.opsForValue().get(email);
        if(storedOTP == null) throw new BadRequestException("OTP already expired");
        if (!storedOTP.equals(otpCode)) throw new BadRequestException("OTP code doesn't match");

        redisTemplate.delete(otpCode);
        appUserRepository.updateVerificationStatus(email);
    }

    @SneakyThrows
    @Override
    public void resend(String emailRequest) {
        String email = emailRequest.toLowerCase();
        AppUser appUser = authRepository.getUserByEmail(email);
        if (appUser == null) throw new NotFoundException("User doesn't exist");
        if(appUser.getIsVerified()) throw new BadRequestException("User already verified");
        String otp = new RandomOtp().generateOtp();

        while (redisTemplate.opsForValue().get(otp) != null) {
            otp = new RandomOtp().generateOtp();
        }

        emailSenderService.sendEmail(appUser.getEmail(), otp);
        redisTemplate.opsForValue().set(appUser.getEmail(), otp, Duration.ofMinutes(5));
    }

    @Override
    public void forgotPassword(String emailRequest) {
        String email = emailRequest.toLowerCase();
        AppUser appUser = authRepository.getUserByEmail(email);
        if (appUser == null) throw new NotFoundException("User doesn't exist");
        String otp = new RandomOtp().generateOtp();

        while (redisTemplate.opsForValue().get(otp) != null) {
            otp = new RandomOtp().generateOtp();
        }

        emailSenderService.sendEmail(appUser.getEmail(), otp);
        redisTemplate.opsForValue().set(appUser.getEmail(), otp, Duration.ofMinutes(5));
    }

    @Override
    public void verifyForgot(String emailRequest, String otpCode) {
        String email = emailRequest.toLowerCase();
        AppUser appUser = authRepository.getUserByEmail(email);
        if (appUser == null) throw new NotFoundException("User doesn't exist");
        String storedOTP = redisTemplate.opsForValue().get(email);
        if(storedOTP == null) throw new BadRequestException("OTP already expired");
        if (!storedOTP.equals(otpCode)) throw new BadRequestException("OTP code doesn't match");
    }

    @Override
    public AppUserResponse resetPassword(String emailRequest, String otpCode, String newPassword) {
        String email = emailRequest.toLowerCase();
        AppUser appUser = authRepository.getUserByEmail(email);
        if (appUser == null) throw new NotFoundException("User doesn't exist");

        String storedOTP = redisTemplate.opsForValue().get(email);
        if(storedOTP == null) throw new BadRequestException("OTP already expired");
        if (!storedOTP.equals(otpCode)) throw new BadRequestException("OTP code doesn't match");

        redisTemplate.delete(otpCode);

        String password = passwordEncoder.encode(newPassword);
        AppUser newUserPassword = authRepository.updatePassword(email, password);

        return appUserMapper.toResponse(newUserPassword);

    }

    @Override
    public void verifyOldPassword(PasswordRequest passwordRequest) {
        if (!passwordEncoder.matches(passwordRequest.getPassword(), CurrentUser.appUser.getPassword()))
            throw new BadRequestException("Wrong password");
    }

    @Override
    public void changePassword(PasswordRequest passwordRequest) {
        authRepository.changePassword(passwordRequest.getPassword(), CurrentUser.appUserId);
    }

}
