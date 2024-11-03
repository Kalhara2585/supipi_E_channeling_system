package my_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;

public class Select_Doctor_View {
    private String userName;
    private JFrame frame;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JButton selectButton;
    private JButton loadButton;

    private final static String DB_URL = "jdbc:mysql://localhost:4306/supipi"; // Database URL
    private final String USER = "root"; // MySQL username
    private final String PASS = ""; // MySQL password

    public Select_Doctor_View(String userName) {
        this.userName = userName;
        frame = new JFrame("Select Doctor");
        frame.setSize(1400, 690);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create MenuBar
        setupMenuBar();

        // JLayeredPane for background and components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1430, 690));

        JLabel background = new JLabel(new ImageIcon("C:\\Users\\KALHARA\\Desktop\\project folder/R.jpg"));
        background.setBounds(0, 0, 1400, 690);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // Table model for displaying doctor data
        String[] columnNames = {"ID", "Name", "Job", "Gender"};
        tableModel = new DefaultTableModel(columnNames, 0);
        doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBounds(450, 100, 500, 400);
        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);

        loadButton = new JButton("Load Doctors");
        loadButton.setBounds(600, 520, 200, 30);
        loadButton.addActionListener(e -> loadDoctors());
        layeredPane.add(loadButton, JLayeredPane.PALETTE_LAYER);

        selectButton = new JButton("Select Doctor");
        selectButton.setBounds(600, 560, 200, 30);
        selectButton.addActionListener(e -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow != -1) {
                String doctorId = tableModel.getValueAt(selectedRow, 0).toString(); // Get the ID of the selected doctor
                String doctorName = tableModel.getValueAt(selectedRow, 1).toString(); // Get the name
                JOptionPane.showMessageDialog(frame, "You selected: " + doctorName);
                new Channel_Registration(doctorId, userName); // Pass the doctorId to Channel_Registration
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a doctor.");
            }
        });
        layeredPane.add(selectButton, JLayeredPane.PALETTE_LAYER);

        frame.add(layeredPane);
        frame.setVisible(true);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu with Back and Exit
        JMenu fileMenu = new JMenu("File");
        JMenuItem backMenuItem = new JMenuItem("Back");
        backMenuItem.addActionListener(e -> {
            new doc_cust_login(); // Navigate to previous screen
            frame.dispose();
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(backMenuItem);
        fileMenu.add(exitMenuItem);

        // Home menu
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

        // Register menu
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

        // Login menu
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

        // Help menu with About
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Doctor Selection System v1.0"));
        helpMenu.add(aboutItem);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(homeMenu);
        menuBar.add(registerMenu);
        menuBar.add(loginMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
    }

    private void loadDoctors() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure this driver is in your classpath
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading MySQL driver: " + e.getMessage());
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, job, gender FROM doctor")) {

            // Clear existing entries in the table model
            tableModel.setRowCount(0);

            // Fetch doctor details and add them to the table model
            while (rs.next()) {
                String docId = rs.getString("id");
                String name = rs.getString("name");
                String job = rs.getString("job");
                String gender = rs.getString("gender");

                // Add row to the table
                tableModel.addRow(new Object[]{docId, name, job, gender});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading doctors: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Select_Doctor_View("SampleUser"); // Pass a sample username for testing
    }
}
