package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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

    public User update(User u, String authEmail){
        User user = findByEmail(authEmail).orElse(null);
        if(user != null){
            user.setName(u.getName());
            user.setBio(u.getBio());
            if(!u.getImage().equals("")){
                user.setImage(u.getImage());
            }
            /*user.setEmail(u.getEmail());*/
            if(!u.getPassword().equals("")){
                user.setPassword(bcPasswordEncoder.encode(u.getPassword()));
            }
            repository.save(user);
        }
        return user;
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public Optional<User> findById(long id){
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return repository.findByEmail(email);
    }

}
