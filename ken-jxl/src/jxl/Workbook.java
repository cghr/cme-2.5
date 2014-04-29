/*   1:    */ package jxl;
/*   2:    */ 
/*   3:    */ import java.io.FileInputStream;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import jxl.read.biff.BiffException;
/*   9:    */ import jxl.read.biff.PasswordException;
/*  10:    */ import jxl.read.biff.WorkbookParser;
/*  11:    */ import jxl.write.WritableWorkbook;
/*  12:    */ import jxl.write.biff.WritableWorkbookImpl;
/*  13:    */ 
/*  14:    */ public abstract class Workbook
/*  15:    */ {
/*  16:    */   private static final String VERSION = "2.6.12";
/*  17:    */   
/*  18:    */   public abstract Sheet[] getSheets();
/*  19:    */   
/*  20:    */   public abstract String[] getSheetNames();
/*  21:    */   
/*  22:    */   public abstract Sheet getSheet(int paramInt)
/*  23:    */     throws IndexOutOfBoundsException;
/*  24:    */   
/*  25:    */   public abstract Sheet getSheet(String paramString);
/*  26:    */   
/*  27:    */   public static String getVersion()
/*  28:    */   {
/*  29:104 */     return "2.6.12";
/*  30:    */   }
/*  31:    */   
/*  32:    */   public abstract int getNumberOfSheets();
/*  33:    */   
/*  34:    */   public abstract Cell findCellByName(String paramString);
/*  35:    */   
/*  36:    */   public abstract Cell getCell(String paramString);
/*  37:    */   
/*  38:    */   public abstract Range[] findByName(String paramString);
/*  39:    */   
/*  40:    */   public abstract String[] getRangeNames();
/*  41:    */   
/*  42:    */   public abstract boolean isProtected();
/*  43:    */   
/*  44:    */   protected abstract void parse()
/*  45:    */     throws BiffException, PasswordException;
/*  46:    */   
/*  47:    */   public abstract void close();
/*  48:    */   
/*  49:    */   public static Workbook getWorkbook(java.io.File file)
/*  50:    */     throws IOException, BiffException
/*  51:    */   {
/*  52:198 */     return getWorkbook(file, new WorkbookSettings());
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static Workbook getWorkbook(java.io.File file, WorkbookSettings ws)
/*  56:    */     throws IOException, BiffException
/*  57:    */   {
/*  58:213 */     FileInputStream fis = new FileInputStream(file);
/*  59:    */     
/*  60:    */ 
/*  61:    */ 
/*  62:217 */     jxl.read.biff.File dataFile = null;
/*  63:    */     try
/*  64:    */     {
/*  65:221 */       dataFile = new jxl.read.biff.File(fis, ws);
/*  66:    */     }
/*  67:    */     catch (IOException e)
/*  68:    */     {
/*  69:225 */       fis.close();
/*  70:226 */       throw e;
/*  71:    */     }
/*  72:    */     catch (BiffException e)
/*  73:    */     {
/*  74:230 */       fis.close();
/*  75:231 */       throw e;
/*  76:    */     }
/*  77:234 */     fis.close();
/*  78:    */     
/*  79:236 */     Workbook workbook = new WorkbookParser(dataFile, ws);
/*  80:237 */     workbook.parse();
/*  81:    */     
/*  82:239 */     return workbook;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static Workbook getWorkbook(InputStream is)
/*  86:    */     throws IOException, BiffException
/*  87:    */   {
/*  88:253 */     return getWorkbook(is, new WorkbookSettings());
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static Workbook getWorkbook(InputStream is, WorkbookSettings ws)
/*  92:    */     throws IOException, BiffException
/*  93:    */   {
/*  94:268 */     jxl.read.biff.File dataFile = new jxl.read.biff.File(is, ws);
/*  95:    */     
/*  96:270 */     Workbook workbook = new WorkbookParser(dataFile, ws);
/*  97:271 */     workbook.parse();
/*  98:    */     
/*  99:273 */     return workbook;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static WritableWorkbook createWorkbook(java.io.File file)
/* 103:    */     throws IOException
/* 104:    */   {
/* 105:286 */     return createWorkbook(file, new WorkbookSettings());
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static WritableWorkbook createWorkbook(java.io.File file, WorkbookSettings ws)
/* 109:    */     throws IOException
/* 110:    */   {
/* 111:301 */     FileOutputStream fos = new FileOutputStream(file);
/* 112:302 */     WritableWorkbook w = new WritableWorkbookImpl(fos, true, ws);
/* 113:303 */     return w;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static WritableWorkbook createWorkbook(java.io.File file, Workbook in)
/* 117:    */     throws IOException
/* 118:    */   {
/* 119:320 */     return createWorkbook(file, in, new WorkbookSettings());
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static WritableWorkbook createWorkbook(java.io.File file, Workbook in, WorkbookSettings ws)
/* 123:    */     throws IOException
/* 124:    */   {
/* 125:338 */     FileOutputStream fos = new FileOutputStream(file);
/* 126:339 */     WritableWorkbook w = new WritableWorkbookImpl(fos, in, true, ws);
/* 127:340 */     return w;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static WritableWorkbook createWorkbook(OutputStream os, Workbook in)
/* 131:    */     throws IOException
/* 132:    */   {
/* 133:357 */     return createWorkbook(os, in, ((WorkbookParser)in).getSettings());
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static WritableWorkbook createWorkbook(OutputStream os, Workbook in, WorkbookSettings ws)
/* 137:    */     throws IOException
/* 138:    */   {
/* 139:376 */     WritableWorkbook w = new WritableWorkbookImpl(os, in, false, ws);
/* 140:377 */     return w;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static WritableWorkbook createWorkbook(OutputStream os)
/* 144:    */     throws IOException
/* 145:    */   {
/* 146:393 */     return createWorkbook(os, new WorkbookSettings());
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static WritableWorkbook createWorkbook(OutputStream os, WorkbookSettings ws)
/* 150:    */     throws IOException
/* 151:    */   {
/* 152:411 */     WritableWorkbook w = new WritableWorkbookImpl(os, false, ws);
/* 153:412 */     return w;
/* 154:    */   }
/* 155:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.Workbook
 * JD-Core Version:    0.7.0.1
 */