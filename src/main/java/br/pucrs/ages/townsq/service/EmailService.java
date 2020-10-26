package br.pucrs.ages.townsq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService{

    private String mailTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public void sendEmail() throws MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mail, true);


        message.setTo("duduuulessa@gmail.com");
        message.setFrom("c88b3da83e-981698@inbox.mailtrap.io");
        message.setSubject("Test");
        message.setText("<h1>Bem Vindo</h1> <br/> <p>Acho que bombou</p>", true);

        try {
           javaMailSender.send(mail);

        } catch (MailException e) {
            System.out.println(e.getMessage());
        }

    }
}
