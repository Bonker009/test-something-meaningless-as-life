package com.kshrd.devconnect_springboot.model.JSONBTemplate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
    @NotNull(message = "Input cannot be null")
    @Size(min = 1, message = "Input must not be empty")
    private List<@NotNull  Object> input;
    @NotNull(message = "Expected output cannot be null")
    private Object expectedOutput;
}