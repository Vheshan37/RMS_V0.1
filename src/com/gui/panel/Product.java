package com.gui.panel;

import com.gui.CategoryView;
import com.gui.ProductView;
import com.model.CostingData;
import com.model.SQLConnector;
import com.model.getLogger;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class Product extends javax.swing.JPanel {

    public Product() {
        initComponents();
        setComponents();
    }

    public void setComponents() {
        setProductOverView();
        setDepartmentComboBox();
        setCategoryComboBox();
        setUnitComboBox();
        setPageDateTime();
        setGrnPageSuppliers();
        setGrnPagePaymentMethod();
        setLocationComboBox();
        loadEmployeeComboBox();
        damageReturnPage();
    }

    private void setProductOverView() {

        try {
            String query = "SELECT * FROM `product` INNER JOIN `product_department` ON `product_department`.`id`=`product`.`product_department_id`";
            int productCount = 0;
            int botCount = 0;
            int kotCount = 0;
            ResultSet productTable = SQLConnector.search(query);
            while (productTable.next()) {
                productCount += 1;
                if (productTable.getString("department").equals("BOT")) {
                    botCount += 1;
                } else {
                    kotCount += 1;
                }
            }

            jLabel2.setText(String.valueOf(productCount));
            jLabel4.setText(String.valueOf(kotCount));
            jLabel5.setText(String.valueOf(botCount));

            query = "SELECT SUM(qty) AS `count`, `name`,`department` FROM invoice_item "
                    + "INNER JOIN product ON product.id=invoice_item.product_id "
                    + "INNER JOIN product_department ON product_department.id=product.product_department_id "
                    + "WHERE product_department.department='BOT' "
                    + "GROUP BY `name` "
                    + "ORDER BY `count` DESC LIMIT 1";
            ResultSet popularBOT = SQLConnector.search(query);
            if (popularBOT.next()) {
                jLabel10.setText(popularBOT.getString("name"));
            } else {
                jLabel10.setText("???");
            }

            query = "SELECT SUM(qty) AS `count`, `name`,`department` FROM invoice_item "
                    + "INNER JOIN product ON product.id=invoice_item.product_id "
                    + "INNER JOIN product_department ON product_department.id=product.product_department_id "
                    + "WHERE product_department.department='KOT' "
                    + "GROUP BY `name` "
                    + "ORDER BY `count` DESC LIMIT 1";
            ResultSet popularKOT = SQLConnector.search(query);
            if (popularKOT.next()) {
                jLabel8.setText(popularBOT.getString("name"));
            } else {
                jLabel8.setText("???");
            }

            query = "SELECT SUM(qty) AS `count`, `name`,`department` FROM invoice_item "
                    + "INNER JOIN product ON product.id=invoice_item.product_id "
                    + "INNER JOIN product_department ON product_department.id=product.product_department_id "
                    + "WHERE product_department.department='BOT' "
                    + "GROUP BY `name` "
                    + "ORDER BY `count` ASC LIMIT 1";
            ResultSet leastPopularBOT = SQLConnector.search(query);
            if (leastPopularBOT.next()) {
                jLabel14.setText(leastPopularBOT.getString("name"));
            } else {
                jLabel14.setText("???");
            }

            query = "SELECT SUM(qty) AS `count`, `name`,`department` FROM invoice_item "
                    + "INNER JOIN product ON product.id=invoice_item.product_id "
                    + "INNER JOIN product_department ON product_department.id=product.product_department_id "
                    + "WHERE product_department.department='KOT' "
                    + "GROUP BY `name` "
                    + "ORDER BY `count` ASC LIMIT 1";
            ResultSet leastPopularKOT = SQLConnector.search(query);
            if (leastPopularKOT.next()) {
                jLabel12.setText(leastPopularKOT.getString("name"));
            } else {
                jLabel12.setText("???");
            }

            query = "SELECT COUNT(*) AS `active_products` FROM (SELECT COUNT(invoice_item.product_id),invoice_item.product_id FROM invoice_item	GROUP BY invoice_item.product_id) AS sub_table;";
            ResultSet activeProducts = SQLConnector.search(query);
            if (activeProducts.next()) {
                jLabel16.setText(activeProducts.getString("active_products"));
            } else {
                jLabel16.setText("???");
            }

        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }

    }

    private static HashMap<String, String> paymentMethodMap = new HashMap<>();
    private static HashMap<String, String> supplierMap = new HashMap<>();
    static HashMap<String, String> departmentMap = new HashMap<>();
    static HashMap<String, String> categoryMap = new HashMap<>();
    static HashMap<String, String> unitMap = new HashMap<>();
    static HashMap<String, String> typeMap = new HashMap<>();
    private static HashMap<String, CostingData> costingMap = new HashMap<>();
    private static HashMap<String, String> locationMap = new HashMap<>();
    private static HashMap<String, String> employeeMap = new HashMap<>();

    private void damageReturnPage() {
        try {
            ResultSet locationTable = SQLConnector.search("SELECT * FROM `location` INNER JOIN `location_status` ON `location_status`.`id`=`location`.`location_status_id` WHERE `location_status`.`status`='Cashier' ORDER BY `location` ASC");
            Vector<String> locationVector = new Vector();
            locationVector.add("Select");
            locationVector.add("Main Store");
            while (locationTable.next()) {
                locationVector.add(locationTable.getString("location"));
                locationMap.put(locationTable.getString("location"), locationTable.getString("location.id"));
            }
            jComboBox16.setModel(new DefaultComboBoxModel(locationVector));
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void loadEmployeeComboBox() {
        try {
            ResultSet employeeTable = SQLConnector.search("SELECT * FROM `employee` INNER JOIN `employee_type` ON `employee_type`.`id`=`employee`.`employee_type_id` ORDER BY `employee_type`.`type` ASC");
            Vector<String> employeeVector = new Vector();
            employeeVector.add("Select");
            while (employeeTable.next()) {
                employeeVector.add(employeeTable.getString("employee_type.type") + " - " + employeeTable.getString("name"));
                employeeMap.put(employeeTable.getString("employee_type.type") + " - " + employeeTable.getString("name"), employeeTable.getString("employee.id"));
            }

            jComboBox11.setModel(new DefaultComboBoxModel(employeeVector));
            jComboBox15.setModel(new DefaultComboBoxModel(employeeVector));
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void setLocationComboBox() {
        try {
            ResultSet locationTable = SQLConnector.search("SELECT * FROM `location` INNER JOIN `location_status` ON `location_status`.`id`=`location`.`location_status_id` WHERE `location_status`.`status`='Cashier' ORDER BY `location` ASC");
            Vector<String> locationVector = new Vector();
            locationVector.add("Select");
            while (locationTable.next()) {
                locationVector.add(locationTable.getString("location"));
                locationMap.put(locationTable.getString("location"), locationTable.getString("location.id"));
            }
            jComboBox10.setModel(new DefaultComboBoxModel(locationVector));
            jComboBox14.setModel(new DefaultComboBoxModel(locationVector));
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void setGrnPagePaymentMethod() {
        String query = "SELECT * FROM `payment_method`";
        try {
            ResultSet paymentMethodTable = SQLConnector.search(query);
            Vector paymentMethodVector = new Vector();
            paymentMethodVector.add("Select");
            while (paymentMethodTable.next()) {
                paymentMethodMap.put(paymentMethodTable.getString("method"), paymentMethodTable.getString("id"));
                paymentMethodVector.add(paymentMethodTable.getString("method"));
            }
            DefaultComboBoxModel paymentMethodModel = new DefaultComboBoxModel(paymentMethodVector);
            jComboBox9.setModel(paymentMethodModel);
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void setGrnPageSuppliers() {
        String query = "SELECT * FROM `supplier` INNER JOIN `supply_company` ON `supply_company`.`id`=`supplier`.`supply_company_id` ORDER BY `supply_company`.`company` ASC";
        try {
            ResultSet supplierTable = SQLConnector.search(query);
            Vector supplierVector = new Vector();
            supplierVector.add("Select");
            while (supplierTable.next()) {
                String supplierName = supplierTable.getString("company") + "-" + supplierTable.getString("name");
                supplierMap.put(supplierName, supplierTable.getString("supplier.id"));
                supplierVector.add(supplierName);
            }
            DefaultComboBoxModel supplierModel = new DefaultComboBoxModel(supplierVector);
            jComboBox8.setModel(supplierModel);
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void setPageDateTime() {
        String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm a")).format(new Date());
        jLabel43.setText(formattedDateTime);
        jLabel56.setText(formattedDateTime);
    }

    public void setUnitComboBox() {
        try {
            String query = "SELECT * FROM `product_unit`";
            ResultSet unitTable = SQLConnector.search(query);
            Vector unitVector = new Vector();
            unitVector.add("SELECT");
            while (unitTable.next()) {
                unitVector.add(unitTable.getString("unit"));
                unitMap.put(unitTable.getString("unit"), unitTable.getString("id"));
            }
            DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(unitVector);
            jComboBox3.setModel(comboBoxModel);
            jComboBox6.setModel(comboBoxModel);
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    public void setDepartmentComboBox() {
        try {
            String query = "SELECT * FROM `product_department`";
            ResultSet departmentTable = SQLConnector.search(query);
            Vector departmentVector = new Vector();
            departmentVector.add("SELECT");
            while (departmentTable.next()) {
                departmentVector.add(departmentTable.getString("department"));
                departmentMap.put(departmentTable.getString("department"), departmentTable.getString("id"));
            }

            JComboBox[] comboBoxArray = new JComboBox[4];
            comboBoxArray[0] = jComboBox1;
            comboBoxArray[1] = jComboBox4;
            comboBoxArray[2] = jComboBox12;
            comboBoxArray[3] = jComboBox13;

            for (int i = 0; i < comboBoxArray.length; i++) {
                DefaultComboBoxModel comboBoxModel1 = new DefaultComboBoxModel(departmentVector);
                comboBoxArray[i].setModel(comboBoxModel1);
            }

        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    public void setCategoryComboBox() {
        try {
            String query = "SELECT * FROM `category` ORDER BY `category` ASC";
            ResultSet categoryTable = SQLConnector.search(query);
            Vector categoryVector = new Vector();
            categoryVector.add("SELECT");
            while (categoryTable.next()) {
                categoryVector.add(categoryTable.getString("category"));
                categoryMap.put(categoryTable.getString("category"), categoryTable.getString("id"));
            }
            DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(categoryVector);
            jComboBox2.setModel(comboBoxModel);
            jComboBox5.setModel(comboBoxModel);
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productTypeSelector = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel25 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel30 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel43 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jTextField15 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel34 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel36 = new javax.swing.JPanel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jLabel55 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel56 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jTextField22 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jTextField27 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel40 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jPanel47 = new javax.swing.JPanel();
        jTextField32 = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jTextField37 = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jPanel48 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel51 = new javax.swing.JPanel();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jComboBox15 = new javax.swing.JComboBox<>();
        jLabel73 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox<>();
        jLabel71 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel46 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jComboBox16 = new javax.swing.JComboBox<>();
        jLabel79 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jPanel49 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel50 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jTextField43 = new javax.swing.JTextField();
        jPanel56 = new javax.swing.JPanel();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jComboBox12 = new javax.swing.JComboBox<>();
        jLabel65 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        jPanel45 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jPanel41 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jTextField29 = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jComboBox13 = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jLabel70 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(29, 44, 50));

        jTabbedPane1.setBackground(new java.awt.Color(29, 44, 50));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(29, 44, 50));

        jPanel10.setBackground(new java.awt.Color(29, 44, 50));
        jPanel10.setLayout(new java.awt.GridLayout(1, 3, 25, 25));

        jPanel11.setBackground(new java.awt.Color(28, 50, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(222, 242, 241));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("All Products");

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(222, 242, 241));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("???");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(28, 50, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(222, 242, 241));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("KOT Products");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(222, 242, 241));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("???");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(28, 50, 60));

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(222, 242, 241));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("???");

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(222, 242, 241));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("BOT Products");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel13);

        jPanel14.setBackground(new java.awt.Color(29, 44, 50));
        jPanel14.setLayout(new java.awt.GridLayout(1, 3, 25, 25));

        jPanel15.setBackground(new java.awt.Color(28, 50, 60));

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(222, 242, 241));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Popular KOT Product");

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(222, 242, 241));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("???");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel15);

        jPanel16.setBackground(new java.awt.Color(28, 50, 60));

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(222, 242, 241));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Popular BOT Product");

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(222, 242, 241));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("???");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel16);

        jPanel17.setBackground(new java.awt.Color(29, 44, 50));
        jPanel17.setLayout(new java.awt.GridLayout(1, 3, 25, 25));

        jPanel18.setBackground(new java.awt.Color(28, 50, 60));

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(222, 242, 241));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Least Popular KOT");

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(222, 242, 241));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("???");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel17.add(jPanel18);

        jPanel19.setBackground(new java.awt.Color(28, 50, 60));

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(222, 242, 241));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Least Popular BOT");

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(222, 242, 241));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("???");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel17.add(jPanel19);

        jPanel20.setBackground(new java.awt.Color(29, 44, 50));
        jPanel20.setLayout(new java.awt.GridLayout(1, 3, 25, 25));

        jPanel21.setBackground(new java.awt.Color(29, 44, 50));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel21);

        jPanel23.setBackground(new java.awt.Color(28, 50, 60));

        jLabel15.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(222, 242, 241));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Active Products");

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(222, 242, 241));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("???");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel20.add(jPanel23);

        jPanel22.setBackground(new java.awt.Color(29, 44, 50));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel20.add(jPanel22);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Overview", jPanel1);

        jPanel2.setBackground(new java.awt.Color(29, 44, 50));

        jPanel25.setBackground(new java.awt.Color(29, 44, 50));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(222, 242, 241));
        jLabel23.setText("Barcode");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(222, 242, 241));
        jLabel21.setText("Unit");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(222, 242, 241));
        jLabel22.setText("Measure");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(222, 242, 241));
        jLabel20.setText("Category");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(222, 242, 241));
        jLabel19.setText("Department");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(222, 242, 241));
        jLabel18.setText("Price");

        jTextField1.setBackground(new java.awt.Color(29, 54, 64));
        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField2.setBackground(new java.awt.Color(29, 54, 64));
        jTextField2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jComboBox1.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jComboBox2.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jComboBox3.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField3.setBackground(new java.awt.Color(29, 54, 64));
        jTextField3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField4.setBackground(new java.awt.Color(29, 54, 64));
        jTextField4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(222, 242, 241));
        jLabel17.setText("Name");

        jPanel24.setBackground(new java.awt.Color(29, 44, 50));

        jButton1.setBackground(new java.awt.Color(0, 60, 150));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Register");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 132, 188));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Refresh");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField4)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 330, Short.MAX_VALUE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addGap(274, 274, 274))
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel22)
                            .addComponent(jLabel18)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(46, 46, 46)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(46, 46, 46)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Create", jPanel2);

        jPanel3.setBackground(new java.awt.Color(29, 44, 50));

        jPanel26.setBackground(new java.awt.Color(29, 44, 50));

        jButton3.setBackground(new java.awt.Color(0, 60, 150));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Update");
        jButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 132, 188));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("Refresh");
        jButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(561, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel27.setBackground(new java.awt.Color(29, 44, 50));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(222, 242, 241));
        jLabel24.setText("Barcode");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(222, 242, 241));
        jLabel25.setText("Unit");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(222, 242, 241));
        jLabel27.setText("Measure");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(222, 242, 241));
        jLabel28.setText("Category");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(222, 242, 241));
        jLabel29.setText("Department");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(222, 242, 241));
        jLabel30.setText("Price");

        jTextField5.setBackground(new java.awt.Color(29, 54, 64));
        jTextField5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField7.setBackground(new java.awt.Color(29, 54, 64));
        jTextField7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jComboBox4.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jComboBox5.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jComboBox6.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField8.setBackground(new java.awt.Color(29, 54, 64));
        jTextField8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField9.setBackground(new java.awt.Color(29, 54, 64));
        jTextField9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(222, 242, 241));
        jLabel31.setText("Name");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(222, 242, 241));
        jLabel34.setText("Type");

        jComboBox7.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField9)
                        .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, 330, Short.MAX_VALUE)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(274, 274, 274))
                    .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField7)
                    .addComponent(jTextField8)
                    .addComponent(jLabel34)
                    .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(46, 46, 46))))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(46, 46, 46)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(46, 46, 46)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(29, 44, 50));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        jTextField6.setBackground(new java.awt.Color(29, 54, 64));
        jTextField6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jLabel32.setForeground(new java.awt.Color(58, 175, 169));
        jLabel32.setText("F3 - View Products");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(222, 242, 241));
        jLabel26.setText("Product ID");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Manage", jPanel3);

        jPanel5.setBackground(new java.awt.Color(29, 44, 50));

        jLabel43.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(58, 175, 169));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Date & Time");

        jPanel32.setBackground(new java.awt.Color(29, 44, 50));

        jLabel44.setBackground(new java.awt.Color(222, 242, 241));
        jLabel44.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel44.setText("Supplier");

        jComboBox8.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jTextField15.setBackground(new java.awt.Color(29, 54, 64));
        jTextField15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField15KeyReleased(evt);
            }
        });

        jLabel45.setBackground(new java.awt.Color(222, 242, 241));
        jLabel45.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel45.setText("Invoice No");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addGap(6, 6, 6))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel33.setBackground(new java.awt.Color(29, 44, 50));

        jScrollPane2.setMinimumSize(new java.awt.Dimension(452, 402));

        jTable2.setBackground(new java.awt.Color(33, 51, 58));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "QTY", "Cost", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel34.setBackground(new java.awt.Color(29, 44, 50));

        jLabel46.setBackground(new java.awt.Color(222, 242, 241));
        jLabel46.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel46.setText("ID");

        jTextField16.setBackground(new java.awt.Color(29, 54, 64));
        jTextField16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField16KeyReleased(evt);
            }
        });

        jLabel47.setBackground(new java.awt.Color(222, 242, 241));
        jLabel47.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel47.setText("Name");

        jTextField17.setBackground(new java.awt.Color(29, 54, 64));
        jTextField17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField17.setEnabled(false);

        jLabel48.setBackground(new java.awt.Color(222, 242, 241));
        jLabel48.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel48.setText("Qty");

        jTextField18.setBackground(new java.awt.Color(29, 54, 64));
        jTextField18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField18KeyReleased(evt);
            }
        });

        jLabel49.setBackground(new java.awt.Color(222, 242, 241));
        jLabel49.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel49.setText("Cost Price");

        jTextField19.setBackground(new java.awt.Color(29, 54, 64));
        jTextField19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField19KeyReleased(evt);
            }
        });

        jLabel50.setBackground(new java.awt.Color(222, 242, 241));
        jLabel50.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel50.setText("Value");

        jTextField20.setBackground(new java.awt.Color(29, 54, 64));
        jTextField20.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField20.setEnabled(false);
        jTextField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField20ActionPerformed(evt);
            }
        });
        jTextField20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField20KeyReleased(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 132, 188));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setText("Add");
        jButton9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel34Layout.createSequentialGroup()
                            .addComponent(jLabel50)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel34Layout.createSequentialGroup()
                            .addComponent(jLabel49)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel34Layout.createSequentialGroup()
                            .addComponent(jLabel48)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel35.setBackground(new java.awt.Color(29, 44, 50));

        jComboBox9.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        jLabel51.setBackground(new java.awt.Color(222, 242, 241));
        jLabel51.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel51.setText("Payment Method");

        jLabel52.setBackground(new java.awt.Color(222, 242, 241));
        jLabel52.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel52.setText("Total");

        jTextField21.setBackground(new java.awt.Color(29, 54, 64));
        jTextField21.setText("0");
        jTextField21.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField21.setEnabled(false);
        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });
        jTextField21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField21KeyReleased(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 60, 150));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setText("Proceed");
        jButton10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel53.setBackground(new java.awt.Color(222, 242, 241));
        jLabel53.setForeground(new java.awt.Color(58, 175, 169));
        jLabel53.setText("F3 - View Products");

        jLabel54.setBackground(new java.awt.Color(222, 242, 241));
        jLabel54.setForeground(new java.awt.Color(58, 175, 169));
        jLabel54.setText("Del - Remove Item");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel54))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("GRN", jPanel5);

        jPanel6.setBackground(new java.awt.Color(29, 44, 50));

        jPanel36.setBackground(new java.awt.Color(29, 44, 50));

        jComboBox10.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        jLabel55.setBackground(new java.awt.Color(222, 242, 241));
        jLabel55.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel55.setText("Location");

        jLabel57.setBackground(new java.awt.Color(222, 242, 241));
        jLabel57.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel57.setText("Goods Receiver");

        jComboBox11.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel55)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel56.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(58, 175, 169));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Date & Time");

        jPanel37.setBackground(new java.awt.Color(29, 44, 50));

        jTextField22.setBackground(new java.awt.Color(29, 54, 64));
        jTextField22.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField22KeyReleased(evt);
            }
        });

        jLabel58.setBackground(new java.awt.Color(222, 242, 241));
        jLabel58.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel58.setText("ID");

        jLabel59.setBackground(new java.awt.Color(222, 242, 241));
        jLabel59.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel59.setText("Name");

        jTextField23.setBackground(new java.awt.Color(29, 54, 64));
        jTextField23.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField23.setEnabled(false);

        jLabel60.setBackground(new java.awt.Color(222, 242, 241));
        jLabel60.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel60.setText("In Hand");

        jTextField24.setBackground(new java.awt.Color(29, 54, 64));
        jTextField24.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField24.setEnabled(false);
        jTextField24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField24KeyReleased(evt);
            }
        });

        jLabel61.setBackground(new java.awt.Color(222, 242, 241));
        jLabel61.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel61.setText("Issue Qty");

        jTextField25.setBackground(new java.awt.Color(29, 54, 64));
        jTextField25.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField25KeyReleased(evt);
            }
        });

        jLabel62.setBackground(new java.awt.Color(222, 242, 241));
        jLabel62.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel62.setText("Unit Price");

        jTextField26.setBackground(new java.awt.Color(29, 54, 64));
        jTextField26.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField26.setEnabled(false);
        jTextField26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField26ActionPerformed(evt);
            }
        });
        jTextField26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField26KeyReleased(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(0, 132, 188));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton11.setText("Add");
        jButton11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextField27.setBackground(new java.awt.Color(29, 54, 64));
        jTextField27.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField27.setEnabled(false);
        jTextField27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField27ActionPerformed(evt);
            }
        });
        jTextField27.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField27KeyReleased(evt);
            }
        });

        jLabel63.setBackground(new java.awt.Color(222, 242, 241));
        jLabel63.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel63.setText("Value");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField23, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(jLabel59))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel38.setBackground(new java.awt.Color(29, 44, 50));

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setBackground(new java.awt.Color(33, 51, 58));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Issue Qty", "Balance Qty", "Value"
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
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel40.setBackground(new java.awt.Color(29, 44, 50));

        jButton14.setBackground(new java.awt.Color(0, 132, 188));
        jButton14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton14.setText("Refresh");
        jButton14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 60, 150));
        jButton15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton15.setText("Issue");
        jButton15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel66.setBackground(new java.awt.Color(222, 242, 241));
        jLabel66.setForeground(new java.awt.Color(58, 175, 169));
        jLabel66.setText("F3 - View Products");

        jLabel67.setBackground(new java.awt.Color(222, 242, 241));
        jLabel67.setForeground(new java.awt.Color(58, 175, 169));
        jLabel67.setText("Del - Remove Item");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67)
                    .addComponent(jLabel66))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                        .addComponent(jLabel66)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel67)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Issue", jPanel6);

        jPanel7.setBackground(new java.awt.Color(29, 44, 50));

        jPanel47.setBackground(new java.awt.Color(29, 44, 50));

        jTextField32.setBackground(new java.awt.Color(29, 54, 64));
        jTextField32.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField32.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField32KeyReleased(evt);
            }
        });

        jLabel75.setBackground(new java.awt.Color(222, 242, 241));
        jLabel75.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel75.setText("ID");

        jLabel76.setBackground(new java.awt.Color(222, 242, 241));
        jLabel76.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel76.setText("Name");

        jTextField33.setBackground(new java.awt.Color(29, 54, 64));
        jTextField33.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField33.setEnabled(false);

        jLabel77.setBackground(new java.awt.Color(222, 242, 241));
        jLabel77.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel77.setText("Return Qty");

        jTextField34.setBackground(new java.awt.Color(29, 54, 64));
        jTextField34.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField34.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField34KeyReleased(evt);
            }
        });

        jLabel78.setBackground(new java.awt.Color(222, 242, 241));
        jLabel78.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel78.setText("Unit Price");

        jTextField35.setBackground(new java.awt.Color(29, 54, 64));
        jTextField35.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField35.setEnabled(false);
        jTextField35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField35KeyReleased(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 132, 188));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton12.setText("Add");
        jButton12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jTextField37.setBackground(new java.awt.Color(29, 54, 64));
        jTextField37.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField37.setEnabled(false);
        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });
        jTextField37.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField37KeyReleased(evt);
            }
        });

        jLabel80.setBackground(new java.awt.Color(222, 242, 241));
        jLabel80.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel80.setText("Amount");

        jTextField38.setBackground(new java.awt.Color(29, 54, 64));
        jTextField38.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField38ActionPerformed(evt);
            }
        });
        jTextField38.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField38KeyReleased(evt);
            }
        });

        jLabel81.setBackground(new java.awt.Color(222, 242, 241));
        jLabel81.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel81.setText("Reason");

        jLabel87.setBackground(new java.awt.Color(222, 242, 241));
        jLabel87.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel87.setText("In Hand");

        jTextField36.setBackground(new java.awt.Color(29, 54, 64));
        jTextField36.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField36.setEnabled(false);
        jTextField36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField36KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addComponent(jTextField38)
                        .addContainerGap())
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel75)
                            .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField33)
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addComponent(jLabel76)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel87)
                            .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77)
                            .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel78))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel80))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel81)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel47Layout.createSequentialGroup()
                            .addComponent(jLabel80)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel47Layout.createSequentialGroup()
                            .addComponent(jLabel78)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel47Layout.createSequentialGroup()
                            .addComponent(jLabel77)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addComponent(jLabel87)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel75)
                            .addComponent(jLabel76))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel48.setBackground(new java.awt.Color(29, 44, 50));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(33, 51, 58));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Item Name", "Return Qty", "Unit Price", "Amount"
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
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        }

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel51.setBackground(new java.awt.Color(29, 44, 50));

        jButton24.setBackground(new java.awt.Color(0, 132, 188));
        jButton24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton24.setText("Refresh");
        jButton24.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(0, 60, 150));
        jButton25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton25.setText("Return");
        jButton25.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jLabel85.setBackground(new java.awt.Color(222, 242, 241));
        jLabel85.setForeground(new java.awt.Color(58, 175, 169));
        jLabel85.setText("F3 - View Products");

        jLabel86.setBackground(new java.awt.Color(222, 242, 241));
        jLabel86.setForeground(new java.awt.Color(58, 175, 169));
        jLabel86.setText("Del - Remove Item");

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86)
                    .addComponent(jLabel85))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel86)))
                .addContainerGap())
        );

        jComboBox15.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox15ActionPerformed(evt);
            }
        });

        jLabel73.setBackground(new java.awt.Color(222, 242, 241));
        jLabel73.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel73.setText("Goods Receiver");

        jComboBox14.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox14ActionPerformed(evt);
            }
        });

        jLabel71.setBackground(new java.awt.Color(222, 242, 241));
        jLabel71.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel71.setText("Location");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel71)
                            .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel73)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Location Return", jPanel7);

        jPanel8.setBackground(new java.awt.Color(29, 44, 50));

        jPanel46.setBackground(new java.awt.Color(29, 44, 50));

        jLabel74.setBackground(new java.awt.Color(222, 242, 241));
        jLabel74.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel74.setText("Location");

        jComboBox16.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox16ActionPerformed(evt);
            }
        });

        jLabel79.setBackground(new java.awt.Color(222, 242, 241));
        jLabel79.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel79.setText("ID");

        jTextField39.setBackground(new java.awt.Color(29, 54, 64));
        jTextField39.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField39.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField39KeyReleased(evt);
            }
        });

        jLabel82.setBackground(new java.awt.Color(222, 242, 241));
        jLabel82.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel82.setText("Name");

        jTextField40.setBackground(new java.awt.Color(29, 54, 64));
        jTextField40.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField40.setEnabled(false);

        jLabel88.setBackground(new java.awt.Color(222, 242, 241));
        jLabel88.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel88.setText("In Hand");

        jTextField41.setBackground(new java.awt.Color(29, 54, 64));
        jTextField41.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField41.setEnabled(false);
        jTextField41.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField41KeyReleased(evt);
            }
        });

        jLabel83.setBackground(new java.awt.Color(222, 242, 241));
        jLabel83.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel83.setText("Damage Qty");

        jTextField42.setBackground(new java.awt.Color(29, 54, 64));
        jTextField42.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField42.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField42KeyReleased(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 132, 188));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton13.setText("Add");
        jButton13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel74)
                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField40, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel88)
                    .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel83)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel46Layout.createSequentialGroup()
                            .addComponent(jLabel83)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel46Layout.createSequentialGroup()
                            .addComponent(jLabel88)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel46Layout.createSequentialGroup()
                            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel79)
                                .addComponent(jLabel82))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jTextField40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(jLabel74)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel49.setBackground(new java.awt.Color(29, 44, 50));

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setBackground(new java.awt.Color(33, 51, 58));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Damage Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setPreferredWidth(80);
            jTable4.getColumnModel().getColumn(1).setPreferredWidth(350);
        }

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel50.setBackground(new java.awt.Color(29, 44, 50));

        jLabel84.setBackground(new java.awt.Color(222, 242, 241));
        jLabel84.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel84.setText("Reason");

        jTextField43.setBackground(new java.awt.Color(29, 54, 64));
        jTextField43.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField43)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addGap(46, 46, 46))
                    .addComponent(jTextField43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel56.setBackground(new java.awt.Color(29, 44, 50));

        jButton34.setBackground(new java.awt.Color(0, 132, 188));
        jButton34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton34.setText("Refresh");
        jButton34.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(0, 60, 150));
        jButton35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton35.setText("Add to Damage");
        jButton35.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jLabel97.setBackground(new java.awt.Color(222, 242, 241));
        jLabel97.setForeground(new java.awt.Color(58, 175, 169));
        jLabel97.setText("F3 - View Products");

        jLabel98.setBackground(new java.awt.Color(222, 242, 241));
        jLabel98.setForeground(new java.awt.Color(58, 175, 169));
        jLabel98.setText("Del - Remove Item");

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel98)
                    .addComponent(jLabel97))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel56Layout.createSequentialGroup()
                        .addComponent(jLabel97)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel98)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Damage Return", jPanel8);

        jPanel9.setBackground(new java.awt.Color(29, 44, 50));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jPanel39.setBackground(new java.awt.Color(29, 44, 50));

        jPanel4.setBackground(new java.awt.Color(29, 44, 50));

        jComboBox12.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        jLabel65.setBackground(new java.awt.Color(222, 242, 241));
        jLabel65.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel65.setText("Category Type");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel42.setBackground(new java.awt.Color(29, 44, 50));

        jLabel64.setBackground(new java.awt.Color(222, 242, 241));
        jLabel64.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel64.setText("Category Name");

        jTextField28.setBackground(new java.awt.Color(29, 54, 64));
        jTextField28.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField28.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField28KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField28)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel28.setBackground(new java.awt.Color(29, 44, 50));

        jButton16.setBackground(new java.awt.Color(0, 60, 150));
        jButton16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton16.setText("Register");
        jButton16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(131, Short.MAX_VALUE)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel45.setBackground(new java.awt.Color(29, 44, 50));

        jButton19.setBackground(new java.awt.Color(0, 132, 188));
        jButton19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton19.setText("Refresh");
        jButton19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(364, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel39);

        jPanel41.setBackground(new java.awt.Color(29, 44, 50));
        jPanel41.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(102, 102, 102)));

        jPanel29.setBackground(new java.awt.Color(29, 44, 50));

        jTextField29.setBackground(new java.awt.Color(29, 54, 64));
        jTextField29.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField29KeyReleased(evt);
            }
        });

        jLabel68.setBackground(new java.awt.Color(222, 242, 241));
        jLabel68.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel68.setText("ID");

        jTextField30.setBackground(new java.awt.Color(29, 54, 64));
        jTextField30.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jTextField30.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField30KeyReleased(evt);
            }
        });

        jLabel69.setBackground(new java.awt.Color(222, 242, 241));
        jLabel69.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel69.setText("Category Name");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField30)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(0, 191, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel31.setBackground(new java.awt.Color(29, 44, 50));

        jComboBox13.setBackground(new java.awt.Color(29, 54, 64));
        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jComboBox13.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });

        jLabel72.setBackground(new java.awt.Color(222, 242, 241));
        jLabel72.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel72.setText("Category Type");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox13, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel72)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel72)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel43.setBackground(new java.awt.Color(29, 44, 50));

        jButton17.setBackground(new java.awt.Color(0, 60, 150));
        jButton17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton17.setText("Update Changes");
        jButton17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel43Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel44.setBackground(new java.awt.Color(29, 44, 50));

        jButton18.setBackground(new java.awt.Color(0, 132, 188));
        jButton18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton18.setText("Refresh");
        jButton18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel70.setBackground(new java.awt.Color(222, 242, 241));
        jLabel70.setForeground(new java.awt.Color(58, 175, 169));
        jLabel70.setText("F3 - View Categories");

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel70)))
                .addContainerGap())
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel70)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(364, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel41);

        jTabbedPane1.addTab("Category", jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private static int totalValue;
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        if (jComboBox10.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a valid location", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox11.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a valid employee", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String locationID = locationMap.get(jComboBox10.getSelectedItem());
            String employeeID = employeeMap.get(jComboBox11.getSelectedItem());
            String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
            String query = "INSERT INTO issue (`date_time`,`location_id`,employee_id) "
                    + "VALUES ('" + formattedDateTime + "','" + locationID + "','" + employeeID + "')";
            try {
                long last_inserted_id = SQLConnector.iud(query);
                for (int issueIteration = 0; issueIteration < jTable3.getRowCount(); issueIteration++) {
                    totalValue += Integer.parseInt(String.valueOf(jTable3.getValueAt(issueIteration, 4)));
                    query = "INSERT INTO issue_item (issue_id,product_id,qty) "
                            + "VALUES ('" + last_inserted_id + "','" + jTable3.getValueAt(issueIteration, 0) + "','" + jTable3.getValueAt(issueIteration, 2) + "')";
                    SQLConnector.iud(query);
                    query = "SELECT * FROM `product` WHERE id='" + jTable3.getValueAt(issueIteration, 0) + "'";
                    ResultSet productTable = SQLConnector.search(query);
                    int ProductMeasure = 1;
                    if (productTable.next()) {
                        ProductMeasure = productTable.getInt("measure");
                        int newQty = Integer.parseInt(jTable3.getValueAt(issueIteration, 3).toString()) * ProductMeasure;
                        query = "UPDATE stock SET qty='" + newQty + "' WHERE product_id='" + jTable3.getValueAt(issueIteration, 0) + "'";
                        SQLConnector.iud(query);
                    }

                    query = "SELECT * FROM location_stock WHERE product_id='" + jTable3.getValueAt(issueIteration, 0) + "'";
                    ResultSet locationStockTable = SQLConnector.search(query);
                    if (locationStockTable.next()) {
                        double currentQty = locationStockTable.getDouble("qty");
                        double newQty = currentQty + Double.parseDouble(jTable3.getValueAt(issueIteration, 2).toString()) * ProductMeasure;
                        query = "UPDATE location_stock SET qty='" + newQty + "' WHERE product_id='" + jTable3.getValueAt(issueIteration, 0) + "'";
                        SQLConnector.iud(query);
                    } else {
                        int newQty = Integer.parseInt(jTable3.getValueAt(issueIteration, 2).toString()) * ProductMeasure;
                        query = "INSERT INTO location_stock (qty,product_id,location_id) "
                                + "VALUES ('" + newQty + "','" + jTable3.getValueAt(issueIteration, 0) + "','" + locationMap.get(jComboBox10.getSelectedItem()) + "')";
                        SQLConnector.iud(query);
                    }
                }
                JOptionPane.showMessageDialog(this, "Transaction completed", "Complete", JOptionPane.INFORMATION_MESSAGE);
                viewIssueJasperReport(last_inserted_id, totalValue);
                clearnIssuePage();
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTextField27KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField27KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField27KeyReleased

    private void jTextField27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField27ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (jTextField22.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the 'Product ID'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField25.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the 'Issue QTY'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            Vector productRow = new Vector();
            productRow.add(jTextField22.getText());
            productRow.add(jTextField23.getText());
            productRow.add(jTextField25.getText());
            int inHand = Integer.parseInt(jTextField24.getText());
            int issue = Integer.parseInt(jTextField25.getText());
            productRow.add(inHand - issue);
            productRow.add(jTextField27.getText());

            DefaultTableModel issueTable = (DefaultTableModel) jTable3.getModel();
            issueTable.addRow(productRow);
            jTable3.setModel(issueTable);

            refreshIssuePageProductInput();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTextField26KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField26KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26KeyReleased

    private void jTextField26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26ActionPerformed

    private void jTextField25KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField25KeyReleased
        if (evt.getKeyCode() == 10) {
            int qty = Integer.parseInt(jTextField25.getText());
            int price = Integer.parseInt(jTextField26.getText());
            jTextField27.setText(String.valueOf(qty * price));
            jButton11.grabFocus();
        }
    }//GEN-LAST:event_jTextField25KeyReleased

    private void jTextField24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24KeyReleased

    private void jTextField22KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField22KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == 114) {
            String query = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id`=`product`.`category_id`";
            ProductView pv = new ProductView(query, jTextField22);
            pv.setVisible(true);
            pv.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            jTextField22.grabFocus();
        } else if (evt.getKeyCode() == 10) {
            loadTextField(jTextField22, jTextField23);
            try {
                String query = "SELECT * FROM `stock` INNER JOIN `product` ON `product`.`id`=`stock`.`product_id` WHERE `product_id`='" + jTextField22.getText() + "'";
                ResultSet stockTable = SQLConnector.search(query);
                if (stockTable.next()) {
                    int measure = stockTable.getInt("measure");
                    int stock = stockTable.getInt("qty");
                    int qty = stock / measure;
                    jTextField24.setText(String.valueOf(qty));
                    jTextField26.setText(String.valueOf(stockTable.getInt("sale_price")));
                }
                jTextField25.grabFocus();
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
            jTextField18.grabFocus();
        }
    }//GEN-LAST:event_jTextField22KeyReleased

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11ActionPerformed

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (jComboBox8.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a 'Supplier'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox9.getSelectedItem().equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select a 'Payment Method'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (jTextField15.getText().isEmpty()) {
                int invoiceConfirmation = JOptionPane.showConfirmDialog(this, "Do you want to proceed without 'Invoice No'", "Alert", JOptionPane.YES_NO_OPTION);
                if (invoiceConfirmation == 0) {

                    String status;
                    if (jComboBox9.getSelectedItem().equals("Credit")) {
                        status = "Pending";
                    } else {
                        status = "Paid";
                    }
                    String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
                    String query = "INSERT INTO `grn` (`date_time`,`supplier_id`,`grn_status_id`,`payment_method_id`) "
                            + "VALUES('" + formattedDateTime + "','" + supplierMap.get(jComboBox8.getSelectedItem()) + "',(SELECT id FROM grn_status WHERE status='" + status + "' LIMIT 1),'" + paymentMethodMap.get(jComboBox9.getSelectedItem()) + "')";
                    try {
                        long returnId = SQLConnector.iud(query);
                        for (int grnIteration = 0; grnIteration < jTable2.getRowCount(); grnIteration++) {
                            query = "INSERT INTO grn_item (`grn_id`,`product_id`,`qty`,`cost_price`) "
                                    + "VALUES ('" + returnId + "','" + jTable2.getValueAt(grnIteration, 0) + "','" + jTable2.getValueAt(grnIteration, 2) + "','" + jTable2.getValueAt(grnIteration, 3) + "')";
                            SQLConnector.iud(query);

                            query = "SELECT * FROM `stock` WHERE product_id='" + jTable2.getValueAt(grnIteration, 0) + "'";
                            ResultSet stockTable = SQLConnector.search(query);

                            query = "SELECT * FROM `product` WHERE id='" + jTable2.getValueAt(grnIteration, 0) + "'";
                            ResultSet productTable = SQLConnector.search(query);

                            if (stockTable.next()) {
                                if (productTable.next()) {
                                    int Qty = stockTable.getInt("qty") + (Integer.parseInt(jTable2.getValueAt(grnIteration, 2).toString()) * productTable.getInt("measure"));
                                    query = "UPDATE stock SET qty='" + Qty + "',cost_price='" + jTable2.getValueAt(grnIteration, 3) + "' WHERE product_id='" + jTable2.getValueAt(grnIteration, 0) + "'";
                                    SQLConnector.iud(query);
                                }
                            } else {
                                if (productTable.next()) {
                                    int Qty = (Integer.parseInt(jTable2.getValueAt(grnIteration, 2).toString())) * productTable.getInt("measure");
                                    query = "INSERT INTO stock (cost_price,qty,product_id) VALUES('" + jTable2.getValueAt(grnIteration, 3) + "','" + Qty + "','" + jTable2.getValueAt(grnIteration, 0) + "')";
                                    SQLConnector.iud(query);
                                }
                            }
                        }

                        viewGrnJasperReport();

                        clearGrnPage();
                    } catch (Exception e) {
//                        e.printStackTrace();
                        getLogger.logger().warning(e.toString());
                    }
                }
            } else {
                //                With Invoice No

                String status;
                if (jComboBox9.getSelectedItem().equals("Credit")) {
                    status = "Pending";
                } else {
                    status = "Paid";
                }
                String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
                String query = "INSERT INTO `grn` (`date_time`,`supplier_id`,`grn_status_id`,`payment_method_id`,`invoice_no`) "
                        + "VALUES('" + formattedDateTime + "',"
                        + "'" + supplierMap.get(jComboBox8.getSelectedItem()) + "',(SELECT id FROM grn_status WHERE status='" + status + "' LIMIT 1),'" + paymentMethodMap.get(jComboBox9.getSelectedItem()) + "','" + jTextField15.getText() + "')";
                try {
                    long returnId = SQLConnector.iud(query);
                    for (int grnIteration = 0; grnIteration < jTable2.getRowCount(); grnIteration++) {
                        query = "INSERT INTO grn_item (`grn_id`,`product_id`,`qty`,`cost_price`) "
                                + "VALUES ('" + returnId + "','" + jTable2.getValueAt(grnIteration, 0) + "','" + jTable2.getValueAt(grnIteration, 2) + "','" + jTable2.getValueAt(grnIteration, 3) + "')";
                        SQLConnector.iud(query);

                        query = "SELECT * FROM `stock` WHERE product_id='" + jTable2.getValueAt(grnIteration, 0) + "'";
                        ResultSet stockTable = SQLConnector.search(query);

                        query = "SELECT * FROM `product` WHERE id='" + jTable2.getValueAt(grnIteration, 0) + "'";
                        ResultSet productTable = SQLConnector.search(query);

                        if (stockTable.next()) {
                            if (productTable.next()) {
                                int Qty = stockTable.getInt("qty") + (Integer.parseInt(jTable2.getValueAt(grnIteration, 2).toString()) * productTable.getInt("measure"));
                                query = "UPDATE stock SET qty='" + Qty + "' WHERE product_id='" + jTable2.getValueAt(grnIteration, 0) + "'";
                                SQLConnector.iud(query);
                            }
                        } else {
                            if (productTable.next()) {
                                int Qty = (Integer.parseInt(jTable2.getValueAt(grnIteration, 2).toString())) * productTable.getInt("measure");
                                query = "INSERT INTO stock (cost_price,qty,product_id) VALUES('" + jTable2.getValueAt(grnIteration, 3) + "','" + Qty + "','" + jTable2.getValueAt(grnIteration, 0) + "')";
                                SQLConnector.iud(query);
                            }
                        }
                    }

                    viewGrnJasperReport();

                    clearGrnPage();
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void viewGrnJasperReport() {
        try {
            ResultSet ctrlTable = SQLConnector.search("SELECT * FROM `ctrl`");
            if (ctrlTable.next()) {
                String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd")).format(new Date());
                String date = formattedDateTime;
                formattedDateTime = (new SimpleDateFormat("HH:mm a")).format(new Date());
                String time = formattedDateTime;

                HashMap<String, Object> parameterSet = new HashMap<>();
                parameterSet.put("SystemName", ctrlTable.getString("system_name"));
                parameterSet.put("Date", "Date: " + date);
                parameterSet.put("Time", "Time: " + time);

                if (jTextField15.getText().isEmpty()) {
                    parameterSet.put("InvoiceNo", "Not set");
                } else {
                    parameterSet.put("InvoiceNo", "Invoice No: " + jTextField15.getText());
                }
                parameterSet.put("Supplier", "Supplier: " + String.valueOf(jComboBox8.getSelectedItem()));
                parameterSet.put("Total", "Rs. " + jTextField21.getText() + ".00");
                parameterSet.put("PaymentMethod", String.valueOf(jComboBox9.getSelectedItem()));

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable2.getModel());

                String path = "src/com/reports/newGRN.jasper";

                JasperPrint report = JasperFillManager.fillReport(path, parameterSet, dataSource);
                JasperViewer.viewReport(report, false);
            } else {
                System.out.println("No records found in 'CTRL'");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void viewIssueJasperReport(long issueID, int totalValue) {
        try {
            ResultSet ctrlTable = SQLConnector.search("SELECT * FROM `ctrl`");
            if (ctrlTable.next()) {
                String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd")).format(new Date());
                String date = formattedDateTime;
                formattedDateTime = (new SimpleDateFormat("HH:mm a")).format(new Date());
                String time = formattedDateTime;

                HashMap<String, Object> parameterSet = new HashMap<>();
                parameterSet.put("SystemName", ctrlTable.getString("system_name"));
                parameterSet.put("Date", "Date: " + date);
                parameterSet.put("Time", "Time: " + time);
                parameterSet.put("IssueNo", "Issue No: #" + String.format("%08d", issueID));
                parameterSet.put("Location", "Location: " + String.valueOf(jComboBox10.getSelectedItem()));
                parameterSet.put("Total", "Rs. " + String.valueOf(totalValue) + ".00");
                parameterSet.put("Employee", String.valueOf(jComboBox11.getSelectedItem()));

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable3.getModel());

                String path = "src/com/reports/newIssue.jasper";

                JasperPrint report = JasperFillManager.fillReport(path, parameterSet, dataSource);
                JasperViewer.viewReport(report, false);
            } else {
                System.out.println("No records found in 'CTRL'");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void jTextField21KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField21KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21KeyReleased

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed

    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        addGrnItemToTable();
        clearInputs();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField20KeyReleased
        if (evt.getKeyCode() == 10) {
            addGrnItemToTable();
            clearInputs();
        }
    }//GEN-LAST:event_jTextField20KeyReleased

    private void jTextField20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField20ActionPerformed

    private void jTextField19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField19KeyReleased
        if (evt.getKeyCode() == 10) {
            int value = Integer.parseInt(jTextField18.getText()) * Integer.parseInt(jTextField19.getText());
            jTextField20.setText(String.valueOf(value));
            jButton9.grabFocus();
        }
    }//GEN-LAST:event_jTextField19KeyReleased

    private void jTextField18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyReleased
        if (evt.getKeyCode() == 10) {
            jTextField19.grabFocus();
        }
    }//GEN-LAST:event_jTextField18KeyReleased

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        if (evt.getKeyCode() == 114) {
            String query = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id`=`product`.`category_id`";
            ProductView pv = new ProductView(query, jTextField16);
            pv.setVisible(true);
            pv.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            jTextField16.grabFocus();
        } else if (evt.getKeyCode() == 10) {
            loadTextField(jTextField16, jTextField17);
            try {
                String query = "SELECT * FROM stock WHERE product_id='" + jTextField16.getText() + "'";
                ResultSet stockTable = SQLConnector.search(query);
                if (stockTable.next()) {
                    jTextField19.setText(String.valueOf(stockTable.getInt("cost_price")));
                } else {
                    jTextField19.setText("0");
                }
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
            jTextField18.grabFocus();
        }
    }//GEN-LAST:event_jTextField16KeyReleased

    private void jTextField15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15KeyReleased

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        refreshProductManagePanel();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String name = jTextField5.getText();
        String price = jTextField7.getText();
        String measure = jTextField8.getText();
        String barcode = jTextField9.getText();
        String department = String.valueOf(jComboBox4.getSelectedItem());
        String category = String.valueOf(jComboBox5.getSelectedItem());
        String unit = String.valueOf(jComboBox6.getSelectedItem());

        String query = "UPDATE product SET "
                + "name='" + name + "',"
                + "sale_price='" + price + "',"
                + "measure='" + measure + "',"
                + "barcode='" + barcode + "',"
                + "product_department_id='" + departmentMap.get(department) + "',"
                + "category_id='" + categoryMap.get(category) + "',"
                + "product_unit_id='" + unitMap.get(unit) + "' WHERE id='" + jTextField6.getText() + "'";

        try {
            SQLConnector.iud(query);
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
        refreshProductManagePanel();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        if (evt.getKeyCode() == 114) {
            String query = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id`=`product`.`category_id`";
            ProductView pv = new ProductView(query, jTextField6);
            pv.setVisible(true);
            pv.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } else if (evt.getKeyCode() == 10) {
            if (!jTextField6.getText().isEmpty()) {
                try {
                    String query = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id`=`product`.`category_id` INNER JOIN `product_department` ON `product_department`.`id`=`product`.`product_department_id` INNER JOIN `product_unit` ON `product_unit`.`id`=`product`.`product_unit_id` WHERE `product`.`id`='" + jTextField6.getText() + "'";
                    ResultSet productTable = SQLConnector.search(query);
                    if (productTable.next()) {

                        jTextField5.setText(productTable.getString("name"));
                        jTextField7.setText(productTable.getString("sale_price"));
                        jTextField8.setText(productTable.getString("measure"));
                        jTextField9.setText(productTable.getString("barcode"));
                        jComboBox4.setSelectedItem(productTable.getString("product_department.department"));
                        jComboBox5.setSelectedItem(productTable.getString("category.category"));
                        jComboBox6.setSelectedItem(productTable.getString("product_unit.unit"));

                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Product ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your product ID", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String name = jTextField1.getText();
        String price = jTextField2.getText();
        String measure = jTextField3.getText();
        String barcode = jTextField4.getText();
        String department = String.valueOf(jComboBox1.getSelectedItem());
        String category = String.valueOf(jComboBox2.getSelectedItem());
        String unit = String.valueOf(jComboBox3.getSelectedItem());

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the product name", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the product selling price", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (department.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select valid product department", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (category.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select valid product category", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (unit.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select valid product unit", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (measure.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the product measure", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                Integer.parseInt(price);
                String query;
                if (barcode.isEmpty()) {
                    query = "INSERT INTO `product` "
                            + "(`name`,`sale_price`,`discount`,`vat`,`measure`,`product_unit_id`,`product_department_id`,`category_id`,`barcode`) "
                            + "VALUES('" + name + "','" + price + "','0','0','" + measure + "','" + unitMap.get(unit) + "','" + departmentMap.get(department) + "','" + categoryMap.get(category) + "','0')";
                } else {
                    query = "INSERT INTO `product` "
                            + "(`name`,`sale_price`,`discount`,`vat`,`measure`,`product_unit_id`,`product_department_id`,`category_id`,`barcode`) "
                            + "VALUES('" + name + "','" + price + "','0','0','" + measure + "','" + unitMap.get(unit) + "','" + departmentMap.get(department) + "','" + categoryMap.get(category) + "','" + barcode + "')";
                }

                Long product_id = SQLConnector.iud(query);

                refreshProductRegisterPanel();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Please check the price", "Alert", JOptionPane.INFORMATION_MESSAGE);
                getLogger.logger().warning(e.toString());
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField28KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField28KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField28KeyReleased

    private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed

    }//GEN-LAST:event_jComboBox12ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        String category = jTextField28.getText();
        String departmentType = String.valueOf(jComboBox12.getSelectedItem());
        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the 'Category Name'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox12.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a valid 'Department'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String query = "SELECT * FROM `category` WHERE `category`='" + category + "'";
            try {
                ResultSet categoryTable = SQLConnector.search(query);
                if (categoryTable.next()) {
                    JOptionPane.showMessageDialog(this, "Category is already registerd", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    query = "INSERT INTO `category` (`category`,`product_department_id`) VALUE ('" + category + "','" + departmentMap.get(departmentType) + "')";
                    SQLConnector.iud(query);
                    refreshCategoryPageRegister();
                }
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    public void refreshCategoryPageRegister() {
        jTextField28.setText("");
        jTextField29.setText("");
        jTextField30.setText("");
        jComboBox12.setSelectedIndex(0);
        jComboBox13.setSelectedIndex(0);
        jTextField28.grabFocus();
    }

    private void jTextField29KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField29KeyReleased
        if (evt.getKeyCode() == 114) {
            String query = "SELECT * FROM `category` INNER JOIN `product_department` ON `product_department`.`id`=`category`.`product_department_id`";
            CategoryView pv = new CategoryView(query, jTextField29);
            pv.setVisible(true);
            pv.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } else if (evt.getKeyCode() == 10) {
            if (jTextField29.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter 'Product ID'", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                String query = "SELECT * FROM `category` INNER JOIN `product_department` ON `product_department`.`id`=`category`.`product_department_id` WHERE `category`.`id`='" + jTextField29.getText() + "'";
                try {
                    ResultSet categoryTable = SQLConnector.search(query);
                    if (categoryTable.next()) {
                        jTextField30.setText(categoryTable.getString("category"));
                        jComboBox13.setSelectedItem(categoryTable.getString("department"));
                        jTextField30.grabFocus();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            }
        }
    }//GEN-LAST:event_jTextField29KeyReleased

    private void jTextField30KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField30KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField30KeyReleased

    private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox13ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        String newCategoryName = jTextField30.getText();
        String categoryType = String.valueOf(jComboBox13.getSelectedItem());
        try {
            String query = "SELECT * FROM `category` WHERE id='" + jTextField29.getText() + "'";
            ResultSet categoryTable = SQLConnector.search(query);
            if (jComboBox13.getSelectedIndex() != 0) {
                if (categoryTable.next()) {
                    if (categoryTable.getString("category").equals(newCategoryName) && categoryTable.getString("product_department_id").equals(departmentMap.get(categoryType))) {
                        JOptionPane.showMessageDialog(this, "There has no update to change", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    } else if (!categoryTable.getString("category").equals(newCategoryName) && categoryTable.getString("product_department_id").equals(departmentMap.get(categoryType))) {
                        JOptionPane.showMessageDialog(this, "Category name is changed", "Complete", JOptionPane.INFORMATION_MESSAGE);
                        SQLConnector.iud("UPDATE category SET category='" + jTextField30.getText() + "' WHERE id='" + jTextField29.getText() + "'");
                        refreshCategoryPageManage();
                    } else if (categoryTable.getString("category").equals(newCategoryName) && !categoryTable.getString("product_department_id").equals(departmentMap.get(categoryType))) {
                        JOptionPane.showMessageDialog(this, "Category type is changed", "Complete", JOptionPane.INFORMATION_MESSAGE);
                        SQLConnector.iud("UPDATE category SET product_department_id='" + departmentMap.get(categoryType) + "' WHERE id='" + jTextField29.getText() + "'");
                        refreshCategoryPageManage();
                    } else {
                        JOptionPane.showMessageDialog(this, "Category Details changed", "Complete", JOptionPane.INFORMATION_MESSAGE);
                        SQLConnector.iud("UPDATE category SET product_department_id='" + departmentMap.get(categoryType) + "',product_department_id='" + departmentMap.get(categoryType) + "' WHERE id='" + jTextField29.getText() + "'");
                        refreshCategoryPageManage();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid 'Category Type'", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    public void refreshCategoryPageManage() {
        jTextField29.setText("");
        jTextField30.setText("");
        jComboBox13.setSelectedIndex(0);
    }

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        refreshCategoryPageManage();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        refreshCategoryPageRegister();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        setComponents();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTable2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable2KeyReleased
        if (evt.getKeyCode() == 127) {
            DefaultTableModel table2Model = (DefaultTableModel) jTable2.getModel();
            table2Model.removeRow(jTable2.getSelectedRow());
            calculateGrnValue();
        }
    }//GEN-LAST:event_jTable2KeyReleased

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
    }//GEN-LAST:event_jTable3MouseClicked

    private void jComboBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14ActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed

    private void jTextField32KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField32KeyReleased
        if (evt.getKeyCode() == 114) {
            String query = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id`=`product`.`category_id`";
            ProductView pv = new ProductView(query, jTextField32);
            pv.setVisible(true);
            pv.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            jTextField32.grabFocus();
        } else if (evt.getKeyCode() == 10) {
            if (jComboBox14.getSelectedIndex() != 0) {
                loadTextField(jTextField32, jTextField33);
                try {
                    String query = "SELECT * FROM `location_stock` INNER JOIN `product` ON `product`.`id`=`location_stock`.`product_id` WHERE `product_id`='" + jTextField32.getText() + "' AND location_id='" + locationMap.get(jComboBox14.getSelectedItem()) + "'";
                    ResultSet locationStockTable = SQLConnector.search(query);

                    int productMeasure;
                    if (locationStockTable.next()) {
                        productMeasure = locationStockTable.getInt("measure");

                        int qty = locationStockTable.getInt("qty");
                        int value = (int) qty / productMeasure;

                        jTextField36.setText(String.valueOf(value));
                        jTextField35.setText(String.valueOf(locationStockTable.getInt("sale_price")));
                        jTextField34.grabFocus();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid location", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextField32KeyReleased

    private void jTextField34KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField34KeyReleased
        if (evt.getKeyCode() == 10) {
            int returnQty = Integer.parseInt(jTextField34.getText());
            int unitPrice = Integer.parseInt(jTextField35.getText());
            int value = returnQty * unitPrice;
            jTextField37.setText(String.valueOf(value));
            jButton12.grabFocus();
        }
    }//GEN-LAST:event_jTextField34KeyReleased

    private void jTextField35KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField35KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField35KeyReleased

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        SetReturnItemTable();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37ActionPerformed

    private void jTextField37KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField37KeyReleased
        SetReturnItemTable();
    }//GEN-LAST:event_jTextField37KeyReleased

    private void SetReturnItemTable() {
        Vector<String> returnItemVector = new Vector<>();
        returnItemVector.add(jTextField32.getText());
        returnItemVector.add(jTextField33.getText());
        returnItemVector.add(jTextField34.getText());
        returnItemVector.add(jTextField35.getText());
        returnItemVector.add(jTextField37.getText());
        DefaultTableModel returnItemTableModel = (DefaultTableModel) jTable1.getModel();
        returnItemTableModel.addRow(returnItemVector);
        clearnReturnInputs();
    }

    private void clearnReturnInputs() {
        jTextField32.setText("");
        jTextField33.setText("");
        jTextField34.setText("");
        jTextField35.setText("");
        jTextField36.setText("");
        jTextField37.setText("");
        jTextField32.grabFocus();
    }

    private void jTextField38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField38ActionPerformed

    private void jTextField38KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField38KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField38KeyReleased

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        refreshLocationReturn();
    }//GEN-LAST:event_jButton24ActionPerformed

    private static int totalReturnValue = 0;
    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        if (jTextField38.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your 'Reason'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jComboBox15.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a 'Employee'", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "There has no return items to return", "Alert", JOptionPane.QUESTION_MESSAGE);
            } else {
                String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
                String query = "INSERT INTO location_return (`date_time`,`reason`,`location_id`,`employee_id`) "
                        + "VALUES ('" + formattedDateTime + "','" + jTextField38.getText() + "','" + locationMap.get(jComboBox14.getSelectedItem()) + "','" + employeeMap.get(jComboBox15.getSelectedItem()) + "')";
                try {
                    long insert_id = SQLConnector.iud(query);
                    for (int returnIteration = 0; returnIteration < jTable1.getRowCount(); returnIteration++) {
                        totalReturnValue += Integer.parseInt(String.valueOf(jTable1.getValueAt(returnIteration, 4)));
                        query = "INSERT INTO location_return_item (product_id,location_return_id,qty) "
                                + "VALUES ('" + jTable1.getValueAt(returnIteration, 0) + "','" + insert_id + "','" + jTable1.getValueAt(returnIteration, 2) + "')";
                        SQLConnector.iud(query);
                        query = "SELECT * FROM `location_stock` INNER JOIN `product` ON `product`.`id`=`location_stock`.`product_id` WHERE `product_id`='" + jTable1.getValueAt(returnIteration, 0) + "' AND `location_id`='" + locationMap.get(jComboBox14.getSelectedItem()) + "'";
                        ResultSet locationStockTable = SQLConnector.search(query);
                        double returnAmount;

                        locationStockTable.next();
                        double locationQty = locationStockTable.getDouble("qty");
                        double productMeasure = locationStockTable.getDouble("measure");
                        returnAmount = productMeasure * Double.parseDouble(String.valueOf(jTable1.getValueAt(returnIteration, 2)));
                        double newStock = locationQty - returnAmount;
                        query = "UPDATE `location_stock` SET qty='" + newStock + "' WHERE `product_id`='" + jTable1.getValueAt(returnIteration, 0) + "' AND `location_id`='" + locationMap.get(jComboBox14.getSelectedItem()) + "'";
                        SQLConnector.iud(query);

                        query = "SELECT * FROM stock WHERE product_id='" + jTable1.getValueAt(returnIteration, 0) + "'";
                        ResultSet stockTable = SQLConnector.search(query);
                        stockTable.next();
                        double getStock = stockTable.getDouble("qty");
                        double returnStock = getStock + returnAmount;
                        query = "UPDATE stock SET qty='" + returnStock + "' WHERE product_id='" + jTable1.getValueAt(returnIteration, 0) + "'";
                        SQLConnector.iud(query);
                    }

                    JOptionPane.showMessageDialog(this, "Item returning completed", "Complete", JOptionPane.INFORMATION_MESSAGE);
                    viewLocationReturnJasperReport(insert_id, totalReturnValue);
                    refreshLocationReturn();
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            }
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void viewLocationReturnJasperReport(long returnID, int totalReturnValue) {
        try {
            ResultSet ctrlTable = SQLConnector.search("SELECT * FROM `ctrl`");
            if (ctrlTable.next()) {
                String formattedDateTime = (new SimpleDateFormat("yyyy/MM/dd")).format(new Date());
                String date = formattedDateTime;
                formattedDateTime = (new SimpleDateFormat("HH:mm a")).format(new Date());
                String time = formattedDateTime;

                HashMap<String, Object> parameterSet = new HashMap<>();
                parameterSet.put("SystemName", ctrlTable.getString("system_name"));
                parameterSet.put("Location", String.valueOf(jComboBox14.getSelectedItem()));
                parameterSet.put("Receiver", String.valueOf(jComboBox15.getSelectedItem()));
                parameterSet.put("Reason", String.valueOf(jTextField38.getText()));
                parameterSet.put("Date", "Date: " + date);
                parameterSet.put("Time", "Time: " + time);
                parameterSet.put("Total", "Rs. " + String.valueOf(totalReturnValue) + ".00");
                parameterSet.put("ReturnNo", "Return No: #" + String.format("%08d", returnID));

                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());

                String path = "src/com/reports/returnNote.jasper";

                JasperPrint report = JasperFillManager.fillReport(path, parameterSet, dataSource);
                JasperViewer.viewReport(report, false);
            } else {
                System.out.println("No records found in 'CTRL'");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    private void refreshLocationReturn() {
        jComboBox14.setSelectedIndex(0);
        jComboBox15.setSelectedIndex(0);
        jTextField32.setText("");
        jTextField33.setText("");
        jTextField34.setText("");
        jTextField35.setText("");
        jTextField36.setText("");
        jTextField37.setText("");
        jTextField38.setText("");
        DefaultTableModel returnTable = (DefaultTableModel) jTable1.getModel();
        returnTable.setRowCount(0);
        jTable1.setModel(returnTable);
    }

    private void jTextField36KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField36KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField36KeyReleased

    private void jComboBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox16ActionPerformed

    private void jTextField39KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField39KeyReleased
        if (evt.getKeyCode() == 114) {
            String query = "SELECT * FROM `product` INNER JOIN `category` ON `category`.`id`=`product`.`category_id`";
            ProductView pv = new ProductView(query, jTextField39);
            pv.setVisible(true);
            pv.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            jTextField39.grabFocus();
        } else if (evt.getKeyCode() == 10) {
            if (jComboBox16.getSelectedIndex() != 0) {
                loadTextField(jTextField39, jTextField40);
                try {
                    String query;
                    if (jComboBox16.getSelectedItem().equals("Main Store")) {
                        query = "SELECT * FROM stock INNER JOIN product ON product.id=stock.product_id WHERE product_id='" + jTextField39.getText() + "'";
                    } else {
                        query = "SELECT * FROM location_stock INNER JOIN product ON product.id=location_stock.product_id WHERE product_id='" + jTextField39.getText() + "' AND location_id='" + locationMap.get(jComboBox16.getSelectedItem()) + "'";
                    }
                    System.out.println(query);
                    ResultSet locationStockTable = SQLConnector.search(query);
                    int productMeasure;
                    if (locationStockTable.next()) {
                        productMeasure = locationStockTable.getInt("measure");

                        int qty = locationStockTable.getInt("qty");
                        int value = (int) qty / productMeasure;

                        jTextField41.setText(String.valueOf(value));
                        jTextField42.grabFocus();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    getLogger.logger().warning(e.toString());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid location", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextField39KeyReleased

    private void jTextField41KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField41KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField41KeyReleased

    private void jTextField42KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField42KeyReleased
        if (evt.getKeyCode() == 10) {
            addDamageTable();
        }
    }//GEN-LAST:event_jTextField42KeyReleased

    private void addDamageTable() {
        String id = jTextField39.getText();
        String name = jTextField40.getText();
        String damageQty = jTextField42.getText();
        Vector<String> damageVector = new Vector<>();
        damageVector.add(id);
        damageVector.add(name);
        damageVector.add(damageQty);

        DefaultTableModel damageTable = (DefaultTableModel) jTable4.getModel();
        damageTable.addRow(damageVector);
        jTable4.setModel(damageTable);
        refreshDamageInputs();
    }

    private void refreshDamageInputs() {
        jComboBox16.setEnabled(false);
        jTextField39.setText("");
        jTextField40.setText("");
        jTextField41.setText("");
        jTextField42.setText("");
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        addDamageTable();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        setDamageToDefault();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void setDamageToDefault() {
        jComboBox16.setEnabled(true);
        refreshDamageInputs();
        DefaultTableModel damageTable = (DefaultTableModel) jTable4.getModel();
        damageTable.setRowCount(0);
        jTable4.setModel(damageTable);
    }

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        if (jTable4.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "There are no damaged items to proceed", "Alert", JOptionPane.WARNING_MESSAGE);
            jTextField39.grabFocus();
        } else if (jTextField43.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add the 'Reason' to this", "Alert", JOptionPane.WARNING_MESSAGE);
            jTextField43.grabFocus();
        } else {
            String dateTime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
            String query = "INSERT INTO damage_return (`date_time`,`reason`,`location_id`) "
                    + "VALUES ('" + dateTime + "','" + jTextField43.getText() + "','" + locationMap.get(jComboBox16.getSelectedItem()) + "')";
            System.out.println(query);
            try {
                long insertID = SQLConnector.iud(query);
                for (int damageIteration = 0; damageIteration < jTable4.getRowCount(); damageIteration++) {

                    query = "SELECT * FROM product WHERE id='" + jTable4.getValueAt(damageIteration, 0) + "'";
                    System.out.println(query);
                    ResultSet productTable = SQLConnector.search(query);
                    productTable.next();

                    double newDamage = Double.parseDouble(String.valueOf(jTable4.getValueAt(damageIteration, 2)));
                    double productMeasure = productTable.getDouble("measure");
                    double damageQty = newDamage * productMeasure;
                    query = "INSERT INTO damage_return_item (qty,product_id,damage_return_id) "
                            + "VALUES ('" + damageQty + "','" + jTable4.getValueAt(damageIteration, 0) + "','" + insertID + "')";
                    System.out.println(query);
                    SQLConnector.iud(query);

                    if (jComboBox16.getSelectedItem().equals("Main Store")) {
                        query = "SELECT * FROM `stock` WHERE `product_id`='" + jTable4.getValueAt(damageIteration, 0) + "'";
                        ResultSet stockTable = SQLConnector.search(query);
                        stockTable.next();

                        double stockQty = stockTable.getDouble("qty");
                        double updateStockQty = stockQty - damageQty;
                        query = "UPDATE `stock` SET `qty`='" + updateStockQty + "' WHERE `product_id`='" + jTable4.getValueAt(damageIteration, 0) + "'";
                        System.out.println(query);
                        SQLConnector.iud(query);
                    } else {
                        query = "SELECT * FROM `location_stock` WHERE `location_id`='" + locationMap.get(jComboBox16.getSelectedItem()) + "' AND `product_id`='" + jTable4.getValueAt(damageIteration, 0) + "'";
                        ResultSet stockTable = SQLConnector.search(query);
                        stockTable.next();

                        double stockQty = stockTable.getDouble("qty");
                        double updateStockQty = stockQty - damageQty;
                        query = "UPDATE `location_stock` SET `qty`='" + updateStockQty + "' WHERE `product_id`='" + jTable4.getValueAt(damageIteration, 0) + "' AND location_id='" + locationMap.get(jComboBox16.getSelectedItem()) + "'";
                        System.out.println(query);
                        SQLConnector.iud(query);
                    }

                }
                setDamageToDefault();
            } catch (Exception e) {
//                e.printStackTrace();
                getLogger.logger().warning(e.toString());
            }
        }
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void refreshProductRegisterPanel() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
    }

    private void refreshProductManagePanel() {
        jTextField6.setText("");
        jTextField5.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jComboBox6.setSelectedIndex(0);
    }

    private void clearInputs() {
        jTextField16.setText("");
        jTextField17.setText("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");

        jTextField16.grabFocus();
    }

    private void addGrnItemToTable() {
        DefaultTableModel grnItemTableModel = (DefaultTableModel) jTable2.getModel();
        if (jTextField16.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select 'Product ID'", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (jTextField17.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load the 'Product Name'", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (jTextField18.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the `QTY`", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (jTextField19.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the `Cost Price`", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else if (jTextField20.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please press 'Enter' to 'Cost Price'", "Alert", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Vector grnItemVector = new Vector();
            grnItemVector.add(jTextField16.getText());
            grnItemVector.add(jTextField17.getText());
            grnItemVector.add(jTextField18.getText());
            grnItemVector.add(jTextField19.getText());
            grnItemVector.add(jTextField20.getText());

            grnItemTableModel.addRow(grnItemVector);
            jTable2.setModel(grnItemTableModel);

            calculateGrnValue();
        }
    }

    private void calculateGrnValue() {
        int total = 0;
        if (jTable2.getRowCount() == 0) {
            jTextField21.setText("0");
        } else {
            for (int i = 0; i < jTable2.getRowCount(); i++) {
                int itemValue = Integer.parseInt(jTable2.getValueAt(i, 4).toString());
                total += itemValue;
                jTextField21.setText(String.valueOf(total));
            }
        }
    }

    private void clearGrnPage() {
        jTextField15.setText("");
        jComboBox8.setSelectedIndex(0);
        ((DefaultTableModel) jTable2.getModel()).setRowCount(0);
        jTextField21.setText("0");
        jComboBox9.setSelectedIndex(0);
    }

    private void refreshIssuePageProductInput() {
        jTextField22.setText("");
        jTextField23.setText("");
        jTextField24.setText("");
        jTextField25.setText("");
        jTextField26.setText("");
        jTextField27.setText("");
    }

    private void clearnIssuePage() {
        refreshIssuePageProductInput();
        DefaultTableModel jTable3Model = (DefaultTableModel) jTable3.getModel();
        jTable3Model.setRowCount(0);
        jComboBox10.setSelectedIndex(0);
        jComboBox11.setSelectedIndex(0);
    }

    private void loadTextField(JTextField getter, JTextField setter) {
        try {
            ResultSet productTable = SQLConnector.search("SELECT * FROM product WHERE id='" + getter.getText() + "'");
            if (productTable.next()) {
                setter.setText(productTable.getString("product.name"));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid product id", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    public static javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField5;
    public static javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.ButtonGroup productTypeSelector;
    // End of variables declaration//GEN-END:variables
}
