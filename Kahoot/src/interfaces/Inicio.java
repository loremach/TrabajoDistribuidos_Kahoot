package Interfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import conexiones.Cliente2;

public class Inicio extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cliente2 cliente;
	private VentanaPrincipal ventanaPrincipal;
	
	/**
	 * Create the panel.
	 */
	public Inicio(Cliente2 cliente, VentanaPrincipal ventana) {
		this.cliente = cliente;
		this.ventanaPrincipal = ventana;
		
		this.setBounds(700, 80, 600, 800);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JButton btnCrearSala = new JButton("Crear sala");
		btnCrearSala.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		btnCrearSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idSala = cliente.crearSala(1);
				if(cliente.getLocalPort()!=0 && !idSala.equals("")) {
					cambiarVentanaHost(cliente);
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
				cambiarVentanaJugador(cliente);
			}
		});
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		GroupLayout gl_contentPane = new GroupLayout(this);
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
		
		this.setLayout(gl_contentPane);
	}
	
	private void cambiarVentanaHost(Cliente2 aux) {
		this.cliente = aux;
		ventanaPrincipal.cambiarAClienteHostConfig();
	}

	private void cambiarVentanaJugador(Cliente2 aux) {
		this.cliente = aux;
		ventanaPrincipal.cambiarAClienteEntrarSala();
	}
}
