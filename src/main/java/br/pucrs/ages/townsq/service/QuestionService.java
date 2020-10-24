package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository repository;

    @Autowired
    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
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

        if(currentQuestion == null || !currentQuestion.getUser().getId().equals(user.getId()))
            throw new IllegalArgumentException("A pergunta não pertence ao usuário.");

        currentQuestion.setTitle(question.getTitle());
        currentQuestion.setTopic((question.getTopic()));
        currentQuestion.setDescription(question.getDescription());

        return repository.save(currentQuestion);
    }

    /**
     * Gets the questions to be displayed on the homepage.
     * @return List of questions
     */
    public List<Question> getIndexQuestions(List<Long> params) {
        if(params == null || params.isEmpty()){
            return repository.findTop10ByStatusEqualsOrderByCreatedAtDesc(1);
        }
        Collection<Topic> topics = params.stream().map(e ->
             Topic.builder().id(e).build()).collect(Collectors.toCollection(HashSet::new));
        return repository.findTop10ByStatusEqualsAndTopicInOrderByCreatedAtDesc(1, topics);

    }

    /**
     * Returns a question, if it exists, given it's id
     * @param id The question's id
     * @return Optional of Question
     */
    public Optional<Question> getQuestionById(long id) {
        return repository.findById(id);
    }

    /**
     * Performs a soft delete of a question if the user is it's creator
     * @param userId The user id
     * @param questionId The question id
     * @return boolean
     */
    public boolean delete(long userId, long questionId) {
        Question question = repository.findById(questionId).orElse(null);
        if(question != null){
            if(question.getUser().getId() == userId && question.getStatus() == 1){
                question.setStatus(0);
                repository.save(question);
                return true;
            }
            return false;
        }
        return false;
    }

}
