/*   1:    */ package org.springframework.web.method.support;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import org.springframework.ui.ModelMap;
/*   5:    */ import org.springframework.validation.support.BindingAwareModelMap;
/*   6:    */ import org.springframework.web.bind.support.SessionStatus;
/*   7:    */ import org.springframework.web.bind.support.SimpleSessionStatus;
/*   8:    */ 
/*   9:    */ public class ModelAndViewContainer
/*  10:    */ {
/*  11:    */   private Object view;
/*  12: 49 */   private boolean requestHandled = false;
/*  13: 51 */   private final ModelMap defaultModel = new BindingAwareModelMap();
/*  14:    */   private ModelMap redirectModel;
/*  15: 55 */   private boolean redirectModelScenario = false;
/*  16: 57 */   private boolean ignoreDefaultModelOnRedirect = false;
/*  17: 59 */   private final SessionStatus sessionStatus = new SimpleSessionStatus();
/*  18:    */   
/*  19:    */   public void setViewName(String viewName)
/*  20:    */   {
/*  21: 72 */     this.view = viewName;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String getViewName()
/*  25:    */   {
/*  26: 80 */     return (this.view instanceof String) ? (String)this.view : null;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setView(Object view)
/*  30:    */   {
/*  31: 88 */     this.view = view;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Object getView()
/*  35:    */   {
/*  36: 96 */     return this.view;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public boolean isViewReference()
/*  40:    */   {
/*  41:104 */     return this.view instanceof String;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setRequestHandled(boolean requestHandled)
/*  45:    */   {
/*  46:119 */     this.requestHandled = requestHandled;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isRequestHandled()
/*  50:    */   {
/*  51:126 */     return this.requestHandled;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public ModelMap getModel()
/*  55:    */   {
/*  56:136 */     if (useDefaultModel()) {
/*  57:137 */       return this.defaultModel;
/*  58:    */     }
/*  59:140 */     return this.redirectModel != null ? this.redirectModel : new ModelMap();
/*  60:    */   }
/*  61:    */   
/*  62:    */   private boolean useDefaultModel()
/*  63:    */   {
/*  64:148 */     return (!this.redirectModelScenario) || ((this.redirectModel == null) && (!this.ignoreDefaultModelOnRedirect));
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setRedirectModel(ModelMap redirectModel)
/*  68:    */   {
/*  69:158 */     this.redirectModel = redirectModel;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setRedirectModelScenario(boolean redirectModelScenario)
/*  73:    */   {
/*  74:166 */     this.redirectModelScenario = redirectModelScenario;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect)
/*  78:    */   {
/*  79:178 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public SessionStatus getSessionStatus()
/*  83:    */   {
/*  84:186 */     return this.sessionStatus;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public ModelAndViewContainer addAttribute(String name, Object value)
/*  88:    */   {
/*  89:194 */     getModel().addAttribute(name, value);
/*  90:195 */     return this;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public ModelAndViewContainer addAttribute(Object value)
/*  94:    */   {
/*  95:203 */     getModel().addAttribute(value);
/*  96:204 */     return this;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public ModelAndViewContainer addAllAttributes(Map<String, ?> attributes)
/* 100:    */   {
/* 101:212 */     getModel().addAllAttributes(attributes);
/* 102:213 */     return this;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public ModelAndViewContainer mergeAttributes(Map<String, ?> attributes)
/* 106:    */   {
/* 107:222 */     getModel().mergeAttributes(attributes);
/* 108:223 */     return this;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean containsAttribute(String name)
/* 112:    */   {
/* 113:231 */     return getModel().containsAttribute(name);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public String toString()
/* 117:    */   {
/* 118:239 */     StringBuilder sb = new StringBuilder("ModelAndViewContainer: ");
/* 119:240 */     if (!isRequestHandled())
/* 120:    */     {
/* 121:241 */       if (isViewReference()) {
/* 122:242 */         sb.append("reference to view with name '").append(this.view).append("'");
/* 123:    */       } else {
/* 124:245 */         sb.append("View is [").append(this.view).append(']');
/* 125:    */       }
/* 126:247 */       if (useDefaultModel()) {
/* 127:248 */         sb.append("; default model ");
/* 128:    */       } else {
/* 129:251 */         sb.append("; redirect model ");
/* 130:    */       }
/* 131:253 */       sb.append(getModel());
/* 132:    */     }
/* 133:    */     else
/* 134:    */     {
/* 135:256 */       sb.append("Request handled directly");
/* 136:    */     }
/* 137:258 */     return sb.toString();
/* 138:    */   }
/* 139:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.support.ModelAndViewContainer
 * JD-Core Version:    0.7.0.1
 */