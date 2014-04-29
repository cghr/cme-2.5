/*   1:    */ package org.springframework.http.server;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStreamWriter;
/*   8:    */ import java.io.Writer;
/*   9:    */ import java.net.URI;
/*  10:    */ import java.net.URISyntaxException;
/*  11:    */ import java.net.URLEncoder;
/*  12:    */ import java.util.Arrays;
/*  13:    */ import java.util.Enumeration;
/*  14:    */ import java.util.Iterator;
/*  15:    */ import java.util.List;
/*  16:    */ import java.util.Map;
/*  17:    */ import java.util.Set;
/*  18:    */ import javax.servlet.http.HttpServletRequest;
/*  19:    */ import org.springframework.http.HttpHeaders;
/*  20:    */ import org.springframework.http.HttpMethod;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ 
/*  23:    */ public class ServletServerHttpRequest
/*  24:    */   implements ServerHttpRequest
/*  25:    */ {
/*  26:    */   protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
/*  27:    */   protected static final String FORM_CHARSET = "UTF-8";
/*  28:    */   private static final String METHOD_POST = "POST";
/*  29:    */   private final HttpServletRequest servletRequest;
/*  30:    */   private HttpHeaders headers;
/*  31:    */   
/*  32:    */   public ServletServerHttpRequest(HttpServletRequest servletRequest)
/*  33:    */   {
/*  34: 65 */     Assert.notNull(servletRequest, "'servletRequest' must not be null");
/*  35: 66 */     this.servletRequest = servletRequest;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public HttpServletRequest getServletRequest()
/*  39:    */   {
/*  40: 74 */     return this.servletRequest;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public HttpMethod getMethod()
/*  44:    */   {
/*  45: 78 */     return HttpMethod.valueOf(this.servletRequest.getMethod());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public URI getURI()
/*  49:    */   {
/*  50:    */     try
/*  51:    */     {
/*  52: 83 */       return new URI(this.servletRequest.getScheme(), null, this.servletRequest.getServerName(), 
/*  53: 84 */         this.servletRequest.getServerPort(), this.servletRequest.getRequestURI(), 
/*  54: 85 */         this.servletRequest.getQueryString(), null);
/*  55:    */     }
/*  56:    */     catch (URISyntaxException ex)
/*  57:    */     {
/*  58: 88 */       throw new IllegalStateException("Could not get HttpServletRequest URI: " + ex.getMessage(), ex);
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public HttpHeaders getHeaders()
/*  63:    */   {
/*  64: 93 */     if (this.headers == null)
/*  65:    */     {
/*  66: 94 */       this.headers = new HttpHeaders();
/*  67:    */       Enumeration headerValues;
/*  68: 95 */       for (Enumeration headerNames = this.servletRequest.getHeaderNames(); headerNames.hasMoreElements(); 
/*  69: 98 */             headerValues.hasMoreElements())
/*  70:    */       {
/*  71: 96 */         String headerName = (String)headerNames.nextElement();
/*  72: 97 */         headerValues = this.servletRequest.getHeaders(headerName);
/*  73: 98 */         continue;
/*  74: 99 */         String headerValue = (String)headerValues.nextElement();
/*  75:100 */         this.headers.add(headerName, headerValue);
/*  76:    */       }
/*  77:    */     }
/*  78:104 */     return this.headers;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public InputStream getBody()
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:108 */     if (isFormPost(this.servletRequest)) {
/*  85:109 */       return getBodyFromServletRequestParameters(this.servletRequest);
/*  86:    */     }
/*  87:112 */     return this.servletRequest.getInputStream();
/*  88:    */   }
/*  89:    */   
/*  90:    */   private boolean isFormPost(HttpServletRequest request)
/*  91:    */   {
/*  92:118 */     return (request.getContentType() != null) && (request.getContentType().contains("application/x-www-form-urlencoded")) && ("POST".equalsIgnoreCase(request.getMethod()));
/*  93:    */   }
/*  94:    */   
/*  95:    */   private InputStream getBodyFromServletRequestParameters(HttpServletRequest request)
/*  96:    */     throws IOException
/*  97:    */   {
/*  98:128 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  99:129 */     Writer writer = new OutputStreamWriter(bos, "UTF-8");
/* 100:    */     
/* 101:131 */     Map<String, String[]> form = request.getParameterMap();
/* 102:132 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();)
/* 103:    */     {
/* 104:133 */       String name = (String)nameIterator.next();
/* 105:134 */       List<String> values = Arrays.asList((String[])form.get(name));
/* 106:135 */       for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext();)
/* 107:    */       {
/* 108:136 */         String value = (String)valueIterator.next();
/* 109:137 */         writer.write(URLEncoder.encode(name, "UTF-8"));
/* 110:138 */         if (value != null)
/* 111:    */         {
/* 112:139 */           writer.write(61);
/* 113:140 */           writer.write(URLEncoder.encode(value, "UTF-8"));
/* 114:141 */           if (valueIterator.hasNext()) {
/* 115:142 */             writer.write(38);
/* 116:    */           }
/* 117:    */         }
/* 118:    */       }
/* 119:146 */       if (nameIterator.hasNext()) {
/* 120:147 */         writer.append('&');
/* 121:    */       }
/* 122:    */     }
/* 123:150 */     writer.flush();
/* 124:    */     
/* 125:152 */     return new ByteArrayInputStream(bos.toByteArray());
/* 126:    */   }
/* 127:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.server.ServletServerHttpRequest
 * JD-Core Version:    0.7.0.1
 */