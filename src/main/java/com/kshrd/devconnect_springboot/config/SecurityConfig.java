package com.kshrd.devconnect_springboot.config;


import com.kshrd.devconnect_springboot.exception.CustomAccessDeniedHandler;
import com.kshrd.devconnect_springboot.jwt.JwtAuthEntryPoint;
import com.kshrd.devconnect_springboot.jwt.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntrypoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request

                        //resume-controller
                        .requestMatchers(
                                "/api/v1/create-resume",
                                "/api/v1/update-resume",
                                "/api/v1/delete-resume",
                                "/api/v1/get-resume"
                        ).hasRole("DEVELOPER")

                        
                        .requestMatchers("/api/v1/badge").hasAnyRole("DEVELOPER" , "RECRUITER")
                        //jobs-controller
                        .requestMatchers("/api/v1/jobs/update/status/*")
                        .hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/jobs/*").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/jobs/*").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.POST,"/api/v1/jobs").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/jobs/my-all-job").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/jobs/job-status/*").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/jobs").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/jobs/*").permitAll()

                        // join job controller
                        .requestMatchers(HttpMethod.POST,"/api/v1/join-job/*").hasRole("DEVELOPER")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/join-job/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/join-jobs/all-invitation").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/join-jobs/job-owner/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/join-jobs/invite-job/*/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/join-jobs/approve-invite/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/join-jobs/approve-application").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/join-job").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/join-jobs/*").hasRole("RECRUITER")
                        .requestMatchers("api/v1/join-jobs/invite-job/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/join-jobs/delete-invite/*").hasAnyRole("DEVELOPER", "RECRUITER")

                        // submission controller
                        .requestMatchers("/api/v1/submission/submitCode/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/submission/testCode/*").hasRole("DEVELOPER")

                        // coding challenge controller
                        .requestMatchers("/api/v1/code-challenge/get-all").permitAll()
                        .requestMatchers("/api/v1/code-challenge/get-by-id/*").permitAll()
                        .requestMatchers("/api/v1/code-challenge/create").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/code-challenge/update/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/code-challenge/delete/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/code-challenge/get-all").permitAll()

                        // developer profile controller
                        .requestMatchers("/api/developer-profile/complete-profile").hasRole("DEVELOPER")
                        .requestMatchers("/api/developer-profile/add-github-username/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/developer-profile/get-github-username").hasRole("DEVELOPER")
                        .requestMatchers("/api/developer-profile/").hasRole("DEVELOPER")
                        .requestMatchers("/api/developer-profile/all-developer").hasRole("DEVELOPER")

                        // recruiter profile controller
                        .requestMatchers("/api/v1/recruiter-profile").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/recruiter-profile/update").hasRole("RECRUITER")


                        // project showcase
                        .requestMatchers("/api/v1/projects/invite/join/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/projects/request/join/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/projects/accept-invite/*").hasRole("DEVELOPER")

                        .requestMatchers(HttpMethod.POST,"/api/v1/hackathons").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/hackathons/*").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/hackathons/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/hackathons/recruiter/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/hackathons/evaluate_developer/*").hasRole("RECRUITER")

                        .requestMatchers("/api/v1/hackathons/submit-hackathon/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/hackathons/join-hackathon/*").hasRole("DEVELOPER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/hackathons/history").hasRole("DEVELOPER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/hackathons/history/*").hasRole("DEVELOPER")

                        // developer profile
                        .requestMatchers(
                                "/api/developer-profile/complete-profile",
                                "/api/developer-profile/add-github-username/*",
                                "/api/developer-profile/get-github-username",
                                "/api/developer-profile/"
                        ).hasRole("DEVELOPER")
                        .requestMatchers("/api/developer-profile/all-developer").permitAll()

                        // hackathons controller
                        .requestMatchers(HttpMethod.GET,"/api/v1/hackathons").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/hackathons/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/hackathons").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/hackathons/*").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/hackathons/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/hackathons/join-hackathon/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/hackathons/submit-hackathon/*").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/hackathons/evaluate_developer/*").hasRole("RECRUITER")
                        .requestMatchers("/api/v1/hackathons/history").hasRole("DEVELOPER")
                        .requestMatchers("/api/v1/hackathons/recruiter").hasRole("RECRUITER")

                        // swagger ui
                        .requestMatchers("/api/v1/auths/**", "/v3/api-docs/**",
                                "/comments/reply",
                                "/comments/create",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/v1/files/**"
                        ).permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntrypoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

        return http.build();
    }
}