package io.testoftiramisu.booktour.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// ToDo: Increase test coverage
public class TourTest {
  private TourPackage tourPackage = new TourPackage("CC", "name");
  private Tour tour =
      new Tour(
          "title",
          "description",
          "blurb",
          50,
          "1 day",
          "bullet",
          "keywords",
          tourPackage,
          Difficulty.Difficult,
          Region.Central_Coast);

  @Test
  public void testConstructorAndGetters() {
    assertThat(tour.getId()).isNull();
    assertThat(tour.getTitle()).isEqualTo("title");
    assertThat(tour.getPrice()).isEqualTo(50);
    assertThat(tour.getDifficulty()).isEqualTo(Difficulty.Difficult);
  }

  @Test
  public void equalsHashcodeVerify() {
    TourPackage p = new TourPackage("CC", "name");
    Tour tour2 =
        new Tour(
            "title",
            "description",
            "blurb",
            50,
            "1 day",
            "bullet",
            "keywords",
            p,
            Difficulty.Difficult,
            Region.Central_Coast);

    assertThat(tour).isEqualTo(tour2);
  }
}
