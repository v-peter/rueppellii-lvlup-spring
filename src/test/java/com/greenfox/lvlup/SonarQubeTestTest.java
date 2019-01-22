package com.greenfox.lvlup;

import org.junit.Test;
import static org.junit.Assert.*;

public class SonarQubeTestTest {


//  @Autowired
//  SonarQubeTest test;

  SonarQubeTest test = new SonarQubeTest();

  @Test
  public void method() {
    assertEquals("1", test.method());
  }


}
