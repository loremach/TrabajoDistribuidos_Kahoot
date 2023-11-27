package Interfaces;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import conexiones.AtenderCliente;
import conexiones.Cliente2;
import dominio.Persona;
import dominio.Pregunta;
import dominio.Sala;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaClienteHostJuegoEmpezado extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static Sala sala = null;
	private List<Pregunta> preguntasSala = new ArrayList<>();
	private static int localPort = 0;
	private static HashMap<String, Persona> salas;
	private static boolean listo = false;
	private static List<AtenderCliente> listaEjecuciones = new ArrayList<>();
	private HashMap<Persona, Integer> tablaPuntuaciones;

	private Cliente2 cliente;

	/**
	 * Create the frame.
	 */
	public VentanaClienteHostJuegoEmpezado(Cliente2 cliente) {

		this.cliente = cliente;
		cliente.hostearSala();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 80, 600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JPanel panel = new JPanel();

		JPanel panel_1 = new JPanel();

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE).addContainerGap())
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(5)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 524, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)));

		JLabel lblJugadoresConectados = new JLabel("Jugadores conectados:");
		lblJugadoresConectados.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		lblJugadoresConectados.setToolTipText("");

		JTextArea textPuntuaciones = new JTextArea();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addGap(197).addComponent(lblJugadoresConectados))
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(textPuntuaciones,
								GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addGap(5).addComponent(lblJugadoresConectados)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(textPuntuaciones, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addContainerGap()));
		panel_1.setLayout(gl_panel_1);

		JButton btnEmpezar = new JButton("Empezar");
		btnEmpezar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente.setListo(true);
				tablaPuntuaciones = cliente.enviarPreguntasRecogerRespuestas();
				textPuntuaciones.setText(mostrarPuntuaciones());
			}
		});
		btnEmpezar.setFont(new Font("Kristen ITC", Font.BOLD, 18));
		panel_2.add(btnEmpezar);

		JLabel lblKahoot = new JLabel("Kahoot!");
		lblKahoot.setForeground(new Color(0, 0, 160));
		lblKahoot.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(198).addComponent(lblKahoot).addContainerGap(198, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_panel
				.createSequentialGroup().addContainerGap(32, Short.MAX_VALUE).addComponent(lblKahoot).addGap(25)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	public void mostrarInterfaz() {
		this.setVisible(true);
	}

	public String mostrarPuntuaciones() {
		String mostrar = "";
		if (tablaPuntuaciones != null) {
			for (Persona p : tablaPuntuaciones.keySet()) {
				mostrar = mostrar + p.getIp() + ": " + tablaPuntuaciones.get(p) + " puntos";
			}
		}
		return mostrar;
	}
}
