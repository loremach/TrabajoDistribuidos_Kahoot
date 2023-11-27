package Interfaces;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class VentanaClienteHost extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

    private List<Pregunta> preguntasSala = new ArrayList<>();

    private static HashMap<String, Persona> salas;
    private static boolean listo = false;
    private JTextField textRespuestaCorrecta;
    private JTextField textRespuestaFalsa1;
    private JTextField textRespuestaFalsa2;
    private JTextField textRespuestaFalsa3;
    
    private Cliente2 cliente;

	/**
	 * Create the frame.
	 */
	public VentanaClienteHost(Cliente2 cliente) {
		this.cliente=cliente;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 80, 600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 456, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
		);
		
		JLabel lblNewLabel = new JLabel("Kahoot!");
		lblNewLabel.setForeground(new Color(0, 0, 160));
		lblNewLabel.setFont(new Font("Kristen ITC", Font.BOLD, 40));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(211, Short.MAX_VALUE)
					.addComponent(lblNewLabel)
					.addGap(195))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(58, Short.MAX_VALUE)
					.addComponent(lblNewLabel)
					.addGap(48))
		);
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblPregunta = new JLabel("Introduce una pregunta");
		lblPregunta.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		JTextArea textPregunta = new JTextArea();
		textPregunta.setFont(new Font("Kristen ITC", Font.PLAIN, 13));
		textPregunta.setRows(8);
		
		JLabel lblRespuestaCorrecta = new JLabel("Introduce la respuesta correcta");
		lblRespuestaCorrecta.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaCorrecta = new JTextField();
		textRespuestaCorrecta.setFont(new Font("Kristen ITC", Font.PLAIN, 13));
		textRespuestaCorrecta.setColumns(10);
		
		JLabel lblRespuestaFalsa1 = new JLabel("Introduce la respuesta falsa 1");
		lblRespuestaFalsa1.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaFalsa1 = new JTextField();
		textRespuestaFalsa1.setFont(new Font("Kristen ITC", Font.PLAIN, 13));
		textRespuestaFalsa1.setColumns(10);
		
		JLabel lblRespuestaFalsa2 = new JLabel("Introduce la respuesta falsa 2");
		lblRespuestaFalsa2.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaFalsa2 = new JTextField();
		textRespuestaFalsa2.setFont(new Font("Kristen ITC", Font.PLAIN, 13));
		textRespuestaFalsa2.setColumns(10);
		
		JLabel lblRespuestaFalsa3 = new JLabel("Introduce la respuesta falsa 3");
		lblRespuestaFalsa3.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaFalsa3 = new JTextField();
		textRespuestaFalsa3.setFont(new Font("Kristen ITC", Font.PLAIN, 13));
		textRespuestaFalsa3.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPregunta, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
						.addComponent(textPregunta, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addComponent(lblRespuestaCorrecta, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
						.addComponent(textRespuestaCorrecta, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addComponent(lblRespuestaFalsa1, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
						.addComponent(textRespuestaFalsa1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRespuestaFalsa2, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
						.addComponent(textRespuestaFalsa2, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRespuestaFalsa3, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
						.addComponent(textRespuestaFalsa3, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblPregunta, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textPregunta, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblRespuestaCorrecta, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textRespuestaCorrecta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblRespuestaFalsa1, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textRespuestaFalsa1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblRespuestaFalsa2, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textRespuestaFalsa2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblRespuestaFalsa3, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(textRespuestaFalsa3, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		
		
		JButton btnAnadirPregunta = new JButton("Añadir pregunta");
		btnAnadirPregunta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!textPregunta.getText().equals("") && !textRespuestaCorrecta.getText().equals("")
						&& !textRespuestaFalsa1.getText().equals("")&& !textRespuestaFalsa2.getText().equals("") && !textRespuestaFalsa3.getText().equals("")) {
					Pregunta p = new Pregunta(textPregunta.getText(), textRespuestaCorrecta.getText(), textRespuestaFalsa1.getText(), textRespuestaFalsa2.getText(), textRespuestaFalsa3.getText());
					cliente.aniadirPreguntaASala(p);
					JOptionPane.showMessageDialog(null,"Pregunta añadida correctamente.");
				}else {
					JOptionPane.showMessageDialog(null,"Rellena todos los campos.");
				}
			}
		});
		btnAnadirPregunta.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		
		JButton btnEmpezar = new JButton("Empezar");
		btnEmpezar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaClienteHostJuegoEmpezado empiezaJuego = new VentanaClienteHostJuegoEmpezado(cliente);
				empiezaJuego.mostrarInterfaz();
			}
		});
		btnEmpezar.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(41)
					.addComponent(btnAnadirPregunta, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
					.addComponent(btnEmpezar, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
					.addGap(41))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(38)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAnadirPregunta, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEmpezar, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(39, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
	}
	
	public void mostrarInterfaz(){
        this.setVisible(true);
    }
	

}
