package io.testoftiramisu.booktour.domain;

/** Enumeration of regions of California. */
public enum Region {
  Central_Coast("Central Coast"),
  Southern_California("Southern California"),
  Northern_California("Northern California"),
  Varies("Varies");

  private String label;

  Region(String label) {
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
