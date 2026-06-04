# Progreso 2 Integracion - Jonathan Granja

## 1. Nombre del estudiante

Jonathan Granja

## 2. Descripcion breve de la solucion

Esta solucion implementa una integracion minima para la organizacion Salud360. La API REST recibe solicitudes de cita medica, valida los datos obligatorios y entrega las citas validas a una ruta de Apache Camel. La ruta distribuye la informacion hacia facturacion mediante una cola Point-to-Point, publica un evento para notificaciones y analitica mediante Publish/Subscribe, y genera un archivo CSV para el sistema legado de auditoria.

## 3. Tecnologias utilizadas

- Java 17
- Spring Boot 3.3.5
- Apache Camel 4.8.1
- RabbitMQ
- Docker Compose
- Maven
- JUnit 5

## 4. Instrucciones para levantar RabbitMQ

Desde la raiz del proyecto:

```bash
docker compose up -d
```

RabbitMQ queda disponible en:

- Broker AMQP: `localhost:5672`
- Consola web: `http://localhost:15672`
- Usuario: `guest`
- Contrasena: `guest`

## 5. Instrucciones para ejecutar la aplicacion

En Windows PowerShell, si Maven usa un JRE, configurar primero Java 17:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-17'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

Luego ejecutar:

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## 6. Endpoint disponible

```http
POST /api/citas
Content-Type: application/json
```

## 7. Ejemplo de request valido

```json
{
  "idCita": "CITA-1001",
  "paciente": "Ana Torres",
  "correo": "ana.torres@email.com",
  "especialidad": "Cardiologia",
  "fechaCita": "2026-06-15",
  "sede": "Centro Norte",
  "valor": 45.50
}
```

Ejemplo con `curl`:

```bash
curl -X POST http://localhost:8080/api/citas ^
  -H "Content-Type: application/json" ^
  -d "{\"idCita\":\"CITA-1001\",\"paciente\":\"Ana Torres\",\"correo\":\"ana.torres@email.com\",\"especialidad\":\"Cardiologia\",\"fechaCita\":\"2026-06-15\",\"sede\":\"Centro Norte\",\"valor\":45.50}"
```

Respuesta esperada:

```json
{
  "estado": "ACEPTADA",
  "idCita": "CITA-1001",
  "mensaje": "Cita recibida e integrada correctamente"
}
```

## 8. Ejemplo de request invalido

```json
{
  "idCita": "CITA-ERROR",
  "valor": -1
}
```

Respuesta esperada:

```json
{
  "estado": "RECHAZADA",
  "idCita": "CITA-ERROR",
  "errores": [
    "paciente es obligatorio",
    "correo es obligatorio",
    "especialidad es obligatoria",
    "fechaCita es obligatoria",
    "sede es obligatoria",
    "valor debe ser mayor a 0"
  ]
}
```

## 9. Explicacion de patrones y estilos de integracion

### Point-to-Point

Se aplica en la ruta hacia `billing.queue`. La facturacion debe recibir cada cita confirmada una sola vez, porque genera una orden de cobro y no debe duplicar cargos.

### Publish/Subscribe

Se aplica en el exchange `appointments.events`. El mismo evento `CITA_CONFIRMADA` se publica para que `notifications.queue` y `analytics.queue` lo reciban de forma independiente.

### Transferencia de archivos

Se aplica en `data/outbox/auditoria-citas.csv`. El sistema legado de auditoria no tiene API ni mensajeria, por lo que recibe una linea CSV por cada cita valida procesada.

### Manejo de errores

La API valida campos obligatorios y que `valor` sea mayor a 0. Las solicitudes invalidas no entran al flujo de integracion y quedan registradas en `data/errors/citas-rechazadas.log` con fecha, `idCita`, motivo y payload. La ruta Camel tambien registra errores de procesamiento en el mismo archivo.

## 10. Evidencia esperada para verificar el funcionamiento

Para completar el informe, se deben adjuntar capturas de:

1. Aplicacion Spring Boot ejecutandose sin errores.
2. RabbitMQ levantado con Docker Compose.
3. Request valido enviado desde Postman, curl o Swagger.
4. Respuesta exitosa de la API.
5. Mensaje generado en `billing.queue`.
6. Evento recibido en `notifications.queue`.
7. Evento recibido en `analytics.queue`.
8. Archivo `data/outbox/auditoria-citas.csv` con una cita valida.
9. Archivo `data/errors/citas-rechazadas.log` con una cita invalida.

## Repositorio GitHub

El informe debe contener el enlace publico real del repositorio. URL sugerida si ese usuario esta disponible:

```text
https://github.com/jonathangranja/progreso2-integracion-granja-jonathan
```
