package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) { this.answerRepository = answerRepository; }

    /**
     * Saves an Answer on the database
     * @param a Answer
     * @return Answer
     */
    public Answer saveAnswer(Answer answer, User user, Question question) {
        answer.setQuestion(question);
        answer.setUser(user);
        return this.answerRepository.save(answer);
    }
}
