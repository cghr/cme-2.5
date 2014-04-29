/*   1:    */ package org.springframework.web.servlet.view.jasperreports;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Properties;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import net.sf.jasperreports.engine.JasperPrint;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.beans.BeanUtils;
/*  10:    */ import org.springframework.util.CollectionUtils;
/*  11:    */ 
/*  12:    */ public class JasperReportsMultiFormatView
/*  13:    */   extends AbstractJasperReportsView
/*  14:    */ {
/*  15:    */   public static final String DEFAULT_FORMAT_KEY = "format";
/*  16: 79 */   private String formatKey = "format";
/*  17:    */   private Map<String, Class<? extends AbstractJasperReportsView>> formatMappings;
/*  18:    */   private Properties contentDispositionMappings;
/*  19:    */   
/*  20:    */   public JasperReportsMultiFormatView()
/*  21:    */   {
/*  22: 98 */     this.formatMappings = new HashMap(4);
/*  23: 99 */     this.formatMappings.put("csv", JasperReportsCsvView.class);
/*  24:100 */     this.formatMappings.put("html", JasperReportsHtmlView.class);
/*  25:101 */     this.formatMappings.put("pdf", JasperReportsPdfView.class);
/*  26:102 */     this.formatMappings.put("xls", JasperReportsXlsView.class);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setFormatKey(String formatKey)
/*  30:    */   {
/*  31:110 */     this.formatKey = formatKey;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setFormatMappings(Map<String, Class<? extends AbstractJasperReportsView>> formatMappings)
/*  35:    */   {
/*  36:124 */     if (CollectionUtils.isEmpty(formatMappings)) {
/*  37:125 */       throw new IllegalArgumentException("'formatMappings' must not be empty");
/*  38:    */     }
/*  39:127 */     this.formatMappings = formatMappings;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setContentDispositionMappings(Properties mappings)
/*  43:    */   {
/*  44:137 */     this.contentDispositionMappings = mappings;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Properties getContentDispositionMappings()
/*  48:    */   {
/*  49:146 */     if (this.contentDispositionMappings == null) {
/*  50:147 */       this.contentDispositionMappings = new Properties();
/*  51:    */     }
/*  52:149 */     return this.contentDispositionMappings;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected boolean generatesDownloadContent()
/*  56:    */   {
/*  57:155 */     return true;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response)
/*  61:    */     throws Exception
/*  62:    */   {
/*  63:167 */     String format = (String)model.get(this.formatKey);
/*  64:168 */     if (format == null) {
/*  65:169 */       throw new IllegalArgumentException("No format format found in model");
/*  66:    */     }
/*  67:172 */     if (this.logger.isDebugEnabled()) {
/*  68:173 */       this.logger.debug("Rendering report using format mapping key [" + format + "]");
/*  69:    */     }
/*  70:176 */     Class<? extends AbstractJasperReportsView> viewClass = (Class)this.formatMappings.get(format);
/*  71:177 */     if (viewClass == null) {
/*  72:178 */       throw new IllegalArgumentException("Format discriminator [" + format + "] is not a configured mapping");
/*  73:    */     }
/*  74:181 */     if (this.logger.isDebugEnabled()) {
/*  75:182 */       this.logger.debug("Rendering report using view class [" + viewClass.getName() + "]");
/*  76:    */     }
/*  77:185 */     AbstractJasperReportsView view = (AbstractJasperReportsView)BeanUtils.instantiateClass(viewClass);
/*  78:    */     
/*  79:    */ 
/*  80:188 */     view.setExporterParameters(getExporterParameters());
/*  81:189 */     view.setConvertedExporterParameters(getConvertedExporterParameters());
/*  82:    */     
/*  83:    */ 
/*  84:192 */     populateContentDispositionIfNecessary(response, format);
/*  85:193 */     view.renderReport(populatedReport, model, response);
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void populateContentDispositionIfNecessary(HttpServletResponse response, String format)
/*  89:    */   {
/*  90:204 */     if (this.contentDispositionMappings != null)
/*  91:    */     {
/*  92:205 */       String header = this.contentDispositionMappings.getProperty(format);
/*  93:206 */       if (header != null)
/*  94:    */       {
/*  95:207 */         if (this.logger.isDebugEnabled()) {
/*  96:208 */           this.logger.debug("Setting Content-Disposition header to: [" + header + "]");
/*  97:    */         }
/*  98:210 */         response.setHeader("Content-Disposition", header);
/*  99:    */       }
/* 100:    */     }
/* 101:    */   }
/* 102:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView
 * JD-Core Version:    0.7.0.1
 */