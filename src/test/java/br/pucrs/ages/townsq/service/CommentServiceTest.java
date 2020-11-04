package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.*;
import br.pucrs.ages.townsq.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

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

    @DisplayName("Salva um comentário, criado a partir de uma resposta")
    @Test
    void testSaveCommentWithException() {
        User user = User.builder().name("Juca").password("12345").email("juca@email.com").build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Answer answer = Answer.builder().text("Opa, é isso!").question(question).user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text("").build();

        when(commentRepository.save(comment)).thenReturn(comment);
        when(answerService.findById(anyLong())).thenReturn(java.util.Optional.of(answer));

        assertThrows(IllegalArgumentException.class, () -> commentService.saveComment(comment, user, "anser", 1L));
    }

    @DisplayName("O criador do comentário pode realizar sua deleção")
    @Test
    void testDeleteComment() {
        User user = User.builder().id((long) 6).name("Juca").password("12345").email("juca@email.com").build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).isActive(1).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));

        assertTrue(commentService.deleteComment(1L, user));
    }

    @DisplayName("Apenas o criador do comentário pode realizar sua deleção, se não for moderador.")
    @Test
    void testDeleteCommentAsInvalidUser() {
        User user = User.builder().id((long) 6).name("Juca").password("12345").email("juca@email.com").build();
        User invalidUser = User.builder().id((long) 7).name("Fulano").password("12345").email("fulano@email.com")
                .roles(new HashSet<>()).build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).isActive(1).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));

        assertFalse(commentService.deleteComment(1L, invalidUser));
    }

    @DisplayName("Um moderador pode deletar um comentário de outro usuário.")
    @Test
    void testDeleteCommentAsMod() {
        User user = User.builder().id((long) 8).name("Juca").password("12345").email("juca@email.com").build();
        User mod = User.builder().id((long) 7).name("Fulano").password("12345").email("fulano@email.com")
                .roles(new HashSet<>(Collections.singleton(new Role("ROLE_MODERATOR")))).build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).isActive(1).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));

        assertTrue(commentService.deleteComment(1L, mod));
    }

    @DisplayName("Teste de validações para editar um comentário como criador.")
    @Test
    void testEditComment() {
        User user = User.builder().id((long) 6).name("Juca").password("12345").email("juca@email.com").build();
        User invalidUser = User.builder().id((long) 7).name("Fulano").password("12345").email("fulano@email.com").build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).isActive(1).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertNotNull(commentService.editComment("Meu texto novo", user, 1L));
    }

    @DisplayName("Um moderador pode editar o comentário de outro usuário.")
    @Test
    void testEditCommentAsMod() {
        User user = User.builder().id((long) 8).name("Juca").password("12345").email("juca@email.com").build();
        User mod = User.builder().id((long) 7).name("Fulano").password("12345").email("fulano@email.com")
                .roles(new HashSet<>(Collections.singleton(new Role("ROLE_MODERATOR")))).build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).isActive(1).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertNotNull(commentService.editComment("Meu texto novo", mod, 1L));
    }

    @DisplayName("Apenas o criador do comentário pode editá-lo, a menos que seja um moderador.")
    @Test
    void testEditCommentAsInvalidUser() {
        User user = User.builder().id((long) 8).name("Juca").password("12345").email("juca@email.com").build();
        User invalidUser = User.builder().id((long) 7).name("Fulano").password("12345").email("fulano@email.com")
                .roles(new HashSet<>()).build();
        Question question = Question.builder().title("Olá, isso é uma pergunta.").description("Essa fera ai meu!").user(user).build();
        Comment comment = Comment.builder().question(question).user(user).text(null).isActive(1).build();

        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertThrows(IllegalArgumentException.class, () -> commentService.editComment("Meu texto novo", invalidUser, 1L));
    }

}
