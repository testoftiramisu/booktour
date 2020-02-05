package io.testoftiramisu.booktour.repository;

import io.testoftiramisu.booktour.domain.TourPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/** Tour Package Repository Interface. */
@RepositoryRestResource(collectionResourceRel = "packages", path = "packages")
public interface TourPackageRepository extends CrudRepository<TourPackage, String> {

  /** Finds {@link TourPackage} by proved name. */
  Optional<TourPackage> findByName(@Param("name") String name);

  @Override
  @RestResource(exported = false)
  <S extends TourPackage> S save(S s);

  @Override
  @RestResource(exported = false)
  <S extends TourPackage> Iterable<S> saveAll(Iterable<S> iterable);

  @Override
  @RestResource(exported = false)
  void deleteById(String s);

  @Override
  @RestResource(exported = false)
  void delete(TourPackage tourPackage);

  @Override
  @RestResource(exported = false)
  void deleteAll(Iterable<? extends TourPackage> iterable);

  @Override
  @RestResource(exported = false)
  void deleteAll();
}
