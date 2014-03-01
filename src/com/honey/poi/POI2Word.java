package com.honey.poi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class POI2Word {
	private String directory;
	private String outputPath;
	private Set<String> fileTypeSet;
	public POI2Word(String directory,String outputPath, Set<String> fileTypeSet){
		this.directory = directory;
		this.outputPath = outputPath;
		this.fileTypeSet = fileTypeSet;
	}
	public XWPFDocument document = new XWPFDocument();
	public final static int FONT_SIZE = 10;
	public final static int SELECTED_NO_FILE = 1;
	public final static int READ_PROCESS_FAILED = 2;
	public final static int WRITE_PROCESS_FAILED = 3;
	/*
	 * @fileName absolute path of file
	 * @fileName collect all absolute path of all files recursively
	 */
	public void collectAllFilePath(String fileName,List<String> fileNameList) {
		File file = new File(fileName);
		if(!file.exists()) {
			return;
		}
		//System.out.println(file.getAbsolutePath());
		if(file.isFile()) {
			String[] sa = file.getAbsolutePath().split(".");
			String type = sa[sa.length-1];
			if(fileTypeSet.contains(type))
			fileNameList.add(file.getAbsolutePath());
			return;
		}
		
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				if(f.isFile()) {
					String[] sa = f.getAbsolutePath().split("\\.");
					String type = sa[sa.length-1];
					if(fileTypeSet.contains(type))
					fileNameList.add(f.getAbsolutePath());
					continue;
				}
				if(f.isDirectory()) {
					collectAllFilePath(f.getAbsolutePath(), fileNameList);
				}
			}
			return;
		}
		
	}
	public int doProcess() {
		List<String> slist = new ArrayList<String>();
		collectAllFilePath(directory, slist);
		if(slist.isEmpty()){
			return SELECTED_NO_FILE;
		}
		for(String s : slist){
			File f = new File(s);
			BufferedReader in = null;
			try {
				in = new BufferedReader(
						new InputStreamReader(new FileInputStream(f), "UTF-8"));
				String str;
				while ((str = in.readLine()) != null) {
					//System.out.println(str);
					XWPFParagraph paragraph = document.createParagraph();
					XWPFRun run = paragraph.createRun();
					run.setFontSize(FONT_SIZE);
				    run.setText(str);
				}
				in.close();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				return READ_PROCESS_FAILED;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return READ_PROCESS_FAILED;
			} catch (IOException e) {
				e.printStackTrace();
				return READ_PROCESS_FAILED;
			}
			
		} // end for
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputPath);
			fos.write(new String("").getBytes());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return WRITE_PROCESS_FAILED;
		} catch (IOException e) {
			e.printStackTrace();
			return WRITE_PROCESS_FAILED;
		}
		
		try {
			document.write(fos);
			fos.flush();
			fos.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return WRITE_PROCESS_FAILED;
		} catch(IOException e) {
			e.printStackTrace();
			return WRITE_PROCESS_FAILED;
		}
		return 0;
	}
  
}
