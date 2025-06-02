package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.Certificate;

public interface EmailSenderService {
    void sendEmail(String toEmail, String otp);
    void generatePdf(String toEmail, Certificate name);
}
