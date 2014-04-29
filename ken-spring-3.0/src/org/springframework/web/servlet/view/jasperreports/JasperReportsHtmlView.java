/*  1:   */ package org.springframework.web.servlet.view.jasperreports;
/*  2:   */ 
/*  3:   */ import net.sf.jasperreports.engine.JRExporter;
/*  4:   */ import net.sf.jasperreports.engine.export.JRHtmlExporter;
/*  5:   */ 
/*  6:   */ public class JasperReportsHtmlView
/*  7:   */   extends AbstractJasperReportsSingleFormatView
/*  8:   */ {
/*  9:   */   public JasperReportsHtmlView()
/* 10:   */   {
/* 11:33 */     setContentType("text/html");
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected JRExporter createExporter()
/* 15:   */   {
/* 16:38 */     return new JRHtmlExporter();
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected boolean useWriter()
/* 20:   */   {
/* 21:43 */     return true;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView
 * JD-Core Version:    0.7.0.1
 */