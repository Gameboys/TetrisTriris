package gameboys.tetristriris;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@SuppressWarnings("javadoc")
public class Main {
	
	public static String PROP_FILE = "settings.cfg";
	public static Properties settings;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		settings = new Properties();
		try {
			File settingsFile = new File(PROP_FILE);
			if(!settingsFile.exists()){
				settingsFile.createNewFile();
				addDefaultValues(settings);
			}
			FileInputStream in = new FileInputStream(PROP_FILE);
			settings.load(in);
			if(settings.getProperty("LEFT")==null)addDefaultValues(settings);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ApplicationWindow frame = new ApplicationWindow();
		
		frame.setResizable(false);
		frame.pack();
	    frame.setVisible(true);
	}
	
	protected static void addDefaultValues(Properties settings){
		settings.setProperty("LEFT", "37");
		settings.setProperty("ROTATE", "38");
		settings.setProperty("RIGHT", "39");
		settings.setProperty("FALL", "40");
		settings.setProperty("PAUSE", "80");
		settings.setProperty("BOARDX", "10");
		settings.setProperty("BOARDY", "15");
		settings.setProperty("LEVEL", "1");
		settings.setProperty("BLOCKS", "0");
		settings.setProperty("SCORE1", "No one#0");
		settings.setProperty("SCORE2", "No one#0");
		settings.setProperty("SCORE3", "No one#0");
		settings.setProperty("SCORE4", "No one#0");
		settings.setProperty("SCORE5", "No one#0");
		try {
			OutputStream out = new FileOutputStream(PROP_FILE);
			settings.store(out, "Settings");
			out.close();
		} catch (IOException e) {
			System.err.println("Error at addDefaultValues: " + e);
		}
	}

}
