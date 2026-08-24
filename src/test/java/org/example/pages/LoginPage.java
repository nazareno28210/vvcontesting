package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;
    private final Locator correoInput;
    private final Locator contrasenaInput;
    private final Locator ingresarButton;
    private final Locator alertMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.correoInput = page.locator("#correo");
        this.contrasenaInput = page.locator("#contrasena");
        this.ingresarButton = page.locator(".btn-login");
        this.alertMessage = page.locator("#alertMessage");
    }

    public void navigate(String baseUrl) {
        page.navigate(baseUrl + "/login.html");
    }

    public String obtenerTitulo() {
        return page.title();
    }

    public void llenarCorreo(String correo) {
        correoInput.fill(correo);
    }

    public void llenarContrasena(String contrasena) {
        contrasenaInput.fill(contrasena);
    }

    public void clickIngresar() {
        ingresarButton.click();
    }

    public void login(String correo, String contrasena) {
        llenarCorreo(correo);
        llenarContrasena(contrasena);
        clickIngresar();
    }

    public String obtenerMensajeError() {
        alertMessage.waitFor();
        return alertMessage.innerText();
    }
}
