package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private HashMap<String, Object> templateEmailModel;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine springTemplateEngine;

    @Autowired
    public void sendEmail() {
        try {

            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mail, true);
            Context context = new Context();
            context.setVariables(templateEmailModel);
            String mailTemplate = springTemplateEngine.process("templateEmail", context);

            message.setTo(templateEmailModel.get("questionUserEmail").toString());
            message.setFrom("c88b3da83e-981698@inbox.mailtrap.io");
            message.setSubject("Test");
            message.setText(mailTemplate, true);

            javaMailSender.send(mail);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void createEmail(Object object) {
        templateEmailModel = new HashMap<>();

        if (object instanceof Answer) {
            Answer answer = (Answer) object;

            templateEmailModel.put("mailType", "resposta");
            templateEmailModel.put("answerUserName", answer.getUser().getName());
            templateEmailModel.put("questionTitle", answer.getQuestion().getTitle());
            templateEmailModel.put("questionUserName", answer.getQuestion().getUser().getName());
            templateEmailModel.put("questionUserEmail", answer.getQuestion().getUser().getEmail());

            sendEmail();
        }
    }
}
