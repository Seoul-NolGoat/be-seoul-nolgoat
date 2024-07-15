package wad.seoul_nolgoat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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
