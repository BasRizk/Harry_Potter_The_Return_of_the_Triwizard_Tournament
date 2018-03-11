package harrypotter.view;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import harrypotter.model.character.Champion;
import harrypotter.model.character.Wizard;
import harrypotter.model.magic.DamagingSpell;
import harrypotter.model.magic.HealingSpell;
import harrypotter.model.magic.RelocatingSpell;
import harrypotter.model.magic.Spell;

public class IntroView extends JFrame implements PanelListener, ActionListener {

    private JPanel area;
    private Music music;
    private Music mbutton;
    private ArrayList<Champion> champions;
    private IntroViewListener listener;
    private ArrayList<CharPanel> players;
    private ArrayList<JButton> spellBtns;
    private ArrayList<Spell> spells;
    private int current;
    private Dimension screenSize;
    private double ratioX;
    private double ratioY;
    private double fontFactor;

    public IntroViewListener getListener() {
        return listener;
    }

    public void setListener(IntroViewListener listener) {
        this.listener = listener;
    }

    public JPanel getArea() {
        return area;
    }

    public IntroView() {
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ratioX = screenSize.getWidth()/1920;
        ratioY = screenSize.getHeight()/1080;
        fontFactor = (screenSize.getWidth()*screenSize.getHeight())/(1920*1080);
        
        this.setUndecorated(true);
        
        players = new ArrayList<CharPanel>();
        champions = new ArrayList<Champion>();
        setTitle("Intro");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mbutton = new Music("buttonClick.wav");
                
        IntroPanel area = new IntroPanel();
        area.setFrame(this);
        setContentPane(area.getLabel());
        getContentPane().add(area);
        
        music = area.getMusic();
        music.playSound();
          
        repaint();
        revalidate();
        
    }
    
//    public void charSelect() {
//
//        JButton gryff = new JButton();
//        ImageIcon gryffPic = new ImageIcon("gryff.png");
//        gryff.setIcon(gryffPic);
//        JButton slyth = new JButton();
//        ImageIcon slythPic = new ImageIcon("slyth.png");
//        slyth.setIcon(slythPic);
//        JButton raven = new JButton();
//        ImageIcon ravenPic = new ImageIcon("raven.png");
//        raven.setIcon(ravenPic);
//        JButton huffle = new JButton();
//        ImageIcon hufflePic = new ImageIcon("huffle.png");
//        huffle.setIcon(hufflePic);
//        
//        houses.add(gryff);
//        houses.add(slyth);
//        houses.add(raven);
//        houses.add(huffle);
//
//        repaint();
//        revalidate();
//    }
    
