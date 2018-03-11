package harrypotter.controller;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import harrypotter.exceptions.InvalidTargetCellException;
import harrypotter.exceptions.OutOfBordersException;
import harrypotter.model.character.Champion;
import harrypotter.model.character.Wizard;
import harrypotter.model.magic.Spell;
import harrypotter.model.tournament.Tournament;
import harrypotter.view.IntroView;
import harrypotter.view.IntroViewListener;
import harrypotter.view.Music;


public class HarryPotterGUI implements IntroViewListener, ActionListener {
    
    private IntroView view;
    private Tournament tournament;
    private int spellChamp;
    private Dimension screenSize;
    private double ratioX;
    private double ratioY;
    private double fontFactor;
    
    public HarryPotterGUI() throws IOException {
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ratioX = screenSize.getWidth()/1920;
        ratioY = screenSize.getHeight()/1080;
        fontFactor = (screenSize.getWidth()*screenSize.getHeight())/(1920*1080);
        
        view = new IntroView();
        view.setListener(this);
        view.setVisible(true);
        tournament = new Tournament();
        view.pack();
        view.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        view.setMinimumSize(new Dimension(600, 600));
        spellChamp = 0;

    }
    
    
    public static void main (String[]args) throws IOException {
        new HarryPotterGUI();
    }


    @Override
    public void champDone(ArrayList<Champion> c) {
        tournament.addChampion(c.get(0));
        tournament.addChampion(c.get(1));
        tournament.addChampion(c.get(2));
        tournament.addChampion(c.get(3));
        
        
        ArrayList<Spell> spells = tournament.getSpells();
        ArrayList<Champion> champs = tournament.getChampions();
        view.spellPanel(spells, champs, spellChamp);
        

    }
    public void spellDone() {
        ArrayList<Spell> spells = tournament.getSpells();
        ArrayList<Champion> champs = tournament.getChampions();
        if (spellChamp<3) {
            view.spellPanel(spells, champs, ++spellChamp);
            view.pack();
            view.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }else{
            JLabel startGame = new JLabel();
            startGame.setIcon(new ImageIcon("begin.png"));
            view.setContentPane(startGame);
//           startGame.setLayout(new BorderLayout());
            JButton beginBtn = new JButton();
            beginBtn.addActionListener(this);
            beginBtn.setText("Begin Tournament");
            int style = Font.PLAIN | Font.ITALIC;
            beginBtn.setFont(new Font("Jokerman", style ,(int) (50*fontFactor)));
            beginBtn.setForeground(Color.WHITE);
            beginBtn.setOpaque(false);
            beginBtn.setContentAreaFilled(false);
            beginBtn.setBorderPainted(false);
            view.add(beginBtn);
            beginBtn.setBounds((int)(660*ratioX),(int) (380*ratioY), (int)(650*ratioX),(int) (150*ratioY));

            
            beginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    Music blop = new Music("Blop.wav");
                    blop.playSound();
                    beginBtn.setFont(new Font("Jokerman", style ,(int) (65*fontFactor)));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    beginBtn.setFont(new Font("Jokerman", style ,(int) (50*fontFactor)));
                }
            });
            
            view.pack();
            view.setExtendedState(JFrame.MAXIMIZED_BOTH);
            view.repaint();
            view.revalidate();
        }
            
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    	
        try {
            Music tourBegin = new Music("tourBegin.wav");
            tourBegin.playSound();
            new TaskGUI(tournament);
        } catch (OutOfBordersException | InvalidTargetCellException | IllegalAccessException | IOException e1) {
            JOptionPane.showMessageDialog(view, "An error has been found.");
        }        
	}
    
}
