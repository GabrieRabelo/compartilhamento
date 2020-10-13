package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.AnswerService;
import br.pucrs.ages.townsq.utils.Slugify;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import org.springframework.ui.Model;

@Controller
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) { this.answerService = answerService; }

    /**
     * Post route to create an answer
     * @return String
     */
    @PostMapping("/answer/create")
    public String postCreateAnswer(@AuthenticationPrincipal User user,
                                   @ModelAttribute Answer answer,
                                   @ModelAttribute Question question,
                                   Model model,
                                   final RedirectAttributes redirectAttributes
                                   ) {
        try{
          if(!StringUtils.isEmpty(answer.getText().trim())) {
              answerService.saveAnswer(answer, user, question);
              redirectAttributes.addFlashAttribute("success", "Resposta criada com sucesso!");
              return "redirect:/question/" + question.getId() + "/" + Slugify.toSlug(question.getTitle());
          } else {
              model.addAttribute("error", "Resposta não pode ser vazia!");
              return "question";
          }
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("error", "Não foi possível cadastrar a resposta.");
        }
        return "redirect:/question/" + question.getId() + "/" + Slugify.toSlug(question.getTitle());
    }
}
