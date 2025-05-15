
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

>## Requerimientos ##

|       | Recomendado |
|-------|-------------|
| java  | 21          |
| Maven | 3.8.6       |
| MySQL | 8.0.42      |


## 📄  Quicksetup ##

Primero clona el proyecto desde este mismo repositorio. Luego sigue los siguientes pasos:


## 🗃️ Base de Datos

1-Corre tu servidor MySql.

2- Ejecuta el script SQL `db/init.sql` para crear la base de datos, el usuario y las tablas necesarias:

2.1 - Si prefieres que Hibernate haga las tablas por ti, solo ejecuta:

```
-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS calculadora
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar base de datos
USE calculadora;

-- Crear usuario con autenticación compatible JDBC (MySQL 8)
CREATE USER IF NOT EXISTS 'calculadora_user'@'localhost' IDENTIFIED BY 'calculadora_password';
ALTER USER 'calculadora_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'calculadora_password';
```

```bash
mysql -u root -p < db/init.sql
```
> El script crea un usuario `calculadora_user` y define autenticación compatible con JDBC.

---
## 🗜️ Dependencias Maven
3-Instala las dependencias del proyecto corriendo el siguiente comando en la raíz del proyecto.

```
#Para sistmas Windows:
mvn clean install

#Para sistemas Unix:
./mvn clean install

#Wrapped para sistemas Unix
./mvnw clean install

#Wrapped para sistemas Windows:
mvnw.cmd clean install
```
---



4- Inicia la aplicación. [Conoce más](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)

```
#Para sistemas Windows:
mvn spring-boot:run 

#Para sistemas Unix:
./mvn spring-boot:run 

##Wrapped para sistemas Unix
./mvnw spring-boot:run 

#Wrapped para sistemas Windows:
mvnw.cmd spring-boot:run 
```

## 📂 Estructura del Proyecto
```
calculator/
├── postman/
│   └── Calculadora API.postman_collection.json    # Coleccion de Postman
├── db/
│   └── init.sql                       # Script de inicialización de base de datos
├── src/
│   ├── main/
│   │   ├── java/com/mmdl/calculator/
│   │   │   ├── config/
│   │   │   │   └── SwaggerConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CalculatorController.java
│   │   │   │   └── HistoryController.java
│   │   │   ├── dto/
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── CalculationRequest.java
│   │   │   │   ├── CalculationResponse.java
│   │   │   │   ├── OperationFilterDTO.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   └── UserDto.java
│   │   │   ├── exception/
│   │   │   │   ├── ApiExceptionHandler.java
│   │   │   │   ├── EmailValidationException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UserAlreadyExistsException.java
│   │   │   ├── model/
│   │   │   │   ├── Operation.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── OperationRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── CalculatorService.java
│   │   │   │   ├── EmailValidationService.java
│   │   │   │   └── OperationService.java
│   ├── resources/
│   │   └── application.properties
├── test/
│   ├── java/com/mmdl/calculator/
│   │   ├── controller/
│   │   │   └── HistoryControllerTest.java
│   │   ├── service/
│   │   │   ├── AuthServiceTest.java
│   │   │   ├── CalculatorServiceTest.java
│   │   │   └── OperationServiceTest.java
│   │   └── CalculatorApplicationTests.java
├── .gitattributes
├── .gitignore
├── LICENSE
├── mvnw                              # Maven Wrapper (Unix)
├── mvnw.cmd                          # Maven Wrapper (Windows)
├── pom.xml                           # Configuración de Maven
└── README.md                         # Este archivo
```

## 🔐 Seguridad

- Autenticación con JWT (Bearer Token).
- Encriptación de contraseñas con BCrypt.
- Validación de correos usando [Abstract API](https://www.abstractapi.com/email-verification).
- El token JWT tiene una duración de **5 minutos** (300,000 ms).
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

