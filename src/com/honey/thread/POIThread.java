package com.honey.thread;

import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.honey.poi.POI2Word;


import static com.honey.poi.POI2Word.READ_PROCESS_FAILED;
import static com.honey.poi.POI2Word.SELECTED_NO_FILE;
import static com.honey.poi.POI2Word.WRITE_PROCESS_FAILED;
public class POIThread implements Runnable{
	private String choosedField;
	private String saveField;
	private Set<String> fileTypeSet;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	
	public POIThread(String choosedField, String saveField,
			Set<String> fileTypeSet, JLabel progressLabel,
			JProgressBar progressBar) {
		super();
		this.choosedField = choosedField;
		this.saveField = saveField;
		this.fileTypeSet = fileTypeSet;
		this.progressLabel = progressLabel;
		this.progressBar = progressBar;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		progressLabel.setVisible(true);
		progressBar.setVisible(true);
		progressLabel.setText("程序执行中...");
		progressBar.setIndeterminate(true);
		// do real work here
		// director, outputfile, fileFilter
		POI2Word process = new POI2Word(choosedField,saveField,fileTypeSet);
		int result = process.doProcess();
		String reason = null;
		switch(result){
		case SELECTED_NO_FILE:
			reason = "指定类型的代码不存在";
			JOptionPane.showMessageDialog(null,reason);
			break;
		case READ_PROCESS_FAILED:
			reason = "读取文件错误";
			JOptionPane.showMessageDialog(null,reason);
			break;
		case WRITE_PROCESS_FAILED:
			reason = "写入word文档错误";
			JOptionPane.showMessageDialog(null,reason);
			break;
		default:
			reason = "执行成功！";
			
		}
		if(result ==0){
			progressLabel.setText("执行结束！");
			progressBar.setIndeterminate(false);
			progressBar.setMaximum(100);
			progressBar.setValue(100);
		}

		JOptionPane.showConfirmDialog(null, "是否继续", reason,JOptionPane.OK_OPTION);
		//progressLabel.setVisible(false);
		//progressBar.setVisible(false);
		progressBar.setIndeterminate(false);
		progressBar.setValue(10);
		progressLabel.setText("等待执行...");
	}
}
