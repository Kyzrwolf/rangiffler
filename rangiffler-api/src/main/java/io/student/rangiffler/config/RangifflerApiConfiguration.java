package io.student.rangiffler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.student.rangiffler.service.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.text.SimpleDateFormat;

import static org.apache.tomcat.util.http.Method.POST;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@Profile({"local", "docker", "staging"})
public class RangifflerApiConfiguration {

  private final CorsCustomizer corsCustomizer;

  @Autowired
  public RangifflerApiConfiguration(CorsCustomizer corsCustomizer) {
    this.corsCustomizer = corsCustomizer;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    corsCustomizer.corsCustomizer(http);

    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(customizer ->
            customizer
                .requestMatchers(HttpMethod.POST, "/graphql").permitAll()
                .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  public ObjectMapper objectMapper() {
    var objectMapper = new ObjectMapper();
    var dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssZ");
    objectMapper.setDateFormat(dateFormat);
    return objectMapper;
  }
}
