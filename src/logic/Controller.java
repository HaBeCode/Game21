package logic;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import data.Card;
 
public class Controller {
       
        private Card[] mydeck;
        private String[] submitted;
        private String[] pcSubmitted;
        private Card[][] fieldDeck;
        private String player;
        private int turn;
        private int cSubCard;
        private int cSubmitted;
        private int cPcSubmitted;
        private Card[] stolenCard;
        private final static String Pass = "SMU2015";
        private String[] tmpSubmit;
        private Double ComPoints;
        
        ImageIcon i1;
        ImageIcon i2;
        ImageIcon i3;
        ImageIcon i4;
        ImageIcon i5;
        ImageIcon i6;
        ImageIcon i7;
        ImageIcon i8;
        ImageIcon i9;
        ImageIcon i10;
        ImageIcon ij;
        ImageIcon iq;
        ImageIcon ik;

       
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

                stolenCard[0] = null;
                stolenCard[1] = null;
                stolenCard[2] = null;
                cSubCard = 0;
 
                if (numberPC != 0) {
                        updateComputerCards(p1 ,p2, p3, numberPC);
                }
                deleteCard(p1,p2,p3, 3 - numberPC, 1);
                orderField(1);
                submitted[cSubmitted] = Integer.toString(turn/2) + ":";
                cSubmitted++;
                for (int i = 0; i < numberPC; i++) {
                    submitted[cSubmitted] = "(" + getSignOfCard(stolenCard[i]) + "),";
                    cSubmitted++;               	
                }
                for (int i=numberPC;i < 3; i++) {
                    String tmp =  getSignOfCard(stolenCard[i]);
                    if (i==2) {
                    	tmp = tmp + ";";
                    } else {
                    	tmp = tmp + ",";
                    }
                    submitted[cSubmitted] = tmp;
                    cSubmitted++;                	
                }
                

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
                                        stolenCard[cSubCard] = fieldDeck[pPlayer][x];
                                        cSubCard++;
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
        
        public void clearFields(){
        	fieldDeck = new Card[2][26];
        }
       
        public String finish(Double pPoints){
               StringBuilder sb = new StringBuilder();
               sb.setLength(0);
               sb.append(player).append(";Points: ").append(pPoints).append(";[");
               for (int i = 0; i<cSubmitted;i++) {
            	   sb.append(submitted[i]);
               }
               sb.append("]").append("\n");
               
               sb.append("Computer;Points: " + ComPoints).append(";[");
               for (int i=0; i<cPcSubmitted;i++) {
            	   sb.append(pcSubmitted[i]);
               }
               sb.append("]");
               return sb.toString();
        }
       
        //called by startKI
        private int checkCards(int pValue1, int pValue2, int pSize) {
        	int tmpSum = fieldDeck[0][pValue1].getValue() + fieldDeck[0][pValue2].getValue();
            for (int i = pValue2+1; i < pSize; i++) {
            	if (tmpSum + fieldDeck[0][i].getValue() == 21) {
            		return i;
                }
            }
            return 0;
        }
        
        private int checkPlayerCards(int pValue1) {
        	
        	int tmpSize = getCardNumber(1);
        	
            for (int i = 0; i < tmpSize;i++) {
            	if (pValue1 + fieldDeck[1][i].getValue() == 21) {
            		return i;
            	}
            }
            return 0;
        }
        
        private int[] check2PlayerCards(int pValue1) {
        	int[] cards = new int[2];
        	int tmpSize = getCardNumber(1);
        	for (int i = 0; i < tmpSize; i++){
        		for (int j = i+1; j < tmpSize; j++){
        			if (pValue1 + fieldDeck[1][i].getValue() + fieldDeck[1][j].getValue() == 21){
        				cards[0] = i;
        				cards[1] = j;
        				return cards;
        			}
        		}
        	}
        	cards[0] = -1;
        	return cards;
        }
       
        public int getCardNumber(int index) {
                for(int i = 0; i<26; i++) {
                         if(fieldDeck[index][i] == null) {
                         return i;
                         }
                }
                return 0;
        }
       
