import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {
        // font
        Font headerFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/SFPRODISPLAYBOLD.OTF"));
        Font subtitleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/SFPRODISPLAYREGULAR.OTF"));

        // frame
        JFrame frame = new JFrame();
        frame.setTitle("Coffee Culture Point of Sale System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#FFFFFF"));
        frame.setResizable(false);
        frame.setSize(767, 491);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // POS cafè name
        JLabel title = new JLabel("Coffee Culture");
        title.setFont(headerFont.deriveFont(42f));
        title.setForeground(Color.decode("#000000"));
        title.setBackground(Color.decode("#FFFFFF"));
        title.setOpaque(true);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(0, 0, 767, 60);
        title.setBorder(new MatteBorder(0,0,2,0, Color.decode("#474747")));

        // product panel
        JPanel productPanel = new JPanel(new GridLayout(2, 3, 8, 8));
        productPanel.setBounds(0, 60, 504, 395);
        productPanel.setBackground(Color.decode("#FFFFFF"));
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
        cart.setBounds(504, 60, 251, 264);
        cart.setBackground(Color.decode("#FFFFFF"));
        cart.setBorder(new MatteBorder(0,1,1,0, Color.decode("#474747")));
        cart.setLayout(null);

        JLabel cartLabel = new JLabel("Current Order");
        cartLabel.setFont(headerFont.deriveFont(24f));
        cartLabel.setForeground(Color.decode("#000000"));
        cartLabel.setHorizontalAlignment(JLabel.LEFT);
        cartLabel.setBounds(10, 3, 251, 30);

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
        cartTable.setBounds(10, 54, 231, 96);
        cartTable.setBackground(Color.decode("#FFFFFF"));
        cartTable.setForeground(Color.decode("#000000"));
        cartTable.setFont(subtitleFont.deriveFont(12f));
        cartTable.setShowGrid(true);
        cartTable.setGridColor(Color.decode("#474747"));
        cartTable.setBorder(BorderFactory.createLineBorder(Color.decode("#474747")));

        TableColumnModel columnModel = cartTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(135);
        columnModel.getColumn(1).setPreferredWidth(41);
        columnModel.getColumn(2).setPreferredWidth(55);

        JTableHeader header = cartTable.getTableHeader();
        header.setBounds(10, 35, 231, 20);
        header.setBackground(Color.decode("#FFFFFF"));
        header.setForeground(Color.decode("#000000"));
        header.setFont(headerFont.deriveFont(14f));
        header.setBorder(BorderFactory.createLineBorder(Color.decode("#474747")));

        // total display
        JPanel display = new JPanel(new BorderLayout());
        display.setBackground(Color.decode("#FFFFFF"));
        display.setBounds(504, 322 , 251, 132);
        display.setBorder(new MatteBorder(1,1,0,0, Color.decode("#474747")));
        display.setVisible(true);

        JPanel topDisplay = new JPanel();
        topDisplay.setLayout(new BoxLayout(topDisplay, BoxLayout.Y_AXIS));
        topDisplay.setBackground(Color.decode("#FFFFFF"));
        topDisplay.setBorder(new MatteBorder(1,0,0,0, Color.decode("#474747")));

        totalDisplay totals = new totalDisplay();
        totals.setCartTable(cartTable);

        JLabel subtotalLabel = new JLabel("");
        subtotalLabel.setFont(subtitleFont.deriveFont(20f));
        subtotalLabel.setForeground(Color.decode("#000000"));
        subtotalLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));

        JLabel taxLabel = new JLabel("");
        taxLabel.setFont(subtitleFont.deriveFont(20f));
        taxLabel.setForeground(Color.decode("#000000"));
        taxLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel totalLabel = new JLabel("");
        totalLabel.setFont(headerFont.deriveFont(28f));
        totalLabel.setForeground(Color.decode("#000000"));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        // wire product buttons to cart
        for (Product product : products) {
            ImageIcon rawIcon = product.getImage();
            Image scaledImage = rawIcon.getImage().getScaledInstance(5000, 5000, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            ProductButton prodButton = new ProductButton(
                    product.getName(),
                    product.getPrice(),
                    scaledIcon.getImage()
            );

            prodButton.setBorderPainted(true);
            prodButton.setBorder(BorderFactory.createLineBorder(Color.decode("#000000"), 1));

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
        decreaseButton.setBounds(10, 166, 231, 25);
        decreaseButton.setFont(headerFont.deriveFont(14f));
        decreaseButton.setBackground(Color.decode("#FFFFFF"));
        decreaseButton.setForeground(Color.decode("#000000"));
        decreaseButton.setMargin(new Insets(0, 0, 0, 0));
        decreaseButton.setFocusPainted(false);
        decreaseButton.setBorderPainted(true);
        decreaseButton.setBorder(BorderFactory.createLineBorder(Color.decode("#000000"), 1));

        decreaseButton.addActionListener(_ -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) { // ensure a row is selected
                int quantity = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
                if (quantity > 1) {
                    model.setValueAt(quantity - 1, selectedRow, 1);
                } else {
                    model.removeRow(selectedRow); // remove row if quantity = 1
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to decrease.");
            }
        });

        // button to clear cart
        JButton clearButton = new JButton("Clear Cart");
        clearButton.setBounds(10, 198, 231, 25);
        clearButton.setFont(headerFont.deriveFont(14f));
        clearButton.setBackground(Color.decode("#FFFFFF"));
        clearButton.setForeground(Color.decode("#000000"));
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(true);
        clearButton.setBorder(BorderFactory.createLineBorder(Color.decode("#000000"), 1));

        clearButton.addActionListener(_ -> {
            model.setRowCount(0);
            subtotalLabel.setText("");
            taxLabel.setText("");
            totalLabel.setText("");
        });

        // button to check out cart
        JButton checkoutButton = new JButton("Check Out");
        checkoutButton.setBounds(10, 230, 231, 25);
        checkoutButton.setFont(headerFont.deriveFont(14f));
        checkoutButton.setBackground(Color.decode("#FFFFFF"));
        checkoutButton.setForeground(Color.decode("#00000"));
        checkoutButton.setMargin(new Insets(0, 0, 0, 0));
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorderPainted(true);
        checkoutButton.setBorder(BorderFactory.createLineBorder(Color.decode("#000000"), 1));

        checkoutButton.addActionListener(_ -> {
            subtotalLabel.setText("Subtotal: " + String.format("%.2f", totals.calculateSubtotal()));
            taxLabel.setText("Tax: " + String.format("%.2f", totals.calculateTax()));
            totalLabel.setText("Total: " + String.format("%.2f", totals.calculateTotal()));
        });

        MouseAdapter hoverEffect = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setBackground(Color.decode("#00000"));
                btn.setForeground(Color.decode("#FFFFFF"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setBackground(Color.decode("#FFFFFF"));
                btn.setForeground(Color.decode("#00000"));
            }
        };


        frame.add(title);
        frame.add(productPanel);
        cart.add(cartLabel);
        cart.add(header);
        cart.add(cartTable);
        decreaseButton.addMouseListener(hoverEffect);
        clearButton.addMouseListener(hoverEffect);
        checkoutButton.addMouseListener(hoverEffect);
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
