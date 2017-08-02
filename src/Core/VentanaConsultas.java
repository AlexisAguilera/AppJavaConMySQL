package Core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import quick.dbtable.*;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.GridLayout;

import javax.swing.JLabel;  

import Administrador.AdminGUI;


@SuppressWarnings("serial")
public class VentanaConsultas extends JPanel
{
	
	private AdminGUI manejador;
	
   private JPanel pnlConsulta;
   private JTextArea txtConsulta;
   private JButton botonBorrar;
   private JButton btnEjecutar;
   private DBTable tabla;    
   private JScrollPane scrConsulta;
   private JPanel panel;
   private JPanel panel_1;
   private JPanel panel_2;
   private JList tablesList;
   private JList attributesList;
   private DefaultListModel tablesModel = new DefaultListModel();
   private DefaultListModel atributtesModel = new DefaultListModel();

   protected Connection conexionBD = null;
   private JPanel panel_3;
   private JLabel label;

   
   public VentanaConsultas(AdminGUI manejador) 
   {
      super();
		this.manejador= manejador;
      initGUI();
   }
   
   private void initGUI() 
   {
      try {
         setPreferredSize(new Dimension(800, 600));
         this.setBounds(0, 0, 800, 600);
         setVisible(true);
         BorderLayout thisLayout = new BorderLayout();
         //this.setTitle("Consultas a la Base de Datos");
         //getContentPane().setLayout(thisLayout);
         //this.setClosable(true);
         //this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
         //this.setMaximizable(true);
         this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               thisComponentHidden(evt);
            }
            public void componentShown(ComponentEvent evt) {
               thisComponentShown(evt);
            }
         });
         setLayout(new BorderLayout(0, 0));
         {
         	panel_3 = new JPanel();
         	add(panel_3);
         }
         {
            pnlConsulta = new JPanel();
            add(pnlConsulta);
            {
               scrConsulta = new JScrollPane();
               pnlConsulta.add(scrConsulta);
               {
                  txtConsulta = new JTextArea();
                  scrConsulta.setViewportView(txtConsulta);
                  txtConsulta.setTabSize(3);
                  txtConsulta.setColumns(80);
                  txtConsulta.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                  //txtConsulta.setText("SELECT t.fecha, t.nombre_batalla, b.nombre_barco, b.id, b.capitan, r.resultado \r\nFROM batallas t, resultados r, barcos b \r\nWHERE t.nombre_batalla = r.nombre_batalla \r\nAND r.nombre_barco = b.nombre_barco \r\nORDER BY t.fecha, t.nombre_batalla, b.nombre_barco");
                  txtConsulta.setFont(new java.awt.Font("Monospaced",0,12));
                  txtConsulta.setRows(10);
               }
            }
            {
            	panel = new JPanel();
            	pnlConsulta.add(panel);
            	panel.setLayout(new BorderLayout(0, 0));
            	{
            		panel_1 = new JPanel();
            		panel.add(panel_1, BorderLayout.NORTH);
            		{
            			
            			tablesList = new JList(tablesModel);
            			tablesList.addListSelectionListener(new ListSelectionListener() {
            				public void valueChanged(ListSelectionEvent arg0) {
            					refrescarAtributosTabla();
            				}
            			});
            			panel_1.add(tablesList);
            		}
            		{
            			attributesList = new JList(atributtesModel);
            			panel_1.add(attributesList);
            		}
            	}
            	{
            		panel_2 = new JPanel();
            		panel.add(panel_2, BorderLayout.CENTER);
            		btnEjecutar = new JButton();
            		panel_2.add(btnEjecutar);
            		btnEjecutar.setText("Ejecutar");
            		{
            			botonBorrar = new JButton();
            			panel_2.add(botonBorrar);
            			botonBorrar.setText("Borrar");            
            			botonBorrar.addActionListener(new ActionListener() {
            				public void actionPerformed(ActionEvent arg0) {
            				  txtConsulta.setText("");            			
            				}
            			});
            		}	
            		btnEjecutar.addActionListener(new ActionListener() {
            		   public void actionPerformed(ActionEvent evt) {
            		      btnEjecutarActionPerformed(evt);
            		   }
            		});
            	}
            	{
            	}
            }
         }
         {
        	// crea la tabla  
        	tabla = new DBTable();
        	
        	// Agrega la tabla al frame
            add(tabla, BorderLayout.SOUTH);           
                      
           // setea la tabla para sólo lectura (no se puede editar su contenido)  
           tabla.setEditable(false);       
           
           
           
         }
         {
         	label = new JLabel("");
         	add(label);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void thisComponentShown(ComponentEvent evt) 
   {
      this.conectarBD();
      this.refrescarListaTabla();
   }
   
   private void thisComponentHidden(ComponentEvent evt) 
   {
      this.desconectarBD();
   }

   private void btnEjecutarActionPerformed(ActionEvent evt) 
   {
      this.refrescarTabla();      
   }
   
   private void conectarBD()
   {
	   if (this.conexionBD == null)
	   {
		   try
	       {  // Se carga y registra el driver JDBC de MySQL
	          Class.forName("com.mysql.jdbc.Driver").newInstance();
	       }
	       catch (Exception ex)
	       {  
	          System.out.println(ex.getMessage());
	       }

	   
         try
         {
            String driver ="com.mysql.jdbc.Driver";
        	String servidor = "localhost:3306";
            String baseDatos = "parquimetros";
            String usuario = "admin";
            String clave = "admin";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //se intenta establecer la conección
            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
            
            //establece una conexión con la  B.D. "batallas"  usando directamante una tabla DBTable    
            tabla.connectDatabase(driver, uriConexion, usuario, clave);
           
         }
         catch (SQLException ex)
         {
             JOptionPane.showMessageDialog(this,
                                           "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
                                           "Error",
                                           JOptionPane.ERROR_MESSAGE);
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
         }
         catch (ClassNotFoundException e)
         {
            e.printStackTrace();
         }
	   }
   }

   private void desconectarBD()
   {
	   if (this.conexionBD != null)
	      {
	         try
	         {
	        	tabla.close(); 
	            this.conexionBD.close();
	            this.conexionBD = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	      }  
   }
   
   private void refrescarAtributosTabla()
   {
	   try
	      {
		   //Limpiar lista de atributos
		   atributtesModel.clear();
		   
		   // se crea una sentencia o comando jdbc para realizar la consulta 
	  	 	// a partir de la coneccion establecida (conexionBD)
	       Statement stmt = this.conexionBD.createStatement();
		   if(tablesList.getSelectedIndex() != -1) {
	
		         ResultSet rs = stmt.executeQuery("describe "+tablesModel.get(tablesList.getSelectedIndex()));//md.getTables(null, null, "%", null);
		         while (rs.next()) {
		        	 atributtesModel.addElement(rs.getString(1));
		         }
	       }
	      }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      }  
   }

   private void refrescarListaTabla()
   {
	   try
	      {
	         // se crea una sentencia o comando jdbc para realizar la consulta 
	    	 // a partir de la coneccion establecida (conexionBD)
	         Statement stmt = this.conexionBD.createStatement();
	         /*
	         // se prepara el string SQL de la consulta
	         String sql = "SHOW TABLES " + 
	                      "FROM parquimetros";

	         // se ejecuta la sentencia y se recibe un resultset
	         ResultSet rs = stmt.executeQuery(sql);
	         // se recorre el resulset y se actualiza la tabla en pantalla
	         //((DefaultTableModel) this.tabla.getModel()).setRowCount(0);
	         int i = 0;
	         while (rs.next())
	         {
	        	 // agrega una fila al modelo de la tabla
	            //((DefaultTableModel) this.tabla.getModel()).setRowCount(i + 1);
	            // se agregan a la tabla los datos correspondientes cada celda de la fila recuperada
	            this.tabla.setValueAt(rs.getString("nombre_barco"), i, 0);
	            this.tabla.setValueAt(rs.getInt("id"), i, 1);            
	            this.tabla.setValueAt(rs.getString("capitan"), i, 2);
	            i++;
	         }
	         */
	         
	         DatabaseMetaData md = this.conexionBD.getMetaData();
	         ResultSet rs = stmt.executeQuery("show tables from parquimetros");//md.getTables(null, null, "%", null);
	         while (rs.next()) {
	        	 tablesModel.addElement(rs.getString(1));
	         }
	         
	         // se cierran los recursos utilizados 
	         rs.close();
	         stmt.close();
	         
	      }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      }   
   }
   
   private void refrescarTabla()
   {
      try
      {    
    	  // seteamos la consulta a partir de la cual se obtendrán los datos para llenar la tabla
    	  tabla.setSelectSql(this.txtConsulta.getText().trim());

    	  // obtenemos el modelo de la tabla a partir de la consulta para 
    	  // modificar la forma en que se muestran de algunas columnas  
    	  tabla.createColumnModelFromQuery();    	    
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
    	  // actualizamos el contenido de la tabla.   	     	  
    	  tabla.refresh();
    	  // No es necesario establecer  una conexión, crear una sentencia y recuperar el 
    	  // resultado en un resultSet, esto lo hace automáticamente la tabla (DBTable) a 
    	  // patir  de  la conexión y la consulta seteadas con connectDatabase() y setSelectSql() respectivamente.
          
    	  
    	  
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

   
}
