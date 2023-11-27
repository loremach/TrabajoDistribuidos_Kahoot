package Interfaces;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import conexiones.Cliente2;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Color;

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private static Cliente2 cliente;

	public VentanaInicio(Cliente2 cliente) {
		
		this.cliente=cliente;
		setResizable(false);
		setTitle("Kahoot!");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 80, 600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JButton btnCrearSala = new JButton("Crear sala");
		btnCrearSala.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		btnCrearSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String idSala ="";
//				try(Socket cliente = new Socket("localhost", 8000);
//		                ObjectInputStream inSocket = new ObjectInputStream(cliente.getInputStream());
//		                DataOutputStream outSocket = new DataOutputStream(cliente.getOutputStream());){
//					outSocket.writeInt(1);
//	                outSocket.flush();
//	                
//					String salaCreada = inSocket.readLine();
//                    if(salaCreada.equals("Has creado la sala.")){
//                        localPort = cliente.getLocalPort();
//                        idSala = inSocket.readLine();
//
//                    }
//		        }catch(IOException ex){
//		            ex.printStackTrace();
//		        }
//				
//				if(localPort!=0 && !idSala.equals("")) {
//					VentanaClienteHost clienteHost = new VentanaClienteHost(idSala, localPort);
//					clienteHost.mostrarInterfaz();
//				}
				String idSala = cliente.crearSala();

				if(cliente.getLocalPort()!=0 && !idSala.equals("")) {
					VentanaClienteHost clienteHost = new VentanaClienteHost(cliente);
					clienteHost.mostrarInterfaz();
				}
			}
		});
		
		JLabel lblBienvenido = new JLabel("¡Bienvenido a Kahoot!");
		lblBienvenido.setForeground(new Color(0, 0, 160));
		lblBienvenido.setFont(new Font("Kristen ITC", Font.BOLD, 32));
		lblBienvenido.setToolTipText("");
		
		JLabel lblNewLabel_1 = new JLabel("¿Qué quieres hacer?");
		lblNewLabel_1.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		
		JButton btnUnirse = new JButton("Unirse a una sala");
		btnUnirse.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		btnUnirse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(107, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnCrearSala, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1)
								.addComponent(btnUnirse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnSalir, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE))
							.addGap(173))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblBienvenido, GroupLayout.PREFERRED_SIZE, 372, GroupLayout.PREFERRED_SIZE)
							.addGap(95))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(101)
					.addComponent(lblBienvenido, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(76)
					.addComponent(lblNewLabel_1)
					.addGap(84)
					.addComponent(btnCrearSala, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGap(52)
					.addComponent(btnUnirse, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addGap(57)
					.addComponent(btnSalir, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(132, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void mostrarInterfaz(){
        this.setVisible(true);
    }
}
