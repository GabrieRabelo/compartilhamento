package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.ReputationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReputationLogRepository extends JpaRepository<ReputationLog, Long> {


}