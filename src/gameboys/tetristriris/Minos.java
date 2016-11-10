package gameboys.tetristriris;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("javadoc")
public class Minos {

	private Color color;
	protected char shape;
	protected Color[][] dataMatrix;
	protected int mark = -20;
	protected boolean rotatesBack = false;
	protected int dataMatrixX;
	protected int dataMatrixY = mark;

	public Minos(char shape){
		this.shape = shape;
		switch(shape){
		case 'Z': 
			color = Color.GREEN;
			dataMatrix = new Color[][] {{color,color,null},
										{null,color,color},
										{null,null,null}};
			rotatesBack = true;
			break;
		case 'S': 
			color = Color.CYAN;
			dataMatrix = new Color[][] {{null,color,color},
										{color,color,null},
										{null,null,null}};
			rotatesBack = true;
			break;
		case 'O': 
			color = Color.RED;
			dataMatrix = new Color[][] {{color,color},
										{color,color}};
			break;
		case 'T': 
			color = Color.YELLOW;
			dataMatrix = new Color[][] {{null,null,null},
										{color,color,color},
										{null,color,null}};
			dataMatrixY = -1;
			break;
		case 'J':
			color = Color.MAGENTA;
			dataMatrix = new Color[][] {{null,null,null},
										{color,color,color},
										{null,null,color}};
			dataMatrixY = -1;
			break;
		case 'L':
			color = Color.BLUE;
			dataMatrix = new Color[][] {{null,null,null},
										{color,color,color},
										{color,null,null}};
			dataMatrixY = -1;
			break;
		case 'I':
			color = Color.ORANGE;
			dataMatrix = new Color[][] {{null,null,null,null},
										{color,color,color,color},
										{null,null,null,null},
										{null,null,null,null}};
			rotatesBack = true;
			dataMatrixY = -1;
			break;
		case 'i':
			color = Color.WHITE;
			dataMatrix = new Color[][] {{null,null,null},
										{color,color,color},
										{null,null,null}};
			dataMatrixY = -1;
			break;
		case 'j':
			color = Color.DARK_GRAY;
			dataMatrix = new Color[][] {{color,color},
										{null,color}};
			break;
		case 'r':
			color = Color.PINK;
			dataMatrix = new Color[][] {{color,color},
										{color,null}};
			break;
		default: System.err.println("Invalid char while creating Minos.");
		break;
		}
	}

	public Color getColor(){
		return color;
	}

	public void setColor(Color color){
		this.color = color;
	}

	public void paint(Graphics g, int x, int y, int width, int height){
		for(int i=0; i<dataMatrix.length; i++){
			for(int j=0; j<dataMatrix[0].length; j++){
				if(dataMatrix[i][j] instanceof Color) new Box(x + (int)((j-dataMatrix.length*0.5)*width),y + (int)((i-dataMatrix[0].length*0.5)*height),width,height,color).paint(g);
			}
		}
	}

}
