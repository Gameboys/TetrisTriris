package gameboys.tetristriris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

@SuppressWarnings({ "javadoc", "serial" })
public class GameWindow extends JSplitPane{

	public static int WIDTH = (int)ApplicationWindow.windowWidth;
	public static int HEIGHT = (int)ApplicationWindow.windowHeight;
	protected static GameBoard board;
	protected static DisplayWindow display;
	protected static ScoreBoard score;

	public GameWindow(Properties settings){
		super(JSplitPane.HORIZONTAL_SPLIT);
		setSize(WIDTH, HEIGHT);
		board = new GameBoard(settings);
		display = new DisplayWindow();
		score = new ScoreBoard(settings);

		JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,display,score);
		rightPanel.setSize(WIDTH*6/10,HEIGHT);
		rightPanel.setDividerLocation(0.4);
		rightPanel.setEnabled(false);

		setEnabled(false);
		setLeftComponent(board);
		setRightComponent(rightPanel);
		setDividerLocation(0.6);
	}

}

@SuppressWarnings("serial")
class GameBoard extends JPanel{

	
//	SOUND FILE TAKEN FROM http://www.youtube.com/watch?v=PV06M-Gqxgg
//	TETRIS - THEME 'A' ACAPELLA BY Smooth McGroove
	
	private String musicFileName = "tetrisacapella.wav";
	protected BigClip clip;
	private Properties settings;
	protected AnimationEventListener eventListener;
	protected char[] minosChars;
	private FallingBlock fallingBlock;
	protected static Stack matrix;
	private Timer timer;
	private Timer refresh;
	private int timerGap;
	private int refreshRate = 5;
	protected boolean mode;

	public GameBoard(Properties settings){
		super();
		setLayout(new BorderLayout());
		this.settings = settings;
		setOpaque(true);
		setBackground(Color.BLACK);
		setVisible(true);

		loadMusic(musicFileName);

		int boardX = Integer.parseInt(settings.getProperty("BOARDX"));
		int boardY = Integer.parseInt(settings.getProperty("BOARDY"));
		chooseBlocks();

		int level = Integer.parseInt(settings.getProperty("LEVEL"));
		timerGap = 1200 - level*200;

		matrix = new Stack(boardX, boardY);

		fallingBlock = new FallingBlock(minosChars[(new Random()).nextInt(minosChars.length)]);
		fallingBlock.put();

		eventListener = new AnimationEventListener();

		timer = new Timer(timerGap, eventListener);
		refresh = new Timer(refreshRate,new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});

