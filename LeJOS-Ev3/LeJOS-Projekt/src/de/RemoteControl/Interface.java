package de.RemoteControl;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jdk.jfr.Recording;

public class Interface implements ActionListener {
	
	private Socket client;
	DataInputStream input;
	DataOutputStream output;

	JPanel panel = new JPanel();
	JFrame frame = new JFrame("Remote Control");
	JButton b_p1 = new JButton("▶️");
	JButton b_p2 = new JButton("↺"); 
	JButton b_enter = new JButton("♦"); 
	JButton b_up = new JButton("⇑"); 
	JButton b_down = new JButton("⇓"); 
	JButton b_right = new JButton("⇒"); 
	JButton b_left = new JButton("⇐"); 
	JButton b_record = new JButton("⏺️");
	JButton b_esc = new JButton("Escape");	
	JButton connect = new JButton("Connect");
	JButton b_delete = new JButton("Delete");
	JButton b_sRec = new JButton("⏸️");
	JTextArea filler = new JTextArea();
	
	JLabel status = new JLabel("Status: unconnected");
	JLabel angleTxt = new JLabel("Rotate Angle:");
	JTextField ip_field = new JTextField("192.168.0.131",20);
	JTextField angle_field = new JTextField("90", 20);
	
	JMenuBar bar = new JMenuBar();
	JMenu help = new JMenu("Help");
	JMenu file = new JMenu("Config");
	JMenuItem about = new JMenuItem("About");
	JMenuItem con = new JMenuItem("Connect");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem darkmode = new JMenuItem("Dark Mode");	
	JMenuItem lightmode = new JMenuItem("Light Mode");	
	JPanel error = new JPanel();
	JPanel conWindow = new JPanel();
	
	JPanel panelManager = new JPanel();
	
	Font font = new Font(Font.SANS_SERIF, Font.BOLD, 15); 
	
	int curV = 0;
	String curB = "0";
	boolean connect_window;
	boolean recording;
	
	public Interface() 
	{
		
		// Einstellungen des Fensters
		frame.setBackground(Color.BLACK);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.darkGray);
		frame.setTitle("Remote Controle");
		frame.setPreferredSize(new Dimension(500,500));
		frame.setResizable(false);
		
		
		// Menuebar erstellen
		exit.addActionListener(this);
		darkmode.addActionListener(this);
		lightmode.addActionListener(this);
		about.addActionListener(this);
		con.addActionListener(this);
		file.insert(exit, 0);
		file.insert(darkmode, 1);
		file.insert(lightmode, 2);
		file.insert(con, 3);
		help.insert(about,0);
		bar.add(file);
		bar.add(help);
		frame.setJMenuBar(bar);
		
		// Layouts
		panel.setLayout(new GridLayout(6,3,10, 10));
		conWindow.setLayout(new GridLayout(6,3,10, 10));
		
		
		// Buttons zum panel hinzufügen
		conWindow.add(connect);
		conWindow.add(ip_field);
		conWindow.add(status);
		conWindow.add(angleTxt);
		conWindow.add(angle_field);
		panel.add(new JTextArea()).setVisible(false);;
		panel.add(b_up);
		panel.add(new JTextArea()).setVisible(false);;
		panel.add(b_left);
		panel.add(b_enter);
		panel.add(b_right);
		panel.add(new JTextArea()).setVisible(false);
		panel.add(b_down);
		panel.add(new JTextArea()).setVisible(false);;
		panel.add(new JTextArea()).setVisible(false);;
		panel.add(new JTextArea()).setVisible(false);;
		panel.add(new JTextArea()).setVisible(false);;
		panel.add(b_p1);
		panel.add(b_sRec);
		panel.add(b_record);
		panel.add(b_p2);
		panel.add(b_delete);
		panel.add(b_esc);
		
		// Größe der Buttons und Schriftart
		b_p1.setPreferredSize(new Dimension(50,60));
		b_p1.setFont(font);
		b_p2.setFont(font);
		b_enter.setFont(font);
		b_down.setFont(font);
		b_right.setFont(font);
		b_left.setFont(font);
		b_esc.setFont(font);
		b_delete.setFont(font);
		b_record.setFont(font);
		b_sRec.setFont(font);
		b_up.setFont(font);
		connect.setFont(font);
		status.setFont(font);
		angleTxt.setFont(font);
		
		changeColor(Color.ORANGE, Color.WHITE);
		
		// Bei Druck eines Buttons auf Override der Klasse verweisen 
		b_p1.addActionListener(this);
		b_p2.addActionListener(this);
		b_left.addActionListener(this);
		b_right.addActionListener(this);
		b_down.addActionListener(this);
		b_up.addActionListener(this);
		b_esc.addActionListener(this);
		b_record.addActionListener(this);
		b_enter.addActionListener(this);
		connect.addActionListener(this);
		b_delete.addActionListener(this);
		b_sRec.addActionListener(this);
		
