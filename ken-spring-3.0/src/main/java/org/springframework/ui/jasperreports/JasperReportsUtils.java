/*   1:    */ package org.springframework.ui.jasperreports;
/*   2:    */ 
/*   3:    */ import java.io.OutputStream;
/*   4:    */ import java.io.Writer;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Map;
/*   7:    */ import net.sf.jasperreports.engine.JRDataSource;
/*   8:    */ import net.sf.jasperreports.engine.JRException;
/*   9:    */ import net.sf.jasperreports.engine.JRExporter;
/*  10:    */ import net.sf.jasperreports.engine.JRExporterParameter;
/*  11:    */ import net.sf.jasperreports.engine.JasperFillManager;
/*  12:    */ import net.sf.jasperreports.engine.JasperPrint;
/*  13:    */ import net.sf.jasperreports.engine.JasperReport;
/*  14:    */ import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
/*  15:    */ import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/*  16:    */ import net.sf.jasperreports.engine.export.JRCsvExporter;
/*  17:    */ import net.sf.jasperreports.engine.export.JRHtmlExporter;
/*  18:    */ import net.sf.jasperreports.engine.export.JRPdfExporter;
/*  19:    */ import net.sf.jasperreports.engine.export.JRXlsExporter;
/*  20:    */ 
/*  21:    */ public abstract class JasperReportsUtils
/*  22:    */ {
/*  23:    */   public static JRDataSource convertReportData(Object value)
/*  24:    */     throws IllegalArgumentException
/*  25:    */   {
/*  26: 62 */     if ((value instanceof JRDataSource)) {
/*  27: 63 */       return (JRDataSource)value;
/*  28:    */     }
/*  29: 65 */     if ((value instanceof Collection)) {
/*  30: 66 */       return new JRBeanCollectionDataSource((Collection)value);
/*  31:    */     }
/*  32: 68 */     if ((value instanceof Object[])) {
/*  33: 69 */       return new JRBeanArrayDataSource((Object[])value);
/*  34:    */     }
/*  35: 72 */     throw new IllegalArgumentException("Value [" + value + "] cannot be converted to a JRDataSource");
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static void render(JRExporter exporter, JasperPrint print, Writer writer)
/*  39:    */     throws JRException
/*  40:    */   {
/*  41: 90 */     exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
/*  42: 91 */     exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, writer);
/*  43: 92 */     exporter.exportReport();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static void render(JRExporter exporter, JasperPrint print, OutputStream outputStream)
/*  47:    */     throws JRException
/*  48:    */   {
/*  49:109 */     exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
/*  50:110 */     exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
/*  51:111 */     exporter.exportReport();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static void renderAsCsv(JasperReport report, Map parameters, Object reportData, Writer writer)
/*  55:    */     throws JRException
/*  56:    */   {
/*  57:129 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/*  58:130 */     render(new JRCsvExporter(), print, writer);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static void renderAsCsv(JasperReport report, Map parameters, Object reportData, Writer writer, Map exporterParameters)
/*  62:    */     throws JRException
/*  63:    */   {
/*  64:149 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/*  65:150 */     JRCsvExporter exporter = new JRCsvExporter();
/*  66:151 */     exporter.setParameters(exporterParameters);
/*  67:152 */     render(exporter, print, writer);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static void renderAsHtml(JasperReport report, Map parameters, Object reportData, Writer writer)
/*  71:    */     throws JRException
/*  72:    */   {
/*  73:170 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/*  74:171 */     render(new JRHtmlExporter(), print, writer);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static void renderAsHtml(JasperReport report, Map parameters, Object reportData, Writer writer, Map exporterParameters)
/*  78:    */     throws JRException
/*  79:    */   {
/*  80:190 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/*  81:191 */     JRHtmlExporter exporter = new JRHtmlExporter();
/*  82:192 */     exporter.setParameters(exporterParameters);
/*  83:193 */     render(exporter, print, writer);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static void renderAsPdf(JasperReport report, Map parameters, Object reportData, OutputStream stream)
/*  87:    */     throws JRException
/*  88:    */   {
/*  89:211 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/*  90:212 */     render(new JRPdfExporter(), print, stream);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static void renderAsPdf(JasperReport report, Map parameters, Object reportData, OutputStream stream, Map exporterParameters)
/*  94:    */     throws JRException
/*  95:    */   {
/*  96:231 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/*  97:232 */     JRPdfExporter exporter = new JRPdfExporter();
/*  98:233 */     exporter.setParameters(exporterParameters);
/*  99:234 */     render(exporter, print, stream);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static void renderAsXls(JasperReport report, Map parameters, Object reportData, OutputStream stream)
/* 103:    */     throws JRException
/* 104:    */   {
/* 105:252 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/* 106:253 */     render(new JRXlsExporter(), print, stream);
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static void renderAsXls(JasperReport report, Map parameters, Object reportData, OutputStream stream, Map exporterParameters)
/* 110:    */     throws JRException
/* 111:    */   {
/* 112:272 */     JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
/* 113:273 */     JRXlsExporter exporter = new JRXlsExporter();
/* 114:274 */     exporter.setParameters(exporterParameters);
/* 115:275 */     render(exporter, print, stream);
/* 116:    */   }
/* 117:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.jasperreports.JasperReportsUtils
 * JD-Core Version:    0.7.0.1
 */