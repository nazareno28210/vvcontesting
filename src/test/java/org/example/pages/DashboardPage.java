package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DashboardPage {

    private final Page page;
    private final Locator userNameDisplay;
    private final Locator welcomeSection;
    private final Locator logoutButton;

    public DashboardPage(Page page) {
        this.page = page;
        this.userNameDisplay = page.locator("#userNameDisplay");
        this.welcomeSection = page.locator("#welcomeSection");
        this.logoutButton = page.locator("#logoutBtn");
    }

    public String obtenerNombreBienvenida() {
        userNameDisplay.waitFor();
        return userNameDisplay.innerText();
    }

    public boolean isWelcomeVisible() {
        welcomeSection.waitFor();
        return welcomeSection.isVisible();
    }

    public void clickCerrarSesion() {
        logoutButton.click();
    }
}
