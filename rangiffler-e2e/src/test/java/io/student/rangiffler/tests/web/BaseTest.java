package io.student.rangiffler.tests.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.config.LocalConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BaseTest {
    protected static final Config CFG = LocalConfig.INSTANCE;

    @BeforeAll
    static void setUp() {
        Configuration.browser = CFG.browser();
        Configuration.browserSize = CFG.browserSize();
        if ("firefox".equals(Configuration.browser)) {
            // Keep screenshots in CSS pixels regardless of the display's HiDPI scale.
            Configuration.browserCapabilities = new FirefoxOptions()
                    .addPreference("layout.css.devPixelsPerPx", "1.0");
        }
    }

    @BeforeEach
    void clearSession() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
        }
    }

}
