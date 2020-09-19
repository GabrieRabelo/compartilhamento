package br.pucrs.ages.townsq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetIndexPage() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    public void testGetUsersPageAsAnon() throws Exception {
        this.mockMvc.perform(get("/users")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("http://localhost/signin"));
    }

    @Test
    @WithMockUser
    public void testGetUsersPageAsUser() throws Exception {
        this.mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(view().name("users"));
    }

    @Test
    public void testGetQuestionPage() throws Exception {
        this.mockMvc.perform(get("/question")).andExpect(status().isOk()).andExpect(view().name("question"));
    }

}
