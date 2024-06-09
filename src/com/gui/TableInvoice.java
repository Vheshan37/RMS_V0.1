package com.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.gui.panel.InvoiceCategoryGrid;
import com.gui.panel.InvoicePaymentPanel;
import com.model.SQLConnector;
import com.model.TakeawayBillData;
import com.model.getLogger;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class TableInvoice extends javax.swing.JFrame {

    private static HashMap<String, String> tableMap;

    public TableInvoice(HashMap<String, String> tableDetails) {
        initComponents();
        tableMap = tableDetails;
        loadPreviousItem();
        setDefaultComponent();
    }

    private int checkReservation() {

        int returnValue = 0;

        String query = "SELECT * FROM `table` INNER JOIN `table_room_status` ON `table_room_status`.`id`=`table`.`table_room_status_id` INNER JOIN `floor` ON `floor`.`id`=`table`.`floor_id` WHERE table.id='" + tableMap.get(tableMap.get("tableName")) + "' AND `floor`.`floor`='" + tableMap.get("floorName") + "' AND `table_room_status`.`status`='Pending'";
        try {
            ResultSet tableData = SQLConnector.search(query);
            if (tableData.next()) {
                query = "SELECT * FROM `table_reservation` INNER JOIN `reservation_status` ON `reservation_status`.`id`=`table_reservation`.`reservation_status_id` WHERE `reservation_status`.`status`='Pending' AND table_id='" + tableData.getString("table.id") + "'";
                ResultSet reservationTable = SQLConnector.search(query);
                if (reservationTable.next()) {
                    returnValue = reservationTable.getInt("table_reservation.id");
                } else {
                    returnValue = 0;
                }
            } else {
                returnValue = 0;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            returnValue = 0;
            getLogger.logger().warning(e.toString());
        }

        return returnValue;
    }

    static int reservationID;

    private void loadPreviousItem() {
        reservationID = checkReservation();
        if (reservationID != 0) {
            String query = "SELECT * FROM `table_reservation_item` INNER JOIN `product` ON `product`.`id`=`table_reservation_item`.`product_id` WHERE `table_reservation_item`.`table_reservation_id`='" + reservationID + "'";
            try {
                ResultSet reservationData = SQLConnector.search(query);
                while (reservationData.next()) {

                    DefaultTableModel ReservationModel = (DefaultTableModel) jTable1.getModel();

                    boolean isPreviousPeoduct = false;
                    for (int tableIteration = 0; tableIteration < jTable1.getRowCount(); tableIteration++) {
                        if (jTable1.getValueAt(tableIteration, 0).equals(reservationData.getString("product_id"))) {

                            String oldQty = String.valueOf(jTable1.getValueAt(tableIteration, 2));
                            String oldValue = String.valueOf(jTable1.getValueAt(tableIteration, 4));
                            int currentQty = reservationData.getInt("qty");
                            int salePrice = reservationData.getInt("sale_price");
                            int currentValue = currentQty * salePrice;
                            int newQty = Integer.parseInt(oldQty) + currentQty;
                            int newValue = Integer.parseInt(oldValue) + currentValue;
                            jTable1.setValueAt(String.valueOf(newQty), tableIteration, 2);
                            jTable1.setValueAt(String.valueOf(newValue), tableIteration, 4);
                            isPreviousPeoduct = true;
                        } else {
                            isPreviousPeoduct = false;
                        }
                    }

                    if (!isPreviousPeoduct) {
                        int qty = reservationData.getInt("qty");
                        int salePrice = reservationData.getInt("sale_price");
                        int value = qty * salePrice;

                        Vector<String> tableRow = new Vector<>();
                        tableRow.add(reservationData.getString("product_id"));
                        tableRow.add(reservationData.getString("name"));
                        tableRow.add(String.valueOf(qty));
                        tableRow.add(String.valueOf(salePrice));
                        tableRow.add(String.valueOf(value));

                        ReservationModel.addRow(tableRow);
                        jTable1.setModel(ReservationModel);
                    }
                }
                loadTotalBillValue();
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        }
        loadTotalBillValue();
    }

    public void setDefaultComponent() {
        jTextField2.grabFocus();
        jPanel3.removeAll();
        jPanel3.add(new InvoiceCategoryGrid(this.jPanel3, jTextField2, jTextField3, jTextField5, jTextField4), BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jPanel3);

        jButton7.setText(tableMap.get("floorName") + " Floor");
        jButton26.setText(tableMap.get("tableName"));

    }

    public void loadTotalBillValue() {
        int totalValue = 0;
        if (jTable1.getRowCount() == 0) {
            jTextField7.setText("0");
        } else {
            for (int row = 0; row < jTable1.getRowCount(); row++) {

                int value = Integer.parseInt(String.valueOf(jTable1.getValueAt(row, 4)));
                totalValue += value;
                jTextField7.setText(String.valueOf(totalValue));
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(450, 776));

        jPanel5.setBackground(new java.awt.Color(23, 37, 42));

        jLabel1.setForeground(new java.awt.Color(222, 242, 241));
        jLabel1.setText("Date:");

        jLabel2.setForeground(new java.awt.Color(222, 242, 241));
        jLabel2.setText("Time:");

        jButton7.setBackground(new java.awt.Color(255, 83, 83));
        jButton7.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(254, 255, 255));
        jButton7.setText("Floor name");
        jButton7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton29.setBackground(new java.awt.Color(255, 83, 83));
        jButton29.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jButton29.setForeground(new java.awt.Color(254, 255, 255));
        jButton29.setText("Back");
        jButton29.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jButton29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setBackground(new java.awt.Color(43, 122, 120));
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jTextField3.setBackground(new java.awt.Color(43, 122, 120));
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jTextField4.setBackground(new java.awt.Color(43, 122, 120));
        jTextField4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jTextField4.setFocusTraversalPolicyProvider(true);
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jTextField5.setBackground(new java.awt.Color(43, 122, 120));
        jTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jTextField6.setBackground(new java.awt.Color(43, 122, 120));
        jTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(23, 37, 42));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("ID");

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(23, 37, 42));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Name");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(23, 37, 42));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("QTY");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(23, 37, 42));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Unit Price");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(23, 37, 42));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Value");

        jTable1.setBackground(new java.awt.Color(23, 37, 42));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item Name", "QTY", "Unit Price", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowHorizontalLines(true);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        }

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jTextField7.setBackground(new java.awt.Color(23, 37, 42));
        jTextField7.setForeground(new java.awt.Color(255, 153, 0));
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.setText("0");
        jTextField7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 10));
        jTextField7.setEnabled(false);

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(23, 37, 42));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Total");

        jButton26.setBackground(new java.awt.Color(83, 141, 255));
        jButton26.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jButton26.setForeground(new java.awt.Color(254, 255, 255));
        jButton26.setText("Table Name");
        jButton26.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton27.setBackground(new java.awt.Color(43, 67, 75));
        jButton27.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jButton27.setForeground(new java.awt.Color(254, 255, 255));
        jButton27.setText("TB");
        jButton27.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setBackground(new java.awt.Color(43, 67, 75));
        jButton28.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jButton28.setForeground(new java.awt.Color(254, 255, 255));
        jButton28.setText("KOT");
        jButton28.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jButton5.setBackground(new java.awt.Color(23, 37, 42));
        jButton5.setForeground(new java.awt.Color(222, 242, 241));
        jButton5.setText("Cash Withdrawal");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(23, 37, 42));
        jButton8.setForeground(new java.awt.Color(222, 242, 241));
        jButton8.setText("Re-Print");
        jButton8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(23, 37, 42));
        jButton1.setForeground(new java.awt.Color(222, 242, 241));
        jButton1.setText("Return");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(23, 37, 42));
        jButton3.setForeground(new java.awt.Color(222, 242, 241));
        jButton3.setText("Room");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(23, 37, 42));
        jButton4.setForeground(new java.awt.Color(222, 242, 241));
        jButton4.setText("Takeaway");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jButton9.setBackground(new java.awt.Color(222, 242, 241));
        jButton9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(23, 37, 42));
        jButton9.setText("7");
        jButton9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton10.setBackground(new java.awt.Color(222, 242, 241));
        jButton10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(23, 37, 42));
        jButton10.setText("8");
        jButton10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton11.setBackground(new java.awt.Color(222, 242, 241));
        jButton11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(23, 37, 42));
        jButton11.setText("9");
        jButton11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton12.setBackground(new java.awt.Color(222, 242, 241));
        jButton12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(23, 37, 42));
        jButton12.setText("4");
        jButton12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton13.setBackground(new java.awt.Color(222, 242, 241));
        jButton13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(23, 37, 42));
        jButton13.setText("5");
        jButton13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton14.setBackground(new java.awt.Color(222, 242, 241));
        jButton14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(23, 37, 42));
        jButton14.setText("6");
        jButton14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton15.setBackground(new java.awt.Color(222, 242, 241));
        jButton15.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(23, 37, 42));
        jButton15.setText("3");
        jButton15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton16.setBackground(new java.awt.Color(222, 242, 241));
        jButton16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton16.setForeground(new java.awt.Color(23, 37, 42));
        jButton16.setText("2");
        jButton16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton17.setBackground(new java.awt.Color(222, 242, 241));
        jButton17.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton17.setForeground(new java.awt.Color(23, 37, 42));
        jButton17.setText("1");
        jButton17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton18.setBackground(new java.awt.Color(222, 242, 241));
        jButton18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton18.setForeground(new java.awt.Color(23, 37, 42));
        jButton18.setText("0");
        jButton18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton19.setBackground(new java.awt.Color(222, 242, 241));
        jButton19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton19.setForeground(new java.awt.Color(23, 37, 42));
        jButton19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton20.setBackground(new java.awt.Color(222, 242, 241));
        jButton20.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton20.setForeground(new java.awt.Color(23, 37, 42));
        jButton20.setText(".");
        jButton20.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton21.setBackground(new java.awt.Color(222, 242, 241));
        jButton21.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton21.setForeground(new java.awt.Color(23, 37, 42));
        jButton21.setText("Cancel");
        jButton21.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(222, 242, 241));
        jButton22.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton22.setForeground(new java.awt.Color(23, 37, 42));
        jButton22.setText("Enter");
        jButton22.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6))
                            .addComponent(jButton19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jButton23.setBackground(new java.awt.Color(58, 175, 169));
        jButton23.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton23.setForeground(new java.awt.Color(222, 242, 241));
        jButton23.setText("Delete");
        jButton23.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(58, 175, 169));
        jButton24.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton24.setForeground(new java.awt.Color(222, 242, 241));
        jButton24.setText("Clear");
        jButton24.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(58, 175, 169));
        jButton25.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton25.setForeground(new java.awt.Color(222, 242, 241));
        jButton25.setText("Payment");
        jButton25.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton6.setBackground(new java.awt.Color(43, 122, 120));
        jButton6.setText("Create");
        jButton6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jTextField3))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)))
            .addComponent(jScrollPane1)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 851, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        TakeawayInvoice takeAwayPanel = new TakeawayInvoice();
        takeAwayPanel.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        RoomFloorSelection roomSelection = new RoomFloorSelection();
        roomSelection.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        int confirmStatus = JOptionPane.showConfirmDialog(this, "Are you sure want to clear this table", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirmStatus == 0) {
            String query = "DELETE FROM table_reservation_item WHERE table_reservation_id='" + reservationID + "'";
            try {
                SQLConnector.iud(query);
                query = "DELETE FROM table_reservation WHERE id='" + reservationID + "'";
                SQLConnector.iud(query);
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
            clearTableStatus();
            viewTableSelection();
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void clearTableStatus() {
        String query = "UPDATE `table_reservation` SET `reservation_status_id`=(SELECT `id` FROM `reservation_status` WHERE `status`='Closed' LIMIT 1) WHERE table_id='" + tableMap.get(tableMap.get("tableName")) + "' AND id='" + reservationID + "'";
        try {
            SQLConnector.iud(query);
            query = "UPDATE `table` SET `table_room_status_id`=(SELECT `id` FROM `table_room_status` WHERE `status`='Available') WHERE id='" + tableMap.get(tableMap.get("tableName")) + "'";
            SQLConnector.iud(query);
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        if (jTextField7.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "There has no items to pay", "ALert", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Vector<TakeawayBillData> takeAwayVector = new Vector();
            for (int row = 0; row < jTable1.getRowCount(); row++) {
                String id = String.valueOf(jTable1.getValueAt(row, 0));
                String qty = String.valueOf(jTable1.getValueAt(row, 2));

                TakeawayBillData takeAwayData = new TakeawayBillData();
                takeAwayData.setId(id);
                takeAwayData.setQty(qty);

                takeAwayVector.add(takeAwayData);
            }
            jPanel3.removeAll();
            jPanel3.add(new InvoicePaymentPanel(jTextField7.getText(), takeAwayVector, jTable1, this.jPanel3, jTextField2, jTextField3, jTextField5, jTextField4, jTextField7, "Table", tableMap.get(tableMap.get("tableName")), String.valueOf(reservationID), null), BorderLayout.CENTER);
            SwingUtilities.updateComponentTreeUI(jPanel3);
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        if (evt.getKeyCode() == 10) {
            int qty = Integer.parseInt(jTextField4.getText());
            int unitPrice = Integer.parseInt(jTextField5.getText());
            int value = qty * unitPrice;
            jTextField6.setText(String.valueOf(value));
            jTextField6.grabFocus();
        }
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        if (evt.getKeyCode() == 10) {

            if (reservationID != 0) {
//                Insert to previous reservationID
                String query = "INSERT INTO table_reservation_item (`qty`,`product_id`,`table_reservation_id`,`reservation_item_status_id`) "
                        + "VALUES ('" + jTextField4.getText() + "','" + jTextField2.getText() + "','" + reservationID + "',(SELECT `id` FROM `reservation_item_status` WHERE `status`='Pending' LIMIT 1))";
                try {
                    SQLConnector.iud(query);
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
//                Insert to new reservationID
                String query = "UPDATE `table` SET table_room_status_id=(SELECT id FROM table_room_status WHERE status='Pending') WHERE id='" + tableMap.get(tableMap.get("tableName")) + "'";
                try {
                    SQLConnector.iud(query);
                    String in_time = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
                    query = "INSERT INTO table_reservation (table_id,in_time,reservation_status_id) "
                            + "VALUES ('" + tableMap.get(tableMap.get("tableName")) + "','" + in_time + "',(SELECT `id` FROM `reservation_status` WHERE `status`='Pending' LIMIT 1))";
                    long insertID = SQLConnector.iud(query);

                    if (reservationID == 0) {
                        reservationID = Integer.parseInt(String.valueOf(insertID));
                    }

                    query = "INSERT INTO table_reservation_item (`qty`,`product_id`,`table_reservation_id`,`reservation_item_status_id`) "
                            + "VALUES ('" + jTextField4.getText() + "','" + jTextField2.getText() + "','" + insertID + "',(SELECT `id` FROM `reservation_item_status` WHERE `status`='Pending' LIMIT 1))";
                    SQLConnector.iud(query);
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            }

            DefaultTableModel invoiceTable = (DefaultTableModel) jTable1.getModel();
            if (checkItemList()) {
                Vector billRow = new Vector();
                billRow.add(jTextField2.getText());
                billRow.add(jTextField3.getText());
                billRow.add(jTextField4.getText());
                billRow.add(jTextField5.getText());
                billRow.add(jTextField6.getText());
                invoiceTable.addRow(billRow);
                jTable1.setModel(invoiceTable);
                loadTotalBillValue();
            }

            clearInputDate();
        }
    }//GEN-LAST:event_jTextField6KeyReleased

    public boolean checkItemList() {
        boolean status = true;
        for (int row = 0; row < jTable1.getRowCount(); row++) {

            String id = String.valueOf(jTable1.getValueAt(row, 0));
            if (id.equals(jTextField2.getText())) {
                String oldQty = String.valueOf(jTable1.getValueAt(row, 2));
                String oldValue = String.valueOf(jTable1.getValueAt(row, 4));
                int currentQty = Integer.parseInt(jTextField4.getText());
                int currentValue = Integer.parseInt(jTextField6.getText());
                int newQty = Integer.parseInt(oldQty) + currentQty;
                int newValue = Integer.parseInt(oldValue) + currentValue;
                jTable1.setValueAt(String.valueOf(newQty), row, 2);
                jTable1.setValueAt(String.valueOf(newValue), row, 4);
                loadTotalBillValue();
                status = false;
                break;
            }
        }
        return status;
    }

    private void clearInputDate() {
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField2.grabFocus();
    }

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        if (evt.getKeyCode() == 10) {
            String id = jTextField2.getText();
            String query = "SELECT * FROM product WHERE id='" + id + "'";
            try {
                ResultSet productTable = SQLConnector.search(query);
                if (productTable.next()) {
                    jTextField3.setText(productTable.getString("name"));
                    jTextField5.setText(String.valueOf(productTable.getInt("sale_price")));
                    jTextField4.grabFocus();
                }
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        if (jTable1.getRowCount() != 0) {
            if (evt.getKeyCode() == 127) {
                int row = jTable1.getSelectedRow();

                String productID = String.valueOf(jTable1.getValueAt(row, 0));
                String query = "DELETE FROM table_reservation_item WHERE product_id='" + productID + "' AND `table_reservation_id`='" + reservationID + "'";
                try {
                    SQLConnector.iud(query);
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }

                DefaultTableModel billTable = (DefaultTableModel) jTable1.getModel();
                billTable.removeRow(row);
                jTable1.setModel(billTable);
                loadTotalBillValue();
            }
        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        viewTableSelection();
    }//GEN-LAST:event_jButton29ActionPerformed

    private void viewTableSelection() {
        TableFloorSelection tableSelection = new TableFloorSelection();
        tableSelection.setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) {
        FlatDarkLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new TableInvoice().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton10;
    public static javax.swing.JButton jButton11;
    public static javax.swing.JButton jButton12;
    public static javax.swing.JButton jButton13;
    public static javax.swing.JButton jButton14;
    public static javax.swing.JButton jButton15;
    public static javax.swing.JButton jButton16;
    public static javax.swing.JButton jButton17;
    public static javax.swing.JButton jButton18;
    public static javax.swing.JButton jButton19;
    public static javax.swing.JButton jButton20;
    public static javax.swing.JButton jButton21;
    public static javax.swing.JButton jButton22;
    public static javax.swing.JButton jButton23;
    public static javax.swing.JButton jButton24;
    public static javax.swing.JButton jButton25;
    public static javax.swing.JButton jButton26;
    public static javax.swing.JButton jButton27;
    public static javax.swing.JButton jButton28;
    public static javax.swing.JButton jButton29;
    public static javax.swing.JButton jButton3;
    public static javax.swing.JButton jButton4;
    public static javax.swing.JButton jButton5;
    public static javax.swing.JButton jButton6;
    public static javax.swing.JButton jButton7;
    public static javax.swing.JButton jButton8;
    public static javax.swing.JButton jButton9;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel6;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JLabel jLabel8;
    public static javax.swing.JLabel jLabel9;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel10;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    public static javax.swing.JPanel jPanel6;
    public static javax.swing.JPanel jPanel7;
    public static javax.swing.JPanel jPanel8;
    public static javax.swing.JPanel jPanel9;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField2;
    public static javax.swing.JTextField jTextField3;
    public static javax.swing.JTextField jTextField4;
    public static javax.swing.JTextField jTextField5;
    public static javax.swing.JTextField jTextField6;
    public static javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
