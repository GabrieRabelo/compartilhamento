package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.ReputationLog;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.ReputationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReputationLogService {

    private ReputationLogRepository repository;

    @Autowired
    public ReputationLogService(ReputationLogRepository repo){
        this.repository = repo;
    }

    public ReputationLog createdQuestionLog(Question question){

        ReputationLog toPersist = ReputationLog.builder()
                .eventType("CREATED_QUESTION")
                .points(10)
                .isActive(1)
                .question(question)
                .toUser(question.getUser())
                .fromUser(question.getUser())
                .build();

        return repository.save(toPersist);
    }

}
