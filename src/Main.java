import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

class totalDisplay {
    private JTable cartTable;
    final double tax = 8.00;

    public void setCartTable(JTable table) {
        this.cartTable = table;
    }

    double calculateSubtotal() {
        double subtotal = 0.00;
        for (int i = 1; i < cartTable.getRowCount(); i++) {
            int quantity = Integer.parseInt(cartTable.getValueAt(i, 1).toString());
            double price = Double.parseDouble(cartTable.getValueAt(i, 2).toString());
            subtotal += quantity * price;
        }

        return Double.parseDouble(String.format("%.2f", subtotal));
    }

    double calculateTax() {
        return Double.parseDouble(String.format("%.2f", ((tax / 100) * calculateSubtotal())));
    }

    double calculateTotal() {
        return Double.parseDouble(String.format("%.2f", (calculateSubtotal() + calculateTax())));
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Point of Sale System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("POINT OF SALE SYSTEM");
        title.setFont(new Font("Century Gothic", Font.BOLD, 30));
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(0, 10, 800, 60);
        title.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

        //menu display
        JPanel menu = new JPanel();
        menu.setBounds(0, 70, 400, 380);
        menu.setLayout(null);

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
        menuLabel.setForeground(Color.BLACK);
        menuLabel.setHorizontalAlignment(JLabel.LEFT);
        menuLabel.setBounds(10, 0, 400, 50);

        //create products
        Product[] products = {
                new FoodItem("Berry Danish", 120.00, "images/berry-danish.png"),
                new FoodItem("Caramel Frappuccino", 180.00, "images/caramel-frappuccino.png"),
                new FoodItem("Latte", 150.00,  "images/latte.png"),
                new FoodItem("Matcha Swiss Roll", 130.00, "images/matcha-swiss-roll.png"),
                new FoodItem("Mocha Coffee", 160.00, "images/mocha-coffee.png"),
                new FoodItem("Strawberry Cheesecake", 180.00,  "images/strawberry-cheesecake.png"),
        };

        //product buttons
        JPanel productPanel = new JPanel(new  GridLayout(3, 2, 10, 10));
        productPanel.setBounds(25, 55, 350, 300);
        for (Product product : products) {
            ImageIcon rawIcon = product.getImage();
            Image scaledImage = rawIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            String label = "<html>" + product.getName() + "<br>₱" + String.format("%.2f", product.getPrice()) + "</html>";
            JButton prodButton = new JButton(label, scaledIcon);

            prodButton.setHorizontalAlignment(JButton.LEFT);
            prodButton.setHorizontalTextPosition(JButton.RIGHT);
            prodButton.setVerticalTextPosition(JButton.CENTER);
            prodButton.setFont(new Font("Century Gothic", Font.BOLD, 12));
            prodButton.setFocusPainted(false);
            prodButton.setBackground(Color.WHITE);
            prodButton.setOpaque(true);
            prodButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            productPanel.add(prodButton);
        }

        //cart display
        JPanel cart = new JPanel();
        cart.setBackground(Color.RED);
        cart.setBounds(400, 70, 400, 280);
        cart.setLayout(null);

        JLabel cartLabel = new JLabel("CURRENT ORDER");
        cartLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
        cartLabel.setForeground(Color.BLACK);
        cartLabel.setHorizontalAlignment(JLabel.LEFT);
        cartLabel.setBounds(10, 0, 400, 50);

        //clear order and check out buttons
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.BLUE);
        buttons.setBounds(0, 450, 400, 80);

        //total display
        JPanel display = new JPanel();
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));

        totalDisplay totals = new totalDisplay();
        totals.setCartTable(cartTable);

        JLabel subtotalLabel = new JLabel("SUBTOTAL: " + "₱" + totals.calculateSubtotal());
        subtotalLabel.setFont(new Font("Century Gothic",Font.BOLD, 28));
        subtotalLabel.setForeground(Color.BLACK);
        subtotalLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel taxLabel = new JLabel("TAX: " + "₱" + totals.calculateTax());
        taxLabel.setFont(new Font("Century Gothic",Font.BOLD, 28));
        taxLabel.setForeground(Color.BLACK);
        taxLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel totalLabel = new JLabel("TOTAL: " + "₱" + totals.calculateTotal());
        totalLabel.setFont(new Font("Century Gothic",Font.BOLD, 30));
        totalLabel.setForeground(Color.BLACK);
        totalLabel.setHorizontalAlignment(JLabel.LEFT);

        display.add(subtotalLabel);
        display.add(taxLabel);
        display.add(totalLabel);

        display.setBounds(410, 450, 400, 180);

        frame.add(title);
        menu.add(menuLabel);
        menu.add(productPanel);
        frame.add(menu);
        cart.add(cartLabel);
        frame.add(cart);
        frame.add(buttons);
        frame.add(display);
        frame.setVisible(true);

    }
}
