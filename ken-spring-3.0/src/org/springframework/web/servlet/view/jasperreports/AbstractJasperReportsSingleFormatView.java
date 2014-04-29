/*   1:    */ package org.springframework.web.servlet.view.jasperreports;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.http.HttpServletResponse;
/*   6:    */ import net.sf.jasperreports.engine.JRExporter;
/*   7:    */ import net.sf.jasperreports.engine.JRExporterParameter;
/*   8:    */ import net.sf.jasperreports.engine.JasperPrint;
/*   9:    */ import org.springframework.ui.jasperreports.JasperReportsUtils;
/*  10:    */ import org.springframework.util.CollectionUtils;
/*  11:    */ 
/*  12:    */ public abstract class AbstractJasperReportsSingleFormatView
/*  13:    */   extends AbstractJasperReportsView
/*  14:    */ {
/*  15:    */   protected boolean generatesDownloadContent()
/*  16:    */   {
/*  17: 49 */     return !useWriter();
/*  18:    */   }
/*  19:    */   
/*  20:    */   protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response)
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 60 */     JRExporter exporter = createExporter();
/*  24:    */     
/*  25: 62 */     Map mergedExporterParameters = getConvertedExporterParameters();
/*  26: 63 */     if (!CollectionUtils.isEmpty(mergedExporterParameters)) {
/*  27: 64 */       exporter.setParameters(mergedExporterParameters);
/*  28:    */     }
/*  29: 67 */     if (useWriter()) {
/*  30: 68 */       renderReportUsingWriter(exporter, populatedReport, response);
/*  31:    */     } else {
/*  32: 71 */       renderReportUsingOutputStream(exporter, populatedReport, response);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected void renderReportUsingWriter(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response)
/*  37:    */     throws Exception
/*  38:    */   {
/*  39: 86 */     String contentType = getContentType();
/*  40: 87 */     String encoding = (String)exporter.getParameter(JRExporterParameter.CHARACTER_ENCODING);
/*  41: 88 */     if (encoding != null) {
/*  42: 90 */       if ((contentType != null) && (!contentType.toLowerCase().contains(";charset="))) {
/*  43: 91 */         contentType = contentType + ";charset=" + encoding;
/*  44:    */       }
/*  45:    */     }
/*  46: 94 */     response.setContentType(contentType);
/*  47:    */     
/*  48:    */ 
/*  49: 97 */     JasperReportsUtils.render(exporter, populatedReport, response.getWriter());
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected void renderReportUsingOutputStream(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response)
/*  53:    */     throws Exception
/*  54:    */   {
/*  55:111 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/*  56:112 */     JasperReportsUtils.render(exporter, populatedReport, baos);
/*  57:113 */     writeToResponse(response, baos);
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected abstract JRExporter createExporter();
/*  61:    */   
/*  62:    */   protected abstract boolean useWriter();
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView
 * JD-Core Version:    0.7.0.1
 */