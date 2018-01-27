package LifeSimulator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class Help extends JFrame{
	public Help(){
		//display message
		Text text=new Text();
		setContentPane (text);
        pack ();
        setTitle ("Help");
        setSize (500, 350);
        setMaximumSize(new Dimension(500,350));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	private class Text extends JPanel{
		public Text(){
			setSize(500, 350);
		}
		public void paintComponent(Graphics g){
			//display message
			g.setColor(Color.black);
			g.setFont(new Font("Calibri", Font.BOLD, 18));
			g.drawString("Use the mouse-wheel to zoom in and out of the map.", 20, 20);
			g.drawString("You can pan by dragging with the left mouse button.", 20, 50);
			g.drawString("Click the Data button to see the population graphs.", 20, 80);
			g.drawString("Check out the Edit menu!", 20, 110);
			g.drawString("When spawning, right-click on the map to spawn a single", 20, 140);
			g.drawString("animal of that species. When mass-spawning, drag an area", 20, 160);
			g.drawString("with the right mouse button, and twenty animals of that", 20, 180);
			g.drawString("species will spawn. When eradicating, drag an area with the", 20, 200);
			g.drawString("right mouse button and the majority of animals of that", 20, 220);
			g.drawString("species will die. Note that when selecting these options in", 20, 240);
			g.drawString("the Edit menu, there is occasionally a bug and you will have", 20, 260);
			g.drawString("to reselect in the combo box and reselect the species if it", 20, 280);
			g.drawString("is not working.", 20, 300);
		}
	}
}
