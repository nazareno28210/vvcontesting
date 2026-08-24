package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Paths;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class BaseTest {

    @LocalServerPort
    protected int port;

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    void setUpBase() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        
        context = browser.newContext();
        
        // Iniciar Tracing para capturar screenshots, snapshots y fuentes
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
    }

    @AfterEach
    void tearDownBase() {
        // En caso de querer guardar siempre o al finalizar la prueba
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext extensionContext, Throwable cause) {
            String testName = extensionContext.getDisplayName().replaceAll("[^a-zA-Z0-9_-]", "_");
            
            try {
                // Guardar captura de pantalla al fallar
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("build/reports/screenshots/" + testName + "-failure.png"))
                        .setFullPage(true));

                // Guardar archivo de traza ZIP
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("build/reports/traces/" + testName + "-trace.zip")));
            } catch (Exception e) {
                System.err.println("No se pudo capturar la evidencia del fallo: " + e.getMessage());
            }
        }
    };
}
