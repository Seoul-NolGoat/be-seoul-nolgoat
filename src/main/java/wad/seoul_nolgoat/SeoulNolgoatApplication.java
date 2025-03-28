package wad.seoul_nolgoat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@EnableRetry
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class SeoulNolgoatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeoulNolgoatApplication.class, args);
    }

    // "No beans of 'RestTemplate' type found" 오류 해결
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
