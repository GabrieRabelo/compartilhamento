package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
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

    public User update(User u, User editUser) throws MalformedURLException {
        if(editUser != null){
            editUser.setName(u.getName());
            editUser.setBio(u.getBio());
            editUser.setCompany(u.getCompany());
            if(!StringUtils.isEmpty(u.getWebsite())){
                new URL(u.getWebsite());
            }
            editUser.setWebsite(u.getWebsite());
            if (u.getImage() != null && !u.getImage().equals(editUser.getImage()))
                editUser.setImage(u.getImage());
            if(!StringUtils.isEmpty(u.getPassword())){
                editUser.setPassword(bcPasswordEncoder.encode(u.getPassword()));
            }
            repository.save(editUser);
        }
        return editUser;
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

    public User updateUserScore(User user, int score){
        if(user == null || user.getId() == null){
            return null;
        }
        user.setScore(user.getScore() + score);
        return repository.save(user);
    }

}
