package Administrador;
import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import quick.dbtable.DBTable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AdminManagmentPanel extends JPanel {

	private AdminGUI manejador;
	private JTextArea comandosSQL;
    private DBTable tabla;    
	private JList ListaTablas;
	private JList ListaAtributos;
	private DefaultListModel modeloTablas = new DefaultListModel();
	private DefaultListModel modeloAtributos = new DefaultListModel();

	
	public AdminManagmentPanel(AdminGUI manejador) 
	{
	    super();
		this.manejador= manejador;
		
		setSize(680, 550);
		
		setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panelSuperior = new JPanel();
		add(panelSuperior);
		panelSuperior.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelTablas = new JPanel();
		panelSuperior.add(panelTablas);
		panelTablas.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Tablas");
		panelTablas.add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel PanelListas = new JPanel();
		panelTablas.add(PanelListas, BorderLayout.CENTER);
		PanelListas.setLayout(new GridLayout(1, 0, 0, 0));
		actualizarTablas();
		
		JScrollPane scrollListaTablas = new JScrollPane();
		PanelListas.add(scrollListaTablas);
		
		ListaTablas = new JList(modeloTablas);
		ListaTablas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				actualizarAtributos();
			}
		});
		
		scrollListaTablas.setViewportView(ListaTablas);
		//PanelListas.add(ListaTablas);
		
		JScrollPane scrollListaAtributos = new JScrollPane();
		PanelListas.add(scrollListaAtributos);
		
		ListaAtributos = new JList(modeloAtributos);
		
		scrollListaAtributos.setViewportView(ListaAtributos);
		//PanelListas.add(ListaAtributos);
		
		JPanel panelComandos = new JPanel();
		panelSuperior.add(panelComandos);
		panelComandos.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollComandos = new JScrollPane();
		panelComandos.add(scrollComandos, BorderLayout.CENTER);
		
		comandosSQL = new JTextArea();
		comandosSQL.setText("Ingrese aqu\u00ED la sentencia SQL que desee.");
		scrollComandos.setViewportView(comandosSQL);
		
		JButton btnNewButton = new JButton("Ejecutar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refrescarTabla();
				actualizarTablas();
				actualizarAtributos();
			}
		});
		panelComandos.add(btnNewButton, BorderLayout.SOUTH);
		
		JPanel panelInferior = new JPanel();
		add(panelInferior);
		panelInferior.setLayout(new GridLayout(0, 1, 0, 0));
		
		tabla = new DBTable();
		panelInferior.add(tabla);
		tabla.setEditable(false); 
		
		try {
			manejador.logica.conectarDBTable(tabla, "admin", "admin");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Actualiza la lista de tablas.
	 */
	private void actualizarTablas(){
		//Limpiar lista
		modeloTablas.clear();
		for (String tab : manejador.logica.consultarTablas()) {
			modeloTablas.addElement(tab);
			//System.out.println(tab);
		}
		
	}
	
	/**
	 * Actualiza la lista de atributos.
	 */
	private void actualizarAtributos(){
		//Limpiar lista
		modeloAtributos.clear();
		
		if(ListaTablas.getSelectedIndex() != -1) {
			for (String at : manejador.logica.consultarAtributos( (String) modeloTablas.get(ListaTablas.getSelectedIndex()) )) {
				modeloAtributos.addElement(at);
				//System.out.println(at);
			}
		}	
	}
	
	private void refrescarTabla()
	   {
	      try
	      {    
	    	  Statement st = tabla.getConnection().createStatement();
	    	      	  
	    	  // actualizamos el contenido de la tabla. 
	    	  if(st.execute(this.comandosSQL.getText().trim()))
	  			tabla.refresh(st.getResultSet());
	    	  // No es necesario establecer  una conexión, crear una sentencia y recuperar el 
	    	  // resultado en un resultSet, esto lo hace automáticamente la tabla (DBTable) a 
	    	  // patir  de  la conexión y la consulta seteadas con connectDatabase() y setSelectSql() respectivamente.
	          
	    	// modificar la forma en que se muestran de algunas columnas  
	    	  for (int i = 0; i < tabla.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tabla.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		  tabla.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 // cambiar el formato en que se muestran los valores de tipo DATE
	    		 if	 (tabla.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tabla.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          } 
	    	  
	    	  
	       }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	      
	   }
	
	/**
	 * Le informa al manejador que ejecute la accion para terminar la sesión.
	 */
	private void logout(){
		try {
			manejador.logout();
		} catch (SQLException e) {
			String mensaje = "Mensaje: "+e.getMessage()+"\n";
			String codigo = "Error Code: "+String.valueOf(e.getErrorCode())+"\n";
			String estado = "Estado: "+ e.getSQLState()+"\n";
			
			//this.mostrarMensaje(mensaje+codigo+estado);
		}
	}
}
