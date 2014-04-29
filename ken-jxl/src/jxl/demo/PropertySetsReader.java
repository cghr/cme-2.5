/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileInputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.OutputStreamWriter;
/*   9:    */ import jxl.WorkbookSettings;
/*  10:    */ import jxl.biff.BaseCompoundFile.PropertyStorage;
/*  11:    */ import jxl.read.biff.BiffException;
/*  12:    */ import jxl.read.biff.CompoundFile;
/*  13:    */ 
/*  14:    */ class PropertySetsReader
/*  15:    */ {
/*  16:    */   private BufferedWriter writer;
/*  17:    */   private CompoundFile compoundFile;
/*  18:    */   
/*  19:    */   public PropertySetsReader(File file, String propertySet, OutputStream os)
/*  20:    */     throws IOException, BiffException
/*  21:    */   {
/*  22: 55 */     this.writer = new BufferedWriter(new OutputStreamWriter(os));
/*  23: 56 */     FileInputStream fis = new FileInputStream(file);
/*  24:    */     
/*  25: 58 */     int initialFileSize = 1048576;
/*  26: 59 */     int arrayGrowSize = 1048576;
/*  27:    */     
/*  28: 61 */     byte[] d = new byte[initialFileSize];
/*  29: 62 */     int bytesRead = fis.read(d);
/*  30: 63 */     int pos = bytesRead;
/*  31: 65 */     while (bytesRead != -1)
/*  32:    */     {
/*  33: 67 */       if (pos >= d.length)
/*  34:    */       {
/*  35: 70 */         byte[] newArray = new byte[d.length + arrayGrowSize];
/*  36: 71 */         System.arraycopy(d, 0, newArray, 0, d.length);
/*  37: 72 */         d = newArray;
/*  38:    */       }
/*  39: 74 */       bytesRead = fis.read(d, pos, d.length - pos);
/*  40: 75 */       pos += bytesRead;
/*  41:    */     }
/*  42: 78 */     bytesRead = pos + 1;
/*  43:    */     
/*  44: 80 */     this.compoundFile = new CompoundFile(d, new WorkbookSettings());
/*  45: 81 */     fis.close();
/*  46: 83 */     if (propertySet == null) {
/*  47: 85 */       displaySets();
/*  48:    */     } else {
/*  49: 89 */       displayPropertySet(propertySet, os);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   void displaySets()
/*  54:    */     throws IOException
/*  55:    */   {
/*  56: 98 */     int numSets = this.compoundFile.getNumberOfPropertySets();
/*  57:100 */     for (int i = 0; i < numSets; i++)
/*  58:    */     {
/*  59:102 */       BaseCompoundFile.PropertyStorage ps = this.compoundFile.getPropertySet(i);
/*  60:103 */       this.writer.write(Integer.toString(i));
/*  61:104 */       this.writer.write(") ");
/*  62:105 */       this.writer.write(ps.name);
/*  63:106 */       this.writer.write("(type ");
/*  64:107 */       this.writer.write(Integer.toString(ps.type));
/*  65:108 */       this.writer.write(" size ");
/*  66:109 */       this.writer.write(Integer.toString(ps.size));
/*  67:110 */       this.writer.write(" prev ");
/*  68:111 */       this.writer.write(Integer.toString(ps.previous));
/*  69:112 */       this.writer.write(" next ");
/*  70:113 */       this.writer.write(Integer.toString(ps.next));
/*  71:114 */       this.writer.write(" child ");
/*  72:115 */       this.writer.write(Integer.toString(ps.child));
/*  73:116 */       this.writer.write(" start block ");
/*  74:117 */       this.writer.write(Integer.toString(ps.startBlock));
/*  75:118 */       this.writer.write(")");
/*  76:119 */       this.writer.newLine();
/*  77:    */     }
/*  78:122 */     this.writer.flush();
/*  79:123 */     this.writer.close();
/*  80:    */   }
/*  81:    */   
/*  82:    */   void displayPropertySet(String ps, OutputStream os)
/*  83:    */     throws IOException, BiffException
/*  84:    */   {
/*  85:132 */     if (ps.equalsIgnoreCase("SummaryInformation")) {
/*  86:134 */       ps = "\005SummaryInformation";
/*  87:136 */     } else if (ps.equalsIgnoreCase("DocumentSummaryInformation")) {
/*  88:138 */       ps = "\005DocumentSummaryInformation";
/*  89:140 */     } else if (ps.equalsIgnoreCase("CompObj")) {
/*  90:142 */       ps = "\001CompObj";
/*  91:    */     }
/*  92:145 */     byte[] stream = this.compoundFile.getStream(ps);
/*  93:146 */     os.write(stream);
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.PropertySetsReader
 * JD-Core Version:    0.7.0.1
 */