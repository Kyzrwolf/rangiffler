package io.student.rangiffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String browser();

  String frontUrl();

  String registerUrl();

  String authJdbcUrl();

  String userdataJdbcUrl();

  String dbUsername();

  String dbPassword();

  String githubUrl();
}
