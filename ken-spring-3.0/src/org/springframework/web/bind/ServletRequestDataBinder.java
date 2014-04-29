/*   1:    */ package org.springframework.web.bind;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletRequest;
/*   4:    */ import org.springframework.beans.MutablePropertyValues;
/*   5:    */ import org.springframework.validation.BindException;
/*   6:    */ import org.springframework.validation.BindingResult;
/*   7:    */ import org.springframework.web.multipart.MultipartRequest;
/*   8:    */ import org.springframework.web.util.WebUtils;
/*   9:    */ 
/*  10:    */ public class ServletRequestDataBinder
/*  11:    */   extends WebDataBinder
/*  12:    */ {
/*  13:    */   public ServletRequestDataBinder(Object target)
/*  14:    */   {
/*  15: 73 */     super(target);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public ServletRequestDataBinder(Object target, String objectName)
/*  19:    */   {
/*  20: 83 */     super(target, objectName);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void bind(ServletRequest request)
/*  24:    */   {
/*  25:106 */     MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
/*  26:107 */     MultipartRequest multipartRequest = (MultipartRequest)WebUtils.getNativeRequest(request, MultipartRequest.class);
/*  27:108 */     if (multipartRequest != null) {
/*  28:109 */       bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
/*  29:    */     }
/*  30:111 */     addBindValues(mpvs, request);
/*  31:112 */     doBind(mpvs);
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {}
/*  35:    */   
/*  36:    */   public void closeNoCatch()
/*  37:    */     throws ServletRequestBindingException
/*  38:    */   {
/*  39:132 */     if (getBindingResult().hasErrors()) {
/*  40:133 */       throw new ServletRequestBindingException(
/*  41:134 */         "Errors binding onto object '" + getBindingResult().getObjectName() + "'", 
/*  42:135 */         new BindException(getBindingResult()));
/*  43:    */     }
/*  44:    */   }
/*  45:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.ServletRequestDataBinder
 * JD-Core Version:    0.7.0.1
 */