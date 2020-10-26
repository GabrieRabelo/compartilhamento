package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.MessagingException;

@Controller
public class EmailController {


    @Autowired
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-email")
    public String sendEmail() throws MessagingException {
        emailService.sendEmail();
        return "redirect:/";
    }
}
