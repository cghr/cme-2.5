/*   1:    */ package org.springframework.jdbc.core.namedparam;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.beans.BeanWrapper;
/*   7:    */ import org.springframework.beans.NotReadablePropertyException;
/*   8:    */ import org.springframework.beans.PropertyAccessorFactory;
/*   9:    */ import org.springframework.jdbc.core.StatementCreatorUtils;
/*  10:    */ 
/*  11:    */ public class BeanPropertySqlParameterSource
/*  12:    */   extends AbstractSqlParameterSource
/*  13:    */ {
/*  14:    */   private final BeanWrapper beanWrapper;
/*  15:    */   private String[] propertyNames;
/*  16:    */   
/*  17:    */   public BeanPropertySqlParameterSource(Object object)
/*  18:    */   {
/*  19: 54 */     this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public boolean hasValue(String paramName)
/*  23:    */   {
/*  24: 59 */     return this.beanWrapper.isReadableProperty(paramName);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public Object getValue(String paramName)
/*  28:    */     throws IllegalArgumentException
/*  29:    */   {
/*  30:    */     try
/*  31:    */     {
/*  32: 64 */       return this.beanWrapper.getPropertyValue(paramName);
/*  33:    */     }
/*  34:    */     catch (NotReadablePropertyException ex)
/*  35:    */     {
/*  36: 67 */       throw new IllegalArgumentException(ex.getMessage());
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String[] getReadablePropertyNames()
/*  41:    */   {
/*  42: 77 */     if (this.propertyNames == null)
/*  43:    */     {
/*  44: 78 */       List<String> names = new ArrayList();
/*  45: 79 */       PropertyDescriptor[] props = this.beanWrapper.getPropertyDescriptors();
/*  46: 80 */       for (PropertyDescriptor pd : props) {
/*  47: 81 */         if (this.beanWrapper.isReadableProperty(pd.getName())) {
/*  48: 82 */           names.add(pd.getName());
/*  49:    */         }
/*  50:    */       }
/*  51: 85 */       this.propertyNames = ((String[])names.toArray(new String[names.size()]));
/*  52:    */     }
/*  53: 87 */     return this.propertyNames;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getSqlType(String paramName)
/*  57:    */   {
/*  58: 96 */     int sqlType = super.getSqlType(paramName);
/*  59: 97 */     if (sqlType != -2147483648) {
/*  60: 98 */       return sqlType;
/*  61:    */     }
/*  62:100 */     Class propType = this.beanWrapper.getPropertyType(paramName);
/*  63:101 */     return StatementCreatorUtils.javaTypeToSqlParameterType(propType);
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
 * JD-Core Version:    0.7.0.1
 */