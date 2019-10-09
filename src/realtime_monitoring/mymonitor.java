package realtime_monitoring;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


import testcases.mycase2;
import testcases.mycase3;
import testcases.mycase4;
import testcases.mycase5;
import testcases.mycase6;
import testcases.mycase1;

public class mymonitor {

	
    // Definition of global values and items that are part of the GUI.
	
	static JDesktopPane desktop;
	static  JFrame frame ;
     
   // This is the new ActionPerformed Method.
   // It catches any events with an ActionListener attached.
   // Using an if statement, we can determine which button was pressed
   // and change the appropriate values in our GUI.
   
     
    private static void createAndShowGUI() throws Exception {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame.setDefaultLookAndFeelDecorated(true);
         frame = new JFrame("HEALTCARE SYSTEM");
         desktop = new JDesktopPane();
         desktop.setBackground(Color.green);
         desktop.setPreferredSize(new Dimension(screenSize.width, 225*6));
         JScrollPane jsp0 = new JScrollPane(desktop,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 	    frame.add(jsp0);
 	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setLocation(0, 0);
         frame.setSize(screenSize.width, 225*6);
         frame.setVisible(true);
         
        
         
         JInternalFrame internalFrame = new JInternalFrame("case 01", true, true, true, true);
         mycase1 demo1 = new mycase1();
         JScrollPane jsp = new JScrollPane(demo1.createContentPane2("case 01"),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         internalFrame.add(jsp);
         internalFrame.setLocation(0, 22);
         internalFrame.setSize(screenSize.width-20, 200);
         internalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         internalFrame.setIcon(true);
         internalFrame.setVisible(true);
         desktop.add(internalFrame);
         
         JInternalFrame internalFrame2 = new JInternalFrame("case 02", true, true, true, true);
         mycase2 demo2 = new mycase2();
         JScrollPane jsp2 = new JScrollPane(demo2.createContentPane2("case 02"),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         internalFrame2.add(jsp2);
         internalFrame2.setLocation(0, 222);
         internalFrame2.setSize(screenSize.width-20, 200);
         internalFrame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         internalFrame2.setIcon(true);
         internalFrame2.setVisible(true);
         desktop.add(internalFrame2);
        
         JInternalFrame internalFrame3 = new JInternalFrame("case 03", true, true, true, true);
         mycase3 demo3 = new mycase3();
         JScrollPane jsp3 = new JScrollPane(demo3.createContentPane2("case 03"),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         internalFrame3.add(jsp3);
         internalFrame3.setLocation(0, 200*2+22);
         internalFrame3.setSize(screenSize.width-20, 200);
         internalFrame3.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         internalFrame3.setIcon(true);
         internalFrame3.setVisible(true);
         desktop.add(internalFrame3);
         
         JInternalFrame internalFrame4 = new JInternalFrame("case 04", true, true, true, true);
         mycase4 demo4 = new mycase4();
         JScrollPane jsp4 = new JScrollPane(demo4.createContentPane2("case 04"),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         internalFrame4.add(jsp4);
         internalFrame4.setLocation(0, 200*3+22);
         internalFrame4.setSize(screenSize.width-20, 200);
         internalFrame4.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         internalFrame4.setIcon(true);
         internalFrame4.setVisible(true);
         desktop.add(internalFrame4);
    
         
         JInternalFrame internalFrame5 = new JInternalFrame("case 05", true, true, true, true);
         mycase5 demo5 = new mycase5();
         JScrollPane jsp5 = new JScrollPane(demo5.createContentPane2("case 05"),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         internalFrame5.add(jsp5);
         internalFrame5.setLocation(0, 200*4+22);
         internalFrame5.setSize(screenSize.width-20, 200);
         internalFrame5.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         internalFrame5.setIcon(true);
         internalFrame5.setVisible(true);
         desktop.add(internalFrame5);
         
         JInternalFrame internalFrame6 = new JInternalFrame("case 06", true, true, true, true);
         mycase6 demo6 = new mycase6();
         JScrollPane jsp6 = new JScrollPane(demo6.createContentPane2("case 06"),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         internalFrame6.add(jsp6);
         internalFrame6.setLocation(0, 200*5+22);
         internalFrame6.setSize(screenSize.width-20, 200);
         internalFrame6.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         internalFrame6.setIcon(true);
         internalFrame6.setVisible(true);
         desktop.add(internalFrame6);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    
}
