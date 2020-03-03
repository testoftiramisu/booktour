package io.testoftiramisu.booktour.service;

import io.testoftiramisu.booktour.domain.Tour;
import io.testoftiramisu.booktour.domain.TourRating;
import io.testoftiramisu.booktour.repository.TourRatingRepository;
import io.testoftiramisu.booktour.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourRatingServiceTest {

  private static final int CUSTOMER_ID = 123;
  private static final int TOUR_ID = 1;
  private static final int TOUR_RATING_ID = 100;

  @Mock private TourRepository tourRepositoryMock;
  @Mock private TourRatingRepository tourRatingRepositoryMock;

  @InjectMocks private TourRatingService service;

  @Mock private Tour tourMock;
  @Mock private TourRating tourRatingMock;

  /** Mock responses to commonly invoked methods. */
  @BeforeEach
  public void setupReturnValuesOfMockMethods() {
    lenient().when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tourMock));
    lenient().when(tourMock.getId()).thenReturn(TOUR_ID);
    lenient()
        .when(tourRatingRepositoryMock.findByTourIdAndCustomerId(TOUR_ID, CUSTOMER_ID))
        .thenReturn(Optional.of(tourRatingMock));
    lenient()
        .when(tourRatingRepositoryMock.findByTourId(TOUR_ID))
        .thenReturn(Collections.singletonList(tourRatingMock));
  }

  /** Verifies the service return value. */
  @Test
  public void lookupRatingById() {
    when(tourRatingRepositoryMock.findById(TOUR_RATING_ID)).thenReturn(Optional.of(tourRatingMock));

    // invoke and verify lookupRatingById
    assertThat(service.lookupRatingById(TOUR_RATING_ID).get()).isEqualTo(tourRatingMock);
  }

  @Test
  public void lookupAll() {
    when(tourRatingRepositoryMock.findAll()).thenReturn(Collections.singletonList(tourRatingMock));

    // invoke and verify lookupAll
    assertThat(service.lookupAll().get(0)).isEqualTo(tourRatingMock);
  }

  @Test
  public void getAverageScore() {
    when(tourRatingMock.getScore()).thenReturn(10);

    // invoke and verify getAverageScore
    assertThat(service.getAverageScore(TOUR_ID)).isEqualTo(10.0);
  }

  @Test
  public void lookupRatings() {
    // create mocks of Pageable and Page (only needed in this test)
    Pageable pageable = mock(Pageable.class);
    Page page = mock(Page.class);
    when(tourRatingRepositoryMock.findByTourId(1, pageable)).thenReturn(page);

    // invoke and verify lookupRatings
    assertThat(service.lookupRatings(TOUR_ID, pageable)).isEqualTo(page);
  }

  /** Verifies the invocation of dependencies. */
  @Test
  public void delete() {
    // invoke delete
    service.delete(1, CUSTOMER_ID);

    // verify tourRatingRepository.delete invoked
    verify(tourRatingRepositoryMock).delete(any(TourRating.class));
  }

  @Test
  public void rateMany() {
    // invoke rateMany
    service.rateMany(TOUR_ID, 10, new Integer[] {CUSTOMER_ID, CUSTOMER_ID + 1});

    // verify tourRatingRepository.save invoked twice
    verify(tourRatingRepositoryMock, times(2)).save(any(TourRating.class));
  }

  @Test
  public void update() {
    // invoke update
    service.update(TOUR_ID, CUSTOMER_ID, 5, "great");

    // verify tourRatingRepository.save invoked once
    verify(tourRatingRepositoryMock).save(any(TourRating.class));

    // verify and tourRating setter methods invoked
    verify(tourRatingMock).setComment("great");
    verify(tourRatingMock).setScore(5);
  }

  @Test
  public void updateSome() {
    // invoke updateSome
    service.updateSome(TOUR_ID, CUSTOMER_ID, 1, "awful");

    // verify tourRatingRepository.save invoked once
    verify(tourRatingRepositoryMock).save(any(TourRating.class));

    // verify and tourRating setter methods invoked
    verify(tourRatingMock).setComment("awful");
    verify(tourRatingMock).setScore(1);
  }

  /** Verifies the invocation of dependencies and verifies the captured parameter values. */
  @Test
  public void createNew() {
    // prepare to capture a TourRating Object
    ArgumentCaptor<TourRating> tourRatingCaptor = ArgumentCaptor.forClass(TourRating.class);

    // invoke createNew
    service.createNew(TOUR_ID, CUSTOMER_ID, 2, "ok");

    // verify tourRatingRepository.save invoked once and capture the TourRating Object
    verify(tourRatingRepositoryMock).save(tourRatingCaptor.capture());

    // verify the attributes of the Tour Rating Object
    assertThat(tourRatingCaptor.getValue().getTour()).isEqualTo(tourMock);
    assertThat(tourRatingCaptor.getValue().getCustomerId()).isEqualTo(CUSTOMER_ID);
    assertThat(tourRatingCaptor.getValue().getScore()).isEqualTo(2);
    assertThat(tourRatingCaptor.getValue().getComment()).isEqualTo("ok");
  }
}
