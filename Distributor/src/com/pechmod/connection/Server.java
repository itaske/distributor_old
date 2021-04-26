package com.pechmod.connection;

import java.io.*;
import java.net.*;
import java.util.*;

import com.pechmod.file.Record;
import com.pechmod.protocol.ClientReply;
import com.pechmod.protocol.ModalFile;
import com.pechmod.protocol.TransferModalFile;



public class Server implements Runnable{

	private ServerSocket server;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Record record;
	private boolean running;
	private ClientReply reply;
	public Server(Record record)
	{
		this.record=record;
		try
		{

			server=new ServerSocket(4444);
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}


	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public ClientReply getClientReply() {
		return reply;
	}

	public void setClientReply(ClientReply reply) {
		this.reply = reply;
	}

	/**
	 * @return ClientReply after connection
	 */
	public ClientReply acceptConnection(){
		System.out.println("Looking for connection");
		ClientReply reply=null;

		try {
			socket = server.accept();
			setUpStreams();
			System.out.println("Done setting streams");
			running = true;
		}
		catch(IOException e){
			e.printStackTrace();
			running = false;
			return null;
		}

		do
		{
			try {
				reply=(ClientReply)ois.readObject(); // to get destination information and others details
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while(reply==null);

		return reply;
	}

	public void sendFiles(ClientReply reply){
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
			this.stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void setUpStreams()throws IOException
	{

		ois=new ObjectInputStream(socket.getInputStream());
		oos=new ObjectOutputStream(socket.getOutputStream());
		oos.flush();


	}

	public void setRunning(boolean running){
		this.running = running;
	}
	public boolean isRunning() {
		return running;
	}


	public ObjectOutputStream getOutput()
	{
		return oos;
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
	public void run() {
		reply = acceptConnection();
		sendFiles(reply);
	}


	public void stopServer()
	{
		try {
			server.close();
			running = false;
			closeConnections();
		} catch (IOException e) {
			closeConnections();
			e.printStackTrace();
		}


	}
}
