package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestionServiceTest {

	private QuestionService questionService;
	private QuestionRepository questionRepository;

	@BeforeEach
	void setUp() {
		questionRepository = mock(QuestionRepository.class);
		questionService = new QuestionService(questionRepository);
	}

	@Test
	void save() {
		Question question = new Question(1L, "Olá", "essa fera ai meu", 1, new Timestamp(1), new Timestamp(1), null, null, 1);
		User user = new User(1L, "Rabelo", "rabelo", "rabelo@rab.elo", 1, null, null, null, null, null, null, null);

		when(questionRepository.save(any(Question.class)))
				.thenReturn(question);

		var result = questionService.save(question, user);

		assertEquals("Olá", result.getTitle());
	}
}