package io.student.rangiffler.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.config.LocalConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    private static final Config CFG = LocalConfig.INSTANCE;

    @BeforeAll
    static void setUp() {
        Configuration.browser = CFG.browser();
    }

    @BeforeEach
    void clearSession() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
        }
    }

}
