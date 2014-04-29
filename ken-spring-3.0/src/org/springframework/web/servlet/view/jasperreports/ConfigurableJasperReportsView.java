/*  1:   */ package org.springframework.web.servlet.view.jasperreports;
/*  2:   */ 
/*  3:   */ import net.sf.jasperreports.engine.JRExporter;
/*  4:   */ import org.springframework.beans.BeanUtils;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class ConfigurableJasperReportsView
/*  8:   */   extends AbstractJasperReportsSingleFormatView
/*  9:   */ {
/* 10:   */   private Class<? extends JRExporter> exporterClass;
/* 11:39 */   private boolean useWriter = true;
/* 12:   */   
/* 13:   */   public void setExporterClass(Class<? extends JRExporter> exporterClass)
/* 14:   */   {
/* 15:48 */     Assert.isAssignable(JRExporter.class, exporterClass);
/* 16:49 */     this.exporterClass = exporterClass;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setUseWriter(boolean useWriter)
/* 20:   */   {
/* 21:58 */     this.useWriter = useWriter;
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected void onInit()
/* 25:   */   {
/* 26:66 */     if (this.exporterClass == null) {
/* 27:67 */       throw new IllegalArgumentException("exporterClass is required");
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected JRExporter createExporter()
/* 32:   */   {
/* 33:79 */     return (JRExporter)BeanUtils.instantiateClass(this.exporterClass);
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected boolean useWriter()
/* 37:   */   {
/* 38:88 */     return this.useWriter;
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.jasperreports.ConfigurableJasperReportsView
 * JD-Core Version:    0.7.0.1
 */