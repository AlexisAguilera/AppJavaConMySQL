# Carga de datos de Prueba
# ALTA DE CONDUCTORES
# FORMATO DE INSERCION: ( DNI, NOMBRE, APELLIDO, DIREECION, TELEFONO, REGISTRO ) 
INSERT INTO Conductores VALUE (35699357, 'Alexis', 'Aguilera', 'Mitre 1535', '423998', '100723');
INSERT INTO Conductores VALUE (36921240, 'Nehuen', 'Antiman', 'Zapiola 653', '490854', '102440');
INSERT INTO Conductores VALUE (36123401, 'Ignacio', 'del Barrio', 'Colón 712', '430854', '97396');
INSERT INTO Conductores VALUE (19234512, 'Roberto', 'Arruabarrena', 'Zelarrayan 503', '423412', '99102');
INSERT INTO Conductores VALUE (12495124, 'Martín', 'Palermo', 'Aguado 902', '430207', '98721');
INSERT INTO Conductores VALUE (12345912, 'Diego Armando', 'Maradona', 'Brandsen', '410101', '1010');
INSERT INTO Conductores VALUE (12921451, 'Juan Román', 'Riquelme', 'J. Armando', '401010', '10101');
INSERT INTO Conductores VALUE (15923512, 'José', 'Perez', 'Brasil 71', '456332', '99321');
INSERT INTO Conductores VALUE (21249123, 'Julieta', 'Orellana', 'España 51', '421234', '98234');


# ALTA DE AUTOMÓVILES 
# FORMATO DE INSERCION: ( PATENTE, MARCA, MODELO, COLOR, DNI )
INSERT INTO Automoviles VALUE ('AAB101', 'Chevrolet', 'Corsa', 'Blanco', 35699357);
INSERT INTO Automoviles VALUE ('AAC102', 'Toyota', 'Hilux', 'Gris', 36921240);
INSERT INTO Automoviles VALUE ('AAD103', 'Ford', 'Escord', 'Verde', 36123401);
INSERT INTO Automoviles VALUE ('AAE104', 'Chevrolet', 'Astra', 'Negro', 19234512);
INSERT INTO Automoviles VALUE ('AAF105', 'Fiat', '128', 'Azul', 12495124);
INSERT INTO Automoviles VALUE ('AAG106', 'Ford', 'Falcon', 'Verde', 12345912);
INSERT INTO Automoviles VALUE ('AAJ107', 'Fiat', 'Uno', 'Negro', 12921451);
INSERT INTO Automoviles VALUE ('AAK108', 'Peugeot', '206', 'Shampagne', 15923512);
INSERT INTO Automoviles VALUE ('AAQ109', 'Honda', 'Civic', 'Gris', 21249123);


# ALTA DE TIPOS_COSPELES
# FORMATO DE INSERCION: ( TIPO, DESCUENTO )
INSERT INTO Tipos_Cospeles VALUE ('Normal', 0.00);
INSERT INTO Tipos_Cospeles VALUE ('A1', 0.25);
INSERT INTO Tipos_Cospeles VALUE ('A2', 0.35);

# ALTA DE COSPELES
# FORMATO DE INSERCION: ( ID_COSPEL, SALDO, TIPO, PATENTE )
INSERT INTO Cospeles VALUE (1, 102.40, 'Normal', 'AAB101');
INSERT INTO Cospeles VALUE (2, 12.90, 'A1', 'AAC102');
INSERT INTO Cospeles VALUE (3, 48.03, 'A1', 'AAD103');
INSERT INTO Cospeles VALUE (4, 71.21, 'Normal', 'AAE104');
INSERT INTO Cospeles VALUE (5, 21.12, 'A2', 'AAF105');
INSERT INTO Cospeles VALUE (6, 92.92, 'A1', 'AAG106');
INSERT INTO Cospeles VALUE (7, 33.12, 'NORMAL', 'AAJ107');
INSERT INTO Cospeles VALUE (8, 82.32, 'A2', 'AAK108');
INSERT INTO Cospeles VALUE (9, 69.89, 'A1', 'AAQ109');