		panelManager.setLayout(new GridLayout(1,2));
		panelManager.add(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(35,35,35,35));
		conWindow.setBorder(BorderFactory.createEmptyBorder(35,35,35,35));
		frame.add(panelManager);
		frame.setLocationRelativeTo(null);;
		frame.pack();
		
	}
	private void changeColor(Color background, Color panelBack) 
	{
		panel.setBackground(background);
		b_p1.setBackground(background);
		b_p2.setBackground(background);
		b_enter.setBackground(background);
		b_down.setBackground(background);
		b_left.setBackground(background);
		b_right.setBackground(background);
		b_up.setBackground(background);
		b_delete.setBackground(background);
		b_record.setBackground(background);
		b_sRec.setBackground(background);
		connect.setBackground(background);
		b_esc.setBackground(background);
		conWindow.setBackground(panelBack);
		panel.setBackground(panelBack);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
			if (e.getSource() == exit) 
			{
				System.exit(0);
			}
			// Verlinkt zu den Githubs der Autoren
			else if (e.getSource() == about)
			{
				Desktop desk = java.awt.Desktop.getDesktop();
				try {
					URI kUri = new URI("https://github.com/KaitoKunTatsu");
					desk.browse(kUri);
					URI cUri = new URI("https://github.com/MConfuse");
					desk.browse(cUri);
					URI sUri = new URI("https://github.com/SladeVG");
					desk.browse(sUri);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, "https://github.com/KaitoKunTatsu\nhttps://github.com/MConfuse\nhttps://github.com/SladeVG", "Author", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
				return;
			}
			if (e.getSource() == darkmode) 
			{
				changeColor(Color.lightGray, Color.DARK_GRAY);
				status.setForeground(Color.WHITE);
				angleTxt.setForeground(Color.WHITE);
				return;
			}
			if (e.getSource() == lightmode) 
			{
				changeColor(Color.ORANGE, Color.WHITE);
				status.setForeground(Color.black);
				angleTxt.setForeground(Color.BLACK);
				return;
			}

			if (e.getSource() == con) 
			{
				if (!connect_window) {
					panelManager.add(conWindow);
					panelManager.updateUI();
					connect_window = true;
					frame.setPreferredSize(new Dimension(800,500));
					frame.pack();
				}
				else  
				{
					panelManager.remove(conWindow);
					panelManager.updateUI();
					connect_window = false;
					frame.setPreferredSize(new Dimension(500,500));
					frame.pack();
				}
				return;
			}
			
			try {
				// Knopfdrücke auf die Variable "curB" übertragen
				if (output != null) {	
					if (e.getSource() == b_enter) 
					{
						curB = "ENTER";
					}
					else if (e.getSource() == b_down) 
					{
						curB = "DOWN";
					}
					else if (e.getSource() == b_right) 
					{
						curB = "RIGHT";
					}
					else if (e.getSource() == b_up) 
					{
						curB = "UP";
					}
					else if (e.getSource() == b_left) 
					{
						curB = "LEFT";
					}
					else if (e.getSource() == b_esc) 
					{
						curB = "ESCAPE";
					}
					else if (e.getSource() == b_record) 
					{
						curB = "REC";
						if (!recording) 
						{
							recording = true;
							b_record.setBackground(Color.GREEN);
							panel.updateUI();
						}
					}
					else if (e.getSource() == b_p1) 
					{
						curB = "PLAY1";
					}	
					else if (e.getSource() == b_p2) 
					{
						curB = "PLAY2";
					}
					else if (e.getSource() == b_delete) 
					{
						curB = "DELETE";
					}
					else if (e.getSource() == b_sRec) 
					{
						curB = "STOPREC";
						if (recording) 
						{
							recording = false;
							b_record.setBackground(Color.RED);
							panel.updateUI();
						}
					}
				}
				
			// Ist connect gedrückt und der EV3 NICHT verbunden, wird dieser verbunden und der Outputstream initalisiert
			if (e.getSource() == connect && client == null) 
			{
				client = new Socket(ip_field.getText(), 1415);
				status.setText("Status: connected");
				output = new DataOutputStream(client.getOutputStream());
				return;
			}
			// Kann die Eingabe für die Rotation zu int konvertiert werden, fortsetzen
			try 
			{
				Integer.parseInt(angle_field.getText());
			}
			// Sonst eine Fehlermeldung werfen
			catch(Exception exx) 
			{
				JOptionPane.showMessageDialog(frame, "Can not convert angle to int. Please fix your input", "Input Error", JOptionPane.ERROR_MESSAGE); 	
				return;
			}
			// String zum EV3 übertragen, welcher den Input, Zähler an Knopfdrücken und die Gradzahl überträgt
			output.writeUTF(curB + ";" + Integer.toString(curV) + ";" + Integer.parseInt(angle_field.getText()));
			curV++;
			} 
			// Fehler beim übertragen als Fehlermeldung werfen
			catch(Exception ex) 
			{ 
				JOptionPane.showMessageDialog(frame, "No connection", "Connection Error", JOptionPane.ERROR_MESSAGE); 
			}
		}
	
	public static void main(String[] args) 
	{
		new Interface();
	}
}
