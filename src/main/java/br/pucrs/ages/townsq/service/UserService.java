package br.pucrs.ages.townsq.service;

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

    @Autowired
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder, RoleRepository roleRepo){
        bcPasswordEncoder = encoder;
        repository = repo;
        roleRepository = roleRepo;
    }

    public User save(User u){
        if (u.getPassword() == null || u.getPassword().isEmpty() || u.getPassword().isBlank() ) throw new IllegalArgumentException("A senha é obrigatória.");
        u.setPassword(bcPasswordEncoder.encode(u.getPassword()));
        roleRepository.findByName("ROLE_USER").ifPresent(userRole -> u.setRoles(new HashSet<>(Collections.singletonList(userRole))));
        return repository.save(u);
    }

    public User update(User u, String authEmail) throws MalformedURLException {
        User user = findByEmail(authEmail).orElse(null);
        if(user != null){
            user.setName(u.getName());
            user.setBio(u.getBio());
            user.setCompany(u.getCompany());
            if(!StringUtils.isEmpty(u.getWebsite())){
                new URL(u.getWebsite());
            }
            user.setWebsite(u.getWebsite());
            if (u.getImage() != null && !u.getImage().equals(user.getImage()))
                user.setImage(u.getImage());
            if(!StringUtils.isEmpty(u.getPassword())){
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
