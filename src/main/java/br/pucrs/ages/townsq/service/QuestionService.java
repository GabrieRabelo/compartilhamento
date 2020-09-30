package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository repository;

    @Autowired
    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public Question save(Question question){
        return repository.save(question);
    }

    public List<Question> getIndexQuestions() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

}
