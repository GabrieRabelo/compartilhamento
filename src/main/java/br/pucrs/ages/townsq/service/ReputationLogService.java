package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.*;
import br.pucrs.ages.townsq.repository.ReputationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReputationLogService {

    private final ReputationLogRepository reputationRepository;

    @Autowired
    public ReputationLogService(ReputationLogRepository reputationRepository){
        this.reputationRepository = reputationRepository;
    }

    public ReputationLog createNewQuestionLog(Question question){

        ReputationLog toPersist = ReputationLog.builder()
                .eventType(ReputationEventType.CREATED_QUESTION.getValue())
                .points(ReputationPoints.CREATED_QUESTION.getValue())
                .isActive(1)
                .question(question)
                .toUser(question.getUser())
                .fromUser(question.getUser())
                .build();

        return reputationRepository.save(toPersist);
    }

    public ReputationLog createUserProfileLog(User user){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType(ReputationEventType.COMPLETED_PROFILE.getValue())
                .points(ReputationPoints.CREATED_QUESTION.getValue())
                .isActive(1)
                .toUser(user)
                .fromUser(user)
                .build();
        return reputationRepository.save(toPersist);
    }

    public ReputationLog createDeletedQuestionLog(Question question){
        if(question.getIsActive() == 0){
            ReputationLog creatorLog = ReputationLog.builder()
                    .eventType(ReputationEventType.DELETED_QUESTION.getValue())
                    .question(question)
                    .points(ReputationPoints.DELETED_QUESTION.getValue())
                    .isActive(1)
                    .toUser(question.getUser())
                    .fromUser(question.getUser())
                    .build();
            return reputationRepository.save(creatorLog);
        }
        return null;
    }

}
