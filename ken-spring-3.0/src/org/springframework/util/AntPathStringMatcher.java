/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.regex.Matcher;
/*   7:    */ import java.util.regex.Pattern;
/*   8:    */ 
/*   9:    */ class AntPathStringMatcher
/*  10:    */ {
/*  11: 38 */   private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
/*  12:    */   private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
/*  13:    */   private final Pattern pattern;
/*  14:    */   private String str;
/*  15: 46 */   private final List<String> variableNames = new LinkedList();
/*  16:    */   private final Map<String, String> uriTemplateVariables;
/*  17:    */   
/*  18:    */   AntPathStringMatcher(String pattern, String str, Map<String, String> uriTemplateVariables)
/*  19:    */   {
/*  20: 52 */     this.str = str;
/*  21: 53 */     this.uriTemplateVariables = uriTemplateVariables;
/*  22: 54 */     this.pattern = createPattern(pattern);
/*  23:    */   }
/*  24:    */   
/*  25:    */   private Pattern createPattern(String pattern)
/*  26:    */   {
/*  27: 58 */     StringBuilder patternBuilder = new StringBuilder();
/*  28: 59 */     Matcher m = GLOB_PATTERN.matcher(pattern);
/*  29: 60 */     int end = 0;
/*  30: 61 */     while (m.find())
/*  31:    */     {
/*  32: 62 */       patternBuilder.append(quote(pattern, end, m.start()));
/*  33: 63 */       String match = m.group();
/*  34: 64 */       if ("?".equals(match))
/*  35:    */       {
/*  36: 65 */         patternBuilder.append('.');
/*  37:    */       }
/*  38: 67 */       else if ("*".equals(match))
/*  39:    */       {
/*  40: 68 */         patternBuilder.append(".*");
/*  41:    */       }
/*  42: 70 */       else if ((match.startsWith("{")) && (match.endsWith("}")))
/*  43:    */       {
/*  44: 71 */         int colonIdx = match.indexOf(':');
/*  45: 72 */         if (colonIdx == -1)
/*  46:    */         {
/*  47: 73 */           patternBuilder.append("(.*)");
/*  48: 74 */           this.variableNames.add(m.group(1));
/*  49:    */         }
/*  50:    */         else
/*  51:    */         {
/*  52: 77 */           String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
/*  53: 78 */           patternBuilder.append('(');
/*  54: 79 */           patternBuilder.append(variablePattern);
/*  55: 80 */           patternBuilder.append(')');
/*  56: 81 */           String variableName = match.substring(1, colonIdx);
/*  57: 82 */           this.variableNames.add(variableName);
/*  58:    */         }
/*  59:    */       }
/*  60: 85 */       end = m.end();
/*  61:    */     }
/*  62: 87 */     patternBuilder.append(quote(pattern, end, pattern.length()));
/*  63: 88 */     return Pattern.compile(patternBuilder.toString());
/*  64:    */   }
/*  65:    */   
/*  66:    */   private String quote(String s, int start, int end)
/*  67:    */   {
/*  68: 92 */     if (start == end) {
/*  69: 93 */       return "";
/*  70:    */     }
/*  71: 95 */     return Pattern.quote(s.substring(start, end));
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean matchStrings()
/*  75:    */   {
/*  76:104 */     Matcher matcher = this.pattern.matcher(this.str);
/*  77:105 */     if (matcher.matches())
/*  78:    */     {
/*  79:106 */       if (this.uriTemplateVariables != null)
/*  80:    */       {
/*  81:108 */         Assert.isTrue(this.variableNames.size() == matcher.groupCount(), 
/*  82:109 */           "The number of capturing groups in the pattern segment " + this.pattern + 
/*  83:110 */           " does not match the number of URI template variables it defines, which can occur if " + 
/*  84:111 */           " capturing groups are used in a URI template regex. Use non-capturing groups instead.");
/*  85:112 */         for (int i = 1; i <= matcher.groupCount(); i++)
/*  86:    */         {
/*  87:113 */           String name = (String)this.variableNames.get(i - 1);
/*  88:114 */           String value = matcher.group(i);
/*  89:115 */           this.uriTemplateVariables.put(name, value);
/*  90:    */         }
/*  91:    */       }
/*  92:118 */       return true;
/*  93:    */     }
/*  94:121 */     return false;
/*  95:    */   }
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.AntPathStringMatcher
 * JD-Core Version:    0.7.0.1
 */