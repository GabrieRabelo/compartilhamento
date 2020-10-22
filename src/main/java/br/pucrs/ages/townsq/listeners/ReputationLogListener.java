package br.pucrs.ages.townsq.listeners;

import br.pucrs.ages.townsq.model.ReputationLog;
import br.pucrs.ages.townsq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;

@Component
public class ReputationLogListener {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @PostPersist
    public void updateUserScore(ReputationLog repLog){
        userService.updateUserScore(repLog);
    }

}
