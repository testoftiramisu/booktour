package io.testoftiramisu.booktour.repository;

import io.testoftiramisu.booktour.domain.Difficulty;
import io.testoftiramisu.booktour.domain.Region;
import io.testoftiramisu.booktour.domain.Tour;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** Tour Repository Interface. */
public interface TourRepository extends PagingAndSortingRepository<Tour, Integer> {
  List<Tour> findByTourPackageCode(@Param("code") String code, Pageable pageable);

  Optional<Tour> findByPrice(Integer price);

  Collection<Tour> findByDifficulty(Difficulty difficulty);

  List<Tour> findByRegion(Region region);

  List<Tour> findByTourPackageCodeAndRegion(String code, Region region);

  List<Tour> findByPriceLessThan(Integer maxPrice);

  List<Tour> findByKeywordsContains(String keyword);

  @Query(
      "Select t from Tour t where t.tourPackage.code = ?1 "
          + " and t.difficulty = ?2 and t.region = ?3 and t.price <= ?4")
  List<Tour> lookupTour(String code, Difficulty difficulty, Region region, Integer maxPrice);

  @Override
  @RestResource(exported = false)
  <S extends Tour> S save(S s);

  @Override
  @RestResource(exported = false)
  <S extends Tour> Iterable<S> saveAll(Iterable<S> iterable);

  @Override
  @RestResource(exported = false)
  void deleteById(Integer integer);

  @Override
  @RestResource(exported = false)
  void delete(Tour tour);

  @Override
  @RestResource(exported = false)
  void deleteAll(Iterable<? extends Tour> iterable);

  @Override
  @RestResource(exported = false)
  void deleteAll();
}
