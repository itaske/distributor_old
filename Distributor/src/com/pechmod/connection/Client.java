package com.pechmod.connection;
import java.net.*;
import java.util.LinkedList;
import java.io.*;

import javax.swing.SwingWorker;

import com.pechmod.file.StatusRecord;
import com.pechmod.protocol.ModalFile;
import com.pechmod.protocol.Progress_Monitor;

public class Client extends SwingWorker {

	 Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean connected=false;
	
	StatusRecord record;
	public Client(StatusRecord record)
	{
		this.record=record;
	}
	
	public void connectTo(String hostName,int port)
	{
		do{
			try {
		
			socket=new Socket(hostName,port);
			System.out.println("Connected");
			setUpStreams();
			System.out.println("Done setting streams");
			connected=true;
		}catch(UnknownHostException u){
		
		System.out.println("Unknown Error");
		
			u.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO Error");
			e.printStackTrace();
			
		}
			
		}
		while(connected==false);
	}
	
	public void setUpStreams()throws IOException
	{
		
			
			 
			 oos=new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois=new ObjectInputStream(socket.getInputStream());
			
	}
	
	public ObjectOutputStream getOuput()
	{
		return oos;
	}
	
	public ObjectInputStream getInput()
	{
		return ois;
	}
	
	public void closeConnections()
	{
		try {
			if (socket!=null) {
				oos.close();
				ois.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected Object doInBackground() throws Exception {
		// TODO Auto-generated method stub
		 LinkedList<LinkedList<ModalFile>> filesReceive = null;
		do
		{
			try 
			{
				filesReceive = (LinkedList<LinkedList<ModalFile>>)this.getInput().readObject();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}//close do
		while(filesReceive==null);
		
		
		
		 for(int i=0; i<filesReceive.size(); i++)
	  	   {
    	  if(filesReceive!=null)
    		   {
    		 
    		  
    	 Progress_Monitor progressMonitor=new Progress_Monitor(filesReceive.get(i));
    		
    		 
    		  
    		  progressMonitor.execute();
    		  
    		  for(int j=0; j<filesReceive.size(); j++)
    		  for(int k=0; k<filesReceive.get(j).size(); k++)
    		  { System.out.println(filesReceive.get(j).get(k).getName()+"and this "+filesReceive.get(j).get(k).getDestination());
          	  record.addFile(filesReceive.get(j).get(k).getName(),filesReceive.get(j).get(k).getDestination());
    		  }
    		   }//close if
    	  
	  	   }//close for loop for files Received
		return null;
	}
	
}
