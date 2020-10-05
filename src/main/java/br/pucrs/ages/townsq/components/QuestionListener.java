package br.pucrs.ages.townsq.components;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.service.ReputationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;

@Component
public class QuestionListener {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReputationLogService reputationService;

    @PostPersist
    public void updateQuestionCreatorScore(Question question){
        reputationService.createdQuestionLog(question);
    }

}