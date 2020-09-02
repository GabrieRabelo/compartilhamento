package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository repository;
    private final BCryptPasswordEncoder bcPasswordEncoder;

    @Autowired
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder){
        bcPasswordEncoder = encoder;
        repository = repo;
    }

    public User save(User u){
        u.setPassword(bcPasswordEncoder.encode(u.getPassword()));
        return repository.save(u);
    }

    public List<User> findAll(){
        return (List<User>) repository.findAll();
    }

    public Optional<User> findById(long id){
        return repository.findById(id);
    }

}
