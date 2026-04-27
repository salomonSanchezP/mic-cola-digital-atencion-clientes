# API Turnos

## Descripción del Proyecto
Este proyecto es una API REST desarrollada con Quarkus que gestiona turnos para un sistema de registro. Implementa un proceso de negocio claro y completo que incluye la creación, actualización, y gestión de turnos, con persistencia en base de datos, integración con Kafka para eventos y buenas prácticas de arquitectura.

## Características

### 1. Definición del Proceso de Negocio
El sistema permite gestionar turnos de clientes, incluyendo:
- Creación de turnos con asignación de código y posición en cola.
- Llamado de turnos en orden de prioridad.
- Atención de turnos con validaciones específicas.

### 2. APIs REST con Quarkus
- Implementación de endpoints RESTful para operaciones CRUD.
- Validaciones de entrada y manejo de errores.
- Uso correcto de códigos de estado HTTP.

### 3. Persistencia con Hibernate + Panache
- Uso de entidades y repositorios para la interacción con la base de datos.
- Consultas optimizadas y relaciones entre entidades.

### 4. Consumo de APIs Externas
- Integración con APIs externas para futuras extensiones del sistema.

### 5. Eventos con Kafka
- Producción y consumo de eventos a través de Kafka, integrados al flujo del negocio.

### 6. Arquitectura y Buenas Prácticas
- Estructura clara y modular.
- Uso de capas: DTOs, servicios, repositorios y controladores.
- Separación de responsabilidades.

### 7. Repositorio y Documentación
- Repositorio organizado con un archivo README claro y detallado.
- Uso de Quarkus para simplificar el desarrollo y despliegue.

## Estructura del Proyecto
```
api.turnos/
├── src/
│   ├── main/
│   │   ├── avro/                # Esquemas Avro para eventos Kafka
│   │   ├── docker/              # Archivos Docker para despliegue
│   │   ├── java/                # Código fuente principal
│   │   │   └── pe/registros/ms/turnos/
│   │   │       ├── config/      # Configuración de Kafka y otros servicios
│   │   │       ├── dto/         # Clases DTO para transferencia de datos
│   │   │       ├── entity/      # Entidades JPA para persistencia
│   │   │       ├── mappers/     # Mapeo entre entidades y DTOs
│   │   │       ├── messaging/   # Servicios de mensajería con Kafka
│   │   │       ├── repository/  # Repositorios para acceso a datos
│   │   │       ├── resources/   # Controladores REST
│   │   │       └── service/     # Lógica de negocio
│   └── resources/               # Configuraciones de la aplicación
├── test/                        # Pruebas unitarias e integrales
├── target/                      # Archivos generados por el build
├── pom.xml                      # Configuración de Maven
└── README.md                    # Documentación del proyecto
```

## Requisitos Previos
- Java 25 superior
- Maven 3.8+
- Docker
- Kafka y Schema Registry configurados

## Configuración
1. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd api.turnos
   ```

2. Configurar el archivo `application.yml` en `src/main/resources` con los valores necesarios para la base de datos y Kafka.


3. Construir el proyecto:
   ```bash
   ./mvnw clean package
   ```

4. Levantar los servicios de Kafka y Schema Registry con Docker Compose:
   ```bash
   docker-compose up -d
   ```

5. Ejecutar la aplicación:
   ```bash
   java -jar target/quarkus-app/quarkus-run.jar
   ```

## Endpoints Principales

### Crear Turno
- **POST** `/turnos`
- **Descripción**: Crea un nuevo turno.
- **Cuerpo de la solicitud**:
  ```json
  {
    "nombreCliente": "JOSE MATINEZ C.",
    "documentoIdentidad": 12345678
  }
  ```

### Llamar Turno
- **PUT** `/turnos/llamar`
- **Descripción**: Llama al turno en la cola para su atencion con estado LLAMADO.
- **Cuerpo de la solicitud**:
  ```json
  {
    "codigoTurno": "T001"
  }
  ```

### Atender Turno
- **PUT** `/turnos/atender`
- **Descripción**: Atiende un turno llamado, pasa a un estado ATENDIDO O CANCELADO en caso no se prresente.
- **Cuerpo de la solicitud**:
  ```json
  {
    "codigoTurno": "T001",
    "estado": "LLAMADO"
  }
  ```

## Eventos Kafka
- **Topic**: `turno-topic`
- **Esquema Avro**: Definido en `src/main/avro/turno-event.avsc`.



## Buenas Prácticas
- Uso de DTOs para desacoplar la capa de presentación de la lógica de negocio.
- Separación de responsabilidades en capas (controladores, servicios, repositorios).
- Configuración centralizada en `application.yml`.


