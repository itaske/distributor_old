package com.pechmod.connection;
import java.net.*;
import java.util.LinkedList;
import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.*;

import com.pechmod.file.StatusRecord;
import com.pechmod.protocol.ModalFile;
import com.pechmod.protocol.FileMerger;

public class Client extends SwingWorker {

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean connected=false;
	private long totalBytesToReceive;
    private AtomicLong currentBytesRead;
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

				System.out.println("Could not connect to the server with name given");

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

	public ObjectOutputStream getOutput()
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
		setProgress(0);
		currentBytesRead = new AtomicLong();
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
			if(filesReceive!=null) {
				FileMerger fileMerger = new FileMerger(filesReceive.get(i));

				fileMerger.execute(this);
					for (int k = 0; k < filesReceive.get(i).size(); k++) {
						System.out.println(filesReceive.get(i).get(k).getName() + "and this " + filesReceive.get(i).get(k).getDestination());
						record.addFile(filesReceive.get(i).get(k).getName(), filesReceive.get(i).get(k).getDestination());

//						JOptionPane.showMessageDialog(null, currentBytes+" "+totalBytesToReceive, percentReceived+"", JOptionPane.INFORMATION_MESSAGE);
					}


			}//close if
		}//close for loop for files Received
		setProgress(100);
		return null;
	}

	public long getTotalBytesToReceive() {
		return totalBytesToReceive;
	}

	public void setTotalBytesToReceive(long totalBytesToReceive) {
		this.totalBytesToReceive = totalBytesToReceive;
	}

	public void setProgressValue(int value){
		setProgress(value);
	}

}
