package com.lunex.timeseries;

import com.google.common.reflect.TypeToken;

import org.testng.annotations.Test;

public class GenericTypeTest {

  @Test
  public void testTypeToken() {
    class IKnowMyType<T> {

      TypeToken<T> type = new TypeToken<T>(getClass()) {
      };

    }
    class MyClass extends IKnowMyType<Long> {

    }

    class MyClass2<T> extends IKnowMyType<T> {

    }

    //using concrete subclass
    assert new MyClass().type.getType() == Long.class;

    //using anonymouse subclass
    assert new IKnowMyType<Long>() {}.type.getType() == Long.class;
    //using
    System.out.println(new IKnowMyType<Long>().type.toString());
    assert new IKnowMyType<Long>().type.toString().equals("T");

    assert new MyClass2<Long>().type.toString().equals("T");
  }
}