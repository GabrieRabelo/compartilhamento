package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final ReputationLogService reputationLogService;

    @Autowired
    public QuestionService(QuestionRepository repository, ReputationLogService reputationLogService) {
        this.repository = repository;
        this.reputationLogService = reputationLogService;
    }

    /**
     * Saves a question with it's creator.
     * @param question The Question object
     * @param user The User (creator)
     * @return Question
     */
    public Question save(Question question, User user){
        question.setUser(user);
        return repository.save(question);
    }

    /**
     * Edits a question if it belongs to the user
     * @param question The question
     * @param user The user
     * @return Question
     * @throws Exception Invalid operation
     */
    public Question edit(Question question, User user) throws Exception{
        Question currentQuestion = repository.findById(question.getId()).orElse(null);

        if(currentQuestion == null || (!currentQuestion.getUser().getId().equals(user.getId()) &&
                user.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("ROLE_MODERATOR"))))
            throw new IllegalArgumentException("Usuário não pode editar a pergunta.");
        currentQuestion.setTitle(question.getTitle());
        currentQuestion.setTopic((question.getTopic()));
        currentQuestion.setDescription(question.getDescription());

        return repository.save(currentQuestion);
    }

    /**
     * Gets the questions to be displayed on the homepage.
     * @return List of questions
     */
    public List<Question> getIndexQuestions() {
        return repository.findTop10ByStatusEqualsOrderByCreatedAtDesc(1);
    }

    /**
     * Returns a question, if it exists, given it's id
     * @param id The question's id
     * @return Optional of Question
     */
    public Optional<Question> getQuestionById(long id) {
        return repository.findById(id);
    }

    public Optional<Question> getNonDeletedQuestionById(long id){
        return repository.findByIdEqualsAndStatusEquals(id, 1);
    }

    /**
     * Performs a soft delete of a question if the user is it's creator
     * @param user User
     * @param questionId The question id
     * @return boolean
     */
    public boolean delete(User user, long questionId) {
        Question question = repository.findById(questionId).orElse(null);
        if(question != null){
            if(question.getStatus() == 1 &&
                    (question.getUser().getId().equals(user.getId()) ||
                        user.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ROLE_MODERATOR")))){
                question.setStatus(0);
                reputationLogService.createDeletedQuestionLog(question);
                question.getFavoriteAnswer().ifPresent(reputationLogService::disfavorBestAnswer);
                repository.save(question);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Set a question as closed
     * @param questionFrom Question
     */
    public void closeQuestion(Question questionFrom) {
        questionFrom.setIsActive(0);
        repository.save(questionFrom);
    }

    /**
     * Set a question as open
     * @param questionFrom Question
     */
    public void openQuestion(Question questionFrom){
        questionFrom.setIsActive(1);
        repository.save(questionFrom);
    }
}
