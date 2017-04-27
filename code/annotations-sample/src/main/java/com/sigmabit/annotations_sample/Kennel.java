package com.sigmabit.annotations_sample;

/**
 * Created by SB50307 on 23/04/2017.
 */
@AnimalType("dog")
public class Kennel implements AnimalHouse{


  @Override
  public String getHouse() {
    return "Kennel";
  }
}
