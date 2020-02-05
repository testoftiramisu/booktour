package io.testoftiramisu.booktour.domain;

/** Enumeration of regions of California. */
public enum Region {
  Central_Coast("Central_coast"),
  Southern_California("Southern_California"),
  Northern_California("Northern_California"),
  Varies("Varies");

  private String label;

  private Region(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static Region findByLabel(String label) {
    for (Region region : Region.values()) {
      if (region.label.equalsIgnoreCase(label)) {
        return region;
      }
    }
    return null;
  }
}
