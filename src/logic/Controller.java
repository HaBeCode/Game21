package logic;
import java.net.URL;

import javax.swing.ImageIcon;
import data.Card;

public class Controller {
	
	private Card[] mydeck;
	private Card[] submitted;
	private Card[][] fieldDeck;
	private String player;
	private int turn = 0;
	private int cSubmitted;
	private Card c1;
	private Card c2;
	private Card c3;
	private final static String Pass = "SMU2015";
	
	public void draw(){			
		int ti;
		if (turn%2==0){
			ti = 0;
		} else {
			ti = 1;
		}
		addCard(ti, mydeck[turn]);
		turn++;
	}
	
	public boolean enterPassword(String pPass) {
		if (Pass.equals(pPass)) {
			return true;
		}
		return false;
	}
	
	private void addCard(int pi, Card pCard){
		int tmp = 0;
		while (fieldDeck[pi][tmp] != null){
			tmp++;
		}
		fieldDeck[pi][tmp] = pCard;
	}
	
	public int getTurn(){
		return turn;
	}
	public String getPlayer(){
		return player;
	}
	public void setPlayer(String pPlayer){
		player = pPlayer;
	}
	
	//To-Do change how to save submitted Cards
	public void submit(Card p1, Card p2, Card p3, int numberPC){
		submitted[cSubmitted] = p1;
		submitted[cSubmitted + 1] = p2;
		submitted[cSubmitted + 2] = p3;
		cSubmitted = cSubmitted + 3;

		if (numberPC != 0) {
			updateComputerCards(p1 ,p2, p3, numberPC);
		}
		deleteCard(p1,p2,p3, 3 - numberPC, 1);
		orderField(1);
	}
	
	//when stolen or computer turn;
	private void updateComputerCards(Card p1, Card p2, Card p3, int pNumber){
		deleteCard(p1,p2,p3, pNumber, 0);
		orderField(0);
	}
	
	//order fieldDeck from computer or player after deleting cards
	private void orderField(int pPlayer){
		for (int i = 0; i < 26; i++) {
			if (fieldDeck[pPlayer][i] == null) {
				if (orderElement(pPlayer, i)) {
					return;
				}
			}
		}
	}
	
	//called by orderField
	private boolean orderElement(int pPlayer, int pNew) {
		for (int i = pNew; i < 26; i++) {
			if (fieldDeck[pPlayer][i] != null) {
				fieldDeck[pPlayer][pNew] = fieldDeck[pPlayer][i];
				fieldDeck[pPlayer][i] = null;
				return false;
			}
		}
		return true;
	}
	
	private void deleteCard(Card p1, Card p2, Card p3, int pCounter, int pPlayer) {
		int tmpCounter = 0;
		int x = 0;
		while (tmpCounter != pCounter) {
			if (fieldDeck[pPlayer][x] != null) {
				if (fieldDeck[pPlayer][x].equals(p1) || fieldDeck[pPlayer][x].equals(p2) || fieldDeck[pPlayer][x].equals(p3)) {
					fieldDeck[pPlayer][x] = null;
					tmpCounter++;
				}
			}
			x++;
		}
	}
	
	public Card[][] getFields(){
		return fieldDeck;
	}
	
	public void finish(){
		
	}
	
	public boolean calculate(){
		//int ti = 0;
		return false;
	}
	
	public void startKI(){
		
		if (calculate()){
			deleteCard(c1, c2, c3, 3, 0);
			c1 = null;
			c2 = null;
			c3 = null;
		}
		
	}

	public Controller(){
		player = "Player 2";
		mydeck = new Card[52];
		submitted = new Card[52];
		fieldDeck = new Card[2][26];
		cSubmitted = 0;
		c1 = null;
		c2 = null;
		c3 = null;
		
		ImageIcon i1 = new ImageIcon(Card.class.getClassLoader().getResource("image/1.jpg"));
		ImageIcon i2 = new ImageIcon(Card.class.getClassLoader().getResource("image/2.jpg"));
		ImageIcon i3 = new ImageIcon(Card.class.getClassLoader().getResource("image/3.jpg"));
		ImageIcon i4 = new ImageIcon(Card.class.getClassLoader().getResource("image/4.jpg"));
		ImageIcon i5 = new ImageIcon(Card.class.getClassLoader().getResource("image/5.jpg"));
		ImageIcon i6 = new ImageIcon(Card.class.getClassLoader().getResource("image/6.jpg"));
		ImageIcon i7 = new ImageIcon(Card.class.getClassLoader().getResource("image/7.jpg"));
		ImageIcon i8 = new ImageIcon(Card.class.getClassLoader().getResource("image/8.jpg"));
		ImageIcon i9 = new ImageIcon(Card.class.getClassLoader().getResource("image/9.jpg"));
		ImageIcon i10 = new ImageIcon(Card.class.getClassLoader().getResource("image/10.jpg"));
		ImageIcon ij = new ImageIcon(Card.class.getClassLoader().getResource("image/j.jpg"));
		ImageIcon iq = new ImageIcon(Card.class.getClassLoader().getResource("image/q.jpg"));
		ImageIcon ik = new ImageIcon(Card.class.getClassLoader().getResource("image/k.jpg"));
		
		mydeck[0] = new Card(7, i7);
		mydeck[1] = new Card(1, i1);
		mydeck[2] = new Card(9, i9);  
		mydeck[3] = new Card(1, i1);
		mydeck[4] = new Card(2, i2);
		mydeck[5] = new Card(5, i5);
		mydeck[6] = new Card(9, i9);
		mydeck[7] = new Card(6, i6);
		mydeck[8] = new Card(1, i1);
		mydeck[9] = new Card(10, i10);
		mydeck[10] = new Card(4, i4);
		mydeck[11] = new Card(4, i4);
		mydeck[12] = new Card(8, i8);
		mydeck[13] = new Card(5, i5);
		mydeck[14] = new Card(3, i3);
		mydeck[15] = new Card(10, i10);
		mydeck[16] = new Card(4, i4);
		mydeck[17] = new Card(10, i10);
		mydeck[18] = new Card(10, i10);
		mydeck[19] = new Card(7, i7);
		mydeck[20] = new Card(6, i6);
		mydeck[21] = new Card(2, i2);
		mydeck[22] = new Card(5, i5);
		mydeck[23] = new Card(8, i8);
		mydeck[24] = new Card(10, ik);
		mydeck[25] = new Card(10, iq);
		mydeck[26] = new Card(3, i3);
		mydeck[27] = new Card(10, ij);
		mydeck[28] = new Card(10, ik);
		mydeck[29] = new Card(6, i6);
		mydeck[30] = new Card(5, i5);
		mydeck[31] = new Card(7, i7);
		mydeck[32] = new Card(2, i2);
		mydeck[33] = new Card(8, i8);
		mydeck[34] = new Card(6, i6);
		mydeck[35] = new Card(3, i3);
		mydeck[36] = new Card(1, i1);
		mydeck[37] = new Card(3, i3);
		mydeck[38] = new Card(10, i10);
		mydeck[39] = new Card(2, i2);
		mydeck[40] = new Card(10, i10);
		mydeck[41] = new Card(4, i4);
		mydeck[42] = new Card(7, i7);
		mydeck[43] = new Card(9, i9);
		mydeck[44] = new Card(10, i10);
		mydeck[45] = new Card(10, ik);
		mydeck[46] = new Card(10, ik);
		mydeck[47] = new Card(10, iq);
		mydeck[48] = new Card(10, ij);
		mydeck[49] = new Card(10, iq);
		mydeck[50] = new Card(8, i8);
		mydeck[51] = new Card(9, i9);
	}
}
