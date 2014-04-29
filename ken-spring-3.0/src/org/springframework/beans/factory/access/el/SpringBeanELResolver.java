/*   1:    */ package org.springframework.beans.factory.access.el;
/*   2:    */ 
/*   3:    */ import java.beans.FeatureDescriptor;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import javax.el.ELContext;
/*   6:    */ import javax.el.ELException;
/*   7:    */ import javax.el.ELResolver;
/*   8:    */ import javax.el.PropertyNotWritableException;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ 
/*  13:    */ public abstract class SpringBeanELResolver
/*  14:    */   extends ELResolver
/*  15:    */ {
/*  16: 43 */   protected final Log logger = LogFactory.getLog(getClass());
/*  17:    */   
/*  18:    */   public Object getValue(ELContext elContext, Object base, Object property)
/*  19:    */     throws ELException
/*  20:    */   {
/*  21: 48 */     if (base == null)
/*  22:    */     {
/*  23: 49 */       String beanName = property.toString();
/*  24: 50 */       BeanFactory bf = getBeanFactory(elContext);
/*  25: 51 */       if (bf.containsBean(beanName))
/*  26:    */       {
/*  27: 52 */         if (this.logger.isTraceEnabled()) {
/*  28: 53 */           this.logger.trace("Successfully resolved variable '" + beanName + "' in Spring BeanFactory");
/*  29:    */         }
/*  30: 55 */         elContext.setPropertyResolved(true);
/*  31: 56 */         return bf.getBean(beanName);
/*  32:    */       }
/*  33:    */     }
/*  34: 59 */     return null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Class<?> getType(ELContext elContext, Object base, Object property)
/*  38:    */     throws ELException
/*  39:    */   {
/*  40: 64 */     if (base == null)
/*  41:    */     {
/*  42: 65 */       String beanName = property.toString();
/*  43: 66 */       BeanFactory bf = getBeanFactory(elContext);
/*  44: 67 */       if (bf.containsBean(beanName))
/*  45:    */       {
/*  46: 68 */         elContext.setPropertyResolved(true);
/*  47: 69 */         return bf.getType(beanName);
/*  48:    */       }
/*  49:    */     }
/*  50: 72 */     return null;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setValue(ELContext elContext, Object base, Object property, Object value)
/*  54:    */     throws ELException
/*  55:    */   {
/*  56: 77 */     if (base == null)
/*  57:    */     {
/*  58: 78 */       String beanName = property.toString();
/*  59: 79 */       BeanFactory bf = getBeanFactory(elContext);
/*  60: 80 */       if (bf.containsBean(beanName)) {
/*  61: 81 */         throw new PropertyNotWritableException(
/*  62: 82 */           "Variable '" + beanName + "' refers to a Spring bean which by definition is not writable");
/*  63:    */       }
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isReadOnly(ELContext elContext, Object base, Object property)
/*  68:    */     throws ELException
/*  69:    */   {
/*  70: 89 */     if (base == null)
/*  71:    */     {
/*  72: 90 */       String beanName = property.toString();
/*  73: 91 */       BeanFactory bf = getBeanFactory(elContext);
/*  74: 92 */       if (bf.containsBean(beanName)) {
/*  75: 93 */         return true;
/*  76:    */       }
/*  77:    */     }
/*  78: 96 */     return false;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base)
/*  82:    */   {
/*  83:101 */     return null;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Class<?> getCommonPropertyType(ELContext elContext, Object base)
/*  87:    */   {
/*  88:106 */     return Object.class;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected abstract BeanFactory getBeanFactory(ELContext paramELContext);
/*  92:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.access.el.SpringBeanELResolver
 * JD-Core Version:    0.7.0.1
 */