# ALTA INSPECTORES
# FORMATO DE INSERCION: ( LEGAJO, DNI, NOMBRE, APELLIDO, PASSWORD )
INSERT INTO Inspectores VALUE (1,10000000, 'Walter', 'Grandineti', md5('Walter'));
INSERT INTO Inspectores VALUE (2,30000000, 'Gabriela', 'Lopez Franz', md5('Gabriela'));
INSERT INTO Inspectores VALUE (3,20000000, 'Marian', 'Fernandez Benssati', md5('Marian'));
INSERT INTO Inspectores VALUE (4,40000000, 'Gabriela', 'Diaz', md5('Gabriela'));


# ALTA UBICACIONES
# FORMATO DE INSERCION: ( CALLE, ALTURA, TARIFA )
INSERT INTO Ubicaciones VALUE ('San Martin','100',4.20);
INSERT INTO Ubicaciones VALUE ('San Martin','200',2.20);
INSERT INTO Ubicaciones VALUE ('San Martin','300',1.20);
INSERT INTO Ubicaciones VALUE ('San Martin','400',4.20);
INSERT INTO Ubicaciones VALUE ('San Martin','500',2.80);
INSERT INTO Ubicaciones VALUE ('Colon','100',3.20);
INSERT INTO Ubicaciones VALUE ('Colon','200',3.20);
INSERT INTO Ubicaciones VALUE ('Colon','300',1.20);
INSERT INTO Ubicaciones VALUE ('Colon','400',1.20);
INSERT INTO Ubicaciones VALUE ('Colon','500',0.80);
INSERT INTO Ubicaciones VALUE ('Alem','100',1.20);
INSERT INTO Ubicaciones VALUE ('Alem','200',5.20);
INSERT INTO Ubicaciones VALUE ('Alem','300',3.20);
INSERT INTO Ubicaciones VALUE ('Alem','500',2.20);
INSERT INTO Ubicaciones VALUE ('Estomba','120',3.20);
INSERT INTO Ubicaciones VALUE ('Estomba','230',1.20);
INSERT INTO Ubicaciones VALUE ('Estomba','450',0.80);
INSERT INTO Ubicaciones VALUE ('Estomba','1100',3.20);


# ALTA PARQUIMETROS
# FORMATO DE INSERCION: ( ID_PARQ, NUMERO, CALLE, ALTURA )
INSERT INTO Parquimetros VALUE (1,1,'San Martin','100');
INSERT INTO Parquimetros VALUE (2,1,'San Martin','200');
INSERT INTO Parquimetros VALUE (3,1,'San Martin','300');
INSERT INTO Parquimetros VALUE (4,1,'San Martin','400');
INSERT INTO Parquimetros VALUE (5,1,'San Martin','500');
INSERT INTO Parquimetros VALUE (6,2,'San Martin','500');
INSERT INTO Parquimetros VALUE (7,2,'San Martin','400');
INSERT INTO Parquimetros VALUE (8,2,'San Martin','300');
INSERT INTO Parquimetros VALUE (9,2,'San Martin','200');
INSERT INTO Parquimetros VALUE (10,2,'San Martin','100');
INSERT INTO Parquimetros VALUE (11,1,'Colon','100');
INSERT INTO Parquimetros VALUE (12,1,'Colon','200');
INSERT INTO Parquimetros VALUE (13,1,'Colon','300');
INSERT INTO Parquimetros VALUE (14,1,'Colon','400');
INSERT INTO Parquimetros VALUE (15,1,'Colon','500');
INSERT INTO Parquimetros VALUE (16,1,'Estomba','120');
INSERT INTO Parquimetros VALUE (17,1,'Estomba','450');
INSERT INTO Parquimetros VALUE (18,1,'Estomba','1100');
INSERT INTO Parquimetros VALUE (19,1,'Estomba','230');



# ALTA ESTACIONAMIENTOS
# FORMATO DE INSERCION: ( ID_COSPEL, ID_PARQUIMETRO, FECHA_ENTRADA, HORA_ENTRAD, FECHA_SALIDA, HORA_SALIDA )

