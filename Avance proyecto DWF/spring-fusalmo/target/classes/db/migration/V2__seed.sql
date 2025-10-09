-- Datos de ejemplo
INSERT INTO campus(nombre,direccion) VALUES ('Sede Central','Dirección demo') ON CONFLICT DO NOTHING;

INSERT INTO category(nombre,descripcion) VALUES ('Deporte',''),('Arte','')
ON CONFLICT DO NOTHING;

INSERT INTO academy(nombre,descripcion,campus_id) VALUES 
('Shaw Music Academy','',1),
('Steve Notar Academy','',1)
ON CONFLICT DO NOTHING;

INSERT INTO program(nombre,descripcion,academy_id,category_id,cupo_max,estado) VALUES
('Guitarra Básica','Aprende acordes y ritmo',1,2,3,'ACTIVO'),
('Fútbol Juvenil','Técnica y táctica',2,1,2,'ACTIVO');
/* ===== Categorías extra ===== */
INSERT INTO category (nombre, descripcion)
SELECT 'Tecnología','STEAM/Robótica' WHERE NOT EXISTS (SELECT 1 FROM category WHERE nombre='Tecnología');
INSERT INTO category (nombre, descripcion)
SELECT 'Idiomas','Inglés' WHERE NOT EXISTS (SELECT 1 FROM category WHERE nombre='Idiomas');
INSERT INTO category (nombre, descripcion)
SELECT 'Verano','Programas de temporada' WHERE NOT EXISTS (SELECT 1 FROM category WHERE nombre='Verano');
INSERT INTO category (nombre, descripcion)
SELECT 'Fe','Formación en la fe' WHERE NOT EXISTS (SELECT 1 FROM category WHERE nombre='Fe');

/* ===== Campus por si hace falta ===== */
INSERT INTO campus (nombre, direccion)
SELECT 'Sede Central','Dirección demo' WHERE NOT EXISTS (SELECT 1 FROM campus WHERE nombre='Sede Central');

/* ===== Academias (las del mockup) ===== */
INSERT INTO academy (nombre, descripcion, campus_id)
SELECT 'Steam Maker Academy','Tecnología y creatividad',
       (SELECT id FROM campus WHERE nombre='Sede Central')
    WHERE NOT EXISTS (SELECT 1 FROM academy WHERE nombre='Steam Maker Academy');

INSERT INTO academy (nombre, descripcion, campus_id)
SELECT 'Academia de Fútbol','Entrenamiento formativo',
       (SELECT id FROM campus WHERE nombre='Sede Central')
    WHERE NOT EXISTS (SELECT 1 FROM academy WHERE nombre='Academia de Fútbol');

INSERT INTO academy (nombre, descripcion, campus_id)
SELECT 'Academia de Básquet','Desarrollo técnico',
       (SELECT id FROM campus WHERE nombre='Sede Central')
    WHERE NOT EXISTS (SELECT 1 FROM academy WHERE nombre='Academia de Básquet');

INSERT INTO academy (nombre, descripcion, campus_id)
SELECT 'Academia de Inglés','Comunicación en inglés',
       (SELECT id FROM campus WHERE nombre='Sede Central')
    WHERE NOT EXISTS (SELECT 1 FROM academy WHERE nombre='Academia de Inglés');

INSERT INTO academy (nombre, descripcion, campus_id)
SELECT 'Verano Aventura','Actividades de temporada',
       (SELECT id FROM campus WHERE nombre='Sede Central')
    WHERE NOT EXISTS (SELECT 1 FROM academy WHERE nombre='Verano Aventura');

INSERT INTO academy (nombre, descripcion, campus_id)
SELECT 'Catequesis','Formación cristiana',
       (SELECT id FROM campus WHERE nombre='Sede Central')
    WHERE NOT EXISTS (SELECT 1 FROM academy WHERE nombre='Catequesis');

/* ===== Programas: muchos (evitamos duplicados) ===== */

/* Steam Maker Academy (Tecnología) */
INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Robótica Básica','Arduino, sensores y lógica',
       (SELECT id FROM academy WHERE nombre='Steam Maker Academy'),
       (SELECT id FROM category WHERE nombre='Tecnología'), 15, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Robótica Básica');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Impresión 3D','Modelado e impresión FDM',
       (SELECT id FROM academy WHERE nombre='Steam Maker Academy'),
       (SELECT id FROM category WHERE nombre='Tecnología'), 12, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Impresión 3D');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Electrónica para Jóvenes','Protoboard, componentes y proyectos',
       (SELECT id FROM academy WHERE nombre='Steam Maker Academy'),
       (SELECT id FROM category WHERE nombre='Tecnología'), 15, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Electrónica para Jóvenes');

/* Academia de Fútbol (Deporte) */
INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Fútbol Infantil','Coordinación y fundamentos (6–9 años)',
       (SELECT id FROM academy WHERE nombre='Academia de Fútbol'),
       (SELECT id FROM category WHERE nombre='Deporte'), 20, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Fútbol Infantil');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Escuela de Porteros','Técnica específica para arqueros',
       (SELECT id FROM academy WHERE nombre='Academia de Fútbol'),
       (SELECT id FROM category WHERE nombre='Deporte'), 12, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Escuela de Porteros');

/* Academia de Básquet (Deporte) */
INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Básquet Sub-15','Fundamentos y táctica',
       (SELECT id FROM academy WHERE nombre='Academia de Básquet'),
       (SELECT id FROM category WHERE nombre='Deporte'), 18, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Básquet Sub-15');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Básquet Femenino','Liga formativa femenina',
       (SELECT id FROM academy WHERE nombre='Academia de Básquet'),
       (SELECT id FROM category WHERE nombre='Deporte'), 18, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Básquet Femenino');

/* Academia de Inglés (Idiomas) */
INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Inglés Inicial','A1: vocabulario y speaking básico',
       (SELECT id FROM academy WHERE nombre='Academia de Inglés'),
       (SELECT id FROM category WHERE nombre='Idiomas'), 25, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Inglés Inicial');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Inglés Intermedio','B1: conversación y listening',
       (SELECT id FROM academy WHERE nombre='Academia de Inglés'),
       (SELECT id FROM category WHERE nombre='Idiomas'), 22, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Inglés Intermedio');

/* Verano Aventura (Verano) */
INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Campamento de Verano','Deportes, excursiones y talleres',
       (SELECT id FROM academy WHERE nombre='Verano Aventura'),
       (SELECT id FROM category WHERE nombre='Verano'), 40, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Campamento de Verano');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Natación Recreativa','Técnica y recreación en piscina',
       (SELECT id FROM academy WHERE nombre='Verano Aventura'),
       (SELECT id FROM category WHERE nombre='Verano'), 16, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Natación Recreativa');

/* Catequesis (Fe) */
INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Primera Comunión','Preparación catequética',
       (SELECT id FROM academy WHERE nombre='Catequesis'),
       (SELECT id FROM category WHERE nombre='Fe'), 30, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Primera Comunión');

INSERT INTO program (nombre, descripcion, academy_id, category_id, cupo_max, estado)
SELECT 'Confirmación Juvenil','Itinerario para jóvenes',
       (SELECT id FROM academy WHERE nombre='Catequesis'),
       (SELECT id FROM category WHERE nombre='Fe'), 28, 'ACTIVO'
    WHERE NOT EXISTS (SELECT 1 FROM program WHERE nombre='Confirmación Juvenil');
