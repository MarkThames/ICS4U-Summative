package LifeSimulator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// The menu that displays the edit options
public class EditMenu extends JDialog {
    // JTabbedPane for displaying tabs
    private JTabbedPane pane;
    // The following 3 variables all extend JPanel
    // They are used to display the information
    // And are added to the JTabbedPane
    private SpeciesListMenu slm;
    private AddSpeciesMenu asm;
    private SpeciesEditMenu sem;

    // Constructor
    public EditMenu() {
        // DISPOSE_ON_CLOSE so previously entered information is not retained
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        // Initialize all three tabs
        pane = new JTabbedPane();
        slm = new SpeciesListMenu();
        asm = new AddSpeciesMenu();
        sem = new SpeciesEditMenu();
        // Add to the JTabbedPane
        pane.addTab("Species List", null, new JScrollPane(slm), "List all species");
        pane.addTab("Add Species", null, new JScrollPane(asm), "Add a species");
        pane.addTab("Edit Species", null, new JScrollPane(sem), "Edit a species");
        // Add the the Dialog
        add(pane);
        setPreferredSize(new Dimension(400, 300));
        pack();
    }

    // Here just for convenience
    // Updates the species lists
    public void updateSpecies() {
        slm.updateSpecies();
        sem.updateSpecies();
    }

    // Allows for the addition of a brand-new species to the map
    class AddSpeciesMenu extends JPanel {
        // These are used to display the colours in a computer-readable and human-readable format, respectively
        private final Color[] colours={Color.cyan,Color.magenta,Color.orange,Color.pink,Color.red,Color.white};
    	private final String[] colourNames={"Cyan","Magenta","Orange","Pink","Red","White"};
        // Checkboxes so the use can input whether animal eats plants, animals, or both
        protected JCheckBox ePlant, eAnimal;
        // JComboBox to display colours, decided not to use JColourChooser since certain colours (e.g. blue)
        // would conflict with the map
        protected JComboBox<String>color;
        // JComboBox to give options for size and strength
        protected JComboBox size, strength;
        // JTextFields to enter name
        protected JTextField name;
        // JButton to confirm choices
        protected JButton confirm;

