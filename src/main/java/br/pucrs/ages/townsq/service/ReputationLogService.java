package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.*;
import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.ReputationLog;
import br.pucrs.ages.townsq.model.User;
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

    /**
     * Creates a ReputationLog when user creates a question
     * @param question The question
     * @return ReputationLog
     */
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

    /**
     * Creates a ReputationLog when user completes it's profile
     * @param user User
     * @return ReputationLog
     */
    public ReputationLog createUserProfileLog(User user){
        if(user.getHasCompletedProfile() == 1){
            ReputationLog toPersist = ReputationLog.builder()
                    .eventType(ReputationEventType.COMPLETED_PROFILE.getValue())
                    .points(ReputationPoints.COMPLETED_PROFILE.getValue())
                    .isActive(1)
                    .toUser(user)
                    .fromUser(user)
                    .build();
            return reputationRepository.save(toPersist);
        }
        return null;
    }

    /**
     * Creates a ReputationLog when user deletes a question
     * @param question Question
     * @return ReputationLog
     */
    public ReputationLog createDeletedQuestionLog(Question question){
        if(question.getStatus() == 0){
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

    public void favoriteBestAnswer(Answer answer){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType(ReputationEventType.FAVORED_ANSWER.getValue())
                .points(ReputationPoints.FAVORED_ANSWER.getValue())
                .isActive(1)
                .question(answer.getQuestion())
                .toUser(answer.getUser())
                .fromUser(answer.getQuestion().getUser())
                .build();
        reputationRepository.save(toPersist);
    }

    public void disfavorBestAnswer(Answer answer){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType(ReputationEventType.UNFAVORED_ANSWER.getValue())
                .points(ReputationPoints.UNFAVORED_ANSWER.getValue())
                .isActive(1)
                .question(answer.getQuestion())
                .toUser(answer.getUser())
                .fromUser(answer.getQuestion().getUser())
                .build();
        reputationRepository.save(toPersist);
    }

    public void createdAnswerLog(Answer answer){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType(ReputationEventType.CREATED_ANSWER.getValue())
                .points(ReputationPoints.CREATED_ANSWER.getValue())
                .isActive(1)
                .question(answer.getQuestion())
                .toUser(answer.getUser())
                .fromUser(answer.getUser())
                .build();
        reputationRepository.save(toPersist);
    }

    public void deleteAnswerLog(Answer answer){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType(ReputationEventType.CREATED_ANSWER.getValue())
                .points(ReputationPoints.DELETED_ANSWER.getValue())
                .isActive(1)
                .question(answer.getQuestion())
                .toUser(answer.getUser())
                .fromUser(answer.getUser())
                .build();
        reputationRepository.save(toPersist);
    }

    public void voteLog(VoteLog vote, int points, User toUser){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType(vote.getEventType())
                .points(points)
                .isActive(1)
                .question(vote.getQuestion())
                .answer(vote.getAnswer())
                .toUser(toUser)
                .fromUser(vote.getUser())
                .build();
        reputationRepository.save(toPersist);
    }


}