		setMode(true);
		addKeys();
	}

	protected void chooseBlocks(){
		switch(Integer.parseInt(settings.getProperty("BLOCKS"))){
		case 0:
			minosChars = new char[]{'Z','O','S','T','J','I','L'};
			break;
		case 1:
			minosChars = new char[]{'j','r','i'};
			break;
		case 2:
			minosChars = new char[]{'Z','O','S','T','J','I','L','j','r','i'};
			break;
		default: break;
		}
	}

	public boolean isFocusable() { return true; }

	public void addKeys(){
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((Integer.parseInt(settings.getProperty("PAUSE"))),0), "PAUSE");
		getActionMap().put("PAUSE", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				PauseScreen pause = new PauseScreen();
				if(mode == true){
					JPanel source = (JPanel)e.getSource();
					source.add(pause,BorderLayout.CENTER);
					source.validate();
					source.repaint();
					setMode(false);
				}

			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((Integer.parseInt(settings.getProperty("LEFT"))),0), "LEFT");
		getActionMap().put("LEFT", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(mode == true){
					fallingBlock.goLeft();
				}
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((Integer.parseInt(settings.getProperty("RIGHT"))),0), "RIGHT");
		getActionMap().put("RIGHT", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(mode == true){
					fallingBlock.goRight();
				}
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((Integer.parseInt(settings.getProperty("ROTATE"))),0), "ROTATE");
		getActionMap().put("ROTATE", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(mode == true){
					fallingBlock.rotate();
				}
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((Integer.parseInt(settings.getProperty("FALL"))),0), "FALL");
		getActionMap().put("FALL", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(mode == true){
					timer.setDelay(timerGap/2);
				}
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((Integer.parseInt(settings.getProperty("FALL"))),0,true), "released FALL");
		getActionMap().put("released FALL", new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(mode == true){
					timer.setDelay(timerGap);
				}
			}
		});

	}

	public void setMode(boolean m){
		// modifies: this
		// effects: changes the mode to <m>.
		mode = m;
		if (mode == true){
			requestFocus();           // make sure keyboard is directed to us
			timer.start();
			refresh.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}else{
			timer.stop();
			refresh.stop();
			clip.stop();
		}
	}

	public void paint(Graphics g){
		super.paint(g);
		if(mode)matrix.paint(g);
	}

	public void loadMusic(String musicFileName){
		try{
			Clip a = AudioSystem.getClip();
			clip = new BigClip(a);
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(musicFileName));
			clip.open(inputStream);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public int placeInTopScores(int score){
		int place = -1;
		for(int i=1; i<=TopScores.NUM_TOP_SCORES; i++){
			StringTokenizer st = new StringTokenizer(settings.getProperty("SCORE"+i),"£");
			st.nextToken();
			String playerScore = st.nextToken();
			if(score>Integer.parseInt(playerScore)){
				place = i;
				break;
			}
		}
		return place;
	}

	public void addToTopScores(String playerName, int score){
		int place = placeInTopScores(score);
		if(place!=-1)shiftScoresDown(place);
		addScore(playerName, score, place);
		writeScoreChangesInFile();
	}

	public void shiftScoresDown(int fromRow){
		for(int i=TopScores.NUM_TOP_SCORES; i>fromRow; i--){
			settings.setProperty("SCORE"+i, settings.getProperty("SCORE"+(i-1)));
		}
	}

	public void addScore(String name, int score, int place){
		settings.setProperty("SCORE"+place, name+"£"+score);
	}

	public void writeScoreChangesInFile(){
		try {
			OutputStream out = new FileOutputStream(Main.PROP_FILE);
			settings.store(out, "Settings");
			out.close();
		} catch (IOException e) {
			System.err.println("Error at addDefaultValues: " + e);
		}
	}

	class AnimationEventListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(fallingBlock.stopped==true){
				if(matrix.isGameOver()){
					setMode(false);
					int score = Integer.parseInt(GameWindow.score.score.getText());
					GameOverScreen gameOver = new GameOverScreen(score);
					JPanel source = GameWindow.board;
					source.add(gameOver,BorderLayout.CENTER);
					source.validate();
					source.repaint();
				}
				matrix.checkRows();
				fallingBlock = new FallingBlock(GameWindow.display.minos.shape);
				fallingBlock.put();
				GameWindow.display.minos = new Minos(minosChars[(new Random()).nextInt(minosChars.length)]);
				GameWindow.display.repaint();
			}else fallingBlock.fall();
		}
	}

	class PauseScreen extends JPanel{
		public PauseScreen(){
			super();
			setLayout(new BorderLayout());
			JLabel pause = new JLabel("PAUSE");
			pause.setOpaque(true);
			pause.setHorizontalAlignment(SwingConstants.CENTER);
			pause.setFont(new Font("Arial",Font.BOLD,126));
			pause.setForeground(Color.RED);
			pause.setBackground(Color.DARK_GRAY);
			add(pause,BorderLayout.CENTER);

			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Integer.parseInt(settings.getProperty("PAUSE")),0), "PAUSE");
			getActionMap().put("PAUSE", new AbstractAction(){
				public void actionPerformed(ActionEvent e) {
					if(mode == false){
						JPanel source = (JPanel)e.getSource();
						Container parent = source.getParent();
						parent.remove(source);
						parent.validate();
						parent.repaint();
						setMode(true);
					}

				}
			});
		}
	}

	class GameOverScreen extends JPanel{

		public int WIDTH = (int)ApplicationWindow.windowWidth*6/10-3;
		public int HEIGHT = (int)ApplicationWindow.windowHeight-20;
		private int score;

		public GameOverScreen(int score){
			super();
			this.score = score;
			setLayout(null);
			setOpaque(true);
			setBackground(Color.DARK_GRAY);
			
			addGameOver();

			int place = GameWindow.board.placeInTopScores(score);
			if(place!=-1)addEnterName(place);

		}
		
		private void addGameOver(){
			JLabel game = new JLabel("GAME");
			game.setBounds(-WIDTH/10,HEIGHT/8-HEIGHT/8,WIDTH,HEIGHT/4);
			game.setHorizontalAlignment(SwingConstants.CENTER);
			game.setFont(new Font("Arial",Font.BOLD,100));
			game.setForeground(Color.RED);
			game.setBackground(Color.DARK_GRAY);
			add(game);

			JLabel over = new JLabel("OVER");
			over.setBounds(WIDTH/10,HEIGHT*2/8-HEIGHT/8,WIDTH,HEIGHT/4);
			over.setHorizontalAlignment(SwingConstants.CENTER);
			over.setFont(new Font("Arial",Font.BOLD,100));
			over.setForeground(Color.RED);
			over.setBackground(Color.DARK_GRAY);
			add(over);
		}
		
		private void addEnterName(int place){
			JLabel gratz = new JLabel("Congratulations!");
			gratz.setBounds(0,HEIGHT*8/16-HEIGHT/16,WIDTH,HEIGHT/8);
			gratz.setHorizontalAlignment(SwingConstants.CENTER);
			gratz.setVerticalAlignment(SwingConstants.CENTER);
			gratz.setFont(new Font("Arial",Font.BOLD,36));
			gratz.setForeground(Color.RED);
			add(gratz);

			JLabel number = new JLabel("You are number " + place + "!");
			number.setBounds(0,HEIGHT*9/16-HEIGHT/16,WIDTH,HEIGHT/8);
			number.setHorizontalAlignment(SwingConstants.CENTER);
			number.setVerticalAlignment(SwingConstants.CENTER);
			number.setFont(new Font("Arial",Font.BOLD,36));
			number.setForeground(Color.RED);
			add(number);

			JLabel enterName = new JLabel("Please enter your name:");
			enterName.setBounds(0,HEIGHT*10/16-HEIGHT/16,WIDTH,HEIGHT/8);
			enterName.setHorizontalAlignment(SwingConstants.CENTER);
			enterName.setVerticalAlignment(SwingConstants.CENTER);
			enterName.setFont(new Font("Arial",Font.BOLD,36));
			enterName.setForeground(Color.RED);
			add(enterName);

			JTextField nameField= new JTextField();
			nameField.setBounds(WIDTH/2-WIDTH/3, HEIGHT*12/16-HEIGHT/10, WIDTH*2/3,HEIGHT/5);
			nameField.setHorizontalAlignment(SwingConstants.CENTER);
			nameField.setFont(new Font("Arial",Font.BOLD,64));
			nameField.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					String playerName = ((JTextField)e.getSource()).getText();
					addToTopScores(playerName,score);
					System.exit(0);
				}
			});
			add(nameField);
			
		}
		
	}

}

