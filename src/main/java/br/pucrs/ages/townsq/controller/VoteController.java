package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.VoteService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PutMapping(value = "/upvote/{entity}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTestUpvote(@PathVariable String entity, @PathVariable Long id, @AuthenticationPrincipal User user){
        try{
            int scoreDelta = voteService.upVote(entity, id, user);
            JSONObject response = new JSONObject();
            response.put("target", entity);
            response.put("id", id);
            response.put("delta", scoreDelta);
            return response.toString();
        }catch (IllegalArgumentException e){
            return "{ \"error\": true }";
        }
    }

    @PutMapping(value = "/downvote/{entity}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTestDownvote(@PathVariable String entity, @PathVariable Long id, @AuthenticationPrincipal User user){
        try{
            int scoreDelta = voteService.downVote(entity, id, user);
            JSONObject response = new JSONObject();
            response.put("target", entity);
            response.put("id", id);
            response.put("delta", scoreDelta);
            return response.toString();
        }catch (IllegalArgumentException e){
            return "{ \"error\": true }";
        }
    }
}
