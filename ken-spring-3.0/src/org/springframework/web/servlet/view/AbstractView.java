/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Properties;
/*  10:    */ import java.util.StringTokenizer;
/*  11:    */ import javax.servlet.ServletOutputStream;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import javax.servlet.http.HttpServletResponse;
/*  14:    */ import org.apache.commons.logging.Log;
/*  15:    */ import org.springframework.beans.factory.BeanNameAware;
/*  16:    */ import org.springframework.util.CollectionUtils;
/*  17:    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*  18:    */ import org.springframework.web.servlet.View;
/*  19:    */ import org.springframework.web.servlet.support.RequestContext;
/*  20:    */ 
/*  21:    */ public abstract class AbstractView
/*  22:    */   extends WebApplicationObjectSupport
/*  23:    */   implements View, BeanNameAware
/*  24:    */ {
/*  25:    */   public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
/*  26:    */   private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;
/*  27:    */   private String beanName;
/*  28: 66 */   private String contentType = "text/html;charset=ISO-8859-1";
/*  29:    */   private String requestContextAttribute;
/*  30: 71 */   private final Map<String, Object> staticAttributes = new HashMap();
/*  31: 74 */   private boolean exposePathVariables = true;
/*  32:    */   
/*  33:    */   public void setBeanName(String beanName)
/*  34:    */   {
/*  35: 81 */     this.beanName = beanName;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getBeanName()
/*  39:    */   {
/*  40: 89 */     return this.beanName;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setContentType(String contentType)
/*  44:    */   {
/*  45: 99 */     this.contentType = contentType;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String getContentType()
/*  49:    */   {
/*  50:106 */     return this.contentType;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setRequestContextAttribute(String requestContextAttribute)
/*  54:    */   {
/*  55:114 */     this.requestContextAttribute = requestContextAttribute;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getRequestContextAttribute()
/*  59:    */   {
/*  60:121 */     return this.requestContextAttribute;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setAttributesCSV(String propString)
/*  64:    */     throws IllegalArgumentException
/*  65:    */   {
/*  66:132 */     if (propString != null)
/*  67:    */     {
/*  68:133 */       StringTokenizer st = new StringTokenizer(propString, ",");
/*  69:134 */       while (st.hasMoreTokens())
/*  70:    */       {
/*  71:135 */         String tok = st.nextToken();
/*  72:136 */         int eqIdx = tok.indexOf("=");
/*  73:137 */         if (eqIdx == -1) {
/*  74:138 */           throw new IllegalArgumentException("Expected = in attributes CSV string '" + propString + "'");
/*  75:    */         }
/*  76:140 */         if (eqIdx >= tok.length() - 2) {
/*  77:141 */           throw new IllegalArgumentException(
/*  78:142 */             "At least 2 characters ([]) required in attributes CSV string '" + propString + "'");
/*  79:    */         }
/*  80:144 */         String name = tok.substring(0, eqIdx);
/*  81:145 */         String value = tok.substring(eqIdx + 1);
/*  82:    */         
/*  83:    */ 
/*  84:148 */         value = value.substring(1);
/*  85:149 */         value = value.substring(0, value.length() - 1);
/*  86:    */         
/*  87:151 */         addStaticAttribute(name, value);
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setAttributes(Properties attributes)
/*  93:    */   {
/*  94:170 */     CollectionUtils.mergePropertiesIntoMap(attributes, this.staticAttributes);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setAttributesMap(Map<String, ?> attributes)
/*  98:    */   {
/*  99:183 */     if (attributes != null) {
/* 100:184 */       for (Map.Entry<String, ?> entry : attributes.entrySet()) {
/* 101:185 */         addStaticAttribute((String)entry.getKey(), entry.getValue());
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public Map<String, Object> getAttributesMap()
/* 107:    */   {
/* 108:198 */     return this.staticAttributes;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void addStaticAttribute(String name, Object value)
/* 112:    */   {
/* 113:212 */     this.staticAttributes.put(name, value);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public Map<String, Object> getStaticAttributes()
/* 117:    */   {
/* 118:222 */     return Collections.unmodifiableMap(this.staticAttributes);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setExposePathVariables(boolean exposePathVariables)
/* 122:    */   {
/* 123:237 */     this.exposePathVariables = exposePathVariables;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean isExposePathVariables()
/* 127:    */   {
/* 128:244 */     return this.exposePathVariables;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/* 132:    */     throws Exception
/* 133:    */   {
/* 134:254 */     if (this.logger.isTraceEnabled()) {
/* 135:255 */       this.logger.trace("Rendering view with name '" + this.beanName + "' with model " + model + 
/* 136:256 */         " and static attributes " + this.staticAttributes);
/* 137:    */     }
/* 138:259 */     Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
/* 139:    */     
/* 140:261 */     prepareResponse(request, response);
/* 141:262 */     renderMergedOutputModel(mergedModel, request, response);
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected Map<String, Object> createMergedOutputModel(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/* 145:    */   {
/* 146:272 */     Map<String, Object> pathVars = this.exposePathVariables ? 
/* 147:273 */       (Map)request.getAttribute(View.PATH_VARIABLES) : null;
/* 148:    */     
/* 149:    */ 
/* 150:276 */     int size = this.staticAttributes.size();
/* 151:277 */     size += (model != null ? model.size() : 0);
/* 152:278 */     size += (pathVars != null ? pathVars.size() : 0);
/* 153:279 */     Map<String, Object> mergedModel = new HashMap(size);
/* 154:280 */     mergedModel.putAll(this.staticAttributes);
/* 155:281 */     if (pathVars != null) {
/* 156:282 */       mergedModel.putAll(pathVars);
/* 157:    */     }
/* 158:284 */     if (model != null) {
/* 159:285 */       mergedModel.putAll(model);
/* 160:    */     }
/* 161:289 */     if (this.requestContextAttribute != null) {
/* 162:290 */       mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel));
/* 163:    */     }
/* 164:293 */     return mergedModel;
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model)
/* 168:    */   {
/* 169:310 */     return new RequestContext(request, response, getServletContext(), model);
/* 170:    */   }
/* 171:    */   
/* 172:    */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response)
/* 173:    */   {
/* 174:321 */     if (generatesDownloadContent())
/* 175:    */     {
/* 176:322 */       response.setHeader("Pragma", "private");
/* 177:323 */       response.setHeader("Cache-Control", "private, must-revalidate");
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected boolean generatesDownloadContent()
/* 182:    */   {
/* 183:338 */     return false;
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected abstract void renderMergedOutputModel(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 187:    */     throws Exception;
/* 188:    */   
/* 189:    */   protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request)
/* 190:    */     throws Exception
/* 191:    */   {
/* 192:365 */     for (Map.Entry<String, Object> entry : model.entrySet())
/* 193:    */     {
/* 194:366 */       String modelName = (String)entry.getKey();
/* 195:367 */       Object modelValue = entry.getValue();
/* 196:368 */       if (modelValue != null)
/* 197:    */       {
/* 198:369 */         request.setAttribute(modelName, modelValue);
/* 199:370 */         if (this.logger.isDebugEnabled()) {
/* 200:371 */           this.logger.debug("Added model object '" + modelName + "' of type [" + modelValue.getClass().getName() + 
/* 201:372 */             "] to request in view with name '" + getBeanName() + "'");
/* 202:    */         }
/* 203:    */       }
/* 204:    */       else
/* 205:    */       {
/* 206:376 */         request.removeAttribute(modelName);
/* 207:377 */         if (this.logger.isDebugEnabled()) {
/* 208:378 */           this.logger.debug("Removed model object '" + modelName + 
/* 209:379 */             "' from request in view with name '" + getBeanName() + "'");
/* 210:    */         }
/* 211:    */       }
/* 212:    */     }
/* 213:    */   }
/* 214:    */   
/* 215:    */   protected ByteArrayOutputStream createTemporaryOutputStream()
/* 216:    */   {
/* 217:391 */     return new ByteArrayOutputStream(4096);
/* 218:    */   }
/* 219:    */   
/* 220:    */   protected void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos)
/* 221:    */     throws IOException
/* 222:    */   {
/* 223:402 */     response.setContentType(getContentType());
/* 224:403 */     response.setContentLength(baos.size());
/* 225:    */     
/* 226:    */ 
/* 227:406 */     ServletOutputStream out = response.getOutputStream();
/* 228:407 */     baos.writeTo(out);
/* 229:408 */     out.flush();
/* 230:    */   }
/* 231:    */   
/* 232:    */   public String toString()
/* 233:    */   {
/* 234:414 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 235:415 */     if (getBeanName() != null) {
/* 236:416 */       sb.append(": name '").append(getBeanName()).append("'");
/* 237:    */     } else {
/* 238:419 */       sb.append(": unnamed");
/* 239:    */     }
/* 240:421 */     return sb.toString();
/* 241:    */   }
/* 242:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.AbstractView
 * JD-Core Version:    0.7.0.1
 */