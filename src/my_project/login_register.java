package my_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class login_register extends JFrame {

    private static final long serialVersionUID = -3593180934887939297L;
    JFrame loginterface = new JFrame();
    JLabel background = new JLabel(new ImageIcon("C:\\Users\\KALHARA\\Desktop\\project folder/loginregister.png"));

    JButton btnlogin = new JButton("Login");
    JButton btnregister = new JButton("Register");

    JPanel jmain = new JPanel(null);  // Set layout manager to null for custom layout

    // Create menu bar components
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    JMenu menuHome = new JMenu("Home");
    JMenu menuRegister = new JMenu("Register");
    JMenu menuLogin = new JMenu("Login");
    JMenu menuView = new JMenu("View");
    JMenu menuHelp = new JMenu("Help");

    // File Menu Items
    JMenuItem itemBack = new JMenuItem("Back");
    JMenuItem itemExit = new JMenuItem("Exit");
    JMenuItem itemAdmin = new JMenuItem("Admin"); // New Admin Menu Item

    // View Menu Items
    JMenuItem itemFullscreen = new JMenuItem("Toggle Fullscreen");

    // Help Menu Item
    JMenuItem itemAbout = new JMenuItem("About");

    public login_register() {
        // Set frame properties
        loginterface.setTitle("Supipi E Channeling");
        loginterface.setSize(1430, 690);
        loginterface.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginterface.setLocationRelativeTo(null);  // Center window
        loginterface.setLayout(null);
        loginterface.setResizable(false);

        // Set jmain panel properties
        jmain.setSize(1430, 690);
        jmain.setVisible(true);
        jmain.setLayout(null);

        // Set buttons properties and positions
        btnlogin.setSize(200, 45);
        btnlogin.setLocation(1075, 300);
        jmain.add(btnlogin);  // Add login button to jmain panel

        btnregister.setSize(200, 45);
        btnregister.setLocation(1075, 400);
        jmain.add(btnregister);  // Add register button to jmain panel

        // Set background label bounds and add it to jmain
        background.setBounds(0, 0, 1460, 750);
        jmain.add(background);  // Add the background image as the last element

        // Add jmain to the main frame
        loginterface.add(jmain);

        // Configure the menu bar
        setupMenuBar();

        loginterface.setVisible(true);  // Display the main frame

        // Register button action listener
        btnregister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new customer_doctor_selection_view();  // Open customer-doctor selection view
                loginterface.dispose();  // Close the current window
            }
        });

        // Login button action listener
        btnlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new doc_cust_login(); // Open login page
                loginterface.dispose();  // Close the current window
            }
        });
    }

    // Method to set up the menu bar
    private void setupMenuBar() {
        // Add items to the File menu
        menuFile.add(itemBack);
        menuFile.add(itemExit);
        menuFile.add(itemAdmin); // Add the Admin menu item

        // Add items to the View menu
        menuView.add(itemFullscreen);

        // Add item to the Help menu
        menuHelp.add(itemAbout);

        // Add menus to the menu bar
        menuBar.add(menuFile);
        menuBar.add(menuHome);
        menuBar.add(menuRegister);
        menuBar.add(menuLogin);
        menuBar.add(menuView);
        menuBar.add(menuHelp);

        // Set the menu bar to the frame
        loginterface.setJMenuBar(menuBar);

        // Action listener for "Back" menu item
        itemBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new login_register();  // Go back to the main view
                loginterface.dispose();  // Close the current window
            }
        });

        // Action listener for "Exit" menu item
        itemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Exit the application
            }
        });

        // Action listener for Admin menu item
        itemAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform action when Admin is clicked, e.g., open an admin interface
                new login_Admin();  // Assuming admin_view is another JFrame for admin actions
                loginterface.dispose();  // Close current window
            }
        });

        // Action listener for Home menu
        menuHome.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new login_register();  // Open home view
                loginterface.dispose();  // Close current window
            }
            public void menuDeselected(MenuEvent e) {}
            public void menuCanceled(MenuEvent e) {}
        });

        // Action listener for Register menu
        menuRegister.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new customer_doctor_selection_view();  // Open register view
                loginterface.dispose();  // Close current window
            }
            public void menuDeselected(MenuEvent e) {}
            public void menuCanceled(MenuEvent e) {}
        });

        // Action listener for Login menu
        menuLogin.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                new doc_cust_login();  // Open login view
                loginterface.dispose();  // Close current window
            }
            public void menuDeselected(MenuEvent e) {}
            public void menuCanceled(MenuEvent e) {}
        });

        // Action listener for View menu item
        itemFullscreen.addActionListener(e -> toggleFullscreen());

        // Action listener for About menu item
        itemAbout.addActionListener(e -> JOptionPane.showMessageDialog(loginterface, "Supipi E Channeling v1.0\nDeveloped by Kalhara."));
    }

    // Method to toggle fullscreen mode
    private void toggleFullscreen() {
        if (loginterface.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            loginterface.setExtendedState(JFrame.NORMAL);  // Exit fullscreen
        } else {
            loginterface.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Enter fullscreen
        }
    }
}
