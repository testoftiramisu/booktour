package io.testoftiramisu.booktour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooktourApplication {
  private static Logger logger = LoggerFactory.getLogger(BooktourApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BooktourApplication.class, args);
  }
}
