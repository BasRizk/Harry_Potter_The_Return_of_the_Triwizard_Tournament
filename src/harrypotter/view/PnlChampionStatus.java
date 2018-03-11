package harrypotter.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import harrypotter.model.character.*;
import harrypotter.model.magic.DamagingSpell;
import harrypotter.model.magic.HealingSpell;
import harrypotter.model.magic.RelocatingSpell;
import harrypotter.model.magic.Spell;

@SuppressWarnings("serial")
public class PnlChampionStatus extends JPanel{
	private JLabel pnlbackground;
	private Champion owner;
	private JLabel championPic;
	private JLabel nameLabel;
	private JLabel hp;
	private JLabel ip;
	private JLabel traitCooldown;
	private JButton castingSpell;
	private JLabel[] spellsBtns;
	private JButton inventory;
	private boolean alive;
	private boolean traitActivated;
	
	public PnlChampionStatus(Champion champion){

		super();
		this.owner = champion;
		this.alive = true;
		this.traitActivated = false;
		Wizard champ = (Wizard) champion;
        this.setBounds(3, 5, 200, 320);
        this.setOpaque(false);
        
        Font gameFont = TaskView.getGameFont(Font.HANGING_BASELINE,30);
        
		ImageIcon graphicIcon = new ImageIcon("championPanel_Normal.png");
		pnlbackground = new JLabel();
		graphicIcon = new ImageIcon(TaskView.getScaledImage(graphicIcon.getImage(),400,450));
		pnlbackground.setIcon(graphicIcon);
		
		pnlbackground.setLayout(new GridBagLayout());
        pnlbackground.setBounds(3, 5, 200, 320);

		this.add(pnlbackground);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        
		championPic = new JLabel();
		ImageIcon champIcon;
		if(champ instanceof GryffindorWizard){
			champIcon = new ImageIcon("gryffk.png");
		}else if(champ instanceof HufflepuffWizard){
			champIcon = new ImageIcon("hufflek.png");
		}else if(champ instanceof RavenclawWizard){
			champIcon = new ImageIcon("ravenk.png");
		}else{ // it is SlytherinWizard
			champIcon = new ImageIcon("slythk.png");
		}
		
    	champIcon = new ImageIcon(TaskView.getScaledImage(champIcon.getImage(),200,200));
		championPic.setIcon(champIcon);


        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 5;
        pnlbackground.add(championPic,c);
        
        JLabel name = new JLabel(" NAME: " + champ.getName().toUpperCase(), SwingConstants.CENTER);
        //name.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 30));
        name.setFont(gameFont);
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 2.0;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 4;
        pnlbackground.add(name,c);
        
        hp = new JLabel(" HP " + champ.getHp() + " ");
        //hp.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 25));
        hp.setFont(gameFont);
        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 6;
        pnlbackground.add(hp,c);
        
        ip = new JLabel(" IP " + champ.getIp() + " " );
        //ip.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 25));
        ip.setFont(gameFont);
        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 2;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 7;
        pnlbackground.add(ip,c);
        
        traitCooldown = new JLabel("Trait " + champ.getTraitCooldown() + " turns.");
        //traitCooldown.setFont(new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 25));
        traitCooldown.setFont(gameFont);
        
        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 8;
        pnlbackground.add(traitCooldown,c);
        
        
        JPanel castSpell = new JPanel();
        castSpell.setOpaque(false);
        spellsBtns = new JLabel[3];
        for(int i=0; i<spellsBtns.length ;i++){
        	spellsBtns[i] = new JLabel();
        	spellsBtns[i].setFont(gameFont);
        	Spell spell = champ.getSpells().get(i);
        	ImageIcon picIcon;
        	if(champ.getSpells().get(i) instanceof HealingSpell){
            	picIcon = new ImageIcon("healing_1.png");
            	HealingSpell healingSpell = (HealingSpell)spell;
            	spellsBtns[i].setText(spell.getCoolDown() + "");
            	spellsBtns[i].setToolTipText(healingSpell.getName() + ", Cost " + healingSpell.getCost() +
            			", Heals " + healingSpell.getHealingAmount() + " Points, Cooldown " + healingSpell.getCoolDown());
            	
        	}else if(champ.getSpells().get(i) instanceof DamagingSpell){
            	picIcon = new ImageIcon("damaging_1.png");
            	spellsBtns[i].setText(champ.getSpells().get(i).getCoolDown() + "");
            	DamagingSpell damagingSpell = (DamagingSpell)spell;
            	spellsBtns[i].setText(spell.getCoolDown() + "");
            	spellsBtns[i].setToolTipText(damagingSpell.getName() + ", Cost " + damagingSpell.getCost() +
            			", Damages " + damagingSpell.getDamageAmount()+ " Points, Cooldown " + damagingSpell.getCoolDown());

        	}else{ // it is a relocating
            	picIcon = new ImageIcon("relocating_1.png");
            	spellsBtns[i].setText(champ.getSpells().get(i).getCoolDown() + "");
            	RelocatingSpell relocatingSpell = (RelocatingSpell)spell;
            	spellsBtns[i].setText(spell.getCoolDown() + "");
            	spellsBtns[i].setToolTipText(relocatingSpell.getName() + ", Cost " + relocatingSpell.getCost() +
            			", Range to " + relocatingSpell.getRange()+ " cells, Cooldown " + relocatingSpell.getCoolDown());

        	}
        	
        	
        	picIcon = new ImageIcon(TaskView.getScaledImage(picIcon.getImage(),100,100));
        	spellsBtns[i].setIcon(picIcon);
        	spellsBtns[i].setOpaque(false);
        	spellsBtns[i].setPreferredSize(new Dimension(120,90));
        	castSpell.add(spellsBtns[i]);
        }
                
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 9;
        pnlbackground.add(castSpell,c);
        
        
        
        inventory = new JButton();
        inventory.setFont(new Font(Font.MONOSPACED,Font.CENTER_BASELINE,20));
        //inventory.setFont(TaskView.getGameFont(Font.PLAIN,20));
        inventory.setText( "Inventory " + ((Wizard)owner).getInventory().size() );
    	inventory.setPreferredSize(new Dimension(300,150));
    	inventory.setEnabled(false);
    	inventory.setBackground(Color.BLACK);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 2;
        c.weightx = 4.0;
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 10;
        pnlbackground.add(inventory,c);
        

        repaint();
        validate();
        
	}
	
