package Inspector;
import javax.swing.DefaultListModel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Font;
import javax.swing.text.MaskFormatter;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;




/**
 * Panel de administración de la BD que usará el inspector del sistema.
 * Es uno de los paneles de InspectorGUI.
 * @see InspectorGUI
 */
public class InspectorManagmentPanel extends JPanel {

	//Atributos

	private InspectorGUI manejador;
	
	private long legajo;
	private String [] fechas;

	private Calendar fechaHora;
	
	private JLabel titulo;
	private JLabel lblTurno;
	private JLabel lblFechasasas;
	private JLabel lblMultas;
	private JLabel lblHora;
	private JLabel lblFecha;
	private JLabel lblErrorPat;
	private JLabel lblErrorParq;
	private JLabel lblFormato;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	
	private JPanel Patentes;
	private JPanel Estadisticas;
	private JPanel Resultados;
	private JPanel Parquimetro;
	
	private JButton btnBaja;
	private JButton btnConfirmar;
	private JButton btnAtras;
	private JButton btnAlta;
	private JButton finCarga;
	
	private JTextField fechaField;
	private JTextField turnoField;
	private JFormattedTextField patenteField;
		
	private JComboBox boxParquimetros;
	private JScrollPane scrollResultados;
	
	private JTable tblResultados;
	private JList listaPatentes;
	
	private DefaultListModel modelPatentes;
	private DefaultComboBoxModel modelParquimetros;
	
//Constructores

	/**
	 * Constructor de la clase.
	 * Inicializa los atributos y crea todos los componentes del panel.
	 * @param manejador El objeto encargado de la visualización de las diferentes pantallas.
	 */
	public InspectorManagmentPanel(InspectorGUI manejador, long leg){
		super();
		
		legajo = leg;
		fechaHora = Calendar.getInstance();
		fechas = new String[] {"Do","Lu","Ma","Mi","Ju","Vi","Sa"};
		this.manejador = manejador;
		
		
		//Panel principal.
		this.setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(679, 616));
		this.setSize(679, 659);

		titulo = new JLabel("Inspector Managment; legajo N° "+legajo);
		this.add(titulo, "cell 0 0");
		titulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titulo.setBounds(7, 14, 286, 20);

