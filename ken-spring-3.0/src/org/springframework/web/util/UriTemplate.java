/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.net.URI;
/*   6:    */ import java.net.URISyntaxException;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.LinkedHashMap;
/*   9:    */ import java.util.LinkedList;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.regex.Matcher;
/*  13:    */ import java.util.regex.Pattern;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public class UriTemplate
/*  17:    */   implements Serializable
/*  18:    */ {
/*  19: 47 */   private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*  20:    */   private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
/*  21:    */   private final UriComponents uriComponents;
/*  22:    */   private final List<String> variableNames;
/*  23:    */   private final Pattern matchPattern;
/*  24:    */   private final String uriTemplate;
/*  25:    */   
/*  26:    */   public UriTemplate(String uriTemplate)
/*  27:    */   {
/*  28: 66 */     Parser parser = new Parser(uriTemplate, null);
/*  29: 67 */     this.uriTemplate = uriTemplate;
/*  30: 68 */     this.variableNames = Parser.access$1(parser);
/*  31: 69 */     this.matchPattern = parser.getMatchPattern();
/*  32: 70 */     this.uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).build();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public List<String> getVariableNames()
/*  36:    */   {
/*  37: 78 */     return this.variableNames;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public URI expand(Map<String, ?> uriVariables)
/*  41:    */   {
/*  42:101 */     UriComponents expandedComponents = this.uriComponents.expand(uriVariables);
/*  43:102 */     UriComponents encodedComponents = expandedComponents.encode();
/*  44:103 */     return encodedComponents.toUri();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public URI expand(Object... uriVariableValues)
/*  48:    */   {
/*  49:121 */     UriComponents expandedComponents = this.uriComponents.expand(uriVariableValues);
/*  50:122 */     UriComponents encodedComponents = expandedComponents.encode();
/*  51:123 */     return encodedComponents.toUri();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean matches(String uri)
/*  55:    */   {
/*  56:134 */     if (uri == null) {
/*  57:135 */       return false;
/*  58:    */     }
/*  59:137 */     Matcher matcher = this.matchPattern.matcher(uri);
/*  60:138 */     return matcher.matches();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Map<String, String> match(String uri)
/*  64:    */   {
/*  65:154 */     Assert.notNull(uri, "'uri' must not be null");
/*  66:155 */     Map<String, String> result = new LinkedHashMap(this.variableNames.size());
/*  67:156 */     Matcher matcher = this.matchPattern.matcher(uri);
/*  68:157 */     if (matcher.find()) {
/*  69:158 */       for (int i = 1; i <= matcher.groupCount(); i++)
/*  70:    */       {
/*  71:159 */         String name = (String)this.variableNames.get(i - 1);
/*  72:160 */         String value = matcher.group(i);
/*  73:161 */         result.put(name, value);
/*  74:    */       }
/*  75:    */     }
/*  76:164 */     return result;
/*  77:    */   }
/*  78:    */   
/*  79:    */   @Deprecated
/*  80:    */   protected URI encodeUri(String uri)
/*  81:    */   {
/*  82:    */     try
/*  83:    */     {
/*  84:177 */       String encoded = UriUtils.encodeUri(uri, "UTF-8");
/*  85:178 */       return new URI(encoded);
/*  86:    */     }
/*  87:    */     catch (UnsupportedEncodingException ex)
/*  88:    */     {
/*  89:182 */       throw new IllegalStateException(ex);
/*  90:    */     }
/*  91:    */     catch (URISyntaxException ex)
/*  92:    */     {
/*  93:185 */       throw new IllegalArgumentException("Could not create URI from [" + uri + "]: " + ex, ex);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String toString()
/*  98:    */   {
/*  99:191 */     return this.uriTemplate;
/* 100:    */   }
/* 101:    */   
/* 102:    */   private static class Parser
/* 103:    */   {
/* 104:200 */     private final List<String> variableNames = new LinkedList();
/* 105:202 */     private final StringBuilder patternBuilder = new StringBuilder();
/* 106:    */     
/* 107:    */     private Parser(String uriTemplate)
/* 108:    */     {
/* 109:205 */       Assert.hasText(uriTemplate, "'uriTemplate' must not be null");
/* 110:206 */       Matcher m = UriTemplate.NAMES_PATTERN.matcher(uriTemplate);
/* 111:207 */       int end = 0;
/* 112:208 */       while (m.find())
/* 113:    */       {
/* 114:209 */         this.patternBuilder.append(quote(uriTemplate, end, m.start()));
/* 115:210 */         String match = m.group(1);
/* 116:211 */         int colonIdx = match.indexOf(':');
/* 117:212 */         if (colonIdx == -1)
/* 118:    */         {
/* 119:213 */           this.patternBuilder.append("(.*)");
/* 120:214 */           this.variableNames.add(match);
/* 121:    */         }
/* 122:    */         else
/* 123:    */         {
/* 124:217 */           if (colonIdx + 1 == match.length()) {
/* 125:218 */             throw new IllegalArgumentException("No custom regular expression specified after ':' in \"" + match + "\"");
/* 126:    */           }
/* 127:220 */           String variablePattern = match.substring(colonIdx + 1, match.length());
/* 128:221 */           this.patternBuilder.append('(');
/* 129:222 */           this.patternBuilder.append(variablePattern);
/* 130:223 */           this.patternBuilder.append(')');
/* 131:224 */           String variableName = match.substring(0, colonIdx);
/* 132:225 */           this.variableNames.add(variableName);
/* 133:    */         }
/* 134:227 */         end = m.end();
/* 135:    */       }
/* 136:229 */       this.patternBuilder.append(quote(uriTemplate, end, uriTemplate.length()));
/* 137:230 */       int lastIdx = this.patternBuilder.length() - 1;
/* 138:231 */       if ((lastIdx >= 0) && (this.patternBuilder.charAt(lastIdx) == '/')) {
/* 139:232 */         this.patternBuilder.deleteCharAt(lastIdx);
/* 140:    */       }
/* 141:    */     }
/* 142:    */     
/* 143:    */     private String quote(String fullPath, int start, int end)
/* 144:    */     {
/* 145:237 */       if (start == end) {
/* 146:238 */         return "";
/* 147:    */       }
/* 148:240 */       return Pattern.quote(fullPath.substring(start, end));
/* 149:    */     }
/* 150:    */     
/* 151:    */     private List<String> getVariableNames()
/* 152:    */     {
/* 153:244 */       return Collections.unmodifiableList(this.variableNames);
/* 154:    */     }
/* 155:    */     
/* 156:    */     private Pattern getMatchPattern()
/* 157:    */     {
/* 158:248 */       return Pattern.compile(this.patternBuilder.toString());
/* 159:    */     }
/* 160:    */   }
/* 161:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.UriTemplate
 * JD-Core Version:    0.7.0.1
 */