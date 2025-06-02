package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    @NotBlank(message = "Skill Name not allow null")
    @Size(max = 100 , message = "Skill Name can't greater than 100")
    private String skillName;
}
