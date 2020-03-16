package io.testoftiramisu.booktour.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Rating DTO tests. */
public class RatingDtoTest {

  @Test
  public void testConstructor() {
    RatingDto dto = new RatingDto(1, "comment", 2);

    assertThat(dto.getScore()).isEqualTo(1);
    assertThat(dto.getComment()).isEqualTo("comment");
    assertThat(dto.getCustomerId()).isEqualTo(2);
  }

  @Test
  public void testSetters() {
    RatingDto dto = new RatingDto();
    dto.setComment("comment");
    dto.setCustomerId(2);
    dto.setScore(1);

    assertThat(dto.getScore()).isEqualTo(1);
    assertThat(dto.getComment()).isEqualTo("comment");
    assertThat(dto.getCustomerId()).isEqualTo(2);
  }
}
