package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bcPasswordEncoder;
    private final ReputationLogService reputationLogService;

    @Autowired
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder, ReputationLogService repService){
        bcPasswordEncoder = encoder;
        repository = repo;
        reputationLogService = repService;
    }

    public User save(User u){
        if (u.getPassword() == null || u.getPassword().isEmpty() || u.getPassword().isBlank() ) throw new IllegalArgumentException("A senha é obrigatória.");
        u.setPassword(bcPasswordEncoder.encode(u.getPassword()));
        return repository.save(u);
    }

    public User update(User u, User editUser) throws MalformedURLException {
        if(editUser != null){
            editUser.setName(u.getName().isBlank() ? editUser.getName() : u.getName());
            editUser.setBio(u.getBio().isBlank() ? null : u.getBio());
            editUser.setCompany(u.getCompany().isBlank() ? null : u.getCompany());
            if(u.getWebsite().isBlank()){
                editUser.setWebsite(null);
            } else {
                new URL(u.getWebsite());
                editUser.setWebsite(u.getWebsite());
            }
            if (u.getImage() != null && !u.getImage().equals(editUser.getImage()))
                editUser.setImage(u.getImage());
            if(!StringUtils.isEmpty(u.getPassword())){
                editUser.setPassword(bcPasswordEncoder.encode(u.getPassword()));
            }
            if(!StringUtils.isEmpty(editUser.getBio()) && !StringUtils.isEmpty(editUser.getImage())
                    && editUser.getHasCompletedProfile() == 0){
                editUser.setHasCompletedProfile(1);
                reputationLogService.createUserProfileLog(editUser);
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