INSERT INTO Estacionamientos (id_cospel, id_parq, fecha_ent, hora_ent) VALUE (1,1,'2014.11.01', '12:12:50');
INSERT INTO Estacionamientos (id_cospel, id_parq, fecha_ent, hora_ent) VALUE (2,2,'2014.11.11', '21:20:10');
INSERT INTO Estacionamientos (id_cospel, id_parq, fecha_ent, hora_ent) VALUE (3,3,'2014.12.11', '15:20:00');
INSERT INTO Estacionamientos (id_cospel, id_parq, fecha_ent, hora_ent) VALUE (4,4,'2014.11.11', '02:57:30');
INSERT INTO Estacionamientos VALUE (5,5,'2014.04.12', '12:02:40','2012.01.15', '14:05:25' );
INSERT INTO Estacionamientos VALUE (6,6,'2014.05.13', '12:21:52','2012.01.15', '19:59:59' );
INSERT INTO Estacionamientos VALUE (7,7,'2015.05.14', '20:20:14','2012.01.19', '19:59:59' );
INSERT INTO Estacionamientos VALUE (8,8,'2015.05.05', '14:33:10','2012.02.01', '18:30:54' );
INSERT INTO Estacionamientos VALUE (9,9,'2014.06.06', '01:55:24','2012.05.02', '12:05:09' );
INSERT INTO Estacionamientos VALUE (4,10,'2014.06.17', '12:32:40', '2012.05.18', '18:00:00');
INSERT INTO Estacionamientos VALUE (2,11,'2014.06.21', '09:59:59','2012.05.23', '09:25:12' );


# ALTA ACCEDE
# FORMATO DE INSERCION: ( LEGAJO, ID_PARQUIMETRO, FECHA, HORA )
INSERT INTO Accede VALUE (1,1,'2015.02.15', '14:07:45' );
INSERT INTO Accede VALUE (2,2,'2015.02.15', '18:51:16' );
INSERT INTO Accede VALUE (3,3,'2015.01.19', '15:30:00' );
INSERT INTO Accede VALUE (4,8,'2015.02.01', '18:27:00' );
INSERT INTO Accede VALUE (1,9,'2015.05.02', '07:05:09' );
INSERT INTO Accede VALUE (2,1,'2015.05.18', '08:16:35' );
INSERT INTO Accede VALUE (3,8,'2015.05.23', '18:25:12' );
INSERT INTO Accede VALUE (4,9,'2015.10.01', '07:30:14' );
INSERT INTO Accede VALUE (1,11,'2015.10.01', '15:02:46' );
INSERT INTO Accede VALUE (2,2,'2015.10.01', '16:01:01' );
INSERT INTO Accede VALUE (3,3,'2015.10.01', '13:40:20' );


# ALTA ASOCIADO_CON
# FORMATO DE INSERCION: ( ID_ASOCIADO_CON, LEGAJO, CALLE, ALTURA, DIA, TURNO )
INSERT INTO Asociado_con VALUE (1,1,'Estomba','120', 'vi','t');
INSERT INTO Asociado_con VALUE (2,2,'Estomba','450','vi','t');
INSERT INTO Asociado_con VALUE (3,3,'Estomba','120','ju','t');
INSERT INTO Asociado_con VALUE (4,4,'Estomba','450','mi','t');
INSERT INTO Asociado_con VALUE (5,1,'Estomba','120','lu','t');
INSERT INTO Asociado_con VALUE (6,2,'Estomba','1100', 'sa','t');
INSERT INTO Asociado_con VALUE (7,3,'Estomba','1100','lu','m');
INSERT INTO Asociado_con VALUE (8,4,'Estomba','450','vi','m');
INSERT INTO Asociado_con VALUE (9,1,'Estomba','450','ju','t');
INSERT INTO Asociado_con VALUE (10,2,'Estomba','120','sa','t');
INSERT INTO Asociado_con VALUE (11,3,'Colon','100','vi','t');


# ALTA MULTA
# FORMATO DE INSERCION: ( NUMERO, FECHA, HORA, PATENTE, ID_ASOCIADO_CON )
INSERT INTO Multa VALUE (1, '2015.11.21', '15:07:45', 'AAB101',1);
INSERT INTO Multa VALUE (2, '2015.11.21', '16:51:16', 'AAC102',2);
INSERT INTO Multa VALUE (3, '2025.11.21', '16:30:00', 'AAC102',3);
INSERT INTO Multa VALUE (4, '2032.11.21', '18:27:00', 'AAB101',4);
INSERT INTO Multa VALUE (5, '2014.11.21', '17:05:09', 'AAF105',5);