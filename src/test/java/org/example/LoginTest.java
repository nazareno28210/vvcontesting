package org.example;

import com.microsoft.playwright.Page;
import org.example.pages.DashboardPage;
import org.example.pages.LoginPage;
import org.example.pages.RegistroPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseTest {

    @Test
    @DisplayName("Verificar título de la página de Login")
    void test_verificarTituloLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(getBaseUrl());
        
        assertTrue(loginPage.obtenerTitulo().contains("Iniciar Sesión"),
                "El título debe contener 'Iniciar Sesión'");
    }

    @Test
    @DisplayName("Test de Login Exitoso con POM")
    void test_loginExitoso() {
        // 1. Registrar usuario previo
        RegistroPage registroPage = new RegistroPage(page);
        registroPage.navigate(getBaseUrl());
        registroPage.registrarUsuario("Ana", "Lopez", "analopez", "ana@ejemplo.com", "ClaveSegura123");

        // Esperar redirección a login.html
        page.waitForURL("**/login.html");

        // 2. Realizar Login mediante LoginPage POM
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("ana@ejemplo.com", "ClaveSegura123");

        // 3. Validar vista con DashboardPage POM
        DashboardPage dashboardPage = new DashboardPage(page);
        assertTrue(dashboardPage.isWelcomeVisible(), "El panel de bienvenida debe estar visible");
        assertEquals("analopez", dashboardPage.obtenerNombreBienvenida(), "El nombre de usuario recibido debe ser analopez");
    }

    @Test
    @DisplayName("Test de Login Fallido con credenciales incorrectas")
    void test_loginFallido() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate(getBaseUrl());
        loginPage.login("usuario_inexistente@ejemplo.com", "ClaveErronea999");

        String mensajeError = loginPage.obtenerMensajeError();
        assertNotNull(mensajeError, "Debe mostrarse un mensaje de error");
        assertTrue(mensajeError.contains("incorrect") || mensajeError.contains("inválid") || mensajeError.contains("error"),
                "El mensaje de error debe indicar credenciales incorrectas");
    }
}
