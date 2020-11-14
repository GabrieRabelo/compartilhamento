package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.model.VoteLog;
import br.pucrs.ages.townsq.repository.VoteLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private QuestionService questionService;
    private VoteLogRepository voteLogRepository;

    @Autowired
    public VoteService(QuestionService questionService, VoteLogRepository voteLogRepository) {
        this.questionService = questionService;
        this.voteLogRepository = voteLogRepository;
    }

    public VoteLog upVote(String type, long id, User user) {
        if (type.equals("question")) {
            Question question = questionService.getNonDeletedQuestionById(id).orElse(null);
            if (question == null) {
                throw new IllegalArgumentException("ID inválido");
            }
            VoteLog vote = voteLogRepository.getVoteLogByQuestionAndUser(question, user).orElse(null);
            if (vote == null) {
                VoteLog toSave = VoteLog.builder()
                        .user(user)
                        .question(question)
                        .eventType("UPVOTE")
                        .score(1)
                        .build();
                return voteLogRepository.save(toSave);
            }
        }
      return null;
    }

}
