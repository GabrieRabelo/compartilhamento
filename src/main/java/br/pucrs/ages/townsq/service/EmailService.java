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


    private static final String USER_EMAIL = "userEmail";
    private static final String EMAIL_SUBJECT = "emailSubject";
    private static final String EMAIL_TEXT_INITIAL = "emailTextInitial";
    private static final String EMAIL_TEXT_END = "emailTextEnd";
    private static final String QUESTION_URL = "questionUrl";
    private static final String QUESTION_TITLE = "questionTitle";
    private static final String USER_NAME = "userName";

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

            message.setTo(templateEmailModel.get(USER_EMAIL).toString());
            message.setFrom("c88b3da83e-981698@inbox.mailtrap.io");
            message.setSubject(templateEmailModel.get(EMAIL_SUBJECT).toString());
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

            templateEmailModel.put(EMAIL_TEXT_INITIAL, "A sua pergunta " );
            templateEmailModel.put(EMAIL_TEXT_END, " tem uma nova resposta.");
            templateEmailModel.put(QUESTION_URL, urlQuestion + answer.getQuestion().getId());
            templateEmailModel.put(EMAIL_SUBJECT,"TownSQ - Alguém respondeu sua pergunta");
            templateEmailModel.put(QUESTION_TITLE, answer.getQuestion().getTitle());
            templateEmailModel.put(USER_NAME, answer.getQuestion().getUser().getName());
            templateEmailModel.put(USER_EMAIL, answer.getQuestion().getUser().getEmail());


            sendEmail();
        } else if (object instanceof Comment) {
            Comment comment = (Comment) object;

            if (comment.getAnswer() != null) {
                Answer answer = comment.getAnswer();
                templateEmailModel.put(EMAIL_TEXT_INITIAL, "A sua resposta na pergunta ");
                templateEmailModel.put(QUESTION_TITLE, answer.getQuestion().getTitle());
                templateEmailModel.put(EMAIL_TEXT_END, " tem um novo comentário.");
                templateEmailModel.put(EMAIL_SUBJECT,"TownSQ - Alguém comentou sua resposta");
                templateEmailModel.put(USER_NAME, answer.getUser().getName());
                templateEmailModel.put(USER_EMAIL, answer.getUser().getEmail());
                templateEmailModel.put(QUESTION_URL, urlQuestion  + answer.getQuestion().getId());
            } else if (comment.getQuestion() != null) {
                Question question = comment.getQuestion();
                templateEmailModel.put(EMAIL_TEXT_INITIAL, "A sua pergunta " );
                templateEmailModel.put(EMAIL_TEXT_END, " tem um novo comentário.");
                templateEmailModel.put(QUESTION_TITLE, question.getTitle());
                templateEmailModel.put(EMAIL_SUBJECT,"TownSQ - Alguém comentou a sua pergunta");
                templateEmailModel.put(USER_NAME, question.getUser().getName());
                templateEmailModel.put(USER_EMAIL, question.getUser().getEmail());
                templateEmailModel.put(QUESTION_URL, urlQuestion + question.getId());
            }
            sendEmail();
        }
    }
}
