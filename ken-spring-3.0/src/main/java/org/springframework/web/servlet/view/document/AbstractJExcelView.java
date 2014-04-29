/*   1:    */ package org.springframework.web.servlet.view.document;
/*   2:    */ 
/*   3:    */ import java.io.OutputStream;
/*   4:    */ import java.util.Locale;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import jxl.Workbook;
/*   9:    */ import jxl.write.WritableWorkbook;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.core.io.Resource;
/*  12:    */ import org.springframework.core.io.support.LocalizedResourceHelper;
/*  13:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  14:    */ import org.springframework.web.servlet.view.AbstractView;
/*  15:    */ 
/*  16:    */ public abstract class AbstractJExcelView
/*  17:    */   extends AbstractView
/*  18:    */ {
/*  19:    */   private static final String CONTENT_TYPE = "application/vnd.ms-excel";
/*  20:    */   private static final String EXTENSION = ".xls";
/*  21:    */   private String url;
/*  22:    */   
/*  23:    */   public AbstractJExcelView()
/*  24:    */   {
/*  25:101 */     setContentType("application/vnd.ms-excel");
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setUrl(String url)
/*  29:    */   {
/*  30:108 */     this.url = url;
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected boolean generatesDownloadContent()
/*  34:    */   {
/*  35:114 */     return true;
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  39:    */     throws Exception
/*  40:    */   {
/*  41:125 */     response.setContentType(getContentType());
/*  42:126 */     OutputStream out = response.getOutputStream();
/*  43:    */     WritableWorkbook workbook;
/*  44:    */     WritableWorkbook workbook;
/*  45:129 */     if (this.url != null)
/*  46:    */     {
/*  47:130 */       Workbook template = getTemplateSource(this.url, request);
/*  48:131 */       workbook = Workbook.createWorkbook(out, template);
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52:134 */       this.logger.debug("Creating Excel Workbook from scratch");
/*  53:135 */       workbook = Workbook.createWorkbook(out);
/*  54:    */     }
/*  55:138 */     buildExcelDocument(model, workbook, request, response);
/*  56:    */     
/*  57:    */ 
/*  58:    */ 
/*  59:    */ 
/*  60:143 */     workbook.write();
/*  61:144 */     out.flush();
/*  62:145 */     workbook.close();
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected Workbook getTemplateSource(String url, HttpServletRequest request)
/*  66:    */     throws Exception
/*  67:    */   {
/*  68:156 */     LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
/*  69:157 */     Locale userLocale = RequestContextUtils.getLocale(request);
/*  70:158 */     Resource inputFile = helper.findLocalizedResource(url, ".xls", userLocale);
/*  71:161 */     if (this.logger.isDebugEnabled()) {
/*  72:162 */       this.logger.debug("Loading Excel workbook from " + inputFile);
/*  73:    */     }
/*  74:164 */     return Workbook.getWorkbook(inputFile.getInputStream());
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected abstract void buildExcelDocument(Map<String, Object> paramMap, WritableWorkbook paramWritableWorkbook, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*  78:    */     throws Exception;
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.document.AbstractJExcelView
 * JD-Core Version:    0.7.0.1
 */