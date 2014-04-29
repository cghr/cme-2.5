/*   1:    */ package org.springframework.web.jsf.el;
/*   2:    */ 
/*   3:    */ import java.beans.FeatureDescriptor;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import javax.el.ELContext;
/*   6:    */ import javax.el.ELException;
/*   7:    */ import javax.el.ELResolver;
/*   8:    */ import javax.faces.context.FacesContext;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.web.context.WebApplicationContext;
/*  13:    */ import org.springframework.web.jsf.FacesContextUtils;
/*  14:    */ 
/*  15:    */ public class WebApplicationContextFacesELResolver
/*  16:    */   extends ELResolver
/*  17:    */ {
/*  18:    */   public static final String WEB_APPLICATION_CONTEXT_VARIABLE_NAME = "webApplicationContext";
/*  19: 67 */   protected final Log logger = LogFactory.getLog(getClass());
/*  20:    */   
/*  21:    */   public Object getValue(ELContext elContext, Object base, Object property)
/*  22:    */     throws ELException
/*  23:    */   {
/*  24: 72 */     if (base != null)
/*  25:    */     {
/*  26: 73 */       if ((base instanceof WebApplicationContext))
/*  27:    */       {
/*  28: 74 */         WebApplicationContext wac = (WebApplicationContext)base;
/*  29: 75 */         String beanName = property.toString();
/*  30: 76 */         if (this.logger.isTraceEnabled()) {
/*  31: 77 */           this.logger.trace("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
/*  32:    */         }
/*  33: 79 */         if (wac.containsBean(beanName))
/*  34:    */         {
/*  35: 80 */           if (this.logger.isDebugEnabled()) {
/*  36: 81 */             this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
/*  37:    */           }
/*  38: 83 */           elContext.setPropertyResolved(true);
/*  39:    */           try
/*  40:    */           {
/*  41: 85 */             return wac.getBean(beanName);
/*  42:    */           }
/*  43:    */           catch (BeansException ex)
/*  44:    */           {
/*  45: 88 */             throw new ELException(ex);
/*  46:    */           }
/*  47:    */         }
/*  48: 93 */         return null;
/*  49:    */       }
/*  50:    */     }
/*  51: 98 */     else if ("webApplicationContext".equals(property))
/*  52:    */     {
/*  53: 99 */       elContext.setPropertyResolved(true);
/*  54:100 */       return getWebApplicationContext(elContext);
/*  55:    */     }
/*  56:104 */     return null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Class<?> getType(ELContext elContext, Object base, Object property)
/*  60:    */     throws ELException
/*  61:    */   {
/*  62:109 */     if (base != null)
/*  63:    */     {
/*  64:110 */       if ((base instanceof WebApplicationContext))
/*  65:    */       {
/*  66:111 */         WebApplicationContext wac = (WebApplicationContext)base;
/*  67:112 */         String beanName = property.toString();
/*  68:113 */         if (this.logger.isDebugEnabled()) {
/*  69:114 */           this.logger.debug("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
/*  70:    */         }
/*  71:116 */         if (wac.containsBean(beanName))
/*  72:    */         {
/*  73:117 */           if (this.logger.isDebugEnabled()) {
/*  74:118 */             this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
/*  75:    */           }
/*  76:120 */           elContext.setPropertyResolved(true);
/*  77:    */           try
/*  78:    */           {
/*  79:122 */             return wac.getType(beanName);
/*  80:    */           }
/*  81:    */           catch (BeansException ex)
/*  82:    */           {
/*  83:125 */             throw new ELException(ex);
/*  84:    */           }
/*  85:    */         }
/*  86:130 */         return null;
/*  87:    */       }
/*  88:    */     }
/*  89:135 */     else if ("webApplicationContext".equals(property))
/*  90:    */     {
/*  91:136 */       elContext.setPropertyResolved(true);
/*  92:137 */       return WebApplicationContext.class;
/*  93:    */     }
/*  94:141 */     return null;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setValue(ELContext elContext, Object base, Object property, Object value)
/*  98:    */     throws ELException
/*  99:    */   {}
/* 100:    */   
/* 101:    */   public boolean isReadOnly(ELContext elContext, Object base, Object property)
/* 102:    */     throws ELException
/* 103:    */   {
/* 104:150 */     if ((base instanceof WebApplicationContext))
/* 105:    */     {
/* 106:151 */       elContext.setPropertyResolved(true);
/* 107:152 */       return false;
/* 108:    */     }
/* 109:154 */     return false;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base)
/* 113:    */   {
/* 114:159 */     return null;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public Class<?> getCommonPropertyType(ELContext elContext, Object base)
/* 118:    */   {
/* 119:164 */     return Object.class;
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected WebApplicationContext getWebApplicationContext(ELContext elContext)
/* 123:    */   {
/* 124:177 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 125:178 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/* 126:    */   }
/* 127:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.el.WebApplicationContextFacesELResolver
 * JD-Core Version:    0.7.0.1
 */