package Inspector;
import java.util.Vector;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import Core.BDLogic;

/**
 * Especifica a BDLogic con el objetivo de proveer facilidades
 * especificas para el inspector de la BD.
 * @see BDLogic
 */
public class InspectorLogic extends BDLogic{

//Constructores
	
	/**
	 * Constructor de la clase. Inicializa los atributos.
	 * @param servidor El servidor desde el cual se conectarï¿½ a la BD.
	 * @param bd La base de datos a la que se quiere conectar.
	 */
	public InspectorLogic(String servidor, String bd){
		super(servidor, bd);
	}
	
//Consultas y Comandos

	/**
	 * Verifica que el inspector insp con password psw sea vï¿½lido en la base de datos.
	 * @param insp Legajo del inspector a loguear.
	 * @param psw Password de inspector a loguear.
	 * @return True si la validaciï¿½n es correcta, False en caso contrario.-
	 * @throws SQLException En caso de que la validaciï¿½n sea incorrecta.
	 */
	public boolean verificarLogin( long insp, String psw) throws SQLException{
		
		//Variables locales
		int i;
		String consulta;
		ResultSet resultado = null;
		
		//Consulta SQL
		consulta = "SELECT * FROM Inspectores WHERE legajo ='"+insp+"' and password = md5('"+psw+"') ;";
		
		//Se ejecuta la consulta y se obtiene el resultado.
		Statement st = conexion.createStatement(); 
		if(st.execute(consulta))
			resultado = st.getResultSet();
			
		//Si encontramos un resultado quiere decir que los datos son correctos,
		//de lo contrario el legajo o contraseña estan mal
		return resultado != null && resultado.isBeforeFirst() ;
	}
	
	/**
	 * Consulta la base de datos y obtiene los parquï¿½metros dados de alta; luego le da formato
	 * al resultado de la consulta y finalmente retorna los parquï¿½metros dados de alta en la 
	 * base de datos con el formato establecido con InspectorGUI que es el siguiente:
	 * String = "Id-@-Numero" donde id y nï¿½mero son los atributos de cada parquï¿½metro.-
	 * @return Vector de Strings con el formato establecido.-
	 * @throws SQLException en caso de algï¿½n error en la consulta con la base de datos.-
	 */
	public Vector<String> getParquimetros() throws SQLException{
		
		//Variables locales
		Vector<Vector<Object>> resultado;
		Vector<String> toReturn;

		//Consulta SQL
		String comando = "SELECT id_parq,numero FROM parquimetros;";
		
		//Ejecuta el comando, operaciï¿½n proveï¿½da por BDLogic.-
		resultado = ejecutarComando( comando );


		//Se encarga de tomar todos los resultados del comando, y le da el formato
		//que se estableciï¿½ con InspectorGUI
		toReturn = darFormato( resultado );
		
		return toReturn;
	}
	
	/**
	 * Consulta la base de datos, en particular la tabla asociado_con, y verifica si para un legajo,
	 * dï¿½a, turno, y la ubicaciï¿½n en la que se ubica el parquï¿½metro con id_parq y num_parq, existe una
	 * tupla que indica que el inspector tiene que recorrer y verificar el establecimiento en esa ubicaciï¿½n.-
	 * @param leg Legajo del inspector
	 * @param id_parq Id del parquï¿½metro al que quiere acceder.
	 * @param num_parq Numero del parquï¿½metro al que quiere acceder.
	 * @param turno 'M' o 'T' indicando maï¿½ana o tarde correspondientemente.
	 * @param dia Primeras dos letras indicando el dï¿½a de la semana.
	 * @return True si es posible que acceda a ese parquï¿½metro bajo este contexto, false en caso contrario.
	 * @throws SQLException Si ocurre alguna falla en la base de datos.-
	 */
	public boolean controlarParquimetro ( long leg, long id_parq, long num_parq, char turno, String dia ) throws SQLException{
		//Variables locales.-
		Vector<Vector<Object>> resultado;
		String comando, calle;
		long altura;
		
		//Consultas SQL
		
		//Se consulta la calle y la altura del parquï¿½metro id-parq
		comando = "SELECT calle, altura FROM Parquimetros WHERE id_parq=" + id_parq +" ;";
		resultado = ejecutarComando( comando );
		calle = resultado.elementAt(1).elementAt(0).toString();
		altura = Long.parseLong( resultado.elementAt(1).elementAt(1).toString() );
		
		//Se consulta por una tupla de Asociado_con que coincida con los datos del inspector, dia, turno,
		//calle y altura para saber si puede o no acceder al parquimetro seleccionado
		comando = "SELECT legajo, calle, altura, dia, turno FROM Asociado_con";
		comando+= " WHERE legajo="+leg+" and calle=\""+calle+"\" and altura="+altura;
		comando+= " and dia=\""+dia+"\" and turno=\""+turno+"\" ;";
		resultado = ejecutarComando( comando );
		
		//En caso que el vector de resultados tenga mï¿½s de una tupla ( la tupla 1 la descontamos ya que
		//contiene los encabezados de las columnas ) podemos afirmar que existe al menos 1 tupla que
		//indica que el inspector con legajo dado puede acceder al parquï¿½metro en esa ubicaciï¿½n, por lo
		//que se retorna true; caso contrario false.-
		if ( resultado.size()>1 )
			return true;
		
		return false;
	}
	
