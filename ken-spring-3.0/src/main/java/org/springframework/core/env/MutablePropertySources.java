/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.LinkedList;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public class MutablePropertySources
/*   8:    */   implements PropertySources
/*   9:    */ {
/*  10:    */   static final String NON_EXISTENT_PROPERTY_SOURCE_MESSAGE = "PropertySource named [%s] does not exist";
/*  11:    */   static final String ILLEGAL_RELATIVE_ADDITION_MESSAGE = "PropertySource named [%s] cannot be added relative to itself";
/*  12: 42 */   private final LinkedList<PropertySource<?>> propertySourceList = new LinkedList();
/*  13:    */   
/*  14:    */   public MutablePropertySources() {}
/*  15:    */   
/*  16:    */   public MutablePropertySources(PropertySources propertySources)
/*  17:    */   {
/*  18: 55 */     for (PropertySource<?> propertySource : propertySources) {
/*  19: 56 */       addLast(propertySource);
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   public boolean contains(String name)
/*  24:    */   {
/*  25: 61 */     return this.propertySourceList.contains(PropertySource.named(name));
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PropertySource<?> get(String name)
/*  29:    */   {
/*  30: 65 */     return (PropertySource)this.propertySourceList.get(this.propertySourceList.indexOf(PropertySource.named(name)));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Iterator<PropertySource<?>> iterator()
/*  34:    */   {
/*  35: 69 */     return this.propertySourceList.iterator();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void addFirst(PropertySource<?> propertySource)
/*  39:    */   {
/*  40: 76 */     removeIfPresent(propertySource);
/*  41: 77 */     this.propertySourceList.addFirst(propertySource);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void addLast(PropertySource<?> propertySource)
/*  45:    */   {
/*  46: 84 */     removeIfPresent(propertySource);
/*  47: 85 */     this.propertySourceList.addLast(propertySource);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource)
/*  51:    */   {
/*  52: 93 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/*  53: 94 */     removeIfPresent(propertySource);
/*  54: 95 */     int index = assertPresentAndGetIndex(relativePropertySourceName);
/*  55: 96 */     addAtIndex(index, propertySource);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource)
/*  59:    */   {
/*  60:104 */     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
/*  61:105 */     removeIfPresent(propertySource);
/*  62:106 */     int index = assertPresentAndGetIndex(relativePropertySourceName);
/*  63:107 */     addAtIndex(index + 1, propertySource);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int precedenceOf(PropertySource<?> propertySource)
/*  67:    */   {
/*  68:114 */     return this.propertySourceList.indexOf(propertySource);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public PropertySource<?> remove(String name)
/*  72:    */   {
/*  73:122 */     int index = this.propertySourceList.indexOf(PropertySource.named(name));
/*  74:123 */     if (index >= 0) {
/*  75:124 */       return (PropertySource)this.propertySourceList.remove(index);
/*  76:    */     }
/*  77:126 */     return null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void replace(String name, PropertySource<?> propertySource)
/*  81:    */   {
/*  82:137 */     int index = assertPresentAndGetIndex(name);
/*  83:138 */     this.propertySourceList.set(index, propertySource);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int size()
/*  87:    */   {
/*  88:145 */     return this.propertySourceList.size();
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource)
/*  92:    */   {
/*  93:152 */     String newPropertySourceName = propertySource.getName();
/*  94:153 */     Assert.isTrue(!relativePropertySourceName.equals(newPropertySourceName), 
/*  95:154 */       String.format("PropertySource named [%s] cannot be added relative to itself", new Object[] { newPropertySourceName }));
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void removeIfPresent(PropertySource<?> propertySource)
/*  99:    */   {
/* 100:161 */     if (this.propertySourceList.contains(propertySource)) {
/* 101:162 */       this.propertySourceList.remove(propertySource);
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   private void addAtIndex(int index, PropertySource<?> propertySource)
/* 106:    */   {
/* 107:170 */     removeIfPresent(propertySource);
/* 108:171 */     this.propertySourceList.add(index, propertySource);
/* 109:    */   }
/* 110:    */   
/* 111:    */   private int assertPresentAndGetIndex(String propertySourceName)
/* 112:    */   {
/* 113:179 */     int index = this.propertySourceList.indexOf(PropertySource.named(propertySourceName));
/* 114:180 */     Assert.isTrue(index >= 0, String.format("PropertySource named [%s] does not exist", new Object[] { propertySourceName }));
/* 115:181 */     return index;
/* 116:    */   }
/* 117:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.MutablePropertySources
 * JD-Core Version:    0.7.0.1
 */