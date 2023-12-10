package Interfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import conexiones.clasesClienteGrafico.Cliente2;
import dominio.Persona;

public class ClienteEntrarSala extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textIdSala;
	private JTextField textNombreJugador;
	private Cliente2 cliente;
	private VentanaPrincipal ventanaPrincipal;
	/**
	 * Create the panel.
	 */
	public ClienteEntrarSala(Cliente2 cliente, VentanaPrincipal ventana) {
		this.cliente = cliente;
		this.ventanaPrincipal = ventana;
		
		this.setBounds(700, 80, 600, 800);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel panelKahoot = new JPanel();
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JLabel lblNombre = new JLabel("Introduce tu nombre:");
		lblNombre.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		
		textNombreJugador = new JTextField();
		textNombreJugador.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textNombreJugador.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(151)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(textNombreJugador, Alignment.LEADING)
						.addComponent(lblNombre, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
					.addContainerGap(156, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNombre)
					.addGap(34)
					.addComponent(textNombreJugador, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panelGestionarEntrada = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(this);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panelKahoot, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 570, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(2)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panelGestionarEntrada, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
								.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
								.addComponent(panel_1, 0, 0, Short.MAX_VALUE))))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panelKahoot, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelGestionarEntrada, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
		);
		
		JLabel lblNewLabel_1 = new JLabel("Kahoot!");
		lblNewLabel_1.setForeground(new Color(0, 0, 128));
		lblNewLabel_1.setFont(new Font("Kristen ITC", Font.BOLD, 42));
		GroupLayout gl_panelKahoot = new GroupLayout(panelKahoot);
		gl_panelKahoot.setHorizontalGroup(
			gl_panelKahoot.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelKahoot.createSequentialGroup()
					.addGap(184)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(188, Short.MAX_VALUE))
		);
		gl_panelKahoot.setVerticalGroup(
			gl_panelKahoot.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelKahoot.createSequentialGroup()
					.addContainerGap(65, Short.MAX_VALUE)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addGap(57))
		);
		panelKahoot.setLayout(gl_panelKahoot);
		
		JLabel lblInformarError = new JLabel("");
		lblInformarError.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformarError.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		
		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textNombreJugador.getText()!="" || textIdSala.getText()!="") {
					String idSala = textIdSala.getText();
					String alias = textNombreJugador.getText();
					ArrayList<Persona> personas = cliente.conectarConSala(2, idSala, alias);

					if(personas!=null){
						lblInformarError.setText("");
		                Persona personaHost = personas.get(0);
						Persona personaJugador = personas.get(1);
		                
						cambiarVentana(cliente, personaHost, personaJugador);
		            }else{
		            	lblInformarError.setText("La sala no existe");
		            }
				}else {
					lblInformarError.setText("¡Rellena todos los campos!");
				}
			}
		});
		btnNewButton.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		
		GroupLayout gl_panelGestionarEntrada = new GroupLayout(panelGestionarEntrada);
		gl_panelGestionarEntrada.setHorizontalGroup(
			gl_panelGestionarEntrada.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGestionarEntrada.createSequentialGroup()
					.addContainerGap(102, Short.MAX_VALUE)
					.addComponent(lblInformarError, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE)
					.addGap(95))
				.addGroup(gl_panelGestionarEntrada.createSequentialGroup()
					.addGap(195)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(197, Short.MAX_VALUE))
		);
		gl_panelGestionarEntrada.setVerticalGroup(
			gl_panelGestionarEntrada.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelGestionarEntrada.createSequentialGroup()
					.addGap(64)
					.addComponent(lblInformarError, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGap(80)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(132, Short.MAX_VALUE))
		);
		panelGestionarEntrada.setLayout(gl_panelGestionarEntrada);
		
		JLabel lblPedirID = new JLabel("Introduce el ID de la sala:");
		lblPedirID.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		
		textIdSala = new JTextField();
		textIdSala.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textIdSala.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(151)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(textIdSala, Alignment.LEADING)
						.addComponent(lblPedirID, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(156, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPedirID)
					.addGap(34)
					.addComponent(textIdSala, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		this.setLayout(gl_contentPane);
	}

	public void cambiarVentana(Cliente2 aux, Persona personaHost, Persona personaJugador) {
		this.cliente = aux;
		ventanaPrincipal.cambiarAClienteJugador(personaHost, personaJugador);
	}

}
