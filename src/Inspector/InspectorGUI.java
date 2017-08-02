package Inspector;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Core.GeneralGUI;

/**
 * Se encarga de proveer al inspector una interfaz gr�fica para
 * el manejo de la base de datos.
 * @see GeneralGUI
 */
public class InspectorGUI extends GeneralGUI{

//Atributos
	
	private InspectorLogic logica;
	
//Constructores
	
	/**
	 * Constructor de la clase.
	 * Inicializa la aplicacion del inspector
	 */
	public InspectorGUI(){
		super();
		logica= new InspectorLogic("localhost:3306", "parquimetros");
		
		this.actualizarPantalla(new InspectorRegisterPanel(this)); 
		
		try {
			login("inspector");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(ventana, e.getMessage(), "ErrorSQL Codigo: "+String.valueOf(e.getErrorCode()), JOptionPane.ERROR_MESSAGE);
		}
		ventana.setVisible(true);
		setTitle("LOGIN || Usuario Inspector.-");
	}
	
//Consultas y Comandos
	
	/**
	 * Intenta logear al inspector de la BD.
	 * @param psw La contrase�a del inspector.
	 * @throws SQLException En caso de que no se pueda establecer la conexi�n.
	 */
	public void login(String psw) throws SQLException {
		try{
			logica.conectarse("inspector", psw); 
		}catch(SQLException e) {
			throw new SQLException("Contrase�a incorrecta");
		}
		this.actualizarPantalla(new InspectorRegisterPanel(this));
		setTitle("REGISTRANDO INSPECTOR || Ingrese sus datos.-");
	}

	/**
	 * Termina con la sesi�n del inspector y avisa a la logica que cierre la base de datos.
	 * @throws SQLException En caso de que no pueda desconectarse de la base de datos.
	 */
	public void logout() throws SQLException {
		logica.desconectarse();
		logica=null;
		this.finalizar();
	}
	
	/**
	 * Ordena a la l�gica que se eval�e si los datos corresponden con un inspector registrado.
	 * En caso de serlo, abre la ventana correspondiente al control de parqu�metros.-
	 * @param insp Legajo del inspector.
	 * @param psw Password correspondiente al inspector.
	 * @throws SQLException En el caso de alguna falla a la hora de acceder a la base de datos.-
	 */
	public boolean registrarInspector(long insp, String psw) throws SQLException{
		
		boolean toReturn = logica.verificarLogin(insp, psw);
		
		if ( toReturn ){
			this.actualizarPantalla( new InspectorManagmentPanel( this, insp ));
			setTitle("CONTROL DE PARQ. || Ingrese los datos.-");
		}
		
		return toReturn;
		
	}
	
	/**
	 * Se encarga de cargar los parqu�metros cargados en la base de datos dentro del modelParquimetros.-
	 * @param modelParquimetros ModelComboBox donde se guardar�n los parqu�metros.-
	 * @throws SQLException en caso de alg�n error en la base de datos ante la consulta.-
	 */
	public void cargarParquimetros(DefaultComboBoxModel modelParquimetros) throws SQLException{
		
		//Variables locales.-
		int i;
		Vector<String> parquimetros;
		
		//Obtengo los parquimetros con el formato establecido.-
		parquimetros = logica.getParquimetros();
		
		//Cargo el modelParquimetros con los valores consutados.-
		for(i=0; i<parquimetros.size(); i++)
			modelParquimetros.addElement( parquimetros.elementAt(i));
		
	}
	
