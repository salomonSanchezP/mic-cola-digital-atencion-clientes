# API Dashboard - Gestión de Turnos

Este proyecto implementa un sistema de gestión de turnos usando Quarkus, Hibernate Panache, Kafka y buenas prácticas de arquitectura. Cumple con los siguientes criterios:

## Estructura del Proyecto

- **src/main/java/pe/registros/ms/ventanilla**
  - **entity/**: Entidades JPA (TurnoEntity)
  - **dto/**: Objetos de transferencia (TurnoResponseDTO, TurnoEnEsperaDTO)
  - **repository/**: Acceso a datos con Panache
  - **service/**: Lógica de negocio (TurnoService, TurnoServiceImpl)
  - **resources/**: Endpoints REST (TurnoResource)
  - **messagin/**: Integración con Kafka
  - **mappers/**: Conversión entre entidades y DTOs
- **src/main/resources/application.yml**: Configuración de base de datos, Kafka, puertos, etc.

## Endpoints REST principales

- `GET /api/v1/dashboard/health` — Health check
- `GET /api/v1/dashboard/turnos` — Listar todos los turnos
- (Puedes agregar POST, PUT, DELETE para CRUD completo)

## Ejemplo de respuesta JSON
`GET /api/v1/dashboard/turnos`
```json
[
  {
    "codigoTurno": "T-001",
    "nombreCliente": "salomon",
    "documentoIdentidad": 72780686,
    "fechaHoraCreacion": "2026-04-26T19:42:30.166542",
    "fechaHoraAtendido": "2026-04-26T19:42:30.166542",
    "estado": "ATENDIDO"
  }
]
```

## Configuración

- **Base de datos:** PostgreSQL (ver credenciales en `application.yml`)
- **Kafka:** Configurado para eventos de turnos
- **Puerto HTTP:** 8082

## Ejecución local

1. Instala Java 25 y Maven
2. Configura PostgreSQL y Kafka según `application.yml`
3. Ejecuta:
   ```bash
   ./mvnw clean compile quarkus:dev
   ```
4. Accede a `http://localhost:8082/api/v1/dashboard/health`



> Proyecto de ejemplo para gestión de turnos con Quarkus, Hibernate, Kafka y arquitectura limpia.
