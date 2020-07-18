package com.pechmod.connection;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import com.pechmod.file.Record;
import com.pechmod.protocol.BroadcastHandler;
import com.pechmod.protocol.ClientReply;
import com.pechmod.protocol.ModalFile;
import com.pechmod.protocol.TransferModalFile;



public class Server implements Runnable{

	private ServerSocket server;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Record record;
	
	private JLabel label;
	public Server(Record record,JLabel label)
	{
		this.record=record;
		this.label=label;
		try
		{
			
		   server=new ServerSocket(4444);
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
		
		
	}
	
	public void acceptConnection(){
		
			System.out.println("Looking for connection");
		
		try {
			socket=server.accept();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			setUpStreams();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done setting streams");
		
		ClientReply reply=null;
		
		do
		{
			try {
				reply=(ClientReply)ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		while(reply==null);
		
LinkedList<LinkedList<ModalFile>> transferList=new LinkedList<LinkedList<ModalFile>>();
		
		//send Files selected by client
		for(int i=0; i<record.getSize(); i++)
		{
			LinkedList<ModalFile>prepareFile=new LinkedList<ModalFile>();
			TransferModalFile.makeModalList(prepareFile, record.getFile(i), reply.getDestination());
			
			transferList.add(prepareFile);
		
		}//close for loop for making Modal Files
		
		try {
			oos.writeObject(transferList);
			
			
			label.setText("Connection: OFF");
			this.stopThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	public void setUpStreams()throws IOException
	{
		
			
		 ois=new ObjectInputStream(socket.getInputStream());
		 
		 oos=new ObjectOutputStream(socket.getOutputStream());
		 
		oos.flush();
		
		
	}
	
	public Socket getSocket()
	{
		return socket;
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
				socket=null;
			}
			server=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		acceptConnection();
		
	}
	
	
	public void stopThread()
	{
		try {
			server.close();
			closeConnections();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			closeConnections();
			e.printStackTrace();
		}
		
		
	}
}
