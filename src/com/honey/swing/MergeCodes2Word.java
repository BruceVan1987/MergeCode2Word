package com.honey.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.honey.swing.CheckTable.DisplayPanel;
import com.honey.thread.POIThread;

public class MergeCodes2Word extends JFrame {

	/**
	 *  design a frame here, real work will begin where beginButton is clicked
	 */
	private static final long serialVersionUID = 6329570329698339009L;
	private JLabel jLabel;
	private JTextField choosedField;
	private JButton choosedBtn;
	private JTextField saveField;
	private JButton saveBtn;
	private JButton beginBtn;
	private JButton clearBtn;
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private JButton fileTypeButton;
	private JTextField fileTypeField;
	
	private Set<String> fileTypeSet = new HashSet<String>();
	/**
	 * @param args
	 */
	public MergeCodes2Word(){
		super();
		this.setSize(500,500);
		this.getContentPane().setLayout(null);
		this.add(getJLabel(), null);
		this.add(getChoosedField(), null);
		this.add(getChoosedButton(), null);
		this.add(getSaveField(), null);
		this.add(getSaveButton(), null);
		this.add(getBeginButton(), null);
		this.add(getClearButton(), null);
		this.add(getProgressBar(),null);
		this.add(getProgressLabel(),null);
		this.add(getFileTypeButton(), null);
		this.add(getFileTypeField(), null);
		this.setTitle("代码合并软件beta");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JButton getChoosedButton() {
		if(choosedBtn == null) {
			choosedBtn = new JButton();
			choosedBtn.setBounds(34, 100, 100, 27);
			choosedBtn.setText("选择目录");
			//jButton.set
			choosedBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					JFileChooser jFileChooser = new JFileChooser();
					jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = jFileChooser.showOpenDialog(hw);
					if(result == JFileChooser.APPROVE_OPTION){
						File file = jFileChooser.getSelectedFile();
						choosedField.setText(file.getAbsolutePath());
					}
					return;
				}
			});
		}
		return choosedBtn;
	}

	private JTextField getChoosedField() {
		if(choosedField == null) {
			choosedField = new JTextField();
			choosedField.setBounds(150, 100, 250, 25);
		}
		return choosedField;
	}
	private JButton getSaveButton(){
		if(saveBtn == null){
			saveBtn = new JButton();
			saveBtn.setBounds(34, 200, 100, 27);
			saveBtn.setText("保存文件");
			saveBtn.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jFileChooser = new JFileChooser();
					jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
					// we need to filter file type
					jFileChooser.addChoosableFileFilter(new DocxFileFilter());
					int result = jFileChooser.showDialog(hw, "保存文件");
					if(result == JFileChooser.APPROVE_OPTION){
						File file = jFileChooser.getSelectedFile();
						String filePath = file.getAbsolutePath();
						while(filePath.endsWith(".docx")){
							filePath = filePath.replace(".docx", "");
						}
						saveField.setText(filePath + ".docx");
					}
				}
				
			});
		}
		return saveBtn;
	}
	private JTextField getSaveField(){
		if(saveField == null){
			saveField = new JTextField();
			saveField.setBounds(150,200,250,25);
		}
		return saveField;
	}
	private JButton getBeginButton(){
		if(beginBtn == null){
			beginBtn = new JButton();
			beginBtn.setBounds(96, 280, 60, 27);
			beginBtn.setText("开始");
			beginBtn.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(choosedField.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(null,"请选择代码目录");
						return;
					}
					if(saveField.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(null,"请选择保存文件");
						return;
					}
					if(fileTypeField.getText().trim().equals("")||fileTypeSet==null||fileTypeSet.isEmpty()){
						JOptionPane.showMessageDialog(null,"请选择文件类型");
						return;
					}
					// do our copy work...
					// runnable instance 
					// label, progressBar,choosedField,saveField，代码类型
					//runnable(chooseField,List<String>,saveField,progressLabel,progressBar);

					POIThread pt = 
					new POIThread(choosedField.getText().trim(),saveField.getText().trim(),fileTypeSet,getProgressLabel(),getProgressBar());
					new Thread(pt).start();
					
				}
				
			});
		}
		return beginBtn;
	}
	private JProgressBar getProgressBar(){
		if(progressBar == null){
			progressBar = new JProgressBar();
			progressBar.setBounds(150, 240, 250, 25);
			progressBar.setIndeterminate(true);
			progressBar.setVisible(false);
		}
		return progressBar;
	}
	private JLabel getProgressLabel(){
		if(progressLabel ==null){
			progressLabel = new JLabel();
			progressLabel.setBounds(34, 240, 100, 27);
			progressLabel.setText("程序运行中...");
			progressLabel.setVisible(false);
		}
		
		return progressLabel;
	}
	private JButton getFileTypeButton(){
		if(fileTypeButton==null){
			fileTypeButton = new JButton();
			fileTypeButton.setBounds(34, 150, 100, 27);
			fileTypeButton.setText("代码类型");
			fileTypeButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//修改指定的代码类型
					final CheckTable checkTable = new CheckTable();
			        JFrame f = new JFrame("CheckTable");
			        f.addWindowListener(new WindowListener(){

						@Override
						public void windowOpened(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowClosing(WindowEvent e) {
							/** we should save file types to fileTypeField */
							String field = checkTable.fileTypeSelected();
							fileTypeSet = checkTable.fileTypeSelectedSet();
							fileTypeField.setText(field);
						}

						@Override
						public void windowClosed(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowIconified(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowDeiconified(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowActivated(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void windowDeactivated(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
			        	
			        });
			        f.setLayout(new GridLayout(1, 0));
	                f.add(new JScrollPane(checkTable.getTable()));
	                f.add(new DisplayPanel(checkTable.getModel()));
			        f.pack();
			        f.setLocationRelativeTo(null);
			        f.setVisible(true);
				}
				
			});
		}
		return fileTypeButton;
	}
	private JTextField getFileTypeField(){
		if(fileTypeField ==null){
			fileTypeField = new JTextField();
			fileTypeField.setBounds(150,150,250,25);
		}
		return fileTypeField;
	}
	private JButton getClearButton(){
		if(clearBtn == null){
			clearBtn = new JButton();
			clearBtn.setBounds(200, 280, 60, 27);
			clearBtn.setText("清除");
		}
		return clearBtn;
	}
	private JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new JLabel();
			jLabel.setBounds(34, 49, 250, 18);
			jLabel.setText("选择需要拷贝到word文档的源程序代码目录");
		}
		return jLabel;
	}
	static MergeCodes2Word hw = null;
	public static void main(String[] args) {
		hw = new MergeCodes2Word();
		hw.setVisible(true);

	}
	class DocxFileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			if(f.isDirectory()){
				return true;
			}
			String name = f.getName();
			return name.toLowerCase().endsWith("docx");
			//return false;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "*.docx";
		}
		
	}
}
