package data;

import javax.swing.ImageIcon;

public class Card {

	private int value;
	private ImageIcon picture;
	
	public Card(int pvalue, ImageIcon ppicture){
		value = pvalue;
		picture = ppicture;
	}
	
	public int getValue(){
		return value;
	}
	
	public ImageIcon getPicture(){
		return picture;
	}

}
