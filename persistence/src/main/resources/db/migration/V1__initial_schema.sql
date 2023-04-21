CREATE TABLE IF NOT EXISTS users
(
    id          SERIAL PRIMARY KEY,
    email       VARCHAR(100) NOT NULL UNIQUE,
    pass        VARCHAR(100),
--     pass        VARCHAR(100) NOT NULL,

    username    VARCHAR(100)
--         username    VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS professors
(
    id          SERIAL PRIMARY KEY,
    profName    VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects
(
    id          VARCHAR(100) PRIMARY KEY,
    subName     VARCHAR(100) NOT NULL,
    department  VARCHAR(100) NOT NULL,
    credits     INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS degrees
(
    id          SERIAL PRIMARY KEY,
    degName     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews
(
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users(id),
    userEmail   VARCHAR(100) NOT NULL references users(email),
    idSub       VARCHAR(100) NOT NULL REFERENCES subjects,
    score INTEGER,
--     score       INTEGER NOT NULL,
    easy        INTEGER, -- 0-easy, 1-normal, 2-hard
    timeDemanding INTEGER, -- 0-no 1-yes
    revText     TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS professorsSubjects
(
    idProf  INTEGER NOT NULL REFERENCES professors,
    idSub   VARCHAR(100) NOT NULL REFERENCES subjects,

    PRIMARY KEY (idProf, idSub)
);

CREATE TABLE IF NOT EXISTS subjectsDegrees
(
    idSub   VARCHAR(100) NOT NULL REFERENCES subjects,
    semester INTEGER, -- puede ser null por las correlativas
    idDeg   INTEGER NOT NULL REFERENCES degrees,

    PRIMARY KEY (idSub, idDeg)
);

CREATE TABLE IF NOT EXISTS prereqSubjects
(
    idSub       VARCHAR(100) NOT NULL REFERENCES subjects,
    idPrereq    VARCHAR(100) NOT NULL REFERENCES subjects,

    PRIMARY KEY (idSub, idPrereq)
);

CREATE TABLE IF NOT EXISTS class
(
    idSub       VARCHAR(100) NOT NULL REFERENCES subjects,
    idClass     VARCHAR(100) NOT NULL,

    PRIMARY KEY (idSub,idClass)
);

CREATE TABLE IF NOT EXISTS classProfessors
(
    idSub       VARCHAR(100) NOT NULL,
    idClass     VARCHAR(100) NOT NULL,
    idProf      INTEGER NOT NULL REFERENCES professors,

    FOREIGN KEY (idSub, idClass) REFERENCES class(idsub,idClass),
    PRIMARY KEY (idSub,idClass, idProf)
);

CREATE TABLE IF NOT EXISTS classLocTime
(
    idLocTime   SERIAL PRIMARY KEY,
    idSub       VARCHAR(100) NOT NULL,
    idClass     VARCHAR(100) NOT NULL,
    day         INTEGER NOT NULL,
    startTime   TIME NOT NULL,
    endTime     TIME NOT NULL,
    class       VARCHAR(100) NOT NULL,
    building    VARCHAR(100) NOT NULL,
    mode        VARCHAR(100) NOT NULL,

    FOREIGN KEY (idSub, idClass) REFERENCES class(idsub,idClass)
);
INSERT INTO degrees(degname) VALUES('Ingenieria en Informatica');

INSERT INTO subjects VALUES (31.08, 'Sistemas de Representación', 'Ciencias Exactas y Naturales', 3);
INSERT INTO subjects VALUES (72.03, 'Introducción a la Informática', 'Sistemas Digitales y Datos', 3);
INSERT INTO subjects VALUES (93.26, 'Análisis Matemático I', 'Ciencias Exactas y Naturales', 6 );
INSERT INTO subjects VALUES (93.58, 'Álgebra', 'Ciencias Exactas y Naturales', 9);
INSERT INTO subjects VALUES (94.24, 'Metodología del Aprendizaje', 'Economia y Negocios', 3);
INSERT INTO subjects VALUES (72.31, 'Programación Imperativa', 'Sistemas Digitales y Datos', 9);
INSERT INTO subjects VALUES (93.28, 'Análisis Matemático II', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (93.41, 'Física I', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (93.59, 'Matemática Discreta', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (12.09, 'Química', 'Ciencias Exactas y Naturales', 3);
INSERT INTO subjects VALUES (72.32, 'Diseño y Procesamiento de Documentos XML', 'Sistemas Digitales y Datos', 3);
INSERT INTO subjects VALUES (72.33, 'Programación Orientada a Objetos', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (93.35, 'Lógica Computacional', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (93.42, 'Física II', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (72.08, 'Arquitectura de Computadoras', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.34, 'Estructura de Datos y Algoritmos', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (93.24, 'Probabilidad y Estadística', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (93.43, 'Física III', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES (72.11, 'Sistemas Operativos', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.35, 'Ingenieria de Software I', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.36, 'Interacción Hombre', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.37, 'Base de Datos I', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.07, 'Protocolos de Comunicación', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.38, 'Proyecto de Aplicaciones Web', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES (72.39, 'Autómatas, Teoría de Lenguajes y Compiladores', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES(93.07, 'Métodos Numéricos', 'Ciencias Exactas y Naturales', 3);
INSERT INTO subjects VALUES(94.21, 'Formación General I', 'Economia y Negocios', 3);
INSERT INTO subjects VALUES(94.51, 'Inglés I', '', 0);
INSERT INTO subjects VALUES(61.23, 'Economía para Ingenieros', 'Economia y Negocios', 3);
INSERT INTO subjects VALUES(61.32, 'Derecho para Ingenieros', 'Economia y Negocios', 3);
INSERT INTO subjects VALUES(72.40, 'Ingenieria del Software II', 'Sistemas Digitales y Datos', 3);
INSERT INTO subjects VALUES(72.41, 'Base de Datos II', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES(72.42, 'Programación de Objetos Distribuidos', 'Sistemas Digitales y Datos', 3);
INSERT INTO subjects VALUES(93.75, 'Métodos Numéricos Avanzados', 'Ciencias Exactas y Naturales', 6);
INSERT INTO subjects VALUES(72.25, 'Simulación de Sistemas', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES(72.27, 'Sistemas de Inteligencia Artificial', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES(72.43, 'Gestión de Proyectos Informáticos', 'Sistemas Digitales y Datos', 3);
INSERT INTO subjects VALUES(72.44, 'Criptografía y Seguridad', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES(12.83, 'Seguridad Ocupacional y Ambiental', 'Ambiente Y Movilidad', 3);
INSERT INTO subjects VALUES(72.45, 'Proyecto Final', 'Sistemas Digitales y Datos', 12);
INSERT INTO subjects VALUES(72.20, 'Redes de Información', 'Sistemas Digitales y Datos', 6);
INSERT INTO subjects VALUES(72.98, 'Práctica Laboral', '', 0);
INSERT INTO subjects VALUES(94.23, 'Formación General III', 'Economia y Negocios', 3);
INSERT INTO subjects VALUES(94.52, 'Inglés II', '', 0);


INSERT INTO prereqSubjects VALUES(72.31, 93.58);
INSERT INTO prereqSubjects VALUES(72.31, 72.03);
INSERT INTO prereqSubjects VALUES(93.28, 93.58);
INSERT INTO prereqSubjects VALUES(93.28, 93.26);
INSERT INTO prereqSubjects VALUES(93.41, 93.26);
INSERT INTO prereqSubjects VALUES(93.59, 93.58);
INSERT INTO prereqSubjects VALUES(72.32, 72.31);
INSERT INTO prereqSubjects VALUES(72.33, 72.31);
INSERT INTO prereqSubjects VALUES(93.35, 93.58);
INSERT INTO prereqSubjects VALUES(93.42, 93.28);
INSERT INTO prereqSubjects VALUES(72.08, 72.31);
INSERT INTO prereqSubjects VALUES(72.34, 93.59);
INSERT INTO prereqSubjects VALUES(72.34, 72.33);
INSERT INTO prereqSubjects VALUES(93.24, 93.28);
INSERT INTO prereqSubjects VALUES(93.43, 93.28);
INSERT INTO prereqSubjects VALUES(93.43, 93.41);
INSERT INTO prereqSubjects VALUES(72.11, 72.08);
INSERT INTO prereqSubjects VALUES(72.11, 72.34);
INSERT INTO prereqSubjects VALUES(72.35, 72.33);
INSERT INTO prereqSubjects VALUES(72.36, 72.33);
INSERT INTO prereqSubjects VALUES(72.36, 72.32);
INSERT INTO prereqSubjects VALUES(72.37, 72.34);
INSERT INTO prereqSubjects VALUES(72.37, 93.35);
INSERT INTO prereqSubjects VALUES(72.07, 72.11);
INSERT INTO prereqSubjects VALUES(72.38, 72.11);
INSERT INTO prereqSubjects VALUES(72.38, 72.37);
INSERT INTO prereqSubjects VALUES(72.38, 72.36);
INSERT INTO prereqSubjects VALUES(72.38, 72.35);
INSERT INTO prereqSubjects VALUES(72.39, 72.34);
INSERT INTO prereqSubjects VALUES(93.07, 93.28);
INSERT INTO prereqSubjects VALUES(61.23, 93.24);
INSERT INTO prereqSubjects VALUES(72.40, 72.38);
INSERT INTO prereqSubjects VALUES(72.40, 72.35);
INSERT INTO prereqSubjects VALUES(72.41, 72.11);
INSERT INTO prereqSubjects VALUES(72.41, 72.37);
INSERT INTO prereqSubjects VALUES(72.42, 72.07);
INSERT INTO prereqSubjects VALUES(72.42, 72.38);
INSERT INTO prereqSubjects VALUES(93.75, 93.07);
INSERT INTO prereqSubjects VALUES(72.25, 72.34);
INSERT INTO prereqSubjects VALUES(72.25, 93.07);
INSERT INTO prereqSubjects VALUES(72.27, 93.24);
INSERT INTO prereqSubjects VALUES(72.27, 72.34);
INSERT INTO prereqSubjects VALUES(72.43, 72.40);
INSERT INTO prereqSubjects VALUES(72.44, 72.07);
INSERT INTO prereqSubjects VALUES(72.44, 72.38);
INSERT INTO prereqSubjects VALUES(72.45, 72.43);
INSERT INTO prereqSubjects VALUES(72.20, 72.07);
INSERT INTO prereqSubjects VALUES(94.52, 94.51);


INSERT INTO professors(profname) VALUES
                                     ('De Piero, Maria Ines'),
                                     ('Pfister, Patricia Sylvina'),
                                     ('Ducrey, Mariana'),
                                     ('Giménez, Analía Paula'),
                                     ('Porcellana, Jorge Alberto'),
                                     ('Quiros, Dante Patricio'),
                                     ('Buquete, Maria Alejandra'),
                                     ('Turrin, Marcelo Emiliano'),
                                     ('Mancilla Aguilar, Jose Luis'),
                                     ('Florit, Patricia Teresa'),
                                     ('Fernández Sau, Mercedes'),
                                     ('Fraga, Claudia Elsa'),
                                     ('Francisco, Diego Hernán'),
                                     ('Garcia Serrano, Silvina Aida'),
                                     ('González Rodríguez, Rafael Ricardo'),
                                     ('Pérez, Luciano Alejo'),
                                     ('Benítez, Oscar Matías'),
                                     ('Fages, Luciano Rodolfo'),
                                     ('Noni, Maria Laura'),
                                     ('D´Alesio Souto, Sofía Nerina'),
                                     ('Epstein, Julián'),
                                     ('Oviedo, Martina Guadalupe'),
                                     ('Fages, Luciano Rodolfo'),
                                     ('Gayoso, Maria Celia'),
                                     ('Manterola, Sergio Daniel'),
                                     ('Buzzi, Paula Veronica'),
                                     ('Álvarez, Paola Marta'),
                                     ('Iwachow, María Alejandra'),
                                     ('Toranzo Calderón, Joaquín Santiago'),
                                     ('Garberoglio, Marcelo Fabio'),
                                     ('Arias Roig, Ana Maria'),
                                     ('Meola, Franco Román'),
                                     ('Boutet, Stella Maris'),
                                     ('Marti, Maria Elisabeth'),
                                     ('Pérez, Luciano Alejo'),
                                     ('Venturiello, Verónica'),
                                     ('Diaz, Alejandro Raul'),
                                     ('Medus, Andrés Daniel'),
                                     ('Perotti, Marcelo Gustavo'),
                                     ('Carnovale, Elsa Beatriz'),
                                     ('Di Lorenzo, Francisco Javier'),
                                     ('Hikawczuk, Pablo Alejandro'),
                                     ('Lopez, Daniel Marcelo'),
                                     ('Repossi, Patricia del Valle'),
                                     ('Vieytes, Roberto Eduardo'),
                                     ('Bobadilla, Joel Ivan'),
                                     ('Fernández Arancibia, Sol María'),
                                     ('Calahonra, Yamil Ariel'),
                                     ('Bonucci, Pablo Leandro'),
                                     ('Sosa, Marina'),
                                     ('Wilk, Bernardo Matías'),
                                     ('Testa Fernandez, Juan Jose'),
                                     ('Meich, Veronica Cecilia'),
                                     ('Vanarelli, Mauro Esteban'),
                                     ('Cammarata, Maria del mar'),
                                     ('Fontanella De Santis, Teresa Natalia'),
                                     ('Soliani Valeria Ines'),
                                     ('Meola, Franco Roman'),
                                     ('Bagini, Matias Eugenio'),
                                     ('Castro Pena'),
                                     ('Fernandez Sau, Mercedes'),
                                     ('Poggi, Facundo Sebastian'),
                                     ('Vega, Santiago'),
                                     ('Oviedo, Martina Guadalupe'),
                                     ('Konig, Pablo German'),
                                     ('Gigli, Miriam Laura'),
                                     ('Hikawczuk, Pablo Alejandro'),
                                     ('Palazzo, Edgardo'),
                                     ('Aita, Hugo Alberto'),
                                     ('Duvidovich, Laura Patricia'),
                                     ('Granado, Mauro'),
                                     ('Valles, Santiago Raul'),
                                     ('Merovich, Horacio Victor'),
                                     ('Ramos, Federico Gabriel'),
                                     ('Gomez, Leticia Irene'),
                                     ('Mader Blanco, Conrado Eugenio Fernando'),
                                     ('Staudenmann, Luis Alberto'),
                                     ('Fierens, Pablo Ignacio'),
                                     ('Hayes, Alejandro'),
                                     ('Pantazis, Lucio Jose'),
                                     ('Medus, Andres Daniel'),
                                     ('Alessio, Maria Gabriela'),
                                     ('Berlin, Guido'),
                                     ('Di Lorenzo, Francisco Javier'),
                                     ('Fernandez, Fernando Sergio'),
                                     ('Minces, Pablo Sebastian'),
                                     ('Oreglia, Eduardo Victor'),
                                     ('Sinardi, Norberto Antonio'),
                                     ('Bobadilla, Joel Ivan'),
                                     ('Florentin, Raul Felix'),
                                     ('Frank, Guillermo Alberto'),
                                     ('Lopez, Lucia'),
                                     ('Godio, Ariel'),
                                     ('Aquili, Alejo Ezequiel'),
                                     ('Mogni, Guido Matias'),
                                     ('Buquete, Maria Alejandra'),
                                     ('Botti, Federico Jose'),
                                     ('Dolagaratz, Gonzalo Matias'),
                                     ('Vilas, Carlos Emiliano'),
                                     ('Guerrero, Marcela Alejandra'),
                                     ('Rodriguez Babino, Cecilia'),
                                     ('Kulesz, Sebastian'),
                                     ('Sotuyo Dodero, Juan Martin'),
                                     ('Donofrio, Nicolas'),
                                     ('Sanguineti Arena, Francisco Javier'),
                                     ('Arias Roig, Ana Maria'),
                                     ('Ramele, Rodrigo Ezequiel'),
                                     ('Devoto, Jorge Andres'),
                                     ('Epstein, Julian'),
                                     ('Wilk, Bernardo Matias'),
                                     ('Dias Leimbacher, Enrique Daniel'),
                                     ('Calamante, Romina'),
                                     ('Cravino, Ana Maria'),
                                     ('Roldan, Juan Eduardo'),
                                     ('Coccolo, Pablo Bartolome'),
                                     ('Abayu, Jorge Alejandro'),
                                     ('Massucco, Gustavo'),
                                     ('Yanez, Esteban Hernan'),
                                     ('Lalanne, Santiago'),
                                     ('Palandella, Mauricio Guillermo'),
                                     ('Perego, Pablo Alejandro'),
                                     ('Blazques, Oscar Gustavo'),
                                     ('Pringles Canto, Cristina del Valle'),
                                     ('Garcia, Guillermo'),
                                     ('Aizemberg, Diego Ariel'),
                                     ('Benitez, Oscar Matias'),
                                     ('Parisi, Daniel Ricardo'),
                                     ('Patterson, German Agustin'),
                                     ('Wiebke, Lucas'),
                                     ('Pierri, Alan'),
                                     ('Bianchi, Luciano Gustavo'),
                                     ('Fuster, Marina'),
                                     ('Pineiro, Eugenia Sol'),
                                     ('Mon, Alicia Laura'),
                                     ('Carrodani, Luz Maria Isabel'),
                                     ('Abad, Pablo Eduardo'),
                                     ('Affranchino, Gustavo Osvaldo'),
                                     ('Ordonez, Hernan Dario'),
                                     ('Fernandez Velazco, Santiago'),
                                     ('Debrouvier, Hemilse'),
                                     ('Huerta, Jorge Ernseto'),
                                     ('Soliani, Valeria Ines'),
                                     ('Cortes Rodriguez, Kevin Imanol'),
                                     ('Ortiz, Pablo Gabriel'),
                                     ('Pedro, Cecilia Angelica'),
                                     ('Pekers, Paula'),
                                     ('Perez Aguirre, Florencia');


INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 31.08
FROM professors
WHERE professors.profName = 'De Piero, Maria Ines';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 31.08
FROM professors
WHERE professors.profName = 'Pfister, Patricia Sylvina';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 31.08
FROM professors
WHERE professors.profName = 'Ducrey, Mariana';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 31.08
FROM professors
WHERE professors.profName = 'Giménez, Analía Paula';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 31.08
FROM professors
WHERE professors.profName = 'Porcellana, Jorge Roberto';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 31.08
FROM professors
WHERE professors.profName = 'Quiros, Dante Patricio';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.03
FROM professors
WHERE professors.profName = 'Buquete, Maria Alejandra';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.03
FROM professors
WHERE professors.profName = 'Turrin, Marcelo Emilano';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Mancilla Aguilar, Jose Luis';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Florit, Patricia Teresa';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Fernández Sau, Mercedes';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Fraga, Claudia Elsa';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Francisco, Diego Hernán';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Garcia Serrano, Silvina Aida';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Gonzáles Rodríguez, Rafael Ricardo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Pérez, Luciano Alejo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Benítez, Oscar Matías';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.26
FROM professors
WHERE professors.profName = 'Fages, Luciano Rodolfo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.58
FROM professors
WHERE professors.profName = 'Noni, Maria Laura';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.58
FROM professors
WHERE professors.profName = 'D´Alesio Souto, Sofía Nerina';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.58
FROM professors
WHERE professors.profName = 'Epstein, Julían';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.58
FROM professors
WHERE professors.profName = 'Oviedo, Martina Guadalupe';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.58
FROM professors
WHERE professors.profName = 'Fages, Luciano Rodolfo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.24
FROM professors
WHERE professors.profName = 'Gayoso, Maria Celia';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.24
FROM professors
WHERE professors.profName = 'Manterola, Sergio Daniel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.24
FROM professors
WHERE professors.profName = 'Buzzi, Paula Veronica';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.24
FROM professors
WHERE professors.profName = 'Álvarez, Paola Marta';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.24
FROM professors
WHERE professors.profName = 'Iwachow, María Alejandra';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.24
FROM professors
WHERE professors.profName = 'Toranzo Calderón, Joaquín Santiago';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.31
FROM professors
WHERE professors.profName = 'Garberoglio, Marcelo Fabio';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.31
FROM professors
WHERE professors.profName = 'Arias Roig, Ana Maria';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.31
FROM professors
WHERE professors.profName = 'Meola, Franco Román';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.28
FROM professors
WHERE professors.profName = 'Boutet, Stella Maris';


INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.28
FROM professors
WHERE professors.profName = 'Marti, Maria Elisabeth';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.28
FROM professors
WHERE professors.profName = 'Pérez, Luciano Alejo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.28
FROM professors
WHERE professors.profName = 'Venturiello, Verónica';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Diaz, Alejandro Raul';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Medus, Andrés Daniel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Perotti, Marcelo Gustavo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Carnovale, Elsa Beatriz';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Di Lorenzo, Francisco Javier';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Hikawczuk, Pablo Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Lopez, Daniel Marcelo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Repossi, Patricia del Valle';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName ='Vieytes, Roberto Eduardo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.41
FROM professors
WHERE professors.profName = 'Bobadilla, Joel Ivan';

INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.41 FROM professors WHERE professors.profName = 'Fernández Arancibia, Sol María';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.41 FROM professors WHERE professors.profName = 'Calahonra, Yamil Ariel';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.59 FROM professors WHERE professors.profName = 'Bonucci, Pablo Leandro';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.59 FROM professors WHERE professors.profName = 'Sosa, Marina';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.59 FROM professors WHERE professors.profName = 'Wilk, Bernardo Matías';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 12.09 FROM professors WHERE professors.profName = 'Testa Fernandez, Juan Jose';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 12.09 FROM professors WHERE professors.profName = 'Meich, Veronica Cecilia';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 12.09 FROM professors WHERE professors.profName = 'Vanarelli, Mauro Esteban';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 12.09 FROM professors WHERE professors.profName = 'Cammarata, Maria del mar';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 72.32 FROM professors WHERE professors.profName = 'Fontanella De Santis, Teresa Natalia';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 72.32 FROM professors WHERE professors.profName = 'Soliani Valeria Ines';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 72.33 FROM professors WHERE professors.profName = 'Meola, Franco Roman';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 72.33 FROM professors WHERE professors.profName = 'Bagini, Matias Eugenio';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 72.33 FROM professors WHERE professors.profName = 'Castro Pena';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.35 FROM professors WHERE professors.profName = 'Noni, Maria Laura';
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.35 FROM professors WHERE professors.profName = 'Fernandez Sau, Mercedes';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.35
FROM professors
WHERE professors.profName = 'Poggi, Facundo Sebastian';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.35
FROM professors
WHERE professors.profName = 'Vega, Santiago';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.35
FROM professors
WHERE professors.profName = 'Oviedo, Martina Guadalupe';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Konig, Pablo German';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Diaz, Alejandro Raul';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Gigli, Miriam Laura';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Hikawczuk, Pablo Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Palazzo, Edgardo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Aita, Hugo Alberto';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Duvidovich, Laura Patricia';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.42
FROM professors
WHERE professors.profName = 'Granado, Mauro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.08
FROM professors
WHERE professors.profName = 'Valles, Santiago Raul';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.08
FROM professors
WHERE professors.profName = 'Merovich, Horacio Victor';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.08
FROM professors
WHERE professors.profName = 'Ramos, Federico Gabriel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.34
FROM professors
WHERE professors.profName = 'Gomez, Leticia Irene';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.34
FROM professors
WHERE professors.profName = 'Mader Blanco, Conrado Eugenio Fernando';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.34
FROM professors
WHERE professors.profName = 'Staudenmann, Luis Alberto';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.24
FROM professors
WHERE professors.profName = 'Fierens, Pablo Ignacio';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.24
FROM professors
WHERE professors.profName = 'Hayes, Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.24
FROM professors
WHERE professors.profName = 'Pantazis, Lucio Jose';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Medus, Andres Daniel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Alessio, Maria Gabriela';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Berlin, Guido';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Di Lorenzo, Francisco Javier';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Fernandez, Fernando Sergio';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Hikawczuk, Pablo Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Minces, Pablo Sebastian';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Oreglia, Eduardo Victor';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Sinardi, Norberto Antonio';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Bobadilla, Joel Ivan';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Florentin, Raul Felix';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Frank, Guillermo Alberto';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Granado, Mauro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.43
FROM professors
WHERE professors.profName = 'Lopez, Lucia';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.11
FROM professors
WHERE professors.profName = 'Godio, Ariel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.11
FROM professors
WHERE professors.profName = 'Aquili, Alejo Ezequiel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.11
FROM professors
WHERE professors.profName = 'Mogni, Guido Matias';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.35
FROM professors
WHERE professors.profName = 'Botti, Federico Jose';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.35
FROM professors
WHERE professors.profName = 'Buquete, Maria Alejandra';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.36
FROM professors
WHERE professors.profName = 'Dolagaratz, Gonzalo Matias';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.36
FROM professors
WHERE professors.profName = 'Vilas, Carlos Emiliano';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.36
FROM professors
WHERE professors.profName = 'Guerrero, Marcela Alejandra';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.37
FROM professors
WHERE professors.profName = 'Gomez, Leticia Irene';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.37
FROM professors
WHERE professors.profName = 'Rodriguez Babino, Cecilia';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.07
FROM professors
WHERE professors.profName = 'Garberoglio, Marcelo Fabio';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.07
FROM professors
WHERE professors.profName = 'Kulesz, Sebastian';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.38
FROM professors
WHERE professors.profName = 'Sotuyo Dodero, Juan Martin';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.38
FROM professors
WHERE professors.profName = 'Donofrio, Nicolas';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.38
FROM professors
WHERE professors.profName = 'Sanguineti Arena, Francisco Javier';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.39
FROM professors
WHERE professors.profName = 'Arias Roig, Ana Maria';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.39
FROM professors
WHERE professors.profName = 'Ramele, Rodrigo Ezequiel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.07
FROM professors
WHERE professors.profName = 'Devoto, Jorge Andres';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.07
FROM professors
WHERE professors.profName = 'Epstein, Julian';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.07
FROM professors
WHERE professors.profName = 'Oviedo, Martina Guadalupe';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.07
FROM professors
WHERE professors.profName = 'Wilk, Bernardo Matias';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.21
FROM professors
WHERE professors.profName = 'Dias Leimbacher, Enrique Daniel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.21
FROM professors
WHERE professors.profName = 'Calamante, Romina';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.21
FROM professors
WHERE professors.profName = 'Cravino, Ana Maria';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 94.21
FROM professors
WHERE professors.profName = 'Roldan, Juan Eduardo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.23
FROM professors
WHERE professors.profName = 'Coccolo, Pablo Bartolome';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.23
FROM professors
WHERE professors.profName = 'Abayu, Jorge Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.23
FROM professors
WHERE professors.profName = 'Massucco, Gustavo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.23
FROM professors
WHERE professors.profName = 'Yanez, Esteban Hernan';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.23
FROM professors
WHERE professors.profName = 'Lalanne, Santiago';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.23
FROM professors
WHERE professors.profName = 'Palandella, Mauricio Guillermo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.32
FROM professors
WHERE professors.profName = 'Perego, Pablo Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.32
FROM professors
WHERE professors.profName = 'Blazques, Oscar Gustavo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 61.32
FROM professors
WHERE professors.profName = 'Pringles Canto, Cristina del Valle';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.40
FROM professors
WHERE professors.profName = 'Sotuyo Dodero, Juan Martin';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.40
FROM professors
WHERE professors.profName = 'Garcia, Guillermo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.41
FROM professors
WHERE professors.profName = 'Aizemberg, Diego Ariel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.41
FROM professors
WHERE professors.profName = 'Rodriguez Babino, Cecilia';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.42
FROM professors
WHERE professors.profName = 'Meola, Franco Roman';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.42
FROM professors
WHERE professors.profName = 'Turrin, Marcelo Emiliano';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.75
FROM professors
WHERE professors.profName = 'Hayes, Alejandro';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 93.75
FROM professors
WHERE professors.profName = 'Benitez, Oscar Matias';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.25
FROM professors
WHERE professors.profName = 'Parisi, Daniel Ricardo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.25
FROM professors
WHERE professors.profName = 'Patterson, German Agustin';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.25
FROM professors
WHERE professors.profName = 'Wiebke, Lucas';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.27
FROM professors
WHERE professors.profName = 'Ramele, Rodrigo Ezequiel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.27
FROM professors
WHERE professors.profName = 'Pierri, Alan';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.27
FROM professors
WHERE professors.profName = 'Bianchi, Luciano Gustavo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.27
FROM professors
WHERE professors.profName = 'Fuster, Marina';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.27
FROM professors
WHERE professors.profName = 'Pineiro, Eugenia Sol';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.43
FROM professors
WHERE professors.profName = 'Mon, Alicia Laura';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.43
FROM professors
WHERE professors.profName = 'Carrodani, Luz Maria Isabel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.44
FROM professors
WHERE professors.profName = 'Abad, Pablo Eduardo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.44
FROM professors
WHERE professors.profName = 'Arias Roig, Ana Maria';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.44
FROM professors
WHERE professors.profName = 'Ramele, Rodrigo Ezequiel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 12.83
FROM professors
WHERE professors.profName = 'Affranchino, Gustavo Osvaldo';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 12.83
FROM professors
WHERE professors.profName = 'Ordonez, Hernan Dario';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 12.83
FROM professors
WHERE professors.profName = 'Fernandez Velazco, Santiago';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.45
FROM professors
WHERE professors.profName = 'Mon, Alicia Laura';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.45
FROM professors
WHERE professors.profName = 'Debrouvier, Hemilse';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.45
FROM professors
WHERE professors.profName = 'Huerta, Jorge Ernseto';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.45
FROM professors
WHERE professors.profName = 'Soliani, Valeria Ines';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Valles, Santiago Raul';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Staudenmann, Luis Alberto';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Cortes Rodriguez, Kevin Imanol';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Ortiz, Pablo Gabriel';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Pedro, Cecilia Angelica';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Calamante, Romina';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Pekers, Paula';

INSERT INTO professorsSubjects (idProf, idSub)
SELECT professors.id, 72.20
FROM professors
WHERE professors.profName = 'Perez Aguirre, Florencia';

INSERT INTO subjectsdegrees VALUES (31.08, 1, 1);
INSERT INTO subjectsdegrees VALUES (72.03, 1, 1);
INSERT INTO subjectsdegrees VALUES (93.26, 1, 1);
INSERT INTO subjectsdegrees VALUES (93.58, 1, 1);
INSERT INTO subjectsdegrees VALUES (94.24, 1, 1);
INSERT INTO subjectsdegrees VALUES (72.31, 2, 1);
INSERT INTO subjectsdegrees VALUES (93.28, 2, 1);
INSERT INTO subjectsdegrees VALUES (93.41, 2, 1);
INSERT INTO subjectsdegrees VALUES (93.59, 2, 1);
INSERT INTO subjectsdegrees VALUES (12.09, 3, 1);
INSERT INTO subjectsdegrees VALUES (72.32, 3, 1);
INSERT INTO subjectsdegrees VALUES (72.33, 3, 1);
INSERT INTO subjectsdegrees VALUES (93.35, 3, 1);
INSERT INTO subjectsdegrees VALUES (93.42, 3, 1);
INSERT INTO subjectsdegrees VALUES (72.08, 4, 1);
INSERT INTO subjectsdegrees VALUES (72.34, 4, 1);
INSERT INTO subjectsdegrees VALUES (93.24, 4, 1);
INSERT INTO subjectsdegrees VALUES (93.43, 4, 1);
INSERT INTO subjectsdegrees VALUES (72.11, 5, 1);
INSERT INTO subjectsdegrees VALUES (72.35, 5, 1);
INSERT INTO subjectsdegrees VALUES (72.36, 5, 1);
INSERT INTO subjectsdegrees VALUES (72.37, 5, 1);
INSERT INTO subjectsdegrees VALUES (72.07, 6, 1);
INSERT INTO subjectsdegrees VALUES (72.38, 6, 1);
INSERT INTO subjectsdegrees VALUES (72.39, 6, 1);
INSERT INTO subjectsdegrees VALUES (93.07, 6, 1);
INSERT INTO subjectsdegrees VALUES (94.21, 6, 1);
INSERT INTO subjectsdegrees VALUES (94.51, 6, 1);
INSERT INTO subjectsdegrees VALUES (61.23, 7, 1);
INSERT INTO subjectsdegrees VALUES (61.32, 7, 1);
INSERT INTO subjectsdegrees VALUES (72.40, 7, 1);
INSERT INTO subjectsdegrees VALUES (72.41, 7, 1);
INSERT INTO subjectsdegrees VALUES (72.42, 7, 1);
INSERT INTO subjectsdegrees VALUES (93.75, 7, 1);
INSERT INTO subjectsdegrees VALUES (72.25, 8, 1);
INSERT INTO subjectsdegrees VALUES (72.27, 8, 1);
INSERT INTO subjectsdegrees VALUES (72.43, 8, 1);
INSERT INTO subjectsdegrees VALUES (72.44, 8, 1);
INSERT INTO subjectsdegrees VALUES (12.83, 9, 1);
INSERT INTO subjectsdegrees VALUES (72.45, 9, 1);
INSERT INTO subjectsdegrees VALUES (72.20, 10, 1);
INSERT INTO subjectsdegrees VALUES (72.98, 10, 1);
INSERT INTO subjectsdegrees VALUES (94.23, 10, 1);
INSERT INTO subjectsdegrees VALUES (94.52, 10, 1);

INSERT INTO class VALUES('31.08','A');
INSERT INTO class VALUES('31.08','B');
INSERT INTO class VALUES('31.08','C');
INSERT INTO class VALUES('31.08','E');
INSERT INTO class VALUES('31.08','F');
INSERT INTO class VALUES('31.08','G');
INSERT INTO class VALUES('31.08','H');
INSERT INTO class VALUES('31.08','K');
INSERT INTO class VALUES('31.08','S');
INSERT INTO class VALUES('72.03','S');
INSERT INTO class VALUES('72.03','S1');
INSERT INTO class VALUES('93.26','A');
INSERT INTO class VALUES('93.26','B');
INSERT INTO class VALUES('93.26','C');
INSERT INTO class VALUES('93.26','D');
INSERT INTO class VALUES('93.26','E');
INSERT INTO class VALUES('93.26','F');
INSERT INTO class VALUES('93.26','G');
INSERT INTO class VALUES('93.26','H');
INSERT INTO class VALUES('93.58','A');
INSERT INTO class VALUES('93.58','B');
INSERT INTO class VALUES('93.58','C');
INSERT INTO class VALUES('94.24','A');
INSERT INTO class VALUES('94.24','C');
INSERT INTO class VALUES('94.24','DF');
INSERT INTO class VALUES('72.31','S');
INSERT INTO class VALUES('93.28','A');
INSERT INTO class VALUES('93.28','B');
INSERT INTO class VALUES('93.41','A');
INSERT INTO class VALUES('93.41','B');
INSERT INTO class VALUES('93.41','C');
INSERT INTO class VALUES('93.41','D');
INSERT INTO class VALUES('93.41','E');
INSERT INTO class VALUES('93.41','F');
INSERT INTO class VALUES('93.41','G');
INSERT INTO class VALUES('93.41','H');
INSERT INTO class VALUES('93.41','K');
INSERT INTO class VALUES('93.41','S');
INSERT INTO class VALUES('93.41','S1');
INSERT INTO class VALUES('93.59','A');
INSERT INTO class VALUES('93.59','B');
INSERT INTO class VALUES('12.09','S');
INSERT INTO class VALUES('12.09','S2');
INSERT INTO class VALUES('72.32','S');
INSERT INTO class VALUES('72.33','S');
INSERT INTO class VALUES('93.35','A');
INSERT INTO class VALUES('93.35','B');
INSERT INTO class VALUES('93.42','A');
INSERT INTO class VALUES('93.42','B');
INSERT INTO class VALUES('93.42','C');
INSERT INTO class VALUES('93.42','D');
INSERT INTO class VALUES('93.42','E');
INSERT INTO class VALUES('93.42','K');
INSERT INTO class VALUES('93.42','MN');
INSERT INTO class VALUES('93.42','S');
INSERT INTO class VALUES('72.08','S');
INSERT INTO class VALUES('72.34','S');
INSERT INTO class VALUES('93.24','A');
INSERT INTO class VALUES('93.24','B');
INSERT INTO class VALUES('93.43','A');
INSERT INTO class VALUES('93.43','B');
INSERT INTO class VALUES('93.43','C');
INSERT INTO class VALUES('93.43','D');
INSERT INTO class VALUES('93.43','E');
INSERT INTO class VALUES('93.43','MNP');
INSERT INTO class VALUES('93.43','Q');
INSERT INTO class VALUES('93.43','S');
INSERT INTO class VALUES('72.11','S');
INSERT INTO class VALUES('72.35','S');
INSERT INTO class VALUES('72.36','S');
INSERT INTO class VALUES('72.37','S');
INSERT INTO class VALUES('72.07','S');
INSERT INTO class VALUES('72.38','S');
INSERT INTO class VALUES('72.39','S');
INSERT INTO class VALUES('93.07','A');
INSERT INTO class VALUES('93.07','B');
INSERT INTO class VALUES('93.07','C');
INSERT INTO class VALUES('93.07','D');
INSERT INTO class VALUES('94.21','A');
INSERT INTO class VALUES('94.21','BK');
INSERT INTO class VALUES('94.21','C');
INSERT INTO class VALUES('94.21','D');
INSERT INTO class VALUES('94.21','E');
INSERT INTO class VALUES('94.21','H');
INSERT INTO class VALUES('61.23','K');
INSERT INTO class VALUES('61.23','KM');
INSERT INTO class VALUES('61.23','PQ');
INSERT INTO class VALUES('61.23','S');
INSERT INTO class VALUES('61.32','KS');
INSERT INTO class VALUES('61.32','SK');
INSERT INTO class VALUES('72.40','S');
INSERT INTO class VALUES('72.41','S');
INSERT INTO class VALUES('72.42','S');
INSERT INTO class VALUES('93.75','A');
INSERT INTO class VALUES('72.25','S');
INSERT INTO class VALUES('72.27','S');
INSERT INTO class VALUES('72.43','S');
INSERT INTO class VALUES('72.44','S');
INSERT INTO class VALUES('12.83','BIO');
INSERT INTO class VALUES('12.83','S');
INSERT INTO class VALUES('72.45','S');
INSERT INTO class VALUES('72.20','S');
INSERT INTO class VALUES('94.23','AB');
INSERT INTO class VALUES('94.23','C');
INSERT INTO class VALUES('94.23','KMP');

INSERT INTO classProfessors  SELECT '31.08','A', professors.id FROM professors WHERE professors.profName ='Ducrey, Mariana';
INSERT INTO classProfessors  SELECT '31.08','A', professors.id FROM professors WHERE professors.profName ='Pfister, Patricia Sylvina';
INSERT INTO classProfessors  SELECT '31.08','A', professors.id FROM professors WHERE professors.profName ='Porcellana, Jorge Alberto';
INSERT INTO classProfessors  SELECT '31.08','B', professors.id FROM professors WHERE professors.profName ='Ducrey, Mariana';
INSERT INTO classProfessors  SELECT '31.08','B', professors.id FROM professors WHERE professors.profName ='Pfister, Patricia Sylvina';
INSERT INTO classProfessors  SELECT '31.08','C', professors.id FROM professors WHERE professors.profName ='Ducrey, Mariana';
INSERT INTO classProfessors  SELECT '31.08','C', professors.id FROM professors WHERE professors.profName ='Pfister, Patricia Sylvina';
INSERT INTO classProfessors  SELECT '31.08','E', professors.id FROM professors WHERE professors.profName ='Giménez, Analía Paula';
INSERT INTO classProfessors  SELECT '31.08','E', professors.id FROM professors WHERE professors.profName ='Porcellana, Jorge Alberto';
INSERT INTO classProfessors  SELECT '31.08','F', professors.id FROM professors WHERE professors.profName ='Giménez, Analía Paula';
INSERT INTO classProfessors  SELECT '31.08','F', professors.id FROM professors WHERE professors.profName ='De Piero, Maria Ines';
INSERT INTO classProfessors  SELECT '31.08','G', professors.id FROM professors WHERE professors.profName ='De Piero, Maria Ines';
INSERT INTO classProfessors  SELECT '31.08','G', professors.id FROM professors WHERE professors.profName ='Porcellana, Jorge Alberto';
INSERT INTO classProfessors  SELECT '31.08','H', professors.id FROM professors WHERE professors.profName ='Ducrey, Mariana';
INSERT INTO classProfessors  SELECT '31.08','H', professors.id FROM professors WHERE professors.profName ='Pfister, Patricia Sylvina';
INSERT INTO classProfessors  SELECT '31.08','K', professors.id FROM professors WHERE professors.profName ='Pfister, Patricia Sylvina';
INSERT INTO classProfessors  SELECT '31.08','K', professors.id FROM professors WHERE professors.profName ='Quiros, Dante Patricio';
INSERT INTO classProfessors  SELECT '31.08','S', professors.id FROM professors WHERE professors.profName ='Ducrey, Mariana';
INSERT INTO classProfessors  SELECT '31.08','S', professors.id FROM professors WHERE professors.profName ='Pfister, Patricia Sylvina';
INSERT INTO classProfessors  SELECT '72.03','S', professors.id FROM professors WHERE professors.profName ='Turrin, Marcelo Emiliano';
INSERT INTO classProfessors  SELECT '72.03','S', professors.id FROM professors WHERE professors.profName ='Buquete, Maria Alejandra';
INSERT INTO classProfessors  SELECT '72.03','S1', professors.id FROM professors WHERE professors.profName ='Turrin, Marcelo Emiliano';
INSERT INTO classProfessors  SELECT '72.03','S1', professors.id FROM professors WHERE professors.profName ='Buquete, Maria Alejandra';
INSERT INTO classProfessors  SELECT '93.26','A', professors.id FROM professors WHERE professors.profName ='Mancilla Aguilar, Jose Luis';
INSERT INTO classProfessors  SELECT '93.26','A', professors.id FROM professors WHERE professors.profName ='Fernández Sau, Mercedes';
INSERT INTO classProfessors  SELECT '93.26','B', professors.id FROM professors WHERE professors.profName ='Garcia Serrano, Silvina Aida';
INSERT INTO classProfessors  SELECT '93.26','B', professors.id FROM professors WHERE professors.profName ='Benítez, Oscar Matías';
INSERT INTO classProfessors  SELECT '93.26','C', professors.id FROM professors WHERE professors.profName ='Fraga, Claudia Elsa';
INSERT INTO classProfessors  SELECT '93.26','C', professors.id FROM professors WHERE professors.profName ='Florit, Patricia Teresa';
INSERT INTO classProfessors  SELECT '93.26','D', professors.id FROM professors WHERE professors.profName ='Benítez, Oscar Matías';
INSERT INTO classProfessors  SELECT '93.26','D', professors.id FROM professors WHERE professors.profName ='Florit, Patricia Teresa';
INSERT INTO classProfessors  SELECT '93.26','E', professors.id FROM professors WHERE professors.profName ='Francisco, Diego Hernán';
INSERT INTO classProfessors  SELECT '93.26','E', professors.id FROM professors WHERE professors.profName ='Florit, Patricia Teresa';
INSERT INTO classProfessors  SELECT '93.26','F', professors.id FROM professors WHERE professors.profName ='González Rodríguez, Rafael Ricardo';
INSERT INTO classProfessors  SELECT '93.26','F', professors.id FROM professors WHERE professors.profName ='Fraga, Claudia Elsa';
INSERT INTO classProfessors  SELECT '93.26','G', professors.id FROM professors WHERE professors.profName ='Francisco, Diego Hernán';
INSERT INTO classProfessors  SELECT '93.26','G', professors.id FROM professors WHERE professors.profName ='Pérez, Luciano Alejo';
INSERT INTO classProfessors  SELECT '93.26','H', professors.id FROM professors WHERE professors.profName ='González Rodríguez, Rafael Ricardo';
INSERT INTO classProfessors  SELECT '93.26','H', professors.id FROM professors WHERE professors.profName ='Fages, Luciano Rodolfo';
INSERT INTO classProfessors  SELECT '93.58','A', professors.id FROM professors WHERE professors.profName ='D´Alesio Souto, Sofía Nerina';
INSERT INTO classProfessors  SELECT '93.58','A', professors.id FROM professors WHERE professors.profName ='Epstein, Julián';
INSERT INTO classProfessors  SELECT '93.58','B', professors.id FROM professors WHERE professors.profName ='Noni, Maria Laura';
INSERT INTO classProfessors  SELECT '93.58','B', professors.id FROM professors WHERE professors.profName ='Fages, Luciano Rodolfo';
INSERT INTO classProfessors  SELECT '93.58','C', professors.id FROM professors WHERE professors.profName ='Oviedo, Martina Guadalupe';
INSERT INTO classProfessors  SELECT '93.58','C', professors.id FROM professors WHERE professors.profName ='Epstein, Julián';
INSERT INTO classProfessors  SELECT '94.24','A', professors.id FROM professors WHERE professors.profName ='Iwachow, María Alejandra';
INSERT INTO classProfessors  SELECT '94.24','A', professors.id FROM professors WHERE professors.profName ='Álvarez, Paola Marta';
INSERT INTO classProfessors  SELECT '94.24','A', professors.id FROM professors WHERE professors.profName ='Gayoso, Maria Celia';
INSERT INTO classProfessors  SELECT '94.24','A', professors.id FROM professors WHERE professors.profName ='Buzzi, Paula Veronica';
INSERT INTO classProfessors  SELECT '94.24','A', professors.id FROM professors WHERE professors.profName ='Manterola, Sergio Daniel';
INSERT INTO classProfessors  SELECT '94.24','A', professors.id FROM professors WHERE professors.profName ='Toranzo Calderón, Joaquín Santiago';
INSERT INTO classProfessors  SELECT '94.24','C', professors.id FROM professors WHERE professors.profName ='Iwachow, María Alejandra';
INSERT INTO classProfessors  SELECT '94.24','C', professors.id FROM professors WHERE professors.profName ='Álvarez, Paola Marta';
INSERT INTO classProfessors  SELECT '94.24','C', professors.id FROM professors WHERE professors.profName ='Gayoso, Maria Celia';
INSERT INTO classProfessors  SELECT '94.24','C', professors.id FROM professors WHERE professors.profName ='Buzzi, Paula Veronica';
INSERT INTO classProfessors  SELECT '94.24','C', professors.id FROM professors WHERE professors.profName ='Manterola, Sergio Daniel';
INSERT INTO classProfessors  SELECT '94.24','C', professors.id FROM professors WHERE professors.profName ='Toranzo Calderón, Joaquín Santiago';
INSERT INTO classProfessors  SELECT '94.24','DF', professors.id FROM professors WHERE professors.profName ='Iwachow, María Alejandra';
INSERT INTO classProfessors  SELECT '94.24','DF', professors.id FROM professors WHERE professors.profName ='Álvarez, Paola Marta';
INSERT INTO classProfessors  SELECT '94.24','DF', professors.id FROM professors WHERE professors.profName ='Gayoso, Maria Celia';
INSERT INTO classProfessors  SELECT '94.24','DF', professors.id FROM professors WHERE professors.profName ='Buzzi, Paula Veronica';
INSERT INTO classProfessors  SELECT '94.24','DF', professors.id FROM professors WHERE professors.profName ='Manterola, Sergio Daniel';
INSERT INTO classProfessors  SELECT '94.24','DF', professors.id FROM professors WHERE professors.profName ='Toranzo Calderón, Joaquín Santiago';
INSERT INTO classProfessors  SELECT '72.31','S', professors.id FROM professors WHERE professors.profName ='Garberoglio, Marcelo Fabio';
INSERT INTO classProfessors  SELECT '72.31','S', professors.id FROM professors WHERE professors.profName ='Meola, Franco Román';
INSERT INTO classProfessors  SELECT '72.31','S', professors.id FROM professors WHERE professors.profName ='Arias Roig, Ana Maria';
INSERT INTO classProfessors  SELECT '93.28','A', professors.id FROM professors WHERE professors.profName ='Marti, Maria Elisabeth';
INSERT INTO classProfessors  SELECT '93.28','A', professors.id FROM professors WHERE professors.profName ='Pérez, Luciano Alejo';
INSERT INTO classProfessors  SELECT '93.28','A', professors.id FROM professors WHERE professors.profName ='Venturiello, Verónica';
INSERT INTO classProfessors  SELECT '93.28','B', professors.id FROM professors WHERE professors.profName ='Boutet, Stella Maris';
INSERT INTO classProfessors  SELECT '93.28','B', professors.id FROM professors WHERE professors.profName ='Pérez, Luciano Alejo';
INSERT INTO classProfessors  SELECT '93.41','A', professors.id FROM professors WHERE professors.profName ='Lopez, Daniel Marcelo';
INSERT INTO classProfessors  SELECT '93.41','A', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','B', professors.id FROM professors WHERE professors.profName ='Carnovale, Elsa Beatriz';
INSERT INTO classProfessors  SELECT '93.41','B', professors.id FROM professors WHERE professors.profName ='Lopez, Daniel Marcelo';
INSERT INTO classProfessors  SELECT '93.41','B', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','B', professors.id FROM professors WHERE professors.profName ='Fernández Arancibia, Sol María';
INSERT INTO classProfessors  SELECT '93.41','C', professors.id FROM professors WHERE professors.profName ='Carnovale, Elsa Beatriz';
INSERT INTO classProfessors  SELECT '93.41','C', professors.id FROM professors WHERE professors.profName ='Diaz, Alejandro Raul';
INSERT INTO classProfessors  SELECT '93.41','C', professors.id FROM professors WHERE professors.profName ='Repossi, Patricia del Valle';
INSERT INTO classProfessors  SELECT '93.41','D', professors.id FROM professors WHERE professors.profName ='Carnovale, Elsa Beatriz';
INSERT INTO classProfessors  SELECT '93.41','D', professors.id FROM professors WHERE professors.profName ='Diaz, Alejandro Raul';
INSERT INTO classProfessors  SELECT '93.41','D', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','D', professors.id FROM professors WHERE professors.profName ='Perotti, Marcelo Gustavo';
INSERT INTO classProfessors  SELECT '93.41','D', professors.id FROM professors WHERE professors.profName ='Repossi, Patricia del Valle';
INSERT INTO classProfessors  SELECT '93.41','E', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.41','E', professors.id FROM professors WHERE professors.profName ='Carnovale, Elsa Beatriz';
INSERT INTO classProfessors  SELECT '93.41','E', professors.id FROM professors WHERE professors.profName ='Bobadilla, Joel Ivan';
INSERT INTO classProfessors  SELECT '93.41','E', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','F', professors.id FROM professors WHERE professors.profName ='Di Lorenzo, Francisco Javier';
INSERT INTO classProfessors  SELECT '93.41','F', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','G', professors.id FROM professors WHERE professors.profName ='Carnovale, Elsa Beatriz';
INSERT INTO classProfessors  SELECT '93.41','G', professors.id FROM professors WHERE professors.profName ='Diaz, Alejandro Raul';
INSERT INTO classProfessors  SELECT '93.41','G', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','G', professors.id FROM professors WHERE professors.profName ='Repossi, Patricia del Valle';
INSERT INTO classProfessors  SELECT '93.41','H', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.41','H', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','H', professors.id FROM professors WHERE professors.profName ='Perotti, Marcelo Gustavo';
INSERT INTO classProfessors  SELECT '93.41','H', professors.id FROM professors WHERE professors.profName ='Fernández Arancibia, Sol María';
INSERT INTO classProfessors  SELECT '93.41','K', professors.id FROM professors WHERE professors.profName ='Carnovale, Elsa Beatriz';
INSERT INTO classProfessors  SELECT '93.41','K', professors.id FROM professors WHERE professors.profName ='Diaz, Alejandro Raul';
INSERT INTO classProfessors  SELECT '93.41','K', professors.id FROM professors WHERE professors.profName ='Perotti, Marcelo Gustavo';
INSERT INTO classProfessors  SELECT '93.41','K', professors.id FROM professors WHERE professors.profName ='Repossi, Patricia del Valle';
INSERT INTO classProfessors  SELECT '93.41','S', professors.id FROM professors WHERE professors.profName ='Bobadilla, Joel Ivan';
INSERT INTO classProfessors  SELECT '93.41','S', professors.id FROM professors WHERE professors.profName ='Vieytes, Roberto Eduardo';
INSERT INTO classProfessors  SELECT '93.41','S', professors.id FROM professors WHERE professors.profName ='Perotti, Marcelo Gustavo';
INSERT INTO classProfessors  SELECT '93.41','S', professors.id FROM professors WHERE professors.profName ='Repossi, Patricia del Valle';
INSERT INTO classProfessors  SELECT '93.41','S1', professors.id FROM professors WHERE professors.profName ='Diaz, Alejandro Raul';
INSERT INTO classProfessors  SELECT '93.41','S1', professors.id FROM professors WHERE professors.profName ='Di Lorenzo, Francisco Javier';
INSERT INTO classProfessors  SELECT '93.41','S1', professors.id FROM professors WHERE professors.profName ='Perotti, Marcelo Gustavo';
INSERT INTO classProfessors  SELECT '93.59','A', professors.id FROM professors WHERE professors.profName ='Bonucci, Pablo Leandro';
INSERT INTO classProfessors  SELECT '93.59','A', professors.id FROM professors WHERE professors.profName ='Sosa, Marina';
INSERT INTO classProfessors  SELECT '93.59','A', professors.id FROM professors WHERE professors.profName ='Wilk, Bernardo Matías';
INSERT INTO classProfessors  SELECT '93.59','B', professors.id FROM professors WHERE professors.profName ='Bonucci, Pablo Leandro';
INSERT INTO classProfessors  SELECT '93.59','B', professors.id FROM professors WHERE professors.profName ='Sosa, Marina';
INSERT INTO classProfessors  SELECT '93.59','B', professors.id FROM professors WHERE professors.profName ='Wilk, Bernardo Matías';
INSERT INTO classProfessors  SELECT '12.09','S', professors.id FROM professors WHERE professors.profName ='Testa Fernandez, Juan Jose';
INSERT INTO classProfessors  SELECT '12.09','S', professors.id FROM professors WHERE professors.profName ='Vanarelli, Mauro Esteban';
INSERT INTO classProfessors  SELECT '12.09','S2', professors.id FROM professors WHERE professors.profName ='Cammarata, María del mar';
INSERT INTO classProfessors  SELECT '12.09','S2', professors.id FROM professors WHERE professors.profName ='Meich, Veronica Cecilia';
INSERT INTO classProfessors  SELECT '72.32','S', professors.id FROM professors WHERE professors.profName ='Fontanella De Santis, Teresa Natalia';
INSERT INTO classProfessors  SELECT '72.32','S', professors.id FROM professors WHERE professors.profName ='Soliani, Valeria Inés';
INSERT INTO classProfessors  SELECT '72.33','S', professors.id FROM professors WHERE professors.profName ='Bagini, Matias Eugenio';
INSERT INTO classProfessors  SELECT '72.33','S', professors.id FROM professors WHERE professors.profName ='Castro Peña, Gonzalo';
INSERT INTO classProfessors  SELECT '72.33','S', professors.id FROM professors WHERE professors.profName ='Meola, Franco Román';
INSERT INTO classProfessors  SELECT '93.35','A', professors.id FROM professors WHERE professors.profName ='Fernández Sau, Mercedes';
INSERT INTO classProfessors  SELECT '93.35','A', professors.id FROM professors WHERE professors.profName ='Noni, Maria Laura';
INSERT INTO classProfessors  SELECT '93.35','B', professors.id FROM professors WHERE professors.profName ='Poggi, Facundo Sebastian';
INSERT INTO classProfessors  SELECT '93.35','B', professors.id FROM professors WHERE professors.profName ='Oviedo, Martina Guadalupe';
INSERT INTO classProfessors  SELECT '93.35','B', professors.id FROM professors WHERE professors.profName ='Vega, Santiago';
INSERT INTO classProfessors  SELECT '93.42','A', professors.id FROM professors WHERE professors.profName ='Gigli, Miriam Laura';
INSERT INTO classProfessors  SELECT '93.42','A', professors.id FROM professors WHERE professors.profName ='König, Pablo Germán';
INSERT INTO classProfessors  SELECT '93.42','A', professors.id FROM professors WHERE professors.profName ='AITA, HUGO ALBERTO';
INSERT INTO classProfessors  SELECT '93.42','B', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.42','B', professors.id FROM professors WHERE professors.profName ='Gigli, Miriam Laura';
INSERT INTO classProfessors  SELECT '93.42','B', professors.id FROM professors WHERE professors.profName ='Palazzo, Edgardo';
INSERT INTO classProfessors  SELECT '93.42','B', professors.id FROM professors WHERE professors.profName ='AITA, HUGO ALBERTO';
INSERT INTO classProfessors  SELECT '93.42','C', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.42','C', professors.id FROM professors WHERE professors.profName ='Gigli, Miriam Laura';
INSERT INTO classProfessors  SELECT '93.42','C', professors.id FROM professors WHERE professors.profName ='Palazzo, Edgardo';
INSERT INTO classProfessors  SELECT '93.42','C', professors.id FROM professors WHERE professors.profName ='AITA, HUGO ALBERTO';
INSERT INTO classProfessors  SELECT '93.42','D', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.42','D', professors.id FROM professors WHERE professors.profName ='Diaz, Alejandro Raul';
INSERT INTO classProfessors  SELECT '93.42','D', professors.id FROM professors WHERE professors.profName ='Palazzo, Edgardo';
INSERT INTO classProfessors  SELECT '93.42','D', professors.id FROM professors WHERE professors.profName ='AITA, HUGO ALBERTO';
INSERT INTO classProfessors  SELECT '93.42','E', professors.id FROM professors WHERE professors.profName ='DUVIDOVICH, LAURA PATRICIA';
INSERT INTO classProfessors  SELECT '93.42','E', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.42','E', professors.id FROM professors WHERE professors.profName ='König, Pablo Germán';
INSERT INTO classProfessors  SELECT '93.42','K', professors.id FROM professors WHERE professors.profName ='DUVIDOVICH, LAURA PATRICIA';
INSERT INTO classProfessors  SELECT '93.42','K', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.42','K', professors.id FROM professors WHERE professors.profName ='Palazzo, Edgardo';
INSERT INTO classProfessors  SELECT '93.42','K', professors.id FROM professors WHERE professors.profName ='AITA, HUGO ALBERTO';
INSERT INTO classProfessors  SELECT '93.42','MN', professors.id FROM professors WHERE professors.profName ='DUVIDOVICH, LAURA PATRICIA';
INSERT INTO classProfessors  SELECT '93.42','MN', professors.id FROM professors WHERE professors.profName ='Gigli, Miriam Laura';
INSERT INTO classProfessors  SELECT '93.42','MN', professors.id FROM professors WHERE professors.profName ='König, Pablo Germán';
INSERT INTO classProfessors  SELECT '93.42','MN', professors.id FROM professors WHERE professors.profName ='AITA, HUGO ALBERTO';
INSERT INTO classProfessors  SELECT '93.42','S', professors.id FROM professors WHERE professors.profName ='DUVIDOVICH, LAURA PATRICIA';
INSERT INTO classProfessors  SELECT '93.42','S', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '72.08','S', professors.id FROM professors WHERE professors.profName ='Merovich, Horacio Víctor';
INSERT INTO classProfessors  SELECT '72.08','S', professors.id FROM professors WHERE professors.profName ='Ramos, Federico Gabriel';
INSERT INTO classProfessors  SELECT '72.08','S', professors.id FROM professors WHERE professors.profName ='Vallés, Santiago Raúl';
INSERT INTO classProfessors  SELECT '72.34','S', professors.id FROM professors WHERE professors.profName ='Mader Blanco, Conrado Eugenio Fernando';
INSERT INTO classProfessors  SELECT '72.34','S', professors.id FROM professors WHERE professors.profName ='Staudenmann, Luis Alberto';
INSERT INTO classProfessors  SELECT '72.34','S', professors.id FROM professors WHERE professors.profName ='Gomez, Leticia Irene';
INSERT INTO classProfessors  SELECT '93.24','A', professors.id FROM professors WHERE professors.profName ='Fierens, Pablo Ignacio';
INSERT INTO classProfessors  SELECT '93.24','A', professors.id FROM professors WHERE professors.profName ='Pantazis, Lucio José';
INSERT INTO classProfessors  SELECT '93.24','B', professors.id FROM professors WHERE professors.profName ='Hayes, Alejandro';
INSERT INTO classProfessors  SELECT '93.24','B', professors.id FROM professors WHERE professors.profName ='Fierens, Pablo Ignacio';
INSERT INTO classProfessors  SELECT '93.43','A', professors.id FROM professors WHERE professors.profName ='Fernandez, Fernando Sergio';
INSERT INTO classProfessors  SELECT '93.43','A', professors.id FROM professors WHERE professors.profName ='Oreglia, Eduardo Victor';
INSERT INTO classProfessors  SELECT '93.43','A', professors.id FROM professors WHERE professors.profName ='Minces, Pablo Sebastián';
INSERT INTO classProfessors  SELECT '93.43','A', professors.id FROM professors WHERE professors.profName ='Bobadilla, Joel Ivan';
INSERT INTO classProfessors  SELECT '93.43','A', professors.id FROM professors WHERE professors.profName ='Sinardi, Norberto Antonio';
INSERT INTO classProfessors  SELECT '93.43','B', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','B', professors.id FROM professors WHERE professors.profName ='Fernandez, Fernando Sergio';
INSERT INTO classProfessors  SELECT '93.43','B', professors.id FROM professors WHERE professors.profName ='Minces, Pablo Sebastián';
INSERT INTO classProfessors  SELECT '93.43','B', professors.id FROM professors WHERE professors.profName ='Bobadilla, Joel Ivan';
INSERT INTO classProfessors  SELECT '93.43','C', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','C', professors.id FROM professors WHERE professors.profName ='Oreglia, Eduardo Victor';
INSERT INTO classProfessors  SELECT '93.43','C', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.43','D', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','D', professors.id FROM professors WHERE professors.profName ='Alessio, Maria Gabriela';
INSERT INTO classProfessors  SELECT '93.43','D', professors.id FROM professors WHERE professors.profName ='Oreglia, Eduardo Victor';
INSERT INTO classProfessors  SELECT '93.43','D', professors.id FROM professors WHERE professors.profName ='Granado, Mauro';
INSERT INTO classProfessors  SELECT '93.43','D', professors.id FROM professors WHERE professors.profName ='Sinardi, Norberto Antonio';
INSERT INTO classProfessors  SELECT '93.43','E', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','E', professors.id FROM professors WHERE professors.profName ='Florentín, Raúl Félix';
INSERT INTO classProfessors  SELECT '93.43','E', professors.id FROM professors WHERE professors.profName ='Bobadilla, Joel Ivan';
INSERT INTO classProfessors  SELECT '93.43','E', professors.id FROM professors WHERE professors.profName ='Sinardi, Norberto Antonio';
INSERT INTO classProfessors  SELECT '93.43','MNP', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','MNP', professors.id FROM professors WHERE professors.profName ='Florentín, Raúl Félix';
INSERT INTO classProfessors  SELECT '93.43','MNP', professors.id FROM professors WHERE professors.profName ='Lopez, Lucía F.';
INSERT INTO classProfessors  SELECT '93.43','MNP', professors.id FROM professors WHERE professors.profName ='Berlin, Guido';
INSERT INTO classProfessors  SELECT '93.43','Q', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','Q', professors.id FROM professors WHERE professors.profName ='Di Lorenzo, Francisco Javier';
INSERT INTO classProfessors  SELECT '93.43','Q', professors.id FROM professors WHERE professors.profName ='Alessio, Maria Gabriela';
INSERT INTO classProfessors  SELECT '93.43','Q', professors.id FROM professors WHERE professors.profName ='Frank, Guillermo Alberto';
INSERT INTO classProfessors  SELECT '93.43','Q', professors.id FROM professors WHERE professors.profName ='Oreglia, Eduardo Victor';
INSERT INTO classProfessors  SELECT '93.43','S', professors.id FROM professors WHERE professors.profName ='Medus, Andrés Daniel';
INSERT INTO classProfessors  SELECT '93.43','S', professors.id FROM professors WHERE professors.profName ='Di Lorenzo, Francisco Javier';
INSERT INTO classProfessors  SELECT '93.43','S', professors.id FROM professors WHERE professors.profName ='Frank, Guillermo Alberto';
INSERT INTO classProfessors  SELECT '93.43','S', professors.id FROM professors WHERE professors.profName ='Berlin, Guido';
INSERT INTO classProfessors  SELECT '93.43','S', professors.id FROM professors WHERE professors.profName ='Sinardi, Norberto Antonio';
INSERT INTO classProfessors  SELECT '72.11','S', professors.id FROM professors WHERE professors.profName ='Mogni, Guido Matías';
INSERT INTO classProfessors  SELECT '72.11','S', professors.id FROM professors WHERE professors.profName ='AQUILI, ALEJO EZEQUIEL';
INSERT INTO classProfessors  SELECT '72.11','S', professors.id FROM professors WHERE professors.profName ='Godio, Ariel';
INSERT INTO classProfessors  SELECT '72.35','S', professors.id FROM professors WHERE professors.profName ='Buquete, Maria Alejandra';
INSERT INTO classProfessors  SELECT '72.35','S', professors.id FROM professors WHERE professors.profName ='Botti, Federico José';
INSERT INTO classProfessors  SELECT '72.36','S', professors.id FROM professors WHERE professors.profName ='Dolagaratz, Gonzalo Matias';
INSERT INTO classProfessors  SELECT '72.36','S', professors.id FROM professors WHERE professors.profName ='Guerrero, Marcela Alejandra';
INSERT INTO classProfessors  SELECT '72.36','S', professors.id FROM professors WHERE professors.profName ='Vilas, Carlos Emiliano';
INSERT INTO classProfessors  SELECT '72.37','S', professors.id FROM professors WHERE professors.profName ='Rodriguez Babino, Cecilia';
INSERT INTO classProfessors  SELECT '72.37','S', professors.id FROM professors WHERE professors.profName ='Gomez, Leticia Irene';
INSERT INTO classProfessors  SELECT '72.07','S', professors.id FROM professors WHERE professors.profName ='Garberoglio, Marcelo Fabio';
INSERT INTO classProfessors  SELECT '72.07','S', professors.id FROM professors WHERE professors.profName ='Kulesz, Sebastian';
INSERT INTO classProfessors  SELECT '72.38','S', professors.id FROM professors WHERE professors.profName ='Sotuyo Dodero, Juan Martin';
INSERT INTO classProfessors  SELECT '72.38','S', professors.id FROM professors WHERE professors.profName ='SANGUINETI ARENA, FRANCISCO JAVIER';
INSERT INTO classProfessors  SELECT '72.38','S', professors.id FROM professors WHERE professors.profName ='D´ONOFRIO, NICOLÁS';
INSERT INTO classProfessors  SELECT '72.39','S', professors.id FROM professors WHERE professors.profName ='Arias Roig, Ana Maria';
INSERT INTO classProfessors  SELECT '72.39','S', professors.id FROM professors WHERE professors.profName ='Ramele, Rodrigo Ezequiel';
INSERT INTO classProfessors  SELECT '93.07','A', professors.id FROM professors WHERE professors.profName ='Epstein, Julián';
INSERT INTO classProfessors  SELECT '93.07','A', professors.id FROM professors WHERE professors.profName ='Devoto, Jorge Andres';
INSERT INTO classProfessors  SELECT '93.07','B', professors.id FROM professors WHERE professors.profName ='Wilk, Bernardo Matías';
INSERT INTO classProfessors  SELECT '93.07','B', professors.id FROM professors WHERE professors.profName ='Devoto, Jorge Andres';
INSERT INTO classProfessors  SELECT '93.07','C', professors.id FROM professors WHERE professors.profName ='Oviedo, Martina Guadalupe';
INSERT INTO classProfessors  SELECT '93.07','C', professors.id FROM professors WHERE professors.profName ='Devoto, Jorge Andres';
INSERT INTO classProfessors  SELECT '93.07','D', professors.id FROM professors WHERE professors.profName ='Epstein, Julián';
INSERT INTO classProfessors  SELECT '93.07','D', professors.id FROM professors WHERE professors.profName ='Wilk, Bernardo Matías';
INSERT INTO classProfessors  SELECT '93.07','D', professors.id FROM professors WHERE professors.profName ='Devoto, Jorge Andres';
INSERT INTO classProfessors  SELECT '94.21','A', professors.id FROM professors WHERE professors.profName ='Roldan, Juan Eduardo';
INSERT INTO classProfessors  SELECT '94.21','A', professors.id FROM professors WHERE professors.profName ='DIAZ LEIMBACHER, ENRIQUE DANIEL';
INSERT INTO classProfessors  SELECT '94.21','BK', professors.id FROM professors WHERE professors.profName ='Roldan, Juan Eduardo';
INSERT INTO classProfessors  SELECT '94.21','BK', professors.id FROM professors WHERE professors.profName ='DIAZ LEIMBACHER, ENRIQUE DANIEL';
INSERT INTO classProfessors  SELECT '94.21','C', professors.id FROM professors WHERE professors.profName ='Roldan, Juan Eduardo';
INSERT INTO classProfessors  SELECT '94.21','C', professors.id FROM professors WHERE professors.profName ='DIAZ LEIMBACHER, ENRIQUE DANIEL';
INSERT INTO classProfessors  SELECT '94.21','D', professors.id FROM professors WHERE professors.profName ='Roldan, Juan Eduardo';
INSERT INTO classProfessors  SELECT '94.21','D', professors.id FROM professors WHERE professors.profName ='DIAZ LEIMBACHER, ENRIQUE DANIEL';
INSERT INTO classProfessors  SELECT '94.21','E', professors.id FROM professors WHERE professors.profName ='Cravino, Ana Maria';
INSERT INTO classProfessors  SELECT '94.21','E', professors.id FROM professors WHERE professors.profName ='DIAZ LEIMBACHER, ENRIQUE DANIEL';
INSERT INTO classProfessors  SELECT '94.21','H', professors.id FROM professors WHERE professors.profName ='Roldan, Juan Eduardo';
INSERT INTO classProfessors  SELECT '94.21','H', professors.id FROM professors WHERE professors.profName ='DIAZ LEIMBACHER, ENRIQUE DANIEL';
INSERT INTO classProfessors  SELECT '61.23','K', professors.id FROM professors WHERE professors.profName ='Palandella, Mauricio Guillermo';
INSERT INTO classProfessors  SELECT '61.23','K', professors.id FROM professors WHERE professors.profName ='Coccolo, Pablo Bartolome';
INSERT INTO classProfessors  SELECT '61.23','KM', professors.id FROM professors WHERE professors.profName ='Lalanne, Santiago';
INSERT INTO classProfessors  SELECT '61.23','KM', professors.id FROM professors WHERE professors.profName ='Abayu, Jorge Alejandro';
INSERT INTO classProfessors  SELECT '61.23','KM', professors.id FROM professors WHERE professors.profName ='Massucco, Gustavo';
INSERT INTO classProfessors  SELECT '61.23','KM', professors.id FROM professors WHERE professors.profName ='Yáñez, Esteban Hernan';
INSERT INTO classProfessors  SELECT '61.23','KM', professors.id FROM professors WHERE professors.profName ='Coccolo, Pablo Bartolome';
INSERT INTO classProfessors  SELECT '61.23','PQ', professors.id FROM professors WHERE professors.profName ='Lalanne, Santiago';
INSERT INTO classProfessors  SELECT '61.23','PQ', professors.id FROM professors WHERE professors.profName ='Abayu, Jorge Alejandro';
INSERT INTO classProfessors  SELECT '61.23','PQ', professors.id FROM professors WHERE professors.profName ='Massucco, Gustavo';
INSERT INTO classProfessors  SELECT '61.23','PQ', professors.id FROM professors WHERE professors.profName ='Yáñez, Esteban Hernan';
INSERT INTO classProfessors  SELECT '61.23','PQ', professors.id FROM professors WHERE professors.profName ='Coccolo, Pablo Bartolome';
INSERT INTO classProfessors  SELECT '61.23','S', professors.id FROM professors WHERE professors.profName ='Palandella, Mauricio Guillermo';
INSERT INTO classProfessors  SELECT '61.23','S', professors.id FROM professors WHERE professors.profName ='Abayu, Jorge Alejandro';
INSERT INTO classProfessors  SELECT '61.23','S', professors.id FROM professors WHERE professors.profName ='Coccolo, Pablo Bartolome';
INSERT INTO classProfessors  SELECT '61.32','KS', professors.id FROM professors WHERE professors.profName ='Blázquez, Oscar Gustavo';
INSERT INTO classProfessors  SELECT '61.32','KS', professors.id FROM professors WHERE professors.profName ='Perego, Pablo Alejandro';
INSERT INTO classProfessors  SELECT '61.32','SK', professors.id FROM professors WHERE professors.profName ='Blázquez, Oscar Gustavo';
INSERT INTO classProfessors  SELECT '61.32','SK', professors.id FROM professors WHERE professors.profName ='Perego, Pablo Alejandro';
INSERT INTO classProfessors  SELECT '61.32','SK', professors.id FROM professors WHERE professors.profName ='Pringles Canto, Cristina del Valle';
INSERT INTO classProfessors  SELECT '72.40','S', professors.id FROM professors WHERE professors.profName ='García, Guillermo';
INSERT INTO classProfessors  SELECT '72.40','S', professors.id FROM professors WHERE professors.profName ='Sotuyo Dodero, Juan Martin';
INSERT INTO classProfessors  SELECT '72.41','S', professors.id FROM professors WHERE professors.profName ='Aizemberg, Diego Ariel';
INSERT INTO classProfessors  SELECT '72.41','S', professors.id FROM professors WHERE professors.profName ='Rodriguez Babino, Cecilia';
INSERT INTO classProfessors  SELECT '72.42','S', professors.id FROM professors WHERE professors.profName ='Turrin, Marcelo Emiliano';
INSERT INTO classProfessors  SELECT '72.42','S', professors.id FROM professors WHERE professors.profName ='Meola, Franco Román';
INSERT INTO classProfessors  SELECT '93.75','A', professors.id FROM professors WHERE professors.profName ='Hayes, Alejandro';
INSERT INTO classProfessors  SELECT '93.75','A', professors.id FROM professors WHERE professors.profName ='Benítez, Oscar Matías';
INSERT INTO classProfessors  SELECT '72.25','S', professors.id FROM professors WHERE professors.profName ='Patterson, Germán Agustín';
INSERT INTO classProfessors  SELECT '72.25','S', professors.id FROM professors WHERE professors.profName ='Parisi, Daniel Ricardo';
INSERT INTO classProfessors  SELECT '72.25','S', professors.id FROM professors WHERE professors.profName ='Wiebke, Lucas';
INSERT INTO classProfessors  SELECT '72.27','S', professors.id FROM professors WHERE professors.profName ='Pierri, Alan';
INSERT INTO classProfessors  SELECT '72.27','S', professors.id FROM professors WHERE professors.profName ='FUSTER, MARINA';
INSERT INTO classProfessors  SELECT '72.27','S', professors.id FROM professors WHERE professors.profName ='Ramele, Rodrigo Ezequiel';
INSERT INTO classProfessors  SELECT '72.27','S', professors.id FROM professors WHERE professors.profName ='PIÑEIRO, EUGENIA SOL';
INSERT INTO classProfessors  SELECT '72.27','S', professors.id FROM professors WHERE professors.profName ='BIANCHI, LUCIANO GUSTAVO';
INSERT INTO classProfessors  SELECT '72.43','S', professors.id FROM professors WHERE professors.profName ='Carrodani, Luz María Isabel';
INSERT INTO classProfessors  SELECT '72.43','S', professors.id FROM professors WHERE professors.profName ='Mon, Alicia Laura';
INSERT INTO classProfessors  SELECT '72.44','S', professors.id FROM professors WHERE professors.profName ='Arias Roig, Ana Maria';
INSERT INTO classProfessors  SELECT '72.44','S', professors.id FROM professors WHERE professors.profName ='Ramele, Rodrigo Ezequiel';
INSERT INTO classProfessors  SELECT '72.44','S', professors.id FROM professors WHERE professors.profName ='Abad, Pablo Eduardo';
INSERT INTO classProfessors  SELECT '12.83','BIO', professors.id FROM professors WHERE professors.profName ='Fernandez Velazco, Santiago';
INSERT INTO classProfessors  SELECT '12.83','BIO', professors.id FROM professors WHERE professors.profName ='Ordoñez, Hernán Darío';
INSERT INTO classProfessors  SELECT '12.83','BIO', professors.id FROM professors WHERE professors.profName ='Affranchino, Gustavo Osvaldo';
INSERT INTO classProfessors  SELECT '12.83','S', professors.id FROM professors WHERE professors.profName ='Fernandez Velazco, Santiago';
INSERT INTO classProfessors  SELECT '12.83','S', professors.id FROM professors WHERE professors.profName ='Ordoñez, Hernán Darío';
INSERT INTO classProfessors  SELECT '12.83','S', professors.id FROM professors WHERE professors.profName ='Affranchino, Gustavo Osvaldo';
INSERT INTO classProfessors  SELECT '72.45','S', professors.id FROM professors WHERE professors.profName ='Debrouvier, Hemilse';
INSERT INTO classProfessors  SELECT '72.45','S', professors.id FROM professors WHERE professors.profName ='Huerta, Jorge Ernesto';
INSERT INTO classProfessors  SELECT '72.45','S', professors.id FROM professors WHERE professors.profName ='Mon, Alicia Laura';
INSERT INTO classProfessors  SELECT '72.45','S', professors.id FROM professors WHERE professors.profName ='Soliani, Valeria Inés';
INSERT INTO classProfessors  SELECT '72.20','S', professors.id FROM professors WHERE professors.profName ='Staudenmann, Luis Alberto';
INSERT INTO classProfessors  SELECT '72.20','S', professors.id FROM professors WHERE professors.profName ='Ortiz, Pablo Gabriel';
INSERT INTO classProfessors  SELECT '72.20','S', professors.id FROM professors WHERE professors.profName ='CORTÉS RODRÍGUEZ, KEVIN IMANOL';
INSERT INTO classProfessors  SELECT '72.20','S', professors.id FROM professors WHERE professors.profName ='Vallés, Santiago Raúl';
INSERT INTO classProfessors  SELECT '94.23','AB', professors.id FROM professors WHERE professors.profName ='Calamante, Romina';
INSERT INTO classProfessors  SELECT '94.23','AB', professors.id FROM professors WHERE professors.profName ='Pedró, Cecilia Angélica';
INSERT INTO classProfessors  SELECT '94.23','C', professors.id FROM professors WHERE professors.profName ='Calamante, Romina';
INSERT INTO classProfessors  SELECT '94.23','C', professors.id FROM professors WHERE professors.profName ='Pedró, Cecilia Angélica';
INSERT INTO classProfessors  SELECT '94.23','KMP', professors.id FROM professors WHERE professors.profName ='Pekers, Paula';

INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'A', 2, '11:00', '14:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'B', 2, '08:00', '11:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'C', 3, '11:00', '14:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'E', 4, '11:00', '14:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'F', 4, '08:00', '11:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'G', 2, '08:00', '11:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'H', 3, '08:00', '11:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'K', 4, '14:00', '17:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('31.08', 'S', 3, '14:00', '17:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.03', 'S', 1, '17:00', '20:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.03', 'S1', 1, '17:00', '20:00', '203R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'A', 1, '11:00', '13:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'A', 3, '10:00', '12:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'A', 5, '11:00', '13:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'B', 1, '09:00', '11:00', '104R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'B', 3, '08:00', '10:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'B', 5, '13:00', '15:00', '101R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'C', 1, '10:00', '12:00', '201R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'C', 3, '08:00', '10:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'C', 5, '08:00', '10:00', '005R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'D', 1, '08:00', '10:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'D', 3, '08:00', '10:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'D', 5, '12:00', '14:00', '201R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'E', 1, '12:00', '14:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'E', 3, '10:00', '12:00', '101R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'E', 5, '10:00', '12:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'F', 1, '14:00', '16:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'F', 3, '10:00', '12:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'F', 5, '08:00', '10:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'G', 1, '14:00', '16:00', '104R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'G', 3, '12:00', '14:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'G', 5, '12:00', '14:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'H', 2, '08:00', '10:00', '101R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'H', 4, '09:00', '11:00', '104R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.26', 'H', 5, '08:00', '10:00', '001R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'A', 2, '09:00', '11:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'A', 2, '11:00', '13:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'A', 4, '11:00', '14:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'A', 5, '14:00', '16:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'B', 2, '11:00', '13:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'B', 2, '13:00', '15:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'B', 4, '11:00', '14:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'B', 5, '12:00', '14:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'C', 1, '11:00', '13:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'C', 4, '14:00', '16:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'C', 5, '11:00', '13:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.58', 'C', 5, '14:00', '17:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.24', 'A', 1, '14:00', '17:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.24', 'C', 3, '14:00', '17:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.24', 'DF', 3, '11:00', '14:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.31', 'S', 1, '08:30', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.31', 'S', 2, '18:00', '20:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.31', 'S', 3, '08:30', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.31', 'S', 5, '14:00', '18:00', '006R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.31', 'S', 5, '14:00', '18:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'A', 1, '12:00', '14:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'A', 1, '12:00', '14:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'A', 2, '13:00', '15:00', '201T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'A', 4, '12:00', '14:00', '003T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'A', 4, '12:00', '14:00', '004T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'B', 1, '12:00', '14:00', '102R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'B', 2, '13:00', '15:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.28', 'B', 4, '15:00', '17:00', '104R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'A', 1, '14:00', '16:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'A', 2, '08:00', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'A', 3, '10:00', '12:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'B', 1, '09:00', '11:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'B', 2, '08:00', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'B', 4, '12:00', '14:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'C', 1, '14:00', '16:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'C', 4, '09:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'C', 5, '10:00', '12:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'D', 2, '16:00', '18:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'D', 3, '14:00', '16:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'D', 4, '10:00', '12:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'E', 2, '13:00', '15:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'E', 3, '08:00', '10:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'E', 5, '08:00', '10:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'F', 1, '14:00', '16:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'F', 3, '15:00', '17:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'F', 5, '16:00', '18:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'G', 2, '12:00', '14:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'G', 3, '15:00', '17:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'G', 4, '14:00', '16:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'H', 1, '11:00', '13:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'H', 2, '13:00', '15:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'H', 3, '12:00', '14:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'K', 1, '15:00', '17:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'K', 2, '14:00', '16:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'K', 5, '12:00', '14:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'S', 1, '16:00', '18:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'S', 2, '16:00', '18:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'S', 3, '16:00', '18:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'S1', 1, '16:00', '18:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'S1', 2, '16:00', '18:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.41', 'S1', 3, '16:00', '18:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.59', 'A', 4, '08:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.59', 'A', 5, '11:00', '14:00', '203R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.59', 'B', 4, '08:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.59', 'B', 5, '11:00', '14:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('12.09', 'S', 5, '08:00', '11:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('12.09', 'S2', 5, '08:00', '11:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.32', 'S', 3, '08:00', '11:00', '201R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.33', 'S', 3, '14:00', '17:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.33', 'S', 4, '18:00', '21:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'A', 1, '11:00', '13:00', '003T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'A', 1, '11:00', '13:00', '004T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'A', 2, '09:00', '11:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'A', 2, '14:00', '16:00', '101R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'B', 1, '18:00', '20:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'B', 2, '09:00', '11:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.35', 'B', 2, '14:00', '16:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'A', 1, '11:00', '13:00', '006R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'A', 2, '08:00', '10:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'A', 3, '14:00', '16:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'B', 1, '08:00', '10:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'B', 2, '14:00', '16:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'B', 3, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'C', 1, '10:00', '12:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'C', 3, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'C', 4, '10:00', '12:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'D', 1, '14:00', '16:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'D', 1, '14:00', '16:00', '102T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'D', 2, '14:00', '16:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'D', 4, '08:00', '10:00', 'LT103 Lab Física II', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'E', 1, '08:00', '10:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'E', 2, '08:00', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'E', 2, '16:00', '18:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'K', 1, '08:00', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'K', 1, '12:00', '14:00', '003R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'K', 2, '12:00', '14:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'MN', 2, '10:00', '12:00', 'LT104 Lab Física III', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'MN', 3, '08:00', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'MN', 4, '14:00', '16:00', '004R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'S', 1, '08:00', '10:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'S', 4, '10:00', '12:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.42', 'S', 4, '12:00', '14:00', 'LT105 Lab Física I', 'SDT', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.08', 'S', 1, '16:00', '19:00', '001R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.08', 'S', 3, '16:00', '19:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.34', 'S', 1, '08:00', '11:00', '201T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.34', 'S', 3, '08:00', '11:00', '201T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.24', 'A', 1, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.24', 'A', 2, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.24', 'A', 4, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.24', 'B', 1, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.24', 'B', 2, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.24', 'B', 4, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'A', 2, '15:00', '17:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'A', 3, '14:00', '16:00', '006R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'A', 4, '16:00', '18:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'B', 3, '10:00', '12:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'B', 4, '14:30', '16:30', '006R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'B', 5, '11:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'C', 2, '10:00', '12:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'C', 3, '14:00', '16:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'C', 4, '18:00', '20:00', '001R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'D', 3, '18:00', '20:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'D', 4, '14:00', '16:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'D', 4, '16:00', '18:00', '102R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'E', 4, '09:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'E', 4, '16:00', '18:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'E', 5, '12:00', '14:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'MNP', 1, '09:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'MNP', 4, '18:00', '20:00', '101T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'MNP', 5, '16:00', '18:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'Q', 3, '12:00', '14:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'Q', 3, '18:00', '20:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'Q', 5, '14:00', '16:00', '001R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'S', 4, '14:00', '16:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'S', 5, '14:00', '16:00', 'Laboratorio', 'Laboratorio', 'Laboratorio');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.43', 'S', 5, '16:00', '18:00', '005R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.11', 'S', 1, '16:00', '19:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.11', 'S', 2, '18:00', '21:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.35', 'S', 1, '13:00', '16:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.35', 'S', 2, '08:00', '11:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.35', 'S', 2, '08:00', '11:00', '203R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.36', 'S', 3, '18:30', '21:30', '101R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.36', 'S', 4, '18:30', '21:30', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.37', 'S', 2, '14:00', '17:00', '203R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.37', 'S', 4, '09:00', '12:00', '006R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.07', 'S', 2, '16:00', '19:00', '006R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.07', 'S', 2, '16:00', '19:00', '007R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.07', 'S', 5, '16:00', '19:00', '102R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.07', 'S', 5, '16:00', '19:00', '103R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.38', 'S', 1, '19:00', '22:00', '901F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.38', 'S', 3, '19:00', '22:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.39', 'S', 3, '15:00', '18:00', '201T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.39', 'S', 4, '15:00', '18:00', '201T', 'SDT', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.07', 'A', 5, '09:00', '12:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.07', 'B', 1, '08:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.07', 'C', 1, '14:00', '17:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.07', 'D', 4, '17:00', '20:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.21', 'A', 4, '11:00', '14:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.21', 'BK', 3, '14:00', '17:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.21', 'C', 4, '08:00', '11:00', '002R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.21', 'D', 1, '14:00', '17:00', '203R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.21', 'E', 5, '14:00', '17:00', '203R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.21', 'H', 4, '14:00', '17:00', '202R', 'Sede Rectorado', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('61.23', 'K', 4, '13:00', '16:00', '501F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('61.23', 'KM', 3, '19:00', '22:00', '603F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('61.23', 'PQ', 3, '19:00', '22:00', '604F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('61.23', 'S', 2, '11:00', '14:00', '801F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('61.32', 'KS', 2, '12:00', '15:00', '902F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('61.32', 'SK', 5, '16:00', '19:00', '604F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.41', 'S', 4, '19:00', '22:00', '501F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.41', 'S', 2, '15:00', '18:00', '401F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.41', 'S', 2, '18:30', '21:30', '401F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.42', 'S', 3, '18:00', '21:00', '400F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.75', 'A', 4, '13:00', '16:00', '701F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('93.75', 'A', 5, '14:00', '17:00', '501F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.25', 'S', 1, '10:00', '13:00', '601F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.25', 'S', 5, '10:00', '13:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.27', 'S', 2, '09:00', '12:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.27', 'S', 4, '08:00', '11:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.43', 'S', 1, '18:00', '21:00', '801F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.44', 'S', 1, '15:00', '18:00', '301F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.44', 'S', 1, '15:00', '18:00', '302F', 'Sede Distrito Financiero', 'presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.44', 'S', 4, '16:00', '19:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('12.83', 'BIO', 1, '18:30', '21:30', '401F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('12.83', 'S', 4, '18:30', '21:30', '503F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.45', 'S', 6, '20:00', '21:00', 'Virtual', 'Virtual', 'Virtual');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.20', 'S', 2, '17:00', '20:00', '703F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('72.20', 'S', 4, '18:00', '21:00', '701F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.23', 'AB', 1, '18:00', '21:00', '703F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.23', 'C', 5, '16:00', '19:00', '701F', 'Sede Distrito Financiero', 'Presencial');
INSERT INTO classLocTime (idSub, idClass, day, startTime, endTime, class, building, mode) VALUES('94.23', 'KMP', 5, '16:00', '19:00', '601F', 'Sede Distrito Financiero', 'Presencial');



