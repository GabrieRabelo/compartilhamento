package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	List<Question> findTop10ByStatusEqualsOrderByCreatedAtDesc(int status);
	Optional<Question> findByIdEqualsAndStatusEquals(long id, int status);
}