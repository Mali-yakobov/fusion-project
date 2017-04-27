package com.sigmabit.annotaions_sample.test;

import com.sigmabit.annotations_sample.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by SB50307 on 23/04/2017.
 */
public class TestPerson {

  @Test
  public void testPerson(){
    Random random = new Random();
    int age = random.nextInt();
    Person p = new Person(age);
    Assert.assertEquals(p.getAge(), age);
    age = random.nextInt();
    p.setAge(age);
    Assert.assertEquals(p.getAge(), age);
  }
}
