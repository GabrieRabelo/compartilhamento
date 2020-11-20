package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.model.VoteLog;
import br.pucrs.ages.townsq.repository.VoteLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final VoteLogRepository voteLogRepository;

    @Autowired
    public VoteService(QuestionService questionService, VoteLogRepository voteLogRepository, AnswerService answerService) {
        this.questionService = questionService;
        this.voteLogRepository = voteLogRepository;
        this.answerService = answerService;
    }

    public int upVote(String type, long id, User user) {
        if (type.equals("question")) {
            Question question = questionService.getNonDeletedQuestionById(id).orElse(null);
            if (question == null || question.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Operação inválida");
            }
            VoteLog vote = voteLogRepository.getVoteLogByQuestionAndUser(question, user).orElse(null);
            if (vote == null) {
                VoteLog toSave = VoteLog.builder()
                        .user(user)
                        .question(question)
                        .eventType("UPVOTE")
                        .score(1)
                        .isActive(1)
                        .build();
                voteLogRepository.save(toSave);
                return 1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("DOWNVOTE")){
                    VoteLog toSave = VoteLog.builder()
                            .user(user)
                            .question(question)
                            .eventType("UPVOTE")
                            .score(1)
                            .isActive(1)
                            .build();
                    voteLogRepository.save(toSave);
                    return 2;
                }
                return -1;
            }
        }
        if (type.equals("answer")) {
            Answer answer = answerService.findById(id).orElse(null);
            if (answer == null || answer.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Operação inválida");
            }
            VoteLog vote = voteLogRepository.getVoteLogByAnswerAndUser(answer, user).orElse(null);
            if (vote == null) {
                VoteLog toSave = VoteLog.builder()
                        .user(user)
                        .answer(answer)
                        .eventType("UPVOTE")
                        .score(1)
                        .isActive(1)
                        .build();
                voteLogRepository.save(toSave);
                return 1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("DOWNVOTE")){
                    VoteLog toSave = VoteLog.builder()
                            .user(user)
                            .answer(answer)
                            .eventType("UPVOTE")
                            .score(1)
                            .isActive(1)
                            .build();
                    voteLogRepository.save(toSave);
                    return 2;
                }
                return -1;
            }
        }
        return 0;
    }

    public int downVote(String type, long id, User user) {
        if (type.equals("question")) {
            Question question = questionService.getNonDeletedQuestionById(id).orElse(null);
            if (question == null || question.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Operação inválida");
            }
            VoteLog vote = voteLogRepository.getVoteLogByQuestionAndUser(question, user).orElse(null);
            if (vote == null) {
                VoteLog toSave = VoteLog.builder()
                        .user(user)
                        .question(question)
                        .eventType("DOWNVOTE")
                        .score(-1)
                        .isActive(1)
                        .build();
                voteLogRepository.save(toSave);
                return -1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("UPVOTE")){
                    VoteLog toSave = VoteLog.builder()
                            .user(user)
                            .question(question)
                            .eventType("DOWNVOTE")
                            .score(-1)
                            .isActive(1)
                            .build();
                    voteLogRepository.save(toSave);
                    return -2;
                }
                return 1;
            }
        }
        if (type.equals("answer")) {
            Answer answer = answerService.findById(id).orElse(null);
            if (answer == null || answer.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Operação inválida");
            }
            VoteLog vote = voteLogRepository.getVoteLogByAnswerAndUser(answer, user).orElse(null);
            if (vote == null) {
                VoteLog toSave = VoteLog.builder()
                        .user(user)
                        .answer(answer)
                        .eventType("DOWNVOTE")
                        .score(-1)
                        .isActive(1)
                        .build();
                voteLogRepository.save(toSave);
                return -1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("UPVOTE")){
                    VoteLog toSave = VoteLog.builder()
                            .user(user)
                            .answer(answer)
                            .eventType("DOWNVOTE")
                            .score(-1)
                            .isActive(1)
                            .build();
                    voteLogRepository.save(toSave);
                    return -2;
                }
                return 1;
            }
        }
        return 0;
    }

}
