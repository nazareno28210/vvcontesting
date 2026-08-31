document.addEventListener('DOMContentLoaded', () => {
    const registroForm = document.getElementById('registroForm');

    const nombreInput = document.getElementById('nombre');
    const apellidoInput = document.getElementById('apellido');
    const nombreDeUsuarioInput = document.getElementById('nombreDeUsuario');
    const correoInput = document.getElementById('correo');
    const contrasenaInput = document.getElementById('contrasena');
    const confirmarContrasenaInput = document.getElementById('confirmarContrasena');

    const reqLength = document.getElementById('reqLength');
    const reqUppercase = document.getElementById('reqUppercase');
    const reqLowercase = document.getElementById('reqLowercase');
    const reqNumber = document.getElementById('reqNumber');
    const reqSpecial = document.getElementById('reqSpecial');
    const reqMatch = document.getElementById('reqMatch');

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

    function marcarEstado(input, esValido) {
        if (input.value.trim() === '') {
            input.classList.remove('invalid', 'valid');
            return;
        }
        if (esValido) {
            input.classList.remove('invalid');
            input.classList.add('valid');
        } else {
            input.classList.remove('valid');
            input.classList.add('invalid');
        }
    }

    function validarNombre() {
        const val = nombreInput.value.trim();
        const esValido = val.length > 0 && !/\d/.test(val);
        marcarEstado(nombreInput, esValido);
        return esValido;
    }

    function validarApellido() {
        const val = apellidoInput.value.trim();
        const esValido = val.length > 0 && !/\d/.test(val);
        marcarEstado(apellidoInput, esValido);
        return esValido;
    }

    function validarUsuario() {
        const val = nombreDeUsuarioInput.value.trim();
        const esValido = val.length >= 5;
        marcarEstado(nombreDeUsuarioInput, esValido);
        return esValido;
    }

    function validarCorreo() {
        const val = correoInput.value.trim();
        const esValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
        marcarEstado(correoInput, esValido);
        return esValido;
    }

    function validarContrasena() {
        const pass = contrasenaInput.value;
        const confirmPass = confirmarContrasenaInput.value;

        const cumpleLongitud = pass.length >= 8;
        const tieneMayuscula = /[A-Z]/.test(pass);
        const tieneMinuscula = /[a-z]/.test(pass);
        const tieneNumero = /[0-9]/.test(pass);
        const tieneEspecial = /[^a-zA-Z0-9]/.test(pass);
        const coinciden = pass.length > 0 && pass === confirmPass;

        actualizarRequisito(reqLength, cumpleLongitud, 'Mínimo 8 caracteres');
        actualizarRequisito(reqUppercase, tieneMayuscula, 'Al menos una mayúscula');
        actualizarRequisito(reqLowercase, tieneMinuscula, 'Al menos una minúscula');
        actualizarRequisito(reqNumber, tieneNumero, 'Al menos un número');
        actualizarRequisito(reqSpecial, tieneEspecial, 'Al menos un carácter especial (!@#$%...)');
        actualizarRequisito(reqMatch, coinciden, 'Las contraseñas coinciden');

        const todoValido = cumpleLongitud && tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;

        marcarEstado(contrasenaInput, todoValido);
        marcarEstado(confirmarContrasenaInput, coinciden);

        return todoValido && coinciden;
    }

    function actualizarRequisito(elem, cumplido, texto) {
        if (elem) {
            if (cumplido) {
                elem.innerText = '✓ ' + texto;
                elem.classList.add('met');
            } else {
                elem.innerText = '✕ ' + texto;
                elem.classList.remove('met');
            }
        }
    }

    nombreInput.addEventListener('input', validarNombre);
    apellidoInput.addEventListener('input', validarApellido);
    nombreDeUsuarioInput.addEventListener('input', validarUsuario);
    correoInput.addEventListener('input', validarCorreo);
    contrasenaInput.addEventListener('input', validarContrasena);
    confirmarContrasenaInput.addEventListener('input', validarContrasena);

    registroForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const nombreOk = validarNombre();
        const apellidoOk = validarApellido();
        const usuarioOk = validarUsuario();
        const correoOk = validarCorreo();
        const contrasenaOk = validarContrasena();

        if (!nombreOk || !apellidoOk || !usuarioOk || !correoOk || !contrasenaOk) {
            mostrarNotificacion('Por favor, corrige los campos marcados en rojo antes de continuar.', 'error');
            return;
        }

        const payload = {
            nombre: nombreInput.value.trim(),
            apellido: apellidoInput.value.trim(),
            nombreDeUsuario: nombreDeUsuarioInput.value.trim(),
            correo: correoInput.value.trim(),
            contrasena: contrasenaInput.value.trim(),
            confirmarContrasena: confirmarContrasenaInput.value.trim()
        };

        try {
            const response = await axios.post('/api/auth/registro', payload);

            if (response.status === 201 || response.data) {
                mostrarNotificacion('¡Registro completado exitosamente! Ya puedes iniciar sesión.', 'success');
                registroForm.reset();
                [nombreInput, apellidoInput, nombreDeUsuarioInput, correoInput, contrasenaInput, confirmarContrasenaInput].forEach(input => input.classList.remove('valid', 'invalid'));
                actualizarRequisito(reqLength, false, 'Mínimo 6 caracteres');
                actualizarRequisito(reqMatch, false, 'Las contraseñas coinciden');
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 1800);
            }
        } catch (error) {
            const mensaje = error.response && error.response.data && error.response.data.mensaje
                ? error.response.data.mensaje
                : 'Ocurrió un error al registrar la cuenta.';
            mostrarNotificacion(mensaje, 'error');
        }
    });

    function mostrarNotificacion(mensaje, tipo = 'error') {
        Toastify({
            text: mensaje,
            duration: 3500,
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
