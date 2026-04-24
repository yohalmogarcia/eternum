/**
 * DeactivationController - Maneja solicitudes de desactivación de cuentas
 * Ruta: static/js/deactivation/DeactivationController.js
 */

const DeactivationController = {
    
    baseUrl: '/api/deactivation-requests',
    
    /**
     * Enviar solicitud de desactivación
     */
    submitRequest: async function(formData) {
        try {
            // Preparar datos
            const requestData = {
                deceasedFullName: formData.get('deceasedFullName'),
                deceasedEmail: formData.get('deceasedEmail'),
                socialMediaPlatform: formData.get('socialMediaPlatform'),
                socialMediaUsername: formData.get('socialMediaUsername'),
                deathCertificateUrl: '', // Se subiría a un servicio de almacenamiento
                proofDocumentUrl: '' // Se subiría a un servicio de almacenamiento
            };
            
            // Simular subida de archivos (en producción usaríamos S3 o similar)
            const deathCertFile = formData.get('deathCertificate');
            const proofDocFile = formData.get('proofDocument');
            
            if (deathCertFile && deathCertFile.name) {
                requestData.deathCertificateUrl = await this.uploadFile(deathCertFile);
            }
            
            if (proofDocFile && proofDocFile.name) {
                requestData.proofDocumentUrl = await this.uploadFile(proofDocFile);
            }
            
            const response = await fetch(this.baseUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });
            
            const data = await response.json();
            
            if (data.success) {
                this.showSuccess('Solicitud enviada exitosamente. Te notificaremos cuando haya actualizaciones.');
                document.getElementById('deactivationForm').reset();
                
                // Limpiar nombres de archivos
                document.getElementById('deathCertName').classList.add('hidden');
                document.getElementById('proofDocName').classList.add('hidden');
            } else {
                this.showError(data.message || 'Error al enviar la solicitud.');
            }
            
            return data;
        } catch (error) {
            console.error('Submit deactivation request error:', error);
            this.showError('Error al procesar la solicitud. Intente nuevamente.');
            return null;
        }
    },
    
    /**
     * Obtener solicitudes del usuario
     */
    getMyRequests: async function() {
        try {
            const response = await fetch(`${this.baseUrl}/my-requests`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('eternum_token') || ''}`
                }
            });
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Get my requests error:', error);
            return null;
        }
    },
    
    /**
     * Subir archivo (placeholder - implementar con servicio real)
     */
    uploadFile: async function(file) {
        // En producción, esto subiría a AWS S3, Cloudinary, etc.
        // Por ahora, simulamos una URL
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(`https://storage.eternum.com/documents/${Date.now()}_${file.name}`);
            }, 500);
        });
    },
    
    /**
     * Obtener texto descriptivo del estado
     */
    getStatusText: function(status) {
        const statuses = {
            'PENDING': { text: 'Pendiente', class: 'bg-yellow-100 text-yellow-800' },
            'IN_REVIEW': { text: 'En Revisión', class: 'bg-blue-100 text-blue-800' },
            'APPROVED': { text: 'Aprobada', class: 'bg-green-100 text-green-800' },
            'REJECTED': { text: 'Rechazada', class: 'bg-red-100 text-red-800' },
            'COMPLETED': { text: 'Completada', class: 'bg-green-100 text-green-800' }
        };
        return statuses[status] || { text: status, class: 'bg-gray-100 text-gray-800' };
    },
    
    /**
     * Mostrar mensaje de error
     */
    showError: function(message) {
        const errorDiv = document.getElementById('errorMessage');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.classList.remove('hidden');
            document.getElementById('successMessage')?.classList.add('hidden');
            
            // Scroll al mensaje
            errorDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
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
            document.getElementById('errorMessage')?.classList.add('hidden');
            
            // Scroll al mensaje
            successDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    }
};

/**
 * Handler para enviar formulario de desactivación
 */
function submitDeactivationRequest(event) {
    event.preventDefault();
    
    const form = document.getElementById('deactivationForm');
    const formData = new FormData(form);
    
    // Validar archivos
    const deathCert = formData.get('deathCertificate');
    if (!deathCert || !deathCert.name) {
        DeactivationController.showError('El certificado de defunción es obligatorio.');
        return false;
    }
    
    // Mostrar loading
    const btn = document.getElementById('submitBtn');
    const originalText = btn.innerHTML;
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>Enviando...';
    
    DeactivationController.submitRequest(formData).finally(() => {
        btn.disabled = false;
        btn.innerHTML = originalText;
    });
    
    return false;
}

/**
 * Handler para selección de archivo
 */
function handleFileSelect(input, displayId) {
    const file = input.files[0];
    const display = document.getElementById(displayId);
    
    if (file && display) {
        // Validar tamaño (10MB)
        if (file.size > 10 * 1024 * 1024) {
            alert('El archivo es demasiado grande. Máximo 10MB.');
            input.value = '';
            return;
        }
        
        // Validar tipo
        const allowedTypes = ['application/pdf', 'image/jpeg', 'image/png', 'image/jpg'];
        if (!allowedTypes.includes(file.type)) {
            alert('Tipo de archivo no permitido. Use PDF, JPG o PNG.');
            input.value = '';
            return;
        }
        
        display.textContent = `Archivo seleccionado: ${file.name} (${(file.size / 1024 / 1024).toFixed(2)} MB)`;
        display.classList.remove('hidden');
    }
}
