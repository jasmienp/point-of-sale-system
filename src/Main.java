import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {
        // font
        Font headerFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Bogista-DEMO.otf")).deriveFont(24f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(headerFont);

        Font subtitleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Akwe-Pro-Bold.otf")).deriveFont(14f);

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
        title.setFont(headerFont.deriveFont(42f));
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
        cart.setBounds(335, 70, 251, 264);
        cart.setBackground(Color.decode("#C13631"));
        cart.setLayout(null);

        JLabel cartLabel = new JLabel("Current Order");
        cartLabel.setFont(headerFont.deriveFont(24f));
        cartLabel.setForeground(Color.decode("#FEFCEF"));
        cartLabel.setHorizontalAlignment(JLabel.LEFT);
        cartLabel.setBounds(0, 5, 251, 30);

        // cart table
        String[] columnNames = {"ITEM", "QTY", "PRICE"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            // makes table not editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable cartTable = new JTable(model);
        cartTable.setBounds(0, 54, 241, 96);
        cartTable.setBackground(Color.decode("#C13631"));
        cartTable.setForeground(Color.decode("#FEFCEF"));
        cartTable.setFont(subtitleFont.deriveFont(12f));
        cartTable.setShowGrid(true);
        cartTable.setGridColor(Color.decode("#FEFCEF"));
        cartTable.setBorder(BorderFactory.createLineBorder(Color.decode("#FEFCEF")));

        TableColumnModel columnModel = cartTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(140);
        columnModel.getColumn(1).setPreferredWidth(41);
        columnModel.getColumn(2).setPreferredWidth(60);

        JTableHeader header = cartTable.getTableHeader();
        header.setBounds(0, 35, 241, 20);
        header.setBackground(Color.decode("#FEFCEF"));
        header.setForeground(Color.decode("#C13631"));
        header.setFont(subtitleFont.deriveFont(14f));

        // total display
        JPanel display = new JPanel(new BorderLayout());
        display.setBackground(Color.decode("#FEFCEF"));
        display.setBounds(338, 336, 251, 122);

        JPanel topDisplay = new JPanel();
        topDisplay.setLayout(new BoxLayout(topDisplay, BoxLayout.Y_AXIS));
        topDisplay.setBackground(Color.decode("#FEFCEF"));

        totalDisplay totals = new totalDisplay();
        totals.setCartTable(cartTable);

        JLabel subtotalLabel = new JLabel("");
        subtotalLabel.setFont(subtitleFont.deriveFont(20f));
        subtotalLabel.setForeground(Color.decode("#C13631"));

        JLabel taxLabel = new JLabel("");
        taxLabel.setFont(subtitleFont.deriveFont(20f));
        taxLabel.setForeground(Color.decode("#C13631"));

        JLabel totalLabel = new JLabel("");
        totalLabel.setFont(subtitleFont.deriveFont(28f));
        totalLabel.setForeground(Color.decode("#C13631"));

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
            prodButton.addActionListener(_ -> {
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
        JButton decreaseButton = new JButton("Decrease Qty");
        decreaseButton.setBounds(0, 166, 241, 25);
        decreaseButton.setFont(subtitleFont.deriveFont(14f));
        decreaseButton.setBackground(Color.decode("#FEFCEF"));
        decreaseButton.setForeground(Color.decode("#C13631"));
        decreaseButton.setMargin(new Insets(0, 0, 0, 0));
        decreaseButton.setFocusPainted(false);

        decreaseButton.addActionListener(_ -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) { // ensure a row is selected
                int quantity = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
                if (quantity > 1) {
                    model.setValueAt(quantity - 1, selectedRow, 1);
                } else {
                    model.removeRow(selectedRow); // remove row if quantity = 1
                }

                // update totals
                subtotalLabel.setText("Subtotal: " + String.format("%.2f", totals.calculateSubtotal()));
                taxLabel.setText("Tax: " + String.format("%.2f", totals.calculateTax()));
                totalLabel.setText("TOTAL: " + String.format("%.2f", totals.calculateTotal()));
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to decrease.");
            }
        });

        JButton clearButton = new JButton("Clear Cart");
        clearButton.setBounds(0, 198, 241, 25);
        clearButton.setFont(subtitleFont.deriveFont(14f));
        clearButton.setBackground(Color.decode("#FEFCEF"));
        clearButton.setForeground(Color.decode("#C13631"));
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setFocusPainted(false);

        clearButton.addActionListener(_ -> {
            model.setRowCount(0);
            display.setVisible(false);
        });

        // button to check out cart
        JButton checkoutButton = new JButton("Check Out");
        checkoutButton.setBounds(0, 230, 241, 25);
        checkoutButton.setFont(subtitleFont.deriveFont(14f));
        checkoutButton.setBackground(Color.decode("#FEFCEF"));
        checkoutButton.setForeground(Color.decode("#C13631"));
        checkoutButton.setMargin(new Insets(0, 0, 0, 0));
        checkoutButton.setFocusPainted(false);

        checkoutButton.addActionListener(_ -> {
            subtotalLabel.setText("Subtotal: " + String.format("%.2f", totals.calculateSubtotal()));
            taxLabel.setText("Tax: " + String.format("%.2f", totals.calculateTax()));
            totalLabel.setText("Total: " + String.format("%.2f", totals.calculateTotal()));

            display.setVisible(true);
        });

        frame.add(title);
        frame.add(productPanel);
        cart.add(cartLabel);
        cart.add(header);
        cart.add(cartTable);
        cart.add(decreaseButton);
        cart.add(clearButton);
        cart.add(checkoutButton);
        frame.add(cart);
        topDisplay.add(subtotalLabel);
        topDisplay.add(taxLabel);
        display.add(topDisplay, BorderLayout.CENTER);
        display.add(totalLabel, BorderLayout.SOUTH);
        frame.add(display);
        frame.setVisible(true);
    }
}
