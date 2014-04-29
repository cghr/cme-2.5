/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.OutputStreamWriter;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.UnsupportedEncodingException;
/*   9:    */ import jxl.Cell;
/*  10:    */ import jxl.Sheet;
/*  11:    */ import jxl.SheetSettings;
/*  12:    */ import jxl.Workbook;
/*  13:    */ 
/*  14:    */ public class CSV
/*  15:    */ {
/*  16:    */   public CSV(Workbook w, OutputStream out, String encoding, boolean hide)
/*  17:    */     throws IOException
/*  18:    */   {
/*  19: 52 */     if ((encoding == null) || (!encoding.equals("UnicodeBig"))) {
/*  20: 54 */       encoding = "UTF8";
/*  21:    */     }
/*  22:    */     try
/*  23:    */     {
/*  24: 59 */       OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
/*  25: 60 */       BufferedWriter bw = new BufferedWriter(osw);
/*  26: 62 */       for (int sheet = 0; sheet < w.getNumberOfSheets(); sheet++)
/*  27:    */       {
/*  28: 64 */         Sheet s = w.getSheet(sheet);
/*  29: 66 */         if ((!hide) || (!s.getSettings().isHidden()))
/*  30:    */         {
/*  31: 68 */           bw.write("*** " + s.getName() + " ****");
/*  32: 69 */           bw.newLine();
/*  33:    */           
/*  34: 71 */           Cell[] row = (Cell[])null;
/*  35: 73 */           for (int i = 0; i < s.getRows(); i++)
/*  36:    */           {
/*  37: 75 */             row = s.getRow(i);
/*  38: 77 */             if (row.length > 0)
/*  39:    */             {
/*  40: 79 */               if ((!hide) || (!row[0].isHidden())) {
/*  41: 81 */                 bw.write(row[0].getContents());
/*  42:    */               }
/*  43: 86 */               for (int j = 1; j < row.length; j++)
/*  44:    */               {
/*  45: 88 */                 bw.write(44);
/*  46: 89 */                 if ((!hide) || (!row[j].isHidden())) {
/*  47: 91 */                   bw.write(row[j].getContents());
/*  48:    */                 }
/*  49:    */               }
/*  50:    */             }
/*  51: 97 */             bw.newLine();
/*  52:    */           }
/*  53:    */         }
/*  54:    */       }
/*  55:101 */       bw.flush();
/*  56:102 */       bw.close();
/*  57:    */     }
/*  58:    */     catch (UnsupportedEncodingException e)
/*  59:    */     {
/*  60:106 */       System.err.println(e.toString());
/*  61:    */     }
/*  62:    */   }
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.CSV
 * JD-Core Version:    0.7.0.1
 */