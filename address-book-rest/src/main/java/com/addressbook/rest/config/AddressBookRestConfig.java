package com.addressbook.rest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@EnableWebMvc
public class AddressBookRestConfig {

  /**
   * 
   * @return Bean to configurer swagger UI
   */
  @Bean
  public OpenAPI addressBookOpenApi() {
    return new OpenAPI().info(
        new Info().title("Addressbook API").description("Addressbook Application").version("v1.0.0")
            .license(new License().name("MIT").url("https://opensource.org/licenses/Apache-2.0")));
  }

}
