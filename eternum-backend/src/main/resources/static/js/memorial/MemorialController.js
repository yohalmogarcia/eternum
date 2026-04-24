/**
 * MemorialController - Maneja las interacciones con memoriales
 * Ruta: static/js/memorial/MemorialController.js
 */

const MemorialController = {
    
    baseUrl: '/eternum/api/memorials',
    
    /**
     * Crear un nuevo memorial
     */
    create: async function(memorialData) {
        try {
            const token = localStorage.getItem('eternum_token');
            const userData = localStorage.getItem('eternum_user');
            const user = userData ? JSON.parse(userData) : null;
            
            const response = await fetch(`${this.baseUrl}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token ? `Bearer ${token}` : '',
                    'X-User-Email': user ? user.email : ''
                },
                body: JSON.stringify(memorialData)
            });
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Create memorial error:', error);
            return { success: false, message: 'Error al crear el memorial' };
        }
    },
    
    /**
     * Obtener memorial por ID
     */
    getById: async function(memorialId) {
        try {
            const response = await fetch(`${this.baseUrl}/${memorialId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Get memorial error:', error);
            return { success: false, message: 'Error al obtener el memorial' };
        }
    },
    
    /**
     * Actualizar memorial existente
     */
    update: async function(memorialId, memorialData) {
        try {
            const token = localStorage.getItem('eternum_token');
            const userData = localStorage.getItem('eternum_user');
            const user = userData ? JSON.parse(userData) : null;
            
            const response = await fetch(`${this.baseUrl}/${memorialId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token ? `Bearer ${token}` : '',
                    'X-User-Email': user ? user.email : ''
                },
                body: JSON.stringify(memorialData)
            });
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Update memorial error:', error);
            return { success: false, message: 'Error al actualizar el memorial' };
        }
    },
    
    /**
     * Encender una vela virtual
     */
    lightCandle: async function(memorialId, message = '') {
        try {
            const response = await fetch(`${this.baseUrl}/${memorialId}/candles`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ fkMemorial: memorialId, candleMessage: message })
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Actualizar contador
                const countElement = document.getElementById('candleCount');
                if (countElement) {
                    countElement.textContent = parseInt(countElement.textContent) + 1;
                }
                
                // Animación del botón
                const btn = document.getElementById('lightCandleBtn');
                if (btn) {
                    btn.innerHTML = '<i class="fas fa-check mr-2"></i>Vela Encendida';
                    btn.disabled = true;
                    btn.style.opacity = '0.7';
                    
                    setTimeout(() => {
                        btn.innerHTML = '<i class="fas fa-fire mr-2"></i>Encender una Vela Virtual';
                        btn.disabled = false;
                        btn.style.opacity = '1';
                    }, 3000);
                }
                
                this.showNotification('Has encendido una vela virtual 🕯️', 'success');
            } else {
                this.showNotification(data.message, 'error');
            }
            
            return data;
        } catch (error) {
            console.error('Light candle error:', error);
            this.showNotification('Error al encender la vela. Intente nuevamente.', 'error');
            return null;
        }
    },
    
    /**
     * Enviar condolencia
     */
    submitCondolence: async function(memorialId, authorName, authorEmail, message) {
        try {
            const response = await fetch(`${this.baseUrl}/${memorialId}/condolences`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    fkMemorial: memorialId,
                    authorName: authorName,
                    authorEmail: authorEmail,
                    message: message
                })
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Limpiar formulario
                document.getElementById('condolenceMessage').value = '';
                
                // Recargar condolencias
                this.loadCondolences(memorialId);
                
                this.showNotification('Tu mensaje ha sido publicado 💐', 'success');
            } else {
                this.showNotification(data.message, 'error');
            }
            
            return data;
        } catch (error) {
            console.error('Submit condolence error:', error);
            this.showNotification('Error al publicar el mensaje. Intente nuevamente.', 'error');
            return null;
        }
    },
    
    /**
     * Cargar condolencias
     */
    loadCondolences: async function(memorialId) {
        try {
            const response = await fetch(`${this.baseUrl}/${memorialId}/condolences`, {
                method: 'GET'
            });
            
            const data = await response.json();
            
            if (data.success && data.data) {
                this.renderCondolences(data.data);
            }
            
            return data;
        } catch (error) {
            console.error('Load condolences error:', error);
            return null;
        }
    },
    
    /**
     * Renderizar condolencias en el DOM
     */
    renderCondolences: function(condolences) {
        const container = document.getElementById('condolencesList');
        if (!container) return;
        
        if (condolences.length === 0) {
            container.innerHTML = `
                <div class="text-center py-8 text-gray-500">
                    <i class="fas fa-comment-dots text-4xl mb-2 opacity-50"></i>
                    <p>Aún no hay mensajes. Sé el primero en dejar un recuerdo.</p>
                </div>
            `;
            return;
        }
        
        container.innerHTML = condolences.map(c => `
            <div class="flex gap-4 p-4 bg-gray-50 rounded-lg fade-in">
                <div class="w-10 h-10 bg-[#1A2B3C] rounded-full flex items-center justify-center text-white font-bold flex-shrink-0">
                    ${c.authorName.charAt(0).toUpperCase()}
                </div>
                <div class="flex-grow">
                    <div class="flex justify-between items-start mb-1">
                        <p class="font-bold text-sm text-[#1A2B3C]">${c.authorName}</p>
                        <span class="text-xs text-gray-400">${this.formatDate(c.createdDate)}</span>
                    </div>
                    <p class="text-gray-600 text-sm">${this.escapeHtml(c.message)}</p>
                </div>
            </div>
        `).join('');
    },
    
    /**
     * Buscar memoriales
     */
    searchMemorials: async function(query) {
        try {
            const response = await fetch(`${this.baseUrl}/search?query=${encodeURIComponent(query)}`, {
                method: 'GET'
            });
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Search memorials error:', error);
            return null;
        }
    },
    
    /**
     * Compartir memorial
     */
    share: function(platform, url, title) {
        const shareUrls = {
            facebook: `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}`,
            twitter: `https://twitter.com/intent/tweet?url=${encodeURIComponent(url)}&text=${encodeURIComponent(title)}`,
            whatsapp: `https://wa.me/?text=${encodeURIComponent(title + ' ' + url)}`
        };
        
        if (platform === 'copy') {
            navigator.clipboard.writeText(url).then(() => {
                this.showNotification('Enlace copiado al portapapeles', 'success');
            });
        } else if (shareUrls[platform]) {
            window.open(shareUrls[platform], '_blank', 'width=600,height=400');
        }
    },
    
    /**
     * Suscribirse a notificaciones de aniversario
     */
    subscribeToAnniversary: async function(memorialId) {
        // Placeholder - requiere implementación backend
        alert('Funcionalidad de suscripción a aniversarios - En desarrollo');
    },
    
    /**
     * Formatear fecha
     */
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },
    
    /**
     * Escapar HTML para prevenir XSS
     */
    escapeHtml: function(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },
    
    /**
     * Mostrar notificación
     */
    showNotification: function(message, type = 'info') {
        // Crear elemento de notificación
        const notification = document.createElement('div');
        notification.className = `fixed top-20 right-4 z-50 p-4 rounded-lg shadow-lg fade-in ${
            type === 'success' ? 'bg-green-500 text-white' :
            type === 'error' ? 'bg-red-500 text-white' :
            'bg-[#C9A063] text-white'
        }`;
        notification.innerHTML = `
            <div class="flex items-center gap-2">
                <i class="fas ${type === 'success' ? 'fa-check-circle' : type === 'error' ? 'fa-exclamation-circle' : 'fa-info-circle'}"></i>
                <span>${message}</span>
            </div>
        `;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transition = 'opacity 0.5s';
            setTimeout(() => notification.remove(), 500);
        }, 3000);
    }
};