		{
			JButton btnSalir = new JButton("Salir");
			this.add(btnSalir);
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logout();
				}
			});
			btnSalir.setHorizontalAlignment(SwingConstants.RIGHT);
			btnSalir.setBounds(520, 10, 119, 22);
			btnSalir.setText("Desloguearse");
		}
		
		//Panel Parquimetros
		
		{
			Parquimetro = new JPanel();
			this.add(Parquimetro);
			Parquimetro.setBorder(BorderFactory.createTitledBorder(""));
			Parquimetro.setBounds(310, 37, 330, 142);
			Parquimetro.setLayout(null);
			{
				jLabel3 = new JLabel();
				Parquimetro.add(jLabel3);
				jLabel3.setText("Seleccione el parquimetro");
				jLabel3.setBounds(11, 10, 291, 16);
				jLabel3.setFont(new java.awt.Font("Tahoma",0,12));
			}
			{
				modelParquimetros =	new DefaultComboBoxModel();
				boxParquimetros = new JComboBox( modelParquimetros );
				Parquimetro.add(boxParquimetros);
				boxParquimetros.addActionListener( new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						lblErrorParq.setVisible(false);
					}
				});
				boxParquimetros.setBounds(11, 57, 302, 22);
			}
			{
				btnAtras = new JButton();
				Parquimetro.add(btnAtras);
				btnAtras.addActionListener( new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						lblErrorParq.setVisible(false);
						boxParquimetros.setSelectedIndex(-1);
						deshabilitarPanelParquimetros();
						habilitarPanelPatentes();
					}
				});
				btnAtras.setText("Atras");
				btnAtras.setBounds(112, 82, 96, 22);
			}
			{
				btnConfirmar = new JButton();
				Parquimetro.add(btnConfirmar);
				btnConfirmar.addActionListener( new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (boxParquimetros.getSelectedIndex()==-1){
							lblErrorParq.setText("Debe seleccionar parquímetro.-");
							lblErrorParq.setForeground(Color.RED);
							lblErrorParq.setVisible(true);
						}else
							if ( esValidoParq() ){
								
								lblErrorParq.setText("Parquímetro correcto.-");
								lblErrorParq.setForeground(Color.BLUE);
								lblErrorParq.setVisible(true);
								deshabilitarPanelParquimetros();
								generarResultados();
								habilitarPanelResultados();
							}else{
								boxParquimetros.setSelectedIndex(-1);
								lblErrorParq.setText("Parquimetro/Ubicación para este día y turno incorrecto.-");
								lblErrorParq.setForeground(Color.RED);
								lblErrorParq.setVisible(true);
							}
					}
					
				});
				btnConfirmar.setText("Confirmar");
				btnConfirmar.setBounds(11, 82, 96, 22);
			}
			{
				lblErrorParq = new JLabel();
				Parquimetro.add(lblErrorParq);
				lblErrorParq.setBackground(new java.awt.Color(248,16,3));
				lblErrorParq.setVisible(false);
				lblErrorParq.setFont(new java.awt.Font("Segoe UI",1,12));
				lblErrorParq.setForeground(new java.awt.Color(255,17,17));
				lblErrorParq.setBounds(11, 115, 314, 16);
			}
			{
				jLabel5 = new JLabel();
				Parquimetro.add(jLabel5);
				jLabel5.setText("Formato: ID_PARQ -@- NUMERO_PARQ");
				jLabel5.setBounds(11, 31, 302, 16);
			}
		}
		
		//Panel registro de Patentes
		
		{
			Patentes = new JPanel();
			this.add(Patentes);
			Patentes.setBounds(9, 67, 295, 175);
			Patentes.setBorder(BorderFactory.createTitledBorder(""));
			Patentes.setLayout(null);
			{
				jLabel2 = new JLabel();
				Patentes.add(jLabel2);
				jLabel2.setText("Ingrese Patente:");
				jLabel2.setBounds(105, 36, 143, 16);
			}
			{
				MaskFormatter masc = new MaskFormatter();
				
				try {
					masc = new MaskFormatter("UUU-###");
					masc.setPlaceholderCharacter('_');
				} catch (ParseException e) {}
				
				patenteField = new JFormattedTextField( masc );
				Patentes.add(patenteField);
				patenteField.addKeyListener(new KeyAdapter() {
					
					@Override
					public void keyPressed(KeyEvent arg0) {
						lblFormato.setVisible(false);
						lblErrorPat.setVisible(false);
					}				
				});
				patenteField.setBounds(105, 58, 69, 22);
			}
			{
				btnAlta = new JButton();
				Patentes.add(btnAlta);
				btnAlta.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(esValidaPat())
							if(esValidaPat_2()){
								modelPatentes.addElement( patenteField.getText() );
								patenteField.setText("");
								lblFormato.setForeground(Color.BLUE);
								lblFormato.setText("Patente Cargada.-");
								lblErrorPat.setVisible(false);
								lblFormato.setVisible(true);
							}else{
								lblFormato.setForeground(Color.RED);
								lblFormato.setText("Error de Formato.-");
								lblErrorPat.setVisible(false);
								lblFormato.setVisible(true);
							}
						else{
							lblFormato.setForeground(Color.RED);
							lblFormato.setText("Patente duplicada.-");
							lblErrorPat.setVisible(false);
							lblFormato.setVisible(true);
						}
					}
				});
				btnAlta.setText("Alta");
				btnAlta.setBounds(105, 91, 70, 22);
			}
			{
				btnBaja = new JButton();
				Patentes.add(btnBaja);
				btnBaja.addActionListener( new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (listaPatentes.getSelectedIndex() == -1){
							lblErrorPat.setForeground(Color.RED);
							lblErrorPat.setText("Seleccione para borrar.-");
							lblFormato.setVisible(false);
							lblErrorPat.setVisible(true);
						}else{
							modelPatentes.removeElementAt( listaPatentes.getSelectedIndex());
							listaPatentes.setSelectedIndex(-1);
							lblErrorPat.setForeground(Color.BLUE);
							lblErrorPat.setText("Patente Eliminada.-");
							lblFormato.setVisible(false);
							lblErrorPat.setVisible(true);
							
						}
					}
				});
				btnBaja.setText("Baja");
				btnBaja.setBounds(105, 118, 70, 22);
			}
			{
				modelPatentes = new DefaultListModel();
				listaPatentes = new JList(modelPatentes);
				listaPatentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				Patentes.add(listaPatentes);
				JScrollPane sp = new JScrollPane( listaPatentes );
				sp.setBounds(5, 33, 94, 120);
				Patentes.add(sp);				
			}
			{
				lblFormato = new JLabel();
				Patentes.add(lblFormato);
				lblFormato.setBounds(180, 58, 110, 16);
				lblFormato.setBackground(new java.awt.Color(248,16,3));
				lblFormato.setForeground(new java.awt.Color(255,17,17));
				lblFormato.setFont(new java.awt.Font("Segoe UI",1,12));
				lblFormato.setVisible(false);
			}
			{
				jLabel1 = new JLabel();
				Patentes.add(jLabel1, "cell 0 0");
				jLabel1.setText("Registro de Patentes en estacionamiento");
				jLabel1.setFont(new java.awt.Font("Tahoma",0,12));
				jLabel1.setBounds(5, 6, 286, 20);
			}
			{
				lblErrorPat = new JLabel();
				Patentes.add(lblErrorPat);
				lblErrorPat.setBackground(new java.awt.Color(248,16,3));
				lblErrorPat.setForeground(new java.awt.Color(255,17,17));
				lblErrorPat.setFont(new java.awt.Font("Segoe UI",1,12));
				lblErrorPat.setVisible(false);
				lblErrorPat.setBounds(105, 139, 173, 16);
			}
			{
				finCarga = new JButton();
				Patentes.add(finCarga);
				finCarga.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						deshabilitarPanelPatentes();
						generarDatosParquimetros();
						habilitarPanelParquimetros();
					}
					
				});
				finCarga.setText("Fin Carga");
				finCarga.setBounds(186, 91, 93, 49);
			}
		}
		
		//Panel de ESTADÍSTICAS
		{
			Estadisticas = new JPanel();
			this.add(Estadisticas);
			Estadisticas.setBounds(310, 185, 331, 57);
			Estadisticas.setLayout(null);
			Estadisticas.setBorder(BorderFactory.createTitledBorder(""));
			{
				jLabel4 = new JLabel();
				Estadisticas.add(jLabel4);
				jLabel4.setText("Generación de estadísiticas");
				jLabel4.setBounds(9, 7, 210, 16);
				jLabel4.setFont(new java.awt.Font("Tahoma",0,12));
			}
			{
				lblFecha = new JLabel();
				Estadisticas.add(lblFecha);
				lblFecha.setBounds(9, 29, 110, 17);
				lblFecha.setVisible(true);
			}
			{
				lblHora = new JLabel();
				Estadisticas.add(lblHora);
				lblHora.setBounds(119, 27, 95, 20);
				lblHora.setVisible(true);
			}
			{
				lblMultas = new JLabel();
				Estadisticas.add(lblMultas);
				lblMultas.setBounds(220, 28, 80, 18);
				lblMultas.setVisible(true);
			}
		}
		{
			lblFechasasas = new JLabel();
			this.add(lblFechasasas);
			lblFechasasas.setBounds(7, 39, 46, 16);
			lblFechasasas.setText("Fecha:");
		}
		{
			fechaField = new JTextField();
			this.add(fechaField);
			fechaField.setBounds(50, 39, 53, 19);
			fechaField.setEnabled(false);
			fechaField.setText( fechas[ fechaHora.get(Calendar.DAY_OF_WEEK) -1 ] );
		}
		{
			lblTurno = new JLabel();
			this.add(lblTurno);
			lblTurno.setText("Turno:");
			lblTurno.setBounds(115, 38, 46, 16);
		}
		{
			turnoField = new JTextField();
			this.add(turnoField);
			turnoField.setBounds(161, 38, 53, 19);
			if ( fechaHora.get(Calendar.AM_PM) == Calendar.AM  )
				turnoField.setText("Mañana");
			else
				turnoField.setText("Tarde");
			turnoField.setEnabled(false);
		}
		{
			Resultados = new JPanel();
			this.add(Resultados);
			Resultados.setBounds(12, 254, 629, 350);
			Resultados.setLayout(null);
			Resultados.setBorder(BorderFactory.createTitledBorder(""));
			{
				jLabel6 = new JLabel();
				Resultados.add(jLabel6);
				jLabel6.setText("Visualización de los Resultados");
				jLabel6.setBounds(12, 12, 362, 16);
				jLabel6.setFont(new java.awt.Font("Tahoma",0,12));
			}
			{
				TableModel tblResultadosModel = new DefaultTableModel();
				tblResultados = new JTable();
				//Resultados.add(tblResultados);
				tblResultados.setModel(tblResultadosModel);
				tblResultados.setBounds(12, 34, 10, 10);
			}
			{
				scrollResultados = new JScrollPane();
				Resultados.add(scrollResultados);
				scrollResultados.setViewportView( tblResultados );
				scrollResultados.setBounds(12, 32, 601, 307);
			}
		}

		deshabilitarPanelParquimetros();
		deshabilitarPanelResultados();
	}
	
