/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.beans.BeanMetadataElement;
/*   6:    */ import org.springframework.beans.Mergeable;
/*   7:    */ 
/*   8:    */ public class ManagedList<E>
/*   9:    */   extends ArrayList<E>
/*  10:    */   implements Mergeable, BeanMetadataElement
/*  11:    */ {
/*  12:    */   private Object source;
/*  13:    */   private String elementTypeName;
/*  14:    */   private boolean mergeEnabled;
/*  15:    */   
/*  16:    */   public ManagedList() {}
/*  17:    */   
/*  18:    */   public ManagedList(int initialCapacity)
/*  19:    */   {
/*  20: 47 */     super(initialCapacity);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setSource(Object source)
/*  24:    */   {
/*  25: 56 */     this.source = source;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Object getSource()
/*  29:    */   {
/*  30: 60 */     return this.source;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setElementTypeName(String elementTypeName)
/*  34:    */   {
/*  35: 67 */     this.elementTypeName = elementTypeName;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getElementTypeName()
/*  39:    */   {
/*  40: 74 */     return this.elementTypeName;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setMergeEnabled(boolean mergeEnabled)
/*  44:    */   {
/*  45: 82 */     this.mergeEnabled = mergeEnabled;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isMergeEnabled()
/*  49:    */   {
/*  50: 86 */     return this.mergeEnabled;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public List<E> merge(Object parent)
/*  54:    */   {
/*  55: 91 */     if (!this.mergeEnabled) {
/*  56: 92 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*  57:    */     }
/*  58: 94 */     if (parent == null) {
/*  59: 95 */       return this;
/*  60:    */     }
/*  61: 97 */     if (!(parent instanceof List)) {
/*  62: 98 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*  63:    */     }
/*  64:100 */     List<E> merged = new ManagedList();
/*  65:101 */     merged.addAll((List)parent);
/*  66:102 */     merged.addAll(this);
/*  67:103 */     return merged;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedList
 * JD-Core Version:    0.7.0.1
 */