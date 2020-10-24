package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnswerServiceTest {

	private AnswerService answerService;
	private AnswerRepository answerRepository;

	@BeforeEach
	void setUp() {
		this.answerRepository = mock(AnswerRepository.class);
		this.answerService = new AnswerService(answerRepository);
	}

	@DisplayName("Test save answer should return saved answer")
	@Test
	public void testSaveAnswer() {
		User user = User.builder().name("Juca").password("12345").email("juca@email.com").build();
		Question question = Question.builder().id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
		Answer answer = Answer.builder().id(1L).text("Text").isActive(1).isBest(1).build();

		when(answerRepository.save(any(Answer.class)))
				.thenReturn(answer);

		var result = answerService.saveAnswer(answer, user, question);

		assertEquals(result.getText(), answer.getText());
		assertEquals(result.getUser().getId(), user.getId());
		assertEquals(result.getQuestion().getId(), question.getId());
	}

	@Test
	public void testSaveAnswerShouldThrowIllegalArgumentException() {
		User user = User.builder().name("Juca").password("12345").email("juca@email.com").build();
		Question question = Question.builder().id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
		Answer answer = Answer.builder().id(1L).text("  ").isActive(1).isBest(1).build();

		assertThrows(IllegalArgumentException.class, () -> answerService.saveAnswer(answer, user, question));
	}

	@DisplayName("A moderator should be able to delete a question from another user")
	@Test
	public void testDeleteAnswerAsMod(){
		User user = User.builder().id((long) 1).name("Juca").password("12345").email("juca@email.com").build();
		User mod = User.builder()
				.id((long) 3)
				.name("Mod")
				.password("12345")
				.email("mod@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_MODERATOR"))))
				.build();
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();

		when(answerRepository.save(any(Answer.class)))
				.thenReturn(answer);
		when(answerRepository.findById(anyLong()))
				.thenReturn(java.util.Optional.ofNullable(answer));

		assertTrue(answerService.delete(mod, 1L));
	}

	@DisplayName("A regular user can only delete it's answers")
	@Test
	public void testDeleteAnswerAsInvalidUser(){
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		User invalid = User.builder()
				.id((long) 3)
				.name("Invalid")
				.password("12345")
				.email("ivalid@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();

		when(answerRepository.save(any(Answer.class)))
				.thenReturn(answer);
		when(answerRepository.findById(anyLong()))
				.thenReturn(java.util.Optional.ofNullable(answer));

		assertFalse(answerService.delete(invalid, 1L));
	}

	@DisplayName("A moderator should be able to edit a question from another user")
	@Test
	public void testEditAnswerAsMod(){
		User user = User.builder().id((long) 1).name("Juca").password("12345").email("juca@email.com").build();
		User mod = User.builder()
				.id((long) 3)
				.name("Mod")
				.password("12345")
				.email("mod@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_MODERATOR"))))
				.build();
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();

		when(answerRepository.save(any(Answer.class)))
				.thenReturn(answer);
		when(answerRepository.findById(anyLong()))
				.thenReturn(java.util.Optional.ofNullable(answer));

		assertDoesNotThrow(() -> answerService.editAnswer("New answer text", mod, 1L));
	}

	@DisplayName("A regular user can only edit it's answers")
	@Test
	public void testEditAnswerAsInvalidUser(){
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		User invalid = User.builder()
				.id((long) 3)
				.name("Invalid")
				.password("12345")
				.email("ivalid@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();

		when(answerRepository.save(any(Answer.class)))
				.thenReturn(answer);
		when(answerRepository.findById(anyLong()))
				.thenReturn(java.util.Optional.ofNullable(answer));

		assertThrows(IllegalArgumentException.class, () -> answerService.editAnswer("New text", invalid, 1L));
	}
}
