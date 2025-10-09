-- DDL inicial (puedes ajustarlo)
CREATE TABLE IF NOT EXISTS campus(
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL UNIQUE,
  direccion TEXT
);

CREATE TABLE IF NOT EXISTS academy(
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL UNIQUE,
  descripcion TEXT,
  campus_id INT REFERENCES campus(id)
);

CREATE TABLE IF NOT EXISTS category(
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL UNIQUE,
  descripcion TEXT
);

CREATE TABLE IF NOT EXISTS program(
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(200) NOT NULL,
  descripcion TEXT,
  academy_id INT REFERENCES academy(id),
  category_id INT REFERENCES category(id),
  cupo_max INT,
  estado VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS student(
  id SERIAL PRIMARY KEY,
  nombres VARCHAR(120) NOT NULL,
  apellidos VARCHAR(120) NOT NULL,
  fecha_nacimiento DATE,
  sexo CHAR(1),
  doc_identidad VARCHAR(40),
  telefono VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS enrollment(
  id SERIAL PRIMARY KEY,
  student_id INT NOT NULL REFERENCES student(id),
  program_id INT NOT NULL REFERENCES program(id),
  periodo VARCHAR(20) NOT NULL,
  estado VARCHAR(20),
  fecha TIMESTAMP,
  CONSTRAINT uq_enr UNIQUE(student_id, program_id, periodo)
);
