/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.nio.charset.Charset;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Arrays;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Enumeration;
/*  11:    */ import java.util.LinkedHashMap;
/*  12:    */ import java.util.LinkedHashSet;
/*  13:    */ import java.util.List;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.Set;
/*  16:    */ import javax.servlet.FilterChain;
/*  17:    */ import javax.servlet.ServletException;
/*  18:    */ import javax.servlet.http.HttpServletRequest;
/*  19:    */ import javax.servlet.http.HttpServletRequestWrapper;
/*  20:    */ import javax.servlet.http.HttpServletResponse;
/*  21:    */ import org.springframework.http.HttpInputMessage;
/*  22:    */ import org.springframework.http.MediaType;
/*  23:    */ import org.springframework.http.converter.FormHttpMessageConverter;
/*  24:    */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*  25:    */ import org.springframework.http.server.ServletServerHttpRequest;
/*  26:    */ import org.springframework.util.LinkedMultiValueMap;
/*  27:    */ import org.springframework.util.MultiValueMap;
/*  28:    */ 
/*  29:    */ public class HttpPutFormContentFilter
/*  30:    */   extends OncePerRequestFilter
/*  31:    */ {
/*  32: 63 */   private final FormHttpMessageConverter formConverter = new XmlAwareFormHttpMessageConverter();
/*  33:    */   
/*  34:    */   public void setCharset(Charset charset)
/*  35:    */   {
/*  36: 69 */     this.formConverter.setCharset(charset);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*  40:    */     throws ServletException, IOException
/*  41:    */   {
/*  42: 76 */     if (("PUT".equals(request.getMethod())) && (isFormContentType(request)))
/*  43:    */     {
/*  44: 77 */       HttpInputMessage inputMessage = new ServletServerHttpRequest(request)
/*  45:    */       {
/*  46:    */         public InputStream getBody()
/*  47:    */           throws IOException
/*  48:    */         {
/*  49: 80 */           return request.getInputStream();
/*  50:    */         }
/*  51: 82 */       };
/*  52: 83 */       MultiValueMap<String, String> formParameters = this.formConverter.read(null, inputMessage);
/*  53: 84 */       HttpServletRequest wrapper = new HttpPutFormContentRequestWrapper(request, formParameters);
/*  54: 85 */       filterChain.doFilter(wrapper, response);
/*  55:    */     }
/*  56:    */     else
/*  57:    */     {
/*  58: 88 */       filterChain.doFilter(request, response);
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   private boolean isFormContentType(HttpServletRequest request)
/*  63:    */   {
/*  64: 94 */     String contentType = request.getContentType();
/*  65: 95 */     if (contentType != null)
/*  66:    */     {
/*  67: 96 */       MediaType mediaType = MediaType.parseMediaType(contentType);
/*  68: 97 */       return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
/*  69:    */     }
/*  70:100 */     return false;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static class HttpPutFormContentRequestWrapper
/*  74:    */     extends HttpServletRequestWrapper
/*  75:    */   {
/*  76:    */     private MultiValueMap<String, String> formParameters;
/*  77:    */     
/*  78:    */     public HttpPutFormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters)
/*  79:    */     {
/*  80:109 */       super();
/*  81:110 */       this.formParameters = (parameters != null ? parameters : new LinkedMultiValueMap());
/*  82:    */     }
/*  83:    */     
/*  84:    */     public String getParameter(String name)
/*  85:    */     {
/*  86:115 */       String queryStringValue = super.getParameter(name);
/*  87:116 */       String formValue = (String)this.formParameters.getFirst(name);
/*  88:117 */       return queryStringValue != null ? queryStringValue : formValue;
/*  89:    */     }
/*  90:    */     
/*  91:    */     public Map<String, String[]> getParameterMap()
/*  92:    */     {
/*  93:122 */       Map<String, String[]> result = new LinkedHashMap();
/*  94:123 */       Enumeration<String> names = getParameterNames();
/*  95:124 */       while (names.hasMoreElements())
/*  96:    */       {
/*  97:125 */         String name = (String)names.nextElement();
/*  98:126 */         result.put(name, getParameterValues(name));
/*  99:    */       }
/* 100:128 */       return result;
/* 101:    */     }
/* 102:    */     
/* 103:    */     public Enumeration<String> getParameterNames()
/* 104:    */     {
/* 105:133 */       Set<String> names = new LinkedHashSet();
/* 106:134 */       names.addAll((Collection)Collections.list(super.getParameterNames()));
/* 107:135 */       names.addAll((Collection)this.formParameters.keySet());
/* 108:136 */       return Collections.enumeration(names);
/* 109:    */     }
/* 110:    */     
/* 111:    */     public String[] getParameterValues(String name)
/* 112:    */     {
/* 113:141 */       String[] queryStringValues = super.getParameterValues(name);
/* 114:142 */       List<String> formValues = (List)this.formParameters.get(name);
/* 115:143 */       if (formValues == null) {
/* 116:144 */         return queryStringValues;
/* 117:    */       }
/* 118:146 */       if (queryStringValues == null) {
/* 119:147 */         return (String[])formValues.toArray(new String[formValues.size()]);
/* 120:    */       }
/* 121:150 */       List<String> result = new ArrayList();
/* 122:151 */       result.addAll((Collection)Arrays.asList(queryStringValues));
/* 123:152 */       result.addAll(formValues);
/* 124:153 */       return (String[])result.toArray(new String[result.size()]);
/* 125:    */     }
/* 126:    */   }
/* 127:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.HttpPutFormContentFilter
 * JD-Core Version:    0.7.0.1
 */