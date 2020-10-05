package br.pucrs.ages.townsq.components;

import br.pucrs.ages.townsq.service.ReputationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class UserListener {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReputationLogService reputationService;

}
