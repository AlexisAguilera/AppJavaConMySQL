package Parquimetros;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import Core.BDLogic;

/**
 * Esta clase es la encargada de establecer una interfaz entre la BD
 * y el simulador de parquímetros.
 * @see BDLogic.
 */
public class ParquimetrosLogic extends BDLogic {

	
//Constructores
	
	/**
	 * Inicializa los atributos y se conecta con la BD.
	 * @param servidor El servidor donde se encuentra la BD.
	 * @param bd El nombre del la BD.
	 * @throws SQLException En caso de un error de conexión.
	 */
	public ParquimetrosLogic(String servidor, String bd) throws SQLException {
		super(servidor, bd);
		conectarse("parquimetro", "parq");
		
	}
	
//Consultas y Comandos
	
	/**
	 * Simula la conexión entre un cospel y un parquímetro.
	 * @param l El cospel seleccionado.
	 * @param m El parquímetro seleccionado.
	 * @return El resultado de la conexión.
	 * @throws SQLException En caso de algun error con el procedimiento.
	 */
	public Vector<String> conectar(long l, long m) throws SQLException{
		Vector<String> toReturn = new Vector<String>();
				
		ResultSet r = conexion.createStatement().executeQuery("call conectar (" + l +"," + m + ");");
		r.next();
		
		for(int i=1; i<=r.getMetaData().getColumnCount(); i++){
			toReturn.add(r.getString(i));
		}
		
		return toReturn;
	}
	
	/**
	 * Retorna el id_cospel de todo los cospeles registrados en la BD.
	 * @return Todo los cospeles de la BD.
	 * @throws SQLException Si se produce un error con la consulta SQL.
	 */
	public Vector<String> getCospeles() throws SQLException{
		Vector<String> toReturn = new Vector<String>();
		
		ResultSet r = conexion.createStatement().executeQuery("SELECT id_cospel FROM Cospeles;");
		
		while(r.next()){
			toReturn.add(String.valueOf(r.getInt(1)));
		}
		
		return toReturn;
	}
	
	/**
	 * Retorna el nombre de todas las calles en las que se encuentra algun parquimetro.
	 * @return El nombre de todas las calles con parquimetros.
	 * @throws SQLException Si se produce un error con la consulta SQL.
	 */
	public Vector<String> getUbicaciones() throws SQLException{
		Vector<String> toReturn = new Vector<String>();
		
		ResultSet r = conexion.createStatement().executeQuery("SELECT DISTINCT calle FROM Parquimetros;");
		
		while(r.next()){
			toReturn.add(r.getString(1));
		}
		
		return toReturn;
	}
	
	/**
	 * Retorna todas las alturas asociadas a una calle.
	 * @param ubicacion El nombre de la calle.
	 * @return Todas las alturas asociadas a un calle.
	 * @throws SQLException Si se produce un error con la consulta SQL.
	 */
	public Vector<String> getAlturas(String ubicacion) throws SQLException{
		Vector<String> toReturn = new Vector<String>();
		
		ResultSet r = conexion.createStatement().executeQuery("SELECT DISTINCT altura " +
															  "FROM Parquimetros " +
															  "WHERE calle='"+ubicacion+"';");
		
		while(r.next()){
			toReturn.add(String.valueOf(r.getInt(1)));
		}
		
		return toReturn;
	}
	
	
	/**
	 * Retorna el id_parq de todos los parquimetros que se encuentran en una calle
	 * a una altura determinada.
	 * @param ubicacion El nombre de la calle.
	 * @param altura La altura de la calle.
	 * @return Todos los parquimetros de una calle y altura determinadas.
	 * @throws SQLException Si se produce un error con la consulta SQL.
	 */
	public Vector<String> getParquimetros(String ubicacion, int altura) throws SQLException{
		Vector<String> toReturn = new Vector<String>();
		
		ResultSet r = conexion.createStatement().executeQuery("SELECT id_parq " +
				  											  "FROM Parquimetros " +
				  											  "WHERE calle='"+ubicacion+"' AND "+
				  											  	    "altura="+altura+";");

		while(r.next()){
			toReturn.add(String.valueOf(r.getInt(1)));
		}
		
		return toReturn;
	}

}
