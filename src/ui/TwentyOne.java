package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.Border;

import data.Card;
import logic.Controller;


//JProgressbar
public class TwentyOne extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel play;
	private JPanel control;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem newGame;
	private JMenuItem closeGame;
	private Image background;
	private JProgressBar pbar;

	private JLabel lcounter;
	private JLabel lplayer;
	private JLabel lmoney;
	private JLabel lpcmoney;
	private JLabel lDeck;
	private JLabel[] lPicture;
	private double money;
	private int cPicture;
	private int cFieldPicture;
	private int cValue;
	private int cSteal;
	private Card[] value;
	private JButton bFinish;
	private JButton bDraw;
	private JButton bSubmit;
	private JButton bEnd;
	private Border sborder;
	private Border uborder;
	
	private static Controller mycontroller;
	private Card[][] field;
	private boolean drawed;
	private boolean submitted;
	
	private JTextArea textHistory;

	public static void main(String[] args){
		new TwentyOne();
	}
	
	public TwentyOne() {
		
		mycontroller = new Controller();
		field = new Card[2][26];
		lPicture = new JLabel[52];
		sborder = BorderFactory.createLineBorder(Color.blue, 2);
		uborder = BorderFactory.createLineBorder(Color.gray, 2);
		
		frame = new JFrame("Game 21");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		background = new ImageIcon(Card.class.getClassLoader().getResource("image/grey-background.jpg")).getImage();
		
		control = new JPanel(new GridBagLayout());
		play = new JPanel(new GridBagLayout());
		
		JPanel playLayout = new JPanel(new BorderLayout()) {
			@Override
			  protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			        g.drawImage(background, 0, 0, null);
			}
		};
		
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		newGame = new JMenuItem("New Game");
		menu.add(newGame);
		newGame.addActionListener(this);
		closeGame = new JMenuItem("Close");
		menu.add(closeGame);
		closeGame.addActionListener(this);
		menuBar.add(menu);
		
		textHistory = new JTextArea(8, 15);
		textHistory.setText("Card Submission History:");
		textHistory.setEditable(false);
		
		lcounter = new JLabel("");
		lmoney = new JLabel("");
		lpcmoney = new JLabel("");
		lDeck = new JLabel(getCard());
		pbar = new JProgressBar();
		pbar.setMinimumSize(new Dimension(400,200));
		pbar.setPreferredSize(new Dimension(400,200));
		bDraw = new JButton("Draw");
		bSubmit = new JButton("Submit");
		bFinish = new JButton("Finish");
		bEnd = new JButton("Pass");
		bDraw.setEnabled(false);
		bSubmit.setEnabled(false);
		bFinish.setEnabled(false);
		bEnd.setEnabled(false);
		bDraw.addActionListener(this);
		bSubmit.addActionListener(this);
		bFinish.addActionListener(this);
		bEnd.addActionListener(this);

		initGame();
		paintControl();
		
		JLabel lcomputer = new JLabel("Other");
		lplayer = new JLabel("You");
		lcomputer.setForeground(Color.WHITE);
		lplayer.setForeground(Color.WHITE);
		lcomputer.setFont(new Font("Arial", Font.PLAIN, 20));
		lplayer.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JPanel layoutLabelPlayer = new JPanel(new GridBagLayout());
		GridBagConstraints tmpc = new GridBagConstraints();
		tmpc.gridx = 0;
		tmpc.gridy = 0;
		
		layoutLabelPlayer.add(lcomputer, tmpc);
		tmpc.gridy = 1;
		layoutLabelPlayer.add(new JLabel(" "), tmpc);
		tmpc.gridy= 2;
		layoutLabelPlayer.add(lplayer, tmpc);
		
		playLayout.add(layoutLabelPlayer, BorderLayout.WEST);
		playLayout.add(pbar, BorderLayout.NORTH);
		
		playLayout.add(play, BorderLayout.CENTER);
		
		frame.add(control, BorderLayout.WEST);
		frame.add(playLayout, BorderLayout.CENTER);
		frame.setJMenuBar(menuBar);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
	}
	
	private void initGame(){
		cPicture = 0;
		cFieldPicture = 0;
		cValue = 0;
		cSteal = 0;
		money = 0.0;
		value = new Card[3];
		textHistory.setText("");
		drawed = false;
		submitted = false;	
		mycontroller.clearFields();
		lPicture = new JLabel[52];
		bEnd.setEnabled(false);
	}
	
	private static boolean initAdmin () {
		
		Box box = Box.createHorizontalBox();
	
		JLabel jl = new JLabel("Admin Password: ");
		box.add(jl);
	
		JPasswordField jpf = new JPasswordField(24);
		box.add(jpf);
	
		int button = JOptionPane.showConfirmDialog(null, box, "Enter your password", JOptionPane.OK_CANCEL_OPTION);
	
		if (button == JOptionPane.OK_OPTION) {
			char[] input = jpf.getPassword();
			if (mycontroller.enterPassword(new String(input))) {
				return true;
			}
		}
		return false;
		
	}
	
	public void actionPerformed (ActionEvent ae){
		if (ae.getSource() == this.bDraw) {
			drawCard();
			clearSelection();
			drawed=true;
			bEnd.setEnabled(true);
		}
		else if (ae.getSource() == this.bEnd){
			endTurn();
		}
		else if (ae.getSource() == this.bSubmit) {
			submitted = false;
			submit();
			if (submitted) {
				bSubmit.setEnabled(false);
				if (mycontroller.getTurn()==52) {
					try {
						finish();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					control.removeAll();
					endTurn();
				}
			} else {
				clearSelection();
			}
		}
		else if (ae.getSource() == this.bFinish) {
			try {
				finish();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else if (ae.getSource() == this.newGame) {
			if (!initAdmin()) {
				return;
			}
			initGame();
			mycontroller.initController();
			while (mycontroller.getPlayer().equals("Player 2")) {
				String tmp = "a";
				tmp = (String) JOptionPane.showInputDialog("Please enter your code:");
				if (!tmp.equals(Integer.toString(JOptionPane.CANCEL_OPTION))) {
					mycontroller.setPlayer(tmp);
				} 
			}
			drawCard();
			bDraw.setEnabled(true);
			bEnd.setEnabled(false);
		}
		else if (ae.getSource() == this.closeGame) {
			System.exit(0);
		}
		paintField();
		paintControl();
		frame.repaint();
	}
	
	private void endTurn(){

		bEnd.setEnabled(false);
		pbar.setIndeterminate(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					Thread.sleep(1500);
					drawCard();
					paintField();
					frame.repaint();
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (mycontroller.startKI()) {
					String[] tmpCards = mycontroller.getComputerSubmit();
					textHistory.append("\nPlayer1: " + tmpCards[0] + ", " + tmpCards[1] + ", " + tmpCards[2]);
				}
				
				drawed = false;
				bDraw.setEnabled(true);
				clearSelection();
				
				paintField();
				paintControl();
				frame.repaint();
				pbar.setIndeterminate(false);
			}
		}).start();
		
	}
	
	public void drawCard(){
		int tmpTurn = mycontroller.getTurn();
		lcounter.setText("Cards left: " + (52 - tmpTurn));
		if (tmpTurn == 52) {
			bFinish.setEnabled(true);
			bDraw.setEnabled(false);
			bEnd.setEnabled(false);
			bDraw.setEnabled(false);
			return;
		}
		mycontroller.draw();
		bDraw.setEnabled(false);
	}
	
	private void paintPlayerCards(final int pPlayer, int pY){
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 2;
		play.add(new JLabel(" "), c);
		c.gridy = pY;
		for (int x = 0; x < 26; x++) {
			if (field[pPlayer][x] == null) {
				continue;
			}
			final int tmpX = x;
			lPicture[cPicture] = new JLabel(field[pPlayer][x].getPicture());
			lPicture[cPicture].setBorder(uborder);
			if (cFieldPicture == 10) {
				c.gridy = pY +1;
			}
			play.add(lPicture[cPicture], c);
			lPicture[cPicture].addMouseListener(new MouseAdapter()	{
					public void mouseClicked(MouseEvent me){
						if (!drawed) {
							return;
						}
						JLabel tmp = (JLabel) me.getSource();
						if (tmp.getBorder() == uborder){
							if (cValue <= 2 ) {
								tmp.setBorder(sborder);
								selectPicture(pPlayer, tmpX);
							}
						} else {
							deselectPicture(pPlayer, tmpX);
							tmp.setBorder(uborder);
							bSubmit.setEnabled(false);
						}
						
			}
			});
			c.gridx++;
			cPicture++;
			cFieldPicture++;
		}
		for (int x= c.gridx; x<10; x++){
			play.add(new JLabel(Integer.toString(x)), c);
			c.gridx++;
		}
	}
	
	private void selectPicture(final int pPlayer, final int pX){
		
		for (int i = 0; i < 3; i++) {
			if (value[i] == null){
				value[i] = field[pPlayer][pX];		
				cValue++;
				if (cValue ==3) {
					bSubmit.setEnabled(true);
				}
				if (pPlayer==0) {
					cSteal++;
				}
				return;
			}
		}

	}
	
	private void deselectPicture(final int pPlayer, final int pX) {
		for (int i = 0; i < 3; i++) {
			if (value[i] != null) {
				if (value[i].equals(field[pPlayer][pX])) {
					value[i] = null;
				}
			}
		}
		cValue--;
		if (pPlayer == 0) {
			cSteal--;
		}
	}
	
	private void clearSelection() {
		cValue = 0;
		cSteal = 0;
		value[0] = null;
		value[1] = null;
		value[2] = null;
	}
	
	public void submit(){
		int tmpValue = value[0].getValue() + value[1].getValue() + value[2].getValue();
		
		if (tmpValue == 21) {
			textHistory.append("\nYou: " + mycontroller.getSignOfCard(value[0]) + ", " + mycontroller.getSignOfCard(value[1]) + ", " + mycontroller.getSignOfCard(value[2]));
			if (cSteal == 0) {
				money = money + 1.0;
			} else {
				money = money + 0.5;
			}
			submitted = true;
			mycontroller.submit(value[0], value[1], value[2], cSteal);
		} else {
			JOptionPane.showMessageDialog(frame, "You only reached " + tmpValue + " points instead of 21.");
		}
	}
	
	public void finish() throws FileNotFoundException{
		
		JOptionPane.showMessageDialog(frame, "There are no cards left. The game has ended."
				+ "\nPlease alert the experimenter!\nYour result of " + money + " points"
											+ " will be saved.");
		try {
			String tmp = mycontroller.finish(money);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Game21_result.txt", true)));
			out.println(tmp);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bFinish.setEnabled(false);
		bDraw.setEnabled(false);
		bEnd.setEnabled(false);
	}
	
	public void paintField(){
		play.removeAll();
		cPicture = 0;
		cFieldPicture = 0;
		field = mycontroller.getFields();
		paintPlayerCards(0,0);
		cFieldPicture = 0;
		paintPlayerCards(1,3);
		play.validate();
		play.repaint();
	}
	
	private void paintControl(){
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		lcounter.setText("Cards left: " + (52 - mycontroller.getTurn()));
		lmoney.setText("Your points: " + money);
		lpcmoney.setText("Other points: " + mycontroller.getComPoints());
		
        control.add(lDeck);
        c.gridy++;
        control.add(lcounter,c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(bDraw, c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(bEnd, c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(bSubmit, c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(lmoney, c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(lpcmoney, c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(new JLabel("Card Submission History"), c);
        c.gridy++;
        control.add(textHistory, c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(bFinish, c);
	}

	private ImageIcon getCard(){
		ImageIcon iCardBack = new ImageIcon(Card.class.getClassLoader().getResource("image/back.jpg"));
		Image cards = iCardBack.getImage();
		cards = cards.getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(cards);
	}
}
