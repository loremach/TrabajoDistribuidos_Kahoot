package interfaces;

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

import conexiones.clasesClienteGrafico.Cliente2;
import dominio.Persona;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClienteJugador extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cliente2 cliente;
	private Persona personaJugador;
	private Persona personaHost;
	private VentanaPrincipal ventanaPrincipal;

	/**
	 * Create the panel.
	 */
	public ClienteJugador(Cliente2 cliente, VentanaPrincipal ventana, Persona personaHost, Persona personaJugador) {
		this.cliente = cliente;
		this.personaHost = personaHost;
		this.personaJugador = personaJugador;
		this.ventanaPrincipal = ventana;
		
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
		
		JButton btnRespuestaA = new JButton("");
		btnRespuestaA.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		btnRespuestaA.setEnabled(false);
		btnRespuestaA.setBackground(new Color(41, 167, 241));
		btnRespuestaA.setForeground(new Color(0, 0, 0));
		PanelBotonesRespuesta.add(btnRespuestaA);
		
		JButton btnRespuestaB = new JButton("");
		btnRespuestaB.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		btnRespuestaB.setEnabled(false);
		btnRespuestaB.setBackground(new Color(255, 0, 0));
		PanelBotonesRespuesta.add(btnRespuestaB);
		
		JButton btnRespuestaC = new JButton("");
		btnRespuestaC.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		btnRespuestaC.setEnabled(false);
		btnRespuestaC.setBackground(new Color(255, 255, 0));
		PanelBotonesRespuesta.add(btnRespuestaC);
		
		JButton btnRespuestaD = new JButton("");
		btnRespuestaD.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		btnRespuestaD.setEnabled(false);
		btnRespuestaD.setBackground(new Color(0, 187, 94));
		PanelBotonesRespuesta.add(btnRespuestaD);
		
		//Listeners
		btnRespuestaA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.setRespuestaJugador(1);
				btnRespuestaA.setBackground(new Color(43, 139, 189));

				btnRespuestaA.setEnabled(false);
				btnRespuestaB.setEnabled(false);
				btnRespuestaC.setEnabled(false);
				btnRespuestaD.setEnabled(false);
			}
		});
		btnRespuestaB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.setRespuestaJugador(2);
				btnRespuestaB.setBackground(new Color(197, 0, 0));

				btnRespuestaA.setEnabled(false);
				btnRespuestaB.setEnabled(false);
				btnRespuestaC.setEnabled(false);
				btnRespuestaD.setEnabled(false);
			}
		});
		btnRespuestaC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.setRespuestaJugador(3);
				btnRespuestaC.setBackground(new Color(255, 213, 0));

				btnRespuestaA.setEnabled(false);
				btnRespuestaB.setEnabled(false);
				btnRespuestaC.setEnabled(false);
				btnRespuestaD.setEnabled(false);
			}
		});
		btnRespuestaD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.setRespuestaJugador(4);
				btnRespuestaD.setBackground(new Color(0, 147, 34));

				btnRespuestaA.setEnabled(false);
				btnRespuestaB.setEnabled(false);
				btnRespuestaC.setEnabled(false);
				btnRespuestaD.setEnabled(false);
			}
		});

		JLabel lblKahoot = new JLabel("Kahoot!");
		lblKahoot.setForeground(new Color(0, 0, 128));
		lblKahoot.setFont(new Font("Kristen ITC", Font.BOLD, 42));
		GroupLayout gl_panelKahoot = new GroupLayout(panelKahoot);
		gl_panelKahoot.setHorizontalGroup(
			gl_panelKahoot.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelKahoot.createSequentialGroup()
					.addGap(209)
					.addComponent(lblKahoot)
					.addContainerGap(213, Short.MAX_VALUE))
		);
		gl_panelKahoot.setVerticalGroup(
			gl_panelKahoot.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelKahoot.createSequentialGroup()
					.addGap(38)
					.addComponent(lblKahoot)
					.addContainerGap(41, Short.MAX_VALUE))
		);
		panelKahoot.setLayout(gl_panelKahoot);
		
		JTextPane textPregunta = new JTextPane();
		textPregunta.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textPregunta.setBackground(SystemColor.menu);
		
		JLabel lblPuntos = new JLabel("Puntos: 0");
		lblPuntos.setFont(new Font("Kristen ITC", Font.PLAIN, 20));
		
		JLabel lblTiempo = new JLabel("20");
		lblTiempo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTiempo.setForeground(new Color(255, 0, 0));
		lblTiempo.setFont(new Font("Kristen ITC", Font.BOLD, 26));
		GroupLayout gl_panelPreguntaPuntos = new GroupLayout(panelPreguntaPuntos);
		gl_panelPreguntaPuntos.setHorizontalGroup(
			gl_panelPreguntaPuntos.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelPreguntaPuntos.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelPreguntaPuntos.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelPreguntaPuntos.createSequentialGroup()
							.addComponent(textPregunta, GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panelPreguntaPuntos.createSequentialGroup()
							.addComponent(lblPuntos, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
							.addGap(128)
							.addComponent(lblTiempo, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addGap(43))))
		);
		gl_panelPreguntaPuntos.setVerticalGroup(
			gl_panelPreguntaPuntos.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelPreguntaPuntos.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelPreguntaPuntos.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTiempo, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPuntos, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textPregunta, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panelPreguntaPuntos.setLayout(gl_panelPreguntaPuntos);
		this.setLayout(gl_contentPane);

		Thread jugar = new Thread(new Runnable(){
			public void run(){
				cliente.jugarEnSala(personaHost, personaJugador, panelPreguntaPuntos, PanelBotonesRespuesta);
			}
		});

		jugar.start();
	}
}
