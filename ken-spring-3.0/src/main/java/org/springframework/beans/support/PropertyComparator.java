/*   1:    */ package org.springframework.beans.support;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.BeanWrapperImpl;
/*  10:    */ import org.springframework.beans.BeansException;
/*  11:    */ import org.springframework.util.StringUtils;
/*  12:    */ 
/*  13:    */ public class PropertyComparator
/*  14:    */   implements Comparator
/*  15:    */ {
/*  16: 42 */   protected final Log logger = LogFactory.getLog(getClass());
/*  17:    */   private final SortDefinition sortDefinition;
/*  18: 46 */   private final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(false);
/*  19:    */   
/*  20:    */   public PropertyComparator(SortDefinition sortDefinition)
/*  21:    */   {
/*  22: 54 */     this.sortDefinition = sortDefinition;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public PropertyComparator(String property, boolean ignoreCase, boolean ascending)
/*  26:    */   {
/*  27: 64 */     this.sortDefinition = new MutableSortDefinition(property, ignoreCase, ascending);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public final SortDefinition getSortDefinition()
/*  31:    */   {
/*  32: 71 */     return this.sortDefinition;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int compare(Object o1, Object o2)
/*  36:    */   {
/*  37: 76 */     Object v1 = getPropertyValue(o1);
/*  38: 77 */     Object v2 = getPropertyValue(o2);
/*  39: 78 */     if ((this.sortDefinition.isIgnoreCase()) && ((v1 instanceof String)) && ((v2 instanceof String)))
/*  40:    */     {
/*  41: 79 */       v1 = ((String)v1).toLowerCase();
/*  42: 80 */       v2 = ((String)v2).toLowerCase();
/*  43:    */     }
/*  44:    */     try
/*  45:    */     {
/*  46:    */       int result;
/*  47: 87 */       if (v1 != null) {
/*  48: 88 */         result = v2 != null ? ((Comparable)v1).compareTo(v2) : -1;
/*  49:    */       } else {
/*  50: 91 */         result = v2 != null ? 1 : 0;
/*  51:    */       }
/*  52:    */     }
/*  53:    */     catch (RuntimeException ex)
/*  54:    */     {
/*  55:    */       int result;
/*  56: 95 */       if (this.logger.isWarnEnabled()) {
/*  57: 96 */         this.logger.warn("Could not sort objects [" + o1 + "] and [" + o2 + "]", ex);
/*  58:    */       }
/*  59: 98 */       return 0;
/*  60:    */     }
/*  61:    */     int result;
/*  62:101 */     return this.sortDefinition.isAscending() ? result : -result;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private Object getPropertyValue(Object obj)
/*  66:    */   {
/*  67:    */     try
/*  68:    */     {
/*  69:114 */       this.beanWrapper.setWrappedInstance(obj);
/*  70:115 */       return this.beanWrapper.getPropertyValue(this.sortDefinition.getProperty());
/*  71:    */     }
/*  72:    */     catch (BeansException ex)
/*  73:    */     {
/*  74:118 */       this.logger.info("PropertyComparator could not access property - treating as null for sorting", ex);
/*  75:    */     }
/*  76:119 */     return null;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static void sort(List source, SortDefinition sortDefinition)
/*  80:    */     throws BeansException
/*  81:    */   {
/*  82:133 */     if (StringUtils.hasText(sortDefinition.getProperty())) {
/*  83:134 */       Collections.sort(source, new PropertyComparator(sortDefinition));
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static void sort(Object[] source, SortDefinition sortDefinition)
/*  88:    */     throws BeansException
/*  89:    */   {
/*  90:147 */     if (StringUtils.hasText(sortDefinition.getProperty())) {
/*  91:148 */       Arrays.sort(source, new PropertyComparator(sortDefinition));
/*  92:    */     }
/*  93:    */   }
/*  94:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.support.PropertyComparator
 * JD-Core Version:    0.7.0.1
 */