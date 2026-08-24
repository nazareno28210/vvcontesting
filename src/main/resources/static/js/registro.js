document.addEventListener('DOMContentLoaded', () => {
    const registroForm = document.getElementById('registroForm');
    const alertMessage = document.getElementById('alertMessage');

    registroForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const nombre = document.getElementById('nombre').value.trim();
        const apellido = document.getElementById('apellido').value.trim();
        const nombreDeUsuario = document.getElementById('nombreDeUsuario').value.trim();
        const correo = document.getElementById('correo').value.trim();
        const contrasena = document.getElementById('contrasena').value.trim();

        ocultarAlerta();

        const payload = {
            nombre,
            apellido,
            nombreDeUsuario,
            correo,
            contrasena
        };

        try {
            const response = await fetch('/api/auth/registro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            const data = await response.json();

            if (response.status === 201 || response.ok) {
                mostrarAlerta('success', '¡Registro completado exitosamente! Ya puedes iniciar sesión.');
                registroForm.reset();
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 1800);
            } else {
                mostrarAlerta('error', data.mensaje || 'Ocurrió un error al registrar la cuenta.');
            }
        } catch (error) {
            mostrarAlerta('error', 'Error al comunicar con el servidor. Inténtalo nuevamente.');
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
