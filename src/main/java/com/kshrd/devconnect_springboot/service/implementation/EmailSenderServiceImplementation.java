package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.model.entity.Certificate;
import com.kshrd.devconnect_springboot.service.EmailSenderService;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.activation.DataSource;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImplementation implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @SneakyThrows
    @Override
    public void sendEmail(String toEmail, String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);
        String process = templateEngine.process("index", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setSubject("Email verification OTP code");
        mimeMessageHelper.setText(process, true);
        ClassPathResource image = new ClassPathResource("images/DCLOGO-17.png");
        mimeMessageHelper.addInline("logo", image);
        mimeMessageHelper.setTo(toEmail);

        javaMailSender.send(mimeMessage);
    }

    @SneakyThrows
    @Override
    public void generatePdf(String toEmail, Certificate certificate) {
        // 1. Create context and set variables
        Context context = new Context();
        context.setVariable("firstName", certificate.getUserResponse().getFirstName());
        context.setVariable("lastName", certificate.getUserResponse().getLastName());
        context.setVariable("eventName", certificate.getHackathon().getTitle());
        context.setVariable("month", certificate.getIssuedDate().getMonth());
        context.setVariable("day", certificate.getIssuedDate().getDayOfMonth());
        context.setVariable("year", certificate.getIssuedDate().getYear());

        // 2. Process template with context - CAPTURE the result
        String processedHtml = templateEngine.process("certificate", context);

        // 3. Parse the PROCESSED HTML (not the raw template)
        Document document = Jsoup.parse(processedHtml);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        // 4. Generate PDF
        try (OutputStream os = new FileOutputStream("src/main/resources/static/output.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri("output");
            builder.useDefaultPageSize(680, 435, BaseRendererBuilder.PageSizeUnits.MM);
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
        }

        // 5. Send email with PDF attachment
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Certificate");
        helper.setText("Please find your certificate attached.");

        // Add PDF attachment
        FileSystemResource pdfFile = new FileSystemResource("src/main/resources/static/output.pdf");
        helper.addAttachment("certificate.pdf", pdfFile);

        // Add inline logo
        ClassPathResource image = new ClassPathResource("images/DCLOGO-17.png");
        helper.addInline("logo", image);

        javaMailSender.send(mimeMessage);
    }
}
