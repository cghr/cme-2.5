/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeanMetadataElement;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class AliasDefinition
/*  7:   */   implements BeanMetadataElement
/*  8:   */ {
/*  9:   */   private final String beanName;
/* 10:   */   private final String alias;
/* 11:   */   private final Object source;
/* 12:   */   
/* 13:   */   public AliasDefinition(String beanName, String alias)
/* 14:   */   {
/* 15:44 */     this(beanName, alias, null);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public AliasDefinition(String beanName, String alias, Object source)
/* 19:   */   {
/* 20:54 */     Assert.notNull(beanName, "Bean name must not be null");
/* 21:55 */     Assert.notNull(alias, "Alias must not be null");
/* 22:56 */     this.beanName = beanName;
/* 23:57 */     this.alias = alias;
/* 24:58 */     this.source = source;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public final String getBeanName()
/* 28:   */   {
/* 29:66 */     return this.beanName;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public final String getAlias()
/* 33:   */   {
/* 34:73 */     return this.alias;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public final Object getSource()
/* 38:   */   {
/* 39:77 */     return this.source;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.AliasDefinition
 * JD-Core Version:    0.7.0.1
 */