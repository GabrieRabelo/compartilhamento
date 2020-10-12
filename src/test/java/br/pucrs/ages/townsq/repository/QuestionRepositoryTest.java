package br.pucrs.ages.townsq.repository;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User user = new User(null, "Rabelo", "rabelo", "rabelo@rab.elo", 1, null, null, null, null, null, null, null, 0);
		userRepository.save(user);
		Topic topic = new Topic(1L, "Seguranca", new Timestamp(1), new Timestamp(1), 1);
		topicRepository.save(topic);

		for(long i = 1; i<=15; i++){
			Question question = new Question(i, "Olá", "essa fera ai meu", 1, new Timestamp(i), new Timestamp(i), user, topic, 1, null);
			questionRepository.save(question);
		}
	}

	@DisplayName("Deve retornar as ultimas 10 perguntas criadas em ordem de criação.")
	@Test
	void findTop10ByStatusEqualsOrderByCreatedAtDesc() {

		var result = questionRepository.findTop10ByStatusEqualsOrderByCreatedAtDesc(1);

		assertEquals(15, result.get(0).getId());
		assertEquals(10, result.size());
	}
}