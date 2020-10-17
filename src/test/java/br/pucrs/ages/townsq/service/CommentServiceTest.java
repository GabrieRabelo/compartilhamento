package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentServiceTest {

    private CommentService commentService;
    private QuestionService questionService;
    private CommentRepository commentRepository;
    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        questionService = mock(QuestionService.class);
        answerService = mock(AnswerService.class);
        commentService = new CommentService(commentRepository, questionService, answerService);
    }

    @DisplayName("Salva um comentário, criado a partir de uma pergunta")
    @Test
    void testSaveCommentOfQuestion() {
        User user = User.builder().name("Juca").password("12345").email("juca@email.com").build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text("Just a text.").build();

        when(commentRepository.save(comment)).thenReturn(comment);
        when(questionService.getQuestionById(anyLong())).thenReturn(java.util.Optional.of(question));

        var result = commentService.saveComment(comment, user, "question", 1L);

        assertEquals("Just a text.", result.getText());
    }

    @DisplayName("Salva um comentário, criado a partir de uma resposta")
    @Test
    void testSaveCommentOfAnswer() {
        User user = User.builder().name("Juca").password("12345").email("juca@email.com").build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Answer answer = Answer.builder().text("Opa, é isso!").question(question).user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text("From an answer.").build();

        when(commentRepository.save(comment)).thenReturn(comment);
        when(answerService.findById(anyLong())).thenReturn(java.util.Optional.of(answer));

        var result = commentService.saveComment(comment, user, "answer", 1L);

        assertEquals("From an answer.", result.getText());
    }

    @DisplayName("Teste de validações para deletar um comentário")
    @Test
    void testDeleteComment() {
        User user = User.builder().id((long) 6).name("Juca").password("12345").email("juca@email.com").build();
        User invalidUser = User.builder().id((long) 7).name("Fulano").password("12345").email("fulano@email.com").build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));

        assertFalse(commentService.deleteComment(1L, invalidUser));
    }

}
