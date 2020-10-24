package br.pucrs.ages.townsq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("duduuulessa@gmail.com");
        message.setFrom("c88b3da83e-981698@inbox.mailtrap.io");
        message.setSubject("Test");
        message.setText("This is a test");

        try {
            javaMailSender.send(message);

        } catch (MailException e) {
            System.out.println(e.getMessage());
        }

    }
}
