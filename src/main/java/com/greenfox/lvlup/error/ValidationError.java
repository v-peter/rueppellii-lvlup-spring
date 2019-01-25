package com.greenfox.lvlup.error;


public class ValidationError {
  String error;

  public ValidationError(String error) {
    this.error = error;
  }

  public ValidationError() {
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