//Consultas y Comandos	
	
/****************************************************************************/
/* OPERACIONES ESPECÍFICAS QUE ESTABLECEN RELACIÓN CON InspectorGUI */
/****************************************************************************/
		
	/**
	 * Se encarga de cargar todos los datos de los parquímetros donde
	 * debe controlar los estacionamientos el inspector logueado.-
	 */
	private void generarDatosParquimetros(){
		try{
			
			manejador.cargarParquimetros( modelParquimetros );
			boxParquimetros.setSelectedIndex(-1);
		
		}catch(SQLException e){
			mostrarMensaje("Error N°: " + e.getErrorCode() + " - " + e.getMessage() , JOptionPane.ERROR_MESSAGE);
			mostrarMensaje("Error en la generación de los parquímetros.-", JOptionPane.INFORMATION_MESSAGE);
			deshabilitarPanelResultados();
			deshabilitarPanelParquimetros();
		}
	}
	
	/**
	 * Una vez cargados los datos de las patentes, y seleccionado el parquímetro al cual
	 * se conecta el dispositivo con el que se lleva a cabo el control del estacionamiento
	 * por el inspector, se ordena a InspectorGUI que notifique a la lógica que se debe ejecutar el
	 * labramiento de las multas; a la vez parametriza la tabla donde se deben mostrar los resultados.
	 * Como operación adicional, inspectorGUI debe encargarse de registrar el acceso al parquimetro
	 * por parte del inspector.-
	 */
	private void generarResultados(){
		//Variables locales.-
		String parq = (String) modelParquimetros.getSelectedItem();
		String dia = fechaField.getText().substring(0, 2);
		char turno = turnoField.getText().charAt(0);
		
		Date data;
		Time time;
		
		//Se crean los objetos Date y Time del tipo SQL para parametrizarlos ante la operación 
		//de generación de multas.-
		data = new Date( fechaHora.getTimeInMillis());
		time = new Time( fechaHora.getTimeInMillis());
	
		//Seteo los datos fecha y hora en el panel de estadísticas.-
		lblHora.setText("Hora: " + time.toString());
		lblFecha.setText("Fecha: " + data.toString());
					
		try{
				
			JTable auxiliar = manejador.generarMultas( legajo, parq , modelPatentes, data, time, dia, turno );
			tblResultados.setModel( auxiliar.getModel() );
			lblMultas.setText("Multas: "+tblResultados.getModel().getRowCount());
			
		}catch(SQLException e){
			mostrarMensaje("Error N°: " + e.getErrorCode() + " - " + e.getMessage() , JOptionPane.ERROR_MESSAGE);
			mostrarMensaje("Error en la generación de multas. No podemos asegurar la consistencia de la BD.-", JOptionPane.INFORMATION_MESSAGE);
			deshabilitarPanelResultados();
			deshabilitarPanelParquimetros();
			deshabilitarPanelPatentes();
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
			deshabilitarPanelResultados();
			deshabilitarPanelParquimetros();
			deshabilitarPanelPatentes();
		}
	}
	
	/**
	 * Controla si el parquímetro seleccionado es válido para el día, turno, e inspector 
	 * que está accediendo. Retorna el resultado del control.- 
	 * @return True si el parquímetro es válido, false en caso contrario.-
	 */
	private boolean esValidoParq(){
		//Variables locales.-
		boolean toReturn = false;
		
		String parq = (String) modelParquimetros.getSelectedItem();
		String dia = fechaField.getText();
		char turno = turnoField.getText().charAt(0);
		
		try{
			
			toReturn = manejador.controlarParquimetro( legajo, parq, turno, dia );
			
		}catch(SQLException e ){
			mostrarMensaje("Error N°: " + e.getErrorCode() + " - " + e.getMessage() , JOptionPane.ERROR_MESSAGE);
			mostrarMensaje("Error en la validación de parquímetro seleccionado. Reintente.-", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return toReturn;

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
	
/****************************************************************************/
/* OPERACIONES ESPECÍFICAS DEL PANEL */
/****************************************************************************/
	
	/**
	 * Controla si la pantente a ingresar ya no se encuentra cargada en la lista de
	 * patentes. Retorna el resultado del control.- 
	 * @return True si es patente válida, false en caso contrario.-
	 */
	private boolean esValidaPat(){
		return ( !modelPatentes.contains( patenteField.getText()));
	}
	
	/**
	 * Controla si la pantente a ingresar mantenga el formato necesario. Retorna el 
	 * resultado del control.- 
	 * @return True si es patente válida, false en caso contrario.-
	 */
	private boolean esValidaPat_2(){
		//Variables locales.-
		int i;
		boolean formato = true;
		String patente = patenteField.getText();
		
		for(i=0; formato && i<patente.length(); i++)
			formato = ( formato && patente.charAt(i)!='_');
		
		return ( formato );
	}
	
	/**
	 * Deshabilita el panel donde se registran las patentes estacionadas.-
	 */
	private void deshabilitarPanelPatentes(){
		int i;
		for(i=0; i<Patentes.getComponentCount(); i++)
			Patentes.getComponent(i).setEnabled(false);
		
		lblErrorPat.setVisible(false);
		lblFormato.setVisible(false);
	}
	
	/**
	 * Habilitar el panel donde se registran las patentes estacionadas.-
	 */
	private void habilitarPanelPatentes(){
		int i;
		for(i=0; i<Patentes.getComponentCount(); i++)
			Patentes.getComponent(i).setEnabled(true);
		
		lblErrorPat.setVisible(false);
		lblFormato.setVisible(false);
	}

	/**
	 * Deshabilita el panel donde se registran las patentes estacionadas.-
	 */
	private void deshabilitarPanelParquimetros(){
		int i;
		for(i=0; i<Parquimetro.getComponentCount(); i++)
			Parquimetro.getComponent(i).setEnabled(false);
	}
	
	/**
	 * Habilitar el panel donde se registran las el parquímetro a utilizar.-
	 */
	private void habilitarPanelParquimetros(){
		int i;
		for(i=0; i<Parquimetro.getComponentCount(); i++)
			Parquimetro.getComponent(i).setEnabled(true);
	}
	
	/**
	 * Habilita tanto los paneles de estadísticas y resultados que son donde se plasman
	 * los resultados de el accionar del inspector en el estacionamiento.-
	 */
	private void habilitarPanelResultados(){
		int i;
		for(i=0; i<Estadisticas.getComponentCount(); i++)
			Estadisticas.getComponent(i).setEnabled(true);
		
		for(i=0; i<Resultados.getComponentCount(); i++)
			Resultados.getComponent(i).setEnabled(true);
	}
	
	/**
	 * Deshabilita tanto los paneles de estadísticas y resultados que son donde se plasman
	 * los resultados de el accionar del inspector en el estacionamiento.-
	 */
	private void deshabilitarPanelResultados(){
		int i;
		for(i=0; i<Estadisticas.getComponentCount(); i++)
			Estadisticas.getComponent(i).setEnabled(false);
		
		for(i=0; i<Resultados.getComponentCount(); i++)
			Resultados.getComponent(i).setEnabled(false);
	}	
}
