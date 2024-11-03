package my_project;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class D_Function {

    private JFrame frame;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;

    public D_Function() {
        frame = new JFrame("View Your Scheduled Channels");
        frame.setSize(1400, 690);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setupMenuBar();

        JLabel background = new JLabel(new ImageIcon("C:\\Users\\KALHARA\\Desktop\\project folder/R.jpg"));
        background.setBounds(0, 0, 1400, 690);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1400, 690));
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        setupScheduleTable();

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        layeredPane.add(scrollPane, JLayeredPane.PALETTE_LAYER);
        scrollPane.setBounds(50, 100, 1200, 500);

        frame.add(layeredPane, BorderLayout.CENTER);
        
        frame.setVisible(true);

        loadDoctorSchedule();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        
        JMenuItem backMenuItem = new JMenuItem("Back");
        backMenuItem.addActionListener(e -> {
            new doc_cust_login();
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
            @Override public void menuDeselected(MenuEvent e) {}
            @Override public void menuCanceled(MenuEvent e) {}
        });

        JMenu registerMenu = new JMenu("Register");
        registerMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new customer_doctor_selection_view();
                frame.dispose();
            }
            @Override public void menuDeselected(MenuEvent e) {}
            @Override public void menuCanceled(MenuEvent e) {}
        });

        JMenu loginMenu = new JMenu("Login");
        loginMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new doc_cust_login();
                frame.dispose();
            }
            @Override public void menuDeselected(MenuEvent e) {}
            @Override public void menuCanceled(MenuEvent e) {}
        });

        JMenu viewMenu = new JMenu("View");
        viewMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                JOptionPane.showMessageDialog(frame, "Viewing Details");
            }
            @Override public void menuDeselected(MenuEvent e) {}
            @Override public void menuCanceled(MenuEvent e) {}
        });

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(frame, "Doctor System v1.0 - For assistance, contact support."));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(homeMenu);
        menuBar.add(registerMenu);
        menuBar.add(loginMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
    }

    private void setupScheduleTable() {
        String[] columns = {"Channel ID", "Doctor ID", "Appointment Date", "Patient Name"};
        tableModel = new DefaultTableModel(columns, 0);
        scheduleTable = new JTable(tableModel);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scheduleTable.setRowHeight(25);
        scheduleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void loadDoctorSchedule() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to MySQL database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:4306/supipi", "root", "");

            // Query to select channel details including doc_id
            String sql = "SELECT channel_no, doc_id, appointment_date, patient_name FROM channeldetails";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Add data to table model
            while (rs.next()) {
                int channelId = rs.getInt("channel_no");
                int doctorId = rs.getInt("doc_id"); // Fetching doctor ID
                Date channelDate = rs.getDate("appointment_date");
                String patientName = rs.getString("patient_name");

                // Add row to the table model
                tableModel.addRow(new Object[]{channelId, doctorId, channelDate, patientName});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading schedule: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }
}
