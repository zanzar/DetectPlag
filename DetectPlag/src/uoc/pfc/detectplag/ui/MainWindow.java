package uoc.pfc.detectplag.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uoc.pfc.detectplag.Principal;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -577321086150760316L;
	private JButton search = new JButton("In Path");
	private JButton pathSalida = new JButton("Out Path");
	private JButton start = new JButton("Start Comparison");
	private JLabel pathInLabel = new JLabel("Select Directory");
	private JLabel pathOutLabel = new JLabel("C:\\detectplag\\result");
	private JLabel warningLabel = new JLabel("Threshold Warning %");
	private JLabel criticalLabel = new JLabel("Threshold Critical %");
	private JTextField warning_txt = new JTextField(2);
	private JTextField critical_txt = new JTextField(2);
	private static JLabel stateLabel  = new JLabel("Select a path and press start");
	private JFileChooser chooser;
	private String choosertitle;
	Image image;

	
	
	public MainWindow() {
		this.setSize(400, 400);
		this.setTitle("Detection Plagiarism");
		this.setLayout(null);
		this.setResizable(false);
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(pathInLabel);
			}
		});
		search.setBounds(10, 20, 120, 30);
		this.add(search);
		
		pathInLabel.setBounds(135, 20, 150, 25);
		this.add(pathInLabel);
		
		pathSalida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(pathOutLabel);
			}
		});
		pathSalida.setBounds(10, 60, 120, 30);
		this.add(pathSalida);
		
		pathOutLabel.setBounds(135, 60, 150, 25);
		this.add(pathOutLabel);
		
		warningLabel.setBounds(10, 100, 120, 25);
		this.add(warningLabel);
		
		criticalLabel.setBounds(10, 130, 120, 25);
		this.add(criticalLabel);
		
		warning_txt.setBounds(150, 100, 60, 25);
		warning_txt.setText("25");
		this.add(warning_txt);
		
		critical_txt.setBounds(150, 130, 60, 25);
		critical_txt.setText("50");
		this.add(critical_txt);
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!pathInLabel.getText().equals("Select Directory")){
					stateLabel.setText("Comparing....");
					startProcess();
				}else{
					stateLabel.setText("Error: Invalid Directory, please select a valid path");
				}
			}
		});
		start.setBounds(95, 250, 200, 30);
		this.add(start);
		
		stateLabel.setBounds(95, 180, 350, 50);
		Font font = new Font("Verdana", Font.BOLD, 12);
		stateLabel.setFont(font);
		stateLabel.setForeground(Color.BLUE);
		this.add(stateLabel);
	}

	private void searchFunction(JLabel path) {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			path.setText(chooser.getSelectedFile().toString());
		} 
	}

	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}
	
	public void changeStateLablel(final String text){
		Thread thread = new Thread(){
		    public void run(){
		    	stateLabel.setText(text);
		    }
		  };
		  thread.start();
	}
	
	public void startProcess(){
		Thread thread = new Thread(){
		    public void run(){
		    	try {
		    		String txtWarning = warning_txt.getText();
		    		String txtCritical = critical_txt.getText();
		    		int numWarning = Integer.parseInt(txtWarning);
		    		int numCritical = Integer.parseInt(txtCritical);
		    		if(numWarning < numCritical){
		    			Principal.startCompare(pathInLabel.getText(),pathOutLabel.getText(),numWarning,numCritical);
		    		}else{
		    			changeStateLablel("Error: Valor warning mayor que critical");
		    		}
					
				} catch (SAXException | IOException | ParserConfigurationException e) {
					e.printStackTrace();
					changeStateLablel("Error: recogiendo los parametros, revise los valores");
				}
		    }
		  };
		  thread.start();
		
	}
}
