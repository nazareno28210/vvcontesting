package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PlaywrightE2ETest {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        // setHeadless(true) ejecuta el navegador en segundo plano.
        // Si se establece en false, se abre la ventana gráfica del navegador.
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void testFlujoCompletoRegistroYLoginConCapturaDePantalla() {
        String baseUrl = "http://localhost:" + port;

        // 1. Navegar a la página de Registro
        page.navigate(baseUrl + "/registro.html");
        assertTrue(page.title().contains("Registro"), "El título de la página debe ser Crear Cuenta - Registro");

        // Captura de pantalla de la página de registro
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("build/captura_registro.png")));

        // 2. Llenar el formulario de Registro simulando un usuario real
        page.fill("#nombre", "Carlos");
        page.fill("#apellido", "Gomez");
        page.fill("#nombreDeUsuario", "carlosg");
        page.fill("#correo", "carlos@ejemplo.com");
        page.fill("#contrasena", "MiClaveSegura123");

        // 3. Hacer clic en el botón de Registrarse
        page.click(".btn-registro");

        // 4. Esperar a que la página se redirija a login.html (después del registro exitoso)
        page.waitForURL("**/login.html", new Page.WaitForURLOptions().setTimeout(5000));
        assertTrue(page.url().contains("login.html"), "Debe redirigir a login.html tras registrarse");

        // Captura de pantalla de la página de login
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("build/captura_login.png")));

        // 5. Llenar el formulario de Login con las credenciales creadas
        page.fill("#correo", "carlos@ejemplo.com");
        page.fill("#contrasena", "MiClaveSegura123");

        // 6. Hacer clic en el botón de Ingresar
        page.click(".btn-login");

        // 7. Validar que aparezca en pantalla el texto de bienvenida con el nombre de usuario
        Locator userNameDisplay = page.locator("#userNameDisplay");
        userNameDisplay.waitFor(new Locator.WaitForOptions().setTimeout(5000));

        assertEquals("carlosg", userNameDisplay.innerText(), "En pantalla debe mostrarse el nombre de usuario 'carlosg'");
        assertTrue(page.isVisible("#welcomeSection"), "El panel de bienvenida debe estar visible en pantalla");

        // 8. Captura de pantalla final de la vista "Bienvenido, carlosg"
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("build/captura_bienvenido.png")));
    }
}
