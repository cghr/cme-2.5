/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.LinkedList;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ 
/*   8:    */ public abstract class AbstractPropertyAccessor
/*   9:    */   extends PropertyEditorRegistrySupport
/*  10:    */   implements ConfigurablePropertyAccessor
/*  11:    */ {
/*  12: 37 */   private boolean extractOldValueForEditor = false;
/*  13:    */   
/*  14:    */   public void setExtractOldValueForEditor(boolean extractOldValueForEditor)
/*  15:    */   {
/*  16: 41 */     this.extractOldValueForEditor = extractOldValueForEditor;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public boolean isExtractOldValueForEditor()
/*  20:    */   {
/*  21: 45 */     return this.extractOldValueForEditor;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setPropertyValue(PropertyValue pv)
/*  25:    */     throws BeansException
/*  26:    */   {
/*  27: 50 */     setPropertyValue(pv.getName(), pv.getValue());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setPropertyValues(Map<?, ?> map)
/*  31:    */     throws BeansException
/*  32:    */   {
/*  33: 54 */     setPropertyValues(new MutablePropertyValues(map));
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setPropertyValues(PropertyValues pvs)
/*  37:    */     throws BeansException
/*  38:    */   {
/*  39: 58 */     setPropertyValues(pvs, false, false);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown)
/*  43:    */     throws BeansException
/*  44:    */   {
/*  45: 62 */     setPropertyValues(pvs, ignoreUnknown, false);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid)
/*  49:    */     throws BeansException
/*  50:    */   {
/*  51: 68 */     List<PropertyAccessException> propertyAccessExceptions = null;
/*  52: 69 */     List<PropertyValue> propertyValues = (pvs instanceof MutablePropertyValues) ? 
/*  53: 70 */       ((MutablePropertyValues)pvs).getPropertyValueList() : Arrays.asList(pvs.getPropertyValues());
/*  54: 71 */     for (PropertyValue pv : propertyValues) {
/*  55:    */       try
/*  56:    */       {
/*  57: 76 */         setPropertyValue(pv);
/*  58:    */       }
/*  59:    */       catch (NotWritablePropertyException ex)
/*  60:    */       {
/*  61: 79 */         if (!ignoreUnknown) {
/*  62: 80 */           throw ex;
/*  63:    */         }
/*  64:    */       }
/*  65:    */       catch (NullValueInNestedPathException ex)
/*  66:    */       {
/*  67: 85 */         if (!ignoreInvalid) {
/*  68: 86 */           throw ex;
/*  69:    */         }
/*  70:    */       }
/*  71:    */       catch (PropertyAccessException ex)
/*  72:    */       {
/*  73: 91 */         if (propertyAccessExceptions == null) {
/*  74: 92 */           propertyAccessExceptions = new LinkedList();
/*  75:    */         }
/*  76: 94 */         propertyAccessExceptions.add(ex);
/*  77:    */       }
/*  78:    */     }
/*  79: 99 */     if (propertyAccessExceptions != null)
/*  80:    */     {
/*  81:100 */       PropertyAccessException[] paeArray = 
/*  82:101 */         (PropertyAccessException[])propertyAccessExceptions.toArray(new PropertyAccessException[propertyAccessExceptions.size()]);
/*  83:102 */       throw new PropertyBatchUpdateException(paeArray);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public <T> T convertIfNecessary(Object value, Class<T> requiredType)
/*  88:    */     throws TypeMismatchException
/*  89:    */   {
/*  90:107 */     return convertIfNecessary(value, requiredType, null);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public Class getPropertyType(String propertyPath)
/*  94:    */   {
/*  95:114 */     return null;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public abstract Object getPropertyValue(String paramString)
/*  99:    */     throws BeansException;
/* 100:    */   
/* 101:    */   public abstract void setPropertyValue(String paramString, Object paramObject)
/* 102:    */     throws BeansException;
/* 103:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.AbstractPropertyAccessor
 * JD-Core Version:    0.7.0.1
 */