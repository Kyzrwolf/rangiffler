package io.student.rangiffler.config;

import javax.annotation.Nonnull;

public enum LocalConfig implements Config {
  INSTANCE;

  @Override
  @Nonnull
  public String browser() { return "firefox"; }

  @Override
  @Nonnull
  public String browserSize() {
    return "1920x1080";
  }

  @Override
  @Nonnull
  public String frontUrl() {
    return "http://localhost:3001";
  }

  @Override
  @Nonnull
  public String registerUrl() { return "http://localhost:9000/register"; }

  @Override
  @Nonnull
  public String authJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rangiffler-auth?serverTimezone=UTC";
  }

  @Override
  @Nonnull
  public String userdataJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rangiffler-api?serverTimezone=UTC";
  }

  @Override
  @Nonnull
  public String dbUsername() {
    return "root";
  }

  @Override
  @Nonnull
  public String dbPassword() {
    return "secret";
  }

  @Override
  @Nonnull
  public String githubUrl() {
    return "https://api.github.com/";
  }

}
