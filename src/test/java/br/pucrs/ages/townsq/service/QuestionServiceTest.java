package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
		Question question = Question.builder().id(1L).title("Olá").description("Essa fera aí meu").createdAt(new Timestamp(1)).updatedAt(new Timestamp(1)).build();
		User user = User.builder().id(1L).name("Rabelo").password("rabelo").email("rabelo@rab.elo").build();

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
			Question question = Question.builder().id(1L).title("Olá").description("Essa fera aí meu").createdAt(new Timestamp(1)).updatedAt(new Timestamp(1)).build();
			questionList.add(question);
		}

		when(questionRepository.findTop10ByStatusEqualsOrderByCreatedAtDesc(1))
				.thenReturn(questionList);

		var result = questionService.getIndexQuestions();

		assertEquals(10, result.size());
	}

	@DisplayName("Deve realizar um soft delete de uma pergunta, com o usuário criador da pergunta.")
	@Test
	void testDeleteQuestionOfUser() {
		User user = User.builder().id((long) 1).name("Fulano").password("123321").email("fulano@teste.com").build();
		Question question = Question.builder().id(1L).title("Teste").description("Essa fera aí meu").user(user).createdAt(new Timestamp(1)).updatedAt(new Timestamp(1)).status(1).build();

		when(questionRepository.findById(1L))
				.thenReturn(Optional.of(question));

		when(questionRepository.save(question))
				.thenReturn(question);

		assertTrue(questionService.delete(user, 1L));
	}

	@DisplayName("Deve bloquear a deleção da pergunta se não for criador da pergunta e não for moderador.")
	@Test
	void testDeleteQuestionAsInvalidUser() {
		User user = User.builder().id((long) 1).name("Juca").password("123321").email("juca@email.com").roles(new HashSet<>()).build();
		User illegal = User.builder().id((long) 2).name("Illegal").password("332211").email("illegal@email.com").roles(new HashSet<>()).build();
		Question question = Question.builder().id(1L).title("Teste").description("Essa fera aí meu").user(user).createdAt(new Timestamp(1)).updatedAt(new Timestamp(1)).status(1).build();

		when(questionRepository.findById(1L))
				.thenReturn(Optional.of(question));

		when(questionRepository.save(question))
				.thenReturn(question);

		assertFalse(questionService.delete(illegal, 1L));
	}

	@DisplayName("Deve permitir a deleção da pergunta se não for criador da pergunta e for moderador.")
	@Test
	void testDeleteQuestionAsMod() {
		User user = User.builder().id((long) 1).name("Juca").password("123321").email("juca@email.com").roles(new HashSet<>()).build();
		User mod = User.builder().
				id((long) 2).
				name("Mod").
				password("332211").
				email("mod@email.com").
				roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_MODERATOR")))).build();

		Question question = Question.builder().id(1L).title("Teste").description("Essa fera aí meu").user(user).createdAt(new Timestamp(1)).updatedAt(new Timestamp(1)).status(1).build();

		when(questionRepository.findById(1L))
				.thenReturn(Optional.of(question));

		when(questionRepository.save(question))
				.thenReturn(question);

		assertTrue(questionService.delete(mod, 1L));
	}
}