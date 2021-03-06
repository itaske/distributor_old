package com.pechmod.gui.receive;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.pechmod.gui.Frame;
import com.pechmod.connection.Client;
import com.pechmod.file.ReceiverRecord;
import com.pechmod.file.StatusRecord;
import com.pechmod.gui.events.Home_Action;
import com.pechmod.main.Test;
import com.pechmod.protocol.ClientReply;
import com.pechmod.protocol.ModalFile;
import com.pechmod.protocol.Progress_Monitor;
import com.pechmod.utils.User;

public class ReceivePanel extends JPanel implements ActionListener{
	JButton back,receive,receiveFiles;
	JLabel label;
	User user;
	
	JScrollPane scroll;
	
	JPanel bottomPanel;
	
	JPanel panel;
	
	JTable table;
	
   
   LinkedList<String> recordList;
   
   StatusRecord record;
   

   
  
	
	public ReceivePanel(JPanel panel)
	{
		
		setSize(350,550);
		setLayout(new BorderLayout());
		this.panel=panel;
		
		
		 
		
		
		
		back=new JButton("Back",new ImageIcon(Frame.class.getResource("events/Images/back.gif")));
		 back.setHorizontalTextPosition(SwingConstants.RIGHT);
		 back.setIconTextGap(5);
		
		
		receive=new JButton("Receive");
		
		
		record=new StatusRecord();
		 table=record.getTable();
		scroll=new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		table.setShowGrid(false);
		table.setShowHorizontalLines(true);
		table.setRowHeight(50);
		label=new JLabel("Connection: OFF");
		user=new User();

	
		
		
		bottomPanel=new JPanel(new BorderLayout());
		
		
		bottomPanel.add(back,BorderLayout.WEST);
		bottomPanel.add(label,BorderLayout.NORTH);
		bottomPanel.add(receive,BorderLayout.EAST);
		
		
		add(bottomPanel,BorderLayout.PAGE_END);
		add(scroll,BorderLayout.PAGE_START);

		
		back.addActionListener(this);
		receive.addActionListener(this);
	    
		
	}	
		
		

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 Client client = new Client(record);
       if (e.getSource()==receive) {
    	  
    	  receive.setEnabled(false); 
    	this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	   
    	   String host=null;
    	   host=JOptionPane.showInputDialog(null, "Please name of the host");
    	   if(host==null)
    		   return ;
		client.connectTo(host,4444);
		
			System.out.println("Connected");  
    		
			  label.setText("Connection: ON");
    	
    		   ClientReply reply= new ClientReply();
    		   reply.setDestination(user.getFileDestination().getAbsolutePath()+File.separator+"Distributor"+File.separator);
    		   
    		   client.execute();
    		   
    		  
			try {
				client.getOuput().writeObject(reply);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
    	
    	   
    	
    	   
    		   
    	  
		
    	  
		 
		 label.setText("Connection: ON");
		 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		 
		 receive.setEnabled(true);
		 
		 

		}//close receive if
		
       
       else if(e.getSource()==back)
       {
    	   
    	   
    	   client.closeConnections();
    	   label.setText("Connection: OFF");
    	   record.clear();
			 CardLayout c = (CardLayout) panel.getLayout();
			 c.show(panel,"Home");
       }
       
       
       
	}//close action performed
	
	}//close Class