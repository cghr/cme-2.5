/*  1:   */ package jxl.demo;
/*  2:   */ 
/*  3:   */ import java.io.BufferedWriter;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import java.io.OutputStreamWriter;
/*  7:   */ import java.io.PrintStream;
/*  8:   */ import java.io.UnsupportedEncodingException;
/*  9:   */ import jxl.Workbook;
/* 10:   */ import jxl.biff.drawing.DrawingGroup;
/* 11:   */ import jxl.biff.drawing.EscherDisplay;
/* 12:   */ import jxl.read.biff.WorkbookParser;
/* 13:   */ 
/* 14:   */ public class EscherDrawingGroup
/* 15:   */ {
/* 16:   */   public EscherDrawingGroup(Workbook w, OutputStream out, String encoding)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:50 */     if ((encoding == null) || (!encoding.equals("UnicodeBig"))) {
/* 20:52 */       encoding = "UTF8";
/* 21:   */     }
/* 22:   */     try
/* 23:   */     {
/* 24:57 */       OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
/* 25:58 */       BufferedWriter bw = new BufferedWriter(osw);
/* 26:   */       
/* 27:60 */       WorkbookParser wp = (WorkbookParser)w;
/* 28:   */       
/* 29:62 */       DrawingGroup dg = wp.getDrawingGroup();
/* 30:64 */       if (dg != null)
/* 31:   */       {
/* 32:66 */         EscherDisplay ed = new EscherDisplay(dg, bw);
/* 33:67 */         ed.display();
/* 34:   */       }
/* 35:70 */       bw.newLine();
/* 36:71 */       bw.newLine();
/* 37:72 */       bw.flush();
/* 38:73 */       bw.close();
/* 39:   */     }
/* 40:   */     catch (UnsupportedEncodingException e)
/* 41:   */     {
/* 42:77 */       System.err.println(e.toString());
/* 43:   */     }
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.EscherDrawingGroup
 * JD-Core Version:    0.7.0.1
 */