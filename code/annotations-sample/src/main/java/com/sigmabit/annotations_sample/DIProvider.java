package com.sigmabit.annotations_sample;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Created by SB50307 on 23/04/2017.
 */
public class DIProvider {

  private static String animalType;

  public static void setAnimalType(String animalType) {
    DIProvider.animalType = animalType;
  }

  @Deprecated
  public static Animal getAnimal() {
    switch (animalType) {
      case "dog":
        return new Dog();
      case "cow":
        return new Cow();
      default:
        throw new RuntimeException("Animal not allowed");
    }
  }

  @SuppressWarnings("ConstantConditions")
  public static <T> T getClazz(Class<T> clazz)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Reflections reflections = new Reflections("com.sigmabit.annotations_sample");
    Set<Class<? extends T>> allClasses = reflections.getSubTypesOf(clazz);
    Class<? extends T> subClass = allClasses.stream()
                                            .filter(clz -> clz.getAnnotation(AnimalType.class)
                                                              .value()
                                                              .equals(animalType))
                                            .findAny()
                                            .get();
    return subClass.getConstructor().newInstance();
  }


}
