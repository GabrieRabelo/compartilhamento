package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.model.VoteLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteLogRepository extends JpaRepository <VoteLog,Long> {
    Optional<VoteLog> getVoteLogByQuestionAndUser(Question question, User user);
    Optional<VoteLog> getVoteLogByAnswerAndUser(Answer answer, User user);
}