    public void spellPanel (ArrayList<Spell> s, ArrayList<Champion> c, int i) {
        
        JLabel label = new JLabel();
        champions = c;
        current = i;
        spellBtns = new ArrayList<JButton>();

        
        label.setLayout(new BorderLayout());
        ImageIcon blur = new ImageIcon("blurry.png");
        ImageIcon background = new ImageIcon(getScaledImage(blur.getImage(),(int) (1920*ratioX),(int) (1080*ratioY)));
        label.setIcon(background);
        
        JPanel playerNum = new JPanel();
        
        JLabel playerLab = new JLabel();
        playerLab.setHorizontalTextPosition(JLabel.CENTER);
        playerLab.setText("Player " + (current+1));
        int style = Font.PLAIN | Font.ITALIC;
        playerLab.setFont(new Font("Jokerman", style , (int) (36*fontFactor)));
        Color goldYell = new Color(0x063C0B);
        playerLab.setForeground(goldYell);
        ImageIcon wood =new ImageIcon("woodBoard.png");
        ImageIcon woodPlayer = new ImageIcon(getScaledImage(wood.getImage(), (int) (400*ratioX), (int) (100*ratioY)));
        playerLab.setIcon(woodPlayer);
        playerLab.setOpaque(false);
        playerNum.add(playerLab);
        playerNum.setOpaque(false);
        label.add(playerNum,BorderLayout.NORTH);
        
        setContentPane(label);
        area = new JPanel();
        area.setOpaque(false);
        
        JPanel damaging = new JPanel();
        damaging.setLayout(new BorderLayout());
        damaging.setOpaque(false);
        
        JPanel damagingTitle = new JPanel();
        
        JLabel damageLabel = new JLabel();
        damageLabel.setHorizontalTextPosition(JLabel.CENTER);
        damageLabel.setText("Damaging Spells");
        damageLabel.setFont(new Font("Jokerman", style , (int) (36*fontFactor)));
        damageLabel.setForeground(goldYell);
        ImageIcon woodS = new ImageIcon(getScaledImage(wood.getImage(), (int) (400*ratioX), (int) (100*ratioY)));
        damageLabel.setIcon(woodS);
        damageLabel.setOpaque(false);
        damagingTitle.add(damageLabel);
        damagingTitle.setOpaque(false);
        
        damaging.add(damagingTitle, BorderLayout.NORTH);
        
        JPanel damagingSpell = new JPanel();
        damagingSpell.setLayout(new GridLayout(0, 1));
        damagingSpell.setOpaque(false);
        
        JPanel relocating = new JPanel();
        relocating.setLayout(new BorderLayout());
        relocating.setOpaque(false);
        
        JPanel relocatingTitle = new JPanel();
        
        JLabel relocatingLabel = new JLabel();
        relocatingLabel.setHorizontalTextPosition(JLabel.CENTER);
        relocatingLabel.setText("relocating Spells");
        relocatingLabel.setFont(new Font("Jokerman", style , (int) (36*fontFactor)));
        relocatingLabel.setForeground(goldYell);
        relocatingLabel.setIcon(woodS);
        relocatingLabel.setOpaque(false);
        relocatingTitle.add(relocatingLabel);
        relocatingTitle.setOpaque(false);
        
        relocating.add(relocatingTitle, BorderLayout.NORTH);
        
        JPanel relocatingSpell = new JPanel();
        relocatingSpell.setLayout(new GridLayout(0, 1));
        relocatingSpell.setOpaque(false);
        
        JPanel healing = new JPanel();
        healing.setLayout(new BorderLayout());
        healing.setOpaque(false);
        
        JPanel healingTitle = new JPanel();
        
        JLabel healingLabel = new JLabel();
        healingLabel.setHorizontalTextPosition(JLabel.CENTER);
        healingLabel.setText("healing Spells");
        healingLabel.setFont(new Font("Jokerman", style , (int) (36*fontFactor)));
        healingLabel.setForeground(goldYell);
        healingLabel.setIcon(woodS);
        healingLabel.setOpaque(false);
        healingTitle.add(healingLabel);
        healingTitle.setOpaque(false);
        
        healing.add(healingTitle, BorderLayout.NORTH);
        
        JPanel healingSpell = new JPanel();
        healingSpell.setLayout(new GridLayout(0, 1));
        healingSpell.setOpaque(false);
        
        for (int j = 0; j<s.size(); j++){
            JButton spell = new JButton();
            if (s.get(j) instanceof DamagingSpell) {
                damagingSpell.add(spell);
                spell.setText(s.get(j).getName() + ", "
                        + "Damage: " + ((DamagingSpell) s.get(j)).getDamageAmount() + ", "
                        + "Cost: " + s.get(j).getCost() + ", "
                        + "CoolDown: " + s.get(j).getDefaultCooldown());
            }
            if (s.get(j) instanceof RelocatingSpell) {
                relocatingSpell.add(spell);
                spell.setText(s.get(j).getName() + ", "
                        + "Range: " + ((RelocatingSpell) s.get(j)).getRange() + ", "
                        + "Cost: " + s.get(j).getCost() + ", "
                        + "CoolDown: " + s.get(j).getDefaultCooldown());
            }
            if (s.get(j) instanceof HealingSpell) {
                healingSpell.add(spell);
                spell.setText(s.get(j).getName() + ", "
                        + "Amount: " + ((HealingSpell) s.get(j)).getHealingAmount() + ", "
                        + "Cost: " + s.get(j).getCost() + ", "
                        + "CoolDown: " + s.get(j).getDefaultCooldown());
            }
            spell.addActionListener(this);
            spellBtns.add(spell);
            repaint();
            revalidate();
            
        }
        
        damaging.add(damagingSpell);
        relocating.add(relocatingSpell);
        healing.add(healingSpell);
        
        area.add(damaging, BorderLayout.WEST);
        area.add(relocating, BorderLayout.CENTER);
        area.add(healing, BorderLayout.EAST);
        add(area);
        spells =s;
        repaint();
        revalidate();
        
    }

    public Music getMbutton() {
        return mbutton;
    }

    public void onFinishingPanel(JPanel p) {
        
        if (p instanceof IntroPanel) {
            
            mbutton.playSound();
            CharPanel area = new CharPanel("Player 1");
            setContentPane(area.getLabel());
            area.setFrame(this);
            add(area.getScreen());
        
        }
        
        if (p instanceof CharPanel) {
            champions.add(((CharPanel) p).getChamp());
            players.add((CharPanel) p);
            
            if (champions.size()<4){
                CharPanel area = new CharPanel("Player " + (champions.size()+1));
                area.setFrame(this);
                setContentPane(area.getLabel());
                add(area.getScreen());
            }else{
                listener.champDone(champions);
            }
        }
        
        repaint();
        revalidate();
        
    }
    
    public Image getScaledImage(Image inImage, int width, int height){
        
        BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(inImage, 0, 0, width, height, null);
        g2.dispose();

        return outImage;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        JButton button = (JButton) e.getSource();
        music = new Music ("buttonClick.wav");
        music.playSound();
        Spell spellIn = null;
        String sName = spells.get(spellBtns.indexOf(button)).getName();
        int sCost = spells.get(spellBtns.indexOf(button)).getCost();
        int sCoolDown = spells.get(spellBtns.indexOf(button)).getDefaultCooldown();
        if (spells.get(spellBtns.indexOf(button)) instanceof DamagingSpell){
            int Amount = ((DamagingSpell) spells.get(spellBtns.indexOf(button))).getDamageAmount();
            spellIn = new DamagingSpell(sName, sCost, sCoolDown, Amount);
        }
        if (spells.get(spellBtns.indexOf(button)) instanceof RelocatingSpell){
            int Amount = ((RelocatingSpell) spells.get(spellBtns.indexOf(button))).getRange();
            spellIn = new RelocatingSpell(sName, sCost, sCoolDown, Amount);
        }
        if (spells.get(spellBtns.indexOf(button)) instanceof HealingSpell){
            int Amount = ((HealingSpell) spells.get(spellBtns.indexOf(button))).getHealingAmount();
            spellIn = new HealingSpell(sName, sCost, sCoolDown, Amount);
        }
        ((Wizard) champions.get(current)).getSpells().add(spellIn);
        if (((Wizard) champions.get(current)).getSpells().size() == 3)
            listener.spellDone();
    }

    
}