	/**
	 * Dado un inspector, un d�a y un turno, ordena controlar que el parqu�metro seleccionado por el 
	 * inspector sea el correspondiente a la ubicaci�n que tal ten�a que controlar; para tal operaci�n,
	 * solicita a la l�gica que realice las consultas necesarias sobre la BD.-
	 * @param leg N�mero de legajo del inspector.-
	 * @param parq String con formato "ID-@-NUMERO" correspondiente a los atributos de un parqu�metro.-
	 * @param turno 'M' o 'T' indicando si el turno es ma�ana o tarde respectivamente.-
	 * @param dia Corresponde a las 2 primeras letras del d�a en el que se accede al parqu�metro.-
	 * @return True si el parqu�metro es v�lido para ese inspector con las condiciones dadas,
	 * false en caso contrario.-
	 * @throws SQLException Si sucede alguna falla en la base de datos.-
	 */
	public boolean controlarParquimetro( long leg, String parq, char turno, String dia) throws SQLException{
		
		//Variables locales.-
		int i,j=0;
		long id_parq, num_parq;
		String aux="";
		
		//Extrae del string parq, el ID del parqu�metro.-
		for(i=0; j==0; i++){
			aux += parq.charAt(i);
			if (parq.charAt(i+1)=='-')
				j=i+4;
		}
		
		id_parq = Long.parseLong(aux); aux="";
		
		//Extrae del string parq, el NUMERO del parqu�metro.-
		for(i=j; i<parq.length(); i++ )
			aux+= parq.charAt(i);
				
		num_parq = Long.parseLong(aux);
		
		return logica.controlarParquimetro ( leg, id_parq, num_parq, turno, dia );
	}
	
	/**
	 * Dado el String parq, se descompone tal informaci�n en id_parq y num_parq; con �stos datos y
	 * un vector de string que contienen todas las patentes que ven�an alojadas en modelPatentes, se 
	 * ordena a la l�gica que genere las multas correspondientes para los autom�viles estacionados,
	 * el parqu�metro seleccionado, y los datos que se tengan en la base de datos. A la vez, se ordena
	 * que se registre el acceso al parqu�metro por parte del inspector con el legajo parametrizado.-
	 * @param modelPatentes Model de un JList que contiene las patentes controladas por el inspector.-
	 * @param parq String con formato: "ID_PARQ-@-NUM_PARQ"
	 * @param time Objeto TimeSQL indicando la hora.-
	 * @param data Objeto DataSQL indicando la fecha.-
	 * @param turno 'M' o 'T' indica ma�ana o tarde.-
	 * @param dia Primeras dos letras del d�a en el que se va a generar la multa.-
	 * @return JTable con los resultados de la generaci�n de multas.-
	 * @throws SQLException En caso de ocurrir alg�n error en la base de datos.-
	 */
	public JTable generarMultas(long legajo, String parq, DefaultListModel modelPatentes, Date data, Time time, String dia, char turno) throws SQLException{
		//Variables locales.-
		int i,j=0;
		long id_parq;
		JTable toReturn;
		String aux="";
			
		//Extrae del string parq, el ID del parqu�metro.-
		for(i=0; j==0; i++){
			aux += parq.charAt(i);
			if (parq.charAt(i+1)=='-')
				j=i+4;
		}
				
		id_parq = Long.parseLong(aux); 
		
		//Se extraen del modelPatentes todas las patentes y se guardan en un arreglo de Strings.-
		Vector<String> patentes = new Vector<String>();
		for(i=0; i<modelPatentes.getSize(); i++){
			aux = (String) modelPatentes.getElementAt(i);
			aux = aux.substring(0, 3) + aux.substring(4,7);
			patentes.add(i, aux );
		}
		
		//Se registra el acceso al parqu�metro por parte del inspector con legajo, fecha y hora
		//parametrizado.-
		logica.registrarAcceso( legajo, id_parq, data, time);
		
		//Se ordena a la l�gica generar las multas y se obtiene el resultado a mostrar por 
		//pantalla en un arreglo que contiene un arreglo por cada tupla con los valores de dicha
		//tupla dentro.- 
		Vector<Vector<Object>> resultado = logica.generarMultas( id_parq, legajo, patentes, data, time, dia, turno);
		
		//Obtengo los nombres de las columnas del resultado.
		Vector<Object> nombres = resultado.get(0);
		resultado.remove(0);
		
		//Creo la tabla con los nombres de las columnas y las tuplas retornadas desde la l�gica.
		toReturn = new JTable( new DefaultTableModel(resultado, nombres));
		
		return toReturn;
	}
		
	/**
	 * Retorna el nombre de la base de datos a la que esta conectada la aplicaci�n.
	 * @return El nombre de la base de datos.
	 */
	public String getBD() {
		return logica.getBD();
	}
}
