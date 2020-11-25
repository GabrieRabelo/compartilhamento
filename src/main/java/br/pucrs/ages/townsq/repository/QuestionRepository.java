package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	List<Question> findTop10ByStatusEqualsOrderByCreatedAtDesc(int status);
	List<Question> findTop10ByStatusEqualsAndTopicInOrderByCreatedAtDesc(int status, Collection<Topic> topics);
	Optional<Question> findByIdEqualsAndStatusEquals(long id, int status);
}