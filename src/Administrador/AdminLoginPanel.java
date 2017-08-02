package Administrador;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/**
 * Panel de login del administrador.
 * Es uno de los paneles de AdminGUI.
 * @see AdminGUI
 */
public class AdminLoginPanel extends JPanel {
	

//Atributos

	private AdminGUI manejador;
	private JPasswordField passwordField;
	
//Constructores
	
	/**
	 * Constructor de la clase.
	 * Inicializa los atributos y crea todos los componentes del panel.
	 * @param manejador El objeto encargado de la visualización de las diferentes pantallas.
	 */
	public AdminLoginPanel(AdminGUI manejador){
		super();
		this.manejador=manejador;
		
		setLayout(new BorderLayout(0, 0));
		setSize(200, 150);
		
		JPanel north = new JPanel();
		north.setBorder(null);
		add(north, BorderLayout.NORTH);
		
		JLabel titulo = new JLabel("Admin Login");
		titulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		north.add(titulo);
		
		JPanel center = new JPanel();
		center.setBorder(null);
		add(center, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Ingrese contrase\u00F1a:");
		center.add(lblNewLabel);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(16);
		//passwordField.setToolTipText("Ingrese la contrase\u00F1a");
		center.add(passwordField);
		
		JPanel south = new JPanel();
		south.setBorder(null);
		add(south, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviarPSW();
			}
		});
		south.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrar();
			}
		});
		south.add(btnNewButton_1);
		
		
	}
	
//Consultas y Comandos
	
	/**
	 * Captura la contraseña ingresada y se la envia al manejador.
	 */
	private void enviarPSW(){
		try {
			manejador.login(String.valueOf(passwordField.getPassword()));
		} catch (SQLException e) {
			mostrarError(e.getMessage(), String.valueOf(e.getErrorCode()));
		}
	}
	
	/**
	 * Lanza un cartel de error avisando que la contraseña ingresada era incorrecta.
	 */
	public void mostrarError(String mensaje, String codigo){
		JOptionPane.showMessageDialog(this, mensaje, "ErrorSQL Codigo: "+codigo, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Envía el mensaje al manejador para que cierre la ventana actual.-
	 */
	private void cerrar(){
		manejador.finalizar();
	}
}