        public boolean startKI(){
            int size = getCardNumber(0);
            int tmpValue;
            cSubCard = 0;
            int dialogResult = 0;
            int[] playerCards = new int[2];
            
            for (int tIndex = 0; tIndex < 3; tIndex++) {
	        	if (tIndex == 0){
	                for (int i = 0; i < size - 2; i++) {
	                	for (int i2 = i+1; i2 < size-1; i2++) {
	                		tmpValue = checkCards(i, i2, size);
	                		if (tmpValue != 0) {
	                			writeCards(0, i, 0, i2, 0, tmpValue);
	                			updateComputerCards(fieldDeck[0][i], fieldDeck[0][i2],fieldDeck[0][tmpValue], 2);
	                			ComPoints += 1.0;
	                			return true;
	                		}
	                	}
	                }
	        	} else if (tIndex == 1){
	                for (int i = 0; i < size - 2; i++) {
	                	for (int i2 = i+1; i2 < size-1; i2++) {
	                		tmpValue = checkPlayerCards(fieldDeck[0][i].getValue() + fieldDeck[0][i2].getValue());
	                    	if(tmpValue != 0) {
	                    	   	dialogResult = JOptionPane.showConfirmDialog (null, "Do you allow other player to take your card " + getSignOfCard(fieldDeck[1][tmpValue]) + " ?","Warning", JOptionPane.YES_NO_OPTION);
	                    	   	if(dialogResult == JOptionPane.NO_OPTION)
	                    	   		return false;
	                    	   	else {
	                    	   		writeCards(0, i, 0, i2, 1, tmpValue);
	                            	deleteCard(fieldDeck[0][i], fieldDeck[0][i2],fieldDeck[1][tmpValue],2,0);
	                            	deleteCard(fieldDeck[0][i], fieldDeck[0][i2],fieldDeck[1][tmpValue],1,1);
	                            	orderField(0);
	                            	orderField(1);
	                    	   		ComPoints += 0.5;
	                    	   		return true;
	                    	   	}
	                    	}
	                	}
	                }
	        	} else if (tIndex == 2){
	                for (int i = 0; i < size - 2; i++) {
	                	playerCards = check2PlayerCards(fieldDeck[0][i].getValue());
	                	if (playerCards[0] != -1){
	                	   	dialogResult = JOptionPane.showConfirmDialog (null, "Do you allow other player to take your card " + getSignOfCard(fieldDeck[1][playerCards[0]]) + " and " + getSignOfCard(fieldDeck[1][playerCards[1]]) + " ?","Warning", JOptionPane.YES_NO_OPTION);
	                	   	if(dialogResult == JOptionPane.NO_OPTION)
	                	   		return false;
	                	   	else {
	                	   		writeCards(0, i, 1, playerCards[0], 1, playerCards[1]);
	                        	deleteCard(fieldDeck[0][i], fieldDeck[1][playerCards[0]],fieldDeck[1][playerCards[1]],1,0);
	                        	deleteCard(fieldDeck[0][i], fieldDeck[1][playerCards[0]],fieldDeck[1][playerCards[1]],2,1);
	                        	orderField(0);
	                        	orderField(1);
	                        	orderField(0);
	                        	orderField(1);
	                	   		ComPoints += 0.5;
	                	   		return true;
	                	   	}
	                	} 
	                } 
	        	}
            }
        	return false;
        }
        private void writeCards(int x0, int y0, int x1, int y1, int x2, int y2){
        	
        	tmpSubmit[0] = getSignOfCard(fieldDeck[x0][y0]);
            tmpSubmit[1] = getSignOfCard(fieldDeck[x1][y1]);
            tmpSubmit[2] = getSignOfCard(fieldDeck[x2][y2]);
                		
            pcSubmitted[cPcSubmitted] = Integer.toString(turn/2 + 1) + ":";
            pcSubmitted[cPcSubmitted + 1] = tmpSubmit[0] + ",";
            if (x1 == 0)
            	pcSubmitted[cPcSubmitted + 2] = tmpSubmit[1] + ",";
            else
            	pcSubmitted[cPcSubmitted + 2] = "(" + tmpSubmit[1] + "),";
            if (x2 == 0)
            	pcSubmitted[cPcSubmitted + 3] = tmpSubmit[2] + ";";
            else
            	pcSubmitted[cPcSubmitted + 3] = "(" + tmpSubmit[2] + ");";
            cPcSubmitted = cPcSubmitted + 4;
        }
            	
        public String[] getComputerSubmit(){
        	return tmpSubmit;
        }
        
        public String getSignOfCard(Card pCard) {
        	if (pCard.getPicture().equals(ij)){
        		return "J";
        	} else if (pCard.getPicture().equals(iq)){
        		return "Q";
        	} else if (pCard.getPicture().equals(ik)){
        		return "K";
        	} else if (pCard.getPicture().equals(i1)){
        		return "A";
        	}
        	return Integer.toString(pCard.getValue());
        }
        
