package br.pucrs.ages.townsq;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.UserRepository;
import br.pucrs.ages.townsq.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(userRepository);
    }


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

}
