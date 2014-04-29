/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import java.util.Properties;
/*  4:   */ import org.springframework.beans.BeanMetadataElement;
/*  5:   */ import org.springframework.beans.Mergeable;
/*  6:   */ 
/*  7:   */ public class ManagedProperties
/*  8:   */   extends Properties
/*  9:   */   implements Mergeable, BeanMetadataElement
/* 10:   */ {
/* 11:   */   private Object source;
/* 12:   */   private boolean mergeEnabled;
/* 13:   */   
/* 14:   */   public void setSource(Object source)
/* 15:   */   {
/* 16:44 */     this.source = source;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Object getSource()
/* 20:   */   {
/* 21:48 */     return this.source;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setMergeEnabled(boolean mergeEnabled)
/* 25:   */   {
/* 26:56 */     this.mergeEnabled = mergeEnabled;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public boolean isMergeEnabled()
/* 30:   */   {
/* 31:60 */     return this.mergeEnabled;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public Object merge(Object parent)
/* 35:   */   {
/* 36:65 */     if (!this.mergeEnabled) {
/* 37:66 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/* 38:   */     }
/* 39:68 */     if (parent == null) {
/* 40:69 */       return this;
/* 41:   */     }
/* 42:71 */     if (!(parent instanceof Properties)) {
/* 43:72 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/* 44:   */     }
/* 45:74 */     Properties merged = new ManagedProperties();
/* 46:75 */     merged.putAll((Properties)parent);
/* 47:76 */     merged.putAll(this);
/* 48:77 */     return merged;
/* 49:   */   }
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedProperties
 * JD-Core Version:    0.7.0.1
 */