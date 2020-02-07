package io.testoftiramisu.booktour.web;

import io.testoftiramisu.booktour.domain.TourRating;
import io.testoftiramisu.booktour.repository.TourRepository;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/** Rating Assembler, convert TourRating to a Hateoas Supported Rating class */
@Component
public class RatingAssembler extends RepresentationModelAssemblerSupport<TourRating, RatingDto> {

  // Helper to fetch Spring Data Rest Repository links.
  private RepositoryEntityLinks entityLinks;

  public RatingAssembler(RepositoryEntityLinks entityLinks) {
    super(RatingController.class, RatingDto.class);
    this.entityLinks = entityLinks;
  }

  /**
   * Generates "self", "rating" and tour links.
   *
   * @param tourRating Tour Rating Entity
   */
  @Override
  public RatingDto toModel(TourRating tourRating) {
    RatingDto rating =
        new RatingDto(tourRating.getScore(), tourRating.getComment(), tourRating.getCustomerId());

    // "self" : ".../ratings/{ratingId}"
    WebMvcLinkBuilder ratingLink =
        linkTo(methodOn(RatingController.class).getRating(tourRating.getId()));
    rating.add(ratingLink.withSelfRel());

    // "tour" : ".../tours/{tourId}"
    Link tourLink =
        entityLinks.linkToItemResource(TourRepository.class, tourRating.getTour().getId());
    rating.add(tourLink.withRel("tour"));
    return rating;
  }


}
