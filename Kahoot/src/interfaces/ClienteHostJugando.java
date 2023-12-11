package interfaces;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import conexiones.clasesClienteGrafico.Cliente2;
import conexiones.clasesClienteGrafico.Empezar2;
import dominio.Persona;

public class ClienteHostJugando extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cliente2 cliente;
	private VentanaPrincipal ventanaPrincipal;
	private HashMap<Persona, Integer> tablaPuntuaciones;


	/**
	 * Create the panel.
	 */
	public ClienteHostJugando(Cliente2 cliente, VentanaPrincipal ventana) {
		this.cliente = cliente;
		this.ventanaPrincipal = ventana;
		

		this.setBounds(700, 80, 600, 800);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setVisible(true);
		
		JPanel panel = new JPanel();

		JPanel panel_1 = new JPanel();

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		
		JPanel panel_3 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(this);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 464, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
		);
		
		JLabel lblID = new JLabel("ID de la sala: "+cliente.getSala().getIdSala());
		lblID.setFont(new Font("Courier New", Font.BOLD, 24));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
					.addContainerGap(123, Short.MAX_VALUE)
					.addComponent(lblID)
					.addGap(121))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblID)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);

		JLabel lblJugadoresConectados = new JLabel("Jugadores conectados:");
		lblJugadoresConectados.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		lblJugadoresConectados.setToolTipText("");

		JTextArea textPuntuaciones = new JTextArea();
		textPuntuaciones.setEditable(false);
		textPuntuaciones.setColumns(1);
		textPuntuaciones.setWrapStyleWord(true);
		textPuntuaciones.setLineWrap(true);
		textPuntuaciones.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(154, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(textPuntuaciones, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
							.addGap(150))
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addComponent(lblJugadoresConectados)
							.addGap(195))))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addComponent(lblJugadoresConectados)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textPuntuaciones, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);

		Thread hiloHostear = new Thread(new Runnable() {
			public void run(){
				cliente.hostearSala(textPuntuaciones);
			}
		});

		hiloHostear.start();

		JButton btnEmpezar = new JButton("Empezar");
		Empezar2 empezar = new Empezar2(cliente, textPuntuaciones);
		btnEmpezar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cliente.getClientesConectados().size()>0){
					empezar.start();
					lblJugadoresConectados.setText("Tabla de puntuaciones:");
					textPuntuaciones.setText("");
					btnEmpezar.setEnabled(false);
					btnEmpezar.setVisible(false);
				}else{
					textPuntuaciones.setText("Debe haber un jugador en la sala como mínimo");
				}
            	
			}
		});
		btnEmpezar.setFont(new Font("Kristen ITC", Font.BOLD, 18));
		panel_2.add(btnEmpezar);

		JLabel lblKahoot = new JLabel("Kahoot!");
		lblKahoot.setForeground(new Color(0, 0, 128));
		lblKahoot.setFont(new Font("Kristen ITC", Font.BOLD, 42));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(212, Short.MAX_VALUE)
					.addComponent(lblKahoot)
					.addGap(200))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(32, Short.MAX_VALUE)
					.addComponent(lblKahoot)
					.addGap(25))
		);
		panel.setLayout(gl_panel);

		this.setLayout(gl_contentPane);
	}	
}
