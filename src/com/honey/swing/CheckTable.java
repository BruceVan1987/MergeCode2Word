package com.honey.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/** @see http://stackoverflow.com/a/13919878/230513 */
public class CheckTable {

    private static final CheckModel model = new CheckModel(20);
    private static final JTable table = new JTable(model) {

        @Override
		public void setRowHeight(int rowHeight) {
			// TODO Auto-generated method stub
			super.setRowHeight(20);
		}

		@Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(150, 400);
        }

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            JCheckBox jcb = (JCheckBox) super.prepareRenderer(renderer, row, column);
            jcb.setHorizontalTextPosition(JCheckBox.LEADING);
            //jcb.setText(String.valueOf(row)+".java");
            String fileType = switchCaseString(row);
            jcb.setText("."+fileType);
            return jcb;
        }
    };
    public JTable getTable(){
    	return table;
    }
    public String fileTypeSelected(){
    	Set<String> ss = new HashSet<String>();
    	CheckModel model = getModel();
    	for(Integer i : model.checked){
    		ss.add(switchCaseString(i.intValue()));
    	}
    	return ss.toString().replace("[", "").replace("]", "");
    }
    public Set<String> fileTypeSelectedSet(){
    	Set<String> ss = new HashSet<String>();
    	CheckModel model = getModel();
    	for(Integer i : model.checked){
    		ss.add(switchCaseString(i.intValue()));
    	}
    	return ss;
    }
    public CheckModel getModel(){
    	return model;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame f = new JFrame("CheckTable");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setLayout(new GridLayout(1, 0));
                f.add(new JScrollPane(table));
                f.add(new DisplayPanel(model));
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    public static class DisplayPanel extends JPanel {

        private DefaultListModel dlm = new DefaultListModel();
        private JList list = new JList(dlm);

        public DisplayPanel(final CheckModel model) {
            super(new GridLayout());
            this.setBorder(BorderFactory.createTitledBorder("已选择的文件类型"));
            this.add(new JScrollPane(list));
            model.addTableModelListener(new TableModelListener() {

                @Override
                public void tableChanged(TableModelEvent e) {
                    dlm.removeAllElements();
                    for (Integer integer : model.checked) {
                        String fileType = switchCaseString(integer.intValue());
                        dlm.addElement("."+fileType);
                    }
                }
            });
        }
    }

    public static class CheckModel extends AbstractTableModel {

        private final int rows;
        private List<Boolean> rowList;
        private Set<Integer> checked = new TreeSet<Integer>();

        public CheckModel(int rows) {
            this.rows = rows;
            rowList = new ArrayList<Boolean>(rows);
            for (int i = 0; i < rows; i++) {
                rowList.add(Boolean.FALSE);
            }
        }

        @Override
        public int getRowCount() {
            return rows;
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public String getColumnName(int col) {
            //return "Column " + col;
        	return "请选择文件类型";
        }

        @Override
        public Object getValueAt(int row, int col) {
                return rowList.get(row);
        }

        @Override
        public void setValueAt(Object aValue, int row, int col) {
            boolean b = (Boolean) aValue;
            rowList.set(row, b);
            if (b) {
                checked.add(row);
            } else {
                checked.remove(row);
            }
            fireTableRowsUpdated(row, row);
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }
    }
    //*switch case help
    private static String switchCaseString(int i){
    	String s = null;
    	switch(i){
    	case 0:
    		s = "asp";
    		break;
    	case 1:
    		s = "aspx";
    		break;
    	case 2:
    		s = "c";
    		break;
    	case 3:
    		s = "cpp";
    		break;
    	case 4:
    		s = "cs";
    		break;
    	case 5:
    		s = "css";
    		break;
    	case 6:
    		s = "cxx";
    		break;
    	case 7:
    		s = "h";
    		break;
    	case 8:
    		s = "hpp";
    		break;
    	case 9:
    		s = "htm";
    		break;
    	case 10:
    		s = "html";
    		break;
    	case 11:
    		s = "java";
    		break;
    	case 12:
    		s = "js";
    		break;
    	case 13:
    		s = "jsp";
    		break;
    	case 14:
    		s = "php";
    		break;
    	case 15:
    		s = "sh";
    		break;
    	case 16:
    		s = "sql";
    		break;
    	case 17:
    		s = "vb";
    		break;
    	case 18:
    		s = "xml";
    		break;
    	default:
    		s = "txt";
    	}
    	return s;
    }
}