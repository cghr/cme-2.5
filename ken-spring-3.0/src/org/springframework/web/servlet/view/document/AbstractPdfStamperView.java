/*  1:   */ package org.springframework.web.servlet.view.document;
/*  2:   */ 
/*  3:   */ import com.lowagie.text.pdf.PdfReader;
/*  4:   */ import com.lowagie.text.pdf.PdfStamper;
/*  5:   */ import java.io.ByteArrayOutputStream;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.util.Map;
/*  8:   */ import javax.servlet.http.HttpServletRequest;
/*  9:   */ import javax.servlet.http.HttpServletResponse;
/* 10:   */ import org.springframework.context.ApplicationContext;
/* 11:   */ import org.springframework.core.io.Resource;
/* 12:   */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/* 13:   */ 
/* 14:   */ public abstract class AbstractPdfStamperView
/* 15:   */   extends AbstractUrlBasedView
/* 16:   */ {
/* 17:   */   public AbstractPdfStamperView()
/* 18:   */   {
/* 19:45 */     setContentType("application/pdf");
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected boolean generatesDownloadContent()
/* 23:   */   {
/* 24:51 */     return true;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 28:   */     throws Exception
/* 29:   */   {
/* 30:59 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/* 31:   */     
/* 32:61 */     PdfReader reader = readPdfResource();
/* 33:62 */     PdfStamper stamper = new PdfStamper(reader, baos);
/* 34:63 */     mergePdfDocument(model, stamper, request, response);
/* 35:64 */     stamper.close();
/* 36:   */     
/* 37:   */ 
/* 38:67 */     writeToResponse(response, baos);
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected PdfReader readPdfResource()
/* 42:   */     throws IOException
/* 43:   */   {
/* 44:79 */     return new PdfReader(getApplicationContext().getResource(getUrl()).getInputStream());
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected abstract void mergePdfDocument(Map<String, Object> paramMap, PdfStamper paramPdfStamper, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 48:   */     throws Exception;
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.document.AbstractPdfStamperView
 * JD-Core Version:    0.7.0.1
 */