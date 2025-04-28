package co.istad.mbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class Sbc11MbankingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sbc11MbankingApiApplication.class, args);
    }

}
