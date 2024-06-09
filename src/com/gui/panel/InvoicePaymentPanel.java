package com.gui.panel;

import com.model.SQLConnector;
import com.model.TakeawayBillData;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class InvoicePaymentPanel extends javax.swing.JPanel {

    Vector<TakeawayBillData> takeAwayVector;
    private static JTable jTable;
    String locationID = "5";

    private static JPanel loadPanel;
    private static JTextField loadTextField1;
    private static JTextField loadTextField2;
    private static JTextField loadTextField3;
    private static JTextField loadTextField4;
    private static JTextField loadTextField7;
    private static String loadBillType;
    private static String loadTableID;
    private static String loadReservationID;
    private static JLabel loadInvNoTextField;

    public InvoicePaymentPanel(String billAmount, Vector takeAwayData, JTable jTable, JPanel jPanel, JTextField jTextField1, JTextField jTextField2, JTextField jTextField3, JTextField jTextField4, JTextField jTextField7, String billTYpe, String tableID, String reservationID, JLabel inv_no_textField) {
        initComponents();

        loadPanel = jPanel;
        this.loadTextField1 = jTextField1;
        this.loadTextField2 = jTextField2;
        this.loadTextField3 = jTextField3;
        this.loadTextField4 = jTextField4;
        this.loadTextField7 = jTextField7;
        this.loadBillType = billTYpe;
        this.loadTableID = tableID;
        this.loadReservationID = reservationID;
        this.loadInvNoTextField = inv_no_textField;

        takeAwayVector = takeAwayData;
        this.jTable = jTable;
        setDefaultComponents(billAmount);
    }

    public void setDefaultComponents(String billAmount) {
        jTextField14.grabFocus();
        jTextField1.setText(billAmount);

        String query = "SELECT * FROM `ctrl`";
        int totalAmount;
        int billDiscount = 0;
        int billCommission = 0;
        try {
            ResultSet ctrlTable = SQLConnector.search(query);
            if (ctrlTable.next()) {

                if (loadBillType == "Table") {
                    billDiscount = ctrlTable.getInt("table_disc");
                    billCommission = ctrlTable.getInt("table_com");
                } else if (loadBillType == "Takeaway") {
                    billDiscount = ctrlTable.getInt("bill_disc");
                    billCommission = ctrlTable.getInt("bill_com");
                } else if (loadBillType == "Room") {
                    billDiscount = ctrlTable.getInt("room_disc");
                    billCommission = ctrlTable.getInt("room_com");
                }

                jTextField17.setText(String.valueOf(billDiscount));
                jTextField18.setText(String.valueOf(billCommission));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (billDiscount != 0) {
            billDiscount = (Integer.parseInt(billAmount) * billDiscount) / 100;
            totalAmount = Integer.parseInt(billAmount) - billDiscount;
        } else if (billCommission != 0) {
            billCommission = (Integer.parseInt(billAmount) * billCommission) / 100;
            totalAmount = Integer.parseInt(billAmount) + billCommission;
        } else {
            totalAmount = Integer.parseInt(billAmount);
        }

        jTextField19.setText(String.valueOf(totalAmount));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        jPanel3.setBackground(new java.awt.Color(222, 242, 241));

        jPanel1.setBackground(new java.awt.Color(222, 242, 241));

        jLabel1.setBackground(new java.awt.Color(23, 37, 42));
        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(23, 37, 42));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Bill Amount");

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setForeground(new java.awt.Color(23, 37, 42));
        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(222, 242, 241));

        jLabel14.setBackground(new java.awt.Color(23, 37, 42));
        jLabel14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(23, 37, 42));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Cash");

        jTextField14.setBackground(new java.awt.Color(255, 255, 255));
        jTextField14.setForeground(new java.awt.Color(23, 37, 42));
        jTextField14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField14KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(222, 242, 241));

        jLabel16.setBackground(new java.awt.Color(23, 37, 42));
        jLabel16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(23, 37, 42));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Credit Card");

        jTextField16.setBackground(new java.awt.Color(255, 255, 255));
        jTextField16.setForeground(new java.awt.Color(23, 37, 42));
        jTextField16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(222, 242, 241));

        jLabel17.setBackground(new java.awt.Color(23, 37, 42));
        jLabel17.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(23, 37, 42));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Discount (%)");

        jTextField17.setBackground(new java.awt.Color(255, 255, 255));
        jTextField17.setForeground(new java.awt.Color(23, 37, 42));
        jTextField17.setText("0");
        jTextField17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jTextField17.setEnabled(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(222, 242, 241));

        jLabel18.setBackground(new java.awt.Color(23, 37, 42));
        jLabel18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(23, 37, 42));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Service Charges (%)");

        jTextField18.setBackground(new java.awt.Color(255, 255, 255));
        jTextField18.setForeground(new java.awt.Color(23, 37, 42));
        jTextField18.setText("0");
        jTextField18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jTextField18.setEnabled(false);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(222, 242, 241));

        jLabel19.setBackground(new java.awt.Color(23, 37, 42));
        jLabel19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(23, 37, 42));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Total Amount");

        jTextField19.setBackground(new java.awt.Color(44, 85, 190));
        jTextField19.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTextField19.setForeground(new java.awt.Color(255, 255, 255));
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(222, 242, 241));

        jLabel20.setBackground(new java.awt.Color(23, 37, 42));
        jLabel20.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(23, 37, 42));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Balance");

        jTextField20.setBackground(new java.awt.Color(23, 37, 42));
        jTextField20.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTextField20.setForeground(new java.awt.Color(255, 153, 0));
        jTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField20.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(23, 37, 42));
        jButton1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jButton1.setText("Back");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(23, 37, 42));
        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jButton2.setText("Complete With Print");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(23, 37, 42));
        jButton3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jButton3.setText("Complete With-Out Print");
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(222, 242, 241));

        jComboBox2.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox2.setForeground(new java.awt.Color(23, 37, 42));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(23, 37, 42));
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(23, 37, 42));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Payment Method");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, 0, 249, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13))
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(474, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 167, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openCategoryGrid();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void openCategoryGrid() {
        loadPanel.removeAll();
        loadPanel.add(new InvoiceCategoryGrid(loadPanel, this.loadTextField4, this.loadTextField4, this.loadTextField4, this.loadTextField4), BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(loadPanel);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addInvoiceBill();
        setDefaultInvoicePage();
        if (loadBillType == "Table") {
            clearTableStatus();
        } else if (loadBillType == "Room") {
            clearRoomStatus();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void clearRoomStatus() {
        String query = "UPDATE `room_reservation` SET `reservation_status_id`=(SELECT `id` FROM `reservation_status` WHERE `status`='Closed' LIMIT 1) WHERE room_id='" + loadTableID + "' AND id='" + loadReservationID + "'";
        try {
            SQLConnector.iud(query);
            query = "UPDATE `room` SET `table_room_status_id`=(SELECT `id` FROM `table_room_status` WHERE `status`='Available') WHERE id='" + loadTableID + "'";
            SQLConnector.iud(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearTableStatus() {
        String query = "UPDATE `table_reservation` SET `reservation_status_id`=(SELECT `id` FROM `reservation_status` WHERE `status`='Closed' LIMIT 1) WHERE table_id='" + loadTableID + "' AND id='" + loadReservationID + "'";
        try {
            SQLConnector.iud(query);
            query = "UPDATE `table` SET `table_room_status_id`=(SELECT `id` FROM `table_room_status` WHERE `status`='Available') WHERE id='" + loadTableID + "'";
            SQLConnector.iud(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addInvoiceBill();
        setDefaultInvoicePage();
        clearTableStatus();
    }//GEN-LAST:event_jButton3ActionPerformed

    public void setDefaultInvoicePage() {
        DefaultTableModel invoiceTable = (DefaultTableModel) jTable.getModel();
        invoiceTable.setRowCount(0);
        loadTextField7.setText("0");
        jTextField1.setText("");
        jTextField14.setText("");
//        jTextField15.setText("");
        jTextField16.setText("");
        jTextField17.setText("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");

        openCategoryGrid();
    }

    public void addInvoiceBill() {
        String billAmount = jTextField19.getText();
        String cashAmount = jTextField14.getText();
        String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
        try {
            String query = "SELECT * FROM `ctrl`";
            ResultSet ctrlDataTable = SQLConnector.search(query);
            ctrlDataTable.next();
            String inv_no = ctrlDataTable.getString("inv_no");

            int next_inv_no = Integer.parseInt(inv_no) + 1;

            if (loadReservationID.isEmpty() && loadReservationID == "") {
                query = "INSERT INTO invoice (inv_no,date_time,amount,paid_amount,payment_method_id,invoice_type_id,discount,service_charge)"
                        + "VALUES ('" + inv_no + "','" + formattedDateTime + "','" + billAmount + "','" + cashAmount + "',(SELECT `id` FROM `payment_method` WHERE `method`='Cash' LIMIT 1),(SELECT `id` FROM `invoice_type` WHERE `type`='" + loadBillType + "' LIMIT 1),'" + Integer.parseInt(jTextField17.getText()) + "','" + Integer.parseInt(jTextField18.getText()) + "')";
            } else {
                query = "INSERT INTO invoice (inv_no,date_time,amount,paid_amount,payment_method_id,invoice_type_id,discount,service_charge,reservation_id)"
                        + "VALUES ('" + inv_no + "','" + formattedDateTime + "','" + billAmount + "','" + cashAmount + "',(SELECT `id` FROM `payment_method` WHERE `method`='Cash' LIMIT 1),(SELECT `id` FROM `invoice_type` WHERE `type`='" + loadBillType + "' LIMIT 1),'" + Integer.parseInt(jTextField17.getText()) + "','" + Integer.parseInt(jTextField18.getText()) + "','" + loadReservationID + "')";
            }

            SQLConnector.iud(query);
            for (int invoiceIteration = 0; invoiceIteration < jTable.getRowCount(); invoiceIteration++) {

                query = "SELECT * FROM `product` WHERE `id`='" + jTable.getValueAt(invoiceIteration, 0) + "'";
                ResultSet productTable = SQLConnector.search(query);
                productTable.next();

                query = "INSERT INTO invoice_item (product_id,qty,invoice_inv_no,unit_price)"
                        + "VALUES ('" + jTable.getValueAt(invoiceIteration, 0) + "','" + jTable.getValueAt(invoiceIteration, 2) + "','" + inv_no + "','" + productTable.getString("sale_price") + "')";
                SQLConnector.iud(query);

                query = "SELECT * FROM `location_stock` WHERE `location_stock`.`product_id`='" + jTable.getValueAt(invoiceIteration, 0) + "'";
                ResultSet locationStockTable = SQLConnector.search(query);
                if (locationStockTable.next()) {
                    double stockQty = locationStockTable.getDouble("qty");
                    double measure = productTable.getDouble("measure");
                    double updateQty = stockQty - (Integer.parseInt(String.valueOf(jTable.getValueAt(invoiceIteration, 2))) * measure);
                    query = "UPDATE location_stock SET qty='" + updateQty + "' WHERE product_id='" + jTable.getValueAt(invoiceIteration, 0) + "' AND location_id='" + locationID + "'";
                    SQLConnector.iud(query);
                } else {
                    double measure = productTable.getDouble("measure");
                    double updateQty = -(Integer.parseInt(String.valueOf(jTable.getValueAt(invoiceIteration, 2))) * measure);
                    query = "INSERT INTO location_stock (product_id,location_id,qty) "
                            + "VALUES ('" + jTable.getValueAt(invoiceIteration, 0) + "','" + locationID + "','" + updateQty + "')";
                    SQLConnector.iud(query);
                }
            }
            query = "UPDATE ctrl SET inv_no='" + String.format("%08d", next_inv_no) + "'";
            SQLConnector.iud(query);

            if (loadInvNoTextField != null) {
                loadInvNoTextField.setText("#" + String.format("%08d", next_inv_no));
            }

            query = "SELECT * FROM `ctrl`";
            ResultSet ctrlTable = SQLConnector.search(query);
            if (ctrlTable.next()) {
                String name = ctrlTable.getString("system_name");
                String address = ctrlTable.getString("address");
                String telephone;
                if (!ctrlTable.getString("telephone_2").equals("") && !ctrlTable.getString("telephone_2").isEmpty()) {
                    telephone = "Tel: " + ctrlTable.getString("telephone_1") + " / " + ctrlTable.getString("telephone_2");
                } else {
                    telephone = "Tel: " + ctrlTable.getString("telephone_1");
                }

                formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd")).format(new Date());
                String date = formattedDateTime;
                formattedDateTime = (new SimpleDateFormat("HH:mm a")).format(new Date());
                String time = formattedDateTime;

                HashMap<String, Object> parameterSet = new HashMap<>();
                parameterSet.put("System_Name", name);
                parameterSet.put("Address", address);
                parameterSet.put("Telephone", telephone);
                parameterSet.put("Date", "Date: " + date);
                parameterSet.put("Time", "Time: " + time);
                parameterSet.put("Invoice_No", "#" + inv_no);

                parameterSet.put("Net_Total", jTextField1.getText() + ".00");
                parameterSet.put("Discount", jTextField17.getText() + "%");
                parameterSet.put("Commission", jTextField18.getText() + "%");
                parameterSet.put("Paid_Amount", jTextField14.getText() + ".00");
                parameterSet.put("Balance", jTextField20.getText() + ".00");
                parameterSet.put("Total_Value", "Rs. " + jTextField19.getText() + ".00");

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable.getModel());
                String path;
                if (loadBillType != "Takeaway") {
                    path = "src/com/reports/tableInvoice.jasper";
                    ResultSet tableData = SQLConnector.search("SELECT * FROM `table` INNER JOIN `floor` ON `floor`.`id`=`table`.`floor_id` WHERE `table`.`id`='" + loadTableID + "'");
                    tableData.next();
                    parameterSet.put("Floor", tableData.getString("floor") + " Floor");
                    parameterSet.put("Table", tableData.getString("table_name"));
                } else {
                    path = "src/com/reports/invoiceBill.jasper";
                }

                JasperPrint report = JasperFillManager.fillReport(path, parameterSet, dataSource);
                JasperViewer.viewReport(report, false);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jTextField14KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyReleased
        if (evt.getKeyCode() == 10) {
            int cashAmount = Integer.parseInt(jTextField14.getText());
            int totalAmount = Integer.parseInt(jTextField19.getText());
            if (cashAmount < totalAmount) {
                JOptionPane.showMessageDialog(this, "Cannot Procees. Use valid cash amount", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                jTextField20.setText(String.valueOf(cashAmount - totalAmount));
            }
        }
    }//GEN-LAST:event_jTextField14KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        System.out.println("Changed");
    }//GEN-LAST:event_jComboBox2ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    // End of variables declaration//GEN-END:variables
}
