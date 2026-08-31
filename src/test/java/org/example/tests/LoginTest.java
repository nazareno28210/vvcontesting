package org.example.tests;

import org.example.BaseTest;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.example.dto.RegistroRequestDto;
import org.example.pages.LoginPage;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.pages.RegistroPage;

import org.example.pages.DashboardPage;

public class LoginTest extends BaseTest {

    @Autowired
    private AuthService authService;

    @Test
    void testNavegarLoginYVerificarTitulo() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navegar();
        assertThat(page).hasTitle("Iniciar Sesión");
    }

    @Test
    void testLoginConCredencialesInvalidas() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navegar();
        loginPage.iniciarSesion(
                "usuario_inexistente@correo.com",
                "ClaveIncorrecta123!");
        assertThat(page.locator(".toastify")).isVisible();
    }

    @Test
    void testLoginExitoso() {
        RegistroRequestDto dto = new RegistroRequestDto();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setNombreDeUsuario("juanperezlogin");
        dto.setCorreo("juanperez@login.com");
        dto.setContrasena("Password123!");
        dto.setConfirmarContrasena("Password123!");
        authService.registrar(dto);

        LoginPage loginPage = new LoginPage(page);
        loginPage.navegar();
        loginPage.iniciarSesion("juanperez@login.com", "Password123!");

        assertThat(page).hasURL("http://localhost:8080/index.html");
    }

    @Test
    void testRegistroYLuegoLogin() {
        RegistroPage registroPage = new RegistroPage(page);
        registroPage.navegar();
        registroPage.registrarUsuario(
                "Pedro",
                "Gomez",
                "pedrogomez",
                "pedro@e2e.com",
                "Password123!",
                "Password123!");

        assertThat(page).hasURL("http://localhost:8080/login.html");

        LoginPage loginPage = new LoginPage(page);
        loginPage.iniciarSesion(
                "pedro@e2e.com",
                "Password123!");
        assertThat(page).hasURL("http://localhost:8080/index.html");

        DashboardPage dashboardPage = new DashboardPage(page);
        dashboardPage.cerrarSesion();
        assertThat(page).hasURL("http://localhost:8080/login.html");
    }
}
