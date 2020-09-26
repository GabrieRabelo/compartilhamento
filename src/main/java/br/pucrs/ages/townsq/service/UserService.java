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
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
        bcPasswordEncoder = encoder;
        repository = repo;
    }

    public User save(User u) {
        if (u.getPassword() == null || u.getPassword().isEmpty() || u.getPassword().isBlank())
            throw new IllegalArgumentException("A senha é obrigatória.");
        u.setPassword(bcPasswordEncoder.encode(u.getPassword()));
        return repository.save(u);
    }

    public User update(User u, String authEmail) {
        User user = findByEmail(authEmail).orElse(null);
        if (user != null) {
            user.setName(u.getName());
            user.setBio(u.getBio());
            user.setCompany(u.getCompany());
            user.setWebsite(u.getWebsite());
            repository.save(user);
        }
        return user;
    }

    public User updatePassword(User u, String authEmail) {
        User user = findByEmail(authEmail).orElse(null);
        if (user != null) {
            if (u.getNewPassword().equals(u.getConfirmNewPassword()) && !StringUtils.isEmpty(u.getNewPassword())) {
                if (bcPasswordEncoder.matches(u.getPassword(), user.getPassword())) {
                    user.setPassword(bcPasswordEncoder.encode(u.getNewPassword()));
                } else {
                    throw new IllegalArgumentException("Senha atual inválida!");
                }
            } else {
                throw new IllegalArgumentException("A nova senha e a confirmação devem ser iguais!");
            }
            repository.save(user);
        }
        return user;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(long id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

}
