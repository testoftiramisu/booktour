package io.testoftiramisu.booktour.service;

import io.testoftiramisu.booktour.TourApp;
import io.testoftiramisu.booktour.domain.TourRating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TourApp.class)
@Transactional
public class TourRatingServiceIntegrationTest {
  private static final int CUSTOMER_ID = 456;
  private static final int TOUR_ID = 1;
  private static final int NOT_A_TOUR_ID = 123;

  @Autowired private TourRatingService service;

  @Test
  @DisplayName("Happy Path: should delete existing TourRating.")
  public void shouldDeleteExistedTourRating() {
    List<TourRating> tourRatings = service.lookupAll();
    service.delete(tourRatings.get(0).getTour().getId(), tourRatings.get(0).getCustomerId());
    assertThat(service.lookupAll().size()).isEqualTo(tourRatings.size() - 1);
  }

  @Test
  @DisplayName(
      "UnHappy Path: should throw NoSuchElementException when deletes non existed TourRating.")
  public void shouldThrowNoSuchElementExceptionWhenDeleteNonExistedTourRating() {
    Assertions.assertThrows(
        NoSuchElementException.class, () -> service.delete(NOT_A_TOUR_ID, 1234));
  }

  @Test
  @DisplayName("Happy Path: should create a new Tour Rating.")
  public void shouldCreateNewTourRating() {
    createNewTourRating();

    TourRating newTourRating = service.verifyTourRating(TOUR_ID, CUSTOMER_ID);
    assertThat(newTourRating.getTour().getId()).isEqualTo(TOUR_ID);
    assertThat(newTourRating.getCustomerId()).isEqualTo(CUSTOMER_ID);
    assertThat(newTourRating.getScore()).isEqualTo(2);
    assertThat(newTourRating.getComment()).isEqualTo("it was fair");
  }

  @Test
  @DisplayName(
      "UnHappy Path: should throw NoSuchElementException when creates tour with non existed id.")
  public void createNewException() {
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> service.createNew(NOT_A_TOUR_ID, CUSTOMER_ID, 2, "it was fair"));
  }

  @Test
  @DisplayName("Happy Path: many customers should Rate one tour.")
  public void rateMany() {
    int ratings = service.lookupAll().size();
    service.rateMany(TOUR_ID, 5, new Integer[] {100, 101, 102});
    assertThat(service.lookupAll().size()).isEqualTo(ratings + 3);
  }

  @Test
  @DisplayName("Unhappy Path: 2nd Invocation should create duplicates in the database.")
  public void rateManyProveRollback() {
    Integer[] customers = {100, 101, 102};

    Assertions.assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          service.rateMany(TOUR_ID, 3, customers);
          service.rateMany(TOUR_ID, 3, customers);
        });
  }

  @Test
  @DisplayName("Happy Path: update a Tour Rating that already in the database.")
  public void update() {
    createNewTourRating();
    TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
    assertThat(tourRating.getTour().getId()).isEqualTo(TOUR_ID);
    assertThat(tourRating.getCustomerId()).isEqualTo(CUSTOMER_ID);
    assertThat(tourRating.getScore()).isEqualTo(1);
    assertThat(tourRating.getComment()).isEqualTo("one");
  }

  @Test
  @DisplayName("Unhappy path: no Tour Rating exists where tourId = 1 and customer = 1.")
  public void updateException() {
    Assertions.assertThrows(NoSuchElementException.class, () -> service.update(1, 1, 1, "one"));
  }

  @Test
  @DisplayName("Happy Path: update a Tour Rating that already in the database.")
  public void updateSome() {
    createNewTourRating();
    TourRating tourRating = service.update(TOUR_ID, CUSTOMER_ID, 1, "one");
    assertThat(tourRating.getTour().getId()).isEqualTo(TOUR_ID);
    assertThat(tourRating.getCustomerId()).isEqualTo(CUSTOMER_ID);
    assertThat(tourRating.getScore()).isEqualTo(1);
    assertThat(tourRating.getComment()).isEqualTo("one");
  }

  @Test
  @DisplayName("Unhappy path: no Tour Rating exists where tourId = 1 and customer = 1.")
  public void updateSomeException() {
    Assertions.assertThrows(NoSuchElementException.class, () -> service.update(1, 1, 1, "one"));
  }

  @Test
  @DisplayName("Happy Path: get average score of a Tour with existed TOUR_ID.")
  public void shouldGetAverageScore() {
    assertThat(service.getAverageScore(TOUR_ID)).isEqualTo(5.0);
  }

  @Test
  @DisplayName("UnHappy Path: tour with NOT_A_TOUR_ID id does not exist.")
  public void getAverageScoreException() {
    Assertions.assertThrows(
        NoSuchElementException.class, () -> service.getAverageScore(NOT_A_TOUR_ID));
  }

  /**
   * Creates a new Tour Rating in the database.
   *
   * @throws NoSuchElementException if TourRating for TOUR_ID by CUSTOMER_ID already exists
   */
  private void createNewTourRating() {
    service.createNew(TOUR_ID, CUSTOMER_ID, 2, "it was fair");
  }
}
