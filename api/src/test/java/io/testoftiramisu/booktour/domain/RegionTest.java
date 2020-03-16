package io.testoftiramisu.booktour.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionTest {

  // ToDo: rewrite to parametrized test, add missing cases
  @Test
  public void findByCentralCoastLabel() {
    assertThat(Region.findByLabel("Central Coast")).isEqualTo(Region.Central_Coast);
  }

  @Test
  public void findByNorthernCaliforniaLabel() {
    assertThat(Region.findByLabel("Northern California")).isEqualTo(Region.Northern_California);
  }

  @Test
  public void getLabel() {
    assertThat(Region.Central_Coast.getLabel()).isEqualTo(("Central Coast"));
  }
}
