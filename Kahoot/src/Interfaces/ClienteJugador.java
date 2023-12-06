package Interfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import conexiones.Cliente2;
import dominio.Persona;

public class ClienteJugador extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cliente2 cliente;
	private Persona persona;
	private VentanaPrincipal ventanaPrincipal;

	/**
	 * Create the panel.
	 */
	public ClienteJugador(Cliente2 cliente, Persona persona, VentanaPrincipal ventana) {
		this.cliente = cliente;
		this.persona = persona;
		this.ventanaPrincipal = ventana;
		this.cliente.jugarEnSala(persona);
		
		this.setBounds(700, 80, 600, 800);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel panelKahoot = new JPanel();
		
		JPanel panelPreguntaPuntos = new JPanel();
		
		JPanel PanelBotonesRespuesta = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(this);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panelKahoot, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addComponent(panelPreguntaPuntos, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addComponent(PanelBotonesRespuesta, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panelKahoot, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelPreguntaPuntos, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(PanelBotonesRespuesta, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))
		);
		PanelBotonesRespuesta.setLayout(new GridLayout(2, 2, 0, 0));
		
		JButton btnRespuestaA = new JButton("New button");
		btnRespuestaA.setBackground(new Color(41, 167, 241));
		btnRespuestaA.setForeground(new Color(0, 0, 0));
		PanelBotonesRespuesta.add(btnRespuestaA);
		
		JButton btnRespuestaB = new JButton("New button");
		btnRespuestaB.setBackground(new Color(255, 0, 0));
		PanelBotonesRespuesta.add(btnRespuestaB);
		
		JButton btnRespuestaC = new JButton("New button");
		btnRespuestaC.setBackground(new Color(255, 255, 0));
		PanelBotonesRespuesta.add(btnRespuestaC);
		
		JButton btnRespuestaD = new JButton("New button");
		btnRespuestaD.setBackground(new Color(0, 187, 94));
		PanelBotonesRespuesta.add(btnRespuestaD);
		
		JLabel lblKahoot = new JLabel("Kahoot!");
		lblKahoot.setForeground(new Color(0, 0, 160));
		lblKahoot.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		GroupLayout gl_panelKahoot = new GroupLayout(panelKahoot);
		gl_panelKahoot.setHorizontalGroup(
			gl_panelKahoot.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelKahoot.createSequentialGroup()
					.addGap(202)
					.addComponent(lblKahoot)
					.addContainerGap(204, Short.MAX_VALUE))
		);
		gl_panelKahoot.setVerticalGroup(
			gl_panelKahoot.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelKahoot.createSequentialGroup()
					.addGap(38)
					.addComponent(lblKahoot)
					.addContainerGap(41, Short.MAX_VALUE))
		);
		panelKahoot.setLayout(gl_panelKahoot);
		
		JTextPane textPane = new JTextPane();
		GroupLayout gl_panelPreguntaPuntos = new GroupLayout(panelPreguntaPuntos);
		gl_panelPreguntaPuntos.setHorizontalGroup(
			gl_panelPreguntaPuntos.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPreguntaPuntos.createSequentialGroup()
					.addContainerGap()
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panelPreguntaPuntos.setVerticalGroup(
			gl_panelPreguntaPuntos.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPreguntaPuntos.createSequentialGroup()
					.addGap(5)
					.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
					.addContainerGap())
		);
		panelPreguntaPuntos.setLayout(gl_panelPreguntaPuntos);
		this.setLayout(gl_contentPane);
	}

}