	/**
	 * Dado los resultados obtenidos tras la consulta de los parquï¿½metros, retorna un vector
	 * que contiene Strings con el formato: "Id-@-Numero", donde Id y nï¿½mero corresponden con
	 * los atributos de cada uno de los parquï¿½metros dentro de resultados.-
	 * @param resultados Vector que contiene las tuplas correspondientes a cada registro de parquimetros.-
	 * @return Vector de Strings, con el formato definido.-
	 */
	private Vector<String> darFormato( Vector<Vector<Object>> resultados){
		
		//Variables locales.-
		int i;
		String cadFormato;
		Vector<String> toReturn = new Vector<String>();
		
		//Por cada tupla <id_parq,numero> dentro de resultados, obtengo esa tupla, y le doy el formato
		//necesario para luego guardarla en el vector toReturn.-
		for(i=1; i< resultados.size(); i++){
			cadFormato="";
			
			//Obtengo el valor del id_parq y lo guardo en cadFormato.-
			cadFormato = resultados.elementAt(i).elementAt(0).toString();
			//Agrego a cadFormato el separador entre id_parq y numero como se estableciï¿½ en el formato.-
			cadFormato += ("-@-");
			//Obtengo el valor de numero y lo guardo en cadFormato.- 
			cadFormato += (resultados.elementAt(i).elementAt(1).toString());
			
			//Agrego la cadena con el formato establecido en el vector a retornar.-
			toReturn.add( cadFormato );
		}
		
		return toReturn;
	}

	/**
	 * Dado un nï¿½mero de legajo parametrizado, junto con la hora, fecha y el identificador del parquï¿½metro,
	 * se registra el acceso del inspector a ï¿½ste ï¿½ltimo.
	 * @param legajo Nï¿½mero de legajo del inspector.-
	 * @param id_parq Identificador del parquï¿½metro.-
	 * @param data Fecha de acceso al parquï¿½metro.-
	 * @param time Hora de acceso al parquï¿½metro.-
	 * @throws SQLException En caso de producirse algï¿½n error en la base de datos.-
	 */
	public void registrarAcceso(long legajo, long id_parq, Date data, Time time) throws SQLException {
		//Comando SQL
		String comando = "INSERT INTO Accede VALUE ("+legajo+","+id_parq+","+"'"+data.toString()+"'"+","+"'"+time.toString()+"'"+");";
		//System.out.println(comando);
		//Se ordena a la lï¿½gica ejecutar el comando que creamos con anterioridad, donde se registra
		//el acceso del inspector en el parquï¿½metro.-
		ejecutarComando(comando);
	}
	

