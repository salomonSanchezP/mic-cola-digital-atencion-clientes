# Sistema de Turnos Virtuales - Documentación Completa

## 📋 Tabla de Contenidos
1. [Descripción General](#descripción-general)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Componentes Principales](#componentes-principales)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Requisitos Previos](#requisitos-previos)
6. [Guía de Ejecución](#guía-de-ejecución)
7. [Endpoints API](#endpoints-api)
8. [Eventos Kafka](#eventos-kafka)
9. [Ejemplos de Uso](#ejemplos-de-uso)
10. [Buenas Prácticas](#buenas-prácticas)

---

## 📱 Descripción General

El **Sistema de Turnos Virtuales** es una solución integral de atención al cliente que permite:

- Registro de clientes y asignación de códigos de turno
- Gestión de colas virtuales
- Llamado de turnos en orden de prioridad
- Monitoreo en tiempo real mediante un dashboard
- Comunicación mediante eventos (Kafka)

**Tecnologías principales:**
- Quarkus (Framework REST)
- PostgreSQL (Base de datos)
- Kafka (Event Bus)
- Hibernate Panache (Persistencia)
- Maven (Gestión de dependencias)

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    SISTEMA DE TURNOS                        │
└─────────────────────────────────────────────────────────────┘

┌──────────────────────┐         ┌──────────────────────┐
│   api.turnos         │         │  api.dashboard       │
│  (Puerto 8085)       │         │  (Puerto 8082)       │
│                      │         │                      │
│ • Crear turnos       │         │ • Listar turnos      │
│ • Llamar turnos      │         │ • Monitorear estado  │
│ • Atender turnos     │         │ • Dashboard en vivo  │
└──────────────────────┘         └──────────────────────┘
         │                                 ▲
         │                                 │
         │        ┌────────────────────┐   │
         ├───────▶│   Kafka (Event)    │──┤
         │        │    Bus / Broker    │   │
         │        └────────────────────┘   │
         │                                 │
         └────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│           PostgreSQL (Base de Datos Persistente)         │
│                     (db_turnos)                          │
└──────────────────────────────────────────────────────────┘
```

---

## 🔧 Componentes Principales

### 1. **api.turnos (Puerto 8085)**

**Responsabilidades:**
- Registro de clientes
- Asignación de códigos de turno
- Gestión de estados de turnos (CREADO, LLAMADO, ATENDIDO, CANCELADO)
- Emisión de eventos Kafka

**Tecnologías:**
- Quarkus
- PostgreSQL
- Kafka Producer
- Hibernate Panache

---

### 2. **api.dashboard (Puerto 8082)**

**Responsabilidades:**
- Consumo de eventos Kafka
- Visualización de información en tiempo real
- Listado de turnos en espera
- Console/Dashboard de monitoreo

**Tecnologías:**
- Quarkus
- PostgreSQL
- Kafka Consumer
- Hibernate Panache

---

### 3. **Infraestructura**

#### PostgreSQL
- Almacenamiento persistente de turnos
- Base de datos: `db_turnos`
- Credenciales configurables en `application.yml`

#### Kafka
- Event Bus centralizado
- Topic principal: `turno-topic`
- Integración mediante Kafka Connect y Schema Registry

---

## 📁 Estructura del Proyecto

```
mic-cola-digital-atencion-clientes/
│
├── 01-kafka-server.yml              # Configuración Docker Compose para Kafka
├── 02-postgres.yml                  # Configuración Docker Compose para PostgreSQL
│
├── api.turnos/                      # Microservicio de gestión de turnos
│   ├── src/
│   │   ├── main/
│   │   │   ├── avro/                # Esquemas Avro para Kafka
│   │   │   │   └── turno-event.avsc
│   │   │   ├── docker/              # Dockerfiles
│   │   │   ├── java/pe/registros/ms/turnos/
│   │   │   │   ├── config/          # Configuración (Kafka, BD)
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # Entidades JPA
│   │   │   │   ├── mappers/         # Mapeo Entity ↔ DTO
│   │   │   │   ├── messaging/       # Servicios Kafka
│   │   │   │   ├── repository/      # Repositorios Panache
│   │   │   │   ├── resources/       # Controladores REST
│   │   │   │   └── service/         # Lógica de negocio
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   └── README.md
│
├── api.dashboard/                   # Microservicio de dashboard
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/pe/registros/ms/ventanilla/
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # Entidades JPA
│   │   │   │   ├── mappers/         # Mapeo Entity ↔ DTO
│   │   │   │   ├── messaging/       # Consumidor Kafka
│   │   │   │   ├── repository/      # Repositorios Panache
│   │   │   │   ├── resources/       # Controladores REST
│   │   │   │   └── service/         # Lógica de negocio
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   └── README.md
│
└── DOCS/                            # Documentación (Postman, etc.)
    └── collection.json
```

---

## ✅ Requisitos Previos

### Software
- **Java 25 o superior**
- **Maven 3.8+**
- **Docker y Docker Compose**
- **Git**

### Servicios Externos
- PostgreSQL (puede estar en Docker)
- Apache Kafka + Zookeeper (en Docker)
- Schema Registry (opcional, en Docker)

### Verificar Instalación
```bash
java -version          # Java 25+
mvn -version          # Maven 3.8+
docker --version      # Docker
docker-compose --version  # Docker Compose
```

---

## 🚀 Guía de Ejecución

### Paso 1: Iniciar Infraestructura (PostgreSQL)

```bash
# En la raíz del proyecto
docker-compose -f 02-postgres.yml up -d

# Verificar que el contenedor está corriendo
docker ps | grep postgres
```

**Crear la base de datos:**
```bash
# Acceder al contenedor PostgreSQL
docker exec -it postgres_container psql -U postgres

# Una vez dentro del cliente psql:
CREATE DATABASE db_turnos;
\l  # Listar bases de datos
\q  # Salir
```

---

### Paso 2: Iniciar Kafka y Zookeeper

```bash
# Levantar Kafka y Zookeeper
docker-compose -f 01-kafka-server.yml up -d

# Verificar que los contenedores están corriendo
docker ps | grep kafka
docker ps | grep zookeeper
```

---

### Paso 3: Ejecutar Microservicio API Turnos

**Terminal 1:**
```bash
cd api.turnos
./mvnw clean quarkus:dev
```

**Salida esperada:**
```
[io.quarkus] (Quarkus Bootstrap) Quarkus 3.x.x started
[io.quarkus] (Quarkus Bootstrap) Listening on: http://localhost:8085
```

---

### Paso 4: Ejecutar Microservicio Dashboard API

**Terminal 2:**
```bash
cd api.dashboard
./mvnw clean quarkus:dev
```

**Salida esperada:**
```
[io.quarkus] (Quarkus Bootstrap) Quarkus 3.x.x started
[io.quarkus] (Quarkus Bootstrap) Listening on: http://localhost:8082
```

---

### Paso 5: Importar Documentación en Postman

1. Abre Postman
2. Menú: **File → Import**
3. Selecciona la carpeta `DOCS/`
4. Importa la colección de requests

---

## 📡 Endpoints API

### API Turnos (Puerto 8085)

#### 1. Crear Turno

```http
POST /turnos
Content-Type: application/json

{
  "nombreCliente": "JOSE MARTINEZ C.",
  "documentoIdentidad": 12345678
}
```

**Respuesta exitosa (201):**
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "JOSE MARTINEZ C.",
  "documentoIdentidad": 12345678,
  "estado": "CREADO",
  "posicionEnCola": 1,
  "fechaHoraCreacion": "2026-04-26T19:42:30.166542"
}
```

---

#### 2. Llamar Turno

```http
PUT /turnos/llamar
Content-Type: application/json

{
  "codigoTurno": "T-001"
}
```

**Respuesta exitosa (200):**
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "JOSE MARTINEZ C.",
  "estado": "LLAMADO",
  "fechaHoraLlamada": "2026-04-26T19:43:00.000000"
}
```

---

#### 3. Atender Turno

```http
PUT /turnos/atender
Content-Type: application/json

{
  "codigoTurno": "T-001",
  "estado": "ATENDIDO"
}
```

**Estados permitidos:**
- `ATENDIDO`: Turno completado exitosamente
- `CANCELADO`: Cliente no se presentó

**Respuesta exitosa (200):**
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "JOSE MARTINEZ C.",
  "estado": "ATENDIDO",
  "fechaHoraAtendido": "2026-04-26T19:44:15.000000"
}
```

---

### API Dashboard (Puerto 8082)

#### 1. Health Check

```http
GET /api/v1/dashboard/health
```

**Respuesta (200):**
```json
{
  "status": "UP"
}
```

---

#### 2. Listar Todos los Turnos

```http
GET /api/v1/dashboard/turnos
```

**Respuesta (200):**
```json
[
  {
    "codigoTurno": "T-001",
    "nombreCliente": "salomon",
    "documentoIdentidad": 72780686,
    "fechaHoraCreacion": "2026-04-26T19:42:30.166542",
    "fechaHoraAtendido": "2026-04-26T19:42:30.166542",
    "estado": "ATENDIDO"
  },
  {
    "codigoTurno": "T-002",
    "nombreCliente": "Juan Pérez",
    "documentoIdentidad": 87654321,
    "fechaHoraCreacion": "2026-04-26T19:43:00.000000",
    "fechaHoraAtendido": null,
    "estado": "LLAMADO"
  }
]
```

---

#### 3. Listar Turnos en Espera

```http
GET /api/v1/dashboard/turnos/en-espera
```

**Respuesta (200):**
```json
[
  {
    "codigoTurno": "T-002",
    "nombreCliente": "Juan Pérez",
    "posicionEnCola": 2
  },
  {
    "codigoTurno": "T-003",
    "nombreCliente": "María García",
    "posicionEnCola": 3
  },
  {
    "codigoTurno": "T-004",
    "nombreCliente": "Carlos López",
    "posicionEnCola": 4
  }
]
```

---

## 📨 Eventos Kafka

### Configuración

- **Topic:** `turno-topic`
- **Servidor:** `localhost:9092`
- **Schema Registry:** `http://localhost:8081` (opcional)

### Esquema Avro (turno-event.avsc)

```json
{
  "type": "record",
  "name": "TurnoEvent",
  "namespace": "pe.registros.ms.turnos.events",
  "fields": [
    {
      "name": "codigoTurno",
      "type": "string"
    },
    {
      "name": "nombreCliente",
      "type": "string"
    },
    {
      "name": "documentoIdentidad",
      "type": "long"
    },
    {
      "name": "estado",
      "type": {
        "type": "enum",
        "name": "EstadoTurno",
        "symbols": ["CREADO", "LLAMADO", "ATENDIDO", "CANCELADO"]
      }
    },
    {
      "name": "fechaHoraEvento",
      "type": "string"
    },
    {
      "name": "posicionEnCola",
      "type": ["null", "int"],
      "default": null
    }
  ]
}
```

### Eventos Producidos

#### Evento: Turno Creado
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "JOSE MARTINEZ C.",
  "documentoIdentidad": 12345678,
  "estado": "CREADO",
  "fechaHoraEvento": "2026-04-26T19:42:30.166542",
  "posicionEnCola": 1
}
```

#### Evento: Turno Llamado
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "JOSE MARTINEZ C.",
  "documentoIdentidad": 12345678,
  "estado": "LLAMADO",
  "fechaHoraEvento": "2026-04-26T19:43:00.000000",
  "posicionEnCola": null
}
```

#### Evento: Turno Atendido/Cancelado
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "JOSE MARTINEZ C.",
  "documentoIdentidad": 12345678,
  "estado": "ATENDIDO",
  "fechaHoraEvento": "2026-04-26T19:44:15.000000",
  "posicionEnCola": null
}
```

---

## 💡 Ejemplos de Uso

### Flujo Completo de un Turno

#### 1. Cliente registra su turno

```bash
curl -X POST http://localhost:8085/turnos \
  -H "Content-Type: application/json" \
  -d '{
    "nombreCliente": "Juan Pérez",
    "documentoIdentidad": 72780686
  }'
```

**Respuesta:**
```json
{
  "codigoTurno": "T-001",
  "nombreCliente": "Juan Pérez",
  "documentoIdentidad": 72780686,
  "estado": "CREADO",
  "posicionEnCola": 1
}
```

---

#### 2. Sistema llama el turno

```bash
curl -X PUT http://localhost:8085/turnos/llamar \
  -H "Content-Type: application/json" \
  -d '{
    "codigoTurno": "T-001"
  }'
```

**Evento Kafka emitido:**
```
📢 EVENTO DE TURNO: LLAMADO
🎫 Código de Turno: T-001
👤 Cliente: Juan Pérez
⏰ Hora del Evento: 2026-04-26T19:43:00.000000

💬 MENSAJE: ¡Juan Pérez, por favor acérquese a la Ventanilla!
```

---

#### 3. Dashboard consume evento

El **api.dashboard** recibe el evento automáticamente y lo registra.

```bash
curl http://localhost:8082/api/v1/dashboard/turnos
```

**Respuesta:**
```json
[
  {
    "codigoTurno": "T-001",
    "nombreCliente": "Juan Pérez",
    "estado": "LLAMADO"
  }
]
```

---

#### 4. Atender turno

```bash
curl -X PUT http://localhost:8085/turnos/atender \
  -H "Content-Type: application/json" \
  -d '{
    "codigoTurno": "T-001",
    "estado": "ATENDIDO"
  }'
