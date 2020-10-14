package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByStatus(int status);
}
