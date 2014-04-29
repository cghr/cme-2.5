/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.OutputStreamWriter;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.UnsupportedEncodingException;
/*   9:    */ import jxl.Cell;
/*  10:    */ import jxl.CellFeatures;
/*  11:    */ import jxl.CellReferenceHelper;
/*  12:    */ import jxl.Sheet;
/*  13:    */ import jxl.Workbook;
/*  14:    */ 
/*  15:    */ public class Features
/*  16:    */ {
/*  17:    */   public Features(Workbook w, OutputStream out, String encoding)
/*  18:    */     throws IOException
/*  19:    */   {
/*  20: 54 */     if ((encoding == null) || (!encoding.equals("UnicodeBig"))) {
/*  21: 56 */       encoding = "UTF8";
/*  22:    */     }
/*  23:    */     try
/*  24:    */     {
/*  25: 61 */       OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
/*  26: 62 */       BufferedWriter bw = new BufferedWriter(osw);
/*  27: 64 */       for (int sheet = 0; sheet < w.getNumberOfSheets(); sheet++)
/*  28:    */       {
/*  29: 66 */         Sheet s = w.getSheet(sheet);
/*  30:    */         
/*  31: 68 */         bw.write(s.getName());
/*  32: 69 */         bw.newLine();
/*  33:    */         
/*  34: 71 */         Cell[] row = (Cell[])null;
/*  35: 72 */         Cell c = null;
/*  36: 74 */         for (int i = 0; i < s.getRows(); i++)
/*  37:    */         {
/*  38: 76 */           row = s.getRow(i);
/*  39: 78 */           for (int j = 0; j < row.length; j++)
/*  40:    */           {
/*  41: 80 */             c = row[j];
/*  42: 81 */             if (c.getCellFeatures() != null)
/*  43:    */             {
/*  44: 83 */               CellFeatures features = c.getCellFeatures();
/*  45: 84 */               StringBuffer sb = new StringBuffer();
/*  46: 85 */               CellReferenceHelper.getCellReference(
/*  47: 86 */                 c.getColumn(), c.getRow(), sb);
/*  48:    */               
/*  49: 88 */               bw.write("Cell " + sb.toString() + 
/*  50: 89 */                 " contents:  " + c.getContents());
/*  51: 90 */               bw.flush();
/*  52: 91 */               bw.write(" comment: " + features.getComment());
/*  53: 92 */               bw.flush();
/*  54: 93 */               bw.newLine();
/*  55:    */             }
/*  56:    */           }
/*  57:    */         }
/*  58:    */       }
/*  59: 98 */       bw.flush();
/*  60: 99 */       bw.close();
/*  61:    */     }
/*  62:    */     catch (UnsupportedEncodingException e)
/*  63:    */     {
/*  64:103 */       System.err.println(e.toString());
/*  65:    */     }
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.Features
 * JD-Core Version:    0.7.0.1
 */