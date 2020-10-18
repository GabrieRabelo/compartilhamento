package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.RoleRepository;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bcPasswordEncoder;
    private final ReputationLogService reputationLogService;

    @Autowired
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder, RoleRepository roleRepo, ReputationLogService repService) {
        bcPasswordEncoder = encoder;
        repository = repo;
        roleRepository = roleRepo;
        reputationLogService = repService;
    }

    public User save(User u) {
        if (u.getPassword() == null || u.getPassword().isEmpty() || u.getPassword().isBlank())
            throw new IllegalArgumentException("A senha é obrigatória.");
        u.setPassword(bcPasswordEncoder.encode(u.getPassword()));
        roleRepository.findByName("ROLE_USER").ifPresent(userRole -> u.setRoles(new HashSet<>(Collections.singletonList(userRole))));
        return repository.save(u);
    }

    public User update(User u, User editUser) throws MalformedURLException {
        if (editUser != null) {
            editUser.setName(u.getName());
            editUser.setBio(u.getBio());
            editUser.setCompany(u.getCompany());
            if (!StringUtils.isEmpty(u.getWebsite())) {
                new URL(u.getWebsite());
            }
            editUser.setWebsite(u.getWebsite());
            if (u.getImage() != null && !u.getImage().equals(editUser.getImage()))
                editUser.setImage(u.getImage());
            if (!u.getPassword().isBlank()) {
                if (u.getNewPassword().equals(u.getConfirmNewPassword()) && !StringUtils.isEmpty(u.getNewPassword())) {
                    if (bcPasswordEncoder.matches(u.getPassword(), editUser.getPassword())) {
                        editUser.setPassword(bcPasswordEncoder.encode(u.getNewPassword()));
                    } else {
                        throw new IllegalArgumentException("Senha atual inválida!");
                    }
                } else {
                    throw new IllegalArgumentException("A nova senha e a confirmação devem ser iguais e não podem estar vazias!");
                }
            } else {
                if(u.getNewPassword().equals(u.getConfirmNewPassword()) && !StringUtils.isEmpty(u.getNewPassword())){
                    throw new IllegalArgumentException("Senha atual inválida!");
                }
            }

            if (!StringUtils.isEmpty(editUser.getBio()) && !StringUtils.isEmpty(editUser.getImage())
                    && editUser.getHasCompletedProfile() == 0) {
                editUser.setHasCompletedProfile(1);
                reputationLogService.createUserProfileLog(editUser);
            }
            repository.save(editUser);
        }
        return editUser;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return repository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User updateUserScore(User user, int score) {
        if (user == null || user.getId() == null) {
            return null;
        }
        user.setScore(user.getScore() + score);
        return repository.save(user);
    }

    public User updateUserToMod(User user) {
        if (user == null) {
            return null;
        }
        Role role = roleRepository.findByName("ROLE_MODERATOR").orElse(null);
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        return repository.save(user);
    }

    public User updateModToUser(User user) {
        if (user == null) {
            return null;
        }
        Role role = roleRepository.findByName("ROLE_USER").orElse(null);
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        return repository.save(user);
    }

    public List<User> getAllModerators() {
        Role role = roleRepository.findByName("ROLE_MODERATOR").orElse(null);
        return repository.findByRolesIn(new HashSet<>(Collections.singletonList(role)));
    }

}