        // Constructor
        public AddSpeciesMenu() {
            JPanel pane = new JPanel();
            // Set the height to something that looks nice
            pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            // Set the width to 10 characters
            name = new JTextField(10);
            // Initalize checkboxes, text fields, and combo box
            ePlant = new JCheckBox();
            eAnimal = new JCheckBox();
            String[] sizeOptions={"Small", "Medium", "Large"};
            size=new JComboBox(sizeOptions);
            color = new JComboBox(colourNames);
            String[] strengthOptions={"Weak", "Fair", "Strong"};
            strength=new JComboBox(strengthOptions);
            confirm = new JButton("Add");
            // Check if clicked
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    // Add a new species in
                    ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(new Species(name.getText(), colours[color.getSelectedIndex()], 50, 200, ePlant.isSelected(), eAnimal.isSelected(), Utility.getGlobalCounter("Species"), size.getSelectedIndex(), strength.getSelectedIndex()));
                    // Clear the textboxes and checkboxes
                    clear();
                    Utility.incrementGlobalCounter("Species");
                    // Update the species lists
                    updateSpecies();
                }
            });
            confirm.setEnabled(false);
            //validate input
            KeyAdapter kl = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent keyEveent) {
                    confirm.setEnabled(isValidInput());
                }

                // Check that text fields contain valid data
                private boolean isValidInput(){
                    if(name.getText().isEmpty())
                        return false;
                    return true;
                }
            };
            // Add the keylistener
            name.addKeyListener(kl);
            // Add everything to the pane, including JLabels to explain each field
            pane.add(new JLabel("Name: "));
            pane.add(name);
            pane.add(new JLabel("Colour: "));
            pane.add(color);
            pane.add(new JLabel("Eats Plants: "));
            pane.add(ePlant);
            pane.add(new JLabel("Eats Animals: "));
            pane.add(eAnimal);
            pane.add(new JLabel("Size: "));
            pane.add(size);
            pane.add(new JLabel("Strength: "));
            pane.add(strength);
            pane.add(confirm);
            add(pane);
        }

        // Helper to clear each field
        private void clear(){
            name.setText("");
            ePlant.setSelected(false);
            eAnimal.setSelected(false);
            size.setEnabled(false);
            strength.setEnabled(false);
            confirm.setEnabled(false);
        }
    }

    // To display all species
    class SpeciesListMenu extends JPanel implements MenuUpdateListener {
        // List of SpeciesWrappers, use to store them so they can later be removed from JPanel
        private List<SpeciesWrapper> wrappers;
        // To allow for different options when it comes to what each click means
        private JComboBox tool;
        // Constructor
        public SpeciesListMenu() {
            // Initalize everything
            wrappers = new ArrayList<>();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        	String[] options={"---", "Single Spawn", "Mass Spawn", "Eradicate"};
        	tool=new JComboBox(options);
        	tool.setMaximumSize(tool.getPreferredSize());
        	tool.addActionListener (new ActionListener () {
        	    public void actionPerformed(ActionEvent e) {
        	    	tool.addActionListener (new ActionListener () {
                	    public void actionPerformed(ActionEvent e) {
                	        Utility.modifyGlobalObject("mouseType", tool.getSelectedIndex());
                	    }
                	});
        	    }
        	});
        	JPanel pane=new JPanel();
        	pane.setMinimumSize(new Dimension(50000, 50));
        	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        	pane.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(pane);
            pane.add(tool);
        }

        // Update the species list
        protected void updateSpecies() {
            // firstly remove all the preexisting wrappers
            for (SpeciesWrapper sw : wrappers)
                remove(sw);
            wrappers.clear();
            // Get all species
            for (Species S : (ArrayList<Species>) Utility.getGlobalObject("Species")) {
                // Construct a GUI wrapper
                SpeciesWrapper sw = new SpeciesWrapper(S);
                // Add to list of wrappers
                wrappers.add(sw);
                // MouseListener to detect which one is clicked
                sw.addMouseListener(new MouseAdapter() {
                    Border blueBorder = BorderFactory.createLineBorder(Color.BLUE);
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        // Which species to insert
                        Utility.modifyGlobalObject("To Insert", S);
                        if(Utility.getGlobalObject("Highlighted SW")!=null)
                            // Remove the border of the old one
                            ((EditMenu.SpeciesWrapper)Utility.getGlobalObject("Highlighted SW")).setBorder(BorderFactory.createEmptyBorder());
                        // Update which SpeciesWrapper has the border
                        Utility.modifyGlobalObject("Highlighted SW",sw);
                        // Update the border
                        sw.setBorder(blueBorder);
                    }
                });
                // Display the SpeciesWrapper
                add(sw);
            }
            // Trigger a repaint
            repaint();
        }

        // To add a new Species
        protected void append(Species S) {
            // Add the global list
            ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(S);
            // Construct wrapper
            SpeciesWrapper sw = new SpeciesWrapper(S);
            // Add wrapper
            wrappers.add(sw);
            add(sw);
        }

        @Override
        public void onMenuUpdate() {
            updateSpecies();
        }
    }
    
    // Menu to allow for editing of species
    class SpeciesEditMenu extends JPanel implements MenuUpdateListener {
        private List<SpeciesWrapper> wrappers;

        // Constructor
        public SpeciesEditMenu() {
            // Initialize everything
            wrappers = new ArrayList<>();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        // Method to update the list of species
        protected void updateSpecies() {
            // Remove all the old wrappers
            for (SpeciesWrapper sw : wrappers)
                remove(sw);
            wrappers.clear();
            // Iterate all species
            for (Species S : (ArrayList<Species>) Utility.getGlobalObject("Species")) {
                // Consruct new wrapper
                SpeciesEditWrapper sw = new SpeciesEditWrapper(S);
                // Add wrapper
                wrappers.add(sw);
                add(sw);
            }
            // Trigger a repaint
            repaint();
        }

        // To add a new Species
        protected void append(Species S) {
            // Add the global list
            ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(S);
            // Construct a wrapper
            SpeciesWrapper sw = new SpeciesWrapper(S);
            // Add the wrapper
            wrappers.add(sw);
            add(sw);
        }

        @Override
        public void onMenuUpdate() {
            updateSpecies();
        }
    }

    // GUI Wrapper class for a species
    class SpeciesWrapper extends JPanel {
        // The species it backs
        protected Species S;
        // The colour of the spcies
        protected ImageIcon col;
        // A JLabel to display the colour
        protected JLabel colDisplay;
        // Whether it eats plants or animals
        protected JCheckBox ePlant, eAnimal;
        // Text fields to display textual information
        protected JTextField name;
        // JComboBoxes to display size and strength
        protected JComboBox size, strength;
        // Constructor
        public SpeciesWrapper(Species sp) {
            // This effectively constrains the maximum height so that it looks more attractive
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            S = sp;
            // Display name
            name = new JTextField(S.name, 10);
            name.setEditable(false);
            // Convert colour into an icon
            col = new ImageIcon(Utility.getColouredSquare(sp.c, 10));
            // Display dietary preferences
            ePlant = new JCheckBox();
            ePlant.setSelected(sp.eatPlants);
            ePlant.setEnabled(false);
            eAnimal = new JCheckBox();
            eAnimal.setSelected(sp.eatAnimals);
            eAnimal.setEnabled(false);
            // Display color
            colDisplay = new JLabel("", col, JLabel.CENTER);
            // Display size-related information
            String[] sizeOptions={"Small", "Medium", "Large"};
            size=new JComboBox(sizeOptions);
            String[] strengthOptions={"Weak", "Fair", "Strong"};
            strength=new JComboBox(strengthOptions);
            size.setSelectedIndex(sp.t1);
            strength.setSelectedIndex(sp.t2);
            size.setEnabled(false);
            strength.setEnabled(false);
            // Add everything to the JPanel
            add(new JLabel("Name: "));
            add(name);
            add(new JLabel("Colour: "));
            add(colDisplay);
            add(new JLabel("Eats Plants: "));
            add(ePlant);
            add(new JLabel("Eats Animals: "));
            add(eAnimal);
            add(new JLabel("Size: "));
            add(size);
            add(new JLabel("Strength: "));
            add(strength);

        }
    }
    
    // Similar to the wrapper above, just with editable fields and a confirmation button
    class SpeciesEditWrapper extends SpeciesWrapper {

        // Button to confirm choice
    	JButton confirm;
        // Constructor
        public SpeciesEditWrapper(Species sp) {
            // Most things are the same as the super class
        	super(sp);
            // Make these fields editable again
            name.setEditable(true);
            ePlant.setEnabled(true);
            eAnimal.setEnabled(true);
            size.setEnabled(true);
            strength.setEnabled(true);
            // Add a the button the confirm
            confirm = new JButton("Edit");
            // Listen for a click
            confirm.addActionListener(new ActionListener() {
                @Override
                // *clicks*
                public void actionPerformed(ActionEvent actionEvent) {
                    // Modify the old species object
                	((ArrayList<Species>) Utility.getGlobalObject("Species")).get(((ArrayList<Species>) Utility.getGlobalObject("Species")).indexOf(sp)).update(name.getText(), 50, 200, ePlant.isSelected(), eAnimal.isSelected(), size.getSelectedIndex(), strength.getSelectedIndex());
                    // Clear the text fields
                    clear();
                    // Update the two menus
                    updateSpecies();
                    // Trigger a repaint
                    repaint();
                }
            });
            // Add the confirm button, since super class doesn't have it
            add(confirm);
            // Add the input verification from AddSpeciesMenu
            KeyAdapter kl = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent keyEveent) {
                    confirm.setEnabled(isValidInput());
                }

                private boolean isValidInput(){
                    if(name.getText().isEmpty())
                        return false;
                    return true;
                }
            };
            // Add the keylistener
            name.addKeyListener(kl);
        }

        // Helper method to clear all fields
        private void clear(){
            name.setText("");
            ePlant.setSelected(false);
            eAnimal.setSelected(false);
            size.setEnabled(false);
            strength.setEnabled(false);
            confirm.setEnabled(false);
        }
    }
}
