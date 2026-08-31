# (V&VconTesting)
---

## 🏛️ Arquitectura Estructurada (Page Object Model)

El proyecto utiliza el patrón **Page Object Model (POM)** para abstraer la estructura y manipulación de las páginas web, separando la interacción con la interfaz de la lógica de las pruebas:

- [`RegistroPage`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/pages/RegistroPage.java): Abstracción del formulario de registro y sus selectores DOM.
- [`LoginPage`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/pages/LoginPage.java): Encapsulamiento del flujo de inicio de sesión y gestión de mensajes de error.
- [`DashboardPage`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/pages/DashboardPage.java): Validación del panel principal post-autenticación.
- [`BaseTest`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/BaseTest.java): Clase base que gestiona el ciclo de vida del navegador Chromium, contextos de Playwright, trazado y captura de evidencias.

---

## 📊 Gestión de Calidad y Casos de Prueba

Se cubren múltiples requisitos verificables formalizados en la suite de pruebas E2E ([`PlaywrightE2ETest`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/PlaywrightE2ETest.java) y [`LoginTest`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/src/test/java/org/example/LoginTest.java)):

1. **Navegación y Título**: Verificación del título y estructura inicial en páginas de Login y Registro.
2. **Registro de Usuario Exitoso**: Llenado del formulario de registro con datos válidos.
3. **Redirección Automatizada**: Verificación de redirección a `login.html` tras completar el registro.
4. **Login Exitoso**: Autenticación correcta utilizando credenciales recién registradas.
5. **Redirección al Dashboard**: Verificación de carga de `index.html` post-login.
6. **Validación de Identidad**: Confirmación de despliegue dinámico del nombre de usuario en pantalla (`#userNameDisplay`).
7. **Control de Errores en Login**: Validación de mensajes de error ante credenciales inválidas.
8. **Captura Continuada de Estado**: Generación de screenshots en puntos clave del flujo.

---

## 🔄 CI/CD y Evidencia de Fallos

### GitHub Actions Pipeline
El archivo de flujo de trabajo [`.github/workflows/playwright-tests.yml`](file:///c:/Users/Usuario/IdeaProjects/vvcontesting/.github/workflows/playwright-tests.yml) ejecuta automáticamente la suite de pruebas en cada `push` o `pull_request` a la rama `main`.

### Captura de Evidencias & Trace Viewer
Ante cualquier fallo en la ejecución:
- **Screenshots:** Se almacena una captura del estado exacto de la pantalla en `build/reports/screenshots/`.
- **Playwright Trace Viewer:** Se genera un archivo comprimido de traza `.zip` en `build/reports/traces/` que permite inspeccionar la ejecución paso a paso mediante Playwright Trace Viewer.
- **Artefactos en GitHub Actions:** Ambos reportes se suben automáticamente como artefactos del pipeline con una retención de 30 días.

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- **Java JDK 21** o superior.
- **Gradle Wrapper** (incluido en el proyecto).

### Ejecutar la Aplicación Web
```bash
./gradlew bootRun
```
*La aplicación estará disponible en `http://localhost:8080`.*

### Ejecutar Suite de Pruebas Automatizadas (E2E)
```bash
./gradlew test
```

### Visualizar Traza de Playwright (en caso de fallo)
```bash
npx playwright show-trace build/reports/traces/<nombre_de_prueba>-trace.zip
```
