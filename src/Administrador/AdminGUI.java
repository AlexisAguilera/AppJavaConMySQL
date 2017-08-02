package Administrador;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Core.GeneralGUI;

//import parquimetros.AdminLogic;
//import parquimetros.AdminLoginPanel;

/**
 * Se encarga de proveer al administrador una interfaz gráfica para
 * el manejo de la base de datos.
 * @see GeneralGUI
 */
public class AdminGUI extends GeneralGUI{

//Atributos
	
	public AdminLogic logica;
	
//Constructores
	
	/**
	 * Constructor de la clase.
	 * Inicializa la aplicacion del administrador
	 */
	public AdminGUI(){
		super();
		logica= new AdminLogic("localhost:3306", "parquimetros");
		
		this.actualizarPantalla(new AdminLoginPanel(this)); 
		ventana.setVisible(true);
		setTitle("Acceso como Administrador");
	}
	
//Consultas y Comandos
	
	/**
	 * Intenta logear al administrador de la BD.
	 * @param psw La contraseña del administrador.
	 * @throws SQLException En caso de que no se pueda establecer la conexión.
	 */
	public void login(String psw) throws SQLException {
		try{
			logica.conectarse("admin", psw);
		}catch(SQLException e) {
			throw new SQLException("Contraseña incorrecta");
		}
		this.actualizarPantalla(new AdminManagmentPanel(this));
		this.setTitle("Administracion de la Base de Datos");
	}

	/**
	 * Termina con la sesión del administrador y avisa a la logica que cierre la base de datos.
	 * @throws SQLException En caso de que no pueda desconectarse de la base de datos.
	 */
	public void logout() throws SQLException {
		logica.desconectarse();
		logica=null;
		this.finalizar();
	}
	
	/**
	 * Envia a la logica el comando que se intenta ejecutar.
	 * @param text El comando para ejecutar.
	 * @throws SQLException En caso de que no se pueda ejecutar el comando correctamente.
	 */
	public JTable ejecutarComando(String comando) throws SQLException {
		JTable toReturn= null;
		Vector<Vector<Object>> resultado = null;//logica.ejecutarComando(comando);
		
		if(resultado!=null){
			
			//Obtengo los nombres de las columnas.
			Vector<Object> nombres = resultado.get(0);
			resultado.remove(0);
			
			//Creo la tabla.
			toReturn = new JTable( new DefaultTableModel(resultado, nombres));		
		}
		
		return toReturn;
	}
	
	/**
	 * Retorna el nombre de la base de datos a la que esta conectada la aplicación.
	 * @return El nombre de la base de datos.
	 */
	public String getBD() {
		return logica.getBD();
	}
	
	/**
	 * Retorna un JTree con la estructura de la BD.
	 * Posee las tablas de la BD y cada uno de sus atributos.
	 * @return La estructura de la BD.
	 * @see JTree.
	 */
	public JTree obtenerTablas(){
		
		//Creo el árbol y adhiero la raíz.
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(logica.getBD());		
		JTree toReturn = new JTree(new DefaultTreeModel(raiz));
		
		
		Iterator<LinkedList<String>> tablas = logica.obtenerTablas().iterator();
		Iterator<String> columnas = null;
		DefaultMutableTreeNode tabla= null;
		
		//Completo el árbol.
		while(tablas.hasNext()){
			columnas=tablas.next().iterator();
			
			//Adhiero la tabla
			if(columnas.hasNext()){
				tabla = new DefaultMutableTreeNode(columnas.next());
				raiz.add(tabla);
			}
			
			//Adhiero los atributos de la tabla
			while(columnas.hasNext()){
				tabla.add(new DefaultMutableTreeNode(columnas.next()));
			}
		}
		
		return toReturn;
	}
	
	 public DefaultListModel obtenerListaTabla()
	   {
		   DefaultListModel toReturn = new DefaultListModel();
		   for (String tab : logica.consultarTablas()) {
			   toReturn.addElement(tab);
			   System.out.println(tab);
		   }
		   return toReturn;
	   }

}
