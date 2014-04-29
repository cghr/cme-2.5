/*   1:    */ package org.springframework.web.context.request;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpSession;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.web.util.WebUtils;
/*  11:    */ 
/*  12:    */ public class ServletRequestAttributes
/*  13:    */   extends AbstractRequestAttributes
/*  14:    */ {
/*  15: 46 */   public static final String DESTRUCTION_CALLBACK_NAME_PREFIX = ServletRequestAttributes.class.getName() + ".DESTRUCTION_CALLBACK.";
/*  16:    */   private final HttpServletRequest request;
/*  17:    */   private volatile HttpSession session;
/*  18: 53 */   private final Map<String, Object> sessionAttributesToUpdate = new HashMap();
/*  19:    */   
/*  20:    */   public ServletRequestAttributes(HttpServletRequest request)
/*  21:    */   {
/*  22: 61 */     Assert.notNull(request, "Request must not be null");
/*  23: 62 */     this.request = request;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public final HttpServletRequest getRequest()
/*  27:    */   {
/*  28: 70 */     return this.request;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected final HttpSession getSession(boolean allowCreate)
/*  32:    */   {
/*  33: 78 */     if (isRequestActive()) {
/*  34: 79 */       return this.request.getSession(allowCreate);
/*  35:    */     }
/*  36: 83 */     if ((this.session == null) && (allowCreate)) {
/*  37: 84 */       throw new IllegalStateException(
/*  38: 85 */         "No session found and request already completed - cannot create new session!");
/*  39:    */     }
/*  40: 87 */     return this.session;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Object getAttribute(String name, int scope)
/*  44:    */   {
/*  45: 93 */     if (scope == 0)
/*  46:    */     {
/*  47: 94 */       if (!isRequestActive()) {
/*  48: 95 */         throw new IllegalStateException(
/*  49: 96 */           "Cannot ask for request attribute - request is not active anymore!");
/*  50:    */       }
/*  51: 98 */       return this.request.getAttribute(name);
/*  52:    */     }
/*  53:101 */     HttpSession session = getSession(false);
/*  54:102 */     if (session != null) {
/*  55:    */       try
/*  56:    */       {
/*  57:104 */         Object value = session.getAttribute(name);
/*  58:105 */         if (value != null) {
/*  59:106 */           synchronized (this.sessionAttributesToUpdate)
/*  60:    */           {
/*  61:107 */             this.sessionAttributesToUpdate.put(name, value);
/*  62:    */           }
/*  63:    */         }
/*  64:110 */         return value;
/*  65:    */       }
/*  66:    */       catch (IllegalStateException localIllegalStateException) {}
/*  67:    */     }
/*  68:116 */     return null;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setAttribute(String name, Object value, int scope)
/*  72:    */   {
/*  73:121 */     if (scope == 0)
/*  74:    */     {
/*  75:122 */       if (!isRequestActive()) {
/*  76:123 */         throw new IllegalStateException(
/*  77:124 */           "Cannot set request attribute - request is not active anymore!");
/*  78:    */       }
/*  79:126 */       this.request.setAttribute(name, value);
/*  80:    */     }
/*  81:    */     else
/*  82:    */     {
/*  83:129 */       HttpSession session = getSession(true);
/*  84:130 */       synchronized (this.sessionAttributesToUpdate)
/*  85:    */       {
/*  86:131 */         this.sessionAttributesToUpdate.remove(name);
/*  87:    */       }
/*  88:133 */       session.setAttribute(name, value);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void removeAttribute(String name, int scope)
/*  93:    */   {
/*  94:138 */     if (scope == 0)
/*  95:    */     {
/*  96:139 */       if (isRequestActive())
/*  97:    */       {
/*  98:140 */         this.request.removeAttribute(name);
/*  99:141 */         removeRequestDestructionCallback(name);
/* 100:    */       }
/* 101:    */     }
/* 102:    */     else
/* 103:    */     {
/* 104:145 */       HttpSession session = getSession(false);
/* 105:146 */       if (session != null)
/* 106:    */       {
/* 107:147 */         synchronized (this.sessionAttributesToUpdate)
/* 108:    */         {
/* 109:148 */           this.sessionAttributesToUpdate.remove(name);
/* 110:    */         }
/* 111:    */         try
/* 112:    */         {
/* 113:151 */           session.removeAttribute(name);
/* 114:    */           
/* 115:153 */           session.removeAttribute(DESTRUCTION_CALLBACK_NAME_PREFIX + name);
/* 116:    */         }
/* 117:    */         catch (IllegalStateException localIllegalStateException) {}
/* 118:    */       }
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String[] getAttributeNames(int scope)
/* 123:    */   {
/* 124:164 */     if (scope == 0)
/* 125:    */     {
/* 126:165 */       if (!isRequestActive()) {
/* 127:166 */         throw new IllegalStateException(
/* 128:167 */           "Cannot ask for request attributes - request is not active anymore!");
/* 129:    */       }
/* 130:169 */       return StringUtils.toStringArray(this.request.getAttributeNames());
/* 131:    */     }
/* 132:172 */     HttpSession session = getSession(false);
/* 133:173 */     if (session != null) {
/* 134:    */       try
/* 135:    */       {
/* 136:175 */         return StringUtils.toStringArray(session.getAttributeNames());
/* 137:    */       }
/* 138:    */       catch (IllegalStateException localIllegalStateException) {}
/* 139:    */     }
/* 140:181 */     return new String[0];
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void registerDestructionCallback(String name, Runnable callback, int scope)
/* 144:    */   {
/* 145:186 */     if (scope == 0) {
/* 146:187 */       registerRequestDestructionCallback(name, callback);
/* 147:    */     } else {
/* 148:190 */       registerSessionDestructionCallback(name, callback);
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public Object resolveReference(String key)
/* 153:    */   {
/* 154:195 */     if ("request".equals(key)) {
/* 155:196 */       return this.request;
/* 156:    */     }
/* 157:198 */     if ("session".equals(key)) {
/* 158:199 */       return getSession(true);
/* 159:    */     }
/* 160:202 */     return null;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public String getSessionId()
/* 164:    */   {
/* 165:207 */     return getSession(true).getId();
/* 166:    */   }
/* 167:    */   
/* 168:    */   public Object getSessionMutex()
/* 169:    */   {
/* 170:211 */     return WebUtils.getSessionMutex(getSession(true));
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected void updateAccessedSessionAttributes()
/* 174:    */   {
/* 175:222 */     this.session = this.request.getSession(false);
/* 176:224 */     synchronized (this.sessionAttributesToUpdate)
/* 177:    */     {
/* 178:225 */       if (this.session != null) {
/* 179:    */         try
/* 180:    */         {
/* 181:227 */           for (Map.Entry<String, Object> entry : this.sessionAttributesToUpdate.entrySet())
/* 182:    */           {
/* 183:228 */             String name = (String)entry.getKey();
/* 184:229 */             Object newValue = entry.getValue();
/* 185:230 */             Object oldValue = this.session.getAttribute(name);
/* 186:231 */             if (oldValue == newValue) {
/* 187:232 */               this.session.setAttribute(name, newValue);
/* 188:    */             }
/* 189:    */           }
/* 190:    */         }
/* 191:    */         catch (IllegalStateException localIllegalStateException) {}
/* 192:    */       }
/* 193:240 */       this.sessionAttributesToUpdate.clear();
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   protected void registerSessionDestructionCallback(String name, Runnable callback)
/* 198:    */   {
/* 199:252 */     HttpSession session = getSession(true);
/* 200:253 */     session.setAttribute(DESTRUCTION_CALLBACK_NAME_PREFIX + name, 
/* 201:254 */       new DestructionCallbackBindingListener(callback));
/* 202:    */   }
/* 203:    */   
/* 204:    */   public String toString()
/* 205:    */   {
/* 206:260 */     return this.request.toString();
/* 207:    */   }
/* 208:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.ServletRequestAttributes
 * JD-Core Version:    0.7.0.1
 */