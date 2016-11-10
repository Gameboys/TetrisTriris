package gameboys.tetristriris;

import java.awt.Font;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings({ "javadoc", "serial" })
public class TopScores extends JPanel{

	private static int WIDTH = (int)ApplicationWindow.windowWidth;
	private static int HEIGHT = (int)ApplicationWindow.windowHeight;
	protected static int NUM_TOP_SCORES = 5;
	protected Properties settings;
	protected JButton back;
	
	public TopScores(){
		super();
		setLayout(null);
		settings = Main.settings;
		
		JLabel topScores = new JLabel("TOP SCORES");
		topScores.setFont(new Font("Arial", Font.BOLD, 48));
		topScores.setBounds(0, HEIGHT/15-HEIGHT/20, WIDTH, HEIGHT/10);
		topScores.setHorizontalAlignment(SwingConstants.CENTER);
		add(topScores);
		
		back = new JButton("Back");
		back.setBounds(WIDTH*3/5, HEIGHT*10/12-HEIGHT/40,WIDTH/10,HEIGHT/20);
		add(back);
		
		addScores();
		
	}
	
	private void addScores(){
		try{
			for(int i=1; i<=NUM_TOP_SCORES; i++){
				StringTokenizer st = new StringTokenizer(settings.getProperty("SCORE"+i),"£");
				JLabel player = new JLabel(st.nextToken());
				player.setFont(new Font("Arial", Font.BOLD, 36));
				player.setBounds(WIDTH*8/20-WIDTH/6,i*HEIGHT/7-HEIGHT/20,WIDTH/3,HEIGHT/10);
				add(player);
				JLabel score = new JLabel(st.nextToken());
				score.setFont(new Font("Arial", Font.BOLD, 36));
				score.setHorizontalAlignment(SwingConstants.RIGHT);
				score.setBounds(WIDTH*11/20-WIDTH/6, i*HEIGHT/7-HEIGHT/20,WIDTH/3,HEIGHT/10);
				add(score);
			}
		}catch(Exception e){
			System.err.println("Error at addScores: " + e);
			e.printStackTrace();
		}
	}

}
