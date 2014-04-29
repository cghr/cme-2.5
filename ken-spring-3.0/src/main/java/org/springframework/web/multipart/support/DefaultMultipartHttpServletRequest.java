/*   1:    */ package org.springframework.web.multipart.support;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.HashSet;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Set;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import org.springframework.http.HttpHeaders;
/*  12:    */ import org.springframework.util.MultiValueMap;
/*  13:    */ import org.springframework.web.multipart.MultipartFile;
/*  14:    */ 
/*  15:    */ public class DefaultMultipartHttpServletRequest
/*  16:    */   extends AbstractMultipartHttpServletRequest
/*  17:    */ {
/*  18:    */   private static final String CONTENT_TYPE = "Content-Type";
/*  19:    */   private Map<String, String[]> multipartParameters;
/*  20:    */   private Map<String, String> multipartParameterContentTypes;
/*  21:    */   
/*  22:    */   public DefaultMultipartHttpServletRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes)
/*  23:    */   {
/*  24: 61 */     super(request);
/*  25: 62 */     setMultipartFiles(mpFiles);
/*  26: 63 */     setMultipartParameters(mpParams);
/*  27: 64 */     setMultipartParameterContentTypes(mpParamContentTypes);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DefaultMultipartHttpServletRequest(HttpServletRequest request)
/*  31:    */   {
/*  32: 72 */     super(request);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public Enumeration<String> getParameterNames()
/*  36:    */   {
/*  37: 78 */     Set<String> paramNames = new HashSet();
/*  38: 79 */     Enumeration paramEnum = super.getParameterNames();
/*  39: 80 */     while (paramEnum.hasMoreElements()) {
/*  40: 81 */       paramNames.add((String)paramEnum.nextElement());
/*  41:    */     }
/*  42: 83 */     paramNames.addAll((Collection)getMultipartParameters().keySet());
/*  43: 84 */     return Collections.enumeration(paramNames);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getParameter(String name)
/*  47:    */   {
/*  48: 89 */     String[] values = (String[])getMultipartParameters().get(name);
/*  49: 90 */     if (values != null) {
/*  50: 91 */       return values.length > 0 ? values[0] : null;
/*  51:    */     }
/*  52: 93 */     return super.getParameter(name);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String[] getParameterValues(String name)
/*  56:    */   {
/*  57: 98 */     String[] values = (String[])getMultipartParameters().get(name);
/*  58: 99 */     if (values != null) {
/*  59:100 */       return values;
/*  60:    */     }
/*  61:102 */     return super.getParameterValues(name);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Map<String, String[]> getParameterMap()
/*  65:    */   {
/*  66:108 */     Map<String, String[]> paramMap = new HashMap();
/*  67:109 */     paramMap.putAll(super.getParameterMap());
/*  68:110 */     paramMap.putAll(getMultipartParameters());
/*  69:111 */     return paramMap;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getMultipartContentType(String paramOrFileName)
/*  73:    */   {
/*  74:115 */     MultipartFile file = getFile(paramOrFileName);
/*  75:116 */     if (file != null) {
/*  76:117 */       return file.getContentType();
/*  77:    */     }
/*  78:120 */     return (String)getMultipartParameterContentTypes().get(paramOrFileName);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public HttpHeaders getMultipartHeaders(String paramOrFileName)
/*  82:    */   {
/*  83:125 */     String contentType = getMultipartContentType(paramOrFileName);
/*  84:126 */     if (contentType != null)
/*  85:    */     {
/*  86:127 */       HttpHeaders headers = new HttpHeaders();
/*  87:128 */       headers.add("Content-Type", contentType);
/*  88:129 */       return headers;
/*  89:    */     }
/*  90:132 */     return null;
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected final void setMultipartParameters(Map<String, String[]> multipartParameters)
/*  94:    */   {
/*  95:142 */     this.multipartParameters = multipartParameters;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected Map<String, String[]> getMultipartParameters()
/*  99:    */   {
/* 100:151 */     if (this.multipartParameters == null) {
/* 101:152 */       initializeMultipart();
/* 102:    */     }
/* 103:154 */     return this.multipartParameters;
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected final void setMultipartParameterContentTypes(Map<String, String> multipartParameterContentTypes)
/* 107:    */   {
/* 108:162 */     this.multipartParameterContentTypes = multipartParameterContentTypes;
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected Map<String, String> getMultipartParameterContentTypes()
/* 112:    */   {
/* 113:171 */     if (this.multipartParameterContentTypes == null) {
/* 114:172 */       initializeMultipart();
/* 115:    */     }
/* 116:174 */     return this.multipartParameterContentTypes;
/* 117:    */   }
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest
 * JD-Core Version:    0.7.0.1
 */