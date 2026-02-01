package io.student.rangiffler.model;

import java.util.Objects;

public class CountryInput {
  private String code;

  public CountryInput() {
  }

  public CountryInput(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "CountryInput{code='" + code + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CountryInput that = (CountryInput) o;
    return Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String code;

    public CountryInput build() {
      CountryInput result = new CountryInput();
      result.code = this.code;
      return result;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }
  }
}
