package io.testoftiramisu.booktour.web;

import io.testoftiramisu.booktour.domain.Tour;
import io.testoftiramisu.booktour.domain.TourRating;
import io.testoftiramisu.booktour.service.TourRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Invoke the Controller methods via HTTP.
 *
 * <p>Do not invoke the tourRatingService methods, use Mock instead
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = io.testoftiramisu.booktour.BooktourApplication.class)
public class RatingControllerTest {
  private static final String RATINGS_URL = "/ratings";

  // These Tour and rating id's do not already exist in the db
  private static final int TOUR_ID = 999;
  private static final int RATING_ID = 555;
  private static final int CUSTOMER_ID = 1000;
  private static final int SCORE = 3;
  private static final String COMMENT = "comment";

  @MockBean private TourRatingService tourRatingServiceMock;

  @Mock private TourRating tourRatingMock;

  @Mock private Tour tourMock;

  @Autowired private TestRestTemplate restTemplate;

  @BeforeEach
  public void setupReturnValuesOfMockMethods() {
    when(tourRatingMock.getTour()).thenReturn(tourMock);
    when(tourMock.getId()).thenReturn(TOUR_ID);
    when(tourRatingMock.getComment()).thenReturn(COMMENT);
    when(tourRatingMock.getScore()).thenReturn(SCORE);
    when(tourRatingMock.getCustomerId()).thenReturn(CUSTOMER_ID);
  }

  /** HTTP GET /ratings */
  @Test
  public void getRatings() {
    when(tourRatingServiceMock.lookupAll())
        .thenReturn(Arrays.asList(tourRatingMock, tourRatingMock, tourRatingMock));

    ResponseEntity<CollectionModel<RatingDto>> response =
        restTemplate.exchange(
            RATINGS_URL, HttpMethod.GET, null, createParameterizedTypeReference());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(3);
  }

  /** HTTP GET /ratings/{id} */
  @Test
  public void getOne() {
    when(tourRatingServiceMock.lookupRatingById(RATING_ID)).thenReturn(Optional.of(tourRatingMock));

    ResponseEntity<RatingDto> response =
        restTemplate.getForEntity(RATINGS_URL + "/" + RATING_ID, RatingDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getCustomerId()).isEqualTo(CUSTOMER_ID);
    assertThat(response.getBody().getComment()).isEqualTo(COMMENT);
    assertThat(response.getBody().getScore()).isEqualTo(SCORE);
  }

  /**
   * Fixes NullPointerException during compilation of Generic ParameterizedTypeReference.
   *
   * <p>javac can't tell during speculative attribution if a diamond expression is creating an
   * anonymous inner class or not.
   *
   * @return parameterized type reference of <CollectionModel<RatingDto>> type
   */
  private ParameterizedTypeReference<CollectionModel<RatingDto>>
      createParameterizedTypeReference() {
    return new ParameterizedTypeReference<>() {};
  }
}
