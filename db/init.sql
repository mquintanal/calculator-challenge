-- init.sql
-- Script de inicialización para la API Calculadora (MySQL 8+)

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS calculadora
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar base de datos
USE calculadora;

-- Crear usuario con autenticación compatible JDBC (MySQL 8)
CREATE USER IF NOT EXISTS 'calculadora_user'@'localhost' IDENTIFIED BY 'calculadora_password';
ALTER USER 'calculadora_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'calculadora_password';

-- Conceder todos los privilegios necesarios
GRANT ALL PRIVILEGES ON calculadora.* TO 'calculadora_user'@'localhost';
FLUSH PRIVILEGES;

-- (Opcional) Crear las tablas manualmente si desactivas hibernate.ddl-auto
-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL
);

-- Tabla de operaciones
CREATE TABLE IF NOT EXISTS operations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    operation VARCHAR(50) NOT NULL,
    operand_a DECIMAL(20,10) NOT NULL,
    operand_b DECIMAL(20,10),
    result DECIMAL(20,10) NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
