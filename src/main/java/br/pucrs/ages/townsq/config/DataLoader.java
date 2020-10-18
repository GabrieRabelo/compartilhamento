package br.pucrs.ages.townsq.config;

import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.RoleRepository;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean done = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * This method executes every time the Application runs or reloads.
     * Intended to be user for creating entries in the database.
     * @param event The application event that trigger this method
     */
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!done){
            Role userRole = createRole("ROLE_USER");
            Role adminRole = createRole("ROLE_ADMIN");
            if(userRole != null && adminRole != null){
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@admin.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
                createUser(admin);
                done = true;
            }
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
