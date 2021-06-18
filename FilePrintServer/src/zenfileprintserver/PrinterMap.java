 package zenfileprintserver;
  
 public class PrinterMap
 {
   private String printer;
   private String filePath;
   private String key;
   private boolean complete = false;
   
   public PrinterMap() {
     this.printer = new String();
     this.filePath = new String();
     this.key = new String();
   }
   
   public PrinterMap(String printer, String key, String filePath) {
     this.printer = printer;
     this.filePath = filePath;
     this.key = key;
   }
   
   public String getPrinter() {
     return this.printer;
   }
   
   public void setPrinter(String printer) {
     this.printer = printer;
   }
   
   public String getFilePath() {
     return this.filePath;
   }
   
   public void setFilePath(String filePath) {
     this.filePath = filePath;
   }
   
   public void setComplete(boolean nValue) {
     this.complete = nValue;
   }
   
   public String getKey() {
     return this.key;
   }
   
   public void setKey(String key) {
     this.key = key;
   }
   
   public boolean getComplete() {
     return this.complete;
   }
 }