@SuppressWarnings("serial")
class DisplayWindow extends JPanel{

	public static int WIDTH = (int)ApplicationWindow.windowWidth*4/10;
	public static int HEIGHT = (int)ApplicationWindow.windowHeight*4/10;
	protected Minos minos;

	public DisplayWindow(){
		super();
		setLayout(null);
		setOpaque(true);
		setVisible(true);

		JLabel next = new JLabel("Next:");
		next.setFont(new Font("Arial",Font.BOLD,48));
		next.setBounds(0,HEIGHT/20,WIDTH,HEIGHT/5);
		next.setHorizontalAlignment(SwingConstants.CENTER);
		add(next);

		char[] minosChars = GameWindow.board.minosChars;
		minos = new Minos(minosChars[(new Random()).nextInt(minosChars.length)]);
	}

	public void paint(Graphics g){
		super.paint(g);
		minos.paint(g,WIDTH/2,HEIGHT/2+HEIGHT/10,Stack.blockWidth,Stack.blockHeight);
	}

}

@SuppressWarnings("serial")
class ScoreBoard extends JPanel{

	public static int WIDTH = (int)ApplicationWindow.windowWidth*4/10;
	public static int HEIGHT = (int)ApplicationWindow.windowHeight*6/10;

	private Properties settings;
	private String levelText = "Level: ";
	private String linesText = "Lines: ";
	private String scoreText = "Score: ";
	public JLabel levelNum;
	public JLabel linesNum;
	public JLabel score;

	public ScoreBoard(Properties settings){
		super();
		this.settings = settings;
		setLayout(null);
		addLabels();
	}

	protected void addLabels(){
		Font font = new Font("Arial",Font.BOLD,36);
		JLabel levelTitle = new JLabel(levelText);
		levelTitle.setBounds(WIDTH/3-WIDTH/4,HEIGHT/5-HEIGHT/16,WIDTH/2,HEIGHT/8);
		levelTitle.setFont(font);
		add(levelTitle);
		JLabel linesTitle = new JLabel(linesText);
		linesTitle.setBounds(WIDTH/3-WIDTH/4,HEIGHT*2/5-HEIGHT/16,WIDTH/2,HEIGHT/8);
		linesTitle.setFont(font);
		add(linesTitle);
		JLabel scoreTitle = new JLabel(scoreText);
		scoreTitle.setFont(font);
		scoreTitle.setBounds(WIDTH/3-WIDTH/4,HEIGHT*3/5-HEIGHT/16,WIDTH/2,HEIGHT/8);
		add(scoreTitle);

		levelNum = new JLabel(settings.getProperty("LEVEL"));
		levelNum.setBounds(WIDTH*3/4-WIDTH/6,HEIGHT/5-HEIGHT/16,WIDTH/3,HEIGHT/8);
		levelNum.setHorizontalAlignment(SwingConstants.CENTER);
		levelNum.setFont(font);
		add(levelNum);
		linesNum = new JLabel("0");
		linesNum.setBounds(WIDTH*3/4-WIDTH/6,HEIGHT*2/5-HEIGHT/16,WIDTH/3,HEIGHT/8);
		linesNum.setHorizontalAlignment(SwingConstants.CENTER);
		linesNum.setFont(font);
		add(linesNum);
		score = new JLabel("0");
		score.setBounds(WIDTH*3/4-WIDTH/6,HEIGHT*3/5-HEIGHT/16,WIDTH/3,HEIGHT/8);
		score.setHorizontalAlignment(SwingConstants.CENTER);
		score.setFont(font);
		add(score);
	}

	public String getLevelText() {
		return levelText;
	}

	public void setLevelText(String levelText) {
		this.levelText = levelText;
	}

	public String getLinesText() {
		return linesText;
	}

	public void setLinesText(String linesText) {
		this.linesText = linesText;
	}

	public String getScoreText() {
		return scoreText;
	}

	public void setScoreText(String scoreText) {
		this.scoreText = scoreText;
	}


}

