package io.testoftiramisu.booktour.web;

import io.testoftiramisu.booktour.service.TourRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

/** Rating Controller. */
@RestController
@RequestMapping(path = "/ratings")
public class RatingController {
  private TourRatingService tourRatingService;
  private RatingAssembler ratingAssembler;

  @Autowired
  public RatingController(TourRatingService tourRatingService, RatingAssembler ratingAssembler) {
    this.tourRatingService = tourRatingService;
    this.ratingAssembler = ratingAssembler;
  }

  @GetMapping
  public CollectionModel<RatingDto> getAll() {
    return ratingAssembler.toCollectionModel(tourRatingService.lookupAll());
  }

  @GetMapping("/{id}")
  public RatingDto getRating(@PathVariable("id") Integer id) {
    return ratingAssembler.toModel(
        tourRatingService
            .lookupRatingById(id)
            .orElseThrow(() -> new NoSuchElementException("Rating " + id + " not found")));
  }

  /**
   * Exception handler if NoSuchElementException is thrown in this Controller
   *
   * @param ex exception
   * @return Error message String.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  public String return400(NoSuchElementException ex) {
    return ex.getMessage();
  }
}
