package de.RemoteControl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Interface implements ActionListener {
	
	private Socket client;
	DataInputStream input;
	DataOutputStream output;

	JPanel panel = new JPanel();
	JFrame frame = new JFrame("inputManag");
	JButton b_p1 = new JButton("Play 1");
	JButton b_p2 = new JButton("Play 2"); 
	JButton b_enter = new JButton("Enter"); 
	JButton b_up = new JButton("Up"); 
	JButton b_down = new JButton("Down"); 
	JButton b_right = new JButton("Right"); 
	JButton b_left = new JButton("Left"); 
	JButton b_record = new JButton("Record");
	JButton b_esc = new JButton("Escape");	
	JButton connect = new JButton("Connect");
	JButton b_delete = new JButton("Delete");
	JButton b_sRec = new JButton("Stop Rec");
	
	JLabel status = new JLabel("Status: unconnected");
	JTextField ip_field = new JTextField("192.168.0.131",20);
	JTextField angel_field = new JTextField("90", 20);
	
	JMenuBar bar = new JMenuBar();
	JMenu help = new JMenu("Help");
	JMenu file = new JMenu("File");
	JMenuItem about = new JMenuItem("About");
	JMenuItem exit = new JMenuItem("Exit");
	
	JPanel error = new JPanel();
	
	int curV = 0;
	String curB = "0";
	
	public Interface() 
	{
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.darkGray);
		frame.setTitle("Remote Controle");
		frame.setResizable(false);
		
		exit.addActionListener(this);
		file.insert(exit, 0);
		help.insert(about,0);
		bar.add(file);
		bar.add(help);
		frame.setJMenuBar(bar);
		
		panel.setLayout(new GridLayout(5,3,10, 10));
		
		panel.add(connect);
		panel.add(ip_field);
		panel.add(status);
		panel.add(b_p1);
		panel.add(b_up);
		panel.add(b_p2);
		panel.add(b_left);
		panel.add(b_enter);
		panel.add(b_right);
		panel.add(b_record);
		panel.add(b_down);
		panel.add(b_esc);
		panel.add(b_delete);
		panel.add(b_sRec);
		panel.add(angel_field);
		
		b_p1.addActionListener(this);
		b_p1.setPreferredSize(new Dimension(50,60));
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
		
		frame.add(panel);
		frame.setLocationRelativeTo(null);;
		frame.pack();
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {		
			if (e.getSource() == exit) 
			{
				System.exit(0);
			}
			try {
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
					}
				}
			if (e.getSource() == connect && client == null) 
			{
				client = new Socket(ip_field.getText(), 1415);
				status.setText("Status: connected");
				input = new DataInputStream(client.getInputStream());
				output = new DataOutputStream(client.getOutputStream());
				return;
			}
			try 
			{
				Integer.parseInt(angel_field.getText());
			}
			catch(Exception exx) 
			{
				JOptionPane.showMessageDialog(frame, "Can not convert angel to int. Please fix your input", "Input Error", JOptionPane.ERROR_MESSAGE); 	
				return;
			}
			output.writeUTF(curB + ";" + Integer.toString(curV) + ";" + Integer.parseInt(angel_field.getText()));
			curV++;
			} 
			catch(Exception ex) 
			{ 
				JOptionPane.showMessageDialog(frame, "Connection lost", "Connection Error", JOptionPane.ERROR_MESSAGE); 
			}
		}
	
	public static void main(String[] args) 
	{
		new Interface();
	}
}
