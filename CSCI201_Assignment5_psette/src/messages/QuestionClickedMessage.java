package messages;

public class QuestionClickedMessage implements Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7152921479190518378L;
	private int xIndex;
	private int yIndex;
	
	public QuestionClickedMessage(int x, int y){
		this.xIndex = x;
		this.yIndex = y;
	}
	
	public int getX(){
		return xIndex;
	}
	
	public int getY(){
		return yIndex;
	}
}
