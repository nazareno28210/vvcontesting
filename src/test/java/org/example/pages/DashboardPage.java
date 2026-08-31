package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DashboardPage {
    private final Page page;
    private final Locator logoutButton;
    private final Locator userNameDisplay;

    public DashboardPage(Page page) {
        this.page = page;
        this.logoutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cerrar Sesión"));
        this.userNameDisplay = page.locator("#userNameDisplay");
    }

    public void cerrarSesion() {
        logoutButton.click();
    }

    public Locator getUserNameDisplay() {
        return userNameDisplay;
    }

    public Locator getLogoutButton() {
        return logoutButton;
    }
}
