package harrypotter.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import harrypotter.model.character.*;

@SuppressWarnings("serial")
public class CharPanel extends JPanel implements ActionListener {
    
    private JLabel label;
    private PanelListener frame;
    private ArrayList<JButton> houses;
    private Boolean[] mouseActivated;
    private ImageIcon[] origImag;
    private JPanel selection;
    private JPanel title;
    private JPanel screen;
    private JTextField tx;
    private JPanel player;
    private JButton submit;
    private Champion champ;
    private Music mButton;
    private boolean houseClicked;
    private String playerNum;
    private Dimension screenSize;
    private double ratioX;
    private double ratioY;
    private double fontFactor;
    private JButton yesAI;
    private JButton noAI;
    private Boolean AIPlayer;
    
    public CharPanel(String plNum) {
        
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ratioX = screenSize.getWidth()/1920;
        ratioY = screenSize.getHeight()/1080;
        fontFactor = screenSize.getWidth()/1920;

        
        playerNum = plNum;
        origImag = new ImageIcon[4];
        mButton = new Music("buttonClick.wav");
        houseClicked = false;
        
        houses = new ArrayList<JButton>();
        mouseActivated = new Boolean[4];
        mouseActivated[0] = true;
        mouseActivated[1] = true;
        mouseActivated[2] = true;
        mouseActivated[3] = true;
        
        ImageIcon backgroundNotR = new ImageIcon("blurry.png");
        ImageIcon background = new ImageIcon(getScaledImage(backgroundNotR.getImage(),(int) (1920*ratioX),(int) (1080*ratioY)));
        label = new JLabel();
        label.setLayout(new FlowLayout(FlowLayout.CENTER));
        label.setIcon(background);
        
        selection = new JPanel();
        selection.setOpaque(false);
       
        int style = Font.PLAIN | Font.ITALIC;
        
        title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BorderLayout());
        
        ImageIcon wood =new ImageIcon("woodBoard.png");
        
        JLabel woodBoard =new JLabel("Select a School");
        woodBoard.setHorizontalTextPosition(JLabel.CENTER);
        woodBoard.setFont(new Font("Jokerman", style ,(int) (30*fontFactor)));
        Color goldYell = new Color(0x063C0B);
        woodBoard.setForeground(goldYell);
        ImageIcon woodSchool = new ImageIcon(getScaledImage(wood.getImage(),(int) (300*ratioX),(int) (100*ratioY)));
        woodBoard.setIcon(woodSchool);
        woodBoard.setOpaque(false);
        title.add(woodBoard, BorderLayout.NORTH);
        
        JLabel name =new JLabel("Enter Champion name");
        name.setHorizontalTextPosition(JLabel.CENTER);
        name.setFont(new Font("Jokerman", style , (int) (30*fontFactor)));
        name.setForeground(goldYell);
        ImageIcon woodName = new ImageIcon(getScaledImage(wood.getImage(),(int) (400*ratioX),(int) (100*ratioY)));
        name.setIcon(woodName);
        name.setOpaque(false);
        title.add(name, BorderLayout.SOUTH);
        
        submit = new JButton();
        submit.setPreferredSize(new Dimension((int)(210*ratioX),(int)(110*ratioY)));
        ImageIcon woodSubmit = new ImageIcon(getScaledImage(wood.getImage(),(int)(200*ratioX),(int)(100*ratioY)));
        submit.setIcon(woodSubmit);
        submit.setOpaque(false);
        submit.setContentAreaFilled(false);
        submit.setBorderPainted(false);
        
        JLabel submitTx = new JLabel("     Submit");
        submitTx.setFont(new Font("Jokerman", style , (int) (30*fontFactor)));
        submitTx.setForeground(goldYell);
        
        submit.add(submitTx, SwingConstants.CENTER);
        submit.setAlignmentX(SwingConstants.CENTER);
        submit.addActionListener(this);

        

        
        JButton gryff = new JButton("Gryffindor");
        houses.add(gryff);
        gryff.setLayout(new BorderLayout());
        gryff.setPreferredSize(new Dimension((int)(200*ratioX),(int)(237*ratioY)));
        gryff.addActionListener(this);
        ImageIcon gryffk = new ImageIcon("gryffk.png");
        ImageIcon gryffkResized = new ImageIcon(getScaledImage(gryffk.getImage(),(int)(166*ratioX),(int)(194*ratioY)));
        origImag[0] = gryffkResized;
        gryff.setIcon(gryffkResized);
        gryff.setOpaque(false);
        gryff.setContentAreaFilled(false);
        gryff.setBorderPainted(false);
        selection.add(gryff,BorderLayout.CENTER);
        gryff.setFocusPainted(false);
      
