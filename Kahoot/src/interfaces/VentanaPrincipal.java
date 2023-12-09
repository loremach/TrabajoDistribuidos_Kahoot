package Interfaces;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import conexiones.clasesClienteGrafico.Cliente2;
import dominio.Persona;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;	
	private Cliente2 cliente;
//	private JPanel clienteHostConfig;
//	private JPanel inicio;
//	private JPanel clienteHostJugando;
//	private JPanel clienteEntrarSala;

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal(Cliente2 cliente) {
		this.cliente=cliente;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Kahoot!");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 80, 600, 800);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0,0));
		
		this.setVisible(true);
	}
	
	public void empezar() {
		contentPane.removeAll();
		contentPane.add(new Inicio(this.cliente, this));
		contentPane.repaint();
		contentPane.revalidate();
		this.setVisible(true);
	}

	public void cambiarAClienteHostConfig() {
		contentPane.removeAll();
		contentPane.add(new ClienteHostConfig(this.cliente, this));
		contentPane.repaint();
		contentPane.revalidate();
	}
	
	public void cambiarAClienteHostJugando() {
		contentPane.removeAll();
		contentPane.add(new ClienteHostJugando(this.cliente, this));
		contentPane.repaint();
		contentPane.revalidate();
		//((ClienteHostJugando) this.clienteHostJugando).empezarAHostear();
	}
	
	public void cambiarAClienteEntrarSala() {
		contentPane.removeAll();
		contentPane.add(new ClienteEntrarSala(this.cliente, this));
		contentPane.repaint();
		contentPane.revalidate();
	}
	
	public void cambiarAClienteJugador(Persona personaHost, Persona personaJugador){
		contentPane.removeAll();
		contentPane.add(new ClienteJugador(this.cliente, this, personaHost, personaJugador));
		contentPane.repaint();
		contentPane.revalidate();
	}
	

}
