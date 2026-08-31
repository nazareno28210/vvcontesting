document.addEventListener('DOMContentLoaded', async () => {
    const dashboardContainer = document.getElementById('dashboardContainer');
    const userNameDisplay = document.getElementById('userNameDisplay');
    const avatarInitial = document.getElementById('avatarInitial');
    const userHandle = document.getElementById('userHandle');
    const userEmail = document.getElementById('userEmail');
    const infoNickname = document.getElementById('infoNickname');
    const infoEmail = document.getElementById('infoEmail');
    const infoRoles = document.getElementById('infoRoles');
    const logoutBtn = document.getElementById('logoutBtn');

    const token = localStorage.getItem('jwtToken');

    // Protección de ruta: Si no hay token, redirigir inmediatamente a login.html
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    try {
        // Verificar validez del token y obtener perfil del usuario
        const response = await axios.get('/api/auth/me', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        const data = response.data;
        const nombre = data.nombre || '';
        const apellido = data.apellido || '';
        const nombreDeUsuario = data.nombreDeUsuario || 'usuario';
        const correo = data.correo || '';
        const roles = data.roles ? (Array.isArray(data.roles) ? data.roles.join(', ') : Array.from(data.roles).join(', ')) : 'USUARIO';

        const nombreCompleto = (nombre || apellido) ? `${nombre} ${apellido}`.trim() : nombreDeUsuario;

        userNameDisplay.innerText = nombreCompleto;
        avatarInitial.innerText = (nombre ? nombre.charAt(0) : nombreDeUsuario.charAt(0)).toUpperCase();
        userHandle.innerText = '@' + nombreDeUsuario;
        userEmail.innerText = correo;
        infoNickname.innerText = nombreDeUsuario;
        infoEmail.innerText = correo;
        infoRoles.innerText = roles;

        dashboardContainer.style.display = 'block';

    } catch (error) {
        // Si el token expiró o es inválido, limpiar almacenamiento y redirigir
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('nombreDeUsuario');
        localStorage.removeItem('nombre');
        localStorage.removeItem('apellido');
        window.location.href = 'login.html';
    }

    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.clear();
            Toastify({
                text: "Sesión cerrada correctamente",
                duration: 2000,
                gravity: "bottom",
                position: "center",
                style: {
                    background: "linear-gradient(to right, #3b82f6, #1d4ed8)",
                    borderRadius: "8px"
                }
            }).showToast();

            setTimeout(() => {
                window.location.href = 'login.html';
            }, 500);
        });
    }
});