/**
 * Handler para encender vela
 */
function lightCandle() {
    const memorialId = document.getElementById('memorialId')?.value;
    if (memorialId) {
        MemorialController.lightCandle(memorialId);
    }
}

/**
 * Handler para enviar condolencia
 */
function submitCondolence(event) {
    event.preventDefault();
    
    const memorialId = document.getElementById('memorialId')?.value;
    const authorName = document.getElementById('condolenceAuthor')?.value;
    const authorEmail = document.getElementById('condolenceEmail')?.value;
    const message = document.getElementById('condolenceMessage')?.value;
    
    if (memorialId && authorName && message) {
        MemorialController.submitCondolence(memorialId, authorName, authorEmail, message);
    }
    
    return false;
}

/**
 * Cargar condolencias
 */
function loadCondolences(memorialId) {
    MemorialController.loadCondolences(memorialId);
}

/**
 * Compartir en redes sociales
 */
function shareOn(platform) {
    const url = window.location.href;
    const title = document.title;
    MemorialController.share(platform, url, title);
}

/**
 * Copiar enlace
 */
function copyLink() {
    const url = window.location.href;
    MemorialController.share('copy', url, '');
}

/**
 * Suscribirse a notificaciones de aniversario
 */
function subscribeToAnniversary() {
    const memorialId = document.getElementById('memorialId')?.value;
    if (memorialId) {
        MemorialController.subscribeToAnniversary(memorialId);
    }
}
