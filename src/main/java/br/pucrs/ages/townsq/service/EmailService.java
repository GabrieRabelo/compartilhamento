package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.util.HashMap;

@Service
public class EmailService {

    private HashMap<String, Object> templateEmailModel;

    private final String urlQuestion = "http://" + InetAddress.getLoopbackAddress().getHostName() + ":8080/question/";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine springTemplateEngine;

    public void sendEmail() {
        try {

            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mail, true);
            Context context = new Context();
            context.setVariables(templateEmailModel);
            String mailTemplate = springTemplateEngine.process("templateEmail", context);

            message.setTo(templateEmailModel.get("userEmail").toString());
            message.setFrom("c88b3da83e-981698@inbox.mailtrap.io");
            message.setSubject(templateEmailModel.get("emailSubject").toString());
            message.setText(mailTemplate, true);

            javaMailSender.send(mail);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Async
    public void createEmail(Object object) {
        templateEmailModel = new HashMap<>();

        if (object instanceof Answer) {
            Answer answer = (Answer) object;

            templateEmailModel.put("emailTextInitial", "A sua pergunta " );
            templateEmailModel.put("emailTextEnd", " tem uma nova resposta.");
            templateEmailModel.put("questionUrl", urlQuestion + answer.getQuestion().getId());
            templateEmailModel.put("emailSubject","TownSQ - Alguém respondeu sua pergunta");
            templateEmailModel.put("questionTitle", answer.getQuestion().getTitle());
            templateEmailModel.put("userName", answer.getQuestion().getUser().getName());
            templateEmailModel.put("userEmail", answer.getQuestion().getUser().getEmail());


            sendEmail();
        } else if (object instanceof Comment) {
            Comment comment = (Comment) object;

            if (comment.getAnswer() != null) {
                Answer answer = comment.getAnswer();
                templateEmailModel.put("emailTextInitial", "A sua resposta na pergunta ");
                templateEmailModel.put("questionTitle", answer.getQuestion().getTitle());
                templateEmailModel.put("emailTextEnd", " tem um novo comentário.");
                templateEmailModel.put("emailSubject","TownSQ - Alguém comentou sua resposta");
                templateEmailModel.put("userName", answer.getUser().getName());
                templateEmailModel.put("userEmail", answer.getUser().getEmail());
                templateEmailModel.put("questionUrl", urlQuestion  + answer.getQuestion().getId());
            } else if (comment.getQuestion() != null) {
                Question question = comment.getQuestion();
                templateEmailModel.put("emailTextInitial", "A sua pergunta " );
                templateEmailModel.put("emailTextEnd", " tem um novo comentário.");
                templateEmailModel.put("questionTitle", question.getTitle());
                templateEmailModel.put("emailSubject","TownSQ - Alguém comentou a sua pergunta");
                templateEmailModel.put("userName", question.getUser().getName());
                templateEmailModel.put("userEmail", question.getUser().getEmail());
                templateEmailModel.put("questionUrl", urlQuestion + question.getId());
            }
            sendEmail();
        }
    }
}
