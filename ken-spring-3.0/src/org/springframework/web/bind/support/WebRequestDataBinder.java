/*   1:    */ package org.springframework.web.bind.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.MutablePropertyValues;
/*   4:    */ import org.springframework.validation.BindException;
/*   5:    */ import org.springframework.validation.BindingResult;
/*   6:    */ import org.springframework.web.bind.WebDataBinder;
/*   7:    */ import org.springframework.web.context.request.NativeWebRequest;
/*   8:    */ import org.springframework.web.context.request.WebRequest;
/*   9:    */ import org.springframework.web.multipart.MultipartRequest;
/*  10:    */ 
/*  11:    */ public class WebRequestDataBinder
/*  12:    */   extends WebDataBinder
/*  13:    */ {
/*  14:    */   public WebRequestDataBinder(Object target)
/*  15:    */   {
/*  16: 69 */     super(target);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public WebRequestDataBinder(Object target, String objectName)
/*  20:    */   {
/*  21: 79 */     super(target, objectName);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void bind(WebRequest request)
/*  25:    */   {
/*  26:102 */     MutablePropertyValues mpvs = new MutablePropertyValues(request.getParameterMap());
/*  27:103 */     if ((request instanceof NativeWebRequest))
/*  28:    */     {
/*  29:104 */       MultipartRequest multipartRequest = (MultipartRequest)((NativeWebRequest)request).getNativeRequest(MultipartRequest.class);
/*  30:105 */       if (multipartRequest != null) {
/*  31:106 */         bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
/*  32:    */       }
/*  33:    */     }
/*  34:109 */     doBind(mpvs);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void closeNoCatch()
/*  38:    */     throws BindException
/*  39:    */   {
/*  40:119 */     if (getBindingResult().hasErrors()) {
/*  41:120 */       throw new BindException(getBindingResult());
/*  42:    */     }
/*  43:    */   }
/*  44:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.WebRequestDataBinder
 * JD-Core Version:    0.7.0.1
 */