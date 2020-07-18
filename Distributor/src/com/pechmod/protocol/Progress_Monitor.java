package com.pechmod.protocol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.SwingWorker;

public class Progress_Monitor extends SwingWorker{
	
	static ProgressMonitor pbar;
	LinkedList<ModalFile>modal;
	static int counter;
	
	public Progress_Monitor(LinkedList<ModalFile>modal)
	{
		
		this.modal=modal;
		counter=0;	
		
	}
	
	
	
	protected Object doInBackground() throws Exception {
		
	
		
	
		for(int i=0; i<modal.size(); i++)
			
		   {
				
			File into=new File(modal.get(i).getDestination());
		System.out.println(modal.get(i).getDestination());
			
	
				try{
					if(into.isFile() && into.exists())
					{
						
						into.renameTo(new File(into.getParent()+File.separator+"1 "+into.getName()));
					}
				ChuckingFiles.mergeFiles(modal.get(i).getFileData(),into);
				
				}
				catch(IOException io)
		
				{
					io.printStackTrace();
				}
				
				
				setProgress(100*(i+1)/modal.size());
				
				
			
		   }
		
		
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
			
			
			
			
			
		
		
	}
		

