package Administrador;

import java.util.LinkedList;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;

import Core.BDLogic;

/**
 * Especifica a BDLogic con el objetivo de proveer facilidades
 * especificas para el administrador de la BD.
 * @see BDLogic
 */
public class AdminLogic extends BDLogic{

//Constructores
	
	/**
	 * Constructor de la clase. Inicializa los atributos.
	 * @param servidor El servidor desde el cual se conectar� a la BD.
	 * @param bd La base de datos a la que se quiere conectar.
	 */
	public AdminLogic(String servidor, String bd){
		super(servidor, bd);
	}
	
//Consultas y Comandos
	
	/**
	 * Devuelve una lista conteniendo una lista por cada tabla de la BD.
	 * Las listas de tabla tienen el siguiente formato:
	 * <nombre_tabla>-<atributo1>-<atributo2>-...-<atributoN>
	 * @return La lista de todas las tablas de la BD con todos sus atributos
	 */
	public LinkedList<LinkedList<String>> obtenerTablas(){
		LinkedList<LinkedList<String>> toReturn = new LinkedList<LinkedList<String>>();
		
		//Agrega una lista por cada tabla conteniendo el nombre
		//de la tabla seguido de sus atributos.
		for( String tabla : consultarTablas()){
			LinkedList<String> lista = new LinkedList<String>();
			lista.addLast(tabla);
			for(String atributo : consultarAtributos(tabla)){
				lista.addLast(atributo);
			}
			toReturn.addLast(lista);
		}
		
		
		return toReturn;
	}

	/**
	 * Retorna una lista con el nombre de todos los atributos de una tabla de la BD.
	 * @param tabla El nombre de la tabla de la BD.
	 * @return Todos los atributos de una tabla.
	 */
	public LinkedList<String> consultarAtributos(String tabla) {
		LinkedList<String> toReturn = new LinkedList<String>();
		
		//Consulta SQL.
		String sql="DESCRIBE "+tabla+";";
		
		
		try {
			ResultSet r= conexion.createStatement().executeQuery(sql);
			
			while(r.next()){
				toReturn.addLast(r.getString(1));
			}
			
		} catch (SQLException e) {e.printStackTrace();}
		
		return toReturn;
	}

	/**
	 * Retorna una lista con el nombre de todas las tablas de la BD.
	 * @return Todas las tablas de la BD.
	 */
	public LinkedList<String> consultarTablas() {
		LinkedList<String> toReturn = new LinkedList<String>();
		
		//Consulta SQL.
		String sql="SHOW TABLES;";
		
		
		try {
			ResultSet r= conexion.createStatement().executeQuery(sql);
			
			while(r.next()){
				toReturn.addLast(r.getString(1));
			}
			
		} catch (SQLException e) {e.printStackTrace();}
		
		return toReturn;
	}

	/*
	public DefaultListModel refrescarAtributosTabla()
	   {
		   try
		      {
			   //Limpiar lista de atributos
			   atributtesModel.clear();
			   
			   // se crea una sentencia o comando jdbc para realizar la consulta 
		  	 	// a partir de la coneccion establecida (conexionBD)
		       Statement stmt = this.conexionBD.createStatement();
			   if	(tablesList.getSelectedIndex() != -1) {
		
			         ResultSet rs = stmt.executeQuery("describe "+tablesModel.get(tablesList.getSelectedIndex()));//md.getTables(null, null, "%", null);
			         while (rs.next()) {
			        	 atributtesModel.addElement(rs.getString(1));
			         }
		       }
		      }
		      catch (SQLException ex)
		      {
		         // en caso de error, se muestra la causa en la consola
		         System.out.println("SQLException: " + ex.getMessage());
		         System.out.println("SQLState: " + ex.getSQLState());
		         System.out.println("VendorError: " + ex.getErrorCode());
		      }  
	   }
*/
	
}
