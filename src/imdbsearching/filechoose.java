package imdbsearching;
//import java.io.*;
//import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
//import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.SwingWorker;

public class filechoose extends JPanel implements ActionListener/*,PropertyChangeListener*/ {
    static private final String newline = "\n";
    JButton openButton, saveButton,clearButton;
    JTextArea log;
    JFileChooser fc;
    JProgressBar progress;
    int listlength;
	int progressvalue=0;
	String filepath="";
	String[] list;
    public filechoose() {
        super(new BorderLayout());

        log = new JTextArea(30,38);
        log.setMargin(new Insets(10,10,10,10));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        fc = new JFileChooser();

 
        openButton = new JButton("Open a movie directory...",createImageIcon("images/open.png"));
        openButton.addActionListener(this);

        saveButton = new JButton("Save this List...",createImageIcon("images/Save.png"));
        saveButton.addActionListener(this);
         
        clearButton=new JButton("Clear this list",createImageIcon("images/clear.png"));
        clearButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
    //    add(clearButton,BorderLayout.WEST);
    //    clearButton.addActionListener(this);
        
        progress=new JProgressBar(0,300);
        progress.setValue(0);
        progress.setStringPainted(true); 

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        add(progress,BorderLayout.PAGE_END);
    }
    
    class Task extends SwingWorker<Void,Void> {

		@Override
		public Void doInBackground() throws Exception {
			ArrayList<String> uniquelist=new ArrayList<String>();
			progressvalue=0;
    		Imdb testimdb=new Imdb();
    		String temp[]={""};
    		listlength=list.length;
    		int increment=(int) Math.ceil((300.0/(double)listlength));
    		if(increment<=0)
    			increment=1;
    		setProgress(0);
    		progress.setStringPainted(true);
        	for(int i=0;i<list.length;i++)
        	{
        		
        		try
        		{
        			
        			String noSpace=list[i];
        			noSpace=list[i].replaceAll(" ","%20");
        			
        			noSpace = noSpace.replaceAll("DVDRip","");
        			noSpace = noSpace.replaceAll("-","");
        			noSpace = noSpace.replaceAll("720p","");
        			noSpace = noSpace.replaceAll("x264","");
        			noSpace = noSpace.replaceAll("\\[DDR\\]","");
        			noSpace = noSpace.replaceAll("BluRay","");
        			noSpace = noSpace.replaceAll("1080p",""); 
        			String regex = "/(\\([0-9]+\\))/";
        			noSpace = noSpace.replaceAll(regex,"");
        			noSpace = noSpace.replaceAll(" ","");
        			temp=testimdb.imdb_search_print(noSpace);


        		}
        		catch(Exception exc)
        		{
        			System.out.println(exc);
       			}
    			
    			progressvalue=progressvalue+increment;
    			//setProgress(Math.min(progressvalue, 300));
    			progress.setValue(Math.min(progressvalue,300));
    			boolean tep=uniquelist.contains(temp[0]);
    			if(!uniquelist.contains(temp[0]))
    			{
    				
    				uniquelist.add(temp[0]);
    				for(int j=0;j<temp.length;j++)
    				{
    					log.append(temp[j]);
    				}
    			
    				log.append("***********************************"+ newline);
    			}
        	}
			
			return null;
		}
		 @Override
	        public void done() {
	            Toolkit.getDefaultToolkit().beep();
	            openButton.setEnabled(true);
	            clearButton.setEnabled(true);
	            saveButton.setEnabled(true);
	            progress.setValue(progress.getMinimum());
	            setCursor(null); //turn off the wait cursor
	            progress.setStringPainted(false);
	            
	        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
        	progressvalue=0;
        	openButton.setEnabled(false);
        	clearButton.setEnabled(false);
        	saveButton.setEnabled(false);
       // 	new Task().setProgress(progressvalue);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(filechoose.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
            	filepath=fc.getCurrentDirectory().getPath();
            	list=new File(filepath).list(new FilenameFilter()
				{
					public boolean accept(File f,String s)
					{
						return s.endsWith("");
					}
				});
            	Task runTask=new Task();
            //	runTask.addPropertyChangeListener(this);
            	runTask.execute();
            
      /*  		Imdb testimdb=new Imdb();
        		String temp[]={""};
        		listlength=list.length;
        		progressvalue=100/listlength;
        		
            	for(int i=0;i<list.length;i++)
            	{
            		
            		try
            		{
            		//	setProgress(progressvalue);
            			String noSpace=list[i];
            			noSpace=list[i].replaceAll(" ","");
            			temp=testimdb.imdb_search_print(noSpace);

            		}
            		catch(Exception exc)
            		{
            			System.out.println(exc);
           			}
            		for(int j=0;j<temp.length;j++)
            		{
            			log.append(temp[j]);
            		}
            		
            		log.append("***********************************"+ newline);
            	}  */

            } 
            else 
            {
                log.append("Open command cancelled by user." + newline);
                openButton.setEnabled(true);
                clearButton.setEnabled(true);
                saveButton.setEnabled(true);
             }
            log.setCaretPosition(log.getDocument().getLength());

        }
        else if (e.getSource() == saveButton) 
        {
        	
        		String saveText=log.getText();
        		try{
        			filepath=fc.getCurrentDirectory().getPath();
        			PrintWriter WriteFile=new PrintWriter(filepath+"/"+"IMDBlist.txt");	
        			WriteFile.write(log.getText());
        			WriteFile.close();
        			File textfile=new File(filepath+"/IMDBlist.txt");
        			Desktop d= Desktop.getDesktop();
        			d.open(textfile);
        		}
        		catch(Exception ex)
        		{
        			System.out.println(ex);
        		}
        }
        else if (e.getSource() == clearButton)
        {
        	log.setText("");
        	openButton.setEnabled(true);
            clearButton.setEnabled(true);
            saveButton.setEnabled(true);
        	
        }
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = filechoose.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Movie search at IMDB");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new filechoose());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.TRUE); 
                createAndShowGUI();
            }
        });
    }

//	@Override
/*	public void propertyChange(PropertyChangeEvent evt) {
	/*	if ("progressvalue" == evt.getPropertyName()) {
            int progressval = (Integer) evt.getNewValue();
            progress.setValue(progressval);
		}*/
//	}
	
}
