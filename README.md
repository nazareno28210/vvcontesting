# V&VconTesting — Automation & E2E Testing Suite

Suite de pruebas end-to-end (E2E) con **Playwright Java**, **JUnit 5** y **Spring Boot Test** enfocada en la validación resiliente del sistema de autenticación.

---

## 🎯 Principios de Testing Resiliente

La arquitectura de pruebas cumple rigurosamente con los 5 pilares de calidad:

1. **Aislamiento por Prueba (`BaseTest`)**: Cada prueba corre en su propio navegador y contexto limpio (`BrowserContext`), evitando contaminación de sesiones o estados de pruebas previas.
2. **Localizadores Accesibles y Resilientes**: Uso prioritario de `getByLabel` y `getByRole` en las clases Page Object para imitar la interacción del usuario real y soportar refactorizaciones de HTML/CSS sin romper las pruebas.
3. **Aserciones Automáticas con Espera Intuitiva**: Empleo de `PlaywrightAssertions` (`assertThat(page).hasTitle()`, `assertThat(page).hasURL()`), eliminando esperas fijas (`Thread.sleep`).
4. **Cobertura Completa (Happy Path & Sad Path)**: Se validan tanto los flujos correctos (registro y login exitoso) como los escenarios de error (contraseñas no coincidentes, credenciales inválidas).
5. **Evidencia Diagnóstica Automática**: Ante fallos, `TestWatcher` captura automáticamente capturas de pantalla (`build/screenshots/`) y trazas completas de Playwright (`build/traces/`).

---

## 🏛️ Arquitectura Page Object Model (POM)

- [`BaseTest`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/BaseTest.java): Configuración del servidor embebido Spring Boot, ciclo de vida del navegador, *Tracing* y captura de pantalla ante fallos.
- [`LoginPage`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/pages/LoginPage.java): Encapsulamiento del formulario de login y sus localizadores accesibles.
- [`RegistroPage`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/pages/RegistroPage.java): Abstracción del formulario de registro de usuario.
- [`LoginTest`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/tests/LoginTest.java): Pruebas E2E del módulo de inicio de sesión.
- [`RegistrarseTest`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/tests/RegistrarseTest.java): Pruebas E2E del módulo de registro.

---

## 🚀 Requisitos y Ejecución de Pruebas

### Prerrequisitos
- **Java JDK 21** o superior.
- **Gradle Wrapper** (incluido en el proyecto).

### Ejecutar Suite Completa de Pruebas
```bash
./gradlew test
```

### Inspección de Evidencias en Caso de Fallo
- **Screenshots de fallos:** `build/screenshots/failure-<nombre_prueba>.png`
- **Trace Viewer de Playwright:** `build/traces/trace-<nombre_prueba>.zip`
```bash
npx playwright show-trace build/traces/trace-testLoginConCredencialesInvalidas().zip
```
