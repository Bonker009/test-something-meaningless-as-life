package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeInformation {
    private List<Education> educations;
    private List<Skill> skills;
    private List<Experience> experiences;
    private List<Reference> references;
    private List<Language> languages;
}