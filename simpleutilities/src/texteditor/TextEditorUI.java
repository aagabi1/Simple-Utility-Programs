package texteditor;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TextEditorUI class constructs the user interface of the text editor application.
 * It adds all the swing elements and initializes them at the base level. The inner
 * workings of these swing elements are controlled by external, dedicated classes.
 *
 * NOTE: This class should nt do anything moe than add and style UI elements.
 * In the case that it becomes too cumbersome to handle, it would be beneficial to
 * divide the styling of related components into classes of their own.
 *
 * @see TextEditorIO
 * @see FileBuilder
 *
 * @author Courage A Agabi
 */

public class TextEditorUI {
    JFrame frame;
    JLabel copyright;
    JTextArea notePad;
    JPanel mainPanel, textPanel, actionPanel, legalPanel;

    JMenuBar menuBar;
    JMenu fileMenu, editMenu;
    JMenuItem saveItem, loadItem, exitItem, cutItem, copyItem, selectAllItem;

    JFileChooser dialog;
    TextEditorIO textIO;

    /**
     * TextEditorUI constructor takes no arguments. It simply handles the initialization
     * of UI components declared within the class. This helps avoid any
     * {@code NullPointerException} that might occur otherwise.
     */
    public TextEditorUI() {
        frame = new JFrame("SIMPLE TEXT EDITOR");

        textPanel = new JPanel();
        mainPanel = new JPanel();
        legalPanel = new JPanel();
        actionPanel = new JPanel();

        notePad = new JTextArea(20, 30);
        copyright = new JLabel("\u00a9 2019 Courage A. Agabi. \nAll Rights Reserved.");

        menuBar = new JMenuBar();
        textIO = new TextEditorIO();
        dialog = new JFileChooser(System.getProperty("user.dir"));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    }

    public static void main(String[] args) {
        new TextEditorUI().go();
    }

    /**
     * The {@code go()} method is a helper method that does the actual user interface
     * configuration. Again, if this method were to grow to an unmanageable scale,
     * it would be beneficial to split it into smaller related parts spread across
     * several lightweight classes.
     */
    public void go() {
        /**
         * Creates {@code ScrollPane} and sets its scrolling policies.
         * Attaches the {@code JTextArea} to the {@code ScrollPane}.
         */
        JScrollPane scroller = new JScrollPane(notePad);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /** Preset borders for the {@code textPanel}. */
        Border line = new LineBorder(Color.BLACK, 1);
        Border padding = new EmptyBorder(30, 10, 10, 10);

        notePad.setLineWrap(true);
        notePad.setFont(new Font (Font.SERIF, Font.PLAIN, 15));
        notePad.setBorder(BorderFactory.createCompoundBorder
                (notePad.getBorder(), new EmptyBorder(5,10,5,10)));

        textPanel.add(scroller);
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(BorderFactory.createTitledBorder(new CompoundBorder(line, padding), "ENTER TEXT"));

        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBackground(Color.WHITE);

        legalPanel.add(copyright);
        legalPanel.setBackground(Color.WHITE);
        legalPanel.setLayout(new BoxLayout(legalPanel, BoxLayout.X_AXIS));

        designUI(menuBar);

        frame.setJMenuBar(menuBar);

        mainPanel.add(textPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(actionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(legalPanel);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setSize(800, 800);
        frame.setVisible(true);

    }

    /**
     * The {@code designUI} method directly handles specific UI customizations. It takes in a {@code JComponent}
     * and tests to see if it is an instance of a particular subclass. {@code designUI} cast the argument to
     * that intended type. It is modular and can be moved to a dedicated class.
     *
     * @param component
     * @see TextEditorUI
     */
    public void designUI(JComponent component) {
        if(component instanceof JButton) {
            /** Assigns button size, font and padding. */
            ((JButton)component).setMargin(new Insets(10, 20, 10, 20));

            /**
             * The foreground and background colors.
             * FOREGROUND = Text color.
             * BACKGROUND = Button color.
             */
            component.setForeground(Color.WHITE);
            component.setBackground(new Color(35, 28, 49));

            /** Removes the button and text borders. */
            ((JButton)component).setFocusPainted(false);
            ((JButton)component).setBorderPainted(false);
        }

        if(component instanceof JMenuBar) {
            try {
                UIManager.put("Menu.selectionBackground", Color.WHITE);
                UIManager.put("MenuBar.background", Color.darkGray);
                UIManager.put("MenuItem.foreground", Color.BLACK);
                UIManager.put("MenuItem.selectionBackground", Color.WHITE);
                UIManager.put("MenuItem.borderPainted", false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            fileMenu = new JMenu("FILE");
            editMenu = new JMenu("EDIT");

            saveItem = new JMenuItem("SAVE");
            loadItem = new JMenuItem("LOAD");
            exitItem = new JMenuItem("EXIT");

            cutItem = new JMenuItem("CUT");
            copyItem = new JMenuItem("COPY");
            selectAllItem = new JMenuItem("SELECT ALL");

            /** Adds menuItems to menu instance. */
            fileMenu.add(saveItem);
            fileMenu.add(loadItem);
            fileMenu.add(exitItem);
            fileMenu.setForeground(Color.WHITE);

            editMenu.add(cutItem);
            editMenu.add(copyItem);
            editMenu.add(selectAllItem);
            editMenu.setForeground(Color.WHITE);

            /** Adds menuItem functions. */
            saveItem.addActionListener(new FileBuilder());
            loadItem.addActionListener(new FileBuilder());
            exitItem.addActionListener(new FileBuilder());

            /** Removes menu and menuItem borders. */
            fileMenu.setBorderPainted(false);
            editMenu.setBorderPainted(false);
            menuBar.setBorderPainted(false);

            menuBar.add(fileMenu);
            menuBar.add(Box.createRigidArea(new Dimension(5, 0)));
            menuBar.add(editMenu);
        }
    }

    /**
     * FileBuilder is an internal class that implements {@code ActionListener}. The use of an
     * internal class allows multiple implementations of the {@code actionPerformed} method
     * so multiple buttons, for instance, can be linked to their own listener classes without
     * naming conflicts.
     *
     * @author Courage A Agabi
     */
    class FileBuilder implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == saveItem) {
                int returnVal = dialog.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    textIO.write(dialog.getSelectedFile().getName(), notePad.getText());
                }
            }

            if(e.getSource() == loadItem) {
                int returnVal = dialog.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    String result = textIO.read(dialog.getSelectedFile());
                    notePad.setText(result);
                }
            }


        }
    }
}
