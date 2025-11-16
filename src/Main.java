import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Simple product base class
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

        // menu display
        JPanel menu = new JPanel();
        menu.setBounds(0, 70, 400, 380);
        menu.setLayout(null);

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
        menuLabel.setForeground(Color.BLACK);
        menuLabel.setHorizontalAlignment(JLabel.LEFT);
        menuLabel.setBounds(25, 0, 400, 50);

        // create products
        Product[] products = {
                new FoodItem("Berry Danish", 120.00, "images/berry-danish.png"),
                new FoodItem("Caramel Frappuccino", 180.00, "images/caramel-frappuccino.png"),
                new FoodItem("Latte", 150.00,  "images/latte.png"),
                new FoodItem("Matcha Swiss Roll", 130.00, "images/matcha-swiss-roll.png"),
                new FoodItem("Mocha Coffee", 160.00, "images/mocha-coffee.png"),
                new FoodItem("Strawberry Cheesecake", 180.00,  "images/strawberry-cheesecake.png"),
        };

        // product buttons
        JPanel productPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        productPanel.setBounds(25, 50, 350, 300);

        // cart display
        JPanel cart = new JPanel();
        cart.setBounds(400, 70, 400, 300);
        cart.setLayout(null);

        JLabel cartLabel = new JLabel("CURRENT ORDER");
        cartLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
        cartLabel.setForeground(Color.BLACK);
        cartLabel.setHorizontalAlignment(JLabel.LEFT);
        cartLabel.setBounds(10, 0, 400, 50);


        // cart table
        String[] columnNames = {"Item", "Quantity", "Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            // makes table not editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable cartTable = new JTable(model);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBounds(10, 50, 380, 200);
        cart.add(cartScroll);

        // total display
        JPanel display = new JPanel();
        BoxLayout boxLayout = new BoxLayout(display, BoxLayout.Y_AXIS);
        display.setLayout(boxLayout);

        totalDisplay totals = new totalDisplay();
        totals.setCartTable(cartTable);

        JLabel subtotalLabel = new JLabel("SUBTOTAL: 0.00");
        subtotalLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
        JLabel taxLabel = new JLabel("TAX: 0.00");
        taxLabel.setFont(new Font("Century Gothic", Font.BOLD, 28));
        JLabel totalLabel = new JLabel("TOTAL: 0.00");
        totalLabel.setFont(new Font("Century Gothic", Font.BOLD, 30));

        display.add(subtotalLabel);
        display.add(taxLabel);
        display.add(totalLabel);
        display.setBounds(410, 450, 400, 180);

        // wire product buttons to cart
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



            // add to cart when clicked
            prodButton.addActionListener(e -> {
                boolean found = false;

                for (int i = 0; i < model.getRowCount(); i++) {
                    String itemName = model.getValueAt(i, 0).toString();
                    if (itemName.equals(product.getName())) {
                        // increases quantity if item is already in the table
                        int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
                        model.setValueAt(quantity + 1, i, 1);
                        found = true;
                        break;
                    }
                }

                // If item is not on the table, creates new row
                if (!found) {
                    model.addRow(new Object[]{product.getName(), 1, product.getPrice()});
                }
                subtotalLabel.setText("SUBTOTAL: " + String.format("%.2f", totals.calculateSubtotal()));
                taxLabel.setText("TAX: " + String.format("%.2f", totals.calculateTax()));
                totalLabel.setText("TOTAL: " + String.format("%.2f", totals.calculateTotal()));
            });

            productPanel.add(prodButton);
        }

        // button to decrease quantity
        JButton decreaseButton = new JButton("Decrease Quantity");
        decreaseButton.setBounds(10, 260, 180, 30);
        cart.add(decreaseButton);

        decreaseButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) { // ensure a row is selected
                int quantity = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
                if (quantity > 1) {
                    model.setValueAt(quantity - 1, selectedRow, 1);
                } else {
                    model.removeRow(selectedRow); // remove row if quantity = 1
                }

                // update totals
                subtotalLabel.setText("SUBTOTAL: " + String.format("%.2f", totals.calculateSubtotal()));
                taxLabel.setText("TAX: " + String.format("%.2f", totals.calculateTax()));
                totalLabel.setText("TOTAL: " + String.format("%.2f", totals.calculateTotal()));
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to decrease.");
            }
        });


        frame.add(title);
        menu.add(menuLabel);
        menu.add(productPanel);
        frame.add(menu);
        cart.add(cartLabel);
        frame.add(cart);
        frame.add(display);
        frame.setVisible(true);
    }
}
