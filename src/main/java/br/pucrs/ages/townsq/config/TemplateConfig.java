package br.pucrs.ages.townsq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class TemplateConfig {

    private final Environment env;

    @Autowired
    public TemplateConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SpringResourceTemplateResolver defaultTemplateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        if(env.getActiveProfiles()[0].equals("dev"))
            templateResolver.setCacheable(false);
        templateResolver.setOrder(0);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

    @Bean
    public SpringResourceTemplateResolver emailTemplateResolver(){
        SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix("classpath:/templates/email/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("HTML");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);
        emailTemplateResolver.setCheckExistence(true);
        return emailTemplateResolver;
    }

}
