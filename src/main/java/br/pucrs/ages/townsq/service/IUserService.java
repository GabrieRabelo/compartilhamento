package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User save(User u);
    List<User> findAll();
    Optional<User> findById(long id);

}
