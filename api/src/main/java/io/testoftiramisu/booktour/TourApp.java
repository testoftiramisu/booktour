package io.testoftiramisu.booktour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.any;

@SpringBootApplication
@EnableSwagger2WebMvc
public class TourApp {

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.testoftiramisu.booktour"))
        .paths(any())
        .build()
        .apiInfo(
            new ApiInfo(
                "Tours API",
                "Microservice with an exposed RESTful API featuring HATEOAS, paging, and sorting",
                "1.0.0.1",
                null,
                new Contact("Github", "http://github.com/testoftiramisu/booktour", ""),
                null,
                null,
                new ArrayList<>()));
  }

  public static void main(String[] args) {
    SpringApplication.run(TourApp.class, args);
  }
}
