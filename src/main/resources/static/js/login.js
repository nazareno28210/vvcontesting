document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const loginSection = document.getElementById('loginSection');
    const welcomeSection = document.getElementById('welcomeSection');
    const userNameDisplay = document.getElementById('userNameDisplay');
    const logoutBtn = document.getElementById('logoutBtn');

    // Configurar toggle para ver/ocultar contraseña
    document.querySelectorAll('.toggle-password-btn').forEach(button => {
        button.addEventListener('click', () => {
            const input = button.parentElement.querySelector('input');
            const eyeOpen = button.querySelector('.eye-open');
            const eyeClosed = button.querySelector('.eye-closed');

            if (input.type === 'password') {
                input.type = 'text';
                eyeOpen.style.display = 'none';
                eyeClosed.style.display = 'block';
            } else {
                input.type = 'password';
                eyeOpen.style.display = 'block';
                eyeClosed.style.display = 'none';
            }
        });
    });

    // Si ya existe token, redirigir a index.html
    if (localStorage.getItem('jwtToken')) {
        window.location.href = 'index.html';
        return;
    }

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const correo = document.getElementById('correo').value.trim();
        const contrasena = document.getElementById('contrasena').value.trim();

        try {
            const response = await axios.post('/api/auth/login', { correo, contrasena });

            if (response.data && response.data.token) {
                localStorage.setItem('jwtToken', response.data.token);
                localStorage.setItem('nombreDeUsuario', response.data.nombreDeUsuario || correo);
                localStorage.setItem('nombre', response.data.nombre || '');
                localStorage.setItem('apellido', response.data.apellido || '');
                
                mostrarNotificacion('¡Inicio de sesión exitoso!', 'success');
                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 800);
            }
        } catch (error) {
            const mensaje = error.response && error.response.data && error.response.data.mensaje 
                ? error.response.data.mensaje 
                : 'Credenciales incorrectas o error en el servidor.';
            mostrarNotificacion(mensaje, 'error');
        }
    });

    function mostrarNotificacion(mensaje, tipo = 'error') {
        Toastify({
            text: mensaje,
            duration: 3000,
            gravity: "bottom",
            position: "center",
            stopOnFocus: true,
            style: {
                background: tipo === 'success' ? "linear-gradient(to right, #22c55e, #16a34a)" : "linear-gradient(to right, #ef4444, #dc2626)",
                borderRadius: "8px",
                fontSize: "0.9rem",
                boxShadow: "0 4px 12px rgba(0,0,0,0.15)"
            }
        }).showToast();
    }
});
