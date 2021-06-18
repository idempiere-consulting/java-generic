 package zenfileprintserver;
 
 import java.util.Hashtable;
 import java.util.Vector;
 import javax.swing.table.AbstractTableModel;

 public class MapsTableModel
   extends AbstractTableModel
 {
   /**
	 * 
	 */
	private static final long serialVersionUID = 2152034542389611648L;
	
private String[] realColumnsName;
   private String[] columnsName;
   private Class<?>[] columnJavaType;
   private Vector<Vector<String>> rows;
   public int editCells = 0;
   private int columnsToShow = 1;
   private int iidColumn = 0;
 
 
   
   private boolean[] updatableColumns;
 
 
   
   public MapsTableModel(Vector<Vector<String>> data, String[] columnsName, String[] realColumnsName, boolean[] updatableColumns, Class<?>[] columnJavaType, int iidColumn, int columnsToShow) {
     this.columnsName = columnsName;
     this.columnsToShow = columnsToShow;
     this.realColumnsName = realColumnsName;
     this.iidColumn = iidColumn;
     this.updatableColumns = updatableColumns;
     this.columnJavaType = columnJavaType;
     this.rows = data;
   }
   
   public String getColumnName(int column) {
     if (this.columnsName != null) {
       return this.columnsName[column];
     }
     return "";
   }
 
   
   public Class<?> getColumnClass(int column) {
     try {
       return this.columnJavaType[column];
     } catch (Exception exc) {
       System.out.println("RTM (GCN_F): " + exc.toString());
       return (new Object()).getClass();
     } 
   }
   
   public String[] getColumnsName() {
     return this.columnsName;
   }
   
   public String[] getRealColumnsName() {
     return this.realColumnsName;
   }
   
   public Class<?>[] getColumnClasses() {
     return this.columnJavaType;
   }
   
   public boolean[] getUpdatableColumns() {
     return this.updatableColumns;
   }
 
 
 
   
   public boolean isCellEditable(int row, int col) {
     return this.updatableColumns[col];
   }
   
   public int getColumnCount() {
     return this.columnsToShow;
   }
   
   public int getRealColumnCount() {
     return this.columnsName.length;
   }
   
   public int getIidColumn() {
     return this.iidColumn;
   }
   
   public String getKeyValueAt(int row) {
     return getValueAt(row, getIidColumn());
   }
 
   
   public int getRowCount() {
     return this.rows.size();
   }
   
   public String getValueAt(int aRow, int aColumn) {
     return ((Vector<String>)this.rows.elementAt(aRow)).elementAt(aColumn);
   }
 
   
   public void setValueAt(Object inObj, int row, int column) {}
 
 
   
   public boolean setRowValuesAt(Hashtable inHt, int row) {
     return false;
   }
 
   public void deleteRow(int[] records) {}
   
   public boolean addRow(Hashtable values) {
     boolean outVal = false;
 
     return outVal;
   }
 
   
   public void refreshTableContent() {
     fireTableDataChanged();
   }
 }


