package br.pucrs.ages.townsq.security;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class Authentication implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public Authentication(UserRepository repo){
        repository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = repository.findByEmail(email);
        if(user.isPresent()) return user.get();
        else throw new UsernameNotFoundException("Usuário não encontrado.");
    }
}
