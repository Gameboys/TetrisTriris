package gameboys.tetristriris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings({ "javadoc", "serial" })
public class ApplicationWindow extends JFrame{
	
	private double scale = 1;
	private double XSCALE = 0.55*scale;
	private double YSCALE = 0.90*scale;
	public static double windowWidth;
	public static double windowHeight;
	
	protected MainMenu mainMenu;
	protected Options options;
	protected GameWindow gameWindow;
	protected TopScores topScores;

	/**
	 * 
	 */
	public ApplicationWindow(){
		super("Gameboys Present: TETRIS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadProperties();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		windowWidth = width*XSCALE;
		windowHeight = height*YSCALE;
		setLocation((int)((width-windowWidth)/2), (int)((height-windowHeight)/2));
		setPreferredSize(new Dimension((int)windowWidth,(int)windowHeight));
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		mainMenu = new MainMenu();
		contentPane.add(mainMenu,BorderLayout.CENTER);
		setContentPane(contentPane);
		addMainMenuListeners();
		
	}
	
	protected void loadProperties(){
		try{
			FileInputStream in = new FileInputStream(Main.PROP_FILE);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
		}catch(IOException e){
			System.err.println("Error at loadProperties: " + e);
		}
	}
	
	private void addMainMenuListeners(){
		mainMenu.play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				gameWindow = new GameWindow(Main.settings);
				getContentPane().removeAll();
				getContentPane().add(gameWindow);
				validate();
				repaint();
			}
		});
		mainMenu.options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				options = new Options();
				getContentPane().removeAll();
				getContentPane().add(options);
				addOptionsListeners();
				validate();
				repaint();
			}
		});
		mainMenu.scores.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				topScores = new TopScores();
				getContentPane().removeAll();
				getContentPane().add(topScores);
				addScoresListeners();
				validate();
				repaint();
			}
		});
	}
	
	private void addOptionsListeners(){
		options.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getContentPane().removeAll();
				getContentPane().add(mainMenu);
				validate();
				repaint();
			}
		});
		options.apply.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Main.settings=options.settings;
				try {
					OutputStream out = new FileOutputStream(Main.PROP_FILE);
					Main.settings.store(out, "Settings");
					out.close();
				} catch (IOException ex) {
					System.err.println("Error at addDefaultValues: " + ex);
				}
				getContentPane().removeAll();
				getContentPane().add(mainMenu);
				validate();
				repaint();
			}
		});
	}
	
	private void addScoresListeners(){
		topScores.back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				getContentPane().removeAll();
				getContentPane().add(mainMenu);
				validate();
				repaint();
			}
		});
	}
	
}

@SuppressWarnings("serial")
class MainMenu extends JPanel{
	
	public static int WIDTH = (int)ApplicationWindow.windowWidth;
	public static int HEIGHT = (int)ApplicationWindow.windowHeight;
	public JButton play;
	public JButton options;
	public JButton scores;
	
	public MainMenu(){
		super();
		setLayout(null);
		addButtons();
		
		JLabel tetriris = new JLabel("TETRIRIS");
		tetriris.setFont(new Font("Arial", Font.BOLD, 84));
		tetriris.setBounds(0,0, WIDTH, HEIGHT/5);
		tetriris.setHorizontalAlignment(SwingConstants.CENTER);
		add(tetriris);
	}
	
	protected void addButtons(){
		
		play = new JButton("Play TETRIRIS");
		play.setBounds(WIDTH/2-WIDTH/10,HEIGHT*8/30,WIDTH/5,HEIGHT/15);
		add(play);
		
		options = new JButton("Options");
		options.setBounds(WIDTH/2-WIDTH/14,HEIGHT*12/30,WIDTH/7,HEIGHT/15);
		add(options);
		
		scores = new JButton("Top Scores");
		scores.setBounds(WIDTH/2-WIDTH/14,HEIGHT*16/30,WIDTH/7,HEIGHT/15);
		add(scores);
		
		JLabel copyright = new JLabel("Copyright © 2014, Gameboys and its respective owners. This game is a project for the COMP302 class. All rights reserved.");
		copyright.setFont(new Font("Arial",Font.PLAIN,9));
		copyright.setHorizontalAlignment(SwingConstants.CENTER);
		copyright.setBounds(0,HEIGHT-75,WIDTH,20);
		add(copyright);
		
	}
	
}



