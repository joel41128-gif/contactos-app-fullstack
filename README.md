# Directorio de Contactos - Full Stack

Aplicación de gestión de contactos con búsqueda dinámica en tiempo real, desarrollada con Java, PostgreSQL y JavaScript.

## Tecnologías utilizadas

- **Backend:** Java 21 (HttpServer nativo y JDBC)
- **Base de datos:** PostgreSQL
- **Frontend:** HTML, CSS, JavaScript (Fetch API)

## Funcionalidades

- Agregar contactos con nombre, teléfono, email y categoría
- Buscar contactos por nombre en tiempo real (búsqueda dinámica con SQL ILIKE)
- Eliminar contactos
- API REST propia (GET, POST, DELETE)

## Estructura del proyecto
contactos-app/
├── src/
│   ├── Conexion.java       # Conexión a PostgreSQL
│   ├── Contacto.java        # Modelo de datos
│   ├── ContactoDAO.java     # Operaciones CRUD y búsqueda
│   └── ServidorApp.java     # Servidor HTTP y API REST
├── web/
│   └── index.html           # Frontend con búsqueda en tiempo real
└── README.md


## Cómo ejecutarlo

1. Crear la base de datos:

```sql
CREATE DATABASE contactos_app;

CREATE TABLE contactos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    categoria VARCHAR(50)
);
```

2. Configurar usuario/contraseña en `Conexion.java`.
3. Colocar el driver JDBC de PostgreSQL en `lib/`.
4. Ejecutar `ServidorApp.java` (puerto 8081).
5. Abrir `web/index.html` en el navegador.

## Autor

Joel — Proyecto de práctica y portafolio personal.

