package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnswerServiceTest {

	private AnswerService answerService;
	private AnswerRepository answerRepository;
	private ReputationLogService reputationService;

	@BeforeEach
	void setUp() {
		this.answerRepository = mock(AnswerRepository.class);
		this.reputationService = mock(ReputationLogService.class);
		this.answerService = new AnswerService(answerRepository, reputationService);
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
}
