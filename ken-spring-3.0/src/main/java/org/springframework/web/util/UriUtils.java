/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public abstract class UriUtils
/*   8:    */ {
/*   9:    */   public static String encodeUri(String uri, String encoding)
/*  10:    */     throws UnsupportedEncodingException
/*  11:    */   {
/*  12: 54 */     UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
/*  13: 55 */     UriComponents encoded = uriComponents.encode(encoding);
/*  14: 56 */     return encoded.toUriString();
/*  15:    */   }
/*  16:    */   
/*  17:    */   public static String encodeHttpUrl(String httpUrl, String encoding)
/*  18:    */     throws UnsupportedEncodingException
/*  19:    */   {
/*  20: 71 */     UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(httpUrl).build();
/*  21: 72 */     UriComponents encoded = uriComponents.encode(encoding);
/*  22: 73 */     return encoded.toUriString();
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static String encodeUriComponents(String scheme, String authority, String userInfo, String host, String port, String path, String query, String fragment, String encoding)
/*  26:    */     throws UnsupportedEncodingException
/*  27:    */   {
/*  28:102 */     int portAsInt = port != null ? Integer.parseInt(port) : -1;
/*  29:    */     
/*  30:104 */     UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
/*  31:105 */     builder.scheme(scheme).userInfo(userInfo).host(host).port(portAsInt);
/*  32:106 */     builder.path(path).query(query).fragment(fragment);
/*  33:    */     
/*  34:108 */     UriComponents encoded = builder.build().encode(encoding);
/*  35:    */     
/*  36:110 */     return encoded.toUriString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static String encodeScheme(String scheme, String encoding)
/*  40:    */     throws UnsupportedEncodingException
/*  41:    */   {
/*  42:124 */     return UriComponents.encodeUriComponent(scheme, encoding, UriComponents.Type.SCHEME);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static String encodeAuthority(String authority, String encoding)
/*  46:    */     throws UnsupportedEncodingException
/*  47:    */   {
/*  48:136 */     return UriComponents.encodeUriComponent(authority, encoding, UriComponents.Type.AUTHORITY);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static String encodeUserInfo(String userInfo, String encoding)
/*  52:    */     throws UnsupportedEncodingException
/*  53:    */   {
/*  54:148 */     return UriComponents.encodeUriComponent(userInfo, encoding, UriComponents.Type.USER_INFO);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static String encodeHost(String host, String encoding)
/*  58:    */     throws UnsupportedEncodingException
/*  59:    */   {
/*  60:160 */     return UriComponents.encodeUriComponent(host, encoding, UriComponents.Type.HOST);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static String encodePort(String port, String encoding)
/*  64:    */     throws UnsupportedEncodingException
/*  65:    */   {
/*  66:172 */     return UriComponents.encodeUriComponent(port, encoding, UriComponents.Type.PORT);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static String encodePath(String path, String encoding)
/*  70:    */     throws UnsupportedEncodingException
/*  71:    */   {
/*  72:184 */     return UriComponents.encodeUriComponent(path, encoding, UriComponents.Type.PATH);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static String encodePathSegment(String segment, String encoding)
/*  76:    */     throws UnsupportedEncodingException
/*  77:    */   {
/*  78:196 */     return UriComponents.encodeUriComponent(segment, encoding, UriComponents.Type.PATH_SEGMENT);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static String encodeQuery(String query, String encoding)
/*  82:    */     throws UnsupportedEncodingException
/*  83:    */   {
/*  84:208 */     return UriComponents.encodeUriComponent(query, encoding, UriComponents.Type.QUERY);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static String encodeQueryParam(String queryParam, String encoding)
/*  88:    */     throws UnsupportedEncodingException
/*  89:    */   {
/*  90:220 */     return UriComponents.encodeUriComponent(queryParam, encoding, UriComponents.Type.QUERY_PARAM);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static String encodeFragment(String fragment, String encoding)
/*  94:    */     throws UnsupportedEncodingException
/*  95:    */   {
/*  96:232 */     return UriComponents.encodeUriComponent(fragment, encoding, UriComponents.Type.FRAGMENT);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static String decode(String source, String encoding)
/* 100:    */     throws UnsupportedEncodingException
/* 101:    */   {
/* 102:255 */     Assert.notNull(source, "'source' must not be null");
/* 103:256 */     Assert.hasLength(encoding, "'encoding' must not be empty");
/* 104:257 */     int length = source.length();
/* 105:258 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
/* 106:259 */     boolean changed = false;
/* 107:260 */     for (int i = 0; i < length; i++)
/* 108:    */     {
/* 109:261 */       int ch = source.charAt(i);
/* 110:262 */       if (ch == 37)
/* 111:    */       {
/* 112:263 */         if (i + 2 < length)
/* 113:    */         {
/* 114:264 */           char hex1 = source.charAt(i + 1);
/* 115:265 */           char hex2 = source.charAt(i + 2);
/* 116:266 */           int u = Character.digit(hex1, 16);
/* 117:267 */           int l = Character.digit(hex2, 16);
/* 118:268 */           if ((u == -1) || (l == -1)) {
/* 119:269 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/* 120:    */           }
/* 121:271 */           bos.write((char)((u << 4) + l));
/* 122:272 */           i += 2;
/* 123:273 */           changed = true;
/* 124:    */         }
/* 125:    */         else
/* 126:    */         {
/* 127:276 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/* 128:    */         }
/* 129:    */       }
/* 130:    */       else {
/* 131:280 */         bos.write(ch);
/* 132:    */       }
/* 133:    */     }
/* 134:283 */     return changed ? new String(bos.toByteArray(), encoding) : source;
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.UriUtils
 * JD-Core Version:    0.7.0.1
 */