package com.kshrd.devconnect_springboot.model.dto.request;

import com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.request.ResumeInformationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "Full name must contain only letters and spaces"
    )
    @Schema(example = "John Doe", defaultValue = "Sok Chanthy")
    private String fullName;

    @NotNull(message = "gender is required")
    private Boolean isFemale;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+\\d{1,3}-\\d{6,10}$" ,message = "Invalid your phone number format example: +855-12345678")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^(?!.*\\.\\.)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format"
    )
    @Schema(example = "example@gmail.com", defaultValue = "example@gmail.com")
    private String email;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dob;

    @NotBlank(message = "Place of birth is required")
    private String placeOfBirth;

    @NotNull
    @Pattern(regexp = "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|jpeg|png)$", message = "Invalid picture URL")
    private String picture;

    @NotNull(message = "Position is required")
    private UUID position;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Resume information is required")
    @Valid
    private ResumeInformationRequest information;

}
