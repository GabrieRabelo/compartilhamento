package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.*;
import br.pucrs.ages.townsq.repository.VoteLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final VoteLogRepository voteLogRepository;
    private final ReputationLogService reputationLogService;

    @Autowired
    public VoteService(QuestionService questionService, VoteLogRepository voteLogRepository, AnswerService answerService, ReputationLogService reputationLogService) {
        this.questionService = questionService;
        this.voteLogRepository = voteLogRepository;
        this.answerService = answerService;
        this.reputationLogService = reputationLogService;
    }

    public int upVote(String type, long id, User user) {
        if (type.equals("question")) {
            Question question = questionService.getNonDeletedQuestionById(id).orElse(null);
            if (question == null || question.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Operação inválida");
            }
            VoteLog vote = voteLogRepository.getVoteLogByQuestionAndUser(question, user).orElse(null);
            if (vote == null) {
                VoteLog log = voteLogRepository.save(_buildVoteLog("UPVOTE", 1, user, question, null));
                reputationLogService.voteLog(log, 1, question.getUser());
                return 1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("DOWNVOTE")){
                    VoteLog log = voteLogRepository.save(_buildVoteLog("UPVOTE", 1, user, question, null));
                    reputationLogService.voteLog(log, 2, question.getUser());
                    return 2;
                }
                reputationLogService.voteLog(vote, -1, question.getUser());
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
                VoteLog log = voteLogRepository.save(_buildVoteLog("UPVOTE", 1, user, null, answer));
                reputationLogService.voteLog(log, 1, answer.getUser());
                return 1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("DOWNVOTE")){
                    VoteLog log = voteLogRepository.save(_buildVoteLog("UPVOTE", 1, user, null, answer));
                    reputationLogService.voteLog(log, 1, answer.getUser());
                    return 2;
                }
                reputationLogService.voteLog(vote, -1, answer.getUser());
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
                VoteLog log = voteLogRepository.save(_buildVoteLog("DOWNVOTE", -1, user, question, null));
                reputationLogService.voteLog(log, -1, question.getUser());
                return -1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("UPVOTE")){
                    VoteLog log = voteLogRepository.save(_buildVoteLog("DOWNVOTE", -1, user, question, null));
                    reputationLogService.voteLog(log, -2, question.getUser());
                    return -2;
                }
                reputationLogService.voteLog(vote, 1, question.getUser());
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
                VoteLog log = voteLogRepository.save(_buildVoteLog("DOWNVOTE", -1, user, null, answer));
                reputationLogService.voteLog(log, -1, answer.getUser());
                return -1;
            }
            else {
                voteLogRepository.delete(vote);
                if(vote.getEventType().equals("UPVOTE")){
                    VoteLog log = voteLogRepository.save(_buildVoteLog("DOWNVOTE", -1, user, null, answer));
                    reputationLogService.voteLog(log, -2, answer.getUser());
                    return -2;
                }
                reputationLogService.voteLog(vote, 1, answer.getUser());
                return 1;
            }
        }
        return 0;
    }

    /**
     * Builds a VoteLog object
     * @param eventType String
     * @param score int
     * @param user User
     * @param question Question
     * @param answer Answer
     * @return VoteLog
     */
    private VoteLog _buildVoteLog(String eventType, int score, User user, Question question, Answer answer){
        return VoteLog.builder()
                .user(user)
                .eventType(eventType)
                .score(score)
                .question(question)
                .answer(answer)
                .isActive(1)
                .build();
    }

}
