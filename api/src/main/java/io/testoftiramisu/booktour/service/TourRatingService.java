package io.testoftiramisu.booktour.service;

import io.testoftiramisu.booktour.domain.Tour;
import io.testoftiramisu.booktour.domain.TourRating;
import io.testoftiramisu.booktour.repository.TourRatingRepository;
import io.testoftiramisu.booktour.repository.TourRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;

/** Tour Rating Service. */
@Service
@Transactional
@Slf4j
public class TourRatingService {
  private TourRatingRepository tourRatingRepository;
  private TourRepository tourRepository;

  /**
   * Constructs TourRatingService.
   *
   * @param tourRatingRepository Tour Rating Repository
   * @param tourRepository Tour Repository
   */
  @Autowired
  public TourRatingService(
      TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
    this.tourRatingRepository = tourRatingRepository;
    this.tourRepository = tourRepository;
  }

  /**
   * Creates a new Tour Rating in the database.
   *
   * @param tourId tour identifier
   * @param customerId customer identifier
   * @param score score of the tour rating
   * @param comment additional comment
   * @throws NoSuchElementException if no Tour found
   */
  public void createNew(int tourId, Integer customerId, Integer score, String comment)
      throws NoSuchElementException {
    log.info("Create Rating for tour {} of customers {}", tourId, customerId);
    tourRatingRepository.save(new TourRating(verifyTour(tourId), customerId, score, comment));
  }

  /**
   * Gets All Tour Ratings.
   *
   * @return {@link List} of TourRatings
   */
  public List<TourRating> lookupAll() {
    log.info("Lookup all Ratings");
    return tourRatingRepository.findAll();
  }

  /**
   * Gets a ratings by id.
   *
   * @param id rating identifier
   * @return {@link Optional} of TourRatings
   */
  public Optional<TourRating> lookupRatingById(int id) {
    log.info("Lookup Rating for tour {}", id);
    return tourRatingRepository.findById(id);
  }

  /**
   * Gets a page of tour ratings for a tour.
   *
   * @param tourId tour identifier
   * @param pageable page parameters to determine which elements to fetch
   * @return {@link Page} of TourRatings
   * @throws NoSuchElementException if no Tour found.
   */
  public Page<TourRating> lookupRatings(int tourId, Pageable pageable)
      throws NoSuchElementException {
    log.info("Lookup Rating for tour {}", tourId);
    return tourRatingRepository.findByTourId(verifyTour(tourId).getId(), pageable);
  }

  /**
   * Updates all of the elements of a Tour Rating.
   *
   * @param tourId tour identifier
   * @param score score of the tour rating
   * @param comment additional comment
   * @return Tour Rating Domain Object
   * @throws NoSuchElementException if no Tour found
   */
  public TourRating update(int tourId, Integer customerId, Integer score, String comment)
      throws NoSuchElementException {
    log.info("Update all of Rating for tour {} of customers {}", tourId, customerId);
    TourRating rating = verifyTourRating(tourId, customerId);
    rating.setScore(score);
    rating.setComment(comment);
    return tourRatingRepository.save(rating);
  }

  /**
   * Update some of the elements of a Tour Rating.
   *
   * @param tourId tour identifier
   * @param customerId customer identifier
   * @param score score of the tour rating
   * @param comment additional comment
   * @return Tour Rating Domain Object
   * @throws NoSuchElementException if no Tour found.
   */
  public TourRating updateSome(int tourId, Integer customerId, Integer score, String comment)
      throws NoSuchElementException {
    log.info("Update some of Rating for tour {} of customers {}", tourId, customerId);
    TourRating rating = verifyTourRating(tourId, customerId);
    if (score != null) {
      rating.setScore(score);
    }
    if (comment != null) {
      rating.setComment(comment);
    }
    return tourRatingRepository.save(rating);
  }

  /**
   * Delete a Tour Rating.
   *
   * @param tourId tour identifier
   * @param customerId customer identifier
   * @throws NoSuchElementException if no Tour found.
   */
  public void delete(int tourId, Integer customerId) throws NoSuchElementException {
    log.info("Delete Rating for tour {} and customer {}", tourId, customerId);
    TourRating rating = verifyTourRating(tourId, customerId);
    tourRatingRepository.delete(rating);
  }
  /**
   * Get the average score of a tour.
   *
   * @param tourId tour identifier
   * @return average score as a Double.
   * @throws NoSuchElementException exception
   */
  public Double getAverageScore(int tourId) throws NoSuchElementException {
    log.info("Get average score of tour {}", tourId);
    List<TourRating> ratings = tourRatingRepository.findByTourId(verifyTour(tourId).getId());
    OptionalDouble average = ratings.stream().mapToInt(TourRating::getScore).average();
    return average.isPresent() ? average.getAsDouble() : null;
  }
  /**
   * Service for many customers to give the same score for a service
   *
   * @param tourId tour identifier
   * @param score tour score
   * @param customers array of customer ids
   */
  public void rateMany(int tourId, int score, Integer[] customers) {
    log.info("Rate tour {} by customers {}", tourId, Arrays.asList(customers).toString());
    Tour tour = tourRepository.findById(tourId).orElseThrow(NoSuchElementException::new);

    Arrays.stream(customers)
        .forEach(
            (c) -> {
              log.debug("Attempt to create Tour Rating for customer {}", c);
              tourRatingRepository.save(new TourRating(tour, c, score));
            });
  }

  /**
   * Verify and return the Tour given a tourId.
   *
   * @param tourId tour identifier
   * @return the found Tour
   * @throws NoSuchElementException if no Tour found
   */
  private Tour verifyTour(int tourId) throws NoSuchElementException {
    return tourRepository
        .findById(tourId)
        .orElseThrow(() -> new NoSuchElementException("Tour does not exist " + tourId));
  }

  /**
   * Verifies and returns the TourRating for a particular tourId and Customer.
   *
   * @param tourId tour identifier
   * @param customerId customer identifier
   * @return the found TourRating
   * @throws NoSuchElementException if no TourRating found
   */
  TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException {
    return tourRatingRepository
        .findByTourIdAndCustomerId(tourId, customerId)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "Tour-Rating pair for request(" + tourId + " for customer" + customerId));
  }
}
