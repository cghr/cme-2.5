/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.lang.reflect.Array;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.SortedSet;
/*  10:    */ import java.util.TreeSet;
/*  11:    */ 
/*  12:    */ public class CustomCollectionEditor
/*  13:    */   extends PropertyEditorSupport
/*  14:    */ {
/*  15:    */   private final Class collectionType;
/*  16:    */   private final boolean nullAsEmptyCollection;
/*  17:    */   
/*  18:    */   public CustomCollectionEditor(Class collectionType)
/*  19:    */   {
/*  20: 61 */     this(collectionType, false);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public CustomCollectionEditor(Class collectionType, boolean nullAsEmptyCollection)
/*  24:    */   {
/*  25: 83 */     if (collectionType == null) {
/*  26: 84 */       throw new IllegalArgumentException("Collection type is required");
/*  27:    */     }
/*  28: 86 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/*  29: 87 */       throw new IllegalArgumentException(
/*  30: 88 */         "Collection type [" + collectionType.getName() + "] does not implement [java.util.Collection]");
/*  31:    */     }
/*  32: 90 */     this.collectionType = collectionType;
/*  33: 91 */     this.nullAsEmptyCollection = nullAsEmptyCollection;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setAsText(String text)
/*  37:    */     throws IllegalArgumentException
/*  38:    */   {
/*  39:100 */     setValue(text);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setValue(Object value)
/*  43:    */   {
/*  44:109 */     if ((value == null) && (this.nullAsEmptyCollection))
/*  45:    */     {
/*  46:110 */       super.setValue(createCollection(this.collectionType, 0));
/*  47:    */     }
/*  48:112 */     else if ((value == null) || ((this.collectionType.isInstance(value)) && (!alwaysCreateNewCollection())))
/*  49:    */     {
/*  50:114 */       super.setValue(value);
/*  51:    */     }
/*  52:116 */     else if ((value instanceof Collection))
/*  53:    */     {
/*  54:118 */       Collection source = (Collection)value;
/*  55:119 */       Collection target = createCollection(this.collectionType, source.size());
/*  56:120 */       for (Object elem : source) {
/*  57:121 */         target.add(convertElement(elem));
/*  58:    */       }
/*  59:123 */       super.setValue(target);
/*  60:    */     }
/*  61:125 */     else if (value.getClass().isArray())
/*  62:    */     {
/*  63:127 */       int length = Array.getLength(value);
/*  64:128 */       Collection target = createCollection(this.collectionType, length);
/*  65:129 */       for (int i = 0; i < length; i++) {
/*  66:130 */         target.add(convertElement(Array.get(value, i)));
/*  67:    */       }
/*  68:132 */       super.setValue(target);
/*  69:    */     }
/*  70:    */     else
/*  71:    */     {
/*  72:136 */       Collection target = createCollection(this.collectionType, 1);
/*  73:137 */       target.add(convertElement(value));
/*  74:138 */       super.setValue(target);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected Collection createCollection(Class collectionType, int initialCapacity)
/*  79:    */   {
/*  80:150 */     if (!collectionType.isInterface()) {
/*  81:    */       try
/*  82:    */       {
/*  83:152 */         return (Collection)collectionType.newInstance();
/*  84:    */       }
/*  85:    */       catch (Exception ex)
/*  86:    */       {
/*  87:155 */         throw new IllegalArgumentException(
/*  88:156 */           "Could not instantiate collection class [" + collectionType.getName() + "]: " + ex.getMessage());
/*  89:    */       }
/*  90:    */     }
/*  91:159 */     if (List.class.equals(collectionType)) {
/*  92:160 */       return new ArrayList(initialCapacity);
/*  93:    */     }
/*  94:162 */     if (SortedSet.class.equals(collectionType)) {
/*  95:163 */       return new TreeSet();
/*  96:    */     }
/*  97:166 */     return new LinkedHashSet(initialCapacity);
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected boolean alwaysCreateNewCollection()
/* 101:    */   {
/* 102:178 */     return false;
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected Object convertElement(Object element)
/* 106:    */   {
/* 107:196 */     return element;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public String getAsText()
/* 111:    */   {
/* 112:206 */     return null;
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CustomCollectionEditor
 * JD-Core Version:    0.7.0.1
 */