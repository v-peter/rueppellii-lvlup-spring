package com.greenfox.lvlup.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.lvlup.model.Badge;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static javax.management.Query.value;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest {

  Badge validBadge = new Badge("English speaker",
      2,
      3,
      "Hello World! My English is bloody gorgeous.",
      new ArrayList<>(Arrays.asList("balazs.jozsef", "benedek.vamosi", "balazs.barna")));

  Badge inValidBadge = new Badge("English speaker",
      2,
      3,
      "Hello World! My English is bloody gorgeous."
  );

  /*String badgeInJson = "{ \"badgeName\": \"" + "English speaker" + "\", " +
        "\"oldLVL\": \"" + 2 + "\"," +
        "\"pitchedLVL\": \"" + 3 + "\", " +
        "\"pitchMessage\": \"" + "Hello World! My English is bloody gorgeous." + "\", " +
        "\"holders\": [\"balazs.jozsef\", \"balazs.jozsef\", \"balazs.jozsef\"]}";*/

  String token = "token123";

//  @Autowired
//  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setup(){
    this.mockMvc = MockMvcBuilders.standaloneSetup(new MainController()).build();
    //this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void pitchBadgeValidHeaderAndBody() throws Exception {
    this.mockMvc.perform(post("/pitch")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userTokenAuth", token)
        .content(stringify(validBadge)))
        .andExpect(status().isCreated())
        .andReturn();
  }

  @Test
  public void pitchBadgeValidHeaderAndBodyProperMessage() throws Exception {
    this.mockMvc.perform(post("/pitch")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userTokenAuth", token)
        .content(stringify(validBadge)))
        .andExpect(jsonPath("$.message").value("Success"))
        .andReturn();
  }

  @Test
  public void pitchBadgeMissingContentType() throws Exception {
    this.mockMvc.perform(post("/pitch")
        .header("userTokenAuth", token)
        .content(stringify(validBadge)))
        .andExpect(status().isUnsupportedMediaType())
        .andReturn();
  }

  @Test
  public void pitchBadgeInvalidTokenReturnsWithProperStatusCode() throws Exception {
    this.mockMvc.perform(post("/pitch")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userTokenAuth", "")
        .content(stringify(validBadge)))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  public void pitchBadgeInvalidTokenReturnsWithProperErrorField() throws Exception {
    this.mockMvc.perform(post("/pitch")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userTokenAuth", "")
        .content(stringify(validBadge)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andReturn();
  }

  @Test
  public void pitchBadgeInvalidRequestBody() throws Exception {
    this.mockMvc.perform(post("/pitch")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userTokenAuth", token)
        .content(stringify(inValidBadge)))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  private String stringify(Object object) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(object);
  }
}
