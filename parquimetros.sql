#Archivo batch (parquimetros.sql) para la creacion de la 
#Base de datos del proyecto

#Lo que esta despues del "#" es un comentario

# Creo de la Base de Datos
CREATE DATABASE parquimetros;

# selecciono la base de datos sobre la cual voy a hacer modificaciones
USE parquimetros;

#---------------------------------------------------------------------
# Creación Tablas para las entidades

DROP TABLE IF EXISTS Conductores;
CREATE TABLE Conductores (
	dni INT UNSIGNED NOT NULL, 
	nombre VARCHAR(45) NOT NULL, 
	apellido VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45),
	registro INT UNSIGNED NOT NULL,
 
	CONSTRAINT pk_Conductores
	PRIMARY KEY (dni)
 
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Automoviles;
CREATE TABLE Automoviles (
	patente VARCHAR(6) NOT NULL,
	marca VARCHAR(45) NOT NULL,
	modelo VARCHAR(45) NOT NULL,
	color VARCHAR(45) NOT NULL,
	dni INT UNSIGNED NOT NULL,

	CONSTRAINT pk_Automoviles
	PRIMARY KEY (patente),
 
	CONSTRAINT FK_Conductores_dni
	FOREIGN KEY (dni) REFERENCES Conductores (dni) 
	ON DELETE RESTRICT ON UPDATE CASCADE /*esto no sé si está bien.*/

) ENGINE=InnoDB;

