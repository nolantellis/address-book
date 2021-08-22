package com.addressbook.db;

import com.addressbook.db.config.DbConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbConfig.class)
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public abstract class AbstractBaseTest {

}
