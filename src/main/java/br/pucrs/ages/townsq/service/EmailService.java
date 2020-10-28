package br.pucrs.ages.townsq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService{

    private String mailTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    public void sendEmail() throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mail, true);
        Context context = new Context();
        mailTemplate = springTemplateEngine.process("email", context);

        message.setTo("duduuulessa@gmail.com");
        message.setFrom("c88b3da83e-981698@inbox.mailtrap.io");
        message.setSubject("Test");
        message.setText(mailTemplate, true);

        try {
           javaMailSender.send(mail);

        } catch (MailException e) {
            System.out.println(e.getMessage());
        }

    }
}
