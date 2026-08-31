package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RegistroPage {

    private final Page page;
    private final Locator nombreInput;
    private final Locator apellidoInput;
    private final Locator usuarioInput;
    private final Locator correoInput;
    private final Locator contrasenaInput;
    private final Locator confirmarContrasenaInput;
    private final Locator registrarButton;

    public RegistroPage(Page page) {
        this.page = page;
        this.nombreInput = page.locator("#nombre");
        this.apellidoInput = page.locator("#apellido");
        this.usuarioInput = page.locator("#nombreDeUsuario");
        this.correoInput = page.locator("#correo");
        this.contrasenaInput = page.locator("#contrasena");
        this.confirmarContrasenaInput = page.locator("#confirmarContrasena");
        this.registrarButton = page.locator(".btn-registro");
    }

    public void navigate(String baseUrl) {
        page.navigate(baseUrl + "/registro.html");
    }

    public void registrarUsuario(String nombre, String apellido, String usuario, String correo, String contrasena) {
        nombreInput.fill(nombre);
        apellidoInput.fill(apellido);
        usuarioInput.fill(usuario);
        correoInput.fill(correo);
        contrasenaInput.fill(contrasena);
        confirmarContrasenaInput.fill(contrasena);
        registrarButton.click();
    }
}
