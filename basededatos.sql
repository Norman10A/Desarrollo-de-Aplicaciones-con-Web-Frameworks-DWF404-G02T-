-- Crear base de datos
CREATE DATABASE IF NOT EXISTS spring_api CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE spring_api;

-- Tabla: users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Tabla: user_roles (roles de usuario)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla: categories
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

-- Tabla: courses
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category_id BIGINT NOT NULL,
    image VARCHAR(255) NOT NULL DEFAULT 'assets/default-course.jpg',
    short_description VARCHAR(500),
    info TEXT,
    capacity INT NOT NULL DEFAULT 20,
    enrolled INT NOT NULL DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Tabla: enrollments (inscripciones)
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    student_last_name VARCHAR(100) NOT NULL,
    student_age INT NOT NULL,
    parent_name VARCHAR(100) NOT NULL,
    parent_dui VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    message TEXT,
    branch VARCHAR(50) NOT NULL, -- NUEVO: sede
    enrollment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- Datos de ejemplo: categorías
INSERT IGNORE INTO categories (name, description) VALUES
('Programación', 'Cursos de desarrollo de software'),
('Diseño Gráfico', 'Cursos de diseño y creatividad digital'),
('Música', 'Cursos de instrumentos y teoría musical'),
('Idiomas', 'Cursos de inglés, francés y otros'),
('Matemáticas', 'Cursos de álgebra, cálculo y estadística'),
('Ciencias', 'Biología, química y física'),
('Deportes', 'Fútbol, baloncesto y atletismo'),
('Artes', 'Pintura, teatro y danza');

-- Datos de ejemplo: usuarios (contraseña: "password123" encriptada con BCrypt)
-- Puedes generar nuevas contraseñas en: https://bcrypt-generator.com/
INSERT IGNORE INTO users (username, email, password) VALUES
('admin', 'arturo@fusalmo.org', '$2a$10$X6c3vJq5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J'), -- ADMIN
('user1', 'user1@gmail.com', '$2a$10$X6c3vJq5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J5J');

-- Asignar roles
INSERT IGNORE INTO user_roles (user_id, role) VALUES
(1, 'ADMIN'),
(2, 'USER');

-- Datos de ejemplo: cursos
INSERT IGNORE INTO courses (title, category_id, short_description, capacity, enrolled) VALUES
('Bootcamp Full Stack', 1, 'Aprende desarrollo web completo', 20, 5),
('Photoshop Avanzado', 2, 'Domina el diseño gráfico profesional', 15, 10),
('Piano para Principiantes', 3, 'Iníciate en el mundo de la música', 12, 8),
('Inglés Básico', 4, 'Aprende inglés desde cero', 25, 20),
('Álgebra Lineal', 5, 'Fundamentos de matemáticas universitarias', 30, 15);

-- Datos de ejemplo: inscripciones
INSERT IGNORE INTO enrollments (course_id, student_name, student_last_name, student_age, parent_name, parent_dui, email, phone, branch) VALUES
(1, 'Juan', 'Pérez', 16, 'María Pérez', '12345678-9', 'juan.perez@gmail.com', '7000-1111', 'Fusalmo Soyapango'),
(2, 'Ana', 'López', 14, 'Carlos López', '87654321-0', 'ana.lopez@gmail.com', '7000-2222', 'Fusalmo Santa Ana'),
(3, 'Luis', 'Hernández', 12, 'Sofía Hernández', '11223344-5', 'luis.hernandez@gmail.com', '7000-3333', 'Fusalmo San Miguel');