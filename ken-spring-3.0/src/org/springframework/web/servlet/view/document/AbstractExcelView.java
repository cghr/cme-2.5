/*   1:    */ package org.springframework.web.servlet.view.document;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletOutputStream;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.poi.hssf.usermodel.HSSFCell;
/*  10:    */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*  11:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  12:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  13:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  14:    */ import org.springframework.core.io.Resource;
/*  15:    */ import org.springframework.core.io.support.LocalizedResourceHelper;
/*  16:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  17:    */ import org.springframework.web.servlet.view.AbstractView;
/*  18:    */ 
/*  19:    */ public abstract class AbstractExcelView
/*  20:    */   extends AbstractView
/*  21:    */ {
/*  22:    */   private static final String CONTENT_TYPE = "application/vnd.ms-excel";
/*  23:    */   private static final String EXTENSION = ".xls";
/*  24:    */   private String url;
/*  25:    */   
/*  26:    */   public AbstractExcelView()
/*  27:    */   {
/*  28:113 */     setContentType("application/vnd.ms-excel");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setUrl(String url)
/*  32:    */   {
/*  33:120 */     this.url = url;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected boolean generatesDownloadContent()
/*  37:    */   {
/*  38:126 */     return true;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  42:    */     throws Exception
/*  43:    */   {
/*  44:    */     HSSFWorkbook workbook;
/*  45:    */     HSSFWorkbook workbook;
/*  46:137 */     if (this.url != null)
/*  47:    */     {
/*  48:138 */       workbook = getTemplateSource(this.url, request);
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52:141 */       workbook = new HSSFWorkbook();
/*  53:142 */       this.logger.debug("Created Excel Workbook from scratch");
/*  54:    */     }
/*  55:145 */     buildExcelDocument(model, workbook, request, response);
/*  56:    */     
/*  57:    */ 
/*  58:148 */     response.setContentType(getContentType());
/*  59:    */     
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:154 */     ServletOutputStream out = response.getOutputStream();
/*  65:155 */     workbook.write(out);
/*  66:156 */     out.flush();
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected HSSFWorkbook getTemplateSource(String url, HttpServletRequest request)
/*  70:    */     throws Exception
/*  71:    */   {
/*  72:167 */     LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
/*  73:168 */     Locale userLocale = RequestContextUtils.getLocale(request);
/*  74:169 */     Resource inputFile = helper.findLocalizedResource(url, ".xls", userLocale);
/*  75:172 */     if (this.logger.isDebugEnabled()) {
/*  76:173 */       this.logger.debug("Loading Excel workbook from " + inputFile);
/*  77:    */     }
/*  78:175 */     POIFSFileSystem fs = new POIFSFileSystem(inputFile.getInputStream());
/*  79:176 */     return new HSSFWorkbook(fs);
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected abstract void buildExcelDocument(Map<String, Object> paramMap, HSSFWorkbook paramHSSFWorkbook, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*  83:    */     throws Exception;
/*  84:    */   
/*  85:    */   protected HSSFCell getCell(HSSFSheet sheet, int row, int col)
/*  86:    */   {
/*  87:202 */     HSSFRow sheetRow = sheet.getRow(row);
/*  88:203 */     if (sheetRow == null) {
/*  89:204 */       sheetRow = sheet.createRow(row);
/*  90:    */     }
/*  91:206 */     HSSFCell cell = sheetRow.getCell((short)col);
/*  92:207 */     if (cell == null) {
/*  93:208 */       cell = sheetRow.createCell((short)col);
/*  94:    */     }
/*  95:210 */     return cell;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void setText(HSSFCell cell, String text)
/*  99:    */   {
/* 100:219 */     cell.setCellType(1);
/* 101:220 */     cell.setCellValue(text);
/* 102:    */   }
/* 103:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.document.AbstractExcelView
 * JD-Core Version:    0.7.0.1
 */