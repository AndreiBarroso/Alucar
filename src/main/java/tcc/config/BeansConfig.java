package tcc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tcc.service.SendGridEmailService;

@Configuration
public class BeansConfig {

    @Value("${sendgrid.key}")
    private String apiKey;

    @Value("${remetenteDefault}")
    private String remetenteDefault;

    @Bean
    public SendGridEmailService sendGridEmailService() {
        return new SendGridEmailService(apiKey, remetenteDefault);
    }

}
