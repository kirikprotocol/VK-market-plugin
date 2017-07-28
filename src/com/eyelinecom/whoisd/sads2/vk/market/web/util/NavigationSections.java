package com.eyelinecom.whoisd.sads2.vk.market.web.util;


/**
 * author: Artem Voronov
 */
public class NavigationSections {

  private final int prevSection;
  private final int currSection;
  private final int nextSection;
  private final int sectionsCount;
  private final int partitionSize;
  private final int quantity;

  public NavigationSections(Integer startSection, int quantity, int partitionSize) {
    this.quantity = quantity;
    this.partitionSize = partitionSize;
    sectionsCount = findSectionsCount();
    currSection = findCurrentSection(startSection);
    prevSection = currSection == 0 ? sectionsCount - 1 : currSection - 1;
    nextSection = currSection == sectionsCount - 1 ? 0 : currSection + 1;
  }

  private int findCurrentSection(Integer startSection) {
    if(startSection == null)
      return 0;

    return startSection;
  }

  private int findSectionsCount() {
    int modResult = quantity % partitionSize;
    int divResult = quantity / partitionSize;

    if (modResult == 0)
      return divResult;

    return divResult + 1;
  }

  public int getPrevSection() {
    return prevSection;
  }

  public int getCurrSection() {
    return currSection;
  }

  public int getNextSection() {
    return nextSection;
  }

  public int getSectionsCount() {
    return sectionsCount;
  }
}
