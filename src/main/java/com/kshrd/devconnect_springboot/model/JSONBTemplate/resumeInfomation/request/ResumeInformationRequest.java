package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.request;

import com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeInformationRequest {
        @NotNull(message = "Education not allow null")
        @Valid
        private List<Education> educations;
        @NotNull(message = "SKills not allow null")
        @Valid
        private List<UUID> skills;
        @Valid
        private List<ExperienceRequest> experiences;
        @Valid
        private List<Reference> references;
        @NotNull(message = "Language not allow null")
        @Valid
        private List<Language> languages;
}
