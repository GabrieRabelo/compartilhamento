package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.AnswerRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AnswerServiceTest {

	private AnswerService answerService;
	private AnswerRepository answerRepository;
	private ReputationLogService reputationService;
	private QuestionService questionService;

	@BeforeEach
	void setUp() {
		this.answerRepository = mock(AnswerRepository.class);
		this.reputationService = mock(ReputationLogService.class);
		this.questionService = mock(QuestionService.class);
		this.answerService = new AnswerService(answerRepository, reputationService, questionService);
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
		Question question = Question.builder().id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).isActive(1).build();
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(0).user(user).question(question).build();

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
		Question question = Question.builder().isActive(1).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
		Answer answer = Answer.builder().question(question).id(1L).text("Something").isActive(1).isBest(1).user(user).build();

		when(answerRepository.save(any(Answer.class)))
				.thenReturn(answer);
		when(answerRepository.findById(anyLong()))
				.thenReturn(java.util.Optional.ofNullable(answer));

		assertThrows(IllegalArgumentException.class, () ->
				answerService.editAnswer("New text", invalid, 1L));
	}

	@Test
	public void testFavoriteAnswerShouldThrowSecurityException(){
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		User otherUser = User.builder()
				.id((long) 3)
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Long id = 1L;
		Question question = Question.builder().id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(otherUser).build();

		assertThrows(SecurityException.class, () -> answerService.favoriteAnswer(user, id, question));
	}

	@Test
	public void testFavoriteAnswerShouldThrowNotFoundException(){
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Long id = 1L;
		Question question = Question.builder().id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
		when(answerRepository.findById(anyLong()))
				.thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> answerService.favoriteAnswer(user, id, question));
	}

	@Test
	public void testFavoriteAnswerShouldThrowIllegalArgumentException() {
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Long id = 1L;
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();
		Question question = Question.builder().answers(Collections.singletonList(answer)).id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();

		when(answerRepository.findById(anyLong()))
				.thenReturn(Optional.ofNullable(answer));

		assertThrows(IllegalArgumentException.class, () -> answerService.favoriteAnswer(user, id, question));

	}

	@Test
	public void testFavoriteAnswerShouldFavoriteTheNewAnswer() throws NotFoundException {
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Long id = 3L;
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();
		Question question = Question.builder().answers(Collections.singletonList(answer)).id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();

		when(answerRepository.findById(anyLong()))
				.thenReturn(Optional.ofNullable(answer));

		answerService.favoriteAnswer(user, id, question);

		verify(answerRepository, times(2)).save(any(Answer.class));
	}

	@Test
	public void testFavoriteAnswerShouldFavoriteOneAnswer() throws NotFoundException {
		User user = User.builder()
				.id((long) 1)
				.name("Juca")
				.password("12345")
				.email("juca@email.com")
				.roles(new HashSet<>(Collections.singletonList(new Role(1L, "ROLE_USER"))))
				.build();
		Long id = 3L;
		Answer answer = Answer.builder().id(1L).text("Something").isActive(1).isBest(1).user(user).build();
		Question question = Question.builder().answers(new ArrayList<>()).id(1L).title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();

		when(answerRepository.findById(anyLong()))
				.thenReturn(Optional.ofNullable(answer));

		answerService.favoriteAnswer(user, id, question);

		verify(answerRepository, times(1)).save(any(Answer.class));
	}
}
