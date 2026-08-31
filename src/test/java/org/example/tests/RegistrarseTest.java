package org.example.tests;

import org.example.BaseTest;
import org.example.pages.RegistroPage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegistrarseTest extends BaseTest {

    @Test
    void testNavegarRegistroYVerificarTitulo() {
        RegistroPage registroPage = new RegistroPage(page);
        registroPage.navegar();
        assertThat(page).hasTitle("Crear Cuenta - Registro");
    }

    @Test
    void testRegistroConContrasenasNoCoincidentes() {
        RegistroPage registroPage = new RegistroPage(page);
        registroPage.navegar();
        registroPage.registrarUsuario(
                "Ana",
                "Gomez",
                "anagomez",
                "ana@correo.com",
                "Password123!",
                "PasswordDiferente123!");
        assertThat(page.locator(".toastify")).isVisible();
    }

    @Test
    void testRegistroExitoso() {
        RegistroPage registroPage = new RegistroPage(page);
        registroPage.navegar();
        registroPage.registrarUsuario(
                "Carlos",
                "Lopez",
                "carlosl",
                "carlos@registro.com",
                "Password123!",
                "Password123!");
        assertThat(page).hasURL("http://localhost:8080/login.html");
    }
}
