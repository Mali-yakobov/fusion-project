package com.sigmabit.annotations_sample;

import java.time.LocalDate;

/**
 * Created by SB50307 on 23/04/2017.
 */
public class Person implements Someone {

  private int year;

  public Person(int age) {
    this.year = LocalDate.now().getYear() - age;
  }

  @Override
  public int getAge() {
    return LocalDate.now().getYear() - year;
  }

  @Override
  public int getYear() {
    return year;
  }

  public Person setAge(int age) {
    this.year = LocalDate.now().getYear() - age;
    return this;
  }

  public Person setYear(int year) {
    this.year = year;
    return this;
  }
}
