package com.sigmabit.annotations_sample;

/**
 * Created by SB50307 on 23/04/2017.
 */
@AnimalType("cow")
public class Cowshed implements AnimalHouse {
  @Override
  public String getHouse() {
    return "Cowshed";
  }
}
