/*   1:    */ package org.springframework.web.servlet.view.document;
/*   2:    */ 
/*   3:    */ import com.lowagie.text.Document;
/*   4:    */ import com.lowagie.text.DocumentException;
/*   5:    */ import com.lowagie.text.PageSize;
/*   6:    */ import com.lowagie.text.pdf.PdfWriter;
/*   7:    */ import java.io.ByteArrayOutputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.util.Map;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ import org.springframework.web.servlet.view.AbstractView;
/*  13:    */ 
/*  14:    */ public abstract class AbstractPdfView
/*  15:    */   extends AbstractView
/*  16:    */ {
/*  17:    */   public AbstractPdfView()
/*  18:    */   {
/*  19: 54 */     setContentType("application/pdf");
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected boolean generatesDownloadContent()
/*  23:    */   {
/*  24: 60 */     return true;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  28:    */     throws Exception
/*  29:    */   {
/*  30: 68 */     ByteArrayOutputStream baos = createTemporaryOutputStream();
/*  31:    */     
/*  32:    */ 
/*  33: 71 */     Document document = newDocument();
/*  34: 72 */     PdfWriter writer = newWriter(document, baos);
/*  35: 73 */     prepareWriter(model, writer, request);
/*  36: 74 */     buildPdfMetadata(model, document, request);
/*  37:    */     
/*  38:    */ 
/*  39: 77 */     document.open();
/*  40: 78 */     buildPdfDocument(model, document, writer, request, response);
/*  41: 79 */     document.close();
/*  42:    */     
/*  43:    */ 
/*  44: 82 */     writeToResponse(response, baos);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Document newDocument()
/*  48:    */   {
/*  49: 93 */     return new Document(PageSize.A4);
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected PdfWriter newWriter(Document document, OutputStream os)
/*  53:    */     throws DocumentException
/*  54:    */   {
/*  55:104 */     return PdfWriter.getInstance(document, os);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
/*  59:    */     throws DocumentException
/*  60:    */   {
/*  61:125 */     writer.setViewerPreferences(getViewerPreferences());
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected int getViewerPreferences()
/*  65:    */   {
/*  66:139 */     return 2053;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {}
/*  70:    */   
/*  71:    */   protected abstract void buildPdfDocument(Map<String, Object> paramMap, Document paramDocument, PdfWriter paramPdfWriter, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*  72:    */     throws Exception;
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.document.AbstractPdfView
 * JD-Core Version:    0.7.0.1
 */