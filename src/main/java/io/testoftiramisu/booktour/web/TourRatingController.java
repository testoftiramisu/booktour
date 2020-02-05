package io.testoftiramisu.booktour.web;

import io.testoftiramisu.booktour.domain.TourRating;
import io.testoftiramisu.booktour.service.TourRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/** Tour Rating Controller. */
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
  private TourRatingService tourRatingService;

  @Autowired
  public TourRatingController(TourRatingService tourRatingService) {
    this.tourRatingService = tourRatingService;
  }

  protected TourRatingController() {}

  /**
   * Creates a Tour Rating.
   *
   * @param tourId tour identifier
   * @param ratingDto tour rating data transfer object
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createTourRating(
      @PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
    tourRatingService.createNew(
        tourId, ratingDto.getCustomerId(), ratingDto.getScore(), ratingDto.getComment());
  }

  /**
   * Creates Several Tour Ratings for one tour, score and several customers.
   *
   * @param tourId tour identifier
   * @param score tour score
   * @param customers customers ids
   */
  @PostMapping("/{score}")
  @ResponseStatus(HttpStatus.CREATED)
  public void createManyTourRatings(
      @PathVariable(value = "tourId") int tourId,
      @PathVariable(value = "score") int score,
      @RequestParam("customers") Integer customers[]) {
    tourRatingService.rateMany(tourId, score, customers);
  }

  /**
   * Looks up a the Ratings for a tour.
   *
   * @param tourId tour identifier
   * @param pageable pageable object
   * @return
   */
  @GetMapping
  public Page<RatingDto> getAllRatingsForTour(
      @PathVariable(value = "tourId") int tourId, Pageable pageable) {
    Page<TourRating> tourRatingPage = tourRatingService.lookupRatings(tourId, pageable);
    List<RatingDto> ratingDtoList =
        tourRatingPage.getContent().stream().map(this::toDto).collect(Collectors.toList());
    return new PageImpl<RatingDto>(ratingDtoList, pageable, tourRatingPage.getTotalPages());
  }

  /**
   * Calculates the average Score of a Tour.
   *
   * @param tourId tour identifier
   * @return Tuple of "average" and the average value
   */
  @GetMapping("/average")
  public AbstractMap.SimpleEntry<String, Double> getAverage(
      @PathVariable(value = "tourId") int tourId) {
    return new AbstractMap.SimpleEntry<String, Double>(
        "average", tourRatingService.getAverageScore(tourId));
  }

  /**
   * Updates score and comment of a Tour Rating.
   *
   * @param tourId tour identifier
   * @param ratingDto
   * @return The modified Rating DTO
   */
  @PutMapping
  public RatingDto updateWithPut(
      @PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
    return toDto(
        tourRatingService.update(
            tourId, ratingDto.getCustomerId(), ratingDto.getScore(), ratingDto.getComment()));
  }
  /**
   * Updates score or comment of a Tour Rating.
   *
   * @param tourId tour identifier
   * @param ratingDto
   * @return The modified Rating DTO.
   */
  @PatchMapping
  public RatingDto updateWithPatch(
      @PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
    return toDto(
        tourRatingService.updateSome(
            tourId, ratingDto.getCustomerId(), ratingDto.getScore(), ratingDto.getComment()));
  }

  /**
   * Deletes a Rating of a tour made by a customer.
   *
   * @param tourId tour identifier
   * @param customerId customer identifier
   */
  @DeleteMapping("/{customerId}")
  public void delete(
      @PathVariable(value = "tourId") int tourId,
      @PathVariable(value = "customerId") int customerId) {
    tourRatingService.delete(tourId, customerId);
  }

  /**
   * Converts the TourRating entity to a RatingDto.
   *
   * @param tourRating
   * @return RatingDto
   */
  private RatingDto toDto(TourRating tourRating) {
    return new RatingDto(
        tourRating.getScore(), tourRating.getComment(), tourRating.getCustomerId());
  }

  /**
   * Exception handler if NoSuchElementException is thrown in this Controller.
   *
   * @param ex exception
   * @return Error message String
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public String return400(NoSuchElementException ex) {
    return ex.getMessage();
  }
}