        gryff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon gryffkC = new ImageIcon("gryffClick.png");
                ImageIcon gryffkCR = new ImageIcon(getScaledImage(gryffkC.getImage(),(int)(166*(ratioX+0.1)),(int)(197*(ratioY+0.1))));
                gryff.setIcon(gryffkCR);}
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (mouseActivated[0])
                    gryff.setIcon(gryffkResized);
            }
        });
        
        
        JButton huffle= new JButton("Hufflepuff");
        houses.add(huffle);
        huffle.setPreferredSize(new Dimension((int)(200*ratioX),(int)(237*ratioY)));
        huffle.setLayout(new BorderLayout());
        huffle.addActionListener(this);
        ImageIcon hufflek = new ImageIcon("hufflek.png");
        ImageIcon hufflekResized = new ImageIcon(getScaledImage(hufflek.getImage(),(int)(166*ratioX),(int)(194*ratioY)));
        origImag[1] = hufflekResized;
        huffle.setIcon(hufflekResized);
        huffle.setOpaque(false);
        huffle.setContentAreaFilled(false);
        huffle.setBorderPainted(false);
        selection.add(huffle,FlowLayout.CENTER);
        huffle.setFocusPainted(false);
        
        huffle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon hufflekC = new ImageIcon("huffleClick.png");
                ImageIcon hufflekCR = new ImageIcon(getScaledImage(hufflekC.getImage(),(int)(166*(ratioX+0.1)),(int)(197*(ratioY+0.1))));
                huffle.setIcon(hufflekCR);
                }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (mouseActivated[1])
                    huffle.setIcon(hufflekResized);
            }
        });
        
        
        JButton raven= new JButton("Ravenclaw");
        houses.add(raven);
        raven.setPreferredSize(new Dimension((int)(200*ratioX),(int)(237*ratioY)));
        raven.setLayout(new FlowLayout());
        raven.addActionListener(this);
        ImageIcon ravenk = new ImageIcon("ravenk.png");
        ImageIcon ravenkResized = new ImageIcon(getScaledImage(ravenk.getImage(),(int)(166*(ratioX)),(int)(194*(ratioY))));
        origImag[2] = ravenkResized;
        raven.setIcon(ravenkResized);
        raven.setOpaque(false);
        raven.setContentAreaFilled(false);
        raven.setBorderPainted(false);
        selection.add(raven,FlowLayout.CENTER);
        raven.setFocusPainted(false);
        
        raven.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon ravenkC = new ImageIcon("ravenClick.png");
                ImageIcon ravenkCR = new ImageIcon(getScaledImage(ravenkC.getImage(),(int)(166*(ratioX+0.1)),(int)(197*(ratioY+0.1))));
                raven.setIcon(ravenkCR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (mouseActivated[2])
                    raven.setIcon(ravenkResized);
            }
        });
        
        JButton slyth = new JButton("Slytherin");
        houses.add(slyth);
        slyth.setPreferredSize(new Dimension((int)(200*ratioX),(int)(237*ratioY)));
        slyth.setLayout(new FlowLayout());
        slyth.addActionListener(this);
        ImageIcon slythk = new ImageIcon("slythk.png");
        ImageIcon slythkResized = new ImageIcon(getScaledImage(slythk.getImage(),(int)(152*(ratioX)),(int)(194*(ratioY))));
        origImag[3] = slythkResized;
        slyth.setIcon(slythkResized);
        slyth.setOpaque(false);
        slyth.setContentAreaFilled(false);
        slyth.setBorderPainted(false);
        selection.add(slyth,FlowLayout.CENTER);
        slyth.setFocusPainted(false);
        
        slyth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImageIcon slythkC = new ImageIcon("slythClick.png");
                ImageIcon slythkCR = new ImageIcon(getScaledImage(slythkC.getImage(),(int)(166*(ratioX + 0.1)),(int)(197*(ratioY + 0.1))));
                slyth.setIcon(slythkCR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (mouseActivated[3])
                    slyth.setIcon(slythkResized);
            }
        });
        
        JPanel AI =new JPanel();
        AI.setOpaque(false);
        
        
        JLabel AIText = new JLabel("AI");
        AIText.setHorizontalTextPosition(JLabel.CENTER);
        AIText.setFont(new Font("Jokerman", style , (int) (30*fontFactor)));
        AIText.setForeground(goldYell);
        ImageIcon woodAI = new ImageIcon(getScaledImage(wood.getImage(),(int) (400*ratioX),(int) (100*ratioY)));
        AIText.setIcon(woodAI);
        AIText.setOpaque(false);
        AI.add(AIText, BorderLayout.NORTH);
        
        yesAI = new JButton("Computer");
        yesAI.addActionListener(this);
        noAI = new JButton("Player");
        noAI.addActionListener(this);
        AIPlayer= false;
        
        
        
        
        screen =new JPanel();
        player = new JPanel();
        player.setOpaque(false);
        
        JLabel playerLab = new JLabel(playerNum);
        playerLab.setHorizontalTextPosition(JLabel.CENTER);
        playerLab.setFont(new Font("Jokerman", style , (int) (36*fontFactor)));
        playerLab.setForeground(goldYell);
        ImageIcon woodPlayer = new ImageIcon(getScaledImage(wood.getImage(),(int)(400*ratioX),(int)(100*ratioY)));
        playerLab.setIcon(woodPlayer);
        playerLab.setOpaque(false);
        player.add(playerLab);
        
        tx = new JTextField();
        tx.setPreferredSize(new Dimension((int)(310*ratioX),(int)(60*ratioY)));
        tx.setBackground(new Color(0xA4A4A4));
        tx.setFont(new Font("Jokerman", style , (int) (30*fontFactor)));
