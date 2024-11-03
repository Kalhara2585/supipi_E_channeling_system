package my_project;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.util.Properties;

public class Channel_Registration {

    private String doctorId;  // Store the selected doctor's ID
    private String userName;   // Store the username of the customer

    // Constructor to initialize and display the GUI
    public Channel_Registration(String doctorId, String userName) {
        this.doctorId = doctorId;
        this.userName = userName;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Channel Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 690);
        frame.setLayout(new BorderLayout());

        // Create a custom background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\KALHARA\\Desktop\\project folder/p.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem backMenuItem = new JMenuItem("Back");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        backMenuItem.addActionListener(e -> {
            new doc_cust_login();
            frame.dispose();
        });

        exitMenuItem.addActionListener(e -> System.exit(0));

        fileMenu.add(backMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        // Home menu
        JMenu homeMenu = new JMenu("Home");
        menuBar.add(homeMenu);

        // Register menu
        JMenu registerMenu = new JMenu("Register");
        menuBar.add(registerMenu);

        // Login menu
        JMenu loginMenu = new JMenu("Login");
        loginMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                new doc_cust_login();
                frame.dispose();
            }

            @Override
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });
        menuBar.add(loginMenu);

        // View menu with dark and light mode
        JMenu viewMenu = new JMenu("View");
        JMenuItem darkMode = new JMenuItem("Enable Dark Mode");
        JMenuItem lightMode = new JMenuItem("Enable Light Mode");

        darkMode.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Dark Mode activated!", "View", JOptionPane.INFORMATION_MESSAGE));
        lightMode.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Light Mode activated!", "View", JOptionPane.INFORMATION_MESSAGE));

        viewMenu.add(darkMode);
        viewMenu.add(lightMode);
        menuBar.add(viewMenu);

        // Help menu with About item
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "This is a login application developed by Kalhara.", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Channel number label and text field (auto-generated)
        JLabel channelLabel = new JLabel("Your channel number:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(channelLabel, gbc);

        // Generate and display the channel number
        int channelNumber = 0;
        try {
            channelNumber = generateChannelNumber();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        JTextField channelField = new JTextField(String.valueOf(channelNumber));
        channelField.setEditable(false); // Make it read-only
        gbc.gridx = 1;
        backgroundPanel.add(channelField, gbc);

        // Date picker setup
        JLabel dateLabel = new JLabel("Select date:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(dateLabel, gbc);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        gbc.gridx = 1;
        backgroundPanel.add(datePicker, gbc);

        // Patient name input with enhancements
        JLabel patientNameLabel = new JLabel("Patient Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(patientNameLabel, gbc);

        JTextField patientNameField = new JTextField(15);
        patientNameField.setToolTipText("Enter patient's name"); // Placeholder tooltip
        gbc.gridx = 1;
        backgroundPanel.add(patientNameField, gbc);

        // Add a key listener for validation
        patientNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent username) {
                if (!Character.isLetter(username.getKeyChar()) && !Character.isWhitespace(username.getKeyChar())) {
                    username.consume(); // Ignore invalid input
                }
            }
        });

        // Submit button
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String channelNumberStr = channelField.getText();
                Object selectedDate = datePicker.getModel().getValue();
                String patientName = patientNameField.getText();

                if (patientName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a patient name.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Insert the data into the database
                    insertChannelDetails(Integer.parseInt(channelNumberStr), patientName, selectedDate);
                    JOptionPane.showMessageDialog(frame, "Channel: " + channelNumberStr + "\nDate: " + selectedDate + "\nPatient: " + patientName, "Your Channeling Success!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        backgroundPanel.add(submitButton, gbc);

        // Add background panel to the frame
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Show the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        
    }

    private int generateChannelNumber() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:4306/supipi"; // Your database URL
        String dbUsername = "root"; // Your MySQL username
        String dbPassword = ""; // Your MySQL password

        int nextChannelNumber = 1; // Default starting value

        // Connect to the database and retrieve the current max channel number
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String query = "SELECT MAX(channel_no) AS max_channel FROM channeldetails";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int maxChannelNumber = rs.getInt("max_channel");
                    nextChannelNumber = maxChannelNumber + 1; // Increment the maximum channel number
                }
            }
        }

        return nextChannelNumber;
    }

    private void insertChannelDetails(int channelNo, String userName, Object date) {
        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:4306/supipi"; // Update your database URL
        String dbUsername = "root"; // Update your MySQL username
        String dbPassword = ""; // Update your MySQL password

        // Insert the channel details into the database
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String query = "INSERT INTO channeldetails (channel_no, doc_id, patient_name, appointment_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, channelNo);
                stmt.setString(2, doctorId);
                stmt.setString(3, userName);
                stmt.setObject(4, date);
                // Store the doctor ID
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom formatter for the date picker
    class DateLabelFormatter extends javax.swing.JFormattedTextField.AbstractFormatter {
        private java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    // Custom JPanel to display a background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
