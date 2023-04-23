CREATE TABLE IF NOT EXISTS profileImage
(
    id INTEGER PRIMARY KEY,
    image BYTEA NOT NULL,
    FOREIGN KEY (id) REFERENCES users ON DELETE CASCADE
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
INSERT INTO professorsSubjects (idProf, idSub) SELECT professors.id, 93.35 FROM professors WHERE professors.profName = 'Fernandez Sau, Mercedes';

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