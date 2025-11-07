import javax.swing.*;

public class FoodItem implements Product{
    private String name;
    private double price;
    private ImageIcon image;

    public FoodItem(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.image = new ImageIcon(imagePath);
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public ImageIcon getImage() { return image; }
}
