package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        if (u.getPassword() == null || u.getPassword().isEmpty() || u.getPassword().isBlank() ) throw new IllegalArgumentException("A senha é obrigatória.");
        u.setPassword(bcPasswordEncoder.encode(u.getPassword()));
        return repository.save(u);
    }

    public User update(User u, String authEmail){
        User user = repository.findByEmail(authEmail).orElse(null);
        if(user != null){
            user.setName(u.getName());
            user.setBio(u.getBio());
            user.setCompany(u.getCompany());
            user.setWebsite(u.getWebsite());
            if(!StringUtils.isEmpty(u.getPassword())){
                user.setPassword(bcPasswordEncoder.encode(u.getPassword()));
            }
            repository.save(user);
        }
        return user;
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public Optional<User> getUserById(long id){
        return repository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){
        return repository.findByEmail(email);
    }

}
