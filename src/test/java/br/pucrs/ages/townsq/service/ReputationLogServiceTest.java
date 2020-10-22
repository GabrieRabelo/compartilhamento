package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReputationLogServiceTest extends Base{

    @Autowired
    ReputationLogService reputationLogService;
    @Autowired
    QuestionService questionService;

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

        assertEquals(10, dummyUser.getScore());

        ReputationLog reputationLog = reputationLogService.createDeletedQuestionLog(question);

        assertEquals(ReputationEventType.DELETED_QUESTION.getValue(),
                reputationLog.getEventType());
        assertEquals(ReputationPoints.DELETED_QUESTION.getValue(),
                reputationLog.getPoints());
        assertEquals(0, dummyUser.getScore());
    }

    @DisplayName("Verifica se o usuário ganha pontuação ao completar o perfil.")
    @Test
    @Order(2)
    void testCompletedProfilePoints(){
        User dummyUser      = userService.getUserByEmail("dummy@emailtest.com").orElse(null);

        assertNotNull(dummyUser);

        dummyUser.setHasCompletedProfile(1);

        ReputationLog reputationLog = reputationLogService.createUserProfileLog(dummyUser);

        assertEquals(ReputationEventType.COMPLETED_PROFILE.getValue(),
                reputationLog.getEventType());
        assertEquals(ReputationPoints.COMPLETED_PROFILE.getValue(),
                reputationLog.getPoints());
        assertEquals(20, dummyUser.getScore());
    }

}
