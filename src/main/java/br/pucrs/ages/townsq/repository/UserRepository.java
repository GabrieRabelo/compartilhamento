package br.pucrs.ages.townsq.repository;


import br.pucrs.ages.townsq.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
