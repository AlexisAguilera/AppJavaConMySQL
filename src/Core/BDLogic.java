package Core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import quick.dbtable.DBTable;

/**
 * Clase encargada de interactuar con la base de datos del sistema.
 * Provee una interfaz para conectarse a cualquier servidor y base de datos,
 * los cuales deben ser especificados en las clases descendientes.
 * Ademas de posibilita la ejecución de consultas y comandos.
 */
public abstract class BDLogic {

//Atributos

	protected String servidor;
	protected String bd;
	protected String url;
	protected Connection conexion;
	
//Constructores
	
	/**
	 * Constructor de la clase. Configura los datos para la conexión al servidor.
	 * @param servidor El servidor desde el cual se desea ingresar a la base de datos.
	 * @param bd La base de datos a la que se desea conectar.
	 */
	protected BDLogic(String servidor, String bd){
		
		this.servidor=servidor;
		this.bd=bd;
		
		// Se carga y registra el driver JDBC de MySQL
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
		url= "jdbc:mysql://"+servidor+"/"+bd;
	}

//Consultas y Comandos
	
	/**
	 * Crea una conexión con el la base de datos.
	 * @param usuario El usuario con el que se desea ingresar.
	 * @param psw La contraseña del usuario.
	 * @throws SQLException En caso de que no se pueda establecer la conexión.
	 */
	public void conectarse(String usuario, String psw) throws SQLException{
		
		//conexion = java.sql.DriverManager.getConnection(url, usuario, psw);
		//se intenta establecer la conección
        this.conexion = DriverManager.getConnection(url, usuario, psw);
	}
	
	public void conectarDBTable(DBTable table, String usuario, String psw) throws SQLException, ClassNotFoundException{
		//establece una conexión con la  B.D. "batallas"  usando directamante una tabla DBTable    
		table.connectDatabase("com.mysql.jdbc.Driver", url, usuario, psw);
	}
	
	/**
	 * Desconecta la aplicacion de la BD.
	 * @throws SQLException En caso de que no se pueda desconectar.
	 */
	public void desconectarse() throws SQLException{
		conexion.close();
	}
	
	/**
	 * Ejecuta un comando SQL en la base de datos a la que esta conectada la apliación.
	 * @param comando El comando que se desea ejecutar.
	 * @return En caso de ser una consulta se devuelve una tabla de vectores con todos los
	 * datos de la consulta (En la primer columna estan los nombres de las columnas). 
	 * En caso contrario se rotorna NULL.
	 * @throws SQLException En caso de que no se pueda ejecutar el comando.
	 */
	
	public Vector<Vector<Object>> ejecutarComando(String comando) throws SQLException{
		Vector<Vector<Object>> toReturn = null;
		Statement st = conexion.createStatement(); 
		
		if(st.execute(comando))
			toReturn = conversionVectores(st.getResultSet());
			
		return toReturn;
	}
	
	/**
	 * Obtiene todos los datos del ResultSet parametrizado y los convierte en un Vector de Vectores
	 * @param result El ResulSet obtenido de la consulta.
	 * @return Una tabla con todos los resultados
	 * @throws SQLException En caso de que ocurra algun error con los resultados.
	 */
	
	private Vector<Vector<Object>> conversionVectores(ResultSet result) throws SQLException {
		Vector<Vector<Object>> toReturn = new Vector<Vector<Object>>();
		
		//Agrega los nombres de las columnas.
		Vector<Object> nombres = new Vector<Object>();
		for(int i=1; i<=result.getMetaData().getColumnCount(); i++){
			nombres.add(result.getMetaData().getColumnName(i));
		}
		toReturn.add(nombres);
		
		//Agrega cada uno de los datos.
		while(result.next()){
			Vector<Object> row= new Vector<Object>();
			for(int i=1; i<=result.getMetaData().getColumnCount(); i++){
				row.add(result.getObject(i));
			}
			toReturn.add(row);
		}
		
		return toReturn;		
	}

	/**
	 * Retorna el nombre de la base de datos a la que esta conectada la aplicación.
	 * @return El nombre de la base de datos.
	 */
	public String getBD(){
		return bd;
	}
	
	/**
	 * Retorna el nombre del servidor desde donde se ejecuta la conexión.
	 * @return El nombre del servidor
	 */
	public String getServidor(){
		return servidor;
	}

}
