package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.RoleRepository;
import br.pucrs.ages.townsq.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {

    private UserService service;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ReputationLogService reputationLogService;
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp(){
        userRepository = mock(UserRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

        this.service = new UserService(userRepository, bCryptPasswordEncoder, roleRepository, reputationLogService);
    }

    /*
    @Test
    void testCreateAndSaveUser() {
        User testUser = new User();
        testUser.setName("fulano silva");
        testUser.setPassword("1234qwe");
        testUser.setEmail("emails@email.com");

        when(userRepository.save(testUser))
                .thenReturn(testUser);

        when(bCryptPasswordEncoder.encode(anyString()))
                .thenReturn("cr1pt0gr4f4d0");

        var result = service.save(testUser);

        assertEquals("fulano silva", result.getName());
    }
     */

}
