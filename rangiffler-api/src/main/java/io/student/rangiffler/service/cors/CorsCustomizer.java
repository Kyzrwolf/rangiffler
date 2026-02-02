package io.student.rangiffler.service.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CorsCustomizer {

  private final String rangifflerFronUrl;

  @Autowired
  public CorsCustomizer(@Value("${rangiffler-front.base-uri}") String rangifflerFrontUrl) {
    this.rangifflerFronUrl = rangifflerFrontUrl;
  }

  public void corsCustomizer(HttpSecurity http) throws Exception {
    http.cors(c -> {
      CorsConfigurationSource source = s -> {
        CorsConfiguration cc = new CorsConfiguration();
        cc.setAllowCredentials(true);
        cc.setAllowedOrigins(List.of(rangifflerFronUrl));
        cc.setAllowedHeaders(List.of("*"));
        cc.setAllowedMethods(List.of("*"));
        return cc;
      };

      c.configurationSource(source);
    });
  }
}
