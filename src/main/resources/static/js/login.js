document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const alertMessage = document.getElementById('alertMessage');

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
                mostrarAlerta('success', `¡Bienvenido ${data.nombreDeUsuario || ''}! Inicio de sesión exitoso.`);
            } else {
                mostrarAlerta('error', data.mensaje || 'Credenciales incorrectas o error en el servidor.');
            }
        } catch (error) {
            mostrarAlerta('error', 'No se pudo conectar con el servidor. Inténtalo nuevamente.');
        }
    });

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
