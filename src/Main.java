import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
    public static void main(String[] args) throws IOException, FontFormatException {
        // font
        Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Bogista-DEMO.otf")).deriveFont(24f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);

        // frame
        JFrame frame = new JFrame();
        frame.setTitle("Coffee Culture Point of Sale System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#FEFCEF"));
        frame.setResizable(false);
        frame.setSize(600, 500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // POS cafè name
        JLabel title = new JLabel("Coffee Culture");
        title.setFont(customFont.deriveFont(42f));
        title.setForeground(Color.decode("#C13631"));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(0, 10, 600, 60);

        // product panel
        JPanel productPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        productPanel.setBounds(0, 70, 335, 393);
        productPanel.setBackground(Color.decode("#C13631"));
        productPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // product buttons inside product panel
        Product[] products = {
                new FoodItem("Berry Glaze Danish", 120.00, "images/berry-danish.png"),
                new FoodItem("Matcha Cloud Roll", 130.00, "images/matcha-swiss-roll.png"),
                new FoodItem("Artisan Latte", 150.00,  "images/latte.png"),
                new FoodItem("Velvet Mocha", 160.00, "images/mocha-coffee.png"),
                new FoodItem("Caramel Frappe", 180.00, "images/caramel-frappuccino.png"),
                new FoodItem("Strawberry Cheesecake", 180.00,  "images/strawberry-cheesecake.png"),
        };

        // cart display
        JPanel cart = new JPanel();
        cart.setBounds(335, 70, 251, 260);
        cart.setBackground(Color.decode("#C13631"));
        cart.setLayout(null);

        JLabel cartLabel = new JLabel("Current Order");
        cartLabel.setFont(customFont.deriveFont(24f));
        cartLabel.setForeground(Color.decode("#FEFCEF"));
        cartLabel.setHorizontalAlignment(JLabel.LEFT);
        cartLabel.setBounds(0, 5, 251, 30);

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
        cartTable.setBounds(0, 65, 241, 120);

        JTableHeader header = cartTable.getTableHeader();
        header.setBounds(0, 35, 241, 30);

        cart.add(header);
        cart.add(cartTable);

        // total display
        JPanel display = new JPanel();
        display.setBackground(Color.decode("#FEFCEF"));
        BoxLayout boxLayout = new BoxLayout(display, BoxLayout.Y_AXIS);
        display.setLayout(boxLayout);

        totalDisplay totals = new totalDisplay();
        totals.setCartTable(cartTable);

        JLabel subtotalLabel = new JLabel("Subtotal: 0.00");
        subtotalLabel.setFont(customFont.deriveFont(28f));
        subtotalLabel.setForeground(Color.decode("#4144AF"));

        JLabel taxLabel = new JLabel("Tax: 0.00");
        taxLabel.setFont(customFont.deriveFont(28f));
        taxLabel.setForeground(Color.decode("#4144AF"));

        JLabel totalLabel = new JLabel("Total: 0.00");
        totalLabel.setFont(customFont.deriveFont(28f));
        totalLabel.setForeground(Color.decode("#4144AF"));

        display.add(subtotalLabel);
        display.add(taxLabel);
        display.add(totalLabel);
        display.setBounds(410, 450, 400, 180);

        // wire product buttons to cart
        for (Product product : products) {
            ImageIcon rawIcon = product.getImage();
            Image scaledImage = rawIcon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            ProductButton prodButton = new ProductButton(
                    product.getName(),
                    product.getPrice(),
                    scaledIcon.getImage()
            );

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

            // rise hover effect on buttons
            prodButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    prodButton.setLocation(prodButton.getX(), prodButton.getY() - 3);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    prodButton.setLocation(prodButton.getX(), prodButton.getY() + 3);
                }
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

        //total display
        JPanel display = new JPanel();
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));

        totalDisplay totals = new totalDisplay();
        totals.setCartTable(cartTable);

        JLabel subtotalLabel = new JLabel("SUBTOTAL: " + "₱" + totals.calculateSubtotal());
        subtotalLabel.setFont(new Font("Century Gothic",Font.BOLD, 28));
        subtotalLabel.setForeground(Color.BLACK);
        subtotalLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel taxLabel = new JLabel("TAX (8%): " + "₱" + totals.calculateTax());
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
        frame.add(productPanel);
        cart.add(cartLabel);
        frame.add(cart);
        frame.add(display);

        JPanel buttons = new JPanel();
        buttons.setBounds(0, 450, 400, 80);
        buttons.setBackground(Color.decode("#FEFCEF"));
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // button to clear cart
        JButton clearButton = new JButton("Clear Cart");
        clearButton.setFont(new Font("Century Gothic", Font.BOLD, 14));
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> {
            model.setRowCount(0);
            display.setVisible(false);
        });

        // button to check out cart
        JButton checkoutButton = new JButton("Check Out");
        checkoutButton.setFont(new Font("Century Gothic", Font.BOLD, 14));
        checkoutButton.setBackground(Color.GREEN.darker());
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> {
            subtotalLabel.setText("SUBTOTAL: " + String.format("%.2f", totals.calculateSubtotal()));
            taxLabel.setText("TAX: " + String.format("%.2f", totals.calculateTax()));
            totalLabel.setText("TOTAL: " + String.format("%.2f", totals.calculateTotal()));

            display.setVisible(true);
        });

        buttons.add(clearButton);
        buttons.add(checkoutButton);
        frame.add(buttons);

        frame.setVisible(true);
    }
}
