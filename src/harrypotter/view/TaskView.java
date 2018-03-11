package harrypotter.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import harrypotter.exceptions.InvalidTargetCellException;
import harrypotter.exceptions.OutOfBordersException;
import harrypotter.model.character.Champion;
import harrypotter.model.character.GryffindorWizard;
import harrypotter.model.character.HufflepuffWizard;
import harrypotter.model.character.RavenclawWizard;
import harrypotter.model.character.Wizard;
import harrypotter.model.world.Cell;
import harrypotter.model.world.ChampionCell;
import harrypotter.model.world.CollectibleCell;
import harrypotter.model.world.CupCell;
import harrypotter.model.world.EmptyCell;
import harrypotter.model.world.Merperson;
import harrypotter.model.world.Obstacle;
import harrypotter.model.world.ObstacleCell;
import harrypotter.model.world.PhysicalObstacle;
import harrypotter.model.world.TreasureCell;
import harrypotter.model.world.WallCell;


@SuppressWarnings("serial")
public class TaskView extends JFrame{
	
	private JLabel taskBackground;
	private JLabel instructions;
	private PnlChampionStatus pnlChampion1;
	private PnlChampionStatus pnlChampion2;
	private PnlChampionStatus pnlChampion3;
	private PnlChampionStatus pnlChampion4;
	private JLabel mapIntro;
	private JLabel pnlMap;
	private JPanel [][]mapGraphic;
	private JLabel pnlDescription;
	private JLabel descriptionBackground;
	private JPanel pnlLeft; 
	private JPanel pnlRight;
	private JLabel winnerOfTheGame;
	private JLabel gameOver;
	private static Font gameFont;
	
