package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import com.microsoft.playwright.Page.GetByLabelOptions;
import com.microsoft.playwright.options.AriaRole;

public class RegistroPage {
    private final Page page;
    private final Locator nombreInput;
    private final Locator apellidoInput;
    private final Locator usuarioInput;
    private final Locator correoInput;
    private final Locator contrasenaInput;
    private final Locator confirmarContrasenaInput;
    private final Locator registroButton;
    private final Locator loginLink;

    public RegistroPage(Page page) {
        this.page = page;
        this.nombreInput = page.getByLabel("Nombre", new GetByLabelOptions().setExact(true));
        this.apellidoInput = page.getByLabel("Apellido");
        this.usuarioInput = page.getByLabel("Nombre de Usuario");
        this.correoInput = page.getByLabel("Correo Electrónico");
        this.contrasenaInput = page.getByLabel("Contraseña", new GetByLabelOptions().setExact(true));
        this.confirmarContrasenaInput = page.getByLabel("Confirmar Contraseña");
        this.registroButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Registrarse"));
        this.loginLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Inicia sesión aquí"));
    }

    public void navegar() {
        page.navigate("http://localhost:8080/registro.html");
    }

    public void registrarUsuario(String nombre, String apellido, String usuario, String correo, String contrasena, String confirmarContrasena) {
        nombreInput.fill(nombre);
        apellidoInput.fill(apellido);
        usuarioInput.fill(usuario);
        correoInput.fill(correo);
        contrasenaInput.fill(contrasena);
        confirmarContrasenaInput.fill(confirmarContrasena);
        registroButton.click();
    }

    public Locator getLoginLink() {
        return loginLink;
    }
}