```

---

### Consultar Cola de Espera

```bash
curl http://localhost:8082/api/v1/dashboard/turnos/en-espera
```

**Salida esperada:**
```
============================================================
📋 LISTA DE TURNOS EN ESPERA
============================================================
codigo: T-002 ....... posición En Cola: 2
codigo: T-003 ....... posición En Cola: 3
codigo: T-004 ....... posición En Cola: 4
============================================================
```

---

## ✨ Buenas Prácticas

### 1. **Separación de Responsabilidades**

```
Controller (Recursos)
    ↓
Service (Lógica de Negocio)
    ↓
Repository (Acceso a Datos)
    ↓
Entity (Persistencia JPA)
```

### 2. **Uso de DTOs**
- Desacopla la capa de presentación
- Mejora seguridad (no expone todas las propiedades)
- Facilita evolución de la API

### 3. **Configuración Centralizada**
- `application.yml` contiene toda la configuración
- Fácil de cambiar entre ambientes (dev, test, prod)

### 4. **Validaciones**
- Validación de entrada en DTOs
- Manejo de excepciones personalizado
- Códigos HTTP apropiados

### 5. **Versionamiento API**
- Endpoints con versión: `/api/v1/dashboard/...`
- Facilita cambios sin romper clientes existentes

### 6. **Documentación**
- README.md en cada microservicio
- Postman collection para pruebas
- Comentarios en código complejo

### 7. **Eventos Asíncronos**
- Uso de Kafka para desacoplamiento
- Schema Avro para contrato de eventos
- Permite escalabilidad horizontal

---

## 🔧 Troubleshooting

### Problema: Puerto 8085 ya está en uso

```bash
# Cambiar puerto en application.yml
quarkus:
  http:
    port: 8086
```

---

### Problema: PostgreSQL no conecta

**Verificar credenciales en `application.yml`:**
```yaml
quarkus:
  datasource:
    db-kind:
    username:
    password:
    jdbc:
      url: jdbc:postgresql://localhost:5432/db_turnos
```

---

### Problema: Kafka no conecta

**Verificar conexión:**
```bash
docker logs kafka_container
docker logs zookeeper_container

# Probar conectividad
telnet localhost 9092
```

---

## 📚 Referencias

- [Quarkus Documentation](https://quarkus.io/guides/)
- [Hibernate Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [Kafka Connector](https://quarkus.io/guides/kafka)
- [PostgreSQL JDBC](https://jdbc.postgresql.org/)

---

## 📝 Notas Importantes

- Todos los endpoints retornan JSON
- Las fechas se envían en formato ISO 8601
- Los códigos de turno se generan automáticamente (T-001, T-002, etc.)
- Los eventos de Kafka son procesados de forma asíncrona
- La base de datos se crea automáticamente con Hibernate (DDL auto)

---

**Última actualización:** Abril 2026
**Versión del Sistema:** 1.0.0
**Ambiente:** Desarrollo Local

