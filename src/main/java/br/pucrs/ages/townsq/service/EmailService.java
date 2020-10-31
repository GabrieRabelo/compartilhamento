package br.pucrs.ages.townsq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine springTemplateEngine;

    @Autowired
    public void sendEmail() throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mail, true);
        Context context = new Context();
        String mailTemplate = springTemplateEngine.process("email", context);

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
