package com.sigmabit.annotations_sample;

/**
 * Created by SB50307 on 23/04/2017.
 */
@AnimalType("cow")
public class Cow implements Animal {
  @Override
  public String sound() {
    return "Mooooo";
  }

  @Override
  public void work() {
    System.out.println("Produce Milk");
  }
}
