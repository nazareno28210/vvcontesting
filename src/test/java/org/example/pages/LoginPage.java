package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import com.microsoft.playwright.Page.GetByLabelOptions;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private final Page page;
    private final Locator correoInput;
    private final Locator contrasenaInput;
    private final Locator loginButton;
    private final Locator alertMessage;
    private final Locator registroLink;

    public LoginPage(Page page) {
        this.page = page;
        this.correoInput = page.getByLabel("Correo Electrónico");
        this.contrasenaInput = page.getByLabel("Contraseña", new GetByLabelOptions().setExact(true));
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ingresar"));
        this.alertMessage = page.locator("#alertMessage");
        this.registroLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Regístrate aquí"));
    }

    public void navegar() {
        page.navigate("http://localhost:8080/login.html");
    }

    public void ingresarCorreo(String correo) {
        correoInput.fill(correo);
    }

    public void ingresarContrasena(String contrasena) {
        contrasenaInput.fill(contrasena);
    }

    public void clickBotonIngresar() {
        loginButton.click();
    }

    public void iniciarSesion(String correo, String contrasena) {
        ingresarCorreo(correo);
        ingresarContrasena(contrasena);
        clickBotonIngresar();
    }

    public Locator getAlertMessage() {
        return alertMessage;
    }

    public Locator getRegistroLink() {
        return registroLink;
    }
}
