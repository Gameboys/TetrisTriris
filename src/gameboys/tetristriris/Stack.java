package gameboys.tetristriris;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("javadoc")
public class Stack {

	public static int WIDTH = (int)ApplicationWindow.windowWidth*6/10-3;
	public static int HEIGHT = (int)ApplicationWindow.windowHeight-20;
	public static int blockWidth;
	public static int blockHeight;

	private Color[][] matrix;
	private int x;
	private int y;

	public Stack(int x, int y){
		matrix = new Color[y][x];
		this.x = x;
		this.y = y;
		blockWidth = WIDTH/x;
		blockHeight = HEIGHT/y;
	}


	public Color get(int i, int j){
		return matrix[i][j];
	}
	public void set(int i, int j, Color val){
		matrix[i][j] = val;
	}
	public int getHeight(){
		return y;
	}
	public int getWidth(){
		return x;
	}

	private void deleteRows(int a, int b){
		int deleted = b-a+1;
		for(int i=a; i<=b; i++){
			deleteRow(i);
		}
		int oldLines = Integer.parseInt(GameWindow.score.linesNum.getText());
		int newLines = oldLines + deleted;
		GameWindow.score.linesNum.setText(""+newLines);
		addScore(deleted);
		lowerBlocks(b,deleted);
	}

	private void addScore(int numDeletedRows){
		int score = 0;
		switch (numDeletedRows){
		case 1: score = 1;
		break;
		case 2: score = 3;
		break;
		case 3: score = 6;
		break;
		case 4: score = 10;
		break;
		default: System.err.println("Error at addScore.");
		break;
		}
		int oldScore = Integer.parseInt(GameWindow.score.score.getText());
		int newScore = oldScore + score;
		GameWindow.score.score.setText(""+newScore);
	}

	private void deleteRow(int row){
		matrix[row] = new Color[x];
	}

	private void lowerBlocks(int untilRow, int lowerCount){
		for(int i=untilRow; i>-1; i--){
			if(i-lowerCount<0)matrix[i] = new Color[x];
			else matrix[i] = matrix[i-lowerCount];
		}
	}

	public void checkRows(){
		int deleteFrom = -1;
		int deleteTo = -1;
		deleteLoop:
			for(int i=y-1; i>-1; i--){
				for(int j=0; j<x; j++){
					if(matrix[i][j]==null){
						continue deleteLoop;
					}
				}
				if(deleteFrom == -1) deleteTo = i;
				deleteFrom = i;
			}
		if(deleteFrom!=-1) deleteRows(deleteFrom, deleteTo);
	}

	public boolean isGameOver(){
		return (matrix[0][x/2]!=null || matrix[0][x/2+1]!=null);
	}

	public void paint(Graphics g){
		for(int i=0; i<y; i++){
			for(int j=0; j<x; j++){
				if(matrix[i][j] instanceof Color) new Box(j*blockWidth,i*blockHeight,blockWidth+1,blockHeight+1, matrix[i][j]).paint(g);
			}
		}
	}

	public String toString(){
		String res="";
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				res+=matrix[i][j]+" ";
			}
			res+="\n";
		}
		return res;
	}
}
