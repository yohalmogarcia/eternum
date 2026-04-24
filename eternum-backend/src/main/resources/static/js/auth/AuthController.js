/**
 * AuthController - Maneja la autenticación de usuarios
 * Ruta: static/js/auth/AuthController.js
 */

const AuthController = {
    
    baseUrl: '/eternum/api/users',
    
    /**
     * Iniciar sesión
     */
    login: async function(email, password) {
        try {
            const response = await fetch(`${this.baseUrl}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Guardar token en localStorage
                localStorage.setItem('eternum_token', data.data.token);
                localStorage.setItem('eternum_user', JSON.stringify(data.data.user));
                // Actualizar header antes de redirigir (si estamos en la misma página)
                if (typeof updateHeaderAuth === 'function') {
                    updateHeaderAuth();
                }
                // Verificar si hay una URL guardada para redirigir después del login
                const redirectUrl = sessionStorage.getItem('redirectAfterLogin');
                if (redirectUrl) {
                    sessionStorage.removeItem('redirectAfterLogin');
                    window.location.href = redirectUrl;
                } else {
                    window.location.href = '/eternum';
                }
            } else {
                this.showError(data.message);
            }
            
            return data;
        } catch (error) {
            console.error('Login error:', error);
            this.showError('Error al iniciar sesión. Intente nuevamente.');
            return null;
        }
    },
    
    /**
     * Registrar nuevo usuario
     */
    register: async function(userData) {
        try {
            const response = await fetch(`${this.baseUrl}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });
            
            const data = await response.json();
            
            if (data.success) {
                this.showSuccess('Cuenta creada exitosamente. Por favor verifica tu email.');
                setTimeout(() => {
                    window.location.href = '/login';
                }, 2000);
            } else {
                this.showError(data.message);
            }
            
            return data;
        } catch (error) {
            console.error('Register error:', error);
            this.showError('Error al crear la cuenta. Intente nuevamente.');
            return null;
        }
    },
    
    /**
     * Solicitar recuperación de contraseña
     */
    requestPasswordReset: async function(email) {
        try {
            const response = await fetch(`${this.baseUrl}/password-reset-request`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email })
            });
            
            const data = await response.json();
            
            if (data.success) {
                this.showSuccess('Se han enviado las instrucciones a tu email.');
            } else {
                this.showError(data.message);
            }
            
            return data;
        } catch (error) {
            console.error('Password reset request error:', error);
            this.showError('Error al procesar la solicitud.');
            return null;
        }
    },
    
    /**
     * Verificar email con token
     */
    verifyEmail: async function(token) {
        try {
            const response = await fetch(`${this.baseUrl}/verify-email?token=${token}`, {
                method: 'GET'
            });
            
            const data = await response.json();
            
            if (data.success) {
                this.showSuccess('Email verificado exitosamente.');
            } else {
                this.showError(data.message);
            }
            
            return data;
        } catch (error) {
            console.error('Email verification error:', error);
            this.showError('Error al verificar el email.');
            return null;
        }
    },
    
    /**
     * Cerrar sesión
     */
    logout: function() {
        localStorage.removeItem('eternum_token');
        localStorage.removeItem('eternum_user');
        window.location.href = '/eternum';
    },
    
    /**
     * Obtener usuario actual
     */
    getCurrentUser: function() {
        const user = localStorage.getItem('eternum_user');
        return user ? JSON.parse(user) : null;
    },
    
    /**
     * Verificar si está autenticado
     */
    isAuthenticated: function() {
        return !!localStorage.getItem('eternum_token');
    },
    
    /**
     * Mostrar mensaje de error
     */
    showError: function(message) {
        const errorDiv = document.getElementById('errorMessage');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.classList.remove('hidden');
            setTimeout(() => errorDiv.classList.add('hidden'), 5000);
        }
    },
    
    /**
     * Mostrar mensaje de éxito
     */
    showSuccess: function(message) {
        const successDiv = document.getElementById('successMessage');
        if (successDiv) {
            successDiv.textContent = message;
            successDiv.classList.remove('hidden');
            setTimeout(() => successDiv.classList.add('hidden'), 5000);
        }
    }
};

/**
 * Handler para formulario de registro
 */
function handleRegister(event) {
    event.preventDefault();
    
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (password !== confirmPassword) {
        AuthController.showError('Las contraseñas no coinciden');
        return false;
    }
    
    const userData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        password: password,
        hasConsentPrivacy: document.getElementById('hasConsentPrivacy').checked
    };
    
    const btn = document.getElementById('registerBtn');
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Creando cuenta...';
    
    AuthController.register(userData).finally(() => {
        btn.disabled = false;
        btn.innerHTML = '<i class="fas fa-user-plus mr-2"></i>Crear Cuenta';
    });
    
    return false;
}

/**
 * Mostrar modal de recuperación de contraseña
 */
function showPasswordReset() {
    const email = prompt('Ingresa tu correo electrónico para recuperar tu contraseña:');
    if (email) {
        AuthController.requestPasswordReset(email);
    }
}

/**
 * Login social (placeholder)
 */
function socialLogin(provider) {
    alert(`Login con ${provider} - Funcionalidad en desarrollo`);
}

/**
 * Registro social (placeholder)
 */
function socialRegister(provider) {
    alert(`Registro con ${provider} - Funcionalidad en desarrollo`);
}

/**
 * Handler para formulario de login
 */
async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const btn = document.getElementById('loginBtn');
    
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Ingresando...';
    
    await AuthController.login(email, password);
    
    btn.disabled = false;
    btn.innerHTML = 'Ingresar';
    
    return false;
}
