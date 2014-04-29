/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.LinkedList;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.servlet.ServletRequest;
/*  10:    */ import javax.servlet.ServletResponse;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import javax.servlet.http.HttpServletResponse;
/*  13:    */ import javax.servlet.jsp.JspException;
/*  14:    */ import javax.servlet.jsp.JspWriter;
/*  15:    */ import javax.servlet.jsp.PageContext;
/*  16:    */ import org.springframework.util.StringUtils;
/*  17:    */ import org.springframework.web.servlet.support.RequestContext;
/*  18:    */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*  19:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  20:    */ import org.springframework.web.util.HtmlUtils;
/*  21:    */ import org.springframework.web.util.JavaScriptUtils;
/*  22:    */ import org.springframework.web.util.TagUtils;
/*  23:    */ import org.springframework.web.util.UriUtils;
/*  24:    */ 
/*  25:    */ public class UrlTag
/*  26:    */   extends HtmlEscapingAwareTag
/*  27:    */   implements ParamAware
/*  28:    */ {
/*  29:    */   private static final String URL_TEMPLATE_DELIMITER_PREFIX = "{";
/*  30:    */   private static final String URL_TEMPLATE_DELIMITER_SUFFIX = "}";
/*  31:    */   private static final String URL_TYPE_ABSOLUTE = "://";
/*  32:    */   private List<Param> params;
/*  33:    */   private Set<String> templateParams;
/*  34:    */   private UrlType type;
/*  35:    */   private String value;
/*  36:    */   private String context;
/*  37:    */   private String var;
/*  38: 99 */   private int scope = 1;
/*  39:101 */   private boolean javaScriptEscape = false;
/*  40:    */   
/*  41:    */   public void setValue(String value)
/*  42:    */   {
/*  43:108 */     if (value.contains("://"))
/*  44:    */     {
/*  45:109 */       this.type = UrlType.ABSOLUTE;
/*  46:110 */       this.value = value;
/*  47:    */     }
/*  48:112 */     else if (value.startsWith("/"))
/*  49:    */     {
/*  50:113 */       this.type = UrlType.CONTEXT_RELATIVE;
/*  51:114 */       this.value = value;
/*  52:    */     }
/*  53:    */     else
/*  54:    */     {
/*  55:117 */       this.type = UrlType.RELATIVE;
/*  56:118 */       this.value = value;
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setContext(String context)
/*  61:    */   {
/*  62:126 */     if (context.startsWith("/")) {
/*  63:127 */       this.context = context;
/*  64:    */     } else {
/*  65:130 */       this.context = ("/" + context);
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setVar(String var)
/*  70:    */   {
/*  71:139 */     this.var = var;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setScope(String scope)
/*  75:    */   {
/*  76:147 */     this.scope = TagUtils.getScope(scope);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setJavaScriptEscape(String javaScriptEscape)
/*  80:    */     throws JspException
/*  81:    */   {
/*  82:155 */     this.javaScriptEscape = 
/*  83:156 */       ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, this.pageContext);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void addParam(Param param)
/*  87:    */   {
/*  88:160 */     this.params.add(param);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public int doStartTagInternal()
/*  92:    */     throws JspException
/*  93:    */   {
/*  94:166 */     this.params = new LinkedList();
/*  95:167 */     this.templateParams = new HashSet();
/*  96:168 */     return 1;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int doEndTag()
/* 100:    */     throws JspException
/* 101:    */   {
/* 102:173 */     String url = createUrl();
/* 103:    */     
/* 104:175 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 105:176 */     ServletRequest request = this.pageContext.getRequest();
/* 106:177 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 107:178 */       url = processor.processUrl((HttpServletRequest)request, url);
/* 108:    */     }
/* 109:181 */     if (this.var == null) {
/* 110:    */       try
/* 111:    */       {
/* 112:184 */         this.pageContext.getOut().print(url);
/* 113:    */       }
/* 114:    */       catch (IOException e)
/* 115:    */       {
/* 116:187 */         throw new JspException(e);
/* 117:    */       }
/* 118:    */     } else {
/* 119:192 */       this.pageContext.setAttribute(this.var, url, this.scope);
/* 120:    */     }
/* 121:194 */     return 6;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private String createUrl()
/* 125:    */     throws JspException
/* 126:    */   {
/* 127:204 */     HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
/* 128:205 */     HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
/* 129:206 */     StringBuilder url = new StringBuilder();
/* 130:207 */     if (this.type == UrlType.CONTEXT_RELATIVE) {
/* 131:209 */       if (this.context == null) {
/* 132:210 */         url.append(request.getContextPath());
/* 133:    */       } else {
/* 134:213 */         url.append(this.context);
/* 135:    */       }
/* 136:    */     }
/* 137:216 */     if ((this.type != UrlType.RELATIVE) && (this.type != UrlType.ABSOLUTE) && (!this.value.startsWith("/"))) {
/* 138:217 */       url.append("/");
/* 139:    */     }
/* 140:219 */     url.append(replaceUriTemplateParams(this.value, this.params, this.templateParams));
/* 141:220 */     url.append(createQueryString(this.params, this.templateParams, url.indexOf("?") == -1));
/* 142:    */     
/* 143:222 */     String urlStr = url.toString();
/* 144:223 */     if (this.type != UrlType.ABSOLUTE) {
/* 145:226 */       urlStr = response.encodeURL(urlStr);
/* 146:    */     }
/* 147:230 */     urlStr = isHtmlEscape() ? HtmlUtils.htmlEscape(urlStr) : urlStr;
/* 148:231 */     urlStr = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(urlStr) : urlStr;
/* 149:    */     
/* 150:233 */     return urlStr;
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected String createQueryString(List<Param> params, Set<String> usedParams, boolean includeQueryStringDelimiter)
/* 154:    */     throws JspException
/* 155:    */   {
/* 156:250 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/* 157:251 */     StringBuilder qs = new StringBuilder();
/* 158:252 */     for (Param param : params) {
/* 159:253 */       if ((!usedParams.contains(param.getName())) && (StringUtils.hasLength(param.getName())))
/* 160:    */       {
/* 161:254 */         if ((includeQueryStringDelimiter) && (qs.length() == 0)) {
/* 162:255 */           qs.append("?");
/* 163:    */         } else {
/* 164:258 */           qs.append("&");
/* 165:    */         }
/* 166:    */         try
/* 167:    */         {
/* 168:261 */           qs.append(UriUtils.encodeQueryParam(param.getName(), encoding));
/* 169:262 */           if (param.getValue() != null)
/* 170:    */           {
/* 171:263 */             qs.append("=");
/* 172:264 */             qs.append(UriUtils.encodeQueryParam(param.getValue(), encoding));
/* 173:    */           }
/* 174:    */         }
/* 175:    */         catch (UnsupportedEncodingException ex)
/* 176:    */         {
/* 177:268 */           throw new JspException(ex);
/* 178:    */         }
/* 179:    */       }
/* 180:    */     }
/* 181:272 */     return qs.toString();
/* 182:    */   }
/* 183:    */   
/* 184:    */   protected String replaceUriTemplateParams(String uri, List<Param> params, Set<String> usedParams)
/* 185:    */     throws JspException
/* 186:    */   {
/* 187:287 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/* 188:288 */     for (Param param : params)
/* 189:    */     {
/* 190:289 */       String template = "{" + param.getName() + "}";
/* 191:290 */       if (uri.contains(template))
/* 192:    */       {
/* 193:291 */         usedParams.add(param.getName());
/* 194:    */         try
/* 195:    */         {
/* 196:293 */           uri = uri.replace(template, UriUtils.encodePath(param.getValue(), encoding));
/* 197:    */         }
/* 198:    */         catch (UnsupportedEncodingException ex)
/* 199:    */         {
/* 200:296 */           throw new JspException(ex);
/* 201:    */         }
/* 202:    */       }
/* 203:    */     }
/* 204:300 */     return uri;
/* 205:    */   }
/* 206:    */   
/* 207:    */   private static enum UrlType
/* 208:    */   {
/* 209:307 */     CONTEXT_RELATIVE,  RELATIVE,  ABSOLUTE;
/* 210:    */   }
/* 211:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.UrlTag
 * JD-Core Version:    0.7.0.1
 */