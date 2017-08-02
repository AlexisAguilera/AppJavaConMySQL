package Parquimetros;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;


public class ParquimetrosPanel extends JPanel {

//Atributos

	private ParquimetrosGUI manejador;
	private JComboBox choiceCospel, choiceUbic, choiceAltura, choiceParq;
	private JButton btnConectar, btnAtras;
	private JTextPane output;
	private char estado;
	private boolean init;
	private JButton btnOkC;
	private JButton btnOkU;
	private JButton btnOkA;
	private JButton btnOkP;
	
//Constructores
	
	public ParquimetrosPanel(ParquimetrosGUI manejador){
		super();
		this.manejador=manejador;
		estado='I';
		init =true;
		
		setSize(461,330);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new MigLayout("", "[269.00,grow][grow]", "[208.00,grow,baseline][32.00,grow,baseline]"));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 0 1 2,grow");
		
		output = new JTextPane();
		output.setForeground(Color.GREEN);
		output.setEditable(false);
		scrollPane.setViewportView(output);
		output.setBackground(Color.BLACK);
		output.setFont(new Font("Tahoma", Font.BOLD, 12));
		output.setText("SELECCIONAR COSPEL.");
		
		JPanel selectPane = new JPanel();
		selectPane.setBackground(Color.GRAY);
		add(selectPane, "cell 1 0,grow");
		selectPane.setLayout(new MigLayout("", "[70.00,grow][grow]", "[][][][18.00][][][][][][]"));
		
		btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				atras();
			}
		});
		btnAtras.setEnabled(false);
		selectPane.add(btnAtras, "cell 0 0,alignx left");
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		selectPane.add(btnSalir, "cell 1 0,alignx right");
		
		JLabel lblCospel = new JLabel("Cospel:");
		selectPane.add(lblCospel, "cell 0 2,alignx left");
		
		
		choiceCospel = new JComboBox();
		selectPane.add(choiceCospel, "cell 0 3,growx");
		
		btnOkC = new JButton("OK");
		btnOkC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOkC.setEnabled(false);
				choiceCospel.setEnabled(false);
				btnAtras.setEnabled(true);
				choiceUbic.setEnabled(true);
				btnOkU.setEnabled(true);
				mostrarMensaje("SELECCIONAR UBICACÍON.");
				estado='U';
			}
		});
		selectPane.add(btnOkC, "cell 1 3,alignx right");
		
		JLabel lblUbicacin = new JLabel("Ubicaci\u00F3n:");
		selectPane.add(lblUbicacin, "cell 0 4,alignx left");
		
		choiceUbic = new JComboBox();
		choiceUbic.setEnabled(false);
		selectPane.add(choiceUbic, "cell 0 5,growx");
		
		btnOkU = new JButton("OK");
		btnOkU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choiceAltura.setEnabled(true);
				btnOkA.setEnabled(true);
				choiceUbic.setEnabled(false);
				btnOkU.setEnabled(false);
				mostrarMensaje("SELECCIONAR ALTURA.");
				estado='A';
				cargarAlturas((String) choiceUbic.getSelectedItem());
			}
		});
		btnOkU.setEnabled(false);
		selectPane.add(btnOkU, "cell 1 5,alignx right");
		
		JLabel lblAltura = new JLabel("Altura:");
		selectPane.add(lblAltura, "cell 0 6,alignx left");
		
		choiceAltura = new JComboBox();
		choiceAltura.setEnabled(false);
		selectPane.add(choiceAltura, "cell 0 7,growx");
		
		btnOkA = new JButton("OK");
		btnOkA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				choiceParq.setEnabled(true);
				btnOkP.setEnabled(true);
				choiceAltura.setEnabled(false);
				btnOkA.setEnabled(false);
				mostrarMensaje("SELECCIONAR PARQUÍMETRO.");
				estado='P';
				cargarParq((String)choiceUbic.getSelectedItem(), (String)choiceAltura.getSelectedItem());
			}
		});
		btnOkA.setEnabled(false);
		selectPane.add(btnOkA, "cell 1 7,alignx right");
		
		JLabel lblIdParquimetro = new JLabel("Id. Parquimetro:");
		selectPane.add(lblIdParquimetro, "cell 0 8,alignx left");
		
		choiceParq = new JComboBox();
		choiceParq.setEnabled(false);
		selectPane.add(choiceParq, "cell 0 9,growx");
		
		btnOkP = new JButton("OK");
		btnOkP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConectar.setEnabled(true);
				btnOkP.setEnabled(false);
				choiceParq.setEnabled(false);
				mostrarMensaje("LISTO PARA CONECTAR.");
				estado='C';
			}
		});
		btnOkP.setEnabled(false);
		selectPane.add(btnOkP, "cell 1 9,alignx right");
		
		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectar((String)choiceCospel.getSelectedItem(), (String)choiceParq.getSelectedItem());
				btnConectar.setEnabled(false);
			}
		});
		btnConectar.setBackground(Color.GRAY);
		add(btnConectar, "cell 1 1,grow");
		btnConectar.setEnabled(false);
		
		cargarDatos();
		this.setVisible(true);
	}
	
	
