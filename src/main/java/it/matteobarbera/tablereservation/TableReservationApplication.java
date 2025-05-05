package it.matteobarbera.tablereservation;

import it.matteobarbera.tablereservation.log.LoggingInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@RestController
@EnableCaching
public class TableReservationApplication implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    public TableReservationApplication(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(TableReservationApplication.class, args);

    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }


}