        public double getComPoints(){
        	return ComPoints;
        }
        
        public void initController(){
        	turn = 0;
        	cPcSubmitted = 0;
        	ComPoints = 0.0;
            player = "Player 2";
            mydeck = new Card[52];
            pcSubmitted = new String[76];
            submitted = new String[76];
            fieldDeck = new Card[2][26];
            cSubmitted = 0;
            stolenCard = new Card[3];
            tmpSubmit = new String[3];
            mydeck[0] = new Card(10, ij);
            mydeck[1] = new Card(3, i3);
            mydeck[2] = new Card(1, i1); 
            mydeck[3] = new Card(9, i9);
            mydeck[4] = new Card(6, i6);
            mydeck[5] = new Card(9, i9);
            mydeck[6] = new Card(4, i4);
            mydeck[7] = new Card(6, i6);
            mydeck[8] = new Card(10, iq);
            mydeck[9] = new Card(1, i1);
            mydeck[10] = new Card(2, i2);
            mydeck[11] = new Card(7, i7);
            mydeck[12] = new Card(10, iq);
            mydeck[13] = new Card(7, i7);
            mydeck[14] = new Card(2, i2);
            mydeck[15] = new Card(10, i10);
            mydeck[16] = new Card(10, ij);
            mydeck[17] = new Card(5, i5);
            mydeck[18] = new Card(7, i7);
            mydeck[19] = new Card(10, ik);
            mydeck[20] = new Card(10, ik);
            mydeck[21] = new Card(10, i10);
            mydeck[22] = new Card(4, i4);
            mydeck[23] = new Card(3, i3);
            mydeck[24] = new Card(4, i4);
            mydeck[25] = new Card(10, iq);
            mydeck[26] = new Card(10, i10);
            mydeck[27] = new Card(9, i9);
            mydeck[28] = new Card(1, i1);
            mydeck[29] = new Card(10, i10);
            mydeck[30] = new Card(10, ij);
            mydeck[31] = new Card(10, ik);
            mydeck[32] = new Card(1, i1);
            mydeck[33] = new Card(10, ik);
            mydeck[34] = new Card(5, i5);
            mydeck[35] = new Card(10, iq);
            mydeck[36] = new Card(2, i2);
            mydeck[37] = new Card(4, i4);
            mydeck[38] = new Card(5, i5);
            mydeck[39] = new Card(8, i8);
            mydeck[40] = new Card(6, i6);
            mydeck[41] = new Card(8, i8);
            mydeck[42] = new Card(3, i3);
            mydeck[43] = new Card(7, i7);
            mydeck[44] = new Card(10, ij);
            mydeck[45] = new Card(9, i9);
            mydeck[46] = new Card(2, i2);
            mydeck[47] = new Card(3, i3);
            mydeck[48] = new Card(8, i8);
            mydeck[49] = new Card(5, i5);
            mydeck[50] = new Card(8, i8);
            mydeck[51] = new Card(6, i6);        	
        }
 
        public Controller(){
        	initPicture();
        	initController();
        }
        
        private void initPicture(){
            i1 = new ImageIcon(Card.class.getClassLoader().getResource("image/1.jpg"));
            i2 = new ImageIcon(Card.class.getClassLoader().getResource("image/2.jpg"));
            i3 = new ImageIcon(Card.class.getClassLoader().getResource("image/3.jpg"));
            i4 = new ImageIcon(Card.class.getClassLoader().getResource("image/4.jpg"));
            i5 = new ImageIcon(Card.class.getClassLoader().getResource("image/5.jpg"));
            i6 = new ImageIcon(Card.class.getClassLoader().getResource("image/6.jpg"));
            i7 = new ImageIcon(Card.class.getClassLoader().getResource("image/7.jpg"));
            i8 = new ImageIcon(Card.class.getClassLoader().getResource("image/8.jpg"));
            i9 = new ImageIcon(Card.class.getClassLoader().getResource("image/9.jpg"));
            i10 = new ImageIcon(Card.class.getClassLoader().getResource("image/10.jpg"));
            ij = new ImageIcon(Card.class.getClassLoader().getResource("image/j.jpg"));
            iq = new ImageIcon(Card.class.getClassLoader().getResource("image/q.jpg"));
            ik = new ImageIcon(Card.class.getClassLoader().getResource("image/k.jpg"));
        }
}