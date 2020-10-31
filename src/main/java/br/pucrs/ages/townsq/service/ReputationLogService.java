package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.ReputationLog;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.ReputationLogRepository;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReputationLogService {

    private ReputationLogRepository reputationRepository;
    private UserRepository userRepository;

    @Autowired
    public ReputationLogService(ReputationLogRepository reputationRepository, UserRepository userRepo){
        this.reputationRepository = reputationRepository;
        this.userRepository = userRepo;
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

        return reputationRepository.save(toPersist);
    }

    public void createUserProfileLog(User user){
        ReputationLog toPersist = ReputationLog.builder()
                .eventType("COMPLETED_PROFILE")
                .points(20)
                .isActive(1)
                .toUser(user)
                .fromUser(user)
                .build();
        reputationRepository.save(toPersist);
    }

    public void favoriteBestAnswer(Answer answer){

        ReputationLog toPersist = ReputationLog.builder()
                .eventType("BEST_ANSWER")
                .points(50)
                .isActive(1)
                .question(answer.getQuestion())
                .toUser(answer.getUser())
                .fromUser(answer.getQuestion().getUser())
                .build();

        reputationRepository.save(toPersist);
    }

}
