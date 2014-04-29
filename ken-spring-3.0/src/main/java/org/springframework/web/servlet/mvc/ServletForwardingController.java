/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import javax.servlet.RequestDispatcher;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.beans.factory.BeanNameAware;
/*  10:    */ import org.springframework.web.servlet.ModelAndView;
/*  11:    */ import org.springframework.web.util.WebUtils;
/*  12:    */ 
/*  13:    */ public class ServletForwardingController
/*  14:    */   extends AbstractController
/*  15:    */   implements BeanNameAware
/*  16:    */ {
/*  17:    */   private String servletName;
/*  18:    */   private String beanName;
/*  19:    */   
/*  20:    */   public void setServletName(String servletName)
/*  21:    */   {
/*  22:101 */     this.servletName = servletName;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setBeanName(String name)
/*  26:    */   {
/*  27:105 */     this.beanName = name;
/*  28:106 */     if (this.servletName == null) {
/*  29:107 */       this.servletName = name;
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*  34:    */     throws Exception
/*  35:    */   {
/*  36:116 */     RequestDispatcher rd = getServletContext().getNamedDispatcher(this.servletName);
/*  37:117 */     if (rd == null) {
/*  38:118 */       throw new ServletException("No servlet with name '" + this.servletName + "' defined in web.xml");
/*  39:    */     }
/*  40:121 */     if (useInclude(request, response))
/*  41:    */     {
/*  42:122 */       rd.include(request, response);
/*  43:123 */       if (this.logger.isDebugEnabled()) {
/*  44:124 */         this.logger.debug("Included servlet [" + this.servletName + 
/*  45:125 */           "] in ServletForwardingController '" + this.beanName + "'");
/*  46:    */       }
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50:129 */       rd.forward(request, response);
/*  51:130 */       if (this.logger.isDebugEnabled()) {
/*  52:131 */         this.logger.debug("Forwarded to servlet [" + this.servletName + 
/*  53:132 */           "] in ServletForwardingController '" + this.beanName + "'");
/*  54:    */       }
/*  55:    */     }
/*  56:135 */     return null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected boolean useInclude(HttpServletRequest request, HttpServletResponse response)
/*  60:    */   {
/*  61:153 */     return (WebUtils.isIncludeRequest(request)) || (response.isCommitted());
/*  62:    */   }
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.ServletForwardingController
 * JD-Core Version:    0.7.0.1
 */