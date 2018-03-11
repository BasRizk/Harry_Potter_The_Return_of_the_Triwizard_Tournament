package harrypotter.controller;


import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import harrypotter.exceptions.InCooldownException;
import harrypotter.exceptions.InvalidTargetCellException;
import harrypotter.exceptions.NotEnoughIPException;
import harrypotter.exceptions.OutOfBordersException;
import harrypotter.exceptions.OutOfRangeException;
import harrypotter.model.character.Champion;
import harrypotter.model.character.GryffindorWizard;
import harrypotter.model.character.HufflepuffWizard;
import harrypotter.model.character.RavenclawWizard;
import harrypotter.model.character.SlytherinWizard;
import harrypotter.model.character.Wizard;
import harrypotter.model.magic.Collectible;
import harrypotter.model.magic.DamagingSpell;
import harrypotter.model.magic.HealingSpell;
import harrypotter.model.magic.Potion;
import harrypotter.model.magic.RelocatingSpell;
import harrypotter.model.magic.Spell;
import harrypotter.model.tournament.FirstTask;
import harrypotter.model.tournament.SecondTask;
import harrypotter.model.tournament.Task;
import harrypotter.model.tournament.ThirdTask;
import harrypotter.model.tournament.Tournament;
import harrypotter.model.tournament.TournamentListener;
import harrypotter.model.world.*;
import harrypotter.view.*;

public class TaskGUI implements TournamentListener, KeyListener{

	private Tournament tournament;
	private TaskView taskView;
	private boolean [] onTaskFlags;
	private boolean currentlyFree;
	private Spell spellInAction;
	private ArrayList<Direction> directionsInAction;
	private Task currentTask;
	private String tmpLastDescription;
	private boolean loading;
	//private boolean battlemode;
	
	public TaskGUI(Tournament t) throws IOException, OutOfBordersException, InvalidTargetCellException, IllegalAccessException {

		tournament = t;
		tournament.setListener(this);
		
		currentlyFree = true;
		onTaskFlags = new boolean[3];
		directionsInAction = new ArrayList<Direction>();
		//battlemode = false;
		JDialog.setDefaultLookAndFeelDecorated(false);
		
		JOptionPane.setRootFrame(taskView);
		
		
		taskView = new TaskView(tournament.getChampions());
		taskView.setUndecorated(true);
		taskView.addKeyListener(this);
		taskView.setFocusable(true);
		taskView.setFocusTraversalKeysEnabled(false);
		
		tournament.beginTournament();	
		
		
		if(currentTask instanceof FirstTask){
			ImageIcon picIcon = new ImageIcon("egg.png");
			picIcon = new ImageIcon(TaskView.getScaledImage(picIcon.getImage(),50,50));
			JLabel eggPic = new JLabel(picIcon); eggPic.setVisible(true);
			taskView.getMapGraphic()[4][4].add(eggPic);	
		}

		
		highlightCurrentChampion();
		
    	taskView.setVisible(true);
		taskView.getTaskBackground().setVisible(false);
		taskView.getInstructions().setVisible(true);
		loading = true;
		
		Timer timer = new Timer(7000, new ActionListener() {
		    @Override 
		    public void actionPerformed(ActionEvent arg0) {
				
		    	ImageIcon imageIcon = new ImageIcon("firstTaskIntro.gif");
				imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(taskView.getWidth(), taskView.getHeight(), Image.SCALE_DEFAULT));
				taskView.getMapIntro().setIcon(imageIcon);
				taskView.getMapIntro().setVisible(true);
				taskView.getTaskBackground().setVisible(false);
				taskView.getInstructions().setVisible(false);
				loading = true;
				Music taskMusic = new Music("sounds/taskSoundEffect.wav");
				taskMusic.playSound();

				Timer timer = new Timer(10000, new ActionListener() {
				    @Override 
				    public void actionPerformed(ActionEvent arg0) {
				    	taskView.getMapIntro().setVisible(false);
				    	taskView.getTaskBackground().setVisible(true);
				    	taskView.getPnlDescription().setText(tmpLastDescription);
				    	taskView.getTaskBackground().setVisible(true);
				    	
				    	loading = false;
				    	computerTurn();
				    }
				    
				}); 
				timer.setRepeats(false);
				timer.start();
		    }
		    
		}); 
		timer.setRepeats(false);
		timer.start();
		
		taskView.setDefaultCloseOperation(0);
		taskView.addWindowListener(new java.awt.event.WindowAdapter() { 
		    @Override 
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	int option = JOptionPane.showConfirmDialog(taskView,"Are you sure to End the Game?", "I am Closing!",
		        		JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
		        if (option == JOptionPane.YES_OPTION){
		        	System.exit(0);
			    	//windowEvent.getWindow().dispose();
		        }
		    } 
		});
		
