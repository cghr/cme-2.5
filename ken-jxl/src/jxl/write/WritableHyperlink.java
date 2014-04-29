/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.net.URL;
/*   5:    */ import jxl.Hyperlink;
/*   6:    */ import jxl.write.biff.HyperlinkRecord;
/*   7:    */ 
/*   8:    */ public class WritableHyperlink
/*   9:    */   extends HyperlinkRecord
/*  10:    */   implements Hyperlink
/*  11:    */ {
/*  12:    */   public WritableHyperlink(Hyperlink h, WritableSheet ws)
/*  13:    */   {
/*  14: 42 */     super(h, ws);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public WritableHyperlink(int col, int row, URL url)
/*  18:    */   {
/*  19: 54 */     this(col, row, col, row, url);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public WritableHyperlink(int col, int row, int lastcol, int lastrow, URL url)
/*  23:    */   {
/*  24: 68 */     this(col, row, lastcol, lastrow, url, null);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public WritableHyperlink(int col, int row, int lastcol, int lastrow, URL url, String desc)
/*  28:    */   {
/*  29: 88 */     super(col, row, lastcol, lastrow, url, desc);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public WritableHyperlink(int col, int row, File file)
/*  33:    */   {
/*  34:100 */     this(col, row, col, row, file, null);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public WritableHyperlink(int col, int row, File file, String desc)
/*  38:    */   {
/*  39:113 */     this(col, row, col, row, file, desc);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public WritableHyperlink(int col, int row, int lastcol, int lastrow, File file)
/*  43:    */   {
/*  44:128 */     super(col, row, lastcol, lastrow, file, null);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public WritableHyperlink(int col, int row, int lastcol, int lastrow, File file, String desc)
/*  48:    */   {
/*  49:148 */     super(col, row, lastcol, lastrow, file, desc);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public WritableHyperlink(int col, int row, String desc, WritableSheet sheet, int destcol, int destrow)
/*  53:    */   {
/*  54:168 */     this(col, row, col, row, desc, sheet, destcol, destrow, destcol, destrow);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public WritableHyperlink(int col, int row, int lastcol, int lastrow, String desc, WritableSheet sheet, int destcol, int destrow, int lastdestcol, int lastdestrow)
/*  58:    */   {
/*  59:195 */     super(col, row, lastcol, lastrow, desc, sheet, destcol, destrow, lastdestcol, lastdestrow);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setURL(URL url)
/*  63:    */   {
/*  64:205 */     super.setURL(url);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setFile(File file)
/*  68:    */   {
/*  69:215 */     super.setFile(file);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setDescription(String desc)
/*  73:    */   {
/*  74:225 */     super.setContents(desc);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setLocation(String desc, WritableSheet sheet, int destcol, int destrow, int lastdestcol, int lastdestrow)
/*  78:    */   {
/*  79:243 */     super.setLocation(desc, sheet, destcol, destrow, lastdestcol, lastdestrow);
/*  80:    */   }
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableHyperlink
 * JD-Core Version:    0.7.0.1
 */