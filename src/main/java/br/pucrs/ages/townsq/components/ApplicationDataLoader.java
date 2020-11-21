package br.pucrs.ages.townsq.components;

import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.RoleRepository;
import br.pucrs.ages.townsq.repository.TopicRepository;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
@Profile("!test")
public class ApplicationDataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Bean
    @Transactional
    public ApplicationRunner dataInitializer(TopicRepository topicRepo){
        return args -> {

            createRoles();

            if(topicRepo.count() == 0){
                topicRepo.saveAll(Arrays.asList(
                        new Topic().toBuilder().name("Legislação").build(),
                        new Topic().toBuilder().name("Manutenção").build(),
                        new Topic().toBuilder().name("Administração").build(),
                        new Topic().toBuilder().name("Segurança").build(),
                        new Topic().toBuilder().name("Portaria").build(),
                        new Topic().toBuilder().name("Financeiro").build(),
                        new Topic().toBuilder().name("Comunicação").build(),
                        new Topic().toBuilder().name("Síndico Profissional").build(),
                        new Topic().toBuilder().name("Obras e Reformas").build(),
                        new Topic().toBuilder().name("Assembleia").build(),
                        new Topic().toBuilder().name("Outros").build()
                ));
            }
        };
    }

    public void createRoles() {
        Role userRole = createRole("ROLE_USER");
        Role adminRole = createRole("ROLE_ADMIN");
        Role moderatorRole = createRole("ROLE_MODERATOR");
        if(userRole != null && adminRole != null && moderatorRole != null){
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            createUser(admin);
        }
    }


    /**
     * Creates a role if it is not present in the database
     * @param role The role's name
     * @return The new Role or null
     */
    @Transactional
    public Role createRole(String role){
        Role r = roleRepository.findByName(role).orElse(null);
        if (r == null) {
            r = new Role(role);
            roleRepository.save(r);
        }
        return r;
    }

    /**
     * Creates a new User if it is not present in the database
     * @param u The User Object
     */
    @Transactional
    public void createUser(User u){
        User nUser = userRepository.findByEmail(u.getEmail()).orElse(null);
        if(nUser == null){
            userRepository.save(u);
        }
    }

}
