# Serena Soft — Sistema de Gestión Comercial

Serena Soft es un sistema de gestión comercial de escritorio desarrollado en **Java**, orientado a pequeños y medianos comercios que necesitan administrar de forma centralizada sus productos, stock, ventas, usuarios y operaciones diarias.

El proyecto está desarrollado con una arquitectura organizada en capas, separando entidades, acceso a datos, lógica de negocio y presentación.

> 🚧 Proyecto actualmente en desarrollo.

---

## 📌 Objetivo del proyecto

El objetivo de Serena Soft es construir una solución de gestión comercial modular, mantenible y preparada para adaptarse a distintos tipos de negocios.

El sistema busca centralizar operaciones como:

- Gestión de productos
- Control de stock
- Categorías, rubros y marcas
- Usuarios y autenticación
- Roles y permisos
- Proveedores
- Ventas y caja
- Depósitos
- Movimientos de stock
- Reportes comerciales

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java | Lenguaje principal |
| Java Swing | Interfaz gráfica |
| JDBC | Acceso a datos |
| MySQL | Base de datos relacional |
| NetBeans | Entorno de desarrollo |
| Git | Control de versiones |
| GitHub | Repositorio y seguimiento del proyecto |

---

## 🏗️ Arquitectura

El proyecto utiliza una separación de responsabilidades basada en capas.

```text
┌──────────────────────────┐
│      Presentación        │
│      Java Swing          │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│     Capa de Servicios    │
│   Reglas de negocio      │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│        Capa DAO          │
│    Acceso a datos        │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│         MySQL            │
│    Base de datos         │
└──────────────────────────┘
```

Esta organización permite mantener separada la interfaz gráfica de la lógica de negocio y del acceso a la base de datos.

---

## 📦 Módulos

### 👤 Usuarios, roles y permisos

El sistema cuenta con una estructura de autenticación y autorización basada en:

- Usuarios
- Roles
- Permisos
- Sesión de usuario
- Validación de credenciales

Esto permite controlar las funcionalidades disponibles según el rol asignado.

---

### 📦 Productos

La gestión de productos contempla información como:

- Código interno
- Código de barras
- Nombre
- Descripción
- Rubro
- Categoría
- Marca
- Unidad de medida
- IVA
- Precio
- Stock mínimo
- Ubicación
- Estado

---

### 🏷️ Clasificación de productos

Los productos pueden organizarse mediante:

```text
Rubro
  └── Categoría
        └── Producto
```

También se gestionan entidades complementarias como:

- Marcas
- Unidades de medida
- Tipos de IVA

---

### 📊 Stock

El módulo de stock está diseñado para controlar existencias por producto y depósito.

Permite trabajar con:

- Stock actual
- Ingresos
- Egresos
- Ajustes
- Depósitos
- Movimientos
- Fecha de actualización
- Control de cantidades

---

### 🏢 Proveedores

El sistema contempla la administración de proveedores asociados a las operaciones comerciales y al abastecimiento de productos.

---

### 💰 Ventas y caja

El proyecto contempla módulos destinados a gestionar el proceso comercial, incluyendo ventas y operaciones de caja.

Estos módulos continúan actualmente en desarrollo.

---

## 🔐 Seguridad y configuración

Las credenciales de acceso a la base de datos **no se almacenan directamente en el código fuente**.

La aplicación utiliza un archivo local:

```text
config.properties
```

Este archivo se encuentra excluido del repositorio mediante `.gitignore`.

Ejemplo de configuración:

```properties
db.url=jdbc:mysql://localhost:3306/serena_soft
db.user=YOUR_DATABASE_USER
db.password=YOUR_DATABASE_PASSWORD
```

De esta manera, cada entorno puede utilizar su propia configuración sin modificar el código fuente ni publicar credenciales.

---

## 🗄️ Persistencia

El acceso a MySQL se realiza mediante **JDBC**.

La aplicación utiliza clases DAO para encapsular las operaciones relacionadas con persistencia, evitando realizar consultas SQL directamente desde la interfaz gráfica.

Ejemplo conceptual:

```text
Vista
  ↓
Service
  ↓
DAO
  ↓
JDBC
  ↓
MySQL
```

---

## 🧪 Pruebas durante el desarrollo

Durante el desarrollo se realizan pruebas sobre los principales componentes del sistema, incluyendo:

- Conexión con MySQL
- Usuarios
- Roles
- Permisos
- Rubros
- Categorías
- Marcas
- Productos
- Stock

---

## 📂 Estructura general

```text
SerenaSoft/
│
├── src/
│   ├── Configuracion/
│   ├── Entidades/
│   ├── Dao/
│   ├── Services/
│   └── ...
│
├── test/
├── nbproject/
├── .gitignore
├── README.md
├── build.xml
└── manifest.mf
```

> La estructura puede modificarse a medida que avance el desarrollo.

---

## 🚀 Estado actual

Serena Soft se encuentra en **desarrollo activo**.

Actualmente se está trabajando en la consolidación de los módulos principales del sistema y en la integración entre productos, stock y las operaciones comerciales.

### Implementado / en desarrollo

- [x] Conexión con MySQL
- [x] Usuarios
- [x] Roles
- [x] Permisos
- [x] Autenticación
- [x] Sesión de usuario
- [x] Rubros
- [x] Categorías
- [x] Marcas
- [x] Unidades de medida
- [x] IVA
- [x] Productos
- [x] Base del control de stock
- [ ] Gestión completa de proveedores
- [ ] Flujo completo de ventas
- [ ] Caja
- [ ] Reportes
- [ ] Instalador y distribución

---

## 🗺️ Roadmap

Entre los próximos objetivos del proyecto se encuentran:

1. Completar el módulo de stock.
2. Integrar stock con las operaciones de venta.
3. Completar proveedores.
4. Implementar el flujo completo de ventas.
5. Desarrollar el módulo de caja.
6. Incorporar reportes.
7. Mejorar validaciones y manejo de errores.
8. Preparar la aplicación para distribución.

---

## 🎯 Motivación

Serena Soft es un proyecto desarrollado con el objetivo de aplicar conceptos de desarrollo de software en un sistema de gestión comercial real.

El proyecto permite trabajar sobre conceptos como:

- Programación orientada a objetos
- Arquitectura por capas
- Patrón DAO
- Capa de servicios
- Modelado relacional
- JDBC
- Manejo de sesiones
- Roles y permisos
- Validaciones
- Gestión de inventario
- Git y control de versiones

---

## 👨‍💻 Autor

**Santiago Contreras**

Desarrollador Java enfocado en backend y desarrollo de sistemas de gestión.

### Tecnologías de interés

`Java` · `Spring Boot` · `MySQL` · `REST APIs` · `Git` · `GitHub`

---

## 📄 Nota

Este repositorio corresponde a un proyecto en evolución. Algunas funcionalidades pueden encontrarse parcialmente implementadas o sujetas a modificaciones durante el desarrollo.