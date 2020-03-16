package io.testoftiramisu.booktour.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// ToDo: Increase test coverage
public class TourRatingTest {

  private Tour tour =
      new Tour(
          "title",
          "description",
          "blurb",
          50,
          "1 day",
          "bullet",
          "keywords",
          new TourPackage("CC", "name"),
          Difficulty.Difficult,
          Region.Central_Coast);

  @Test
  void testTourRatingComment() {
    TourRating rating = new TourRating(tour, 1, 1, "comment");
    assertThat(rating.getComment()).isEqualTo("comment");
  }

  @Test
  void testTourRatingAutoComment() {
    TourRating rating = new TourRating(tour, 1, 1);
    assertThat(rating.getComment()).isEqualTo("Terrible");
  }

  @Test
  void testConstructor() {
    TourRating rating = new TourRating(tour, 1, 1);
    assertThat(rating.getId()).isNull();
    assertThat(rating.getTour()).isEqualTo(tour);
    assertThat(rating.getCustomerId()).isEqualTo(1);
  }

  @Test
  public void equalsHashcodeVerify() {
    TourRating rating1 = new TourRating(tour, 1, 1, "comment");
    TourRating rating2 = new TourRating(tour, 1, 1, "comment");

    assertThat(rating1).isEqualTo(rating2);
    assertThat(rating1.hashCode()).isEqualTo(rating2.hashCode());
  }
}
