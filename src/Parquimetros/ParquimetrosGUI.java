package Parquimetros;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

import Core.GeneralGUI;


/**
 * Se encarga de proveer una interfaz gráfica para simular la conexión
 * entre los cospeles y los parquimetros.
 * @see GeneralGUI
 */
public class ParquimetrosGUI extends GeneralGUI {

//Atributos
	
	private ParquimetrosLogic logica;
	
//Constructores
	
	/**
	 * Constructor de la clase.
	 * Inicializa la interfaz para la simulación de los parquimetros.
	 */
	public ParquimetrosGUI() {
		super();
		
		try {
			
			logica= new ParquimetrosLogic("localhost:3306", "parquimetros");
			this.actualizarPantalla(new ParquimetrosPanel(this)); 
			ventana.setVisible(true);
			setTitle("Parquimetro");
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this.ventana, "No se pude establecer conexión con la BD.", "ErrorSQL Codigo: "+e.getErrorCode(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
//Consultas y Comandos
	
	/**
	 * Retorna el nombre de todas las ubicaciones registradas en la base de datos.
	 * @return La ubicaciones registradas en la BD.
	 * @throws SQLException En caso de un error de conexión. 
	 */
	public Vector<String> getUbicaciones() throws SQLException{
		return logica.getUbicaciones();
	}
	
	/**
	 * Retorna todas la alturas asociadas a una misma ubicación.
	 * @param ubicacion La ubicación para la cual se desean saber sus alturas.
	 * @return Las posible alturas para una ubicación.
	 * @throws SQLException En caso de un error de conexión.
	 */
	public Vector<String> getAlturas(String ubicacion) throws SQLException{
		return logica.getAlturas(ubicacion);
	}
	
	/**
	 * Retorna el Id de todos los parquimetros que se encuentran en una ubicación y
	 * altura específicas.
	 * @param ubicacion La ubicación de los parquimetros.
	 * @param altura La altura asociada a la ubicación de los parquimetros.
	 * @return Los parquimetros en la altura y ubicación parametrizados.
	 * @throws SQLException En caso de un error de conexión.
	 */
	public Vector<String> getParquimetros(String ubicacion, int altura) throws SQLException{
		return logica.getParquimetros(ubicacion, altura);
	}
	
	/**
	 * Retorna el Id de todos los cospeles registrados en la BD del sistema.
	 * @return El id de todos los cospeles de la BD.
	 * @throws SQLException En caso de un error de conexión. 
	 */
	public Vector<String> getCospeles() throws SQLException{
		return logica.getCospeles();
	}
	
	/**
	 * Simula la conexión de un cospel y un parquimetro.
	 * @param m El Id del parquimetro.
	 * @param l El Id del cospel
	 * @return Los resultados de la operación en el siguiente orden:
	 * (apertura|cierre) : Inidica el estado del estacionamiento.
	 * 
	 * (Tiempo [Min])  	 : Si apertura => El tiempo disponible. 
	 * 			  		   Si cierre => El tiempo transcurrido.
	 * 
	 * (Saldo [$]) 		 : Si cierre => El saldo restante.
	 * @throws SQLException En caso de un error de conexión. 
	 */
	public Vector<String> conectar(long l, long m) throws SQLException{
		Vector<String> resultados = logica.conectar(l, m);
		Vector<String> toReturn = new Vector<String>();
		
		toReturn.add(resultados.get(0));
		
		if(resultados.get(1).equals("error"))
			toReturn.add("0");
		else
			if(resultados.get(1).equals("exito"))
				toReturn.add(resultados.get(2));
			else{
				toReturn.add(resultados.get(1));
				toReturn.add(resultados.get(2));
			}
				
		return toReturn;
	}

	/**
	 * Termina la ejecución de la ventana y desconecta al usuario de la BD.
	 * @throws SQLException Si se produce un error en la BD durante la desconexion.
	 */
	public void salir() throws SQLException {
		logica.desconectarse();
		logica=null;
		this.finalizar();
	}

}
