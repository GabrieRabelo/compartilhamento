package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

	@DisplayName("Salva uma pergunta no repositorio e deve retornar a mesma")
	@Test
	void testSaveQuestion() {
		Question question = new Question(1L, "Olá", "essa fera ai meu", 1, new Timestamp(1), new Timestamp(1), null, null, 1);
		User user = new User(1L, "Rabelo", "rabelo", "rabelo@rab.elo", 1, null, null, null, null, null, null, null);

		when(questionRepository.save(any(Question.class)))
				.thenReturn(question);

		var result = questionService.save(question, user);

		assertEquals("Olá", result.getTitle());
	}

	@DisplayName("Deve retornar uma lista de perguntas do repositorio")
	@Test
	void testGetIndexQuestions() {
		List<Question> questionList = new ArrayList<>();
		for(long i = 0; i<10; i++){
			Question question = new Question(i, "Olá", "essa fera ai meu", 1, new Timestamp(i), new Timestamp(i), null, null, 1);
			questionList.add(question);
		}

		when(questionRepository.findTop10ByStatusEqualsOrderByCreatedAtDesc(1))
				.thenReturn(questionList);

		var result = questionService.getIndexQuestions();

		assertEquals(10, result.size());
	}

	@DisplayName("Deve realizar um soft delete de uma pergunta")
	@Test
	void testDeleteQuestionOfUser() {
		User user = new User(1L, "Rabelo", "rabelo", "rabelo@rab.elo", 1, null, null, null, null, null, null, null);
		Question question = new Question(1L, "Olá", "essa fera ai meu", 1, new Timestamp(1L), new Timestamp(1L), user, null, 1);

		when(questionRepository.findById(1L))
				.thenReturn(Optional.of(question));

		when(questionRepository.save(question))
				.thenReturn(question);

		boolean deleted = questionService.deleteQuestionOfUser(1L, 1L);

		assertTrue(deleted);
	}
}