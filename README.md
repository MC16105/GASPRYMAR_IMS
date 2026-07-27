## Proyecto de WebApp para una MIPYMES consta de la Combinacion clasica del FullStack
Con Spring/JAVA en el Backend
React/Axios en el Frontend
Y PostgreSQL como DB determinada
Incluyendo la "Contenerizacion" en Docker

## Dependencias de Spring Boot:
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL Driver
- Lombok
- Validation
- JWT

## Estructura Base de las Entidades
- Rol
- Usuario
- Cliente
- Proveedor
- Producto
- Activo
- Compra
- DetalleCompra
- Produccion
- Venta
- DetalleVenta
- Combustible

## Estructura N-Capas
src/main/java/com/camaronera

├── controller
│
├── dto
│   ├── ClienteDTO.java
│   ├── CompraDTO.java
│   ├── VentaDTO.java
│   └── ...
│
├── entity
│
├── repository
│
├── service
│   ├── ClienteService.java
│   ├── CompraService.java
│   ├── VentaService.java
│   └── ...
│
├── serviceImpl
│   ├── ClienteServiceImpl.java
│   ├── CompraServiceImpl.java
│   ├── VentaServiceImpl.java
│   └── ...
│
├── security
│
└── config
