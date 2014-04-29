/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import jxl.write.biff.File;
/*   7:    */ 
/*   8:    */ public class ConditionalFormat
/*   9:    */ {
/*  10:    */   private ConditionalFormatRangeRecord range;
/*  11:    */   private ArrayList conditions;
/*  12:    */   
/*  13:    */   public ConditionalFormat(ConditionalFormatRangeRecord cfrr)
/*  14:    */   {
/*  15: 51 */     this.range = cfrr;
/*  16: 52 */     this.conditions = new ArrayList();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void addCondition(ConditionalFormatRecord cond)
/*  20:    */   {
/*  21: 62 */     this.conditions.add(cond);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void insertColumn(int col)
/*  25:    */   {
/*  26: 73 */     this.range.insertColumn(col);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void removeColumn(int col)
/*  30:    */   {
/*  31: 84 */     this.range.removeColumn(col);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void removeRow(int row)
/*  35:    */   {
/*  36: 95 */     this.range.removeRow(row);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void insertRow(int row)
/*  40:    */   {
/*  41:106 */     this.range.insertRow(row);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void write(File outputFile)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47:117 */     outputFile.write(this.range);
/*  48:119 */     for (Iterator i = this.conditions.iterator(); i.hasNext();)
/*  49:    */     {
/*  50:121 */       ConditionalFormatRecord cfr = (ConditionalFormatRecord)i.next();
/*  51:122 */       outputFile.write(cfr);
/*  52:    */     }
/*  53:    */   }
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.ConditionalFormat
 * JD-Core Version:    0.7.0.1
 */