package io.student.rangiffler.config;

public enum LocalConfig implements Config {
  INSTANCE;

  @Override
  public String browser() { return "firefox"; }

  @Override
  public String frontUrl() {
    return "http://localhost:3001";
  }

  @Override
  public String registerUrl() { return "http://localhost:9000/register"; }

  @Override
  public String authJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rangiffler-auth?serverTimezone=UTC";
  }

  @Override
  public String userdataJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rangiffler-api?serverTimezone=UTC";
  }

  @Override
  public String dbUsername() {
    return "root";
  }

  @Override
  public String dbPassword() {
    return "secret";
  }

  @Override
  public String githubUrl() {
    return "https://api.github.com/";
  }

}
