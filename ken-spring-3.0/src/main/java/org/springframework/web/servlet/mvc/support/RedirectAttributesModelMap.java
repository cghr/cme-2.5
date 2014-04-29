/*   1:    */ package org.springframework.web.servlet.mvc.support;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.ui.ModelMap;
/*   6:    */ import org.springframework.validation.DataBinder;
/*   7:    */ 
/*   8:    */ public class RedirectAttributesModelMap
/*   9:    */   extends ModelMap
/*  10:    */   implements RedirectAttributes
/*  11:    */ {
/*  12:    */   private final DataBinder dataBinder;
/*  13: 39 */   private final ModelMap flashAttributes = new ModelMap();
/*  14:    */   
/*  15:    */   public RedirectAttributesModelMap(DataBinder dataBinder)
/*  16:    */   {
/*  17: 46 */     this.dataBinder = dataBinder;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public RedirectAttributesModelMap()
/*  21:    */   {
/*  22: 54 */     this(null);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Map<String, ?> getFlashAttributes()
/*  26:    */   {
/*  27: 61 */     return this.flashAttributes;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public RedirectAttributesModelMap addAttribute(String attributeName, Object attributeValue)
/*  31:    */   {
/*  32: 69 */     super.addAttribute(attributeName, formatValue(attributeValue));
/*  33: 70 */     return this;
/*  34:    */   }
/*  35:    */   
/*  36:    */   private String formatValue(Object value)
/*  37:    */   {
/*  38: 74 */     if (value == null) {
/*  39: 75 */       return null;
/*  40:    */     }
/*  41: 77 */     return this.dataBinder != null ? (String)this.dataBinder.convertIfNecessary(value, String.class) : value.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public RedirectAttributesModelMap addAttribute(Object attributeValue)
/*  45:    */   {
/*  46: 85 */     super.addAttribute(attributeValue);
/*  47: 86 */     return this;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public RedirectAttributesModelMap addAllAttributes(Collection<?> attributeValues)
/*  51:    */   {
/*  52: 94 */     super.addAllAttributes(attributeValues);
/*  53: 95 */     return this;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public RedirectAttributesModelMap addAllAttributes(Map<String, ?> attributes)
/*  57:    */   {
/*  58:103 */     if (attributes != null) {
/*  59:104 */       for (String key : attributes.keySet()) {
/*  60:105 */         addAttribute(key, attributes.get(key));
/*  61:    */       }
/*  62:    */     }
/*  63:108 */     return this;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public RedirectAttributesModelMap mergeAttributes(Map<String, ?> attributes)
/*  67:    */   {
/*  68:116 */     if (attributes != null) {
/*  69:117 */       for (String key : attributes.keySet()) {
/*  70:118 */         if (!containsKey(key)) {
/*  71:119 */           addAttribute(key, attributes.get(key));
/*  72:    */         }
/*  73:    */       }
/*  74:    */     }
/*  75:123 */     return this;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Map<String, Object> asMap()
/*  79:    */   {
/*  80:127 */     return this;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Object put(String key, Object value)
/*  84:    */   {
/*  85:136 */     return super.put(key, formatValue(value));
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void putAll(Map<? extends String, ? extends Object> map)
/*  89:    */   {
/*  90:145 */     if (map != null) {
/*  91:146 */       for (String key : map.keySet()) {
/*  92:147 */         put(key, formatValue(map.get(key)));
/*  93:    */       }
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public RedirectAttributes addFlashAttribute(String attributeName, Object attributeValue)
/*  98:    */   {
/*  99:153 */     this.flashAttributes.addAttribute(attributeName, attributeValue);
/* 100:154 */     return this;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public RedirectAttributes addFlashAttribute(Object attributeValue)
/* 104:    */   {
/* 105:158 */     this.flashAttributes.addAttribute(attributeValue);
/* 106:159 */     return this;
/* 107:    */   }
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap
 * JD-Core Version:    0.7.0.1
 */