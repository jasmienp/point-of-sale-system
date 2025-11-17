import javax.swing.*;

public class totalDisplay {
    private JTable cartTable;
    final double tax = 8.00;

    public void setCartTable(JTable table) {
        this.cartTable = table;
    }

    double calculateSubtotal() {
        double subtotal = 0.00;
        for (int i = 0; i < cartTable.getRowCount(); i++) { // start at 0
            int quantity = Integer.parseInt(cartTable.getValueAt(i, 1).toString());
            double price = Double.parseDouble(cartTable.getValueAt(i, 2).toString());
            subtotal += quantity * price;
        }
        return subtotal;
    }

    double calculateTax() {
        return ((tax / 100) * calculateSubtotal());
    }

    double calculateTotal() {
        return calculateSubtotal() + calculateTax();
    }
}