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

    public Topic create(Topic topic){
        var topicOptional = repo.findByName(topic.getName());

        if(topicOptional.isPresent() && topicOptional.get().getStatus() == 1)
            throw new IllegalArgumentException("Este tópico já existe.");

        return topicOptional
                .map(this::setTopicToActive)
                .orElseGet(() -> repo.save(topic));
    }

    private Topic setTopicToActive(Topic topic){
        topic.setStatus(1);
        return repo.save(topic);
    }

    public boolean setTopicToInactive(long id){
        Topic topic= repo.findById(id).orElse(null);
        if (topic != null){
            topic.setStatus(0);
            repo.save(topic);
            return true;
        }
        return false;
    }
}
