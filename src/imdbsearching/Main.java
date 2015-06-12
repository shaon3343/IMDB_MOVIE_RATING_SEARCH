

package imdbsearching;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Shaon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {

    	 SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.TRUE);
                createAndShowGUI();
            }

            private void createAndShowGUI() {
                 JFrame frame = new JFrame("Movie search at IMDB");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Add content to the window.
                frame.add(new filechoose());

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

}
