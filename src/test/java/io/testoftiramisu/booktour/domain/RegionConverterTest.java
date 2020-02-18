package io.testoftiramisu.booktour.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// ToDo: Increase test coverage
public class RegionConverterTest {
  private RegionConverter converter = new RegionConverter();

  @Test
  public void convertsToDatabaseColumn() {
    assertThat(converter.convertToDatabaseColumn(Region.Central_Coast))
        .isEqualTo(Region.Central_Coast.getLabel());
  }
}
