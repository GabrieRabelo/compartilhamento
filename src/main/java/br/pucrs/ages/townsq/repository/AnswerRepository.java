package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByIsActiveAndQuestionEqualsOrderByScoreDescCreatedAtDesc(int isActive, Question question);
}
