package io.testoftiramisu.booktour;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.any;

@Configuration
@Import(SpringDataRestConfiguration.class)
@EnableSwagger2WebMvc
public class SpringFoxConfig {

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.ec"))
        .paths(any())
        .build()
        .apiInfo(
            new ApiInfo(
                "Explore California API's",
                "API's for the Explore California Travel Service",
                "2.0",
                null,
                new Contact("LinkedIn Learning", "https://www.linkedin.com/learning", ""),
                null,
                null,
                new ArrayList()));
  }
}
