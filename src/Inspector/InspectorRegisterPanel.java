package Inspector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/**
 * Panel de administración de la BD que usará el inspector del sistema.
 * Es uno de los paneles de InspectorGUI.
 * @see InspectorGUI
 */
public class InspectorRegisterPanel extends JPanel {

//Atributos

	private InspectorGUI manejador;
	private JLabel encabezado;
	private JLabel jLabel3;
	private JLabel labelStatus;
	private JButton btnLogin;
	private JPasswordField pswField;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JTextField legField;

//Constructores

	/**
	 * Constructor de la clase.
	 * Inicializa los atributos y crea todos los componentes del panel.
	 * @param manejador El objeto encargado de la visualización de las diferentes pantallas.
	 */
	public InspectorRegisterPanel(InspectorGUI manejador){
		super();
		this.manejador= manejador;
		
		//Panel principal.
		this.setSize(450, 250);
		this.setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(435, 225));

		//Panel superior.
		JLabel titulo = new JLabel("Inspector Register");
		this.add(titulo, "cell 0 0");
		titulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titulo.setText("Inspector Register");
		titulo.setBounds(7, 16, 131, 20);

		JPanel panelLogout = new JPanel();
		this.add(panelLogout, "cell 1 0");

		panelLogout.setLayout(new MigLayout("", "[467.00px]", "[23px]"));
		panelLogout.setBounds(597, 7, 76, 37);
		{
			JButton btnSalir = new JButton("Salir");
			this.add(btnSalir);
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logout();
				}
			});
			btnSalir.setHorizontalAlignment(SwingConstants.RIGHT);
			btnSalir.setBounds(334, 10, 75, 22);
		}
		{
			encabezado = new JLabel();
			this.add(encabezado);
			encabezado.setText("Actualmente se encuentra logueado como usuario inspector");
			encabezado.setBounds(7, 42, 357, 16);
		}
		{
			jLabel3 = new JLabel();
			this.add(jLabel3);
			jLabel3.setText("en la BD; ingrese sus datos para poder registrarlos:");
			jLabel3.setBounds(7, 58, 333, 16);
		}
		{
			legField = new JTextField();
			this.add(legField);
			legField.setBounds(130, 86, 134, 22);
		}
		{
			jLabel1 = new JLabel();
			this.add(jLabel1, "cell 0 1");
			jLabel1.setText("Ingrese LEGAJO:");
			jLabel1.setBounds(7, 89, 105, 16);
		}
		{
			jLabel2 = new JLabel();
			this.add(jLabel2, "cell 0 1");
			jLabel2.setText("Ingrese PASSWORD:");
			jLabel2.setBounds(7, 117, 125, 16);
		}
		{
			pswField = new JPasswordField();
			this.add(pswField);
			pswField.setBounds(129, 114, 134, 22);
		}
		{
			btnLogin = new JButton();
			this.add(btnLogin);
			btnLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loguearInspector();
				}
			});
			btnLogin.setText("Ingresar");
			btnLogin.setBounds(132, 148, 84, 22);
		}		
		{
			labelStatus = new JLabel();
			this.add(labelStatus);
			labelStatus.setBounds(7, 181, 416, 16);
			labelStatus.setForeground(new java.awt.Color(255,6,6));
			labelStatus.setFont(new java.awt.Font("Segoe UI",1,12));
			labelStatus.setVisible(true);
		}
	}
	
//Consultas y Comandos

/****************************************************************************/
/* OPERACIONES ESPECÍFICAS QUE ESTABLECEN RELACIÓN CON InspectorGUI */
/****************************************************************************/
	
	/**
	 * Ordena al manejador que valide los datos del inspector que intenta
	 * loguearse. En caso de ser incorrectos los datos, muestra un mensaje.-
	 */
	private void loguearInspector(){
		long legajo = -1;
		
		try{
		if(legField.getText() != "")
			legajo = Integer.parseInt(legField.getText());
		}catch(java.lang.NumberFormatException e){
			pswField.setText("");
			legField.setText("");
			labelStatus.setText("Datos incorrectos. Reintente.-");
			return;
		}
		
		String psw = String.valueOf(pswField.getPassword());
		boolean resultado = false;
		
		try{
		
			resultado = manejador.registrarInspector( legajo, psw);
			
			if( !resultado ){
				pswField.setText("");
				legField.setText("");
				labelStatus.setText("Datos incorrectos. Reintente.-");
			}
		
		}catch(SQLException e){
			mostrarMensaje("Error N°: " + e.getErrorCode() + " - " + e.getMessage() , JOptionPane.ERROR_MESSAGE);
			mostrarMensaje("Error en operación de login. Reintente.-", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	/**
	 * Le informa al manejador que ejecute la accion para terminar la sesión
	 * de inspector dentro de la base de datos.-
	 */
	private void logout(){
		try {
			
			manejador.logout();
		
		} catch (SQLException e) {
			mostrarMensaje("Error N°: " + e.getErrorCode() + " - " + e.getMessage() , JOptionPane.ERROR_MESSAGE);
			mostrarMensaje("Error en operación de logout. Reintente.-", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Muestra un mensaje indicando algún error.-
	 * @param mensaje El mensaje que se desea mostrar.
	 * @param Formato Formato de mensaje. Se asume consistente con los formatos provistos por JOptionPane.
	 * @see JOptionPane
	 */
	private void mostrarMensaje(String mensaje, int formato ){
		JOptionPane.showMessageDialog( this, mensaje, "¡Error! SQL EXCEPTION.-", formato );
	}
}
