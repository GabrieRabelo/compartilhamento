package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.TopicRepository;
import br.pucrs.ages.townsq.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuestionController {

    private TopicRepository topicRepository;
    private QuestionService service;

    @Autowired
    public QuestionController(TopicRepository topicRepository, QuestionService service){
        this.topicRepository = topicRepository;
        this.service = service;
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

    @PostMapping("/question/create")
    public String postQuestionCreate(@AuthenticationPrincipal User user, @ModelAttribute Question question, Model model){
        question.setUser(user);
        service.save(question);
        return "question";
    }

}
