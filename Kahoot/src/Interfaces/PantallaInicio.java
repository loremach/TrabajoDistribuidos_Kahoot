package Interfaces;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PantallaInicio extends JFrame{
    private int opcion;
    private JPanel contentPane;

    public PantallaInicio(){
        setResizable(false);
		setTitle("Kahoot!");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(700, 80, 600, 900);

    }

    public void mostrarInterfaz(){
        this.setVisible(true);
    }
}
