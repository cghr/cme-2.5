/*   1:    */ package org.springframework.web.servlet.view.json;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Set;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ import org.codehaus.jackson.JsonEncoding;
/*  13:    */ import org.codehaus.jackson.JsonFactory;
/*  14:    */ import org.codehaus.jackson.JsonGenerator;
/*  15:    */ import org.codehaus.jackson.map.ObjectMapper;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ import org.springframework.util.CollectionUtils;
/*  18:    */ import org.springframework.validation.BindingResult;
/*  19:    */ import org.springframework.web.servlet.view.AbstractView;
/*  20:    */ 
/*  21:    */ public class MappingJacksonJsonView
/*  22:    */   extends AbstractView
/*  23:    */ {
/*  24:    */   public static final String DEFAULT_CONTENT_TYPE = "application/json";
/*  25: 59 */   private ObjectMapper objectMapper = new ObjectMapper();
/*  26: 61 */   private JsonEncoding encoding = JsonEncoding.UTF8;
/*  27: 63 */   private boolean prefixJson = false;
/*  28:    */   private Set<String> modelKeys;
/*  29: 67 */   private boolean extractValueFromSingleKeyModel = false;
/*  30: 69 */   private boolean disableCaching = true;
/*  31:    */   
/*  32:    */   public MappingJacksonJsonView()
/*  33:    */   {
/*  34: 75 */     setContentType("application/json");
/*  35: 76 */     setExposePathVariables(false);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setObjectMapper(ObjectMapper objectMapper)
/*  39:    */   {
/*  40: 89 */     Assert.notNull(objectMapper, "'objectMapper' must not be null");
/*  41: 90 */     this.objectMapper = objectMapper;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setEncoding(JsonEncoding encoding)
/*  45:    */   {
/*  46: 97 */     Assert.notNull(encoding, "'encoding' must not be null");
/*  47: 98 */     this.encoding = encoding;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setPrefixJson(boolean prefixJson)
/*  51:    */   {
/*  52:109 */     this.prefixJson = prefixJson;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setModelKey(String modelKey)
/*  56:    */   {
/*  57:117 */     this.modelKeys = Collections.singleton(modelKey);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setModelKeys(Set<String> modelKeys)
/*  61:    */   {
/*  62:125 */     this.modelKeys = modelKeys;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Set<String> getModelKeys()
/*  66:    */   {
/*  67:132 */     return this.modelKeys;
/*  68:    */   }
/*  69:    */   
/*  70:    */   /**
/*  71:    */    * @deprecated
/*  72:    */    */
/*  73:    */   public void setRenderedAttributes(Set<String> renderedAttributes)
/*  74:    */   {
/*  75:141 */     this.modelKeys = renderedAttributes;
/*  76:    */   }
/*  77:    */   
/*  78:    */   /**
/*  79:    */    * @deprecated
/*  80:    */    */
/*  81:    */   public Set<String> getRenderedAttributes()
/*  82:    */   {
/*  83:149 */     return this.modelKeys;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setDisableCaching(boolean disableCaching)
/*  87:    */   {
/*  88:158 */     this.disableCaching = disableCaching;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel)
/*  92:    */   {
/*  93:171 */     this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response)
/*  97:    */   {
/*  98:176 */     response.setContentType(getContentType());
/*  99:177 */     response.setCharacterEncoding(this.encoding.getJavaName());
/* 100:178 */     if (this.disableCaching)
/* 101:    */     {
/* 102:179 */       response.addHeader("Pragma", "no-cache");
/* 103:180 */       response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
/* 104:181 */       response.addDateHeader("Expires", 1L);
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 109:    */     throws Exception
/* 110:    */   {
/* 111:189 */     Object value = filterModel(model);
/* 112:190 */     JsonGenerator generator = 
/* 113:191 */       this.objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(), this.encoding);
/* 114:192 */     if (this.prefixJson) {
/* 115:193 */       generator.writeRaw("{} && ");
/* 116:    */     }
/* 117:195 */     this.objectMapper.writeValue(generator, value);
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected Object filterModel(Map<String, Object> model)
/* 121:    */   {
/* 122:209 */     Map<String, Object> result = new HashMap(model.size());
/* 123:210 */     Set<String> renderedAttributes = 
/* 124:211 */       !CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet();
/* 125:212 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 126:213 */       if ((!(entry.getValue() instanceof BindingResult)) && (renderedAttributes.contains(entry.getKey()))) {
/* 127:214 */         result.put((String)entry.getKey(), entry.getValue());
/* 128:    */       }
/* 129:    */     }
/* 130:217 */     return (this.extractValueFromSingleKeyModel) && (result.size() == 1) ? result.values().iterator().next() : result;
/* 131:    */   }
/* 132:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.json.MappingJacksonJsonView
 * JD-Core Version:    0.7.0.1
 */