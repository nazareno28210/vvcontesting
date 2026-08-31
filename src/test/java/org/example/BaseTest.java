package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(BaseTest.TestResultLogger.class)
public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    private static final ThreadLocal<Page> activePage = new ThreadLocal<>();

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        activePage.set(page);
    }

    @AfterEach
    void closeContext(TestInfo testInfo) {
        activePage.remove();
        if (context != null) {
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("build/traces/trace-" + testInfo.getDisplayName() + ".zip")));
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    public static class TestResultLogger implements TestWatcher {
        @Override
        public void testFailed(ExtensionContext extensionContext, Throwable cause) {
            Page page = activePage.get();
            if (page != null && !page.isClosed()) {
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("build/screenshots/failure-" + extensionContext.getDisplayName() + ".png")));
            }
        }
    }
}
