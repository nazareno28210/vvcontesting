package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

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
        // launch(new BrowserType.LaunchOptions().setHeadless(true)) ejecuta el navegador en segundo plano.
        // Si pones setHeadless(false), verás abrirse la ventana del navegador automáticamente.
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
    void testFlujoCompletoRegistroYLoginConPlaywright() {
        String baseUrl = "http://localhost:" + port;

        // 1. Navegar a la página de Registro
        page.navigate(baseUrl + "/registro.html");
        assertTrue(page.title().contains("Registro"), "El título de la página debe ser Crear Cuenta - Registro");

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
    }
}
