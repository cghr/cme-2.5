/*   1:    */ package org.springframework.web.context.request;
/*   2:    */ 
/*   3:    */ import java.security.Principal;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import javax.faces.context.ExternalContext;
/*   9:    */ import javax.faces.context.FacesContext;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ 
/*  12:    */ public class FacesWebRequest
/*  13:    */   extends FacesRequestAttributes
/*  14:    */   implements NativeWebRequest
/*  15:    */ {
/*  16:    */   public FacesWebRequest(FacesContext facesContext)
/*  17:    */   {
/*  18: 42 */     super(facesContext);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Object getNativeRequest()
/*  22:    */   {
/*  23: 47 */     return getExternalContext().getRequest();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Object getNativeResponse()
/*  27:    */   {
/*  28: 51 */     return getExternalContext().getResponse();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public <T> T getNativeRequest(Class<T> requiredType)
/*  32:    */   {
/*  33: 56 */     if (requiredType != null)
/*  34:    */     {
/*  35: 57 */       Object request = getExternalContext().getRequest();
/*  36: 58 */       if (requiredType.isInstance(request)) {
/*  37: 59 */         return request;
/*  38:    */       }
/*  39:    */     }
/*  40: 62 */     return null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public <T> T getNativeResponse(Class<T> requiredType)
/*  44:    */   {
/*  45: 67 */     if (requiredType != null)
/*  46:    */     {
/*  47: 68 */       Object response = getExternalContext().getResponse();
/*  48: 69 */       if (requiredType.isInstance(response)) {
/*  49: 70 */         return response;
/*  50:    */       }
/*  51:    */     }
/*  52: 73 */     return null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getHeader(String headerName)
/*  56:    */   {
/*  57: 78 */     return (String)getExternalContext().getRequestHeaderMap().get(headerName);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String[] getHeaderValues(String headerName)
/*  61:    */   {
/*  62: 82 */     return (String[])getExternalContext().getRequestHeaderValuesMap().get(headerName);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Iterator<String> getHeaderNames()
/*  66:    */   {
/*  67: 86 */     return getExternalContext().getRequestHeaderMap().keySet().iterator();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getParameter(String paramName)
/*  71:    */   {
/*  72: 90 */     return (String)getExternalContext().getRequestParameterMap().get(paramName);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public Iterator<String> getParameterNames()
/*  76:    */   {
/*  77: 94 */     return getExternalContext().getRequestParameterNames();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String[] getParameterValues(String paramName)
/*  81:    */   {
/*  82: 98 */     return (String[])getExternalContext().getRequestParameterValuesMap().get(paramName);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Map<String, String[]> getParameterMap()
/*  86:    */   {
/*  87:102 */     return getExternalContext().getRequestParameterValuesMap();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Locale getLocale()
/*  91:    */   {
/*  92:106 */     return getFacesContext().getExternalContext().getRequestLocale();
/*  93:    */   }
/*  94:    */   
/*  95:    */   public String getContextPath()
/*  96:    */   {
/*  97:110 */     return getFacesContext().getExternalContext().getRequestContextPath();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getRemoteUser()
/* 101:    */   {
/* 102:114 */     return getFacesContext().getExternalContext().getRemoteUser();
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Principal getUserPrincipal()
/* 106:    */   {
/* 107:118 */     return getFacesContext().getExternalContext().getUserPrincipal();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isUserInRole(String role)
/* 111:    */   {
/* 112:122 */     return getFacesContext().getExternalContext().isUserInRole(role);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean isSecure()
/* 116:    */   {
/* 117:126 */     return false;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public boolean checkNotModified(long lastModifiedTimestamp)
/* 121:    */   {
/* 122:130 */     return false;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean checkNotModified(String eTag)
/* 126:    */   {
/* 127:134 */     return false;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String getDescription(boolean includeClientInfo)
/* 131:    */   {
/* 132:138 */     ExternalContext externalContext = getExternalContext();
/* 133:139 */     StringBuilder sb = new StringBuilder();
/* 134:140 */     sb.append("context=").append(externalContext.getRequestContextPath());
/* 135:141 */     if (includeClientInfo)
/* 136:    */     {
/* 137:142 */       Object session = externalContext.getSession(false);
/* 138:143 */       if (session != null) {
/* 139:144 */         sb.append(";session=").append(getSessionId());
/* 140:    */       }
/* 141:146 */       String user = externalContext.getRemoteUser();
/* 142:147 */       if (StringUtils.hasLength(user)) {
/* 143:148 */         sb.append(";user=").append(user);
/* 144:    */       }
/* 145:    */     }
/* 146:151 */     return sb.toString();
/* 147:    */   }
/* 148:    */   
/* 149:    */   public String toString()
/* 150:    */   {
/* 151:157 */     return "FacesWebRequest: " + getDescription(true);
/* 152:    */   }
/* 153:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.FacesWebRequest
 * JD-Core Version:    0.7.0.1
 */