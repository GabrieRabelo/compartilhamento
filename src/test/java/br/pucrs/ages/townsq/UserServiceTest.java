package br.pucrs.ages.townsq;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    void testCreateAndSaveUser() throws Exception {
        User testUser = new User();
        testUser.setName("fulano silva");
        testUser.setPassword("1234qwe");
        testUser.setEmail("email@email.com");
        assertEquals("fulano silva", service.save(testUser).getName());
    }

}
