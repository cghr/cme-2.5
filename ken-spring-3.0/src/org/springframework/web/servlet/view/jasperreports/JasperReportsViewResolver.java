/*   1:    */ package org.springframework.web.servlet.view.jasperreports;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Properties;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*   8:    */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*   9:    */ 
/*  10:    */ public class JasperReportsViewResolver
/*  11:    */   extends UrlBasedViewResolver
/*  12:    */ {
/*  13:    */   private String reportDataKey;
/*  14:    */   private Properties subReportUrls;
/*  15:    */   private String[] subReportDataKeys;
/*  16:    */   private Properties headers;
/*  17: 45 */   private Map<String, Object> exporterParameters = new HashMap();
/*  18:    */   private DataSource jdbcDataSource;
/*  19:    */   
/*  20:    */   protected Class requiredViewClass()
/*  21:    */   {
/*  22: 55 */     return AbstractJasperReportsView.class;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setReportDataKey(String reportDataKey)
/*  26:    */   {
/*  27: 63 */     this.reportDataKey = reportDataKey;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setSubReportUrls(Properties subReportUrls)
/*  31:    */   {
/*  32: 71 */     this.subReportUrls = subReportUrls;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setSubReportDataKeys(String[] subReportDataKeys)
/*  36:    */   {
/*  37: 79 */     this.subReportDataKeys = subReportDataKeys;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setHeaders(Properties headers)
/*  41:    */   {
/*  42: 87 */     this.headers = headers;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setExporterParameters(Map<String, Object> exporterParameters)
/*  46:    */   {
/*  47: 95 */     this.exporterParameters = exporterParameters;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setJdbcDataSource(DataSource jdbcDataSource)
/*  51:    */   {
/*  52:103 */     this.jdbcDataSource = jdbcDataSource;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected AbstractUrlBasedView buildView(String viewName)
/*  56:    */     throws Exception
/*  57:    */   {
/*  58:109 */     AbstractJasperReportsView view = (AbstractJasperReportsView)super.buildView(viewName);
/*  59:110 */     view.setReportDataKey(this.reportDataKey);
/*  60:111 */     view.setSubReportUrls(this.subReportUrls);
/*  61:112 */     view.setSubReportDataKeys(this.subReportDataKeys);
/*  62:113 */     view.setHeaders(this.headers);
/*  63:114 */     view.setExporterParameters(this.exporterParameters);
/*  64:115 */     view.setJdbcDataSource(this.jdbcDataSource);
/*  65:116 */     return view;
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver
 * JD-Core Version:    0.7.0.1
 */