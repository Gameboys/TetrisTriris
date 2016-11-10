package gameboys.tetristriris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

@SuppressWarnings({ "javadoc", "serial" })
public class Box extends Rectangle{
	
	private int width;
	private int height;
	private Color color;
	private int x;
	private int y;
	
	public Box(int x, int y, int width, int height, Color color){
		super(width,height);
		this.width = width;
		this.height = height;
		this.color = color;
		this.x = x;
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void paint(Graphics g){
		Rectangle clipRect = g.getClipBounds();
		
		if(clipRect.intersects(this)){
			Color color = this.color.darker();
			int blackWidth = 3;
			int gradientWidth = 8;
			
			//Paint the black border
			g.setColor(Color.BLACK);
			g.fillRect(x, y, width, blackWidth);
			g.fillRect(x, y, blackWidth, height);
			g.fillRect(x, y+height-blackWidth, width, blackWidth);
			g.fillRect(x+width-blackWidth, y, blackWidth, height);
			
			//Paint the light and shadow gradients
			g.setColor(color.darker());
			g.fillRect(x+blackWidth, y+blackWidth, gradientWidth, height-2*blackWidth);
			g.fillRect(x+blackWidth, y+height-gradientWidth-blackWidth, width-2*blackWidth, gradientWidth);
			g.setColor(color.brighter());
			g.fillRect(x+blackWidth, y+blackWidth, width-2*blackWidth, gradientWidth);
			g.fillRect(x+width-blackWidth-gradientWidth, y+blackWidth, gradientWidth, height-2*blackWidth);
			
			//Paint the corner triangles
			g.setColor(color.darker());
			g.fillPolygon(new int[]{x+blackWidth,x+blackWidth,x+blackWidth+gradientWidth}, new int[]{y+blackWidth,y+blackWidth+gradientWidth,y+blackWidth+gradientWidth}, 3);
			g.fillPolygon(new int[]{x+width-blackWidth-gradientWidth,x+width-blackWidth-gradientWidth,x+width-blackWidth}, new int[]{y+height-blackWidth-gradientWidth,y+height-blackWidth,y+height-blackWidth}, 3);
			
			//Paint the inner color;
			g.setColor(color);
			g.fillRect(x+gradientWidth+blackWidth, y+gradientWidth+blackWidth, width-2*(gradientWidth+blackWidth), height-2*(gradientWidth+blackWidth));
		}
	}
}