//Consultas y Comandos

	/**
	 * Muestra un mensaje por la pantalla del parquimetro.
	 * @param msj El mensaje que se desea mostrar.
	 */
	private void mostrarMensaje(String msj){
		output.setText(msj);
	}
	
	/**
	 * Carga los cospeles y las ubicaciones de la base de datos dentro de los seleccionadores.
	 */
	private void cargarDatos(){
		try{
			//Carga cospeles.
			for(String id : manejador.getCospeles()){
				choiceCospel.addItem(id);
			}
		
			//Carga ubicaciones.
			for(String ubic : manejador.getUbicaciones()){
				choiceUbic.addItem(ubic);
			}
		}catch(SQLException e){
			mostrarMensaje("ERROR CON LA BASE DE DATOS.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Carga todas las alturas disponibles de la ubicación seleccionada.
	 * @param ubic La ubicación seleccionada por el usuario.
	 */
	private void cargarAlturas(String ubic){
		try{
			for(String alt : manejador.getAlturas(ubic)){
				choiceAltura.addItem(alt);
			}
		}catch(SQLException e){
			mostrarMensaje("ERROR CON LA BASE DE DATOS.");
		}
	}
	
	/**
	 * Carga todos los parquimetros dentro de la ubicación y altura selecionadas.
	 * @param ubic La ubicación seleccionada por el usuario.
	 * @param alt La altura seleccionada por el usuario.
	 */
	private void cargarParq(String ubic, String alt){
		try{
			for(String id : manejador.getParquimetros(ubic, Integer.valueOf(alt))){
				choiceParq.addItem(id);
			}
		}catch(SQLException e){
			mostrarMensaje("ERROR CON LA BASE DE DATOS.");
		}
	}
	
	/**
	 * Simula la conexión de un cospel con un parquimetro.
	 * @param cosp El cospel seleccionado por el usuario.
	 * @param parq El parquimetro seleccionado por el usuario.
	 */
	private void conectar(String cosp, String parq){
		try{
			
			Vector<String> resultados=manejador.conectar(Long.parseLong(cosp), Long.parseLong(parq));
		
			//Operación de apertura.
			if(resultados.get(0).equals("apertura")){
				if(resultados.get(1).equals("0")){
					mostrarMensaje("UD. NO POSEE SALDO EN SU COSPEL.");
				}
				else{
					mostrarMensaje("PARQUIMETRO ABIERTO.\n"+
							   	   "TIEMPO DISPONIBLE: "+ resultados.get(1)+".\n");
				}
			}
			//Operación de cierre.
			else{
				mostrarMensaje("PARQUIMETRO CERRADO.\n"+
						   	   "TIEMPO TRANSCURRIDO [min]: "+resultados.get(1)+".\n"+
						   	   "SALDO DISPONIBLE: "+ resultados.get(2)+".");
			}
		}catch(SQLException e){
			mostrarMensaje("ERROR CON LA BASE DE DATOS.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Vuelve al estado anterior al que se encuentra actualmente la interfaz.
	 * ESTADOS:
	 * I - Estado inicial.
	 * U - Seleccion de ubicacion.
	 * A - Seleccion de altura.
	 * P - Seleccion de parquimetro.
	 * C - Conexión para abrir estacionamiento.
	 */
	private void atras(){
		
		switch (estado){
			//Etapa de Ubicacion.
			case 'U' :{
				btnOkC.setEnabled(true);
				btnOkU.setEnabled(false);
				choiceCospel.setEnabled(true);
				choiceUbic.setEnabled(false);
				btnAtras.setEnabled(false);
				mostrarMensaje("SELECCIONAR COSPEL.");
				estado='I';
				break;
			}
			//Etapa de Altura.
			case 'A' :{
				btnOkU.setEnabled(true);
				btnOkA.setEnabled(false);
				choiceUbic.setEnabled(true);
				choiceAltura.setEnabled(false);
				mostrarMensaje("SELECCIONAR UBICACÍON.");
				estado='U';
				choiceAltura.removeAllItems();
				break;
			}
			//Etapa de Parquimetro.
			case 'P' :{
				btnOkA.setEnabled(true);
				btnOkP.setEnabled(false);
				choiceAltura.setEnabled(true);
				choiceParq.setEnabled(false);
				mostrarMensaje("SELECCIONAR ALTURA.");
				estado='A';
				choiceParq.removeAllItems();
				break;
			}
			//Etapa de Conexion.
			case 'C' :{
				btnOkP.setEnabled(true);
				choiceParq.setEnabled(true);
				mostrarMensaje("SELECCIONAR PARQUÍMETRO.");
				estado='P';
				break;
			}
			
		}
	}
	
	/**
	 * Termina con la ejecución del programa.
	 */
	private void salir(){
		try {
			manejador.salir();
		} catch (SQLException e) {
			mostrarMensaje("Error durante la desconeccion.");
		}
	}
	
	
}
