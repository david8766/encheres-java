package fr.eni.javaee.encheres.utils;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Classe utilitaire permettant de gérer les logger.
 **/
public class ErrorLogger
{
	public static FileHandler fh = null;
	public static ConsoleHandler ch = null;
	
	/**
	 * Permet de récuperer un logger.
	 **/
	public static Logger getLogger(String className)
	{
		Logger monLogger = Logger.getLogger(className);
		monLogger.setLevel(Level.FINEST);
		monLogger.setUseParentHandlers(false);
		
		if(ch == null)
		{
			ch = new ConsoleHandler();
			ch.setLevel(Level.FINEST);
		}

		if(fh == null)
		{
			try 
			{
				fh = new FileHandler("logs.log", true);
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			fh.setLevel(Level.ALL);
			fh.setFormatter(new SimpleFormatter());
		}
		
		monLogger.addHandler(ch);
		monLogger.addHandler(fh);
		
		return monLogger;
	}
}

