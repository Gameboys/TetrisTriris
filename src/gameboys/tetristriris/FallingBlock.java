package gameboys.tetristriris;

import java.awt.Color;
import java.awt.event.ActionEvent;

@SuppressWarnings({"javadoc","serial"})
public class FallingBlock extends Minos{

	private boolean rotated;
	public boolean stopped;

	public FallingBlock(char shape) {
		super(shape);
		rotated = false;
		stopped = false;
		dataMatrixX = GameBoard.matrix.getWidth()/2-1;
		if(dataMatrixY == mark) dataMatrixY = 0;
	}

	public void rotate(){
		clean();
		if(rotatesBack){
			rotateMatrix(dataMatrix,rotated);
			if(rotated) rotated = false;
			else rotated = true;
		}
		else rotateMatrix(dataMatrix,false);
		controlLoop:
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=0; j<dataMatrix[0].length; j++){
					if((dataMatrix[i][j] instanceof Color) && 
							((dataMatrixX+j>=GameBoard.matrix.getWidth())|| (dataMatrixX<0) 
									|| (dataMatrixY+i>=GameBoard.matrix.getHeight()-1)|| (dataMatrixY<0)
									|| (GameBoard.matrix.get(dataMatrixY+i,dataMatrixX+j) instanceof Color)))
					{

						if(rotatesBack){
							rotateMatrix(dataMatrix,rotated);
							if(rotated) rotated = false;
							else rotated = true;
						}else rotateMatrix(dataMatrix,true);
						rotated = false;
						break controlLoop;
					}
				}
			}
		put();

	}

	public void goLeft(){
		boolean canGoLeft = true;
		int firstYIndex = 10;
		int lastYIndex = 0;
		for(int i=0; i<dataMatrix.length; i++){
			for(int j=0; j<dataMatrix[0].length; j++)
				if(dataMatrix[i][j] instanceof Color){
					firstYIndex = Math.min(i,firstYIndex);
					lastYIndex = Math.max(i,lastYIndex);
				}
		}
		for(int i=firstYIndex; i<=lastYIndex; i++){
			if(isLeftFull(i)){
				canGoLeft = false;
				break;
			}
		}

		if(canGoLeft){
			clean();
			dataMatrixX--;
			put();
		}
	}

	boolean isLeftFull(int i){
		int firstXIndex = 0;
		for(int j=0; j<dataMatrix.length; j++){
			if(dataMatrix[i][j] instanceof Color){
				firstXIndex = j;
				break;
			}
		}
		if((dataMatrixX+firstXIndex==0)){
			return true;
		}
		if((GameBoard.matrix.get(dataMatrixY+i,dataMatrixX+firstXIndex-1) instanceof Color)){
			return true;
		}
		else return false;
	}

	public void goRight(){

		boolean canGoRight = true;
		int firstYIndex = 10;
		int lastYIndex = 0;
		for(int i=0; i<dataMatrix.length; i++){
			for(int j=0; j<dataMatrix[0].length; j++)
				if(dataMatrix[i][j] instanceof Color){
					firstYIndex = Math.min(i,firstYIndex);
					lastYIndex = Math.max(i,lastYIndex);
				}
		}
		for(int i=firstYIndex; i<=lastYIndex; i++){
			if(isRightFull(i)){
				canGoRight = false;
				break;
			}
		}

		if(canGoRight){
			clean();
			dataMatrixX++;
			put();
		}
	}

	boolean isRightFull(int i){
		int lastXIndex = 0;
		for(int j=0; j<dataMatrix.length; j++){
			if(dataMatrix[i][j] instanceof Color){
				lastXIndex = j;
			}
		}
		if((dataMatrixX+lastXIndex==GameBoard.matrix.getWidth()-1)){
			return true;
		}
		if(GameBoard.matrix.get(dataMatrixY+i,dataMatrixX+lastXIndex+1) instanceof Color){
			return true;
		}else return false;
	}

	boolean isUnderFull(int j){
		int lastYIndex = -1;
		for(int i=0; i<dataMatrix.length; i++){
			if(dataMatrix[i][j] instanceof Color){
				lastYIndex = i;
			}
		}
		if(lastYIndex!=-1&&(dataMatrixY+lastYIndex==GameBoard.matrix.getHeight()-1)){
			return true;
		}else if(lastYIndex!=-1&&GameBoard.matrix.get(dataMatrixY+lastYIndex+1,dataMatrixX+j) instanceof Color){
			return true;
		}else return false;
	}

	public void fall(){
		boolean canGoDown = true;
		for(int j=0; j<dataMatrix[0].length; j++){
			if(isUnderFull(j)){
				canGoDown = false;
				stop();
				break;
			}
		}

		if(canGoDown){
			clean();
			dataMatrixY++;
			put();
		}
	}

	//Processes the tetriminos' data into the gameboard, puts the tetriminos into the gameboard in other words
	public void put(){

		//Right
		if(dataMatrixX+dataMatrix[0].length-1>=GameBoard.matrix.getWidth()){
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=0; j<GameBoard.matrix.getWidth()-dataMatrixX; j++){
					if(dataMatrix[i][j] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,dataMatrixX+j,getColor());
					}
				}
			}

		}

		//Left
		else if(dataMatrixX < 0){
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=-dataMatrixX; j<dataMatrixX + dataMatrix[0].length+1; j++){
					if(dataMatrix[i][j] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,dataMatrixX+j,getColor());
					}
				}
			}
		}

		//Bottom
		else if(dataMatrixY+dataMatrix.length-1>=GameBoard.matrix.getHeight()){
			for(int i=0; i<GameBoard.matrix.getHeight()-dataMatrixY; i++){
				for(int j=0; j<dataMatrix[0].length; j++){
					if(dataMatrix[i][j] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,dataMatrixX+j,getColor());
					}
				}
			}

		}

		//Middle
		else{
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=0; j<dataMatrix[0].length; j++){
					if(dataMatrix[i][j] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,dataMatrixX+j,getColor());
					}
				}
			}
		}	
	}

	public void clean(){
		//Right
		if(dataMatrixX+dataMatrix[0].length-1>=GameBoard.matrix.getWidth()){
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=dataMatrixX; j<GameBoard.matrix.getWidth(); j++){
					if(dataMatrix[i][j-dataMatrixX] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,j,null);
					}
				}
			}

		}

		//Left
		else if(dataMatrixX < 0){
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=0; j<dataMatrixX + dataMatrix[0].length; j++){
					if(dataMatrix[i][j-dataMatrixX] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,j,null);
					}
				}
			}
		}

		//Bottom
		else if(dataMatrixY+dataMatrix.length-1>=GameBoard.matrix.getHeight()){
			for(int i=dataMatrixY; i<GameBoard.matrix.getHeight(); i++){
				for(int j=0; j<dataMatrix[0].length; j++){
					if(dataMatrix[i-dataMatrixY][j] instanceof Color){
						GameBoard.matrix.set(i,dataMatrixX+j,null);
					}
				}
			}

		}

		//Middle
		else{
			for(int i=0; i<dataMatrix.length; i++){
				for(int j=0; j<dataMatrix[0].length; j++){
					if(dataMatrix[i][j] instanceof Color){
						GameBoard.matrix.set(dataMatrixY+i,dataMatrixX+j,null);
					}
				}
			}
		}
	}

	public void stop(){
		stopped = true;
		if(GameWindow.board.mode)GameWindow.board.eventListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
			//Nothing need go here, the actionPerformed method (with the
			//above arguments) will trigger the respective listener
		});
	}

	private void transpose(Color[][] m) {

		for (int i = 0; i < m.length; i++) {
			for (int j = i; j < m[0].length; j++) {
				Color x = m[i][j];
				m[i][j] = m[j][i];
				m[j][i] = x;
			}
		}
	}

	private void rotateMatrix(Color[][] m, boolean counterClockwise){
		// transpose
		transpose(m);

		if(counterClockwise){
			//counter clockwise
			//swap rows
			for (int  i = 0; i < m.length/2; i++) {
				for (int j = 0; j < m[0].length; j++) {
					Color x = m[i][j];
					m[i][j] = m[m.length -1 -i][j]; 
					m[m.length -1 -i][j] = x;
				}
			}
		}else{
			//clockwise
			// swap columns
			for (int  j = 0; j < m[0].length/2; j++) {
				for (int i = 0; i < m.length; i++) {
					Color x = m[i][j];
					m[i][j] = m[i][m[0].length -1 -j]; 
					m[i][m[0].length -1 -j] = x;
				}
			}
		}
	}

	public String toString(){
		String res="";
		for(int i=0; i<dataMatrix.length; i++){
			for(int j=0; j<dataMatrix[0].length; j++){
				res+=dataMatrix[i][j]+" ";
			}
			res+="\n";
		}
		return res;
	}
}
