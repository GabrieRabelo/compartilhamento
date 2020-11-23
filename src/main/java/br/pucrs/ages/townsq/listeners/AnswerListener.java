package br.pucrs.ages.townsq.listeners;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.service.ReputationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;

@Component
public class AnswerListener {

    @PersistenceContext
    private EntityManager entityManager;

    private ReputationLogService reputationService;

    @Autowired
    public AnswerListener(ReputationLogService service){
        this.reputationService = service;
    }

    public AnswerListener() {
    }

    @PostPersist
    public void updateAnswerCreatorScore(Answer answer) {
        this.reputationService.createdAnswerLog(answer);
    }

}
