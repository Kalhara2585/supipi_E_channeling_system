package my_project;

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

public class customer_doctor_selection_view {

    JFrame customer_or_doctor = new JFrame();
    JLabel background = new JLabel(new ImageIcon("C:\\Users\\KALHARA\\Desktop\\project folder/loginregister.png"));

    JButton patientButton = new JButton("Patient");
    JButton doctorButton = new JButton("Doctor");

    public customer_doctor_selection_view() {
        // Set up the frame
        customer_or_doctor.setTitle("Register");
        customer_or_doctor.setSize(1430, 690);
        customer_or_doctor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customer_or_doctor.setLayout(null);
        customer_or_doctor.setResizable(false);

        // Set up the background label
        background.setBounds(0, 0, 1430, 690);

        // Create a panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 1430, 690);
        buttonPanel.setOpaque(false);

        // Position and size the buttons
        doctorButton.setBounds(1100, 250, 150, 50);
        patientButton.setBounds(1100, 350, 150, 50);

        // Add buttons to the panel
        buttonPanel.add(doctorButton);
        buttonPanel.add(patientButton);

        // Create a JLayeredPane to layer the background and buttons
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1430, 690);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);
        customer_or_doctor.add(layeredPane);

        // Add menu bar to the frame
        customer_or_doctor.setJMenuBar(createMenuBar());

        // Set up button actions
        doctorButton.addActionListener(e -> {
            new Doctor_Register();
            customer_or_doctor.dispose();
        });

        patientButton.addActionListener(e -> {
            new CustomerRegister();
            customer_or_doctor.dispose();
        });

        // Set frame visibility
        customer_or_doctor.setVisible(true);
    }

    // Method to create a menu bar with File, Home, Register, Login, View, Help, and About
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu with Back and Exit options
        JMenu fileMenu = new JMenu("File");
        JMenuItem backMenuItem = new JMenuItem("Back");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        backMenuItem.addActionListener(e -> {
            new login_register();
            customer_or_doctor.dispose();
        });
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(backMenuItem);
        fileMenu.add(exitMenuItem);

        // Home menu
        JMenu homeMenu = new JMenu("Home");
        homeMenu.addMenuListener(new MenuListenerAdapter() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                new login_register();
                customer_or_doctor.dispose();
            }
        });

        // Register menu
        JMenu registerMenu = new JMenu("Register");
        registerMenu.addMenuListener(new MenuListenerAdapter() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                new customer_doctor_selection_view();
                customer_or_doctor.dispose();
            }
        });

        // Login menu
        JMenu loginMenu = new JMenu("Login");
        loginMenu.addMenuListener(new MenuListenerAdapter() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                new doc_cust_login();
                customer_or_doctor.dispose();
            }
        });

        // View menu (placeholder for future features)
        JMenu viewMenu = new JMenu("View");

        // Help menu with About
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(customer_or_doctor,
                "Customer-Doctor Selection App v1.0\nDeveloped by Kalhara.",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(homeMenu);
        menuBar.add(registerMenu);
        menuBar.add(loginMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        new customer_doctor_selection_view();
    }

    // Adapter for MenuListener to handle empty methods
    private abstract class MenuListenerAdapter implements javax.swing.event.MenuListener {
        public void menuSelected(javax.swing.event.MenuEvent e) {}
        public void menuDeselected(javax.swing.event.MenuEvent e) {}
        public void menuCanceled(javax.swing.event.MenuEvent e) {}
    }
}
