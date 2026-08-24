document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const alertMessage = document.getElementById('alertMessage');
    const loginSection = document.getElementById('loginSection');
    const welcomeSection = document.getElementById('welcomeSection');
    const userNameDisplay = document.getElementById('userNameDisplay');
    const logoutBtn = document.getElementById('logoutBtn');

    // Verificar si el usuario ya está autenticado
    const usuarioGuardado = localStorage.getItem('nombreDeUsuario');
    if (usuarioGuardado) {
        mostrarBienvenida(usuarioGuardado);
    }

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const correo = document.getElementById('correo').value.trim();
        const contrasena = document.getElementById('contrasena').value.trim();

        ocultarAlerta();

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ correo, contrasena })
            });

            const data = await response.json();

            if (response.ok && data.token) {
                localStorage.setItem('jwtToken', data.token);
                const nombreUsuario = data.nombreDeUsuario || correo;
                localStorage.setItem('nombreDeUsuario', nombreUsuario);
                
                mostrarBienvenida(nombreUsuario);
            } else {
                mostrarAlerta('error', data.mensaje || 'Credenciales incorrectas o error en el servidor.');
            }
        } catch (error) {
            mostrarAlerta('error', 'No se pudo conectar con el servidor. Inténtalo nuevamente.');
        }
    });

    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('nombreDeUsuario');
            
            welcomeSection.style.display = 'none';
            loginSection.style.display = 'block';
            loginForm.reset();
            ocultarAlerta();
        });
    }

    function mostrarBienvenida(nombreUsuario) {
        userNameDisplay.innerText = nombreUsuario;
        loginSection.style.display = 'none';
        welcomeSection.style.display = 'block';
    }

    function mostrarAlerta(tipo, mensaje) {
        alertMessage.innerText = mensaje;
        alertMessage.className = 'alert-message ' + (tipo === 'success' ? 'alert-success' : 'alert-error');
        alertMessage.style.display = 'block';
    }

    function ocultarAlerta() {
        alertMessage.style.display = 'none';
        alertMessage.innerText = '';
    }
});
