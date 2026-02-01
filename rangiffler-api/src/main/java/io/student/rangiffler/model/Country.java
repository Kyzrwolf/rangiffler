package io.student.rangiffler.model;

import java.util.Objects;

public class Country {
  private String flag;

  private String code;

  private String name;

  public Country() {
  }

  public Country(String flag, String code, String name) {
    this.flag = flag;
    this.code = code;
    this.name = name;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Country{flag='" + flag + "', code='" + code + "', name='" + name + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Country that = (Country) o;
    return Objects.equals(flag, that.flag) &&
        Objects.equals(code, that.code) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flag, code, name);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String flag;

    private String code;

    private String name;

    public Country build() {
      Country result = new Country();
      result.flag = this.flag;
      result.code = this.code;
      result.name = this.name;
      return result;
    }

    public Builder flag(String flag) {
      this.flag = flag;
      return this;
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }
  }
}
