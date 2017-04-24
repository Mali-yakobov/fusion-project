package com.sigmabit.annotations_sample;

/**
 * Created by SB50307 on 23/04/2017.
 */
@AnimalType("dog")
public class Dog implements Animal {

  @Override
  public String sound() {
    return "Woof";
  }

  @Override
  public void work() {
    System.out.println("I am Cuddling");
  }
}
