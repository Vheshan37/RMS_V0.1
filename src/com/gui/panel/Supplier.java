/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gui.panel;

import com.gui.ProductView;
import com.model.SQLConnector;

import java.awt.event.KeyEvent;
import java.math.MathContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Vihanga
 */
public class Supplier extends javax.swing.JPanel {

    /**
     * Creates new form supplierPanel
     */
    private HashMap<String, String> companyNameMap = new HashMap<>();
    private HashMap< String, String> supplierStatusMap = new HashMap<>();
    private HashMap< String, String> supplierMap = new HashMap<>();

    private String supplierCompanyID;
    private String supperID;

    public Supplier() {
        initComponents();

        loadCompanies(jComboBox1);
        loadCompanies(jComboBox5);
        loadCompanies(jComboBox8);
        loadSupplierStatus(jComboBox2);
        loadSupplierStatus(jComboBox6);
        loadSuppliers();
        resetManage();

        jTextArea1.setEditable(false);

    }

    private void calculateAmount() {

        if (jFormattedTextField4.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please. Enter Count of Quantity");

        } else if (Integer.parseInt(jFormattedTextField4.getText()) == 0) {

            JOptionPane.showMessageDialog(this, "Quantity Must Higher than 0");

        } else if (jFormattedTextField5.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please. Enter Return Price");

        } else {

            jFormattedTextField3.setText("");
            jFormattedTextField3.setText(String.valueOf(Double.parseDouble(jFormattedTextField4.getText()) * Double.parseDouble(jFormattedTextField5.getText())));
        }

    }

    private void removeOrderTableRow() {

        int showConfirmDialog = JOptionPane.showConfirmDialog(this, "You want to Delete this row?", "", JOptionPane.YES_NO_OPTION);

        if (showConfirmDialog == 0) {

            int selectedRow = jTable2.getSelectedRow();

            DefaultTableModel modal = (DefaultTableModel) jTable2.getModel();
            modal.removeRow(selectedRow);

        }

    }

    private void removeDamageReturnTableRow() {

        int showConfirmDialog = JOptionPane.showConfirmDialog(this, "You want to Delete this row?", "", JOptionPane.YES_NO_OPTION);

        if (showConfirmDialog == 0) {

            int selectedRow = jTable3.getSelectedRow();

            DefaultTableModel modal = (DefaultTableModel) jTable3.getModel();
            modal.removeRow(selectedRow);

        }

    }

    private String[] getCurrentAndPastDate() {

        LocalDate currentDate = LocalDate.now();

        LocalDate pastDate = currentDate.minusMonths(8);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedCurrentDate = currentDate.format(formatter);
        String formattedPastDate = pastDate.format(formatter);

        return new String[]{formattedCurrentDate, formattedPastDate};
    }

