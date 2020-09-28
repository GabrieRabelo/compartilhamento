package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class QuestionController {

    private TopicRepository topicRepository;

    @Autowired
    public QuestionController(TopicRepository topicRepository){
        this.topicRepository = topicRepository;
    }

    /**
     * Returns the Question page with it's contents. Will be implemented on future sprints.
     * @return question page
     */
    @GetMapping("/question")
    public String getQuestionPage(){
        return "question";
    }

    /**
     * Returns the question creation page with the topics from the database
     * @param m The UI Model
     * @return String
     */
    @GetMapping("/question/create")
    public String getQuestionCreatePage(Model m){
        m.addAttribute("topics", topicRepository.findAllByStatus(1));
        return "questionCreate";
    }

}
