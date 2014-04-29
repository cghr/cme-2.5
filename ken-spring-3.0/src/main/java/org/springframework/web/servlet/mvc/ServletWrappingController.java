/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import java.util.Enumeration;
/*   4:    */ import java.util.Properties;
/*   5:    */ import javax.servlet.Servlet;
/*   6:    */ import javax.servlet.ServletConfig;
/*   7:    */ import javax.servlet.ServletContext;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ import org.springframework.beans.factory.BeanNameAware;
/*  11:    */ import org.springframework.beans.factory.DisposableBean;
/*  12:    */ import org.springframework.beans.factory.InitializingBean;
/*  13:    */ import org.springframework.web.servlet.ModelAndView;
/*  14:    */ 
/*  15:    */ public class ServletWrappingController
/*  16:    */   extends AbstractController
/*  17:    */   implements BeanNameAware, InitializingBean, DisposableBean
/*  18:    */ {
/*  19:    */   private Class servletClass;
/*  20:    */   private String servletName;
/*  21: 94 */   private Properties initParameters = new Properties();
/*  22:    */   private String beanName;
/*  23:    */   private Servlet servletInstance;
/*  24:    */   
/*  25:    */   public void setServletClass(Class servletClass)
/*  26:    */   {
/*  27:107 */     this.servletClass = servletClass;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setServletName(String servletName)
/*  31:    */   {
/*  32:115 */     this.servletName = servletName;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setInitParameters(Properties initParameters)
/*  36:    */   {
/*  37:123 */     this.initParameters = initParameters;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setBeanName(String name)
/*  41:    */   {
/*  42:127 */     this.beanName = name;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void afterPropertiesSet()
/*  46:    */     throws Exception
/*  47:    */   {
/*  48:136 */     if (this.servletClass == null) {
/*  49:137 */       throw new IllegalArgumentException("servletClass is required");
/*  50:    */     }
/*  51:139 */     if (!Servlet.class.isAssignableFrom(this.servletClass)) {
/*  52:140 */       throw new IllegalArgumentException("servletClass [" + this.servletClass.getName() + 
/*  53:141 */         "] needs to implement interface [javax.servlet.Servlet]");
/*  54:    */     }
/*  55:143 */     if (this.servletName == null) {
/*  56:144 */       this.servletName = this.beanName;
/*  57:    */     }
/*  58:146 */     this.servletInstance = ((Servlet)this.servletClass.newInstance());
/*  59:147 */     this.servletInstance.init(new DelegatingServletConfig(null));
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*  63:    */     throws Exception
/*  64:    */   {
/*  65:159 */     this.servletInstance.service(request, response);
/*  66:160 */     return null;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void destroy()
/*  70:    */   {
/*  71:169 */     this.servletInstance.destroy();
/*  72:    */   }
/*  73:    */   
/*  74:    */   private class DelegatingServletConfig
/*  75:    */     implements ServletConfig
/*  76:    */   {
/*  77:    */     private DelegatingServletConfig() {}
/*  78:    */     
/*  79:    */     public String getServletName()
/*  80:    */     {
/*  81:181 */       return ServletWrappingController.this.servletName;
/*  82:    */     }
/*  83:    */     
/*  84:    */     public ServletContext getServletContext()
/*  85:    */     {
/*  86:185 */       return ServletWrappingController.this.getServletContext();
/*  87:    */     }
/*  88:    */     
/*  89:    */     public String getInitParameter(String paramName)
/*  90:    */     {
/*  91:189 */       return ServletWrappingController.this.initParameters.getProperty(paramName);
/*  92:    */     }
/*  93:    */     
/*  94:    */     public Enumeration getInitParameterNames()
/*  95:    */     {
/*  96:193 */       return ServletWrappingController.this.initParameters.keys();
/*  97:    */     }
/*  98:    */   }
/*  99:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.ServletWrappingController
 * JD-Core Version:    0.7.0.1
 */