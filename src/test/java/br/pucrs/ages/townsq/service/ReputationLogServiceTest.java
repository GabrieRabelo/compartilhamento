package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReputationLogServiceTest extends Base{

    @Autowired
    ReputationLogService reputationLogService;
    @Autowired
    QuestionService questionService;
    @Autowired
    AnswerService answerService;

    @DisplayName("Cria uma pergunta, verifica se ganhou a pontuação, e em seguida deleta a pergunta, removendo a pontuação.")
    @Test
    @Order(1)
    void testCreateAndDeleteQuestionPoints(){
        User dummyUser      = userService.getUserByEmail("dummy@emailtest.com").orElse(null);
        Topic dummyTopic    = topicRepository.findById((long) 1).orElse(null);

        assertNotNull(dummyUser);
        assertNotNull(dummyTopic);

        Question question   = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").topic(dummyTopic).build();
        questionService.save(question, dummyUser);

        dummyUser = userService.getUserByEmail("dummy@emailtest.com").orElse(null);

        assertEquals(ReputationPoints.CREATED_QUESTION.getValue(), Objects.requireNonNull(dummyUser).getScore());

        ReputationLog reputationLog = reputationLogService.createDeletedQuestionLog(question);

        dummyUser = userService.getUserByEmail("dummy@emailtest.com").orElse(null);

        assertEquals(ReputationEventType.DELETED_QUESTION.getValue(),
                reputationLog.getEventType());
        assertEquals(ReputationPoints.DELETED_QUESTION.getValue(),
                reputationLog.getPoints());
        assertEquals(0, Objects.requireNonNull(dummyUser).getScore());
    }

    @DisplayName("Verifica se o usuário ganha pontuação ao completar o perfil.")
    @Test
    @Order(2)
    void testCompletedProfilePoints(){
        User dummyUser      = userService.getUserByEmail("dummy@emailtest.com").orElse(null);

        assertNotNull(dummyUser);

        dummyUser.setHasCompletedProfile(1);

        ReputationLog reputationLog = reputationLogService.createUserProfileLog(dummyUser);

        dummyUser = userService.getUserByEmail("dummy@emailtest.com").orElse(null);

        assertEquals(ReputationEventType.COMPLETED_PROFILE.getValue(),
                reputationLog.getEventType());
        assertEquals(ReputationPoints.COMPLETED_PROFILE.getValue(),
                reputationLog.getPoints());
        assertEquals(20, Objects.requireNonNull(dummyUser).getScore());
    }

    @DisplayName("Verifica se o usuário ganha pontuação ao ter sua resposta marcada como melhor.")
    @Test
    @Order(3)
    void testAnswerSetAsBestPoints(){
        Topic dummyTopic            = topicRepository.findById((long) 1).orElse(null);
        User dummyUser              = userService.getUserByEmail("dummy@emailtest.com").orElse(null);
        User dummyUserAnswer        = userService.getUserByEmail("dummyanser@emailtest.com").orElse(null);

        assertNotNull(dummyUser);
        assertNotNull(dummyUserAnswer);
        assertNotNull(dummyTopic);

        Question question   = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").topic(dummyTopic).build();
        Answer answer       = Answer.builder().text("Olá, isso é uma pergunta.").isBest(1).build();

        questionService.save(question, dummyUser);
        answerService.saveAnswer(answer, dummyUserAnswer, question);

        reputationLogService.favoriteBestAnswer(answer);

        dummyUserAnswer = userService.getUserByEmail("dummyanser@emailtest.com").orElse(null);

        // Expects 60, since it's the result of (CREATED ANSWER + BEST ANSWER)
        assertEquals(60, Objects.requireNonNull(dummyUserAnswer).getScore());
    }

}
