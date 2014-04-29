/*  1:   */ package jxl.demo;
/*  2:   */ 
/*  3:   */ import java.io.BufferedWriter;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import java.io.OutputStreamWriter;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import java.io.UnsupportedEncodingException;
/*  9:   */ import jxl.Workbook;
/* 10:   */ import jxl.biff.drawing.DrawingData;
/* 11:   */ import jxl.biff.drawing.EscherDisplay;
/* 12:   */ import jxl.read.biff.SheetImpl;
/* 13:   */ 
/* 14:   */ public class Escher
/* 15:   */ {
/* 16:   */   public Escher(Workbook w, OutputStream out, String encoding)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:50 */     if ((encoding == null) || (!encoding.equals("UnicodeBig"))) {
/* 20:52 */       encoding = "UTF8";
/* 21:   */     }
/* 22:   */     try
/* 23:   */     {
/* 24:57 */       OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
/* 25:58 */       BufferedWriter bw = new BufferedWriter(osw);
/* 26:60 */       for (int i = 0; i < w.getNumberOfSheets(); i++)
/* 27:   */       {
/* 28:62 */         SheetImpl s = (SheetImpl)w.getSheet(i);
/* 29:63 */         bw.write(s.getName());
/* 30:64 */         bw.newLine();
/* 31:65 */         bw.newLine();
/* 32:   */         
/* 33:67 */         DrawingData dd = s.getDrawingData();
/* 34:69 */         if (dd != null)
/* 35:   */         {
/* 36:71 */           EscherDisplay ed = new EscherDisplay(dd, bw);
/* 37:72 */           ed.display();
/* 38:   */         }
/* 39:75 */         bw.newLine();
/* 40:76 */         bw.newLine();
/* 41:77 */         bw.flush();
/* 42:   */       }
/* 43:79 */       bw.flush();
/* 44:80 */       bw.close();
/* 45:   */     }
/* 46:   */     catch (UnsupportedEncodingException e)
/* 47:   */     {
/* 48:84 */       System.err.println(e.toString());
/* 49:   */     }
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.Escher
 * JD-Core Version:    0.7.0.1
 */