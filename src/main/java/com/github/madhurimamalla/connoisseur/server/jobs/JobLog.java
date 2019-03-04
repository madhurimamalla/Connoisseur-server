package com.github.madhurimamalla.connoisseur.server.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JobLog {

	private static final String LOG_DIR = System.getProperty("job.log.dir");

	private Logger logger;

	private String filePath;

	public JobLog(String jobName, long jobId) {
		File directory = new File(LOG_DIR);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		filePath = getJobLogFilePath(jobName, jobId);
		logger = Logger.getLogger(jobName + "_" + jobId);
		FileHandler fh;
		try {
			fh = new FileHandler(filePath);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			logger.info("Initialized");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String msg) {
		logger.info(msg);
	}

	private static String getJobLogFilePath(String jobName, long jobId) {
		return LOG_DIR + "/" + jobName + "_" + jobId + ".log";
	}
	
	public static String readLog(String jobName, long jobId) {
		File file = new File(getJobLogFilePath(jobName, jobId));
		return tail(file, 100);
	}
	
	public static String printLogs(File file){
		
		
		return "";
	}

	public static String tail(File file, int maxLines) {
		StringBuilder buffer = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String[] lines = new String[maxLines];
			int lastNdx = 0;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (lastNdx == lines.length) {
					lastNdx = 0;
				}
				lines[lastNdx++] = line;
			}
			for (int ndx = lastNdx; ndx != lastNdx - 1; ndx++) {
				if (ndx == lines.length) {
					ndx = 0;
				}
				if(lines[ndx] != null && !lines[ndx].trim().isEmpty()) {
					buffer.append(lines[ndx]).append("\n");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
