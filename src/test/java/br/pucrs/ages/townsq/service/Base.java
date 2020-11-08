package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.TopicRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class Base {

    @Autowired
    protected UserService userService;
    @Autowired
    protected TopicRepository topicRepository;

    static boolean done = false;

    @BeforeAll
    static void setup(@Autowired UserService userService, @Autowired TopicRepository topicRepository) {
        if (!done) {
            User dummyUser           = User.builder().name("Dummy User").password("12345").email("dummy@emailtest.com").build();
            Topic dummyTopic          = Topic.builder().name("Dummy").status(1).build();
            User dummyUserAnswer           = User.builder().name("Dummy User Answer").password("12345").email("dummyanser@emailtest.com").build();

            userService.save(dummyUser);
            userService.save(dummyUserAnswer);
            topicRepository.save(dummyTopic);
        }
    }

}
