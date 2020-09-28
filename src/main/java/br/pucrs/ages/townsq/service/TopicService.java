package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private TopicRepository repo;

    @Autowired
    public TopicService(TopicRepository r){
        this.repo = r;
    }

    public List<Topic> getAllTopicsByStatus(int status){
        return repo.findAllByStatus(status);
    }

    public List<Topic> getAllTopics(){
        return repo.findAll();
    }

}
