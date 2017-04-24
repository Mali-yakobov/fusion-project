package com.sigmabit.annotations_sample;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by SB50307 on 23/04/2017.
 */
public class Main {

  public static void main(String ... args)
      throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

    Someone gad = new Person(3);
    System.out.println("Gad's age is: " + gad.getAge());

    DIProvider.setAnimalType(args[0]);
    Animal animal = DIProvider.getAnimal();
    System.out.println("Animal goes: " + animal.sound());
    animal.work();

    Animal diAnimal = DIProvider.getClazz(Animal.class);
    System.out.println("DI Animal goes: " + diAnimal.sound());
    diAnimal.work();

    AnimalHouse house = DIProvider.getClazz(AnimalHouse.class);
    System.out.println(house.getHouse());
  }
}