	public JLabel getPnlBackground() {
		return pnlbackground;
	}

	public void setPnlBackground(JLabel pnlBackground) {
		this.pnlbackground = pnlBackground;
	}

	public void updateChampionData(){
		
		if(alive == false) return;
		
		Wizard champ = (Wizard) owner;
		
		if(champ.getHp()<=0){
			alive = false;
			this.declareDead();
			return;
		}
		
		hp.setText( " HP " + champ.getHp() + " ");
		ip.setText( " IP " + champ.getIp() + " " );
        traitCooldown.setText("Trait " + champ.getTraitCooldown() + " turns.");
           
        for(int i=0; i<spellsBtns.length ;i++){
        	Spell spell = champ.getSpells().get(i);
        	if(champ.getSpells().get(i) instanceof HealingSpell){
        		HealingSpell healingSpell = (HealingSpell)spell;
            	spellsBtns[i].setText(spell.getCoolDown() + "");
            	spellsBtns[i].setToolTipText(healingSpell.getName() + ", Cost " + healingSpell.getCost() +
            			", Heals " + healingSpell.getHealingAmount() + " Points, Cooldown " + healingSpell.getCoolDown());
        	}else if(champ.getSpells().get(i) instanceof DamagingSpell){
            	spellsBtns[i].setText(champ.getSpells().get(i).getCoolDown() + "");
            	DamagingSpell damagingSpell = (DamagingSpell)spell;
            	spellsBtns[i].setText(spell.getCoolDown() + "");
            	spellsBtns[i].setToolTipText(damagingSpell.getName() + ", Cost " + damagingSpell.getCost() +
            			", Damages " + damagingSpell.getDamageAmount()+ " Points, Cooldown " + damagingSpell.getCoolDown());
        	}else if(champ.getSpells().get(i) instanceof RelocatingSpell){
            	spellsBtns[i].setText(champ.getSpells().get(i).getCoolDown() + "");
            	RelocatingSpell relocatingSpell = (RelocatingSpell)spell;
            	spellsBtns[i].setText(spell.getCoolDown() + "");
            	spellsBtns[i].setToolTipText(relocatingSpell.getName() + ", Cost " + relocatingSpell.getCost() +
            			", Range to " + relocatingSpell.getRange()+ " cells, Cooldown " + relocatingSpell.getCoolDown());
        	}
        	
        }
        //inventory graphic need to be updated
        inventory.setText( "Inventory " + ((Wizard)owner).getInventory().size() );
        
       if(traitActivated == true){
        	traitCooldown.setText("Trait is in Activation");
        	traitActivated = false;
        }
        
	}
	

	public void declareDead() {
		owner = null;
		ImageIcon champIcon;
		champIcon = new ImageIcon("deadIcon.png");
		champIcon = new ImageIcon(TaskView.getScaledImage(champIcon.getImage(),200,200));
		championPic.setIcon(champIcon);
		alive = false;
		
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public JLabel[] getSpellsBtns() {
		return spellsBtns;
	}

	public void setSpellsBtns(JLabel[] spellsBtns) {
		this.spellsBtns = spellsBtns;
	}

	public void setNameLabel(JLabel name) {
		this.nameLabel = name;
	}

	public JLabel getHp() {
		return hp;
	}

	public void setHp(JLabel hp) {
		this.hp = hp;
	}

	public JLabel getIp() {
		return ip;
	}

	public void setIp(JLabel ip) {
		this.ip = ip;
	}

	public JLabel getTraitCooldown() {
		return traitCooldown;
	}

	public void setTraitCooldown(JLabel traitCooldown) {
		this.traitCooldown = traitCooldown;
	}

	public JButton getCastingSpell() {
		return castingSpell;
	}

	public void setCastingSpell(JButton castingSpell) {
		this.castingSpell = castingSpell;
	}

	public JButton getInventory() {
		return inventory;
	}

	public void setInventory(JButton inventory) {
		this.inventory = inventory;
	}

	public boolean isAlive() {
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}


	public boolean isTraitActivated() {
		return traitActivated;
	}


	public void setTraitActivated(boolean traitActivated) {
		this.traitActivated = traitActivated;
	}
	
	public Champion getOwner() {
		return owner;
	}

	public void setOwner(Champion owner) {
		this.owner = owner;
	}

	public JLabel getChampionPic() {
		return championPic;
	}

	public void setChampionPic(JLabel championPic) {
		this.championPic = championPic;
	}

}
