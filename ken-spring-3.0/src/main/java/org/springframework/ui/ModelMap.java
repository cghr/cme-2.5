/*   1:    */ package org.springframework.ui;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.springframework.core.Conventions;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class ModelMap
/*  10:    */   extends LinkedHashMap<String, Object>
/*  11:    */ {
/*  12:    */   public ModelMap() {}
/*  13:    */   
/*  14:    */   public ModelMap(String attributeName, Object attributeValue)
/*  15:    */   {
/*  16: 55 */     addAttribute(attributeName, attributeValue);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public ModelMap(Object attributeValue)
/*  20:    */   {
/*  21: 65 */     addAttribute(attributeValue);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ModelMap addAttribute(String attributeName, Object attributeValue)
/*  25:    */   {
/*  26: 75 */     Assert.notNull(attributeName, "Model attribute name must not be null");
/*  27: 76 */     put(attributeName, attributeValue);
/*  28: 77 */     return this;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ModelMap addAttribute(Object attributeValue)
/*  32:    */   {
/*  33: 90 */     Assert.notNull(attributeValue, "Model object must not be null");
/*  34: 91 */     if (((attributeValue instanceof Collection)) && (((Collection)attributeValue).isEmpty())) {
/*  35: 92 */       return this;
/*  36:    */     }
/*  37: 94 */     return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public ModelMap addAllAttributes(Collection<?> attributeValues)
/*  41:    */   {
/*  42:103 */     if (attributeValues != null) {
/*  43:104 */       for (Object attributeValue : attributeValues) {
/*  44:105 */         addAttribute(attributeValue);
/*  45:    */       }
/*  46:    */     }
/*  47:108 */     return this;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public ModelMap addAllAttributes(Map<String, ?> attributes)
/*  51:    */   {
/*  52:116 */     if (attributes != null) {
/*  53:117 */       putAll(attributes);
/*  54:    */     }
/*  55:119 */     return this;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public ModelMap mergeAttributes(Map<String, ?> attributes)
/*  59:    */   {
/*  60:128 */     if (attributes != null) {
/*  61:129 */       for (String key : attributes.keySet()) {
/*  62:130 */         if (!containsKey(key)) {
/*  63:131 */           put(key, attributes.get(key));
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:135 */     return this;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean containsAttribute(String attributeName)
/*  71:    */   {
/*  72:144 */     return containsKey(attributeName);
/*  73:    */   }
/*  74:    */   
/*  75:    */   @Deprecated
/*  76:    */   public ModelMap addObject(String modelName, Object modelObject)
/*  77:    */   {
/*  78:153 */     return addAttribute(modelName, modelObject);
/*  79:    */   }
/*  80:    */   
/*  81:    */   @Deprecated
/*  82:    */   public ModelMap addObject(Object modelObject)
/*  83:    */   {
/*  84:161 */     return addAttribute(modelObject);
/*  85:    */   }
/*  86:    */   
/*  87:    */   @Deprecated
/*  88:    */   public ModelMap addAllObjects(Collection objects)
/*  89:    */   {
/*  90:169 */     return addAllAttributes(objects);
/*  91:    */   }
/*  92:    */   
/*  93:    */   @Deprecated
/*  94:    */   public ModelMap addAllObjects(Map objects)
/*  95:    */   {
/*  96:177 */     return addAllAttributes(objects);
/*  97:    */   }
/*  98:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.ModelMap
 * JD-Core Version:    0.7.0.1
 */