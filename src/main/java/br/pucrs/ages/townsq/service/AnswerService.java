package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.AnswerRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final ReputationLogService reputationService;
    private final QuestionService questionService;

    public AnswerService(AnswerRepository answerRepository, ReputationLogService reputationService, QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.reputationService = reputationService;
        this.questionService = questionService;
    }

    /**
     * Saves an Answer on the database
     * @param answer Answer
     * @return Answer
     */
    public Answer saveAnswer(Answer answer, User user, Question question) {
        if(StringUtils.isEmpty(answer.getText().trim()))
            throw new IllegalArgumentException("O texto da resposta não pode estar vazio");
        if(question.getUser().getId().equals(user.getId()))
            throw new IllegalArgumentException("Você não pode responder a sua própria pergunta.");
        answer.setQuestion(question);
        answer.setUser(user);
        return this.answerRepository.save(answer);
    }

    public void editAnswer(String answer,
                           User user,
                           Long id){
        Answer databaseAnswer = answerRepository.findById(id).orElse(null);
        if((StringUtils.isEmpty(answer.trim())
                || databaseAnswer == null
                || (databaseAnswer.getQuestion().getIsActive() == 0 && user.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("ROLE_MODERATOR")))
                || user.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("ROLE_MODERATOR"))
                && !databaseAnswer.getUser().getId().equals(user.getId()))
        ){
            throw new IllegalArgumentException("Não foi possível editar a resposta.");
        }
        databaseAnswer.setText(answer);
        answerRepository.save(databaseAnswer);
    }

    /**
     * Performs a soft delete of a answer if the user is it's creator or a mod.
     * @param answerId long
     * @return boolean
     */
    public boolean delete(User user, long answerId) {
        Answer answer = answerRepository.findById(answerId).orElse(null);
        if(answer != null) {
            if(answer.getUser().getId().equals(user.getId())
                    && answer.getIsActive() == 1
                    && answer.getQuestion().getIsActive() == 1
                    || user.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ROLE_MODERATOR"))) {
                answer.setIsActive(0);
                if(answer.getIsBest() == 1){
                    reputationService.disfavorBestAnswer(answer);
                    answer.setIsBest(0);
                    questionService.openQuestion(answer.getQuestion());
                }
                answerRepository.save(answer);
                reputationService.deleteAnswerLog(answer);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Returns an answer if it exists, given it's id
     * @param id Answer id
     * @return Optional of Answer
     */
    public Optional<Answer> findById(long id){ return answerRepository.findById(id); }

    /**
     * Gets the answers to be displayed on Question screen
     * @return List of answers
     */
    public List<Answer> getQuestionAnswers(Question question) {
        return answerRepository.findByIsActiveAndQuestionEqualsOrderByCreatedAtDesc(1, question)
                .stream()
                .sorted(Comparator.comparing(Answer::getIsBest).reversed())
                .collect(Collectors.toList());
    }

    public void favoriteAnswer(User user,
                                 Long id,
                                 Question questionFrom) throws NotFoundException {
        if (!questionFrom.getUser().getId().equals(user.getId())){
            throw new SecurityException("Você não pode favoritar esta resposta.");
        }

        Answer databaseAnswer = answerRepository.findById(id).orElse(null);
        if(databaseAnswer == null){
            throw new NotFoundException("Não foi possivel concluir a operação.");
        }

        Answer favoritedAnswer = questionFrom.getFavoriteAnswer().orElse(null);
        if(favoritedAnswer != null){
            if (favoritedAnswer.getId().equals(id)) {
                throw new IllegalArgumentException("Não é possivel desfavoritar uma resposta.");
            }
            favoritedAnswer.setIsBest(0);
            reputationService.disfavorBestAnswer(favoritedAnswer);
            answerRepository.save(favoritedAnswer);
        }
        databaseAnswer.setIsBest(1);
        reputationService.favoriteBestAnswer(databaseAnswer);
        questionService.closeQuestion(questionFrom);
        answerRepository.save(databaseAnswer);
    }

    public void updateAnswerScore(Answer answer, int score){
        answer.setScore(answer.getScore() + score);
        answerRepository.save(answer);
    }
}
