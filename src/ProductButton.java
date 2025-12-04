import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

class ProductButton extends JButton {
    private final Image image;
    private final String name;
    private final String price;
    private final Font titleFont;
    private boolean hover = false;

    public ProductButton(String name, double price, Image image) throws IOException, FontFormatException {
        this.name = name;
        this.price = String.format("%.2f", price);
        this.image = image;
        this.titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/SFPRODISPLAYBOLD.OTF"));

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hover = true;
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.decode("#FFFFFF"));
        g2.fillRect(0, 0, 157, 185);

        if (hover) {
            g2.setColor(Color.decode("#474747"));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRect(0, 0, 156, 184);
        }

        int imgMargin = 10;
        int imgWidth = 135;
        int imgHeight = 135;

        g2.setClip(new Rectangle(11, imgMargin, imgWidth, imgHeight));
        g2.drawImage(image, 11, imgMargin, imgWidth, imgHeight, this);
        g2.setClip(null);

        g2.setFont(titleFont.deriveFont(12f));
        FontMetrics fm = g2.getFontMetrics();
        int nameX = (getWidth() - fm.stringWidth(name)) / 2;
        int nameY = imgMargin + imgHeight + fm.getAscent() + 3;
        g2.setColor(Color.decode("#000000"));
        g2.drawString(name, nameX, nameY);

        String priceText = price;
        g2.setFont(titleFont.deriveFont(10f));
        fm = g2.getFontMetrics();
        int priceX = (getWidth() - fm.stringWidth(priceText)) / 2;
        int priceY = nameY + fm.getHeight() + 1;
        g2.setColor(Color.decode("#069122"));
        g2.drawString(priceText, priceX, priceY);

        g2.dispose();
    }
}