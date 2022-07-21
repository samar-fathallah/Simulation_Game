package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameView extends JFrame{
	public GameView(){
		setTitle("Rescue Simulation");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setLayout(new BorderLayout());
		validate();
	}
	public void addPanel(JPanel p){
		add(p,BorderLayout.CENTER);
	}

}
