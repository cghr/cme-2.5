/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.springframework.ui.ModelMap;
/*   5:    */ import org.springframework.util.CollectionUtils;
/*   6:    */ 
/*   7:    */ public class ModelAndView
/*   8:    */ {
/*   9:    */   private Object view;
/*  10:    */   private ModelMap model;
/*  11: 53 */   private boolean cleared = false;
/*  12:    */   
/*  13:    */   public ModelAndView() {}
/*  14:    */   
/*  15:    */   public ModelAndView(String viewName)
/*  16:    */   {
/*  17: 73 */     this.view = viewName;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ModelAndView(View view)
/*  21:    */   {
/*  22: 83 */     this.view = view;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ModelAndView(String viewName, Map<String, ?> model)
/*  26:    */   {
/*  27: 95 */     this.view = viewName;
/*  28: 96 */     if (model != null) {
/*  29: 97 */       getModelMap().addAllAttributes(model);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ModelAndView(View view, Map<String, ?> model)
/*  34:    */   {
/*  35:112 */     this.view = view;
/*  36:113 */     if (model != null) {
/*  37:114 */       getModelMap().addAllAttributes(model);
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public ModelAndView(String viewName, String modelName, Object modelObject)
/*  42:    */   {
/*  43:126 */     this.view = viewName;
/*  44:127 */     addObject(modelName, modelObject);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public ModelAndView(View view, String modelName, Object modelObject)
/*  48:    */   {
/*  49:137 */     this.view = view;
/*  50:138 */     addObject(modelName, modelObject);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setViewName(String viewName)
/*  54:    */   {
/*  55:148 */     this.view = viewName;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getViewName()
/*  59:    */   {
/*  60:156 */     return (this.view instanceof String) ? (String)this.view : null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setView(View view)
/*  64:    */   {
/*  65:164 */     this.view = view;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public View getView()
/*  69:    */   {
/*  70:172 */     return (this.view instanceof View) ? (View)this.view : null;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean hasView()
/*  74:    */   {
/*  75:180 */     return this.view != null;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean isReference()
/*  79:    */   {
/*  80:189 */     return this.view instanceof String;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected Map<String, Object> getModelInternal()
/*  84:    */   {
/*  85:197 */     return this.model;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public ModelMap getModelMap()
/*  89:    */   {
/*  90:204 */     if (this.model == null) {
/*  91:205 */       this.model = new ModelMap();
/*  92:    */     }
/*  93:207 */     return this.model;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public Map<String, Object> getModel()
/*  97:    */   {
/*  98:215 */     return getModelMap();
/*  99:    */   }
/* 100:    */   
/* 101:    */   public ModelAndView addObject(String attributeName, Object attributeValue)
/* 102:    */   {
/* 103:227 */     getModelMap().addAttribute(attributeName, attributeValue);
/* 104:228 */     return this;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public ModelAndView addObject(Object attributeValue)
/* 108:    */   {
/* 109:238 */     getModelMap().addAttribute(attributeValue);
/* 110:239 */     return this;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public ModelAndView addAllObjects(Map<String, ?> modelMap)
/* 114:    */   {
/* 115:249 */     getModelMap().addAllAttributes(modelMap);
/* 116:250 */     return this;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void clear()
/* 120:    */   {
/* 121:263 */     this.view = null;
/* 122:264 */     this.model = null;
/* 123:265 */     this.cleared = true;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean isEmpty()
/* 127:    */   {
/* 128:273 */     return (this.view == null) && (CollectionUtils.isEmpty(this.model));
/* 129:    */   }
/* 130:    */   
/* 131:    */   public boolean wasCleared()
/* 132:    */   {
/* 133:284 */     return (this.cleared) && (isEmpty());
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String toString()
/* 137:    */   {
/* 138:293 */     StringBuilder sb = new StringBuilder("ModelAndView: ");
/* 139:294 */     if (isReference()) {
/* 140:295 */       sb.append("reference to view with name '").append(this.view).append("'");
/* 141:    */     } else {
/* 142:298 */       sb.append("materialized View is [").append(this.view).append(']');
/* 143:    */     }
/* 144:300 */     sb.append("; model is ").append(this.model);
/* 145:301 */     return sb.toString();
/* 146:    */   }
/* 147:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.ModelAndView
 * JD-Core Version:    0.7.0.1
 */