    private void searchSupplierGRNs() {

        try {

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            String quary = " SELECT * FROM `supplier` INNER JOIN `supply_company` ON `supplier`.`supply_company_id` = `supply_company`.`id` INNER JOIN `supplier_status` ON `supplier`.`supplier_status_id`=`supplier_status`.`id` ";

            if (jComboBox5.getSelectedIndex() != 0 && jComboBox6.getSelectedIndex() == 0) {

                quary += "WHERE `supply_company`.`id` = '" + companyNameMap.get(jComboBox5.getSelectedItem()) + "' ";

            } else if (jComboBox5.getSelectedIndex() == 0 && jComboBox6.getSelectedIndex() != 0) {

                quary += "WHERE `supplier_status`.`id` = '" + supplierStatusMap.get(jComboBox6.getSelectedItem()) + "' ";

            } else if (jComboBox5.getSelectedIndex() != 0 && jComboBox6.getSelectedIndex() != 0) {

                quary += "WHERE `supply_company`.`id` = '" + companyNameMap.get(jComboBox5.getSelectedItem()) + "' AND `supplier_status`.`id` = '" + supplierStatusMap.get(jComboBox6.getSelectedItem()) + "' ";

            }

            ResultSet resultSet = SQLConnector.search(quary);

            while (resultSet.next()) {

                String[] dates = getCurrentAndPastDate();

                ResultSet resultSet1 = SQLConnector.search(" SELECT COUNT(`id`) FROM `grn` WHERE `supplier_id`='" + resultSet.getString("supplier.id") + "' AND `date_time` BETWEEN '" + dates[1] + "' AND '" + dates[0] + "' ");

                if (resultSet1.next()) {

                    String grn_Count = resultSet1.getString("COUNT(`id`)");

                    String value1 = jFormattedTextField1.getText();
                    String value2 = jFormattedTextField2.getText();

                    if (Integer.parseInt(value1) > Integer.parseInt(value2)) {

                        JOptionPane.showMessageDialog(this, "First Value Must be LOWER Than the Second Value", "Warning", JOptionPane.WARNING_MESSAGE);

                    } else if (Integer.parseInt(value1) == Integer.parseInt(value2)) {

                        Vector vector = new Vector();

                        vector.add(resultSet.getString("supplier.id"));
                        vector.add(resultSet.getString("supplier.name"));
                        vector.add(resultSet.getString("supply_company.company"));
                        vector.add(grn_Count);
                        vector.add(resultSet.getString("supplier_status.name"));

                        model.addRow(vector);

                    } else if (Integer.parseInt(value1) < Integer.parseInt(value2)) {

                        if (Integer.parseInt(value1) < Integer.parseInt(grn_Count) & Integer.parseInt(grn_Count) < Integer.parseInt(value2)) {

                            Vector vector = new Vector();

                            vector.add(resultSet.getString("supplier.id"));
                            vector.add(resultSet.getString("supplier.name"));
                            vector.add(resultSet.getString("supply_company.company"));
                            vector.add(grn_Count);
                            vector.add("supplier_status.name");

                            model.addRow(vector);

                        }

                    }

                }

            }

            JOptionPane.showMessageDialog(this, "Table Updated");

        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void resetManage() {

        jTextField10.setText("");
        jTextField11.setText("");
        jTextArea2.setText("");
        jTextField7.setText("");
        jTextField6.setText("");
        jComboBox2.setSelectedIndex(0);

        jTextField11.setEnabled(false);
        jTextArea2.setEnabled(false);
        jTextField7.setEnabled(false);
        jTextField6.setEnabled(false);
        jComboBox2.setEnabled(false);

        jTextField10.requestFocus();

    }

    private void resetCreate() {

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextArea1.setText("");
        jTextField5.setText("");
        jComboBox1.setSelectedIndex(0);
        jTextField1.requestFocus();

    }

    private void resetOrder() {

        jComboBox8.setSelectedIndex(0);

        DefaultComboBoxModel model1 = (DefaultComboBoxModel) jComboBox9.getModel();
        model1.removeAllElements();
        model1.addElement("Select_Company_First");
        jComboBox9.setSelectedIndex(0);

        jDateChooser1.setDate(null);
        jTextField21.setText("");
        jTextField16.setText("");
        jTextField19.setText("");
        jTextField20.setText("0");

        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

    }

    private void resetDamageReturn() {

        jComboBox10.setSelectedIndex(0);

        jTextField17.setText("");
        jTextField23.setText("");
        jFormattedTextField4.setText("0");
        jFormattedTextField5.setText("0.00");
        jFormattedTextField3.setText("0.00");

        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0);

    }

    private void grnRangeValidation() {

        if (jFormattedTextField1.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please. Enter a Number for Value 01");

        } else if (jFormattedTextField2.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please. Enter a Number for Value 02");

        }

    }

    private void loadSuppliers() {

        try {

            ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supplier` ");

            DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox10.getModel();
            model.removeAllElements();

            Vector vector = new Vector();
            vector.add("Select");

            while (resultSet.next()) {

                vector.add(resultSet.getString("name"));
                supplierMap.put(resultSet.getString("name"), resultSet.getString("id"));

            }

            model.addAll(vector);
            jComboBox10.setSelectedIndex(0);

        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadSupplierStatus(JComboBox jComboBox) {

        try {

            ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supplier_status` ; ");

            DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox.getModel();
            model.removeAllElements();

            Vector vector = new Vector();
            vector.add("Select");

            while (resultSet.next()) {

                vector.add(resultSet.getString("name"));
                supplierStatusMap.put(resultSet.getString("name"), resultSet.getString("id"));

            }

            model.addAll(vector);
            jComboBox.setSelectedIndex(0);

        } catch (SQLException ex) {
//            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();

        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);

            ex.printStackTrace();
        }

    }

