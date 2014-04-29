/*   1:    */ package org.springframework.web.servlet.mvc.annotation;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import org.springframework.http.MediaType;
/*   7:    */ import org.springframework.util.ObjectUtils;
/*   8:    */ import org.springframework.web.bind.annotation.RequestMethod;
/*   9:    */ import org.springframework.web.util.WebUtils;
/*  10:    */ 
/*  11:    */ abstract class ServletAnnotationMappingUtils
/*  12:    */ {
/*  13:    */   public static boolean checkRequestMethod(RequestMethod[] methods, HttpServletRequest request)
/*  14:    */   {
/*  15: 43 */     if (ObjectUtils.isEmpty(methods)) {
/*  16: 44 */       return true;
/*  17:    */     }
/*  18: 46 */     RequestMethod[] arrayOfRequestMethod = methods;int j = methods.length;
/*  19: 46 */     for (int i = 0; i < j; i++)
/*  20:    */     {
/*  21: 46 */       RequestMethod method = arrayOfRequestMethod[i];
/*  22: 47 */       if (method.name().equals(request.getMethod())) {
/*  23: 48 */         return true;
/*  24:    */       }
/*  25:    */     }
/*  26: 51 */     return false;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static boolean checkParameters(String[] params, HttpServletRequest request)
/*  30:    */   {
/*  31: 61 */     if (!ObjectUtils.isEmpty(params))
/*  32:    */     {
/*  33: 62 */       String[] arrayOfString = params;int j = params.length;
/*  34: 62 */       for (int i = 0; i < j; i++)
/*  35:    */       {
/*  36: 62 */         String param = arrayOfString[i];
/*  37: 63 */         int separator = param.indexOf('=');
/*  38: 64 */         if (separator == -1)
/*  39:    */         {
/*  40: 65 */           if (param.startsWith("!"))
/*  41:    */           {
/*  42: 66 */             if (WebUtils.hasSubmitParameter(request, param.substring(1))) {
/*  43: 67 */               return false;
/*  44:    */             }
/*  45:    */           }
/*  46: 70 */           else if (!WebUtils.hasSubmitParameter(request, param)) {
/*  47: 71 */             return false;
/*  48:    */           }
/*  49:    */         }
/*  50:    */         else
/*  51:    */         {
/*  52: 75 */           boolean negated = (separator > 0) && (param.charAt(separator - 1) == '!');
/*  53: 76 */           String key = !negated ? param.substring(0, separator) : param.substring(0, separator - 1);
/*  54: 77 */           String value = param.substring(separator + 1);
/*  55: 78 */           boolean match = value.equals(request.getParameter(key));
/*  56: 79 */           if (negated) {
/*  57: 80 */             match = !match;
/*  58:    */           }
/*  59: 82 */           if (!match) {
/*  60: 83 */             return false;
/*  61:    */           }
/*  62:    */         }
/*  63:    */       }
/*  64:    */     }
/*  65: 88 */     return true;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static boolean checkHeaders(String[] headers, HttpServletRequest request)
/*  69:    */   {
/*  70: 98 */     if (!ObjectUtils.isEmpty(headers))
/*  71:    */     {
/*  72: 99 */       String[] arrayOfString = headers;int j = headers.length;
/*  73: 99 */       for (int i = 0; i < j; i++)
/*  74:    */       {
/*  75: 99 */         String header = arrayOfString[i];
/*  76:100 */         int separator = header.indexOf('=');
/*  77:101 */         if (separator == -1)
/*  78:    */         {
/*  79:102 */           if (header.startsWith("!"))
/*  80:    */           {
/*  81:103 */             if (request.getHeader(header.substring(1)) != null) {
/*  82:104 */               return false;
/*  83:    */             }
/*  84:    */           }
/*  85:107 */           else if (request.getHeader(header) == null) {
/*  86:108 */             return false;
/*  87:    */           }
/*  88:    */         }
/*  89:    */         else
/*  90:    */         {
/*  91:112 */           boolean negated = (separator > 0) && (header.charAt(separator - 1) == '!');
/*  92:113 */           String key = !negated ? header.substring(0, separator) : header.substring(0, separator - 1);
/*  93:114 */           String value = header.substring(separator + 1);
/*  94:115 */           if (isMediaTypeHeader(key))
/*  95:    */           {
/*  96:116 */             List<MediaType> requestMediaTypes = MediaType.parseMediaTypes(request.getHeader(key));
/*  97:117 */             List<MediaType> valueMediaTypes = MediaType.parseMediaTypes(value);
/*  98:118 */             boolean found = false;
/*  99:    */             Iterator<MediaType> reqIter;
/* 100:119 */             for (Iterator<MediaType> valIter = valueMediaTypes.iterator(); (valIter.hasNext()) && (!found); (reqIter.hasNext()) && (!found))
/* 101:    */             {
/* 102:120 */               MediaType valueMediaType = (MediaType)valIter.next();
/* 103:121 */               reqIter = requestMediaTypes.iterator();
/* 104:122 */               continue;
/* 105:123 */               MediaType requestMediaType = (MediaType)reqIter.next();
/* 106:124 */               if (valueMediaType.includes(requestMediaType)) {
/* 107:125 */                 found = true;
/* 108:    */               }
/* 109:    */             }
/* 110:130 */             if (!found) {
/* 111:131 */               return negated;
/* 112:    */             }
/* 113:    */           }
/* 114:134 */           else if (!value.equals(request.getHeader(key)))
/* 115:    */           {
/* 116:135 */             return negated;
/* 117:    */           }
/* 118:    */         }
/* 119:    */       }
/* 120:    */     }
/* 121:140 */     return true;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private static boolean isMediaTypeHeader(String headerName)
/* 125:    */   {
/* 126:144 */     return ("Accept".equalsIgnoreCase(headerName)) || ("Content-Type".equalsIgnoreCase(headerName));
/* 127:    */   }
/* 128:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.annotation.ServletAnnotationMappingUtils
 * JD-Core Version:    0.7.0.1
 */