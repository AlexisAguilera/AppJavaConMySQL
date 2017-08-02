package Core;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;


/**
 * Esta clase provee una interfaz para poder actualizar la pantalla principal de
 * las aplicaciones que serÃ¡n ejecutadas por los diferentes usuarios.
 *
 */
public abstract class GeneralGUI {

//Atributos

	public javax.swing.JInternalFrame ventana;
	
	
//Constructures
	
	/**
	 * Constructor de la clase.
	 * Iniciliza la ventana de la aplicacion.
	 */
	protected GeneralGUI(){
		//ventana= new JFrame();
		ventana = new javax.swing.JInternalFrame("", true, true);
		//ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//ventana.setLocationByPlatform(true);
		//ventana.setUndecorated(true);
		//ventana.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	}
		
	
//Consultas y Comandos
		
	/**
	 * Elimina todos los componentes de la ventana actual y carga una nueva pantalla.
	 * @param pantalla La nueva pantalla.
	 */
	protected void actualizarPantalla(JPanel pantalla){
		ventana.getContentPane().removeAll();
		ventana.getContentPane().add(pantalla);
		
		ventana.setResizable(true);
		ventana.setSize(pantalla.getSize());
		ventana.setResizable(false);
	
	}
	
	/**
	 * Elimina todos los objetos asociados a la ventana y la cierra.
	 */
	public void finalizar(){
		ventana.dispose();	
		try {
			finalize();
		} catch (Throwable e) {e.printStackTrace();}
		
	}
	
	/**
	 * Establece el título del JFrame.
	 * @param title Título a introducir en el JFrame.
	 */
	protected void setTitle(String title){
		ventana.setTitle( title );
	}
}
