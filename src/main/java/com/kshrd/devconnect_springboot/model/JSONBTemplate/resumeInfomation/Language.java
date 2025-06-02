package com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language {
    @NotBlank(message = "language not allow null")
    @Size(max = 20 , message = "language can't greater than 20")
    @Pattern(
            regexp = "^[a-zA-Z]+$",
            message = "language must contain only letters "
    )
    private String language;
}