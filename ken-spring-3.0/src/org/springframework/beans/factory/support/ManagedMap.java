/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.beans.BeanMetadataElement;
/*   6:    */ import org.springframework.beans.Mergeable;
/*   7:    */ 
/*   8:    */ public class ManagedMap<K, V>
/*   9:    */   extends LinkedHashMap<K, V>
/*  10:    */   implements Mergeable, BeanMetadataElement
/*  11:    */ {
/*  12:    */   private Object source;
/*  13:    */   private String keyTypeName;
/*  14:    */   private String valueTypeName;
/*  15:    */   private boolean mergeEnabled;
/*  16:    */   
/*  17:    */   public ManagedMap() {}
/*  18:    */   
/*  19:    */   public ManagedMap(int initialCapacity)
/*  20:    */   {
/*  21: 48 */     super(initialCapacity);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setSource(Object source)
/*  25:    */   {
/*  26: 57 */     this.source = source;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Object getSource()
/*  30:    */   {
/*  31: 61 */     return this.source;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setKeyTypeName(String keyTypeName)
/*  35:    */   {
/*  36: 68 */     this.keyTypeName = keyTypeName;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getKeyTypeName()
/*  40:    */   {
/*  41: 75 */     return this.keyTypeName;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setValueTypeName(String valueTypeName)
/*  45:    */   {
/*  46: 82 */     this.valueTypeName = valueTypeName;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getValueTypeName()
/*  50:    */   {
/*  51: 89 */     return this.valueTypeName;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setMergeEnabled(boolean mergeEnabled)
/*  55:    */   {
/*  56: 97 */     this.mergeEnabled = mergeEnabled;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isMergeEnabled()
/*  60:    */   {
/*  61:101 */     return this.mergeEnabled;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Object merge(Object parent)
/*  65:    */   {
/*  66:106 */     if (!this.mergeEnabled) {
/*  67:107 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*  68:    */     }
/*  69:109 */     if (parent == null) {
/*  70:110 */       return this;
/*  71:    */     }
/*  72:112 */     if (!(parent instanceof Map)) {
/*  73:113 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*  74:    */     }
/*  75:115 */     Map<K, V> merged = new ManagedMap();
/*  76:116 */     merged.putAll((Map)parent);
/*  77:117 */     merged.putAll(this);
/*  78:118 */     return merged;
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedMap
 * JD-Core Version:    0.7.0.1
 */