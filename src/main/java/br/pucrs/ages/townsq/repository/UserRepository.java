package br.pucrs.ages.townsq.repository;


import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    List<User> findByRolesIn(Collection<Role> role);
}
