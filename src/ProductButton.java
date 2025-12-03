import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

class ProductButton extends JButton {
    private final Image image;
    private final String name;
    private final String price;
    private final Font titleFont;

    public ProductButton(String name, double price, Image image) throws IOException, FontFormatException {
        this.name = name;
        this.price = String.format("%.2f", price);
        this.image = image;
        this.titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Akwe-Pro-Bold.otf")).deriveFont(12f);

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        g2.setFont(titleFont);

        int nameX = 3;
        int nameY = 16 ;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.drawString(name, nameX + 1, nameY + 1);
        g2.setColor(Color.decode("#FEFCEF"));
        g2.drawString(name, nameX, nameY);

        String priceText = price;
        int priceX = 3;
        int priceY = getHeight() - 6;

        g2.setFont(titleFont.deriveFont(18f));
        g2.setColor(new Color(0, 0, 0, 200));
        g2.drawString(priceText, priceX + 1, priceY + 1);
        g2.setColor(Color.decode("#FEFCEF"));
        g2.drawString(priceText, priceX, priceY);

        g2.dispose();

    }
}