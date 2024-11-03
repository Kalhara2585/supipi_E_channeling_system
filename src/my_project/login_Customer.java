package my_project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;

public class login_Customer {
    JFrame login = new JFrame();
    JLabel userNameLabel = new JLabel("User Name:");
    JTextField userNameField = new JTextField();
    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JPanel panel = new JPanel();

    // Database connection details
    String dbUrl = "jdbc:mysql://localhost:4306/supipi";  // replace with your DB URL
    String dbUsername = "root";  // replace with your DB username
    String dbPassword = "";  // replace with your DB password

    public login_Customer() {
        login.setTitle("Log Your Account");
        login.setResizable(false);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);
        panel.setBounds(0, 0, 1400, 690);
        panel.setOpaque(false);

        JLabel background = new JLabel(new ImageIcon("C:\\Users\\KALHARA\\Desktop\\project folder/login.png"));
        background.setBounds(0, 0, 1430, 690);

        userNameLabel.setBounds(660, 190, 150, 30);
        userNameLabel.setForeground(Color.WHITE);
        panel.add(userNameLabel);

        userNameField.setBounds(570, 220, 250, 30);
        userNameField.setForeground(Color.WHITE);
        userNameField.setBackground(new Color(0, 0, 0, 150));
        panel.add(userNameField);

        passwordLabel.setBounds(660, 260, 150, 30);
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);

        passwordField.setBounds(570, 290, 250, 30);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(new Color(0, 0, 0, 150));
        panel.add(passwordField);

        loginButton.setBounds(590, 370, 200, 50);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String password = new String(passwordField.getPassword());
                if (checkCredentials(userName, password)) {
                    new Select_Doctor_View(userName);
                    login.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Adding a menu bar with the modified menu structure
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem backMenuItem = new JMenuItem("Back");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(backMenuItem);
        fileMenu.add(exitMenuItem);

        backMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new doc_cust_login();
                login.dispose();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Home menu
        JMenu homeMenu = new JMenu("Home");
        homeMenu.addMenuListener(new MenuListenerAdapter() {
            @Override
            public void menuSelected(MenuEvent e) {
                new login_register();
                login.dispose();
            }
        });

        // Register menu
        JMenu registerMenu = new JMenu("Register");
        registerMenu.addMenuListener(new MenuListenerAdapter() {
            @Override
            public void menuSelected(MenuEvent e) {
                new customer_doctor_selection_view();
                login.dispose();
            }
        });

        // Login menu
        JMenu loginMenu = new JMenu("Login");
        loginMenu.addMenuListener(new MenuListenerAdapter() {
            @Override
            public void menuSelected(MenuEvent e) {
                new doc_cust_login();
                login.dispose();
            }
        });

        // View menu with dark and light mode
        JMenu viewMenu = new JMenu("View");
        JMenuItem darkMode = new JMenuItem("Enable Dark Mode");
        JMenuItem lightMode = new JMenuItem("Enable Light Mode");
        viewMenu.add(darkMode);
        viewMenu.add(lightMode);

        darkMode.addActionListener(e -> JOptionPane.showMessageDialog(login, "Dark Mode activated!", "View", JOptionPane.INFORMATION_MESSAGE));
        lightMode.addActionListener(e -> JOptionPane.showMessageDialog(login, "Light Mode activated!", "View", JOptionPane.INFORMATION_MESSAGE));

        // Help menu with About item
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(login, "This is a login application developed by My Project.", "About", JOptionPane.INFORMATION_MESSAGE));

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(homeMenu);
        menuBar.add(registerMenu);
        menuBar.add(loginMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // Set the menu bar to the JFrame
        login.setJMenuBar(menuBar);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1430, 690);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(panel, JLayeredPane.PALETTE_LAYER);

        login.add(layeredPane);
        login.setSize(1430, 690);
        login.setVisible(true);
    }

    private boolean checkCredentials(String userName, String password) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String query = "SELECT * FROM customer1 WHERE name = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userName);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                return rs.next();  // Returns true if a matching record is found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Adapter for MenuListener to handle empty methods
    public abstract class MenuListenerAdapter implements javax.swing.event.MenuListener {
        public void menuSelected(javax.swing.event.MenuEvent e) {}
        public void menuDeselected(javax.swing.event.MenuEvent e) {}
        public void menuCanceled(javax.swing.event.MenuEvent e) {}
    }
}
