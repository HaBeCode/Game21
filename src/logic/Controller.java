package logic;
import javax.swing.ImageIcon;
import data.Card;

public class Controller {
	
	private Card[] mydeck;
	private Card[] submitted;
	private Card[][] fieldDeck;
	private String player;
	private int turn = 0;
	private int cSubmitted;
	
	public void draw(){			
		turn++;
		int ti;
		if (turn%2==1){
			ti = 0;
		} else {
			ti = 1;
		}
		addCard(ti, mydeck[turn]);
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
	
	//do-to: more
	public void submit(Card p1, Card p2, Card p3){
		submitted[cSubmitted] = p1;
		submitted[cSubmitted + 1] = p2;
		submitted[cSubmitted + 2] = p3;
		cSubmitted = cSubmitted + 3;
		int tmpCounter = 0;
		while (tmpCounter != 3) {
			//do more
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
			//remove Cards from areaDeck
		}
		
	}

	public Controller(){
		player = "Player 2";
		mydeck = new Card[52];
		submitted = new Card[52];
		fieldDeck = new Card[2][26];
		cSubmitted = 0;
		
		ImageIcon tmp = new ImageIcon("image/ace.png");
		
		mydeck[0] = new Card(7, tmp);
		mydeck[1] = new Card(1, tmp);
		mydeck[2] = new Card(9, tmp);  
		mydeck[3] = new Card(1, tmp);
		mydeck[4] = new Card(2, tmp);
		mydeck[5] = new Card(5, tmp);
		mydeck[6] = new Card(9, tmp);
		mydeck[7] = new Card(6, tmp);
		mydeck[8] = new Card(1, tmp);
		mydeck[9] = new Card(10, tmp);
		mydeck[10] = new Card(4, tmp);
		mydeck[11] = new Card(4, tmp);
		mydeck[12] = new Card(8, tmp);
		mydeck[13] = new Card(5, tmp);
		mydeck[14] = new Card(3, tmp);
		mydeck[15] = new Card(10, tmp);
		mydeck[16] = new Card(4, tmp);
		mydeck[17] = new Card(10, tmp);
		mydeck[18] = new Card(10, tmp);
		mydeck[19] = new Card(7, tmp);
		mydeck[20] = new Card(6, tmp);
		mydeck[21] = new Card(2, tmp);
		mydeck[22] = new Card(5, tmp);
		mydeck[23] = new Card(8, tmp);
		mydeck[24] = new Card(10, tmp);
		mydeck[25] = new Card(10, tmp);
		mydeck[26] = new Card(3, tmp);
		mydeck[27] = new Card(10, tmp);
		mydeck[28] = new Card(10, tmp);
		mydeck[29] = new Card(6, tmp);
		mydeck[30] = new Card(5, tmp);
		mydeck[31] = new Card(7, tmp);
		mydeck[32] = new Card(2, tmp);
		mydeck[33] = new Card(8, tmp);
		mydeck[34] = new Card(6, tmp);
		mydeck[35] = new Card(3, tmp);
		mydeck[36] = new Card(1, tmp);
		mydeck[37] = new Card(3, tmp);
		mydeck[38] = new Card(10, tmp);
		mydeck[39] = new Card(2, tmp);
		mydeck[40] = new Card(10, tmp);
		mydeck[41] = new Card(4, tmp);
		mydeck[42] = new Card(7, tmp);
		mydeck[43] = new Card(9, tmp);
		mydeck[44] = new Card(10, tmp);
		mydeck[45] = new Card(10, tmp);
		mydeck[46] = new Card(10, tmp);
		mydeck[47] = new Card(10, tmp);
		mydeck[48] = new Card(10, tmp);
		mydeck[49] = new Card(10, tmp);
		mydeck[50] = new Card(8, tmp);
		mydeck[51] = new Card(9, tmp);
	}
}
