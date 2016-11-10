package gameboys.tetristriris;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings({ "javadoc", "serial" })
public class Options extends JPanel{

	private static int WIDTH = (int)ApplicationWindow.windowWidth;
	private static int HEIGHT = (int)ApplicationWindow.windowHeight;
	protected JButton cancel;
	protected JButton apply;
	protected Properties settings;
	
	public Options(){
		super();
		setLayout(null);
		settings = Main.settings;
		
		JLabel options = new JLabel("OPTIONS");
		options.setFont(new Font("Arial", Font.BOLD, 48));
		options.setBounds(0, HEIGHT/15-HEIGHT/20, WIDTH, HEIGHT/10);
		options.setHorizontalAlignment(SwingConstants.CENTER);
		add(options);
		
		addBlockOptions();
		addLevelOptions();
		addBoardOptions();
		addKeyOptions();
		addFinalizeButtons();
		
	}
	
	protected void addBlockOptions(){
		JLabel blockOptions = new JLabel("Block Types:");
		blockOptions.setBounds(WIDTH*2/10-WIDTH/20,HEIGHT/6-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(blockOptions);
		
		String blockChoice = Main.settings.getProperty("BLOCKS");
		
		JRadioButton tetri = new JRadioButton("Tetriminos");
		tetri.setBounds(WIDTH*4/10-48, HEIGHT/6-11, 97, 23);
		tetri.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				settings.setProperty("BLOCKS", "0");
			}
		});
		add(tetri);
		
		JRadioButton tri = new JRadioButton("Triminos");
		tri.setBounds(WIDTH*6/10-48, HEIGHT/6-11,97,23);
		tri.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				settings.setProperty("BLOCKS", "1");
			}
		});
		add(tri);
		
		JRadioButton both = new JRadioButton("Both");
		both.setBounds(WIDTH*8/10-48, HEIGHT/6-11,97,23);
		both.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				settings.setProperty("BLOCKS", "2");
			}
		});
		add(both);
		
		ButtonGroup group = new ButtonGroup();
		group.add(tetri);
		group.add(tri);
		group.add(both);
		
		if(blockChoice.equals("0"))tetri.setSelected(true);
		else if(blockChoice.equals("1"))tri.setSelected(true);
		else if(blockChoice.equals("2"))both.setSelected(true);
		else System.err.println("Error: Invalid blockChoice");
	}
	
	protected void addLevelOptions(){
		JLabel level = new JLabel("Level:");
		level.setBounds(WIDTH*2/10-WIDTH/20, HEIGHT/3-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(level);
		
		int levelChoice = Integer.parseInt(Main.settings.getProperty("LEVEL"));
		
		JSlider levels = new JSlider(1,5);
		levels.setLabelTable(levels.createStandardLabels(1));
		levels.setPaintLabels(true);
		levels.setBounds(WIDTH*7/20,HEIGHT/3-25,WIDTH/2,50);
		levels.setValue(levelChoice);
		levels.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting())settings.setProperty("LEVEL", ""+source.getValue());
			}
		});
		add(levels);
	}
	
	protected void addBoardOptions(){
		JLabel boardSize = new JLabel("Board Size (X*Y):");
		boardSize.setBounds(WIDTH*2/10-WIDTH/20, HEIGHT/2-HEIGHT/20, WIDTH/10,HEIGHT/10);
		add(boardSize);
		
		String boardXChoice = Main.settings.getProperty("BOARDX");
		String boardYChoice = Main.settings.getProperty("BOARDY");
		
		JSlider boardX = new JSlider(8,20);
		boardX.setLabelTable(boardX.createStandardLabels(2));
		boardX.setPaintLabels(true);
		boardX.setBounds(WIDTH*7/20,HEIGHT/2-25,WIDTH/2,50);
		boardX.setValue(Integer.parseInt(boardXChoice));
		boardX.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting())settings.setProperty("BOARDX", ""+source.getValue());
			}
		});
		add(boardX);
		
		JSlider boardY = new JSlider(12,30);
		boardY.setLabelTable(boardY.createStandardLabels(3));
		boardY.setPaintLabels(true);
		boardY.setBounds(WIDTH*7/20,HEIGHT/2+25,WIDTH/2,50);
		boardY.setValue(Integer.parseInt(boardYChoice));
		boardY.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting())settings.setProperty("BOARDY", ""+source.getValue());
			}
		});
		add(boardY);
	}
	
	protected void addKeyOptions(){
		JLabel keyConf = new JLabel("Key Configurations:");
		keyConf.setBounds(WIDTH*2/10-WIDTH/20,HEIGHT*2/3-HEIGHT/20,WIDTH/5,HEIGHT/10);
		add(keyConf);
		
		JLabel left = new JLabel("Move Left");
		left.setBounds(WIDTH*2/10-WIDTH/20,HEIGHT*17/24-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(left);
		JLabel right = new JLabel("Move Right");
		right.setBounds(WIDTH*2/10-WIDTH/20,HEIGHT*18/24-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(right);
		JLabel rotate = new JLabel("Rotate Block");
		rotate.setBounds(WIDTH*9/20-WIDTH/20,HEIGHT*17/24-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(rotate);
		JLabel fall = new JLabel("Fall Down");
		fall.setBounds(WIDTH*9/20-WIDTH/20,HEIGHT*18/24-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(fall);
		JLabel pause = new JLabel("Pause Game");
		pause.setBounds(WIDTH*29/40-WIDTH/20,HEIGHT*17/24-HEIGHT/20,WIDTH/10,HEIGHT/10);
		add(pause);
		
		JToggleButton leftKey = new JToggleButton(KeyEvent.getKeyText(Integer.parseInt(settings.getProperty("LEFT"))));
		leftKey.setBounds(WIDTH*18/60-WIDTH/24,HEIGHT*17/24-HEIGHT/60,WIDTH/12,HEIGHT/30);
		leftKey.setToolTipText("Click to change key");
		leftKey.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				JToggleButton source = (JToggleButton)e.getSource();
				if(source.isSelected()){
					settings.setProperty("LEFT", ""+e.getKeyCode());
					source.setText(KeyEvent.getKeyText(e.getKeyCode()));
					source.setSelected(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
		});
		add(leftKey);
		
		JToggleButton rightKey = new JToggleButton(KeyEvent.getKeyText(Integer.parseInt(settings.getProperty("RIGHT"))));
		rightKey.setBounds(WIDTH*18/60-WIDTH/24,HEIGHT*18/24-HEIGHT/60,WIDTH/12,HEIGHT/30);
		rightKey.setToolTipText("Click to change key");
		rightKey.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				JToggleButton source = (JToggleButton)e.getSource();
				if(source.isSelected()){
					settings.setProperty("RIGHT", ""+e.getKeyCode());
					source.setText(KeyEvent.getKeyText(e.getKeyCode()));
					source.setSelected(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
		});
		add(rightKey);
		
		JToggleButton rotateKey = new JToggleButton(KeyEvent.getKeyText(Integer.parseInt(settings.getProperty("ROTATE"))));
		rotateKey.setBounds(WIDTH*34/60-WIDTH/24,HEIGHT*17/24-HEIGHT/60,WIDTH/12,HEIGHT/30);
		rotateKey.setToolTipText("Click to change key");
		rotateKey.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				JToggleButton source = (JToggleButton)e.getSource();
				if(source.isSelected()){
					settings.setProperty("ROTATE", ""+e.getKeyCode());
					source.setText(KeyEvent.getKeyText(e.getKeyCode()));
					source.setSelected(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
		});
		add(rotateKey);
		
		JToggleButton fallKey = new JToggleButton(KeyEvent.getKeyText(Integer.parseInt(settings.getProperty("FALL"))));
		fallKey.setBounds(WIDTH*34/60-WIDTH/24,HEIGHT*18/24-HEIGHT/60,WIDTH/12,HEIGHT/30);
		fallKey.setToolTipText("Click to change key");
		fallKey.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				JToggleButton source = (JToggleButton)e.getSource();
				if(source.isSelected()){
					settings.setProperty("FALL", ""+e.getKeyCode());
					source.setText(KeyEvent.getKeyText(e.getKeyCode()));
					source.setSelected(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
		});
		add(fallKey);
		
		JToggleButton pauseKey = new JToggleButton(KeyEvent.getKeyText(Integer.parseInt(settings.getProperty("PAUSE"))));
		pauseKey.setBounds(WIDTH*50/60-WIDTH/24,HEIGHT*17/24-HEIGHT/60,WIDTH/12,HEIGHT/30);
		pauseKey.setToolTipText("Click to change key");
		pauseKey.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				JToggleButton source = (JToggleButton)e.getSource();
				if(source.isSelected()){
					settings.setProperty("PAUSE", ""+e.getKeyCode());
					source.setText(KeyEvent.getKeyText(e.getKeyCode()));
					source.setSelected(false);
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
		});
		add(pauseKey);
		
	}
	
	protected void addFinalizeButtons(){
		cancel = new JButton("Cancel");
		cancel.setBounds(WIDTH*3/5, HEIGHT*10/12-HEIGHT/40,WIDTH/10,HEIGHT/20);
		add(cancel);
		
		apply = new JButton("Apply");
		apply.setBounds(WIDTH*3/4,HEIGHT*10/12-HEIGHT/40,WIDTH/10,HEIGHT/20);
		add(apply);
	}

}
