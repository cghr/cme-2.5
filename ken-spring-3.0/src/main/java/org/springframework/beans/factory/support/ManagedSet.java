/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.springframework.beans.BeanMetadataElement;
/*   6:    */ import org.springframework.beans.Mergeable;
/*   7:    */ 
/*   8:    */ public class ManagedSet<E>
/*   9:    */   extends LinkedHashSet<E>
/*  10:    */   implements Mergeable, BeanMetadataElement
/*  11:    */ {
/*  12:    */   private Object source;
/*  13:    */   private String elementTypeName;
/*  14:    */   private boolean mergeEnabled;
/*  15:    */   
/*  16:    */   public ManagedSet() {}
/*  17:    */   
/*  18:    */   public ManagedSet(int initialCapacity)
/*  19:    */   {
/*  20: 46 */     super(initialCapacity);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setSource(Object source)
/*  24:    */   {
/*  25: 55 */     this.source = source;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Object getSource()
/*  29:    */   {
/*  30: 59 */     return this.source;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setElementTypeName(String elementTypeName)
/*  34:    */   {
/*  35: 66 */     this.elementTypeName = elementTypeName;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getElementTypeName()
/*  39:    */   {
/*  40: 73 */     return this.elementTypeName;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setMergeEnabled(boolean mergeEnabled)
/*  44:    */   {
/*  45: 81 */     this.mergeEnabled = mergeEnabled;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isMergeEnabled()
/*  49:    */   {
/*  50: 85 */     return this.mergeEnabled;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Set<E> merge(Object parent)
/*  54:    */   {
/*  55: 90 */     if (!this.mergeEnabled) {
/*  56: 91 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*  57:    */     }
/*  58: 93 */     if (parent == null) {
/*  59: 94 */       return this;
/*  60:    */     }
/*  61: 96 */     if (!(parent instanceof Set)) {
/*  62: 97 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*  63:    */     }
/*  64: 99 */     Set<E> merged = new ManagedSet();
/*  65:100 */     merged.addAll((Set)parent);
/*  66:101 */     merged.addAll(this);
/*  67:102 */     return merged;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedSet
 * JD-Core Version:    0.7.0.1
 */