DROP TABLE IF EXISTS Tipos_Cospeles;
CREATE TABLE Tipos_Cospeles (
	tipo VARCHAR(45) NOT NULL,
	descuento DECIMAL(3,2) UNSIGNED NOT NULL,
 
	CONSTRAINT pk_tipo
	PRIMARY KEY (tipo),
 
	CHECK (descuento >= 0.00 and descuento <= 1.00)
	
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Cospeles;
CREATE TABLE Cospeles (
	id_cospel INT UNSIGNED NOT NULL AUTO_INCREMENT,
	saldo DECIMAL(5,2) NOT NULL DEFAULT '000.00',
	tipo VARCHAR(45) NOT NULL,
	patente VARCHAR(6) NOT NULL,
 
	CONSTRAINT pk_cospeles
	PRIMARY KEY (id_cospel),
 
	CONSTRAINT FK_Cospeles_tipo
	FOREIGN KEY (tipo) REFERENCES Tipos_Cospeles (tipo)
	ON DELETE RESTRICT ON UPDATE CASCADE, /*NO SÉ SI ESTÁ BIEN*/

	CONSTRAINT FK_Conductores_patente
	FOREIGN KEY (patente) REFERENCES Automoviles(patente) 
	ON DELETE RESTRICT ON UPDATE CASCADE, /*NO SÉ SI ESTÁ BIEN*/
	
	CHECK (saldo >= 000.00)

) ENGINE=InnoDB;

DROP TABLE IF EXISTS Inspectores;
CREATE TABLE Inspectores (
	legajo INT UNSIGNED NOT NULL, 
	dni  INT UNSIGNED NOT NULL,
	nombre VARCHAR (45) NOT NULL,
	apellido VARCHAR(45) NOT NULL, 
	password VARCHAR(45) NOT NULL, 
	
 
	CONSTRAINT pk_Inspectores 
	PRIMARY KEY (legajo)
 
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Ubicaciones;
CREATE TABLE Ubicaciones (
	calle VARCHAR(45) NOT NULL, 
	altura INT UNSIGNED NOT NULL,
	tarifa DECIMAL(5,2) UNSIGNED NOT NULL, 
 
	CONSTRAINT pk_Ubicaciones 
	PRIMARY KEY (calle,altura),
	CHECK (tarifa >= 00.00)
 
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Parquimetros;
CREATE TABLE Parquimetros (
	id_parq INT UNSIGNED NOT NULL, 
	numero INT UNSIGNED NOT NULL,
	calle VARCHAR (45) NOT NULL,
	altura INT UNSIGNED NOT NULL,

	CONSTRAINT FK_Parquimetros_Ubicaciones
	FOREIGN KEY (calle,altura) REFERENCES Ubicaciones(calle,altura)
	ON DELETE RESTRICT ON UPDATE CASCADE, /*NO SÉ SI ESTÁ BIEN*/
 
	CONSTRAINT pk_Parquimetros
	PRIMARY KEY (id_parq)
 
) ENGINE=InnoDB;

#-------------------------------------------------------------------------
# Creación Tablas para las relaciones

DROP TABLE IF EXISTS  Estacionamientos;
CREATE TABLE  Estacionamientos (
	id_cospel INT UNSIGNED NOT NULL, 
	id_parq INT UNSIGNED NOT NULL,
	fecha_ent DATE NOT NULL,
	hora_ent TIME NOT NULL,
	fecha_sal DATE DEFAULT NULL,
	hora_sal TIME DEFAULT NULL,
 
	CONSTRAINT fk_Estacionamientos_cospel
	FOREIGN KEY(id_cospel) REFERENCES Cospeles(id_cospel),
	CONSTRAINT FK_Estacionamientos_Parquimetros
	FOREIGN KEY (id_parq) REFERENCES Parquimetros(id_parq)
	ON DELETE RESTRICT ON UPDATE CASCADE, /*NO SÉ SI ESTÁ BIEN*/
 
	CONSTRAINT pk_Estacionamientos
	PRIMARY KEY (id_parq,fecha_ent,hora_ent)
 
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Accede;
CREATE TABLE Accede (
	legajo INT UNSIGNED  NOT NULL,
	id_parq INT UNSIGNED NOT NULL,
	fecha DATE NOT NULL,
	hora TIME  NOT NULL,  								
	
	CONSTRAINT pk_accede
	PRIMARY KEY (id_parq, fecha, hora),
	
	CONSTRAINT fk_accede_legajo
	FOREIGN KEY (legajo) REFERENCES Inspectores(legajo),
	CONSTRAINT fk_accede_parq
	FOREIGN KEY (id_parq) REFERENCES Parquimetros(id_parq)
	
) ENGINE=InnoDB;	
	
DROP TABLE IF EXISTS Asociado_con;
CREATE TABLE Asociado_con (
	id_asociado_con INT UNSIGNED  NOT NULL AUTO_INCREMENT,
	legajo INT UNSIGNED  NOT NULL,
	calle VARCHAR(40) NOT NULL,
	altura INT UNSIGNED NOT NULL,
	dia VARCHAR(2)  NOT NULL,
	turno VARCHAR(1) NOT NULL,
	
	CONSTRAINT pk_asociado_con
	PRIMARY KEY (id_asociado_con),
	
	CONSTRAINT fk_asociado_con_legajo
	FOREIGN KEY (legajo) REFERENCES Inspectores(legajo),
	
	CONSTRAINT fk_asociado_con_calle_altura
	FOREIGN KEY(calle,altura) REFERENCES Ubicaciones(calle,altura)
	
) ENGINE=InnoDB;	
	
DROP TABLE IF EXISTS Multa;
CREATE TABLE Multa (
	numero INT UNSIGNED  NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	hora TIME NOT NULL ,  								
	patente VARCHAR(6) NOT NULL,
	id_asociado_con INT UNSIGNED  NOT NULL ,
	
	CONSTRAINT pk_multa_numero
	PRIMARY KEY (numero),
	
	CONSTRAINT fk_multa_patente
	FOREIGN KEY (patente) REFERENCES Automoviles(patente),
	
	CONSTRAINT fk_multa_id_asociado_con
	FOREIGN KEY(id_asociado_con) REFERENCES Asociado_con(id_asociado_con)
	
) ENGINE=InnoDB;



#Nueva tabla Ventas

DROP TABLE IF EXISTS Ventas;
CREATE TABLE Ventas (
	id_cospel INT UNSIGNED NOT NULL,
	saldo DECIMAL (6,2) NOT NULL, 
	tipo_cospel VARCHAR (20) NOT NULL,								
	fecha DATE NOT NULL,
	hora TIME NOT NULL ,  	
	
	CONSTRAINT pk_id_cospel
	PRIMARY KEY (id_cospel),
	
	CONSTRAINT fk_tipo_cospel
	FOREIGN KEY (tipo_cospel) REFERENCES Tipos_Cospeles(tipo)
	) ENGINE=InnoDB;	

#-------------------------------------------------------------------------
# Creacion de vistas 
CREATE VIEW estacionados(calle, altura, patenteEstacionados) AS
			SELECT calle AS Calle ,altura AS Altura, patente AS Patente
			FROM Cospeles NATURAL JOIN Parquimetros  NATURAL JOIN  Estacionamientos
			WHERE  ISNULL(fecha_sal) AND  ISNULL(hora_sal);

#-------------------------------------------------------------------------


# STORE PROCEDURE PARA REGISTRAR ACCESO Y SALIDA DE UN ESTACIONAMIENTO
			
delimiter !
CREATE PROCEDURE conectar(IN idcospel int UNSIGNED, IN idparq int UNSIGNED)
BEGIN
	DECLARE saldo_proc DECIMAL(6,2);
	DECLARE tarifa_proc DECIMAL(5,2) UNSIGNED;
	DECLARE tiempo_proc DECIMAL(7,2);
	DECLARE descuento_proc decimal(3,2) UNSIGNED;
	DECLARE diferencia TIME;
	DECLARE diferencia_segundos int unsigned;
	DECLARE fecha_entrada DATE ;
	DECLARE hora_entrada TIME;
	DECLARE fecha DATE ;
	DECLARE hora TIME;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	  BEGIN 
	  /*
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO,  
		                             codigo_SQL= RETURNED_SQLSTATE, 
									 mensaje_error= MESSAGE_TEXT;
	    */
		SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado; 
		        /*codigo_MySQL, codigo_SQL,  mensaje_error;*/		
        ROLLBACK;
	  END;		
	
	START TRANSACTION;
	
	/* Se verifica si el cospel y parquimetro ingresados existen en la base de datos.- */
	IF EXISTS (SELECT id_cospel FROM Cospeles WHERE id_cospel = idcospel) AND (SELECT id_parq FROM Parquimetros WHERE id_parq = idparq)
		THEN
		/* Si el cospel figura con estacionamiento abierto, la operación es de cierre.- */
		IF EXISTS ( /*SELECT * 
				FROM estacionados JOIN Cospeles ON estacionados.patenteEstacionados=Cospeles.patente NATURAL JOIN Parquimetros
				WHERE id_cospel=idcospel and id_parq = idparq */
				SELECT * FROM Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and id_cospel = idcospel
				   ) THEN
				   
				   /* Si el cospel esta conectado al parquimetro ingresado.- */
				   IF EXISTS (SELECT * FROM Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and id_cospel = idcospel and id_parq = idparq)
				   THEN
					   /* Obtengo la fecha y hora de entrada.- */
					   SELECT fecha_ent INTO fecha_entrada FROM  Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and 
						id_cospel = idcospel and id_parq = idparq;
					   SELECT hora_ent INTO hora_entrada FROM  Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and 
						id_cospel = idcospel and id_parq = idparq;
					   
					   /* Obtengo el saldo y descuento del cospel y la tarifa de la ubicación.- */
					   SELECT saldo INTO saldo_proc FROM Cospeles WHERE id_cospel = idcospel FOR UPDATE;
					   SELECT tarifa INTO tarifa_proc FROM Parquimetros NATURAL JOIN Ubicaciones WHERE id_parq = idparq;
					   SELECT descuento INTO descuento_proc FROM Cospeles NATURAL JOIN Tipos_Cospeles WHERE id_cospel = idcospel;
					   
					   /* Obtengo la fecha y hora actual.- */
					   SELECT CURDATE() INTO fecha;
					   SELECT CURTIME() INTO hora;
					   
					   SELECT TIMEDIFF( CONCAT( fecha,' ',hora), CONCAT(fecha_entrada,' ',hora_entrada) ) INTO diferencia;
					   SELECT TIME_TO_SEC ( diferencia ) INTO diferencia_segundos;
					   
					   /* Calculo el nuevo saldo teniendo en cuenta la duración del automóvil estacionado.- */
					   SET saldo_proc = saldo_proc - diferencia_segundos * (( tarifa_proc * ( 1 - descuento_proc)) / 60 );
					   
					   /* Se actualiza el saldo del cospel.- */
					   UPDATE Cospeles
					   SET saldo = saldo_proc
					   WHERE id_cospel = idcospel;
					   
					   /* Actualiza la hora y fecha de salida del registro de estacionamiento.- */
					   UPDATE  Estacionamientos
					   SET hora_sal = hora, fecha_sal = fecha
					   WHERE id_cospel = idcospel and isnull(fecha_sal) and isnull(hora_sal) and id_parq = idparq;
					   
					   /* Retorna los resultados de la operación.- */
					   SELECT 'cierre' as Operacion, (diferencia_segundos / 60) as Tiempo, saldo_proc as Saldo;
				   ELSE
						/* Obtengo el parquimetro al que esta conectado el cospel.- */
						SELECT id_parq INTO idparq FROM  Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and 
						id_cospel = idcospel;
						/* Obtengo la fecha y hora de entrada.- */
					   SELECT fecha_ent INTO fecha_entrada FROM  Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and 
						id_cospel = idcospel and id_parq = idparq;
					   SELECT hora_ent INTO hora_entrada FROM  Estacionamientos WHERE isnull(fecha_sal) and isnull(hora_sal) and 
						id_cospel = idcospel and id_parq = idparq;
					   
					   /* Obtengo el saldo y descuento del cospel y la tarifa de la ubicación.- */
					   SELECT saldo INTO saldo_proc FROM Cospeles WHERE id_cospel = idcospel FOR UPDATE;
					   SELECT tarifa INTO tarifa_proc FROM Parquimetros NATURAL JOIN Ubicaciones WHERE id_parq = idparq;
					   SELECT descuento INTO descuento_proc FROM Cospeles NATURAL JOIN Tipos_Cospeles WHERE id_cospel = idcospel;
					   
					   /* Obtengo la fecha y hora actual.- */
					   SELECT CURDATE() INTO fecha;
					   SELECT CURTIME() INTO hora;
					   
					   SELECT TIMEDIFF( CONCAT( fecha,' ',hora), CONCAT(fecha_entrada,' ',hora_entrada) ) INTO diferencia;
					   SELECT TIME_TO_SEC ( diferencia ) INTO diferencia_segundos;
					   
					   /* Calculo el nuevo saldo teniendo en cuenta la duración del automóvil estacionado.- */
					   SET saldo_proc = saldo_proc - diferencia_segundos * (( tarifa_proc * ( 1 - descuento_proc)) / 60 );
					   
					   /* Se actualiza el saldo del cospel.- */
					   UPDATE Cospeles
					   SET saldo = saldo_proc
					   WHERE id_cospel = idcospel;
					   
					   /* Actualiza la hora y fecha de salida del registro de estacionamiento.- */
					   UPDATE  Estacionamientos
					   SET hora_sal = hora, fecha_sal = fecha
					   WHERE id_cospel = idcospel and isnull(fecha_sal) and isnull(hora_sal) and id_parq = idparq;
					   
					   /* Retorna los resultados de la operación.- */
					   SELECT 'cierre' as Operacion, (diferencia_segundos / 60) as Tiempo, saldo_proc as Saldo;
				   END IF;
			/* Si el cospel no figura con estacionamiento cerrado, la operación es de apertura.- */
			ELSE
				   /* Obtengo el saldo y descuento del cospel y la tarifa de la ubicación.- */
				   SELECT saldo INTO saldo_proc FROM Cospeles WHERE id_cospel = idcospel;
				   SELECT tarifa INTO tarifa_proc FROM Parquimetros NATURAL JOIN Ubicaciones WHERE id_parq = idparq;
				   SELECT descuento INTO descuento_proc FROM Cospeles NATURAL JOIN Tipos_Cospeles WHERE id_cospel = idcospel;
				   
				   /* Obtengo la fecha y hora actual.- */
				   SELECT CURDATE() INTO fecha;
				   SELECT CURTIME() INTO hora;
				   
				   /* Si el saldo es cero o menor, no se puede ingresar al estacionamiento.- */
				   IF saldo_proc <=0 THEN 
						SELECT 'apertura' as Operacion, 'Saldo insuficiente para realizar apertura' as Resultado;
				   ELSE
						/* Se registra el acceso al  Estacionamientos.- */
						INSERT INTO  Estacionamientos VALUES (idcospel, idparq, fecha, hora, NULL, NULL);
						
						/* Se calcula el tiempo máximo que según tarifa y saldo podría permanecer el auto en el estacionamiento.- */
						SET tiempo_proc = saldo_proc / ( tarifa_proc * ( 1 - descuento_proc ) );
						
						/* Retorna los resultados de la operación.- */
						SELECT 'apertura' as Operacion , 'exito' as Resultado, tiempo_proc as Tiempo;
				   END IF;
		END IF;
		ELSE  
            SELECT 'ERROR: Cospel o Parquimetro inexistentes' 
		        AS resultado;  
	   END IF; 
	COMMIT;
			
END; ! 
delimiter ; 

# TRIGGER QUE REGISTRA LA VENTA DE COSPELES.-

delimiter !
CREATE TRIGGER Registro_Ventas
AFTER INSERT ON Cospeles
FOR EACH ROW
BEGIN
	/* Se inserta en la tabla ventas, el cospel dado de alta, la fecha y hora, el saldo y tipo de cospel.- */
	INSERT INTO Ventas VALUES (NEW.id_cospel, NEW.saldo, NEW.tipo, CURDATE(), CURTIME()) ; 
END; !
delimiter ;





#---------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios

# creo el usuario y le otorgo privilegios 
# utilizando solo la sentencia GRANT

GRANT ALL PRIVILEGES 
	ON parquimetros.* 
	TO 'admin'@'localhost' 
    IDENTIFIED BY 'admin' WITH GRANT OPTION;

# El usuario 'admin' tiene acceso total a todas las tablas de 
# la base de datos, puede conectarse solo desde la computadora 
# donde se encuentra el servidor de MySQL (localhost), el password de su 
# cuenta es 'admin' y puede otorgar privilegios 
GRANT ALL PRIVILEGES 
	ON parquimetros.* 
	TO 'admin'@'localhost'
    IDENTIFIED BY 'admin' WITH GRANT OPTION;	

CREATE USER 'venta'@'%' IDENTIFIED BY 'venta'; 
GRANT INSERT
	ON parquimetros.Cospeles
	TO 'venta'@'%';									

GRANT SELECT 
	ON parquimetros.estacionados
	TO 'inspector'@'%'
    IDENTIFIED BY 'inspector';

GRANT SELECT 
	ON parquimetros.Inspectores
	TO 'inspector'@'%';
	
GRANT INSERT 
	ON parquimetros.Multa
	TO 'inspector'@'%';

GRANT SELECT 
	ON parquimetros.Multa
	TO 'inspector'@'%';
	
GRANT SELECT 
	ON parquimetros.Parquimetros
	TO 'inspector'@'%';
	
GRANT INSERT 
	ON parquimetros.Accede
	TO 'inspector'@'%';

GRANT SELECT 
	ON parquimetros.Accede
	TO 'inspector'@'%';

GRANT INSERT 
	ON parquimetros.Asociado_con
	TO 'inspector'@'%'; 

GRANT SELECT 
	ON parquimetros.Asociado_con
	TO 'inspector'@'%';  

GRANT SELECT 
	ON parquimetros.Cospeles
	TO 'parquimetro'@'%'
    IDENTIFIED BY 'parq';
 
GRANT SELECT 
	ON parquimetros.Parquimetros
	TO 'parquimetro'@'%';

GRANT EXECUTE ON PROCEDURE parquimetros.conectar to 'parquimetro'@'%';