    private void loadCompanies(JComboBox jComboBox) {

        try {

            ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supply_company` ; ");

            DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox.getModel();
            model.removeAllElements();

            Vector vector = new Vector();
            vector.add("Select");

            while (resultSet.next()) {

                vector.add(resultSet.getString("company"));
                companyNameMap.put(resultSet.getString("company"), resultSet.getString("id"));

            }

            model.addAll(vector);
            jComboBox.setSelectedIndex(0);

        } catch (SQLException ex) {
//            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();

        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);

            ex.printStackTrace();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField10 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel28 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jComboBox8 = new javax.swing.JComboBox<>();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jTextField16 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();

        setPreferredSize(new java.awt.Dimension(710, 726));

        jPanel1.setBackground(new java.awt.Color(29, 44, 50));

        jTabbedPane1.setBackground(new java.awt.Color(29, 44, 50));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(29, 44, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Supplier Details");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Supplier Name");

        jTextField1.setBackground(new java.awt.Color(29, 54, 64));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 0));

        jTextField2.setBackground(new java.awt.Color(29, 54, 64));
        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("New Company");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Existing Company");

        jComboBox1.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField3.setBackground(new java.awt.Color(29, 54, 64));
        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Contact Number");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Email");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Address");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Contact Details");

        jTextField5.setBackground(new java.awt.Color(29, 54, 64));
        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(255, 255, 255));
        jTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

        jButton1.setBackground(new java.awt.Color(0, 60, 150));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Register");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 132, 188));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Refresh");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setBackground(new java.awt.Color(29, 54, 64));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jScrollPane4.setViewportView(jTextArea1);

        jButton5.setBackground(new java.awt.Color(0, 132, 188));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Clear");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(48, 48, 48))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(66, 66, 66)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jTabbedPane1.addTab("Create", jPanel2);

        jPanel3.setBackground(new java.awt.Color(29, 44, 50));

        jButton3.setBackground(new java.awt.Color(0, 132, 188));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Refresh");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 60, 150));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Change Updates");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField6.setBackground(new java.awt.Color(29, 54, 64));
        jTextField6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

        jTextField7.setBackground(new java.awt.Color(29, 54, 64));
        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Email");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Contact Number");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Address");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Contact Details");

        jComboBox2.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField10.setBackground(new java.awt.Color(29, 54, 64));
        jTextField10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Supplier Name");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Supplier Details");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Supplier ID");

        jTextField11.setBackground(new java.awt.Color(29, 54, 64));
        jTextField11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 204, 204));
        jLabel18.setText("F3 - View Supplier");

        jTextArea2.setBackground(new java.awt.Color(29, 54, 64));
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jScrollPane5.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(111, 111, 111))
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator5))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator4))
                            .addComponent(jLabel11)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(48, 48, 48))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(12, 12, 12)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jTabbedPane1.addTab("Manage", jPanel3);

        jPanel4.setBackground(new java.awt.Color(29, 44, 50));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("GRN Range");

        jComboBox5.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.setBorder(null);
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Company");

        jComboBox6.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox6.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Active", "Inactive" }));
        jComboBox6.setBorder(null);
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Status");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 204, 204));
        jLabel24.setText("Set the end value to ‘0’ to display");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 204, 204));
        jLabel25.setText("all items above the start value");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Supplier Name", "Company", "GRNs", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton6.setBackground(new java.awt.Color(0, 132, 188));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Search");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setBackground(new java.awt.Color(29, 54, 64));
        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jFormattedTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField1.setText("0");
        jFormattedTextField1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });

        jFormattedTextField2.setBackground(new java.awt.Color(29, 54, 64));
        jFormattedTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jFormattedTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField2.setText("0");
        jFormattedTextField2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel19)
                            .addComponent(jLabel24)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator7))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(52, 52, 52))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addGap(0, 0, 0)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(665, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("View", jPanel4);

        jPanel5.setBackground(new java.awt.Color(29, 44, 50));

        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Company");

        jButton7.setBackground(new java.awt.Color(0, 132, 188));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Refresh");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 60, 150));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Return");
        jButton8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jComboBox8.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox8.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox8.setBorder(null);
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });
        jComboBox8.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox8PropertyChange(evt);
            }
        });

        jComboBox9.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox9.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select_Company_First" }));
        jComboBox9.setBorder(null);
        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Supplier");

        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Required Date");

        jDateChooser1.setBackground(new java.awt.Color(29, 54, 64));
        jDateChooser1.setForeground(new java.awt.Color(255, 255, 255));
        jDateChooser1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jTextField16.setBackground(new java.awt.Color(29, 54, 64));
        jTextField16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField16.setForeground(new java.awt.Color(255, 255, 255));
        jTextField16.setBorder(null);
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField16KeyReleased(evt);
            }
        });

        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("ID");

        jTextField19.setBackground(new java.awt.Color(29, 54, 64));
        jTextField19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField19.setForeground(new java.awt.Color(255, 255, 255));
        jTextField19.setBorder(null);
        jTextField19.setEnabled(false);

        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Item Name");

        jTextField20.setBackground(new java.awt.Color(29, 54, 64));
        jTextField20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField20.setForeground(new java.awt.Color(255, 255, 255));
        jTextField20.setText("0");
        jTextField20.setBorder(null);

        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Quantity");

        jTextField21.setBackground(new java.awt.Color(29, 54, 64));
        jTextField21.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField21.setForeground(new java.awt.Color(255, 255, 255));
        jTextField21.setBorder(null);

        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Customer Address");

        jButton9.setBackground(new java.awt.Color(0, 132, 188));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Enter");
        jButton9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Item Name", "Required Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(8);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(204);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(8);
        }

        jLabel27.setForeground(new java.awt.Color(0, 204, 204));
        jLabel27.setText("F3 - View Products");

        jLabel31.setForeground(new java.awt.Color(0, 204, 204));
        jLabel31.setText("Del - Delete raw");
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel32)
                                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel38))))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel31))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel36)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(30, 30, 30))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))))
                .addGap(21, 21, 21)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel31)))
                .addGap(24, 24, 24))
        );

        jTabbedPane1.addTab("Order", jPanel5);

        jPanel6.setBackground(new java.awt.Color(29, 44, 50));

        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Supplier");

        jComboBox10.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jComboBox10.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox10.setBorder(null);
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Quantity");

        jTextField23.setEditable(false);
        jTextField23.setBackground(new java.awt.Color(29, 54, 64));
        jTextField23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField23.setBorder(null);

        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Item Name");

        jTextField17.setBackground(new java.awt.Color(29, 54, 64));
        jTextField17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField17.setBorder(null);
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField17KeyReleased(evt);
            }
        });

        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("ID");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item_Name", "Return_Qty", "Return_Price", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable3KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(8);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(204);
            jTable3.getColumnModel().getColumn(2).setResizable(false);
            jTable3.getColumnModel().getColumn(2).setPreferredWidth(8);
            jTable3.getColumnModel().getColumn(3).setResizable(false);
            jTable3.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton11.setBackground(new java.awt.Color(0, 60, 150));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Return");
        jButton11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 132, 188));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Refresh");
        jButton12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel34.setForeground(new java.awt.Color(0, 204, 204));
        jLabel34.setText("Del - Delete raw");
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(0, 204, 204));
        jLabel30.setText("F3 - View Products");

        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Return Price");

        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Amount");

        jButton10.setBackground(new java.awt.Color(0, 132, 188));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Enter");
        jButton10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jFormattedTextField3.setBackground(new java.awt.Color(29, 54, 64));
        jFormattedTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jFormattedTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.setText("0.00");
        jFormattedTextField3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jFormattedTextField4.setBackground(new java.awt.Color(29, 54, 64));
        jFormattedTextField4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jFormattedTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField4.setText("0");
        jFormattedTextField4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jFormattedTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyReleased(evt);
            }
        });

        jFormattedTextField5.setBackground(new java.awt.Color(29, 54, 64));
        jFormattedTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
        jFormattedTextField5.setForeground(new java.awt.Color(255, 255, 255));
        jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField5.setText("0.00");
        jFormattedTextField5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jFormattedTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField5KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator9)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel34))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel43))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel42))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel44)
                                    .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel45)))
                            .addComponent(jLabel29))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel43)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel44)
                                    .addComponent(jLabel45)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel33)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel34)))
                .addGap(24, 24, 24))
        );

        jTabbedPane1.addTab("Damage Return", jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:

        if (jTextField16.getText().isEmpty() && jTextField19.getText().isEmpty() && jTextField20.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "ID , Item name and Quantity must be Filled", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            int rows = jTable2.getRowCount();

            boolean ProductFound = false;

            if (rows != 0) {

                String productID = jTextField16.getText();

                for (int i = 0; i < rows; i++) {

                    String productID_2 = String.valueOf(jTable2.getValueAt(i, 0));

                    if (productID.equals(productID_2)) {

                        if (jTextField20.getText().isEmpty()) {

                            JOptionPane.showMessageDialog(this, "Please. Enter Count of Quantity");

                        } else if (Integer.parseInt(jTextField20.getText()) == 0) {

                            JOptionPane.showMessageDialog(this, "Quantity Must Higher than 0");

                        } else {

                            jTable2.setValueAt(jTextField20.getText(), i, 2);
                            ProductFound = true;
                            JOptionPane.showMessageDialog(this, "Product Updated Successfully");

                            jTextField16.setText("");
                            jTextField19.setText("");
                            jTextField20.setText("0");

                            break;

                        }
                    }

                }

            }

            if (!ProductFound) {

                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();

                Vector vector = new Vector();

                if (jTextField16.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Enter Product ID");

                } else if (jTextField19.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Press 'Enter' key after when you selected the product");

                } else if (jTextField20.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Enter Count of Quantity");

                } else if (Integer.parseInt(jTextField20.getText()) == 0) {

                    JOptionPane.showMessageDialog(this, "Quantity Must Higher than 0");

                } else {

                    vector.add(jTextField16.getText());
                    vector.add(jTextField19.getText());
                    vector.add(jTextField20.getText());

                    model.addRow(vector);

                    JOptionPane.showMessageDialog(this, "Product Added Successfully");

                    jTextField16.setText("");
                    jTextField19.setText("");
                    jTextField20.setText("0");

                }

            }

        }

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        resetCreate();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String supplierName = jTextField1.getText();
        String newCompany = jTextField2.getText();
        String existCompany = companyNameMap.get(jComboBox1.getSelectedItem());
        String address = jTextArea1.getText();
        String number = jTextField3.getText();
        String email = jTextField5.getText();

        if (supplierName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please. Enter Supplier Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please. Enter Supplier Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!number.matches("^07[1,2,4,5,6,7,8][0-9]{7}$")) {
            JOptionPane.showMessageDialog(this, "Please. Check the Mobile number Again.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please. Enter Supplier Email", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Please. Check the Email address Again.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                if (newCompany.isEmpty() && jComboBox1.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, "Please. Select or Enter Company Name.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (newCompany.isEmpty() && jComboBox1.getSelectedIndex() != 0) {

                    SQLConnector.iud(" INSERT INTO `supplier` (`supply_company_id`,`name`,`mobile`,`email`,`supplier_status_id`) VALUES ('" + existCompany + "','" + supplierName + "','" + number + "','" + email + "','1'); ");

                    JOptionPane.showMessageDialog(this, "Supplier Added Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                    resetCreate();

                } else if (!newCompany.isEmpty() && jComboBox1.getSelectedIndex() == 0) {

                    if (address.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please. Enter Company Address", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (!address.isEmpty()) {
                        SQLConnector.iud(" INSERT INTO `supply_company` (`company`,`address`) VALUES ('" + newCompany + "','" + address + "') ");

                        ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supply_company` WHERE `company`='" + newCompany + "' AND `address` = '" + address + "' ");
                        if (resultSet.next()) {

                            SQLConnector.iud(" INSERT INTO `supplier` (`supply_company_id`,`name`,`mobile`,`email`,`supplier_status_id`) VALUES ('" + resultSet.getString("id") + "','" + supplierName + "' , '" + number + "' , '" + email + "','1' )  ");

                            JOptionPane.showMessageDialog(this, "Supplier Added Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                            resetCreate();
                        }

                    }

                } else if (!newCompany.isEmpty() && jComboBox1.getSelectedIndex() != 0) {
                    JOptionPane.showMessageDialog(this, "Please. Select or Enter Company Name.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String supplierID = jTextField10.getText();

            try {

                ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supplier` INNER JOIN `supply_company` ON `supplier`.`supply_company_id` = `supply_company`.`id` INNER JOIN `supplier_status` ON `supplier`.`supplier_status_id` = `supplier_status`.`id` WHERE `supplier`.`id` = '" + supplierID + "'; ");
                if (resultSet.next()) {

                    jTextField11.setEnabled(true);
                    jTextArea2.setEnabled(true);
                    jTextField17.setEnabled(true);
                    jTextField16.setEnabled(true);
                    jComboBox2.setEnabled(true);

                    jTextField11.setText(resultSet.getString("supplier.name"));
                    jTextArea2.setText(resultSet.getString("supply_company.address"));
                    jTextField6.setText(resultSet.getString("supplier.email"));
                    jTextField7.setText(resultSet.getString("supplier.mobile"));
                    jComboBox2.setSelectedIndex((int) Double.parseDouble(resultSet.getString("supplier_status.id")));

                    supplierCompanyID = resultSet.getString("supply_company.id");

                } else {

                    JOptionPane.showMessageDialog(this, "Invalid Supplier", "Error", JOptionPane.ERROR_MESSAGE);

                }

            } catch (SQLException ex) {
//                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }

    }//GEN-LAST:event_jTextField10KeyReleased

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        try {

            String supplierID = jTextField10.getText();
            String supplierName = jTextField11.getText();
            String address = jTextArea2.getText();
            String email = jTextField6.getText();
            String number = jTextField7.getText();

            if (supplierID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please. Enter Supplier ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (supplierName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please. Enter Supplier Name.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please. Enter Supplier Address.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please. Enter Supplier Email.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Please. Check Spplier Email address Again.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (number.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please. Enter Supplier Contact Number.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!number.matches("^07[1,2,4,5,6,7,8][0-9]{7}$")) {
                JOptionPane.showMessageDialog(this, "Please. Enter Supplier ID.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (jComboBox2.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please. Select Supplier Status", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                SQLConnector.iud(" UPDATE `supply_company` SET `address`='" + address + "' WHERE `id` = '" + supplierCompanyID + "' ");
                SQLConnector.iud(" UPDATE `supplier` SET `name`='" + supplierName + "' , `mobile`='" + number + "' , `email`='" + email + "' , `supplier_status_id` = '" + jComboBox2.getSelectedIndex() + "' ");

                JOptionPane.showMessageDialog(this, "Supplier Updated Successfilly", "Success", JOptionPane.INFORMATION_MESSAGE);

                resetManage();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        resetManage();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        jTextField2.setText("");
        jTextArea1.setText("");

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:

        if (jTextField2.getText().isEmpty()) {
            jTextArea1.setEditable(false);
            jTextArea1.setText("");
        } else {
            jTextArea1.setEditable(true);
        }

    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == 114) {

            String quary = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id` = `product`.`category_id` ";

            ProductView productView = new ProductView(quary, jTextField16);
            productView.setVisible(true);
            productView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        } else if (evt.getKeyCode() == 10) {

            try {

                String productId = jTextField16.getText();

                ResultSet resultSet = SQLConnector.search(" SELECT * FROM `product` WHERE `id`='" + productId + "' ");
                if (resultSet.next()) {

                    jTextField19.setText(resultSet.getString("name"));

                }

            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jTextField16KeyReleased


    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:

        searchSupplierGRNs();

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        resetOrder();

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            int selectedRow = jTable2.getSelectedRow();

            jTextField16.setText((String) jTable2.getValueAt(selectedRow, 0));
            jTextField19.setText((String) jTable2.getValueAt(selectedRow, 1));
            jTextField20.setText((String) jTable2.getValueAt(selectedRow, 2));

        }

    }//GEN-LAST:event_jTable2MouseClicked

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 1) {

            removeOrderTableRow();

        }

    }//GEN-LAST:event_jLabel31MouseClicked

    private void jTable2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTable2KeyPressed

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {

            removeOrderTableRow();

        }

    }//GEN-LAST:event_jTable2KeyReleased

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:

//        String existCompany = companyNameMap.get(jComboBox1.getSelectedItem());
        String companyID = companyNameMap.get(jComboBox8.getSelectedItem());
        String supplierID = supplierMap.get(jComboBox9.getSelectedItem());

