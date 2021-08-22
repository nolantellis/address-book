package com.addressbook.core.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class CoreConfig {

  @Bean
  public Mapper beanMapper() {
    log.info("Dozer Plugin Initialise");
    return DozerBeanMapperBuilder.buildDefault();
  }

}