	public Vector<Vector<Object>> generarMultas(long id_parq, long legajo, Vector<String> patentes, Date data, Time time, String dia, char turno) throws SQLException{
		//Variables locales.-
		String comando, calle;
		int indice;
		long altura, id_asoc_con;
		Vector<Vector<Object>> resultado;
		
		//Comando SQL
		comando = "SELECT calle, altura FROM Parquimetros WHERE (id_parq=" + id_parq + ");";
		
		//Ejecuto el comando para obtener la calle y altura asociada al id_parq
		resultado = ejecutarComando( comando );
		calle = (String) resultado.elementAt(1).elementAt(0);
		altura = Long.parseLong( resultado.elementAt(1).elementAt(1).toString() );
		
		//Comando SQL
		comando  = "SELECT id_asociado_con FROM Asociado_con WHERE ";
		comando += "legajo=" + legajo + " and calle='" + calle +"' and altura=" + altura + " and ";
		comando += "dia='" + dia + "' and turno='" + turno +"';";
		
		//Ejecuto el comando para obtener el ID correspondiente a la tupla asociado_con que relaciona
		//al inspector y la ubicaciï¿½n donde se encuentra el parquï¿½metro.-
		resultado = ejecutarComando ( comando );
		id_asoc_con = Long.parseLong( resultado.elementAt(1).elementAt(0).toString() );
		
		//Comando SQL	
		comando = "SELECT patenteEstacionados FROM estacionados NATURAL JOIN Parquimetros WHERE id_parq=" + id_parq + ";";

		//Se ejecuta el comando para consultar los autos con estacionamiento abierto en el parquï¿½metro parametrizado.-
		resultado = ejecutarComando( comando );
		
		//Se eliminan el vector con el encabezado de la consulta previa.-
		resultado.remove(0);
		
		for(indice=0; indice<patentes.size(); indice++){
			
			if ( !tieneEstacionamientoAbierto ( patentes.get(indice), resultado ))
				labrarMulta( patentes.get(indice), data, time, id_asoc_con);
		}
		
		//Comando SQL	
		comando  = "SELECT numero,fecha,hora,patente,legajo,calle,altura FROM Multa natural join Asociado_con ";
		comando += "WHERE fecha='" + data.toString() +"' and hora='" + time.toString() + "';";

		//Se ejecuta el comando para consultar los autos con estacionamiento abierto en el parquï¿½metro parametrizado.-
		resultado = ejecutarComando( comando );
		
		return resultado;
	}
	
	/**
	 * Dada una patente, para una ubicaciï¿½n e inspector representados mediante el id_asoc_con, se registra una
	 * multa con los datos parametrizados ( dia == data y hora == time ). Para ï¿½ste proceso, se genera un nï¿½mero
	 * de multa.-
	 * @param patente Patente a la que se le labrarï¿½ la multa.-
	 * @param data Dï¿½a en el que se registra la multa.-
	 * @param time Hora en el que se registra la multa.-
	 * @param id_asoc_con Identificador de inspector y ubicaciï¿½n donde se labra la multa.-
	 * @throws SQLException En caso de algï¿½n error en la base de datos.-
	 */
	private void labrarMulta(String patente, Date data, Time time, long id_asoc_con) throws SQLException {
		//Variables locales
		String comando;
			
		//Comando SQL
		comando  = "INSERT INTO Multa VALUE ( 0,'" + data.toString() + "','" + time.toString() + "','"; 
		comando += patente + "'," + id_asoc_con + ");";

		//System.out.println(comando);
		//Ejecuto el comando registrando la multa
		ejecutarComando(comando);	
	}

	/**
	 * Dada una patente, consulta el arreglo de tuplas en post de evaluar si existe una tupla que 
	 * contenga tal patente.
	 * @param patente Valores a testear existencia dentro de alguna tupla.
	 * @param resultado Vector de tuplas, donde cada tupla se asume con el siguiente formato:
	 * TUPLA = ( ptte_estacionado )
	 * @return True si la patente existe dentro de alguna tupla, false en caso contrario.-
	 */
	private boolean tieneEstacionamientoAbierto(String patente,	Vector<Vector<Object>> resultado) {
		//Variables locales;
		int i;
		boolean salir = false;
		
		for(i=0; i< resultado.size() && !salir; i++)
			if ( resultado.elementAt(i).elementAt(0).toString().equals( patente ))
				salir = true;
		
		return salir;
		
	}
}
