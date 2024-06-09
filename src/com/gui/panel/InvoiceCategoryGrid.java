package com.gui.panel;

import com.model.SQLConnector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class InvoiceCategoryGrid extends javax.swing.JPanel {

    private static JPanel loadPanel;
    private static JTextField loadTextField1;
    private static JTextField loadTextField2;
    private static JTextField loadTextField3;
    private static JTextField loadTextField4;

    public InvoiceCategoryGrid(JPanel jPanel, JTextField jTextField1, JTextField jTextField2, JTextField jTextField3, JTextField jTextField4) {
        initComponents();

        this.loadPanel = jPanel;
        this.loadTextField1 = jTextField1;
        this.loadTextField2 = jTextField2;
        this.loadTextField3 = jTextField3;
        this.loadTextField4 = jTextField4;

        String query1 = "SELECT COUNT(*) AS `num_rows` FROM `category` INNER JOIN `product_department` ON `product_department`.`id`=`category`.`product_department_id` WHERE `product_department`.`department`='KOT'";
        String query2 = "SELECT * FROM `category` INNER JOIN `product_department` ON `product_department`.`id`=`category`.`product_department_id` WHERE `product_department`.`department`='KOT' ORDER BY `category` ASC";
        setGridLayout(query1, query2, jPanel2);

        query1 = "SELECT COUNT(*) AS `num_rows` FROM `category` INNER JOIN `product_department` ON `product_department`.`id`=`category`.`product_department_id` WHERE `product_department`.`department`='BOT'";
        query2 = "SELECT * FROM `category` INNER JOIN `product_department` ON `product_department`.`id`=`category`.`product_department_id` WHERE `product_department`.`department`='BOT' ORDER BY `category` ASC";
        setGridLayout(query1, query2, jPanel3);
    }

    private void setGridLayout(String query1, String query2, JPanel jPanel) {

        int numRows = 0;
        try {
            ResultSet categoryRows = SQLConnector.search(query1);
            categoryRows.next();
            numRows = categoryRows.getInt("num_rows");
        } catch (Exception e) {
            e.printStackTrace();
        }

        int numColumns;
        numColumns = (int) Math.ceil(Math.sqrt(numRows));

        int gridRows = (int) Math.ceil((double) numRows / numColumns);

        jPanel.setLayout(new GridLayout(gridRows, numColumns, 15, 15));

        try {
            ResultSet categoryTable = SQLConnector.search(query2);
            HashMap<String, String> categoryMap = new HashMap<>();
            while (categoryTable.next()) {
                String categoryName = categoryTable.getString("category");
                String categoryID = categoryTable.getString("category.id");
                categoryMap.put(categoryName, categoryID);
                JButton jButton = new JButton(categoryTable.getString("category"));
                jButton.setBackground(new Color(57, 175, 169));
                jButton.setForeground(new Color(222, 242, 241));

                String query = "SELECT COUNT(*) AS `products` FROM `product` WHERE `category_id`='" + categoryTable.getString("category.id") + "'";
                ResultSet products = SQLConnector.search(query);
                products.next();
                int productCount = products.getInt("products");

                jButton.addActionListener(e -> {
                    if (productCount == 0) {
                        JOptionPane.showMessageDialog(this, "There has no item from this category", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        loadPanel.removeAll();
                        loadPanel.add(new InvoiceProductGrid(categoryName, categoryID, loadPanel, loadTextField1, loadTextField2, loadTextField3, loadTextField4), BorderLayout.CENTER);
                        SwingUtilities.updateComponentTreeUI(loadPanel);
                    }
                });

                jPanel.add(jButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(222, 242, 241));
        jPanel1.setLayout(new java.awt.GridLayout(2, 1));

        jPanel7.setBackground(new java.awt.Color(222, 242, 241));

        jPanel8.setBackground(new java.awt.Color(222, 242, 241));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(43, 122, 120));
        jLabel2.setText("Food Menu");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(516, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(202, 239, 237));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel7);

        jPanel6.setBackground(new java.awt.Color(222, 242, 241));

        jPanel9.setBackground(new java.awt.Color(222, 242, 241));

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(43, 122, 120));
        jLabel3.setText("Beverage Menu");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(473, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(202, 239, 237));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables
}
