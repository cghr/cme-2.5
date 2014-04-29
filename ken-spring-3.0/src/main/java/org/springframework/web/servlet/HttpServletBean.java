/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.util.Enumeration;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import javax.servlet.ServletConfig;
/*   7:    */ import javax.servlet.ServletContext;
/*   8:    */ import javax.servlet.ServletException;
/*   9:    */ import javax.servlet.http.HttpServlet;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.beans.BeanWrapper;
/*  13:    */ import org.springframework.beans.BeansException;
/*  14:    */ import org.springframework.beans.MutablePropertyValues;
/*  15:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  16:    */ import org.springframework.beans.PropertyValue;
/*  17:    */ import org.springframework.beans.PropertyValues;
/*  18:    */ import org.springframework.context.EnvironmentAware;
/*  19:    */ import org.springframework.core.env.Environment;
/*  20:    */ import org.springframework.core.io.Resource;
/*  21:    */ import org.springframework.core.io.ResourceEditor;
/*  22:    */ import org.springframework.core.io.ResourceLoader;
/*  23:    */ import org.springframework.util.StringUtils;
/*  24:    */ import org.springframework.web.context.support.ServletContextResourceLoader;
/*  25:    */ import org.springframework.web.context.support.StandardServletEnvironment;
/*  26:    */ 
/*  27:    */ public abstract class HttpServletBean
/*  28:    */   extends HttpServlet
/*  29:    */   implements EnvironmentAware
/*  30:    */ {
/*  31: 82 */   protected final Log logger = LogFactory.getLog(getClass());
/*  32: 88 */   private final Set<String> requiredProperties = new HashSet();
/*  33: 90 */   private Environment environment = new StandardServletEnvironment();
/*  34:    */   
/*  35:    */   protected final void addRequiredProperty(String property)
/*  36:    */   {
/*  37:103 */     this.requiredProperties.add(property);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public final void init()
/*  41:    */     throws ServletException
/*  42:    */   {
/*  43:114 */     if (this.logger.isDebugEnabled()) {
/*  44:115 */       this.logger.debug("Initializing servlet '" + getServletName() + "'");
/*  45:    */     }
/*  46:    */     try
/*  47:    */     {
/*  48:120 */       PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
/*  49:121 */       BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/*  50:122 */       ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
/*  51:123 */       bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, this.environment));
/*  52:124 */       initBeanWrapper(bw);
/*  53:125 */       bw.setPropertyValues(pvs, true);
/*  54:    */     }
/*  55:    */     catch (BeansException ex)
/*  56:    */     {
/*  57:128 */       this.logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
/*  58:129 */       throw ex;
/*  59:    */     }
/*  60:133 */     initServletBean();
/*  61:135 */     if (this.logger.isDebugEnabled()) {
/*  62:136 */       this.logger.debug("Servlet '" + getServletName() + "' configured successfully");
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected void initBeanWrapper(BeanWrapper bw)
/*  67:    */     throws BeansException
/*  68:    */   {}
/*  69:    */   
/*  70:    */   public final String getServletName()
/*  71:    */   {
/*  72:159 */     return getServletConfig() != null ? getServletConfig().getServletName() : null;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final ServletContext getServletContext()
/*  76:    */   {
/*  77:169 */     return getServletConfig() != null ? getServletConfig().getServletContext() : null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected void initServletBean()
/*  81:    */     throws ServletException
/*  82:    */   {}
/*  83:    */   
/*  84:    */   public void setEnvironment(Environment environment)
/*  85:    */   {
/*  86:189 */     this.environment = environment;
/*  87:    */   }
/*  88:    */   
/*  89:    */   private static class ServletConfigPropertyValues
/*  90:    */     extends MutablePropertyValues
/*  91:    */   {
/*  92:    */     public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties)
/*  93:    */       throws ServletException
/*  94:    */     {
/*  95:208 */       Set<String> missingProps = (requiredProperties != null) && (!requiredProperties.isEmpty()) ? 
/*  96:209 */         new HashSet(requiredProperties) : null;
/*  97:    */       
/*  98:211 */       Enumeration en = config.getInitParameterNames();
/*  99:212 */       while (en.hasMoreElements())
/* 100:    */       {
/* 101:213 */         String property = (String)en.nextElement();
/* 102:214 */         Object value = config.getInitParameter(property);
/* 103:215 */         addPropertyValue(new PropertyValue(property, value));
/* 104:216 */         if (missingProps != null) {
/* 105:217 */           missingProps.remove(property);
/* 106:    */         }
/* 107:    */       }
/* 108:222 */       if ((missingProps != null) && (missingProps.size() > 0)) {
/* 109:223 */         throw new ServletException(
/* 110:224 */           "Initialization from ServletConfig for servlet '" + config.getServletName() + 
/* 111:225 */           "' failed; the following required properties were missing: " + 
/* 112:226 */           StringUtils.collectionToDelimitedString(missingProps, ", "));
/* 113:    */       }
/* 114:    */     }
/* 115:    */   }
/* 116:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.HttpServletBean
 * JD-Core Version:    0.7.0.1
 */