		taskView.repaint();
		taskView.validate();
		

	}

	private void highlightCurrentChampion() {
		
		Champion currentChamp = currentTask.getCurrentChamp();
		int x = ((Wizard)currentChamp).getLocation().x;
		int y = ((Wizard)currentChamp).getLocation().y;
		taskView.getMapGraphic()[x][y].setBackground(Color.BLUE);
		taskView.getMapGraphic()[x][y].setOpaque(true);
		
		ImageIcon iconNormal = new ImageIcon("championPanel_Normal.png");
		ImageIcon iconCurrent = new ImageIcon("championPanel_highlighted.png");
		iconNormal = new ImageIcon(TaskView.getScaledImage(iconNormal.getImage(),400,450));
		iconCurrent = new ImageIcon(TaskView.getScaledImage(iconCurrent.getImage(),400,450));
		
		if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion1()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion1()).getPnlBackground().setIcon(iconCurrent);
			((PnlChampionStatus)taskView.getPnlChampion2()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion3()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion4()).getPnlBackground().setIcon(iconNormal);
		}
		if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion2()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion2()).getPnlBackground().setIcon(iconCurrent);
			((PnlChampionStatus)taskView.getPnlChampion1()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion3()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion4()).getPnlBackground().setIcon(iconNormal);
		}
		if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion3()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion3()).getPnlBackground().setIcon(iconCurrent);
			((PnlChampionStatus)taskView.getPnlChampion2()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion1()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion4()).getPnlBackground().setIcon(iconNormal);
		}
		if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion4()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion4()).getPnlBackground().setIcon(iconCurrent);
			((PnlChampionStatus)taskView.getPnlChampion2()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion3()).getPnlBackground().setIcon(iconNormal);
			((PnlChampionStatus)taskView.getPnlChampion1()).getPnlBackground().setIcon(iconNormal);
		}
           
		tmpLastDescription = "Current Player : "  + ((Wizard)currentChamp).getName() + ",You Have " + currentTask.getAllowedMoves() + " moves.";
		

	}

	protected void computerTurn() {

		if( loading == true || currentTask.getChampions().size()==0 || ((Wizard)currentTask.getCurrentChamp()).isAI() == false){
			return;
		}
		
		int oldInventorySize = ((Wizard)currentTask.getCurrentChamp()).getInventory().size();
		ArrayList<Collectible> inventory = ((Wizard)currentTask.getCurrentChamp()).getInventory();
	
		checkForCollectibles(oldInventorySize, inventory);

		
		Champion AI = currentTask.getCurrentChamp();
		boolean [] actionFlags = new boolean[8];
		boolean turnIsStillOn = true;
		
		while(turnIsStillOn){
			int randomAction = (int) (Math.random()*7);
			
			switch(randomAction){
			
			case 0:
				if(actionFlags[0]==false){
					try {
					currentTask.moveForward();
					} catch (OutOfBordersException | InvalidTargetCellException | IOException e1) {
						
					}actionFlags[0]=true; break;
				}
		
			case 1: 
				if(actionFlags[1]==false){
					try {
					currentTask.moveBackward();
					} catch (OutOfBordersException | InvalidTargetCellException | IOException e1) {
						
					}actionFlags[1]=true; break;
				}
		
			case 2: 
				if(actionFlags[2]==false){
					try {
					currentTask.moveRight();
					} catch (OutOfBordersException | InvalidTargetCellException | IOException e1) {
						
					}actionFlags[2]=true; break;
				}
				
			case 3:
				if(actionFlags[3]==false){
					try {
					currentTask.moveLeft();
					} catch (OutOfBordersException | InvalidTargetCellException | IOException e1) {
						
					}actionFlags[3]=true; break;
				}
				
			case 4:
				if(actionFlags[4]==false){
					castSpellByAI(); actionFlags[4] = true; break;
				}
			
			case 5:
				if(actionFlags[5]==false){
					activateTraitByAI(); actionFlags[5] = true; break;
				}
			
			case 6:
				if(actionFlags[6]==false){
					useInventoryByAI(); actionFlags[6] = true; break;
				}
			
			default:
					try {
					currentTask.finalizeAction();
					} catch (IOException e1) {
						e1.printStackTrace();
						taskView.getPnlDescription().setText("An error is interrupting the game");
					}
			}
			
			if(AI != currentTask.getCurrentChamp()){
				turnIsStillOn = false;
			}else if(currentTask.getAllowedMoves()>=2){
				actionFlags = new boolean[8];
			}
		}
		
		
		keyReleased(null);
	}
	
	private void useInventoryByAI() {
		// TODO more into multiple usages of potions
		Champion AI = currentTask.getCurrentChamp();
		ArrayList<Collectible> inventory = ((Wizard)AI).getInventory();
		if(inventory.size()>0){
			int randomPotion = (int)(Math.random()*inventory.size());
			currentTask.usePotion((Potion)inventory.get(randomPotion));
		}

		
	}

	private void activateTraitByAI() {
		// TODO more into directions of Slytherin trait
		Champion AI = currentTask.getCurrentChamp();
		
		if(AI instanceof GryffindorWizard){
			activateGryffindorTrait();
			
		}else if(AI instanceof HufflepuffWizard){
			activateHufflepuffTrait();
			
		}else if(AI instanceof SlytherinWizard){
			int randomDirection = (int) (Math.random()*3);
			Direction direction;
			switch(randomDirection){
			case 0: direction = Direction.FORWARD; break;
			case 1: direction = Direction.BACKWARD; break;
			case 2: direction = Direction.RIGHT; break;
			default: direction = Direction.LEFT;
			}
			directionsInAction = new ArrayList<Direction>();
			directionsInAction.add(direction);
			activateSlytherinTrait();
		
		}else if(AI instanceof RavenclawWizard){
			activateRavenclawTrait();	
			
		}
		
	}

	private void castSpellByAI() {
		// TODO more into directions of damaging and relocating spells
		boolean spellIsNotActivated = true;
		boolean[] spellsFlags = new boolean[3];
		Champion AI = currentTask.getCurrentChamp();
		ArrayList<Spell> spells = ((Wizard)AI).getSpells();
		while(spellIsNotActivated){
			
			int randomAction = (int) (Math.random()*2);
			Spell spell = null;
			
			switch(randomAction){
			case 0: if(spellsFlags[0]==false) {spell = spells.get(0); spellsFlags[0]=true; break;}
			case 1:	if(spellsFlags[1]==false) {spell = spells.get(1); spellsFlags[1]=true; break;}
			default: if(spellsFlags[2]==false) {spell = spells.get(2); spellsFlags[2] =true;}
			}
			
			if(spell instanceof HealingSpell){
				castHealingSpell(spell);
			}else if(spell instanceof DamagingSpell){
				Direction direction;
				int randomDirection = (int) (Math.random()*3);
				switch(randomDirection){
				case 0: direction = Direction.BACKWARD;
				case 1: direction = Direction.FORWARD;
				case 2: direction = Direction.LEFT;
				default: direction = Direction.RIGHT;
				}
				spellInAction = spell;
				directionsInAction = new ArrayList<Direction>();
				directionsInAction.add(direction);
				castDamagingSpell();
			}else if(spell instanceof RelocatingSpell){
				Direction direction1;
				int randomDirection1 = (int) (Math.random()*3);
				switch(randomDirection1){
				case 0: direction1 = Direction.BACKWARD;
				case 1: direction1 = Direction.FORWARD;
				case 2: direction1 = Direction.LEFT;
				default: direction1 = Direction.RIGHT;
				}
				Direction direction2;
				int randomDirection2 = (int) (Math.random()*3);
				switch(randomDirection2){
				case 0: direction2 = Direction.BACKWARD;
				case 1: direction2 = Direction.FORWARD;
				case 2: direction2 = Direction.LEFT;
				default: direction2 = Direction.RIGHT;
				}
				spellInAction = spell;
				directionsInAction = new ArrayList<Direction>();
				directionsInAction.add(direction1);
				directionsInAction.add(direction2);
				
				int randomRange = (int) (Math.random()*((RelocatingSpell)spell).getRange()) + 1;
				castRelocatingSpell(randomRange);
				
			}else{
				spellIsNotActivated = false;
			}
			if(AI != currentTask.getCurrentChamp()){
				spellIsNotActivated = false;
			}
		}

		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/*	//Task currentTask;
		if(onTaskFlags[0]=true){
			currentTask = tournament.getFirstTask();
		}else if(onTaskFlags[1]=true){
			currentTask = tournament.getSecondTask();
		}else{ //it is in thirdTask
			currentTask = tournament.getThirdTask();
		}*/
		
		if(loading == true || ((Wizard)currentTask.getCurrentChamp()).isAI() ){
			return;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_H){
			taskView.getInstructions().setVisible(true);
			taskView.getTaskBackground().setVisible(false);
			Timer timer =  new Timer(3000,new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					taskView.getTaskBackground().setVisible(true);
					taskView.getInstructions().setVisible(false);
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
		
		int oldInventorySize = ((Wizard)currentTask.getCurrentChamp()).getInventory().size();
		ArrayList<Collectible> inventory = ((Wizard)currentTask.getCurrentChamp()).getInventory();
	
		
		try {
			if(e.getKeyCode() == KeyEvent.VK_UP && currentlyFree == true){
				currentTask.moveForward();
				showTmpDescription();
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN && currentlyFree == true){
				currentTask.moveBackward(); 
				showTmpDescription();
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT && currentlyFree == true){
				currentTask.moveLeft();
				showTmpDescription();
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT && currentlyFree == true){
				currentTask.moveRight();
				showTmpDescription();
			}
		} catch (OutOfBordersException| InvalidTargetCellException| IOException e1) {
			taskView.getPnlDescription().setText("You can not move here!");
		}
		
		/**
		 * Spells and Inventory KeyActions
		 */
		if(e.getKeyCode() == KeyEvent.VK_A && currentlyFree == true){
			Spell activatedSpell = ((Wizard)currentTask.getCurrentChamp()).getSpells().get(0);
			
			if(activatedSpell instanceof HealingSpell){
				castHealingSpell(activatedSpell);
			}else if(activatedSpell instanceof DamagingSpell){
				spellInAction = activatedSpell;
				currentlyFree = false;
				directionsInAction = new ArrayList<Direction>();
				taskView.getPnlDescription().setText("Damage in which direction!");

			}else if(activatedSpell instanceof RelocatingSpell){
				spellInAction = activatedSpell;
				currentlyFree = false;
				directionsInAction = new ArrayList<Direction>();
				taskView.getPnlDescription().setText("Which direction is your target to relocate?");
			}
			
			
		}else if(e.getKeyCode() == KeyEvent.VK_S && currentlyFree == true){
			Spell activatedSpell = ((Wizard)currentTask.getCurrentChamp()).getSpells().get(1);
			if(activatedSpell instanceof HealingSpell){
				castHealingSpell(activatedSpell);
			}else if(activatedSpell instanceof DamagingSpell){
				spellInAction = activatedSpell;
				currentlyFree = false;
				directionsInAction = new ArrayList<Direction>();
				taskView.getPnlDescription().setText("Damage in which direction!");

			}else if(activatedSpell instanceof RelocatingSpell){
				spellInAction = activatedSpell;
				currentlyFree = false;
				directionsInAction = new ArrayList<Direction>();
				taskView.getPnlDescription().setText("Which direction is your target to relocate?");
			}			
			
		}else if(e.getKeyCode() == KeyEvent.VK_D && currentlyFree == true){
			Spell activatedSpell = ((Wizard)currentTask.getCurrentChamp()).getSpells().get(2);
			if(activatedSpell instanceof HealingSpell){
				castHealingSpell(activatedSpell);
			}else if(activatedSpell instanceof DamagingSpell){
				spellInAction = activatedSpell;
				currentlyFree = false;
				directionsInAction = new ArrayList<Direction>();
				taskView.getPnlDescription().setText("Damage in which direction!");

			}else if(activatedSpell instanceof RelocatingSpell){
				spellInAction = activatedSpell;
				currentlyFree = false;
				directionsInAction = new ArrayList<Direction>();
				taskView.getPnlDescription().setText("Which direction is your target to relocate?");
			}
		}else if(e.getKeyCode() == KeyEvent.VK_E && currentlyFree == true){
			try {
				currentTask.finalizeAction();
				showTmpDescription();
			} catch (IOException e1) {
				taskView.getPnlDescription().setText("error has been found");
			}
		}else if(e.getKeyCode() == KeyEvent.VK_F && currentlyFree == true){
			useInventory(inventory);
			
		}else if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(((Wizard)currentTask.getCurrentChamp()) instanceof GryffindorWizard){
				activateGryffindorTrait();
			}
			if(((Wizard)currentTask.getCurrentChamp()) instanceof SlytherinWizard){
				taskView.getPnlDescription().setText("Slytherin trait is Activated, Enter which direction?");
				currentlyFree = false;
			}
			if(((Wizard)currentTask.getCurrentChamp()) instanceof HufflepuffWizard){
				activateHufflepuffTrait();
			}
			if(((Wizard)currentTask.getCurrentChamp()) instanceof RavenclawWizard){
				activateRavenclawTrait();
			}
		}/*else if(e.getKeyCode() == KeyEvent.VK_B){
			
			//for BattleMode
			
			int directionNum =5;
			try{
				String directionString = "Enter Which Direction" + '\n' +  "1. Forward " + '\n' + "2. Backward " + '\n' + "3. Left" + '\n' + "4. Right";
				directionNum = Integer.parseInt(JOptionPane.showInputDialog(directionString));
			}catch(NumberFormatException e1){
				taskView.getPnlDescription().setText("Enter the Corresponding direction Number");
			}
			Direction direction;
			switch(directionNum){
			case 1: direction = Direction.FORWARD; break;
			case 2: direction = Direction.BACKWARD; break;
			case 3: direction = Direction.LEFT; break;
			default: direction = Direction.RIGHT; break;
			}
			battlemode(direction);
		}*/
		
		/**
		 * Spells Action Moves
		 */
		if(e.getKeyCode() == KeyEvent.VK_UP && currentlyFree == false){	
			directionsInAction.add(Direction.FORWARD);
			if(spellInAction instanceof DamagingSpell && directionsInAction.size()==1){
				castDamagingSpell();
				
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==1){
				taskView.getPnlDescription().setText("Where do you wanna relocate it?");
				
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==2){					
				castRelocatingSpell(0);
				
			}else if(currentTask.getCurrentChamp() instanceof SlytherinWizard && directionsInAction.size()==1){
				activateSlytherinTrait();
			}

			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN && currentlyFree == false){
			directionsInAction.add(Direction.BACKWARD);
			if(spellInAction instanceof DamagingSpell && directionsInAction.size()==1){
				castDamagingSpell();
				
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==1){
				taskView.getPnlDescription().setText("Where do you wanna relocate it?");
				
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==2){
				castRelocatingSpell(0);
				
			}else if(currentTask.getCurrentChamp() instanceof SlytherinWizard && directionsInAction.size()==1){
				activateSlytherinTrait();
			}

		}else if(e.getKeyCode() == KeyEvent.VK_LEFT && currentlyFree == false){
			directionsInAction.add(Direction.LEFT);
			if(spellInAction instanceof DamagingSpell && directionsInAction.size()==1){
				castDamagingSpell();
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==1){
				taskView.getPnlDescription().setText("Where do you wanna relocate it?");
				
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==2){
				castRelocatingSpell(0);
				
			}else if(currentTask.getCurrentChamp() instanceof SlytherinWizard && directionsInAction.size()==1){
				activateSlytherinTrait();
			}

			
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT && currentlyFree == false){
			directionsInAction.add(Direction.RIGHT);
			if(spellInAction instanceof DamagingSpell && directionsInAction.size()==1){
				castDamagingSpell();
			
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==1){
				taskView.getPnlDescription().setText("Where do you wanna relocate it?");
				
			}else if(spellInAction instanceof RelocatingSpell && directionsInAction.size()==2){
				castRelocatingSpell(0);
				
			}else if(currentTask.getCurrentChamp() instanceof SlytherinWizard && directionsInAction.size()==1){
				activateSlytherinTrait();
			}
			
		}
		
		checkForCollectibles(oldInventorySize, inventory);


	}

	private void checkForCollectibles(int oldInventorySize, ArrayList<Collectible>inventory) {
		if(oldInventorySize < inventory.size()){
			Potion lastPotionCollected = (Potion) inventory.get(inventory.size()-1);
			lastPotionCollected.getName(); lastPotionCollected.getAmount();
			String tmp = taskView.getPnlDescription().getText();
	    	taskView.getPnlDescription().setText("Potion : " + lastPotionCollected.getName() +
	    			" with " + lastPotionCollected.getAmount() + " Points.");

			Timer timer = new Timer(2000, new ActionListener() {
			    @Override 
			    public void actionPerformed(ActionEvent arg0) {  
			    	taskView.getPnlDescription().setText(tmp);
			    }
			}); 
			timer.setRepeats(false);
			timer.start();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	
	@Override
	public void keyReleased(KeyEvent e) {
		
		if(currentTask.getChampions().size()==0){
			gameOver();
			return;
		}
		
	/*	if(currentTask.getChampions().size()==1 && ((Wizard)currentTask.getCurrentChamp()).isAI()){
			if(currentTask instanceof FirstTask){
				if(((FirstTask)currentTask).getWinners().size()==0){
					showWinner(currentTask.getCurrentChamp());
					return;
				}
			}else if(currentTask instanceof SecondTask){
				if(((SecondTask)currentTask).getWinners().size()==0){
					showWinner(currentTask.getCurrentChamp());
					return;
				}
			}else{
				if(((FirstTask)currentTask).getWinners().size()==0){
					showWinner(currentTask.getCurrentChamp());
					return;
				}
			}
		}*/
		
		
	/*	if(currentTask.getChampions().size()==1 && ((Wizard)currentTask.getCurrentChamp()).isAI() ){
			showWinner(currentTask.getCurrentChamp());
			return;
		}*/
		
		if(currentTask.getChampions().size()>=1){
			
			Timer timer = new Timer(1000, new ActionListener() {
			    @Override 
			    public void actionPerformed(ActionEvent arg0) {            
			    	try {
						taskView.getPnlMap().removeAll();
						taskView.paintTaskMap(currentTask.getMap());
						
						//In case in the first task , keep Egg picture
						if(currentTask instanceof FirstTask){
							ImageIcon picIcon = new ImageIcon("egg.png");
							picIcon = new ImageIcon(TaskView.getScaledImage(picIcon.getImage(),50,50));
							JLabel eggPic = new JLabel(picIcon); eggPic.setVisible(true);
							taskView.getMapGraphic()[4][4].add(eggPic);	
						}
						
						taskView.updateChampionsData();
						highlightCurrentChampion();
						taskView.repaint();
						taskView.validate();
						
					} catch (OutOfBordersException | InvalidTargetCellException | IOException e1) {
						taskView.getPnlDescription().setText("Man! There is an Error");;
						return;
					}
			    	
			    }
			}); 
			timer.setRepeats(false);
			timer.start();
			
			if(((Wizard)currentTask.getCurrentChamp()).isAI()){
				taskView.getPnlDescription().setText("Computer is playing");
				Timer timer2 = new Timer(2500, new ActionListener() {
				    @Override 
				    public void actionPerformed(ActionEvent arg0) {  
				    	
				    	computerTurn();
						return;
				    }
				}); 
				timer2.setRepeats(false);
				timer2.start();
			}
		
		}else{
			gameOver();
			return;
		}

	}

	private void showTmpDescription(){

		Timer timer = new Timer(4000, new ActionListener() {
		    @Override 
		    public void actionPerformed(ActionEvent arg0) {
		    	taskView.getPnlDescription().setText(tmpLastDescription);
		    }
		}); 
		timer.setRepeats(false);
		timer.start();
	}
	
	public void showTraitIsActivated() {
		if(currentTask.isTraitActivated()){
			if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion1()).getOwner()){
				((PnlChampionStatus)taskView.getPnlChampion1()).getTraitCooldown().setText("Trait is in Activation");
				((PnlChampionStatus)taskView.getPnlChampion1()).setTraitActivated(true);
			}
			if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion2()).getOwner()){
				((PnlChampionStatus)taskView.getPnlChampion2()).getTraitCooldown().setText("Trait is in Activation");
				((PnlChampionStatus)taskView.getPnlChampion2()).setTraitActivated(true);
			}
			if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion3()).getOwner()){
				((PnlChampionStatus)taskView.getPnlChampion3()).getTraitCooldown().setText("Trait is in Activation");
				((PnlChampionStatus)taskView.getPnlChampion3()).setTraitActivated(true);
			}
			if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion4()).getOwner()){
				((PnlChampionStatus)taskView.getPnlChampion4()).getTraitCooldown().setText("Trait is in Activation");
				((PnlChampionStatus)taskView.getPnlChampion4()).setTraitActivated(true);
			}
		}
	}

	private void activateRavenclawTrait() {
		try {
			currentTask.getCurrentChamp().useTrait();
			taskView.getPnlDescription().setText("Ravenclaw House trait is Activated");

		} catch (InCooldownException | OutOfBordersException | InvalidTargetCellException | IOException e1) {
			taskView.getPnlDescription().setText("Not Possible to activate the trait");

		}
		currentlyFree = true;
		showTraitIsActivated();
		showTmpDescription();
	}

	private void activateHufflepuffTrait() {
		try {
			currentTask.getCurrentChamp().useTrait();
			taskView.getPnlDescription().setText("Hufflepuff House trait is Activated");

		} catch (InCooldownException | OutOfBordersException | InvalidTargetCellException | IOException e1) {
			taskView.getPnlDescription().setText("Not Possible to activate the trait");

		}
		currentlyFree = true;
		showTraitIsActivated();
		showTmpDescription();
	}

	private void activateSlytherinTrait() {
		try{
			((SlytherinWizard)currentTask.getCurrentChamp()).setTraitDirection(directionsInAction.remove(0));
			currentTask.getCurrentChamp().useTrait();
			taskView.getPnlDescription().setText("Slytherin House trait is Activated");
		} catch (InCooldownException |OutOfBordersException | InvalidTargetCellException
		        | IOException e1) {
			taskView.getPnlDescription().setText("Not Possible to activate the trait");
		}
		currentlyFree = true;
		showTraitIsActivated();
		showTmpDescription();
	}

	private void activateGryffindorTrait() {
		try {
			currentTask.getCurrentChamp().useTrait();
			taskView.getPnlDescription().setText("Grffindor House trait is Activated");

		} catch (InCooldownException | OutOfBordersException | InvalidTargetCellException | IOException e1) {
			taskView.getPnlDescription().setText("Not Possible to activate the trait");
		}
		currentlyFree = true;
		showTraitIsActivated();
		showTmpDescription();
	}

	
	private void useInventory(ArrayList<Collectible> inventory) {
		if(inventory.size()>0){
			String potionsList = "";
			for(int i=0; i< inventory.size(); i++){
				Potion potion = (Potion) inventory.get(i);
				potionsList = potionsList + (i+1)  + ". "  + potion.getName() + " with " + potion.getAmount() + " Points. " + '\n';
			}
			potionsList = potionsList + "- Which one do you want to (Enter Number) : ";
			int numOfPotion = 100;
			try{
				numOfPotion = Integer.parseInt(JOptionPane.showInputDialog(potionsList));
			}catch(NumberFormatException e1){
				taskView.getPnlDescription().setText("Enter Correct Number");
			}
			try{
				currentTask.usePotion((Potion)inventory.remove(numOfPotion - 1));
				Music effect = new Music("sounds/potion_sound.wav");
				effect.playSound();

			}catch(Exception e2){
				taskView.getPnlDescription().setText("There is Nothing here");
			}
			
		}else{
			taskView.getPnlDescription().setText("You have got nothing in your inventory");
		}
	}

	private void castHealingSpell(Spell activatedSpell) {
		try {
			currentTask.castHealingSpell((HealingSpell)activatedSpell);
			Music effect = new Music("sounds/potion_sound.wav");
			effect.playSound();
			currentlyFree = true;
			taskView.getPnlDescription().setText("HealingSpell Activation");
		} catch (InCooldownException e) {
			taskView.getPnlDescription().setText("The Spell still in cool down.");
		} catch (NotEnoughIPException e) {
			taskView.getPnlDescription().setText("Sorry, You don't have enough IP.");
		} catch (IOException e) {
			taskView.getPnlDescription().setText("Error loading the spell");
		}
		spellInAction = null;
		showTmpDescription();
	}
	
	private void castDamagingSpell(){
		try {
			currentTask.castDamagingSpell((DamagingSpell)spellInAction,directionsInAction.remove(0));
			Music effect = new Music("sounds/damaging_sound.wav");
			effect.playSound();
			taskView.getPnlDescription().setText("Damaging Spell activation");
			currentlyFree = true;
		} catch (InCooldownException e) {
			taskView.getPnlDescription().setText("The Spell still in cool down.");
		} catch (NotEnoughIPException e) {
			taskView.getPnlDescription().setText("Sorry, You don't have enough IP.");
		} catch (OutOfBordersException e) {
			taskView.getPnlDescription().setText("You cannot do it here.");
		} catch (InvalidTargetCellException e) {
			taskView.getPnlDescription().setText("What are you doing?!, you can not do on that.");
		} catch (IOException e) {
			taskView.getPnlDescription().setText("Error loading the spell");
		}
		currentlyFree = true;
		spellInAction = null;
		showTmpDescription();
	}
	
	private void castRelocatingSpell(int rangeIfAI){
		
		
		try{
			if(((Wizard)currentTask.getCurrentChamp()).isAI()){
				currentTask.castRelocatingSpell((RelocatingSpell)spellInAction, directionsInAction.remove(0), directionsInAction.remove(0), rangeIfAI);
			}else{
				int range =10;
				try{
					range = Integer.parseInt(JOptionPane.showInputDialog("Enter How Far?"));
				}catch(NumberFormatException e1){
					taskView.getPnlDescription().setText("Put a Number");
				}
				Music effect = new Music("sounds/relocating_sound.wav");
				effect.playSound();
				currentTask.castRelocatingSpell((RelocatingSpell)spellInAction, directionsInAction.remove(0), directionsInAction.remove(0), range);
			}
			
			taskView.getPnlDescription().setText("Relocating Spell activation");
			currentlyFree = true;
		} catch (InCooldownException e) {
			taskView.getPnlDescription().setText("The Spell still in cool down.");
		} catch (NotEnoughIPException e) {
			taskView.getPnlDescription().setText("Sorry, You don't have enough IP.");
		} catch (OutOfBordersException e) {
			taskView.getPnlDescription().setText("You cannot do it here.");
		} catch (InvalidTargetCellException e) {
			taskView.getPnlDescription().setText("What are you doing?!, you can not do on that.");
		} catch (IOException e) {
			taskView.getPnlDescription().setText("Error loading the spell");
		} catch (OutOfRangeException e1) {
			taskView.getPnlDescription().setText("Relocating Range not possible");
		}
		currentlyFree = true;
		spellInAction = null;
		showTmpDescription();

	}
	
	
	@Override
	public void intializeFirstTask(){

		try {
			taskView.paintTaskMap(tournament.getFirstTask().getMap());
			onTaskFlags[0]=true;
			taskView.getPnlDescription().setText("First Task");
			ImageIcon background = new ImageIcon("firstTask_bg.png");
			background = new ImageIcon(TaskView.getScaledImage(background.getImage(),taskView.getWidth(),taskView.getHeight()));
			taskView.getTaskBackground().setIcon(background);
			
			Music music = new Music("sounds/harrypotter_music.wav");
			music.playSoundLoop();
			currentTask = tournament.getFirstTask();
		} catch (OutOfBordersException | InvalidTargetCellException | IOException e) {
			taskView.getPnlDescription().setText("There is an error while loading First Task");
		} 
		taskView.repaint(); taskView.revalidate();
		
		//Music effect = new Music("sounds/sudden-appearance.wav");
		//effect.playSoundLoop();
	
	}

	@Override
	public void intializeSecondTask(ArrayList<Champion> winners){

		taskView.getPnlMap().removeAll();

    	try {
    		onTaskFlags[0]=false;
    		onTaskFlags[1]=true;
			currentTask = tournament.getSecondTask();
			
			ImageIcon background = new ImageIcon("secondTask_bg.png");
			background = new ImageIcon(TaskView.getScaledImage(background.getImage(),taskView.getWidth(),taskView.getHeight()));
			taskView.getTaskBackground().setIcon(background);
			
			taskView.paintTaskMap(tournament.getSecondTask().getMap());
			taskView.getPnlDescription().setText("Second Task");
			taskView.updateChampionDataForNextTask(tournament.getSecondTask().getChampions());
			highlightCurrentChampion();
			
		} catch (OutOfBordersException | InvalidTargetCellException | IOException e) {
			taskView.getPnlDescription().setText("There is an error while loading Second Task");
		}
		taskView.repaint(); taskView.revalidate();
		
		playTaskIntro("secondTaskIntro.gif");
	}
	

	@Override
	public void intializeThirdTask(ArrayList<Champion> winners) {
		taskView.getPnlMap().removeAll();
		try {
			onTaskFlags[0]=false;
			onTaskFlags[1]=false;
			onTaskFlags[2]=true;
			currentTask = tournament.getThirdTask();
			
			ImageIcon background = new ImageIcon("thirdTask_bg.png");
			background = new ImageIcon(TaskView.getScaledImage(background.getImage(),taskView.getWidth(),taskView.getHeight()));
			taskView.getTaskBackground().setIcon(background);
			
			taskView.paintTaskMap(tournament.getThirdTask().getMap());
			taskView.getPnlDescription().setText("Third Task");
			taskView.updateChampionDataForNextTask(tournament.getThirdTask().getChampions());
			highlightCurrentChampion();
		} catch (OutOfBordersException | InvalidTargetCellException | IOException e) {
			taskView.getPnlDescription().setText("There is an error while loading Third Task");
		}
		taskView.repaint(); taskView.revalidate();
		
		playTaskIntro("thirdTaskIntro.gif");

	}

	
	private void playTaskIntro(String string) {
		
		//Intro Playing Part
    	ImageIcon imageIcon = new ImageIcon(string);
		imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(taskView.getWidth(), taskView.getHeight(), Image.SCALE_DEFAULT));
		taskView.getMapIntro().setIcon(imageIcon);
		taskView.getMapIntro().setVisible(true);
		taskView.getTaskBackground().setVisible(false);
		loading = true;
		
		Music taskMusic = new Music("sounds/taskSoundEffect.wav");
		taskMusic.playSound();

		Timer timer = new Timer(10000, new ActionListener() {
		    @Override 
		    public void actionPerformed(ActionEvent arg0) {
		    	taskView.getMapIntro().setVisible(false);
		    	taskView.getTaskBackground().setVisible(true);
		    	taskView.getPnlDescription().setText(tmpLastDescription);
		    	taskView.getTaskBackground().setVisible(true);
		    	
		    	loading = false;
		    	computerTurn();
		    }
		    
		}); 
		timer.setRepeats(false);
		timer.start();
		
	}

	@Override
	public void awarePlayerOfForeignTreasure(int x, int y) {
		taskView.getPnlDescription().setText("You are trying to step in another Champion's Treasure");
		//ImageIcon picIcon = new ImageIcon("foriegnTreasure.png");
		//picIcon = new ImageIcon(getScaledImage(picIcon.getImage(),80,80));
		taskView.getMapGraphic()[x][y].getComponent(0).setVisible(true);
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override 
		    public void actionPerformed(ActionEvent arg0) {
		    	
				showTmpDescription();
				}
		}); 
		timer.setRepeats(false);
		timer.start();

		
	}
	
	public void showActionEffect(int damageAmount) {
		
		if(currentTask instanceof FirstTask){
			ArrayList<Point> targetPoints = tournament.getFirstTask().getMarkedCells();
			for(Point target: targetPoints){
				int x = target.x;
				int y = target.y;
				taskView.getMapGraphic()[x][y].setBackground(Color.RED);
				taskView.getMapGraphic()[x][y].setOpaque(true);
				taskView.getMapGraphic()[x][y].setVisible(true);	
			}
			
			Music effect = new Music("sounds/dragon_fire.wav");
			effect.playSound();
			taskView.getPnlDescription().setText("Dragon Fires..");
		}
		if(currentTask instanceof SecondTask){
			int x = ((Wizard)currentTask.getCurrentChamp()).getLocation().x;
			int y = ((Wizard)currentTask.getCurrentChamp()).getLocation().y;
			taskView.getMapGraphic()[x][y].setBackground(Color.RED);
			taskView.getMapGraphic()[x][y].setOpaque(true);
			taskView.getMapGraphic()[x][y].setVisible(true);
			
			Music effect = new Music("sounds/ah.wav");
			effect.playSound();
			taskView.getPnlDescription().setText("Merperson Attacks you..   - " + damageAmount + " points");
			
		}
		showTmpDescription();
	}

	@Override
	public void showCurrentWinners(Champion currentChamp) {
		ImageIcon champIcon;
		if(currentChamp instanceof GryffindorWizard){
			champIcon = new ImageIcon("gryff_won.png");
		}else if(currentChamp instanceof HufflepuffWizard){
			champIcon = new ImageIcon("huffle_won.png");
		}else if(currentChamp instanceof RavenclawWizard){
			champIcon = new ImageIcon("raven_won.png");
		}else{ // it is SlytherinWizard
			champIcon = new ImageIcon("slyth_won.png");
		}
		if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion1()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion1()).getChampionPic().setIcon(champIcon);
		}else if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion2()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion2()).getChampionPic().setIcon(champIcon);
		}else if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion3()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion3()).getChampionPic().setIcon(champIcon);
		}else if( currentTask.getCurrentChamp() == ((PnlChampionStatus)taskView.getPnlChampion4()).getOwner()){
			((PnlChampionStatus)taskView.getPnlChampion4()).getChampionPic().setIcon(champIcon);
		}
		
	}
	
	@Override
	public void showWinner(Champion winner) {
		//TODO more effects
		taskView.showWinnerOfTheGame(winner);
	}
	
	@Override
	public void gameOver() {
		taskView.showLosersPanel();
	}
	
	
	public static void main(String []args) throws IOException, OutOfBordersException, InvalidTargetCellException, IllegalAccessException{
		Tournament tournament = new Tournament();
		//this part just to test
		GryffindorWizard g = new GryffindorWizard("gryff");
		//g.setAI(true);
		HufflepuffWizard h = new HufflepuffWizard("huff");
		//h.setAI(true);
		RavenclawWizard r = new RavenclawWizard("raven");
		//r.setAI(true);
		SlytherinWizard s = new SlytherinWizard("slyth");
		s.setAI(true);
		java.util.Collections.shuffle(tournament.getSpells());
		
		ArrayList<Spell> spells = tournament.getSpells();
		for(int i=0;i<3;i++ ){
			((Wizard) g).getSpells().add(spells.remove(0));
			((Wizard) h).getSpells().add(spells.remove(0));
			((Wizard) r).getSpells().add(spells.remove(0));
			((Wizard) s).getSpells().add(spells.remove(0));
		}
		
		tournament.addChampion(g);
		tournament.addChampion(h);
		tournament.addChampion(r);
		tournament.addChampion(s);
		
		new TaskGUI(tournament);
	}


	
	
}
