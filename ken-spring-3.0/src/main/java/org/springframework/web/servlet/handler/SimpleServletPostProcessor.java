/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Enumeration;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import javax.servlet.Servlet;
/*   7:    */ import javax.servlet.ServletConfig;
/*   8:    */ import javax.servlet.ServletContext;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import org.springframework.beans.BeansException;
/*  11:    */ import org.springframework.beans.factory.BeanInitializationException;
/*  12:    */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*  13:    */ import org.springframework.web.context.ServletConfigAware;
/*  14:    */ import org.springframework.web.context.ServletContextAware;
/*  15:    */ 
/*  16:    */ public class SimpleServletPostProcessor
/*  17:    */   implements DestructionAwareBeanPostProcessor, ServletContextAware, ServletConfigAware
/*  18:    */ {
/*  19: 70 */   private boolean useSharedServletConfig = true;
/*  20:    */   private ServletContext servletContext;
/*  21:    */   private ServletConfig servletConfig;
/*  22:    */   
/*  23:    */   public void setUseSharedServletConfig(boolean useSharedServletConfig)
/*  24:    */   {
/*  25: 86 */     this.useSharedServletConfig = useSharedServletConfig;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setServletContext(ServletContext servletContext)
/*  29:    */   {
/*  30: 90 */     this.servletContext = servletContext;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setServletConfig(ServletConfig servletConfig)
/*  34:    */   {
/*  35: 94 */     this.servletConfig = servletConfig;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*  39:    */     throws BeansException
/*  40:    */   {
/*  41: 99 */     return bean;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*  45:    */     throws BeansException
/*  46:    */   {
/*  47:103 */     if ((bean instanceof Servlet))
/*  48:    */     {
/*  49:104 */       ServletConfig config = this.servletConfig;
/*  50:105 */       if ((config == null) || (!this.useSharedServletConfig)) {
/*  51:106 */         config = new DelegatingServletConfig(beanName, this.servletContext);
/*  52:    */       }
/*  53:    */       try
/*  54:    */       {
/*  55:109 */         ((Servlet)bean).init(config);
/*  56:    */       }
/*  57:    */       catch (ServletException ex)
/*  58:    */       {
/*  59:112 */         throw new BeanInitializationException("Servlet.init threw exception", ex);
/*  60:    */       }
/*  61:    */     }
/*  62:115 */     return bean;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void postProcessBeforeDestruction(Object bean, String beanName)
/*  66:    */     throws BeansException
/*  67:    */   {
/*  68:119 */     if ((bean instanceof Servlet)) {
/*  69:120 */       ((Servlet)bean).destroy();
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static class DelegatingServletConfig
/*  74:    */     implements ServletConfig
/*  75:    */   {
/*  76:    */     private final String servletName;
/*  77:    */     private final ServletContext servletContext;
/*  78:    */     
/*  79:    */     public DelegatingServletConfig(String servletName, ServletContext servletContext)
/*  80:    */     {
/*  81:136 */       this.servletName = servletName;
/*  82:137 */       this.servletContext = servletContext;
/*  83:    */     }
/*  84:    */     
/*  85:    */     public String getServletName()
/*  86:    */     {
/*  87:141 */       return this.servletName;
/*  88:    */     }
/*  89:    */     
/*  90:    */     public ServletContext getServletContext()
/*  91:    */     {
/*  92:145 */       return this.servletContext;
/*  93:    */     }
/*  94:    */     
/*  95:    */     public String getInitParameter(String paramName)
/*  96:    */     {
/*  97:149 */       return null;
/*  98:    */     }
/*  99:    */     
/* 100:    */     public Enumeration<String> getInitParameterNames()
/* 101:    */     {
/* 102:153 */       return Collections.enumeration(new HashSet());
/* 103:    */     }
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.SimpleServletPostProcessor
 * JD-Core Version:    0.7.0.1
 */