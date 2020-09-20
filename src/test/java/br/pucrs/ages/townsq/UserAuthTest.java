package br.pucrs.ages.townsq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserAuthTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests the request to get the homepage of the application.
     * @throws Exception
     */
    @Test
    public void testGetIndexPage() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    /**
     * Tests a private route (only accessible if logged in) as an anonymous user.
     * @throws Exception
     */
    @Test
    public void testGetUsersPageAsAnon() throws Exception {
        this.mockMvc.perform(get("/users")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/signin"));
    }

    /**
     * Tests a private route (only accessible if logged in) as an logged in user.
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void testGetUsersPageAsUser() throws Exception {
        this.mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(view().name("users"));
    }

    /**
     * Tests the request to get the question page.
     * @throws Exception
     */
    @Test
    public void testGetQuestionPage() throws Exception {
        this.mockMvc.perform(get("/question")).andExpect(status().isOk()).andExpect(view().name("question"));
    }

}
