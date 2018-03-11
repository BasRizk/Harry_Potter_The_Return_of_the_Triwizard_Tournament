package harrypotter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IntroPanel extends JPanel implements ActionListener {
    
    private JButton start;
    private JLabel label;
    private PanelListener frame;
    private Music music;
    private Dimension screenSize;
    private double ratioX;
    private double ratioY;
    
    public IntroPanel() {
        
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ratioX = screenSize.getWidth()/1920;
        ratioY = screenSize.getHeight()/1080;
        
        start = new JButton();
        label = new JLabel();
        
        label.setLayout(new FlowLayout(FlowLayout.CENTER));
        ImageIcon introPic = new ImageIcon("intro.png");
        ImageIcon introPicResized = new ImageIcon(getScaledImage(introPic.getImage(),(int)(1920*ratioX),(int)(1080*ratioY)));
        label.setIcon(introPicResized);
        
        start.setPreferredSize(new Dimension((int) (1920*ratioX),(int)(1080*ratioY)));
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);
        start.addActionListener(this);
        
        add(start);
        setOpaque(false);
        
        music = new Music("intro.wav");
                
        repaint();
        revalidate();
        
    }

    public PanelListener getFrame() {
        return frame;
    }

    public void setFrame(PanelListener frame) {
        this.frame = frame;
    }

    public JButton getStart() {
        return start;
    }

    public JLabel getLabel() {
        return label;
    }
    
    public Music getMusic() {
        return music;
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
    public void actionPerformed (ActionEvent e) {
        frame.onFinishingPanel(this);
    }

}
