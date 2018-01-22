package LifeSimulator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;

public class EditMenu extends JDialog {
    private JTabbedPane pane;
    private SpeciesListMenu sem;
    private AddSpeciesMenu asm;
    private ArrayList<Species> toAdd;

    public EditMenu() {
        toAdd = new ArrayList<>();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pane = new JTabbedPane();
        sem = new SpeciesListMenu();
        asm = new AddSpeciesMenu();
        pane.addTab("Species List", null, new JScrollPane(sem), "Edit a species");
        pane.addTab("Add Species", null, new JScrollPane(asm), "Add a species");
        add(pane);
        setPreferredSize(new Dimension(400, 300));
        pack();
    }

    public void updateSpecies() {
        sem.updateSpecies();
    }

    public void onSpeciesAdded(Species S) {
        ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(S);
        Utility.incrementGlobalCounter("Species");
    }

    class AddSpeciesMenu extends JPanel {
    	private final Color[] colours={Color.cyan,Color.magenta,Color.orange,Color.pink,Color.red,Color.white};
    	private final String[] colourNames={"Cyan","Magenta","Orange","Pink","Red","White"};
        protected JCheckBox ePlant, eAnimal;
        protected JComboBox<String>color;
        protected JTextField name, ad, de, mn, mx;
        protected JButton confirm;

