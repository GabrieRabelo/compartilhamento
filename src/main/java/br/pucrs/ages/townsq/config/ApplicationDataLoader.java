package br.pucrs.ages.townsq.config;

import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.repository.TopicRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
@Profile("!test")
public class ApplicationDataLoader {

    @Bean
    @Transactional
    public ApplicationRunner dataInitializer(TopicRepository topicRepo){
        return args -> {
            if(topicRepo.count() == 0){
                topicRepo.saveAll(Arrays.asList(
                        new Topic().toBuilder().name("Portaria").build(),
                        new Topic().toBuilder().name("Segurança").build()
                ));
            }
        };
    }

}