//        tx.setOpaque(false);
//        tx.setCaretPosition();
        
        player.setPreferredSize(new Dimension((int)(400*ratioX),(int)(100*ratioY)));
        title.setPreferredSize(new Dimension((int)(600*ratioX),(int)(380*ratioY)));
        selection.setPreferredSize(new Dimension((int)(820*ratioX),(int)(270*ratioY)));
        screen.setPreferredSize(new Dimension((int)(1920*ratioX),(int)(1080*ratioY)));
        screen.setLayout(null);
        screen.add(player);
        player.setBounds((int)(810*ratioX),(int)(10*ratioY), (int)(410*ratioX),(int)(120*ratioY));
        screen.add(submit);
        submit.setBounds((int)(900*ratioX),(int)(850*ratioY), (int)(220*ratioX),(int)(120*ratioY));
        screen.add(title);
        title.setBounds((int)(120*ratioX),(int)(200*ratioY),(int)(500*ratioX),(int)(380*ratioY));
        screen.add(selection);
        selection.setBounds((int)(700*ratioX),(int)(180*ratioY),(int)(860*ratioX),(int)(300*ratioY));
        screen.add(tx);
        tx.setBounds((int)(870*ratioX),(int)(520*ratioY),(int)(320*ratioX),(int)(80*ratioY));
        screen.add(AI);
        AI.setBounds((int)(80*ratioX),(int)(680*ratioY),(int)(500*ratioX),(int)(380*ratioY));
        screen.add(yesAI);
        yesAI.setBounds((int)(920*ratioX),(int)(710*ratioY),(int)(100*ratioX),(int)(30*ratioY));
        screen.add(noAI);
        noAI.setBounds((int)(1030*ratioX),(int)(710*ratioY),(int)(100*ratioX),(int)(30*ratioY));
        screen.setOpaque(false);


        
        repaint();
        revalidate();
        
    }

    public PanelListener getFrame() {
        return frame;
    }

    public void setPlayerNum(String playerNum) {
        this.playerNum = playerNum;
    }

    public void setFrame(PanelListener frame) {
        this.frame = frame;
    }

    public JLabel getLabel() {
        return label;
    }

    public JPanel getSelection() {
        return selection;
    }

    public JPanel getTitle() {
        return title;
    }

    public JPanel getScreen() {
        return screen;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        JButton button = (JButton) e.getSource();
        
        if(button == yesAI){
        	AIPlayer = true;
        }
        if(button == noAI){
        	AIPlayer = false;
        }
        //System.out.print(AIPlayer + "First +   ");
        
        if (houses.contains(button)) {
            if((houses.get(0).equals(button)))        tx.setForeground(new Color(0xA60A0A));
            if((houses.get(1).equals(button)))        tx.setForeground(new Color(0xAB6506));
            if((houses.get(2).equals(button)))        tx.setForeground(new Color(0x020770));
            if((houses.get(3).equals(button)))        tx.setForeground(new Color(0x036807));
            
            for(int i = 0; i<4; i++){
                mouseActivated[i] = true;
                if (!(houses.get(i).equals(button)))
                    houses.get(i).setIcon(origImag[i]);
            }
            mouseActivated[houses.indexOf(button)] = false;
            houseClicked = true;
        }
        
        if(button.equals(submit)) {
            if (!(tx.getText().equals(""))) {
                if (tx.getForeground().equals(new Color (0xA60A0A))) {
                    String champName = tx.getText();
                    champ = new GryffindorWizard(champName);
                }
                if (tx.getForeground().equals(new Color (0xAB6506))) {
                    String champName = tx.getText();
                    champ = new HufflepuffWizard(champName);
                }
                if (tx.getForeground().equals(new Color (0x020770))) {
                    String champName = tx.getText();
                    champ = new RavenclawWizard(champName);
                }
                if (tx.getForeground().equals(new Color (0x036807))) {
                    String champName = tx.getText();
                    champ = new SlytherinWizard(champName);
                }
                
                if(champ != null && AIPlayer){
                	((Wizard) champ).setAI(true);
                }
                
                //System.out.println(((Wizard)champ).isAI() + "   ");
                if(houseClicked){
                    mButton.playSound();
                    frame.onFinishingPanel(this);
                }
  
            }
        }
        
    }
    
    public Champion getChamp() {
        return champ;
    }

    public JButton getSubmit() {
        return submit;
    }

    public Image getScaledImage(Image inImage, int width, int height){
        
        BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(inImage, 0, 0, width, height, null);
        g2.dispose();

        return outImage;
        
    }

}
