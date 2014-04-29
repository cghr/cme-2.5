/*   1:    */ package org.springframework.beans.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.springframework.util.StringUtils;
/*   5:    */ 
/*   6:    */ public class MutableSortDefinition
/*   7:    */   implements SortDefinition, Serializable
/*   8:    */ {
/*   9: 34 */   private String property = "";
/*  10: 36 */   private boolean ignoreCase = true;
/*  11: 38 */   private boolean ascending = true;
/*  12: 40 */   private boolean toggleAscendingOnProperty = false;
/*  13:    */   
/*  14:    */   public MutableSortDefinition() {}
/*  15:    */   
/*  16:    */   public MutableSortDefinition(SortDefinition source)
/*  17:    */   {
/*  18: 59 */     this.property = source.getProperty();
/*  19: 60 */     this.ignoreCase = source.isIgnoreCase();
/*  20: 61 */     this.ascending = source.isAscending();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public MutableSortDefinition(String property, boolean ignoreCase, boolean ascending)
/*  24:    */   {
/*  25: 71 */     this.property = property;
/*  26: 72 */     this.ignoreCase = ignoreCase;
/*  27: 73 */     this.ascending = ascending;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public MutableSortDefinition(boolean toggleAscendingOnSameProperty)
/*  31:    */   {
/*  32: 83 */     this.toggleAscendingOnProperty = toggleAscendingOnSameProperty;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setProperty(String property)
/*  36:    */   {
/*  37: 94 */     if (!StringUtils.hasLength(property))
/*  38:    */     {
/*  39: 95 */       this.property = "";
/*  40:    */     }
/*  41:    */     else
/*  42:    */     {
/*  43: 99 */       if (isToggleAscendingOnProperty()) {
/*  44:100 */         this.ascending = ((!property.equals(this.property)) || (!this.ascending));
/*  45:    */       }
/*  46:102 */       this.property = property;
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getProperty()
/*  51:    */   {
/*  52:107 */     return this.property;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setIgnoreCase(boolean ignoreCase)
/*  56:    */   {
/*  57:114 */     this.ignoreCase = ignoreCase;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean isIgnoreCase()
/*  61:    */   {
/*  62:118 */     return this.ignoreCase;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setAscending(boolean ascending)
/*  66:    */   {
/*  67:125 */     this.ascending = ascending;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean isAscending()
/*  71:    */   {
/*  72:129 */     return this.ascending;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setToggleAscendingOnProperty(boolean toggleAscendingOnProperty)
/*  76:    */   {
/*  77:140 */     this.toggleAscendingOnProperty = toggleAscendingOnProperty;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean isToggleAscendingOnProperty()
/*  81:    */   {
/*  82:148 */     return this.toggleAscendingOnProperty;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public boolean equals(Object other)
/*  86:    */   {
/*  87:154 */     if (this == other) {
/*  88:155 */       return true;
/*  89:    */     }
/*  90:157 */     if (!(other instanceof SortDefinition)) {
/*  91:158 */       return false;
/*  92:    */     }
/*  93:160 */     SortDefinition otherSd = (SortDefinition)other;
/*  94:    */     
/*  95:162 */     return (getProperty().equals(otherSd.getProperty())) && (isAscending() == otherSd.isAscending()) && (isIgnoreCase() == otherSd.isIgnoreCase());
/*  96:    */   }
/*  97:    */   
/*  98:    */   public int hashCode()
/*  99:    */   {
/* 100:167 */     int hashCode = getProperty().hashCode();
/* 101:168 */     hashCode = 29 * hashCode + (isIgnoreCase() ? 1 : 0);
/* 102:169 */     hashCode = 29 * hashCode + (isAscending() ? 1 : 0);
/* 103:170 */     return hashCode;
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.support.MutableSortDefinition
 * JD-Core Version:    0.7.0.1
 */