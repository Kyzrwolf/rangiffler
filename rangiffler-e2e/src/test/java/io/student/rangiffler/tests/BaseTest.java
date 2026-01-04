package io.student.rangiffler.tests;

import com.codeborne.selenide.Configuration;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.config.LocalConfig;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    private static final Config CFG = LocalConfig.INSTANCE;

    @BeforeAll
    static void setUp() {
        Configuration.browser = CFG.browser();
    }

}
