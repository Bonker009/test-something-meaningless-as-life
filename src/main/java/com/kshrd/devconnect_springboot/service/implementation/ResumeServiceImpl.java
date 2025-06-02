package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.ResumeRequest;
import com.kshrd.devconnect_springboot.model.entity.Resume;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.*;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.request.ExperienceRequest;
import com.kshrd.devconnect_springboot.respository.PositionRepository;
import com.kshrd.devconnect_springboot.respository.ResumeRepository;
import com.kshrd.devconnect_springboot.respository.SkillRepository;
import com.kshrd.devconnect_springboot.service.ResumeService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository repository;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;

    @Override
    public Resume selectCurrentResumes() {
        Resume resume = repository.selectCurrentResumes(CurrentUser.appUserId);
        if (resume == null) {
            throw new NotFoundException("Resume not found");
        }
        return resume;
    }

    //? UPDATE RESUME
    @Override
    public Resume createResumes(ResumeRequest entity) {

        // ! VALIDATION

        //TODO Check if the user already has a resume
        Resume resume = repository.selectCurrentResumes(CurrentUser.appUserId);
        if (resume != null) {
            throw new BadRequestException("You already have a resume");
        }

        //TODO Check starting year and ending year
        if(entity.getInformation().getExperiences() != null) {
            for (ExperienceRequest experience : entity.getInformation().getExperiences()) {
                if (experience.getStartYear().isEqual(experience.getEndYear()) || experience.getStartYear().isAfter(experience.getEndYear())) {
                    throw new BadRequestException("Start year cannot be equal and greater than end year");
                }

                if(experience.getStartYear().getYear() < 1990 && experience.getStartYear().isEqual(LocalDate.now())) {
                    throw new BadRequestException("Start year must be greater than 1990 and less than or equal to the current year");
                }
                if(experience.getEndYear().getYear() < 1990 && experience.getEndYear().isEqual(LocalDate.now())) {
                    throw new BadRequestException("End year must be greater than 1990 and less than or equal to the current year");
                }
            }
        }

        //TODO check if phone is duplicate
        String phoneNumber = entity.getPhoneNumber().trim();

        if(repository.selectAllForValidation() != null){
            String oldPhone = repository.selectAllForValidation().getPhoneNumber();
            for(Reference reference : repository.selectAllForValidation().getInformation().getReferences()) {
                String referencePhone = reference.getPhoneNumber();
                if(phoneNumber.equals(oldPhone) || phoneNumber.equals(referencePhone)) {
                    throw new BadRequestException("This phone number is already used (your phone)");
                }
                for(Reference ref : entity.getInformation().getReferences()) {
                    String phoneReference = ref.getPhoneNumber();
                    if(referencePhone != null && (referencePhone.equals(oldPhone) || referencePhone.equals(phoneReference))) {
                        throw new BadRequestException("This phone number is already used (reference phone)");
                    }
                }
            }
        }


        // TODO Check if email is duplicate
        if(repository.selectAllForValidation() != null) {
            String email = entity.getEmail().trim();
            String oldEmail = repository.selectAllForValidation().getEmail();
            for(Reference reference : repository.selectAllForValidation().getInformation().getReferences()) {
                String referenceEmail = reference.getEmail();
                if(email.equals(oldEmail) || email.equals(referenceEmail) ) {
                    throw new BadRequestException("This email is already used (your email)");
                }
                for(Reference ref : entity.getInformation().getReferences()) {
                    String emailReference = ref.getEmail();
                    if(referenceEmail != null && (referenceEmail.equals(oldEmail) || referenceEmail.equals(emailReference))) {
                        throw new BadRequestException("This email is already used (reference email)");
                    }
                }

            }
        }


        //TODO Check if the user below 18 years old
        if (entity.getDob() != null) {
            int age = LocalDate.now().getYear() - entity.getDob().getYear();
            if (age <= 18) {
                throw new BadRequestException("You must be at least 18 years old to create a resume");
            }
        }


        //TODO Check if the position exists
        if(positionRepository.getPositionById(entity.getPosition()) == null) {
            throw new NotFoundException("Position not found");
        }

        //TODO check and add position to experience
        List<Experience> experienceNames = new ArrayList<>();
        for (ExperienceRequest experience : entity.getInformation().getExperiences()) {
            //TODO Check if the position exists
            if (positionRepository.getPositionById(experience.getPosition()) == null) {
                throw new NotFoundException("Position not found");
            }
            //* Convert positionId to positionName
            String positionName = positionRepository.getPositionById(experience.getPosition()).getPositionName();
            Experience experienceName = new Experience();
            experienceName.setCompanyName(experience.getCompanyName().trim());
            experienceName.setPosition(positionName.trim());
            experienceName.setCompanyLocation(experience.getCompanyLocation().trim());
            experienceName.setStartYear(experience.getStartYear());
            experienceName.setEndYear(experience.getEndYear());
            experienceNames.add(experienceName);
        }

        //? Trim the string values
        entity.setAddress(entity.getAddress()
                .trim()
                .replaceAll("\\s+", " ")
        );
        entity.setFullName(entity.getFullName()
                .trim()
                .replaceAll("\\s+", " ")
        );
        entity.setEmail(entity.getEmail()
                .trim()
        );
        entity.setDescription(entity.getDescription()
                .trim()
        );
        entity.setPlaceOfBirth(entity.getPlaceOfBirth()
                .trim()
        );
        entity.setPhoneNumber(entity.getPhoneNumber().trim());

        for(Language language : entity.getInformation().getLanguages()) {
            language.setLanguage(language.getLanguage()
                    .trim()
                    .replaceAll("\\s+", " ")
            );
        }

        if(entity.getInformation().getReferences() != null) {
            for(Reference reference : entity.getInformation().getReferences()) {
                reference.setName(reference.getName()
                        .trim()
                        .replaceAll("\\s+", " ")
                );
                reference.setEmail(reference.getEmail().trim());
                reference.setJobTitle(reference.getJobTitle()
                        .trim()
                        .replaceAll("\\s+", " ")
                );
                reference.setPhoneNumber(reference.getPhoneNumber().trim());
            }
        }

        for(Education education : entity.getInformation().getEducations()) {
            education.setSchoolName(education.getSchoolName()
                    .trim()
            );
            education.setSchoolLocation(education.getSchoolLocation()
                    .trim()
            );
        }

        //! Set new Resume entity
        //* Covert the skillId to skillName
        List<UUID> skill = entity.getInformation().getSkills();
        List<Skill> skillName = new ArrayList<>();
        Skill sk = new Skill();
        for(UUID skillId : skill) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found!");
            }
            entity.getInformation().setSkills(skill);
            String skName =skillRepository.getSkillById(skillId).getSkillName();
            sk.setSkillName(skName);
            skillName.add(sk);
        }

        //! SET THE SKILLS IN THE RESUME INFORMATION
        //* Set the skills in the ResumeInformation
        ResumeInformation information = new ResumeInformation();
        information.setSkills(skillName);
        information.setEducations(entity.getInformation().getEducations());
        information.setLanguages(entity.getInformation().getLanguages());
        information.setExperiences(experienceNames);
        information.setReferences(entity.getInformation().getReferences());
        //* Set the position name
        String position = positionRepository.getPositionById(entity.getPosition()).getPositionName();
        return repository.insertResumes(entity , position , information , CurrentUser.appUserId);
    }


    //? UPDATE RESUME

    @Override
    public Resume updateResumes(ResumeRequest entity) {

        // ! VALIDATION

        //TODO Check starting year and ending year
        for (ExperienceRequest experience : entity.getInformation().getExperiences()) {
            if (experience.getStartYear().getYear() > experience.getEndYear().getYear()) {
                throw new BadRequestException("Start year cannot be greater than end year");
            }
        }
        //TODO Check if the user below 18 years old
        if (entity.getDob() != null) {
            int age = LocalDate.now().getYear() - entity.getDob().getYear();
            if (age <= 18) {
                throw new BadRequestException("You must be at least 18 years old to create a resume");
            }
        }

        //TODO Check if the user already has a resume
        Resume resume = repository.selectCurrentResumes(CurrentUser.appUserId);
        if (resume == null) {
            throw new BadRequestException("You don't have a resume for update");
        }

        //TODO Check if the position exists
        if(positionRepository.getPositionById(entity.getPosition()) == null) {
            throw new NotFoundException("Position not found");
        }

        //TODO check and add position to experience
        List<Experience> experienceNames = new ArrayList<>();
        for (ExperienceRequest experience : entity.getInformation().getExperiences()) {
            //TODO Check if the position exists
            if (positionRepository.getPositionById(experience.getPosition()) == null) {
                throw new NotFoundException("Position not found");
            }
            //* Convert positionId to positionName
            String positionName = positionRepository.getPositionById(experience.getPosition()).getPositionName();
            Experience experienceName = new Experience();
            experienceName.setCompanyName(experience.getCompanyName().trim());
            experienceName.setPosition(positionName.trim());
            experienceName.setCompanyLocation(experience.getCompanyLocation().trim());
            experienceName.setStartYear(experience.getStartYear());
            experienceName.setEndYear(experience.getEndYear());
            experienceNames.add(experienceName);
        }

        //? Trim the string values
        entity.setAddress(entity.getAddress()
                .trim()
                .replaceAll("\\s+", " ")
        );
        entity.setFullName(entity.getFullName()
                .trim()
                .replaceAll("\\s+", " ")
        );
        entity.setEmail(entity.getEmail()
                .trim()
        );
        entity.setDescription(entity.getDescription()
                .trim()
                .replaceAll("\\s+", " ")
        );

        entity.setPlaceOfBirth(entity.getPlaceOfBirth()
                .trim()
                .replaceAll("\\s+", " ")
        );
        entity.setPhoneNumber(entity.getPhoneNumber().trim());

        for(Language language : entity.getInformation().getLanguages()) {
            language.setLanguage(language.getLanguage()
                    .trim()
                    .replaceAll("\\s+", " ")
            );
        }

        for(Reference reference : entity.getInformation().getReferences()) {
            reference.setName(reference.getName()
                    .trim()
                    .replaceAll("\\s+", " ")
            );
            reference.setEmail(reference.getEmail().trim());
            reference.setJobTitle(reference.getJobTitle()
                    .trim()
                    .replaceAll("\\s+", " ")
            );
            reference.setPhoneNumber(reference.getPhoneNumber().trim());
        }

        for(Education education : entity.getInformation().getEducations()) {
            education.setSchoolName(education.getSchoolName()
                    .trim()
                    .replaceAll("\\s+", " ")
            );
            education.setSchoolLocation(education.getSchoolLocation().trim());
        }

        //! Set new Resume entity
        //* Covert the skillId to skillName
        List<UUID> skill = entity.getInformation().getSkills();
        List<Skill> skillName = new ArrayList<>();
        Skill sk = new Skill();
        for(UUID skillId : skill) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found!");
            }
            entity.getInformation().setSkills(skill);
            String skName =skillRepository.getSkillById(skillId).getSkillName();
            sk.setSkillName(skName);
            skillName.add(sk);
        }

        //! SET THE SKILLS IN THE RESUME INFORMATION
        //* Set the skills in the ResumeInformation
        ResumeInformation information = new ResumeInformation();
        information.setSkills(skillName);
        information.setEducations(entity.getInformation().getEducations());
        information.setLanguages(entity.getInformation().getLanguages());
        information.setExperiences(experienceNames);
        information.setReferences(entity.getInformation().getReferences());

        //* Set the position name
        String position = positionRepository.getPositionById(entity.getPosition()).getPositionName();

        return repository.updateResumes(entity ,position , information ,CurrentUser.appUserId);
    }

    @Override
    public void deleteResumes() {
        Resume resume = repository.selectCurrentResumes(CurrentUser.appUserId);
        if (resume == null) {
            throw new NotFoundException("You don't have a resume for delete");
        }

        repository.deleteResumes(CurrentUser.appUserId);
    }
}
