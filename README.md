
# 📘 API REST: Calculadora con JWT, Historial y Validación de Email

API REST construida con **Spring Boot 3.4.5** y **Java 21** que permite:

- Registrar y autenticar usuarios con JWT.
- Realizar operaciones matemáticas (suma, resta, multiplicación, división, raíz cuadrada).
- Persistir y consultar historial de operaciones con filtros y paginación.
- Validar correos electrónicos usando una API externa.
- Documentación Swagger integrada.

---

## 🚀 Tecnologías Usadas

| Tecnología        | Descripción                                 |
|-------------------|---------------------------------------------|
| Spring Boot 3.4.5 | Backend principal                           |
| Java 21           | Lenguaje base                               |
| MySQL 8+          | Base de datos relacional                    |
| Spring Security   | Autenticación y autorización con JWT        |
| JJWT 0.11.5       | Generación de tokens JWT                    |
| JPA (Hibernate)   | Persistencia de datos                       |
| Swagger (Springdoc) | Documentación interactiva con OpenAPI    |
| JUnit 5 + Mockito | Pruebas unitarias y de servicio             |

---

## ⚙️ Instalación y Configuración

### 📄 application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/calculadora?useSSL=false&serverTimezone=UTC
spring.datasource.username=calculadora_user
spring.datasource.password=calculadora_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

security.jwt.secret=VvZtB7z9AaDk3TyWqL8MsR4XePnUq3Lr

external.api.abstract.url=https://emailvalidation.abstractapi.com/v1/
external.api.abstract.key=c8eb37b717954aeca13f769a16a14fd8

server.port=8080
```

📌 El token JWT tiene una duración de **5 minutos** (300,000 ms).

---

## 🗃️ Base de Datos

Ejecuta el script SQL `db/init.sql` para crear la base de datos, el usuario y las tablas necesarias:

```bash
mysql -u root -p < db/init.sql
```

> El script crea un usuario `calculadora_user` y define autenticación compatible con JDBC.

---

## 🔐 Seguridad

- Autenticación con JWT (Bearer Token).
- Encriptación de contraseñas con BCrypt.
- Validación de correos usando [Abstract API](https://www.abstractapi.com/email-verification).

---

## 📄 Endpoints Principales

| Método | Endpoint               | Autenticado | Descripción                        |
|--------|------------------------|-------------|------------------------------------|
| POST   | /api/auth/register     | ❌          | Registro de usuario con validación de email |
| POST   | /api/auth/login        | ❌          | Login y generación de JWT         |
| POST   | /api/calculate         | ✅          | Ejecuta y guarda una operación    |
| GET    | /api/history           | ✅          | Consulta historial con filtros    |
| GET    | /api/history/{id}      | ✅          | Consulta detalle de operación     |
| DELETE | /api/history/{id}      | ✅          | Elimina operación                 |

---

## 📟 Ejemplos de uso y respuestas

### 🟠 Registro
#### ❌ Email inválido o desechable
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "email": "user1@gmail.com",
    "password": "123456"
  }'
```
**Response 400:**
```json
{
  "details": ["Disposable or invalid email address"],
  "message": "Email validation failed due to: undeliverable address;",
  "status": 400
}
```

#### ❌ Email duplicado
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "email": "quintana.m.martha@gmail.com",
    "password": "123456"
  }'
```
**Response 409:**
```json
{
  "message": "Email 'quintana.m.martha@gmail.com' is already registered.",
  "status": "409"
}
```

#### ✅ Registro exitoso
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "email": "marthakuinlo@gmail.com",
    "password": "123456"
  }'
```
**Response 201:**
```json
{
  "message": "User registered successfully"
}
```

### 🔑 Login
#### ❌ Credenciales inválidas
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "password": "wrongpassword"
  }'
```
**Response 401:**
```json
{
  "message": "Invalid username or password",
  "status": 401,
  "timestamp": "2025-05-14T15:49:05.8232557"
}
```

#### ✅ Login exitoso
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "password": "123456"
  }'
```
**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### ➕ Operación matemática
```bash
curl -X POST http://localhost:8080/api/calculate \
  -H "Authorization: Bearer TU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "operation": "ADDITION",
    "operandA": 70,
    "operandB": 2
  }'
```
**Response:**
```json
{
  "operation": "ADDITION",
  "operandA": 70,
  "operandB": 2,
  "result": 72,
  "timestamp": "2025-05-14T15:51:15.8900786"
}
```

### 📚 Historial de operaciones
```bash
curl -X GET "http://localhost:8080/api/history?page=0&size=5" \
  -H "Authorization: Bearer TU_TOKEN"
```
**Response:**
```json
[
  {
    "id": 1,
    "operation": "ADDITION",
    "operandA": 5.20,
    "operandB": 3.80,
    "result": 9.00,
    "timestamp": "2025-05-14T12:58:33.331779"
  }
]
```

### 🔍 Detalle por ID
```bash
curl -X GET http://localhost:8080/api/history/1 \
  -H "Authorization: Bearer TU_TOKEN"
```
**Response:**
```json
{
  "id": 1,
  "operation": "ADDITION",
  "operandA": 5.20,
  "operandB": 3.80,
  "result": 9.00,
  "timestamp": "2025-05-14T12:58:33.331779"
}
```

### 🗑️ Eliminar operación
```bash
curl -X DELETE http://localhost:8080/api/history/1 \
  -H "Authorization: Bearer TU_TOKEN"
```
**Response:**
```json
{
  "message": "Operation ID (1) has been removed"
}
```

---

## ✅ Decisiones Técnicas Tomadas

- ✔️ **BigDecimal** para evitar errores de precisión en operaciones matemáticas.
- ✔️ **JWT** para autenticación con tokens de 5 minutos y filtros personalizados.
- ✔️ Validación de emails con Abstract para rechazar correos inválidos o desechables (hasta 500 test de prueba, fácil registro e implementación).
- ✔️ Arquitectura limpia en capas: `controller`, `service`, `repository`, `model`, `dto`, `exception`.
- ✔️ `@ControllerAdvice` para manejo centralizado de errores.
- ✔️ Swagger OpenAPI documentado automáticamente con `springdoc-openapi`.
- ✔️ Uso de `@Valid` en DTOs para asegurar la integridad de datos de entrada.
- ✔️ Repositorios desacoplados y uso de `Optional` para control de existencia.
- ✔️ Soporte completo para pruebas con `JUnit` y `Mockito`.
- ✔️ MySQL8Dialect la versión más actual y segura.

---

## 🧪 Pruebas

- Ubicadas en `src/test/java/com/mmql/calculator`
- Cobertura: `AuthService`, `CalculatorService`, `OperationService`, `HistoryController`

```bash
mvn test
```

---

## 📚 Swagger UI

Disponible en:
```
http://localhost:8080/swagger-ui/index.html
```
Documentación en JSON:
```
http://localhost:8080/v3/api-docs
```

---

## 📦 Recursos

- 🗃️ `db/init.sql`: creación de base de datos y usuario.
- 📫 `postman/Calculadora_API_Collection.postman_collection.json`: colección Postman para pruebas.

---

## ✨ Autor y Licencia

MIT License - Desarrollado por Martha Quintana

---