        String address = jTextField21.getText();

        int rowCount = jTable2.getRowCount();

        if (jComboBox8.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please. Select Company", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (jDateChooser1.getDate() == null) {

            JOptionPane.showMessageDialog(this, "Please. Set Required Date", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (jComboBox9.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please. Select Supplier", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (jComboBox9.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please. Select Supplier", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (rowCount == 0) {

            JOptionPane.showMessageDialog(this, "Please. Add Product to Table", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(jDateChooser1.getDate());

            try {

                if (address.isEmpty()) {

                    SQLConnector.iud(" INSERT INTO `supplier_order` (`require_date`,`supplier_id`) VALUES ('" + formatedDate + "','" + supplierID + "');");

                } else {

                    SQLConnector.iud(" INSERT INTO `supplier_order` (`require_date`,`supplier_id`,`custom_address`) VALUES ('" + formatedDate + "','" + supplierID + "','" + address + "');");

                }

                ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supplier_order` WHERE `require_date` = '" + formatedDate + "' AND `supplier_id`='" + supplierID + "' ");

                if (resultSet.next()) {

                    for (int i = 0; i < rowCount; i++) {

                        SQLConnector.iud(" INSERT INTO `supplier_order_item` (`supplier_order_id`,`product_id`,`qty`) VALUES ('" + resultSet.getString("id") + "','" + jTable2.getValueAt(i, 0) + "','" + jTable2.getValueAt(i, 2) + "') ");

                    }

                }

                System.out.println("WTF7");

                JOptionPane.showMessageDialog(this, "Data Added Successfully");
                resetOrder();

            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox8PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox8PropertyChange
        // TODO add your handling code here:

        //not this

    }//GEN-LAST:event_jComboBox8PropertyChange

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        // TODO add your handling code here:

        try {

            Object selectedItem = companyNameMap.get(jComboBox8.getSelectedItem());

            ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supplier` WHERE `supply_company_id` = '" + selectedItem + "' ");

            DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox9.getModel();
            model.removeAllElements();

            Vector vector = new Vector();
            vector.add("Select");

            while (resultSet.next()) {

                vector.add(resultSet.getString("name"));
                supplierMap.put(resultSet.getString("name"), resultSet.getString("id"));

            }

            model.addAll(vector);
            jComboBox9.setSelectedIndex(0);

        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        // TODO add your handling code here:

        jTextField17.requestFocus();

    }//GEN-LAST:event_jComboBox10ItemStateChanged

    private void jTextField17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == 114) {

            String quary = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id` = `product`.`category_id` ";

            ProductView productView = new ProductView(quary, jTextField17);
            productView.setVisible(true);
            productView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        } else if (evt.getKeyCode() == 10) {

            try {

                String productId = jTextField17.getText();

                ResultSet resultSet = SQLConnector.search(" SELECT * FROM `product` WHERE `id`='" + productId + "' ");
                if (resultSet.next()) {

                    jTextField23.setText(resultSet.getString("name"));

                }

            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jTextField17KeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:

        if (jTextField17.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please. Enter or Select Product ID", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            int rowCount = jTable3.getRowCount();
            boolean productFound = false;

            if (rowCount != 0) {

                String productID = jTextField17.getText();

                for (int i = 0; i < rowCount; i++) {

                    String product_ID_2 = String.valueOf(jTable3.getValueAt(i, 0));

                    if (productID.equals(product_ID_2)) {

                        if (jFormattedTextField4.getText().isEmpty()) {

                            JOptionPane.showMessageDialog(this, "Please. Enter Count of Quantity");

                        } else if (Integer.parseInt(jFormattedTextField4.getText()) == 0) {

                            JOptionPane.showMessageDialog(this, "Quantity Must Higher than 0");

                        } else if (jFormattedTextField5.getText().isEmpty()) {

                            JOptionPane.showMessageDialog(this, "Please. Enter Return Price");

                        } else {

                            jFormattedTextField3.setText("");
                            jFormattedTextField3.setText(String.valueOf(Double.parseDouble(jFormattedTextField4.getText()) * Double.parseDouble(jFormattedTextField5.getText())));

                            jTable3.setValueAt(jFormattedTextField4.getText(), i, 2);
                            jTable3.setValueAt(jFormattedTextField5.getText(), i, 3);
                            jTable3.setValueAt(jFormattedTextField3.getText(), i, 4);

                            productFound = true;

                            JOptionPane.showMessageDialog(this, "Product Updated Successfully");

                            jTextField17.setText("");
                            jTextField23.setText("");
                            jFormattedTextField4.setText("0");
                            jFormattedTextField5.setText("0.00");
                            jFormattedTextField3.setText("0.00");

                            break;

                        }

                    }

                }

            }

            if (!productFound) {

                DefaultTableModel model = (DefaultTableModel) jTable3.getModel();

                Vector vector = new Vector();

                if (jTextField17.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Enter Product ID");

                } else if (jTextField23.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Press 'Enter' key after when you selected the product");

                } else if (jFormattedTextField4.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Enter Count of Quantity");

                } else if (Integer.parseInt(jFormattedTextField4.getText()) == 0) {

                    JOptionPane.showMessageDialog(this, "Quantity Must Higher than 0");

                } else if (jFormattedTextField5.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Please. Enter Return Price");

                } else {

                    jFormattedTextField3.setText("");
                    jFormattedTextField3.setText(String.valueOf(Double.parseDouble(jFormattedTextField4.getText()) * Double.parseDouble(jFormattedTextField5.getText())));

                    vector.add(jTextField17.getText());
                    vector.add(jTextField23.getText());
                    vector.add(jFormattedTextField4.getText());
                    vector.add(jFormattedTextField5.getText());
                    vector.add(jFormattedTextField3.getText());

                    model.addRow(vector);

                    JOptionPane.showMessageDialog(this, "Product Added Successfully");

                    jTextField17.setText("");
                    jTextField23.setText("");
                    jFormattedTextField4.setText("0");
                    jFormattedTextField5.setText("0.00");
                    jFormattedTextField3.setText("0.00");

                }

            }

        }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTable3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable3KeyReleased
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {

            removeDamageReturnTableRow();

        }

    }//GEN-LAST:event_jTable3KeyReleased

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {
            int selectedRow = jTable2.getSelectedRow();

            jTextField17.setText((String) jTable2.getValueAt(selectedRow, 0));
            jTextField23.setText((String) jTable2.getValueAt(selectedRow, 1));
            jFormattedTextField4.setText((String) jTable2.getValueAt(selectedRow, 2));
            jFormattedTextField5.setText((String) jTable2.getValueAt(selectedRow, 3));
            jFormattedTextField3.setText((String) jTable2.getValueAt(selectedRow, 4));

        }

    }//GEN-LAST:event_jTable3MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 1) {

            removeDamageReturnTableRow();

        }

    }//GEN-LAST:event_jLabel34MouseClicked

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:

        resetDamageReturn();

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jFormattedTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyReleased
        // TODO add your handling code here:

        calculateAmount();

    }//GEN-LAST:event_jFormattedTextField4KeyReleased

    private void jFormattedTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField5KeyReleased
        // TODO add your handling code here:

        calculateAmount();

    }//GEN-LAST:event_jFormattedTextField5KeyReleased

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:

        if (jComboBox10.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Please. Select Supplier", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            try {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = simpleDateFormat.format(new Date());

                ResultSet resultSet = SQLConnector.search(" SELECT * FROM `supplier_damage` ORDER BY `id` DESC ");
                if (resultSet.next()) {

                    HashMap< String, Object> reportMap = new HashMap<>();
                    System.out.println("WTF3");

                    reportMap.put("Parameter1", String.valueOf(currentDateTime));
                    reportMap.put("Parameter2", String.valueOf(jComboBox10.getSelectedItem()));
                    reportMap.put("Parameter3", String.valueOf(resultSet.getString("id")));

                    String reportPath = "src/com/reports/Damage_return_1.jasper";

                    JRTableModelDataSource jrTableModelDataSource = new JRTableModelDataSource(jTable3.getModel());
//                    JREmptyDataSource jrEmptyDataSource = new JREmptyDataSource();

                    JasperPrint fillReport = JasperFillManager.fillReport(reportPath, reportMap, jrTableModelDataSource);
//                    JasperPrint fillReport = JasperFillManager.fillReport(reportPath, hashMap, jrEmptyDataSource);

                    JasperViewer.viewReport(fillReport, false);
                }

                SQLConnector.iud(" INSERT INTO `supplier_damage`(`date_time`,`supplier_id`) VALUES ('" + currentDateTime + "','" + supplierMap.get(jComboBox10.getSelectedItem()) + "') ");

                int rowCount = jTable3.getRowCount();
                
                ResultSet resultSet1 = SQLConnector.search(" SELECT * FROM `supplier_damage` WHERE `date_time` LIKE '"+currentDateTime+"%' AND `supplier_id`='"+supplierMap.get(jComboBox10.getSelectedItem())+"' ORDER BY `id` DESC ");
                
                if ( resultSet1.next() ) {
                
                    for (int i = 0; i < rowCount; i++) {

                        SQLConnector.iud(" INSERT INTO `supplier_damage_item` (`product_id`,`return_price`,`qty`,`supplier_damage_id`) VALUES ('" + jTable3.getValueAt(i, 0) + "','" + jTable3.getValueAt(i, 3) + "','" + jTable3.getValueAt(i, 2) + "','"+ resultSet1.getString("id") +"') ");

                    }
                
                }
                
                JOptionPane.showMessageDialog(this, "Data Added Successfully");
                resetDamageReturn();
 
            } catch (SQLException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException ex) {
                Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jButton11ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased
        // TODO add your handling code here:

        grnRangeValidation();

    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jFormattedTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyReleased
        // TODO add your handling code here:

        grnRangeValidation();

    }//GEN-LAST:event_jFormattedTextField2KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
