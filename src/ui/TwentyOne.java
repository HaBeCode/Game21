package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.Border;

import data.Card;
import logic.Controller;

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

	private JLabel lcounter;
	private JLabel lplayer;
	private JLabel lmoney;
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

	public static void main(String[] args){
		new TwentyOne();
	}
	
	public TwentyOne() {
		
		mycontroller = new Controller();
		frame = new JFrame("Game 21");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		background = new ImageIcon("image/grey-background.jpg").getImage();
		play = new JPanel(new GridBagLayout()) {
			@Override
			  protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			        g.drawImage(background, 0, 0, null);
			}
		};
		control = new JPanel(new GridBagLayout());
		
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		newGame = new JMenuItem("New Game");
		menu.add(newGame);
		newGame.addActionListener(this);
		closeGame = new JMenuItem("Close");
		menu.add(closeGame);
		closeGame.addActionListener(this);
		menuBar.add(menu);
		
		lcounter = new JLabel("");
		lmoney = new JLabel("");
		lDeck = new JLabel(getCard());
		bDraw = new JButton("Draw");
		bSubmit = new JButton("Submit");
		bFinish = new JButton("Finish");
		bEnd = new JButton("End turn");
		bDraw.setEnabled(false);
		bSubmit.setEnabled(false);
		bFinish.setEnabled(false);
		bEnd.setEnabled(false);
		bDraw.addActionListener(this);
		bSubmit.addActionListener(this);
		bFinish.addActionListener(this);
		bEnd.addActionListener(this);

		paintControl();
		
		frame.add(control, BorderLayout.WEST);
		paintPlay("Player 2");
		frame.add(play, BorderLayout.CENTER);
		
		frame.setJMenuBar(menuBar);
		//frame.setSize(new Dimension(1275, 645));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setResizable(false);
		frame.setVisible(true);
		
		field = new Card[2][26];
		lPicture = new JLabel[52];
		cPicture = 0;
		cFieldPicture = 0;
		cValue = 0;
		cSteal = 0;
		money = 0.0;
		sborder = BorderFactory.createLineBorder(Color.blue, 2);
		uborder = BorderFactory.createLineBorder(Color.gray, 1);
		value = new Card[3];
	}
	
	public void actionPerformed (ActionEvent ae){
		if (ae.getSource() == this.bDraw) {
			drawCard();
			bEnd.setEnabled(true);
			bDraw.setEnabled(false);
			clearSelection();
		}
		else if (ae.getSource() == this.bEnd){
			endTurn();
			bEnd.setEnabled(false);
			bDraw.setEnabled(true);
			clearSelection();
		}
		else if (ae.getSource() == this.bSubmit) {
			submit();
			bSubmit.setEnabled(false);
			clearSelection();
			control.removeAll();
			paintControl();
		}
		else if (ae.getSource() == this.bFinish) {
			JOptionPane.showMessageDialog(frame, "Thank you very much. Your results will be saved.");
			finish();
		}
		else if (ae.getSource() == this.newGame) {
			
			while (mycontroller.getPlayer().equals("Player 2")) {
				String tmp = "a";
				tmp = (String) JOptionPane.showInputDialog("Please enter your code:");
				if (!tmp.equals(Integer.toString(JOptionPane.CANCEL_OPTION))) {
					mycontroller.setPlayer(tmp);
				} 
			}
			lplayer.setText(mycontroller.getPlayer());
			drawCard();
			bDraw.setEnabled(true);
			paintControl();
		}
		else if (ae.getSource() == this.closeGame) {
			System.exit(0);
		}
		paintField();
		frame.repaint();
	}
	
	public void drawCard(){
		int tmpTurn = mycontroller.getTurn();
		lcounter.setText("Cards left: " + (52 - tmpTurn));
		if (tmpTurn == 51) {
			bFinish.setEnabled(true);
			bDraw.setEnabled(false);
			return;
		}
		mycontroller.draw();
	}
	
	private void paintPlayerCards(final int pPlayer, int pY){
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 2;
		c.gridy = pY;
		for (int x = 0; x < 26; x++) {
			if (field[pPlayer][x] == null) {
				if (field[pPlayer][x+1] == null && field[pPlayer][x+2] == null && field[pPlayer][x+3] == null) {
					break;
				} else {
					continue;
				}
			}
			final int tmpX = x;
			lPicture[cPicture] = new JLabel(field[pPlayer][x].getPicture());
			lPicture[cPicture].setBorder(uborder);
			if (cFieldPicture == 10) {
				c.gridy = pY + 1;
				c.gridx = 2;
			}
			play.add(lPicture[cPicture], c);
			lPicture[cPicture].addMouseListener(new MouseAdapter()	{
					public void mouseClicked(MouseEvent me){
						JLabel tmp = (JLabel) me.getSource();
						if (tmp.getBorder() == uborder){
							if (cValue <= 2 ) {
								tmp.setBorder(sborder);
								selectPicture(pPlayer, tmpX);
								System.out.println(field[pPlayer][tmpX].getValue());
								if (cValue == 3) {
									bSubmit.setEnabled(true);
								}
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
	}
	
	private void selectPicture(final int pPlayer, final int pX){
		value[cValue] = field[pPlayer][pX];
		cValue++;
		if (pPlayer==0) {
			cSteal++;
		}
	}
	
	private void deselectPicture(final int pPlayer, final int pX) {
		for (int i = 0; i< 2; i++) {
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
			if (cSteal == 0) {
				money = money + 1.0;
			} else {
				money = money + 0.5;
			}
			mycontroller.submit(value[0], value[1], value[2], cSteal);
		} else {
			JOptionPane.showMessageDialog(frame, "You only reached " + tmpValue + " points instead of 21.");
		}
	}
	
	public void finish(){
		mycontroller.finish();
	}
	
	public void endTurn(){
		try {
			TimeUnit.SECONDS.sleep(0);
			drawCard();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mycontroller.startKI();
	}
	
	public void paintField(){
		play.removeAll();
		cPicture = 0;
		cFieldPicture = 0;
		field = mycontroller.getFields();
		paintPlay(mycontroller.getPlayer());
		paintPlayerCards(0,0);
		cFieldPicture = 0;
		paintPlayerCards(1,2);
		play.validate();
		play.repaint();
	}
	
	private void paintControl(){
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		lcounter.setText("Cards left: " + (52 - mycontroller.getTurn()));
		lmoney.setText("Cash: " + money + "$");
		

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
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(bFinish, c);
	}

	private void paintPlay(String pPlayer) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		JLabel lcomputer = new JLabel("Player 1");
		lplayer = new JLabel(pPlayer);
		lcomputer.setForeground(Color.WHITE);
		lplayer.setForeground(Color.WHITE);
		lcomputer.setFont(new Font("Arial", Font.PLAIN, 20));
		lplayer.setFont(new Font("Arial", Font.PLAIN, 20));
		
        play.add(lcomputer, c);
        c.gridy++;
        play.add(new JLabel(" "), c);
        c.gridy++;
        play.add(lplayer, c);
        c.gridy++;
        play.add(new JLabel(" "), c);
        c.gridx++;
        c.gridy = 0;
        play.add(new JLabel(" "), c);
        c.gridy++;
        play.add(new JLabel(" "), c);
        c.gridy++;
        play.add(new JLabel(" "), c);
	}

	private ImageIcon getCard(){
		ImageIcon iCardBack = new ImageIcon("image/back.jpg");
		Image cards = iCardBack.getImage();
		cards = cards.getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(cards);
	}
}
