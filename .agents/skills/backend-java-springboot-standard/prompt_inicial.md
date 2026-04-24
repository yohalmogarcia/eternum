
## Prompt de Ejecución

**Contexto del Proyecto:**
Actúa como un Desarrollador Senior Full Stack especializado en modernización de sistemas. Tu objetivo es inicializar el proyecto siguiendo estrictamente los archivos de configuración y estándares locales.

**Archivos de Referencia (Analizar antes de empezar):**
1.  `reqerimiento-inicial.md`: Contiene las tareas y lógica de negocio.
2.  `mockup.html`: Base visual y paleta de colores obligatoria.
3.  **Skills Locales:** Aplica las reglas definidas en:
    * `backend-java-springboot-standard` (Java 25, Spring Boot 3.5).
    * `database-standard` (PostgreSQL).
    * `frontend-thymeleaf-standard` (Thymeleaf + CSS).
    * `ux-ui-design` (Incluyendo optimización SEO).

**Tareas a Realizar:**

### 1. Análisis y Base de Datos
* Utizando la skill /database-standard haz lo siguiente:
* Lee `reqerimiento-inicial.md` y extrae el modelo de datos.
* Genera un script SQL compatible con **PostgreSQL** para la base de datos `eternum_db`.
* Aplica integridad referencial y tipos de datos modernos según `database-standard`.

### 2. Desarrollo Backend
* Utilizando la skill /backend-java-springboot-standard haz lo siguiente:
* Inicializa la estructura del proyecto con **Java 25** y **Spring Boot 3.5**.
* Configura la conexión a `eternum_db`.
* Crea las entidades, repositorios y controladores necesarios para cumplir con las tareas del requerimiento.
* Asegura que el código cumpla con los estándares de `backend-java-springboot-standard`.

### 3. Desarrollo Frontend (Solo Web)
* Utilizando la skill /frontend-thymeleaf-standard haz lo siguiente:
* Implementa la interfaz utilizando **Thymeleaf**.
* **Importante:** Traduce fielmente el diseño de `mockup.html`. Debes extraer y utilizar exactamente los mismos códigos de color (Hex/RGB), espaciados y tipografías.
* Aplica las directrices de `ux-ui-design` para mejorar la navegación y asegurar que el HTML sea **SEO-Friendly** (uso correcto de etiquetas semánticas, meta-tags, y jerarquía de encabezados).
* **OMITIR:** No generes código, configuraciones o vistas específicas para Android o iOS. Enfócate exclusivamente en una experiencia Web Responsiva de escritorio/móvil vía navegador.

### 4. Instrucción Operativa
* Antes de escribir los archivos, preséntame un breve resumen de la estructura de tablas que planeas crear.
* Una vez confirmado, procede a generar los archivos de forma iterativa.
* 
---
