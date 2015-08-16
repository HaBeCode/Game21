package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

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

	private JLabel lcounter;
	private JLabel lplayer;
	private JLabel[] lPicture;
	private int cPicture;
	private int cFieldPicture;
	private int[] value;
	private JButton bFinish;
	private JButton bDraw;
	private JButton bSubmit;
	private JButton bEnd;
	private Border border;
	
	private static Controller mycontroller;
	private Card[][] field;

	public static void main(String[] args){
		
		mycontroller = new Controller();
		new TwentyOne();
	}
	
	public TwentyOne() {
		
		frame = new JFrame("Game 21");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		play = new JPanel(new GridBagLayout());
		
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

		drawControl();
		
		frame.add(control, BorderLayout.WEST);
		drawPlay("Player 2");
		frame.add(play, BorderLayout.CENTER);
		
		frame.setJMenuBar(menuBar);
		frame.pack();
		if (frame.getWidth() < 600) {
			frame.setSize(new Dimension(600, frame.getHeight()));
		}
		frame.setResizable(false);
		frame.setVisible(true);
		
		field = new Card[2][26];
		lPicture = new JLabel[52];
		cPicture = 0;
		cFieldPicture = 0;
		border = BorderFactory.createLineBorder(Color.blue, 2);
	}
	
	public void actionPerformed (ActionEvent ae){
		if (ae.getSource() == this.bDraw) {
			drawCard();
			bEnd.setEnabled(true);
			bDraw.setEnabled(false);
		}
		else if (ae.getSource() == this.bEnd){
			endTurn();
			bEnd.setEnabled(false);
			bDraw.setEnabled(true);
		}
		else if (ae.getSource() == this.bSubmit) {
			//do something
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
		}
		else if (ae.getSource() == this.closeGame) {
			System.exit(0);
		}
		drawField();
		if (cFieldPicture >= 4) {
			frame.pack();
		}
		frame.repaint();
	}
	
	public void drawCard(){
		int tmpTurn = mycontroller.getTurn();
		lcounter.setText("Cards left: " + (52 - (tmpTurn + 1)));
		if (tmpTurn == 51) {
			bFinish.setEnabled(true);
			bDraw.setEnabled(false);
			return;
		}
		mycontroller.draw();
	}
	
	public void drawField(){
		play.removeAll();
		cPicture = 0;
		cFieldPicture = 0;
		field = mycontroller.getFields();
		drawPlay(mycontroller.getPlayer());
		drawPlayerCards(0,0);
		cFieldPicture = 0;
		drawPlayerCards(1,2);
		play.validate();
		play.repaint();
	}
	
	private void drawPlayerCards(final int pPlayer, int pY){
		GridBagConstraints c = new GridBagConstraints();
		int x = 0;
		
		c.gridx = 2;
		c.gridy = pY;
		while (field[pPlayer][x] != null) {
			final int tmpX = x;
			lPicture[cPicture] = new JLabel(field[pPlayer][x].getPicture());
			if (cFieldPicture == 10) {
				c.gridy = pY + 1;
				c.gridx = 2;
			}
			play.add(lPicture[cPicture], c);
			lPicture[cPicture].addMouseListener(new MouseAdapter()	{
					public void mouseClicked(MouseEvent me){
						JLabel tmp = (JLabel) me.getSource();
						if (tmp.getBorder() == null){
							tmp.setBorder(border);
							selectPicture(pPlayer, tmpX);
						} else {
							tmp.setBorder(null);
						}
			}
			});
			c.gridx++;
			x++;
			cPicture++;
			cFieldPicture++;
		}
	}
	
	private void selectPicture(final int pPlayer, final int pX){
		System.out.println(field[pPlayer][pX].getValue());
	}
	
	public void submit(){
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
	
	public ImageIcon getCard(){
		ImageIcon iCardBack = new ImageIcon("image/back.png");
		Image cards = iCardBack.getImage();
		cards = cards.getScaledInstance(100, 120, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(cards);
	}
	
	private void drawControl(){
		control = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		lcounter.setText("Cards left: 52");

        control.add(new JLabel(getCard()),c);
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
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(new JLabel(" "), c);
        c.gridy++;
        control.add(bFinish, c);
	}

	private void drawPlay(String pPlayer) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		JLabel lcomputer = new JLabel("Player 1");
		lplayer = new JLabel(pPlayer);
		
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
}