	public TaskView(ArrayList<Champion> champions) {
		
		setTitle("Harry Potter The Tri Wizard Tournament");
		
		
		taskBackground = new JLabel();
		taskBackground.setBackground(Color.BLACK);
		taskBackground.setLayout(new BorderLayout());
		taskBackground.setSize(this.getSize());
		taskBackground.setOpaque(true);

		instructions = new JLabel();
		instructions.setSize(this.getSize());
		ImageIcon instructionPic = new ImageIcon("instructions.png");
		instructions.setIcon(instructionPic);
		instructions.setVisible(false);
		
		//this.setResizable(false);
		this.setMaximumSize(new Dimension(1980,1080));
		this.setMinimumSize(new Dimension(1280,720));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		JFrame.setDefaultLookAndFeelDecorated(false);
		//Just to test
		
		//Getting the Customized Font
		gameFont = TaskView.getGameFont(Font.CENTER_BASELINE,40);
		
		//setBounds(50, 50, 800, 600);
		getContentPane().setLayout(new CardLayout());
		
		pnlChampion1 = new PnlChampionStatus(champions.get(0));
		pnlChampion2 = new PnlChampionStatus(champions.get(1));
		pnlChampion3 = new PnlChampionStatus(champions.get(2));
		pnlChampion4 = new PnlChampionStatus(champions.get(3));
				
		pnlLeft = new JPanel();
		pnlLeft.setLayout(new GridLayout(2,1));
		pnlLeft.setPreferredSize(new Dimension(3*this.getWidth()/8, this.getHeight()));
		pnlLeft.setOpaque(false);
		
		pnlLeft.add(pnlChampion1, pnlChampion2);
		pnlLeft.add(pnlChampion2,pnlChampion1);

		pnlRight = new JPanel();
		pnlRight.setLayout(new GridLayout(2,1));
		pnlRight.setPreferredSize(new Dimension(3*this.getWidth()/8, this.getHeight()));
		pnlRight.setOpaque(false);
		
		pnlRight.add(pnlChampion3,pnlChampion4);
		pnlRight.add(pnlChampion4,pnlChampion3);

		pnlMap = new JLabel();
		pnlMap.setLayout(new GridLayout(10,10));
		pnlMap.setPreferredSize(new Dimension(6*this.getWidth()/8,this.getHeight()));
		
		mapGraphic = new JPanel[10][10];
		
		descriptionBackground = new JLabel();
		descriptionBackground.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()/9));
		descriptionBackground.setLayout(new FlowLayout());
		
		ImageIcon graphicIcon = new ImageIcon("discriptionBar.png");
		graphicIcon = new ImageIcon(getScaledImage(graphicIcon.getImage(),this.getWidth()*3/2,this.getHeight()/9));
		descriptionBackground.setIcon(graphicIcon);
		//descriptionBackground.setOpaque(false);
		
		pnlDescription = new JLabel("Data of Obstacle Area", SwingConstants.CENTER);
		
		pnlDescription.setFont(gameFont);
		pnlDescription.setText("Data of Obstacle Area Updating Here");
		pnlDescription.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()/9));
		pnlDescription.setAlignmentX(SwingConstants.CENTER);
		pnlDescription.setAlignmentY(SwingConstants.CENTER);
		pnlDescription.repaint();
		pnlDescription.setOpaque(false);

		
		descriptionBackground.add(pnlDescription);
		descriptionBackground.repaint();
		
		intializeMapIntro();
		
		add(taskBackground);
		add(instructions);
		add(mapIntro);

		taskBackground.add(pnlLeft, BorderLayout.WEST);
		taskBackground.add(pnlRight, BorderLayout.EAST);
		taskBackground.add(descriptionBackground,BorderLayout.SOUTH);
		taskBackground.add(pnlMap, BorderLayout.CENTER);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(0,0,screenSize.width, screenSize.height);
		//this.pack();
		
		repaint();
		validate();

	}
	
	public void paintTaskMap(Cell[][] taskMap) throws IOException, OutOfBordersException, InvalidTargetCellException {

		Cell[][] map = taskMap;
		pnlMap.removeAll();

		for(int i=0; i<map.length;i++){
			for(int j=0;j<map[i].length;j++){
				JPanel paintedGrid = new JPanel();
				ImageIcon picIcon;
				
				if(map[i][j] instanceof ChampionCell){
					Champion champ = ((ChampionCell)map[i][j]).getChamp();
					ImageIcon champIcon;
					
					if(champ instanceof GryffindorWizard){
						champIcon = new ImageIcon("gryffindor_char.png");
					}else if(champ instanceof HufflepuffWizard){
						champIcon = new ImageIcon("hufflepuff_char.png");
					}else if(champ instanceof RavenclawWizard){
						champIcon = new ImageIcon("ravenclaw_char.png");
					}else{ // it is SlytherinWizard
						champIcon = new ImageIcon("slytherin_char.png");
					}
					champIcon = new ImageIcon(getScaledImage(champIcon.getImage(),100,90));
					paintedGrid.add(new JLabel(champIcon));
					paintedGrid.setVisible(true);
				}
				if(map[i][j] instanceof CollectibleCell){
					paintedGrid.add(new JLabel("Collectible Cell"));
					paintedGrid.setVisible(false);

				}
				if(map[i][j] instanceof CupCell){
					paintedGrid.add(new JLabel("Cup Cell"));
					paintedGrid.setVisible(false);

				}
				if(map[i][j] instanceof EmptyCell){
					//paintedGrid.add(new JLabel("Empty Cell"));
					paintedGrid.setVisible(true);
				}
				if(map[i][j] instanceof ObstacleCell){
					Obstacle obstacle = ((ObstacleCell)map[i][j]).getObstacle();
					if(obstacle instanceof PhysicalObstacle){
						picIcon = new ImageIcon("obstacle.png");
						picIcon = new ImageIcon(getScaledImage(picIcon.getImage(),80,80));
						paintedGrid.add(new JLabel(picIcon));
						PhysicalObstacle physicalObstacle = (PhysicalObstacle) obstacle;
						paintedGrid.setToolTipText("ObstacleCell" + '\n' + " HP: " + physicalObstacle.getHp());
					}else if(obstacle instanceof Merperson){
						picIcon = new ImageIcon("merperson.png");
						picIcon = new ImageIcon(getScaledImage(picIcon.getImage(),80,80));
						paintedGrid.add(new JLabel(picIcon));
						Merperson merperson = (Merperson) obstacle;
						paintedGrid.setToolTipText("ObstacleCell" + '\n' + " HP : " + merperson.getHp() + '\n' +
								" Damage : "+ merperson.getDamage());
					}
					paintedGrid.setVisible(true);
				}
				if(map[i][j] instanceof TreasureCell){
					picIcon = new ImageIcon("foriegnTreasure.png");
					picIcon = new ImageIcon(getScaledImage(picIcon.getImage(),80,80));
					paintedGrid.add(new JLabel(picIcon));
					paintedGrid.setVisible(false);
				}
				if(map[i][j] instanceof WallCell){
					picIcon = new ImageIcon("wall.png");
					picIcon = new ImageIcon(getScaledImage(picIcon.getImage(),80,80));
					paintedGrid.add(new JLabel(picIcon));
					paintedGrid.setVisible(true);

				}
				paintedGrid.setOpaque(false);
				mapGraphic[i][j]= paintedGrid;
				pnlMap.add(mapGraphic[i][j]);
				pnlMap.setVisible(true);
				
			}

		}
		pnlMap.repaint();
		pnlMap.revalidate();
		this.repaint();
		this.revalidate();
		
	}
		
	public void updateChampionsData() {
		pnlChampion1.updateChampionData();
		pnlChampion2.updateChampionData();
		pnlChampion3.updateChampionData();
		pnlChampion4.updateChampionData();
	}
	
	public void updateChampionDataForNextTask(ArrayList<Champion> champions){
		
		setPnlChampion1(new PnlChampionStatus(champions.get(0)));
		
		if(champions.size()>1){
			setPnlChampion2(new PnlChampionStatus(champions.get(1)));
		}else{
			pnlChampion2.declareDead();
		}
		if(champions.size()>2){
			setPnlChampion3(new PnlChampionStatus(champions.get(2)));
		}else{
			pnlChampion3.declareDead();

		}
		if(champions.size()>3){
			setPnlChampion4(new PnlChampionStatus(champions.get(3)));

		}else{
			pnlChampion4.declareDead();
		}
		
		updateChampionsData();
		pnlLeft.removeAll();
		pnlLeft.add(pnlChampion1, pnlChampion2);
		pnlLeft.add(pnlChampion2,pnlChampion1);
		pnlRight.removeAll();
		pnlRight.add(pnlChampion3,pnlChampion4);
		pnlRight.add(pnlChampion4,pnlChampion3);
		
	}
	
	public void showWinnerOfTheGame(Champion winner) {
//		this.remove(pnlDescription);
//		this.remove(pnlMap);
//		this.remove(pnlLeft);
//		this.remove(pnlRight);
		this.remove(taskBackground);
		this.remove(instructions);
		this.remove(mapIntro);
		taskBackground.setVisible(false);
		winnerOfTheGame = new JLabel("The Winner is " + ((Wizard)winner).getName() + ", Congratulations",SwingConstants.CENTER);
		winnerOfTheGame.setFont(new Font(Font.SERIF,Font.CENTER_BASELINE,100));
		winnerOfTheGame.setSize(this.getSize());
		this.add(winnerOfTheGame);
		this.repaint(); this.revalidate();
		
		Music effect = new Music("sounds/winning_sound.wav");
		effect.playSound();
	}
	
	public void showLosersPanel() {
//		this.remove(pnlDescription);
//		this.remove(pnlMap);
//		this.remove(pnlLeft);
//		this.remove(pnlRight);
		this.remove(taskBackground);
		this.remove(instructions);
		this.remove(mapIntro);
		taskBackground.setVisible(false);
		gameOver = new JLabel("Oh No You All have Lost!?, GameOver",SwingConstants.CENTER);
		gameOver.setFont(new Font(Font.SERIF,Font.CENTER_BASELINE,100));
		gameOver.setSize(this.getSize());
		this.add(gameOver);
		this.repaint(); this.revalidate();
		Music effect = new Music("sounds/gameOver_sound.wav");
		effect.playSound();
	}

	public void intializeMapIntro(){
		mapIntro = new JLabel();
		mapIntro.setSize(this.getSize());
		mapIntro.setVisible(false);
	}
	public static Image getScaledImage(Image inImage, int width, int height){
	    
	    BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = outImage.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(inImage, 0, 0, width, height, null);
	    g2.dispose();

	    return outImage;
		    
	}
	
	public static Font getGameFont(int type,int size){
		//Getting the Customized Font
		BufferedInputStream fontStream;
		gameFont =new Font(Font.MONOSPACED,Font.CENTER_BASELINE,size); //Just in case the font is not found
		try {
			fontStream = new BufferedInputStream(new FileInputStream("fonts/leaguegothic-regular-webfont.ttf"));
			gameFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
	        gameFont = gameFont.deriveFont(type, size);  
	        
		} catch (FontFormatException|IOException e) {
			e.printStackTrace();
		}
		return gameFont;
	}
	public JPanel getPnlChampion1() {
		return pnlChampion1;
	}
	public void setPnlChampion1(PnlChampionStatus pnlChampion1) {
		this.pnlChampion1 = pnlChampion1;
	}
	public PnlChampionStatus getPnlChampion2() {
		return pnlChampion2;
	}
	public void setPnlChampion2(PnlChampionStatus pnlChampion2) {
		this.pnlChampion2 = pnlChampion2;
	}
	public PnlChampionStatus getPnlChampion3() {
		return pnlChampion3;
	}
	public void setPnlChampion3(PnlChampionStatus pnlChampion3) {
		this.pnlChampion3 = pnlChampion3;
	}
	public PnlChampionStatus getPnlChampion4() {
		return pnlChampion4;
	}
	public void setPnlChampion4(PnlChampionStatus pnlChampion4) {
		this.pnlChampion4 = pnlChampion4;
	}
	public JLabel getPnlDescription() {
		return pnlDescription;
	}
	public void setPnlDescription(JLabel pnlDescription) {
		this.pnlDescription = pnlDescription;
	}
	public JLabel getPnlMap() {
		return pnlMap;
	}
	public void setPnlMap(JLabel pnlMap) {
		this.pnlMap = pnlMap;
	}
	public JPanel[][] getMapGraphic() {

		return mapGraphic;
	}
	public void setMapGraphic(JPanel[][] mapGraphic) {
		this.mapGraphic = mapGraphic;
	}
	public JPanel getPnlLeft() {
		return pnlLeft;
	}
	public void setPnlLeft(JPanel pnlLeft) {
		this.pnlLeft = pnlLeft;
	}
	public JPanel getPnlRight() {
		return pnlRight;
	}
	public void setPnlRight(JPanel pnlRight) {
		this.pnlRight = pnlRight;
	}
	public JLabel getTaskBackground() {
		return taskBackground;
	}
	public void setTaskBackground(JLabel taskBackground) {
		this.taskBackground = taskBackground;
	}
	public JLabel getDescriptionBackground() {
		return descriptionBackground;
	}
	public void setDescriptionBackground(JLabel descriptionBackground) {
		this.descriptionBackground = descriptionBackground;
	}
	public JLabel getWinnerOfTheGame() {
		return winnerOfTheGame;
	}
	public void setWinnerOfTheGame(JLabel winnerOfTheGame) {
		this.winnerOfTheGame = winnerOfTheGame;
	}
	public JLabel getGameOver() {
		return gameOver;
	}
	public void setGameOver(JLabel gameOver) {
		this.gameOver = gameOver;
	}
	public JLabel getInstructions() {
		return instructions;
	}
	public void setInstructions(JLabel instructions) {
		this.instructions = instructions;
	}

	public JLabel getMapIntro() {
		return mapIntro;
	}

	public void setMapIntro(JLabel mapIntro) {
		this.mapIntro = mapIntro;
	}

}
