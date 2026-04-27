# mic-cola-digital-atencion-clientes
Sistema de "Turnos Virtuales" para Atención al Cliente (Cola Digital)



 Arquitectura General
 ┌─────────────────┐     ┌──────────────────┐     
│   turnos-api    │────▶│      Kafka       │
│   (Puerto 8085) │     │   (Event Bus)    │     
└─────────────────┘     └──────────────────┘     
         │                        ▲                        
         │                        │                         
         ▼                        │                         
┌─────────────────┐     ┌──────────────────┐              
│   PostgreSQL    │     │  dashboard-api  │           
│   (Turnos BD)   │     │   (Puerto 8082)  │
└─────────────────┘     └──────────────────┘
                            (Consola Log)

Proyecto 1: api-urnos (Puerto 8085)
Responsabilidad: Registro de clientes y asignación de códigos de turno emitir eventos Kafka.

Proyecto 2: dashboard-api (Puerto 8082)
Responsabilidad: Consumir eventos Kafka y mostrar información en terminal



🚀 Guía de Ejecución

1. Iniciar infraestructura (Docker)

docker-compose -f 02-postgres.yml up -d

ingresar a kla base de datos y crear la base datos a usar: db_turnos

2. Iniciar servicions kafka

docker-compose -f 01-kafka-server.yml up -d

3. Ejecutar cada microservicio (en terminales separadas)


# Terminal 1 - api.turnos
cd api.turnos
cd api.turnos
./mvnw quarkus:dev


# Terminal 2 - api.dashboard
cd api.dashboard
cd api.dashboard
./mvnw quarkus:dev

4. importe la documentacion a postaman de la carpeta DOCS


============================================================
📢 EVENTO DE TURNO: LLAMADO
============================================================
🎫 Código de Turno    : T-001
👤 Cliente            : Juan Pérez
⏰ Hora del Evento    : 2024-01-15T10:30:45

💬 MENSAJE: ¡Juan Pérez, por favor acérquese a la Ventanilla !
============================================================

============================================================
📋 LISTA DE TURNOS EN ESPERA "
============================================================
codigo : T-002  ....... posicion En Cola : 2
codigo : T-003  ....... posicion En Cola : 3
codigo : T-004  ....... posicion En Cola : 4