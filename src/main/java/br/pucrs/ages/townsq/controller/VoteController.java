package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VoteController {

    private VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    // Rota para testes
    @GetMapping("/upvote/{entity}/{id}")
    public String getTestUpvote(@PathVariable String entity, @PathVariable Long id, @AuthenticationPrincipal User user){
        voteService.upVote(entity, id, user);
        return "redirect:/";
    }

    @GetMapping("/downvote/{entity}/{id}")
    public String getTestDownvote(@PathVariable String entity, @PathVariable Long id, @AuthenticationPrincipal User user){
        voteService.downVote(entity, id, user);
        return "redirect:/";
    }
}
