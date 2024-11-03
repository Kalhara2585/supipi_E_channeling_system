package my_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class admin {

    private JFrame frame;
    private JList<String> recordList;
    private DefaultListModel<String> listModel;
    private JButton selectButton, loadButton, deleteButton, updateButton, insertButton;
    private JComboBox<String> tableSelection;

    private final String DB_URL = "jdbc:mysql://localhost:4306/supipi"; // Update your database URL
    private final String USER = "root"; // Update your MySQL username
    private final String PASS = ""; // Update your MySQL password

    public admin() {
        frame = new JFrame("Select Doctor or Customer");
        frame.setSize(1400, 690);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create MenuBar
        setupMenuBar();

        // JLayeredPane for background and components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1430, 690));

        JLabel background = new JLabel(new ImageIcon("C:\\Users\\KALHARA\\Desktop\\project folder/R.jpg"));
        background.setBounds(0, 0, 1400, 648);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        listModel = new DefaultListModel<>();
        recordList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(recordList);
        scrollPane.setBounds(450, 100, 500, 400);
        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);

        // Dropdown for selecting table
        tableSelection = new JComboBox<>(new String[]{"Doctors", "Customers", "Channeling"});
        tableSelection.setBounds(600, 60, 200, 30);
        layeredPane.add(tableSelection, JLayeredPane.PALETTE_LAYER);

        loadButton = new JButton("Load Records");
        loadButton.setBounds(500, 520, 150, 30);
        loadButton.addActionListener(e -> loadRecords());
        layeredPane.add(loadButton, JLayeredPane.PALETTE_LAYER);

        selectButton = new JButton("Select Record");
        selectButton.setBounds(700, 520, 150, 30);
        selectButton.addActionListener(e -> selectRecord());
        layeredPane.add(selectButton, JLayeredPane.PALETTE_LAYER);

        deleteButton = new JButton("Delete Record");
        deleteButton.setBounds(500, 560, 150, 30);
        deleteButton.addActionListener(e -> deleteRecord());
        layeredPane.add(deleteButton, JLayeredPane.PALETTE_LAYER);

        updateButton = new JButton("Update Record");
        updateButton.setBounds(700, 560, 150, 30);
        updateButton.addActionListener(e -> updateRecord());
        layeredPane.add(updateButton, JLayeredPane.PALETTE_LAYER);
        
        // New Insert Button
        insertButton = new JButton("Insert Record");
        insertButton.setBounds(900, 520, 150, 30);
        insertButton.addActionListener(e -> insertRecord());
        layeredPane.add(insertButton, JLayeredPane.PALETTE_LAYER);

        frame.add(layeredPane);
        frame.setVisible(true);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem backMenuItem = new JMenuItem("Back");
        backMenuItem.addActionListener(e -> {
            new login_register();
            frame.dispose();
        });
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(backMenuItem);
        fileMenu.add(exitMenuItem);

        JMenu homeMenu = new JMenu("Home");
        homeMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new login_register();
                frame.dispose();
            }

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
        });

        JMenu registerMenu = new JMenu("Register");
        registerMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
            	new customer_doctor_selection_view();
                frame.dispose();
            }

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
        });

        JMenu loginMenu = new JMenu("Login");
        loginMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
            	new doc_cust_login();
                frame.dispose();
            }

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
        });

        JMenu viewMenu = new JMenu("View");
        viewMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                JOptionPane.showMessageDialog(frame, "View selected");
            }

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
        });

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Doctor Selection System v1.0"));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(homeMenu);
        menuBar.add(registerMenu);
        menuBar.add(loginMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
    }

    private void loadRecords() {
        listModel.clear();
        String selectedTable = tableSelection.getSelectedItem().toString();
        String query = selectedTable.equals("Doctors") 
                ? "SELECT id, name, age, phone, job, gender, password, email FROM doctor"
                : selectedTable.equals("Customers")
                ? "SELECT cust_id, name, age, phone, gender, password, email FROM customer1"
                : "SELECT channel_no, doc_id, patient_name, appointment_date FROM channeldetails"; // Use correct table name

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String record;
                if (selectedTable.equals("Doctors")) {
                    record = rs.getInt("id") + ": " 
                            + rs.getString("name") + " | Age: " + rs.getInt("age") 
                            + " | Phone: " + rs.getString("phone") + " | Job: " + rs.getString("job") 
                            + " | Gender: " + rs.getString("gender") + " | Password: " + rs.getString("password") 
                            + " | Email: " + rs.getString("email");
                } else if (selectedTable.equals("Customers")) {
                    record = rs.getInt("cust_id") + ": " 
                            + rs.getString("name") + " | Age: " + rs.getInt("age") 
                            + " | Phone: " + rs.getString("phone") + " | Gender: " + rs.getString("gender") 
                            + " | Password: " + rs.getString("password") + " | Email: " + rs.getString("email");
                } else { // Channeling
                    record = rs.getInt("channel_no") + ": "
                            + "Doctor ID: " + rs.getInt("doc_id") + " | Patient Name: " + rs.getString("patient_name")
                            + " | Appointment Date: " + rs.getDate("appointment_date");
                }
                listModel.addElement(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading records: " + e.getMessage());
        }
    }

    private void insertRecord() {
        String selectedTable = tableSelection.getSelectedItem().toString();

        if (selectedTable.equals("Doctors")) {
            String name = JOptionPane.showInputDialog(frame, "Enter name:");
            String age = JOptionPane.showInputDialog(frame, "Enter age:");
            String phone = JOptionPane.showInputDialog(frame, "Enter phone:");
            String job = JOptionPane.showInputDialog(frame, "Enter job:");
            String gender = JOptionPane.showInputDialog(frame, "Enter gender:");
            String password = JOptionPane.showInputDialog(frame, "Enter password:");
            String email = JOptionPane.showInputDialog(frame, "Enter email:");

            if (name != null && age != null && phone != null && job != null && gender != null && password != null && email != null) {
                String query = "INSERT INTO doctor (name, age, phone, job, gender, password, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, Integer.parseInt(age));
                    pstmt.setString(3, phone);
                    pstmt.setString(4, job);
                    pstmt.setString(5, gender);
                    pstmt.setString(6, password);
                    pstmt.setString(7, email);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Doctor record inserted successfully!");
                    loadRecords(); // Refresh the list
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error inserting doctor record: " + e.getMessage());
                }
            }
        } else if (selectedTable.equals("Customers")) {
            String name = JOptionPane.showInputDialog(frame, "Enter name:");
            String age = JOptionPane.showInputDialog(frame, "Enter age:");
            String phone = JOptionPane.showInputDialog(frame, "Enter phone:");
            String gender = JOptionPane.showInputDialog(frame, "Enter gender:");
            String password = JOptionPane.showInputDialog(frame, "Enter password:");
            String email = JOptionPane.showInputDialog(frame, "Enter email:");

            if (name != null && age != null && phone != null && gender != null && password != null && email != null) {
                String query = "INSERT INTO customer1 (name, age, phone, gender, password, email) VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, Integer.parseInt(age));
                    pstmt.setString(3, phone);
                    pstmt.setString(4, gender);
                    pstmt.setString(5, password);
                    pstmt.setString(6, email);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Customer record inserted successfully!");
                    loadRecords(); // Refresh the list
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error inserting customer record: " + e.getMessage());
                }
            }
        } else if (selectedTable.equals("Channeling")) {
            String docId = JOptionPane.showInputDialog(frame, "Enter Doctor ID:");
            String patientName = JOptionPane.showInputDialog(frame, "Enter Patient Name:");
            String appointmentDate = JOptionPane.showInputDialog(frame, "Enter Appointment Date (YYYY-MM-DD):");

            if (docId != null && patientName != null && appointmentDate != null) {
                String query = "INSERT INTO channeldetails (doc_id, patient_name, appointment_date) VALUES (?, ?, ?)";
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, Integer.parseInt(docId));
                    pstmt.setString(2, patientName);
                    pstmt.setDate(3, Date.valueOf(appointmentDate)); // Convert String to Date
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Channeling record inserted successfully!");
                    loadRecords(); // Refresh the list
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error inserting channeling record: " + e.getMessage());
                }
            }
        }
    }

    private void updateRecord() {
        String selectedRecord = recordList.getSelectedValue();
        if (selectedRecord != null) {
            String selectedTable = tableSelection.getSelectedItem().toString();
            int id = Integer.parseInt(selectedRecord.split(":")[0]); // Extract ID from selected record

            String name = JOptionPane.showInputDialog(frame, "Enter new name:");
            String age = JOptionPane.showInputDialog(frame, "Enter new age:");
            String phone = JOptionPane.showInputDialog(frame, "Enter new phone:");
            String gender = JOptionPane.showInputDialog(frame, "Enter new gender:");
            String password = JOptionPane.showInputDialog(frame, "Enter new password:");
            String email = JOptionPane.showInputDialog(frame, "Enter new email:");

            if (selectedTable.equals("Doctors")) {
                String query = "UPDATE doctor SET name = ?, age = ?, phone = ?, gender = ?, password = ?, email = ? WHERE id = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, Integer.parseInt(age));
                    pstmt.setString(3, phone);
                    pstmt.setString(4, gender);
                    pstmt.setString(5, password);
                    pstmt.setString(6, email);
                    pstmt.setInt(7, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Record updated successfully!");
                    loadRecords(); // Refresh the list
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error updating record: " + e.getMessage());
                }
            } else {
                String query = "UPDATE customer1 SET name = ?, age = ?, phone = ?, gender = ?, password = ?, email = ? WHERE cust_id = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, Integer.parseInt(age));
                    pstmt.setString(3, phone);
                    pstmt.setString(4, gender);
                    pstmt.setString(5, password);
                    pstmt.setString(6, email);
                    pstmt.setInt(7, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Record updated successfully!");
                    loadRecords(); // Refresh the list
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error updating record: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a record to update.");
        }
    }


    private void deleteRecord() {
        String selectedTable = tableSelection.getSelectedItem().toString();
        String selectedRecord = recordList.getSelectedValue();

        if (selectedRecord != null) {
            int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                String id = selectedRecord.split(":")[0];

                String query = selectedTable.equals("Doctors") 
                        ? "DELETE FROM doctor WHERE id=?" 
                        : selectedTable.equals("Customers") 
                        ? "DELETE FROM customer1 WHERE cust_id=?" 
                        : "DELETE FROM channeldetails WHERE channel_no=?"; // Use correct table name

                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, Integer.parseInt(id));
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Record deleted successfully!");
                    loadRecords();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error deleting record: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a record to delete.");
        }
    }

    private void selectRecord() {
        String selectedRecord = recordList.getSelectedValue();
        if (selectedRecord != null) {
            JOptionPane.showMessageDialog(frame, "You selected: " + selectedRecord);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a record.");
        }
    }
}
