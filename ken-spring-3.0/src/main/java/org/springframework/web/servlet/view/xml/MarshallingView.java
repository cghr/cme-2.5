/*   1:    */ package org.springframework.web.servlet.view.xml;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import javax.xml.transform.stream.StreamResult;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.oxm.Marshaller;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.FileCopyUtils;
/*  13:    */ import org.springframework.web.servlet.view.AbstractView;
/*  14:    */ 
/*  15:    */ public class MarshallingView
/*  16:    */   extends AbstractView
/*  17:    */ {
/*  18:    */   public static final String DEFAULT_CONTENT_TYPE = "application/xml";
/*  19:    */   private Marshaller marshaller;
/*  20:    */   private String modelKey;
/*  21:    */   
/*  22:    */   public MarshallingView()
/*  23:    */   {
/*  24: 61 */     setContentType("application/xml");
/*  25: 62 */     setExposePathVariables(false);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public MarshallingView(Marshaller marshaller)
/*  29:    */   {
/*  30: 69 */     Assert.notNull(marshaller, "'marshaller' must not be null");
/*  31: 70 */     setContentType("application/xml");
/*  32: 71 */     this.marshaller = marshaller;
/*  33: 72 */     setExposePathVariables(false);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setMarshaller(Marshaller marshaller)
/*  37:    */   {
/*  38: 79 */     Assert.notNull(marshaller, "'marshaller' must not be null");
/*  39: 80 */     this.marshaller = marshaller;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setModelKey(String modelKey)
/*  43:    */   {
/*  44: 90 */     this.modelKey = modelKey;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void initApplicationContext()
/*  48:    */     throws BeansException
/*  49:    */   {
/*  50: 95 */     Assert.notNull(this.marshaller, "Property 'marshaller' is required");
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  54:    */     throws Exception
/*  55:    */   {
/*  56:102 */     Object toBeMarshalled = locateToBeMarshalled(model);
/*  57:103 */     if (toBeMarshalled == null) {
/*  58:104 */       throw new ServletException("Unable to locate object to be marshalled in model: " + model);
/*  59:    */     }
/*  60:106 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
/*  61:107 */     this.marshaller.marshal(toBeMarshalled, new StreamResult(bos));
/*  62:    */     
/*  63:109 */     response.setContentType(getContentType());
/*  64:110 */     response.setContentLength(bos.size());
/*  65:    */     
/*  66:112 */     FileCopyUtils.copy(bos.toByteArray(), response.getOutputStream());
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected Object locateToBeMarshalled(Map<String, Object> model)
/*  70:    */     throws ServletException
/*  71:    */   {
/*  72:127 */     if (this.modelKey != null)
/*  73:    */     {
/*  74:128 */       Object o = model.get(this.modelKey);
/*  75:129 */       if (o == null) {
/*  76:130 */         throw new ServletException("Model contains no object with key [" + this.modelKey + "]");
/*  77:    */       }
/*  78:132 */       if (!this.marshaller.supports(o.getClass())) {
/*  79:133 */         throw new ServletException("Model object [" + o + "] retrieved via key [" + this.modelKey + 
/*  80:134 */           "] is not supported by the Marshaller");
/*  81:    */       }
/*  82:136 */       return o;
/*  83:    */     }
/*  84:138 */     for (Object o : model.values()) {
/*  85:139 */       if ((o != null) && (this.marshaller.supports(o.getClass()))) {
/*  86:140 */         return o;
/*  87:    */       }
/*  88:    */     }
/*  89:143 */     return null;
/*  90:    */   }
/*  91:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.xml.MarshallingView
 * JD-Core Version:    0.7.0.1
 */