        public AddSpeciesMenu() {
            JPanel pane = new JPanel();
            pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            name = new JTextField(10);
            ePlant = new JCheckBox();
            eAnimal = new JCheckBox();
            ad = new JTextField(10);
            color = new JComboBox(colourNames);
            //ad.setInputVerifier(new NumericVerifier());
            de = new JTextField(10);
            //de.setInputVerifier(new NumericVerifier());
            mn = new JTextField(10);
            //mn.setInputVerifier(new NumericVerifier());
            mx = new JTextField(10);
            //mx.setInputVerifier(new NumericVerifier());
            confirm = new JButton("Add");
            confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(new Species(name.getText(), colours[color.getSelectedIndex()], Integer.parseInt(ad.getText()), Integer.parseInt(de.getText()), ePlant.isSelected(), eAnimal.isSelected(), Utility.getGlobalCounter("Species"), Integer.parseInt(mn.getText()), Integer.parseInt(mx.getText()), 2.0));
                    clear();
                    Utility.incrementGlobalCounter("Species");
                    updateSpecies();
                }
            });
            KeyAdapter kl = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent keyEveent) {
                    confirm.setEnabled(isValidInput());
                }

                private boolean isValidInput(){
                    if(name.getText().isEmpty())
                        return false;
                    if(ad.getText().isEmpty())
                        return false;
                    if(de.getText().isEmpty())
                        return false;
                    if(mn.getText().isEmpty())
                        return false;
                    if(mx.getText().isEmpty())
                        return false;
                    return true;
                }
            };
            name.addKeyListener(kl);
            ad.addKeyListener(kl);
            de.addKeyListener(kl);
            mn.addKeyListener(kl);
            mx.addKeyListener(kl);
            confirm.setEnabled(false);
            pane.add(new JLabel("Name: "));
            pane.add(name);
            pane.add(new JLabel("Colour: "));
            pane.add(color);
            pane.add(new JLabel("Eats Plants: "));
            pane.add(ePlant);
            pane.add(new JLabel("Eats Animals: "));
            pane.add(eAnimal);
            pane.add(new JLabel("Adult Age: "));
            pane.add(ad);
            pane.add(new JLabel("Death Age: "));
            pane.add(de);
            pane.add(new JLabel("Minimum Size: "));
            pane.add(mn);
            pane.add(new JLabel("Maximum Size: "));
            pane.add(mx);
            pane.add(confirm);
            add(pane);
        }
        private void clear(){
            name.setText("");
            ePlant.setSelected(false);
            eAnimal.setSelected(false);
            ad.setText("");
            de.setText("");
            mn.setText("");
            mx.setText("");
            confirm.setEnabled(false);
        }
    }

    class SpeciesListMenu extends JPanel implements MenuUpdateListener {
        private List<SpeciesWrapper> wrappers;

        public SpeciesListMenu() {
            wrappers = new ArrayList<>();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        protected void updateSpecies() {
            for (SpeciesWrapper sw : wrappers)
                remove(sw);
            wrappers.clear();
            for (Species S : (ArrayList<Species>) Utility.getGlobalObject("Species")) {
                SpeciesWrapper sw = new SpeciesWrapper(S);
                wrappers.add(sw);
                sw.addMouseListener(new MouseAdapter() {
                    Border blueBorder = BorderFactory.createLineBorder(Color.BLUE);
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        Utility.modifyGlobalObject("To Insert", S);
                        if(Utility.getGlobalObject("Highlighted SW")!=null)
                            ((EditMenu.SpeciesWrapper)Utility.getGlobalObject("Highlighted SW")).setBorder(BorderFactory.createEmptyBorder());
                        Utility.modifyGlobalObject("Highlighted SW",sw);
                        sw.setBorder(blueBorder);
                    }
                });
                add(sw);
            }
            repaint();
        }

        protected void append(Species S) {
            ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(S);
            SpeciesWrapper sw = new SpeciesWrapper(S);
            wrappers.add(sw);
            add(sw);
        }

        @Override
        public void onMenuUpdate() {
            updateSpecies();
        }
    }

    class SpeciesListMenu extends JPanel implements MenuUpdateListener {
        private List<SpeciesWrapper> wrappers;

        public SpeciesListMenu() {
            wrappers = new ArrayList<>();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        protected void updateSpecies() {
            for (SpeciesWrapper sw : wrappers)
                remove(sw);
            wrappers.clear();
            for (Species S : (ArrayList<Species>) Utility.getGlobalObject("Species")) {
                SpeciesWrapper sw = new SpeciesWrapper(S);
                wrappers.add(sw);
                sw.addMouseListener(new MouseAdapter() {
                    Border blueBorder = BorderFactory.createLineBorder(Color.BLUE);
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        Utility.modifyGlobalObject("To Insert", S);
                        if(Utility.getGlobalObject("Highlighted SW")!=null)
                            ((EditMenu.SpeciesWrapper)Utility.getGlobalObject("Highlighted SW")).setBorder(BorderFactory.createEmptyBorder());
                        Utility.modifyGlobalObject("Highlighted SW",sw);
                        sw.setBorder(blueBorder);
                    }
                });
                add(sw);
            }
            repaint();
        }

        protected void append(Species S) {
            ((ArrayList<Species>) Utility.getGlobalObject("Species")).add(S);
            SpeciesWrapper sw = new SpeciesWrapper(S);
            wrappers.add(sw);
            add(sw);
        }

        @Override
        public void onMenuUpdate() {
            updateSpecies();
        }
    }

    class SpeciesWrapper extends JPanel {
        protected Species S;
        protected ImageIcon col;
        protected JLabel colDisplay;
        protected JCheckBox ePlant, eAnimal;
        protected JTextField name, ad, de;

        public SpeciesWrapper(Species sp) {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            S = sp;
            name = new JTextField(S.name, 10);
            name.setEditable(false);
            col = new ImageIcon(Utility.getColouredSquare(sp.c, 10));
            ePlant = new JCheckBox();
            ePlant.setSelected(sp.eatPlants);
            ePlant.setEnabled(false);
            eAnimal = new JCheckBox();
            eAnimal.setSelected(sp.eatAnimals);
            eAnimal.setEnabled(false);
            colDisplay = new JLabel("", col, JLabel.CENTER);
            ad = new JTextField(String.valueOf(S.adultAge), 10);
            ad.setEditable(false);
            de = new JTextField(String.valueOf(S.deathAge), 10);
            de.setEditable(false);

            add(new JLabel("Name: "));
            add(name);
            add(new JLabel("Colour: "));
            add(colDisplay);
            add(new JLabel("Eats Plants: "));
            add(ePlant);
            add(new JLabel("Eats Animals: "));
            add(eAnimal);
            add(new JLabel("Adult Age: "));
            add(ad);
            add(new JLabel("Death Age: "));
            add(de);

        }
    }
}
class NumericVerifier extends InputVerifier {
    public boolean verify(JComponent input){
        JTextField tf = (JTextField) input;
        if(tf.getText().isEmpty()) return true;
        try{
            Integer.parseInt(tf.getText());
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }
}