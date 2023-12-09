package Interfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import conexiones.clasesClienteGrafico.Cliente2;
import dominio.Persona;
import dominio.Pregunta;

public class ClienteHostConfig extends JPanel {

	private static final long serialVersionUID = 1L;
	private Cliente2 cliente;
	private VentanaPrincipal ventanaPrincipal;
    private List<Pregunta> preguntasSala = new ArrayList<>();

    private static HashMap<String, Persona> salas;
    private static boolean listo = false;
    private JTextField textRespuestaCorrecta;
    private JTextField textRespuestaFalsa1;
    private JTextField textRespuestaFalsa2;
    private JTextField textRespuestaFalsa3;
	/**
	 * Create the panel.
	 */
	public ClienteHostConfig(Cliente2 cliente, VentanaPrincipal ventana) {
		this.cliente = cliente;
		this.ventanaPrincipal = ventana;
		
		this.setBounds(700, 80, 600, 800);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();

		GroupLayout gl_contentPane = new GroupLayout(this);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addComponent(panel_1, 0, 0, Short.MAX_VALUE))
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(333, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 456, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
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
		textPregunta.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textPregunta.setRows(8);
		
		JLabel lblRespuestaCorrecta = new JLabel("Introduce la respuesta correcta");
		lblRespuestaCorrecta.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaCorrecta = new JTextField();
		textRespuestaCorrecta.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textRespuestaCorrecta.setColumns(10);
		
		JLabel lblRespuestaFalsa1 = new JLabel("Introduce la respuesta falsa 1");
		lblRespuestaFalsa1.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaFalsa1 = new JTextField();
		textRespuestaFalsa1.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textRespuestaFalsa1.setColumns(10);
		
		JLabel lblRespuestaFalsa2 = new JLabel("Introduce la respuesta falsa 2");
		lblRespuestaFalsa2.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaFalsa2 = new JTextField();
		textRespuestaFalsa2.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
		textRespuestaFalsa2.setColumns(10);
		
		JLabel lblRespuestaFalsa3 = new JLabel("Introduce la respuesta falsa 3");
		lblRespuestaFalsa3.setFont(new Font("Kristen ITC", Font.BOLD, 15));
		
		textRespuestaFalsa3 = new JTextField();
		textRespuestaFalsa3.setFont(new Font("Kristen ITC", Font.PLAIN, 14));
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
		
		JButton btnAnadirPregunta = new JButton("Añadir pregunta");
		btnAnadirPregunta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!textPregunta.getText().equals("") && !textRespuestaCorrecta.getText().equals("")
						&& !textRespuestaFalsa1.getText().equals("")&& !textRespuestaFalsa2.getText().equals("") && !textRespuestaFalsa3.getText().equals("")) {
					Pregunta p = new Pregunta(textPregunta.getText(), textRespuestaCorrecta.getText(), textRespuestaFalsa1.getText(), textRespuestaFalsa2.getText(), textRespuestaFalsa3.getText());
					cliente.aniadirPreguntaASala(p);
					JOptionPane.showMessageDialog(null,"Pregunta añadida correctamente.");
					textPregunta.setText("");
					textRespuestaCorrecta.setText("");
					textRespuestaFalsa1.setText("");
					textRespuestaFalsa2.setText("");
					textRespuestaFalsa3.setText("");

				}else {
					JOptionPane.showMessageDialog(null,"Rellena todos los campos.");
				}
			}
		});
		btnAnadirPregunta.setFont(new Font("Kristen ITC", Font.BOLD, 16));
		
		JButton btnEmpezar = new JButton("Empezar");
		btnEmpezar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cambiarVentana(cliente);
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
		
		this.setLayout(gl_contentPane);
	}
	
	public void cambiarVentana(Cliente2 aux) {
		this.cliente = aux;
		ventanaPrincipal.cambiarAClienteHostJugando();
	}

}
