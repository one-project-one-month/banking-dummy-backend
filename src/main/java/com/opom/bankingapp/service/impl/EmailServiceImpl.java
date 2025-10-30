package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        logger.info("Preparing to send OTP email to: {}", toEmail);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@opom-banking.com");
            message.setTo(toEmail);
            message.setSubject("Your One-Time Password (OTP) for Registration");
            message.setText("Dear User,\n\n"
                    + "Your One-Time Password (OTP) for registration is: " + otp + "\n"
                    + "This OTP is valid for a short period. Please do not share it with anyone.\n\n"
                    + "Thank you,\n"
                    + "The OPOM Banking Team");

            mailSender.send(message);
            logger.info("Successfully sent OTP email to: {}", toEmail);

        } catch (MailException e) {
            logger.error("Failed to send OTP email to: {}. Error: {}", toEmail, e.getMessage());
        }
    }
}
