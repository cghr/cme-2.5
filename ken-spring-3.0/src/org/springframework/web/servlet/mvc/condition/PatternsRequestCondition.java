/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Comparator;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import java.util.LinkedHashSet;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Set;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import org.springframework.util.AntPathMatcher;
/*  14:    */ import org.springframework.util.PathMatcher;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ import org.springframework.web.util.UrlPathHelper;
/*  17:    */ 
/*  18:    */ public final class PatternsRequestCondition
/*  19:    */   extends AbstractRequestCondition<PatternsRequestCondition>
/*  20:    */ {
/*  21:    */   private final Set<String> patterns;
/*  22:    */   private final UrlPathHelper urlPathHelper;
/*  23:    */   private final PathMatcher pathMatcher;
/*  24:    */   private final boolean useSuffixPatternMatch;
/*  25:    */   
/*  26:    */   public PatternsRequestCondition(String... patterns)
/*  27:    */   {
/*  28: 59 */     this(asList(patterns), null, null, true);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, boolean useSuffixPatternMatch)
/*  32:    */   {
/*  33: 74 */     this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch);
/*  34:    */   }
/*  35:    */   
/*  36:    */   private PatternsRequestCondition(Collection<String> patterns, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, boolean useSuffixPatternMatch)
/*  37:    */   {
/*  38: 84 */     this.patterns = Collections.unmodifiableSet(prependLeadingSlash(patterns));
/*  39: 85 */     this.urlPathHelper = (urlPathHelper != null ? urlPathHelper : new UrlPathHelper());
/*  40: 86 */     this.pathMatcher = (pathMatcher != null ? pathMatcher : new AntPathMatcher());
/*  41: 87 */     this.useSuffixPatternMatch = useSuffixPatternMatch;
/*  42:    */   }
/*  43:    */   
/*  44:    */   private static List<String> asList(String... patterns)
/*  45:    */   {
/*  46: 91 */     return patterns != null ? Arrays.asList(patterns) : Collections.emptyList();
/*  47:    */   }
/*  48:    */   
/*  49:    */   private static Set<String> prependLeadingSlash(Collection<String> patterns)
/*  50:    */   {
/*  51: 95 */     if (patterns == null) {
/*  52: 96 */       return Collections.emptySet();
/*  53:    */     }
/*  54: 98 */     Set<String> result = new LinkedHashSet(patterns.size());
/*  55: 99 */     for (String pattern : patterns)
/*  56:    */     {
/*  57:100 */       if ((StringUtils.hasLength(pattern)) && (!pattern.startsWith("/"))) {
/*  58:101 */         pattern = "/" + pattern;
/*  59:    */       }
/*  60:103 */       result.add(pattern);
/*  61:    */     }
/*  62:105 */     return result;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Set<String> getPatterns()
/*  66:    */   {
/*  67:109 */     return this.patterns;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected Collection<String> getContent()
/*  71:    */   {
/*  72:114 */     return this.patterns;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected String getToStringInfix()
/*  76:    */   {
/*  77:119 */     return " || ";
/*  78:    */   }
/*  79:    */   
/*  80:    */   public PatternsRequestCondition combine(PatternsRequestCondition other)
/*  81:    */   {
/*  82:133 */     Set<String> result = new LinkedHashSet();
/*  83:134 */     if ((!this.patterns.isEmpty()) && (!other.patterns.isEmpty()))
/*  84:    */     {
/*  85:    */       Iterator localIterator2;
/*  86:135 */       for (Iterator localIterator1 = this.patterns.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/*  87:    */       {
/*  88:135 */         String pattern1 = (String)localIterator1.next();
/*  89:136 */         localIterator2 = other.patterns.iterator(); continue;String pattern2 = (String)localIterator2.next();
/*  90:137 */         result.add(this.pathMatcher.combine(pattern1, pattern2));
/*  91:    */       }
/*  92:    */     }
/*  93:141 */     else if (!this.patterns.isEmpty())
/*  94:    */     {
/*  95:142 */       result.addAll(this.patterns);
/*  96:    */     }
/*  97:144 */     else if (!other.patterns.isEmpty())
/*  98:    */     {
/*  99:145 */       result.addAll(other.patterns);
/* 100:    */     }
/* 101:    */     else
/* 102:    */     {
/* 103:148 */       result.add("");
/* 104:    */     }
/* 105:150 */     return new PatternsRequestCondition(result, this.urlPathHelper, this.pathMatcher, this.useSuffixPatternMatch);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public PatternsRequestCondition getMatchingCondition(HttpServletRequest request)
/* 109:    */   {
/* 110:173 */     if (this.patterns.isEmpty()) {
/* 111:174 */       return this;
/* 112:    */     }
/* 113:176 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 114:177 */     List<String> matches = new ArrayList();
/* 115:178 */     for (String pattern : this.patterns)
/* 116:    */     {
/* 117:179 */       String match = getMatchingPattern(pattern, lookupPath);
/* 118:180 */       if (match != null) {
/* 119:181 */         matches.add(match);
/* 120:    */       }
/* 121:    */     }
/* 122:184 */     Collections.sort(matches, this.pathMatcher.getPatternComparator(lookupPath));
/* 123:185 */     return matches.isEmpty() ? null : 
/* 124:186 */       new PatternsRequestCondition(matches, this.urlPathHelper, this.pathMatcher, this.useSuffixPatternMatch);
/* 125:    */   }
/* 126:    */   
/* 127:    */   private String getMatchingPattern(String pattern, String lookupPath)
/* 128:    */   {
/* 129:190 */     if (pattern.equals(lookupPath)) {
/* 130:191 */       return pattern;
/* 131:    */     }
/* 132:193 */     if (this.useSuffixPatternMatch)
/* 133:    */     {
/* 134:194 */       boolean hasSuffix = pattern.indexOf('.') != -1;
/* 135:195 */       if ((!hasSuffix) && (this.pathMatcher.match(pattern + ".*", lookupPath))) {
/* 136:196 */         return pattern + ".*";
/* 137:    */       }
/* 138:    */     }
/* 139:199 */     if (this.pathMatcher.match(pattern, lookupPath)) {
/* 140:200 */       return pattern;
/* 141:    */     }
/* 142:202 */     boolean endsWithSlash = pattern.endsWith("/");
/* 143:203 */     if ((!endsWithSlash) && (this.pathMatcher.match(pattern + "/", lookupPath))) {
/* 144:204 */       return pattern + "/";
/* 145:    */     }
/* 146:206 */     return null;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public int compareTo(PatternsRequestCondition other, HttpServletRequest request)
/* 150:    */   {
/* 151:222 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 152:223 */     Comparator<String> patternComparator = this.pathMatcher.getPatternComparator(lookupPath);
/* 153:    */     
/* 154:225 */     Iterator<String> iterator = this.patterns.iterator();
/* 155:226 */     Iterator<String> iteratorOther = other.patterns.iterator();
/* 156:227 */     while ((iterator.hasNext()) && (iteratorOther.hasNext()))
/* 157:    */     {
/* 158:228 */       int result = patternComparator.compare((String)iterator.next(), (String)iteratorOther.next());
/* 159:229 */       if (result != 0) {
/* 160:230 */         return result;
/* 161:    */       }
/* 162:    */     }
/* 163:233 */     if (iterator.hasNext()) {
/* 164:234 */       return -1;
/* 165:    */     }
/* 166:236 */     if (iteratorOther.hasNext()) {
/* 167:237 */       return 1;
/* 168:    */     }
/* 169:240 */     return 0;
/* 170:    */   }
/* 171:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.PatternsRequestCondition
 * JD-Core Version:    0.7.0.1
 */