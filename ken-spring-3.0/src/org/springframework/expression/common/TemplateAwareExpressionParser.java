/*   1:    */ package org.springframework.expression.common;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Stack;
/*   6:    */ import org.springframework.expression.Expression;
/*   7:    */ import org.springframework.expression.ExpressionParser;
/*   8:    */ import org.springframework.expression.ParseException;
/*   9:    */ import org.springframework.expression.ParserContext;
/*  10:    */ 
/*  11:    */ public abstract class TemplateAwareExpressionParser
/*  12:    */   implements ExpressionParser
/*  13:    */ {
/*  14: 35 */   private static final ParserContext NON_TEMPLATE_PARSER_CONTEXT = new ParserContext()
/*  15:    */   {
/*  16:    */     public String getExpressionPrefix()
/*  17:    */     {
/*  18: 37 */       return null;
/*  19:    */     }
/*  20:    */     
/*  21:    */     public String getExpressionSuffix()
/*  22:    */     {
/*  23: 40 */       return null;
/*  24:    */     }
/*  25:    */     
/*  26:    */     public boolean isTemplate()
/*  27:    */     {
/*  28: 43 */       return false;
/*  29:    */     }
/*  30:    */   };
/*  31:    */   
/*  32:    */   public Expression parseExpression(String expressionString)
/*  33:    */     throws ParseException
/*  34:    */   {
/*  35: 49 */     return parseExpression(expressionString, NON_TEMPLATE_PARSER_CONTEXT);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Expression parseExpression(String expressionString, ParserContext context)
/*  39:    */     throws ParseException
/*  40:    */   {
/*  41: 53 */     if (context == null) {
/*  42: 54 */       context = NON_TEMPLATE_PARSER_CONTEXT;
/*  43:    */     }
/*  44: 56 */     if (context.isTemplate()) {
/*  45: 57 */       return parseTemplate(expressionString, context);
/*  46:    */     }
/*  47: 59 */     return doParseExpression(expressionString, context);
/*  48:    */   }
/*  49:    */   
/*  50:    */   private Expression parseTemplate(String expressionString, ParserContext context)
/*  51:    */     throws ParseException
/*  52:    */   {
/*  53: 64 */     if (expressionString.length() == 0) {
/*  54: 65 */       return new LiteralExpression("");
/*  55:    */     }
/*  56: 67 */     Expression[] expressions = parseExpressions(expressionString, context);
/*  57: 68 */     if (expressions.length == 1) {
/*  58: 69 */       return expressions[0];
/*  59:    */     }
/*  60: 71 */     return new CompositeStringExpression(expressionString, expressions);
/*  61:    */   }
/*  62:    */   
/*  63:    */   private Expression[] parseExpressions(String expressionString, ParserContext context)
/*  64:    */     throws ParseException
/*  65:    */   {
/*  66: 93 */     List<Expression> expressions = new LinkedList();
/*  67: 94 */     String prefix = context.getExpressionPrefix();
/*  68: 95 */     String suffix = context.getExpressionSuffix();
/*  69: 96 */     int startIdx = 0;
/*  70: 97 */     while (startIdx < expressionString.length())
/*  71:    */     {
/*  72: 98 */       int prefixIndex = expressionString.indexOf(prefix, startIdx);
/*  73: 99 */       if (prefixIndex >= startIdx)
/*  74:    */       {
/*  75:101 */         if (prefixIndex > startIdx) {
/*  76:102 */           expressions.add(createLiteralExpression(context, expressionString.substring(startIdx, prefixIndex)));
/*  77:    */         }
/*  78:104 */         int afterPrefixIndex = prefixIndex + prefix.length();
/*  79:105 */         int suffixIndex = skipToCorrectEndSuffix(prefix, suffix, expressionString, afterPrefixIndex);
/*  80:106 */         if (suffixIndex == -1) {
/*  81:107 */           throw new ParseException(expressionString, prefixIndex, "No ending suffix '" + suffix + 
/*  82:108 */             "' for expression starting at character " + prefixIndex + ": " + 
/*  83:109 */             expressionString.substring(prefixIndex));
/*  84:    */         }
/*  85:111 */         if (suffixIndex == afterPrefixIndex) {
/*  86:112 */           throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + 
/*  87:113 */             prefix + suffix + "' at character " + prefixIndex);
/*  88:    */         }
/*  89:115 */         String expr = expressionString.substring(prefixIndex + prefix.length(), suffixIndex);
/*  90:116 */         expr = expr.trim();
/*  91:117 */         if (expr.length() == 0) {
/*  92:118 */           throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + 
/*  93:119 */             prefix + suffix + "' at character " + prefixIndex);
/*  94:    */         }
/*  95:121 */         expressions.add(doParseExpression(expr, context));
/*  96:122 */         startIdx = suffixIndex + suffix.length();
/*  97:    */       }
/*  98:    */       else
/*  99:    */       {
/* 100:126 */         expressions.add(createLiteralExpression(context, expressionString.substring(startIdx)));
/* 101:127 */         startIdx = expressionString.length();
/* 102:    */       }
/* 103:    */     }
/* 104:130 */     return (Expression[])expressions.toArray(new Expression[expressions.size()]);
/* 105:    */   }
/* 106:    */   
/* 107:    */   private Expression createLiteralExpression(ParserContext context, String text)
/* 108:    */   {
/* 109:134 */     return new LiteralExpression(text);
/* 110:    */   }
/* 111:    */   
/* 112:    */   private boolean isSuffixHere(String expressionString, int pos, String suffix)
/* 113:    */   {
/* 114:145 */     int suffixPosition = 0;
/* 115:146 */     for (int i = 0; (i < suffix.length()) && (pos < expressionString.length()); i++) {
/* 116:147 */       if (expressionString.charAt(pos++) != suffix.charAt(suffixPosition++)) {
/* 117:148 */         return false;
/* 118:    */       }
/* 119:    */     }
/* 120:151 */     if (suffixPosition != suffix.length()) {
/* 121:153 */       return false;
/* 122:    */     }
/* 123:155 */     return true;
/* 124:    */   }
/* 125:    */   
/* 126:    */   private int skipToCorrectEndSuffix(String prefix, String suffix, String expressionString, int afterPrefixIndex)
/* 127:    */     throws ParseException
/* 128:    */   {
/* 129:171 */     int pos = afterPrefixIndex;
/* 130:172 */     int maxlen = expressionString.length();
/* 131:173 */     int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
/* 132:174 */     if (nextSuffix == -1) {
/* 133:175 */       return -1;
/* 134:    */     }
/* 135:177 */     Stack<Bracket> stack = new Stack();
/* 136:178 */     while (pos < maxlen)
/* 137:    */     {
/* 138:179 */       if ((isSuffixHere(expressionString, pos, suffix)) && (stack.isEmpty())) {
/* 139:    */         break;
/* 140:    */       }
/* 141:182 */       char ch = expressionString.charAt(pos);
/* 142:183 */       switch (ch)
/* 143:    */       {
/* 144:    */       case '(': 
/* 145:    */       case '[': 
/* 146:    */       case '{': 
/* 147:185 */         stack.push(new Bracket(ch, pos));
/* 148:186 */         break;
/* 149:    */       case ')': 
/* 150:    */       case ']': 
/* 151:    */       case '}': 
/* 152:188 */         if (stack.isEmpty()) {
/* 153:189 */           throw new ParseException(expressionString, pos, "Found closing '" + ch + "' at position " + pos + " without an opening '" + Bracket.theOpenBracketFor(ch) + "'");
/* 154:    */         }
/* 155:191 */         Bracket p = (Bracket)stack.pop();
/* 156:192 */         if (!p.compatibleWithCloseBracket(ch)) {
/* 157:193 */           throw new ParseException(expressionString, pos, "Found closing '" + ch + "' at position " + pos + " but most recent opening is '" + p.bracket + "' at position " + p.pos);
/* 158:    */         }
/* 159:    */         break;
/* 160:    */       case '"': 
/* 161:    */       case '\'': 
/* 162:199 */         int endLiteral = expressionString.indexOf(ch, pos + 1);
/* 163:200 */         if (endLiteral == -1) {
/* 164:201 */           throw new ParseException(expressionString, pos, "Found non terminating string literal starting at position " + pos);
/* 165:    */         }
/* 166:203 */         pos = endLiteral;
/* 167:    */       }
/* 168:206 */       pos++;
/* 169:    */     }
/* 170:208 */     if (!stack.isEmpty())
/* 171:    */     {
/* 172:209 */       Bracket p = (Bracket)stack.pop();
/* 173:210 */       throw new ParseException(expressionString, p.pos, "Missing closing '" + Bracket.theCloseBracketFor(p.bracket) + "' for '" + p.bracket + "' at position " + p.pos);
/* 174:    */     }
/* 175:212 */     if (!isSuffixHere(expressionString, pos, suffix)) {
/* 176:213 */       return -1;
/* 177:    */     }
/* 178:215 */     return pos;
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected abstract Expression doParseExpression(String paramString, ParserContext paramParserContext)
/* 182:    */     throws ParseException;
/* 183:    */   
/* 184:    */   private static class Bracket
/* 185:    */   {
/* 186:    */     char bracket;
/* 187:    */     int pos;
/* 188:    */     
/* 189:    */     Bracket(char bracket, int pos)
/* 190:    */     {
/* 191:229 */       this.bracket = bracket;
/* 192:230 */       this.pos = pos;
/* 193:    */     }
/* 194:    */     
/* 195:    */     boolean compatibleWithCloseBracket(char closeBracket)
/* 196:    */     {
/* 197:234 */       if (this.bracket == '{') {
/* 198:235 */         return closeBracket == '}';
/* 199:    */       }
/* 200:236 */       if (this.bracket == '[') {
/* 201:237 */         return closeBracket == ']';
/* 202:    */       }
/* 203:239 */       return closeBracket == ')';
/* 204:    */     }
/* 205:    */     
/* 206:    */     static char theOpenBracketFor(char closeBracket)
/* 207:    */     {
/* 208:243 */       if (closeBracket == '}') {
/* 209:244 */         return '{';
/* 210:    */       }
/* 211:245 */       if (closeBracket == ']') {
/* 212:246 */         return '[';
/* 213:    */       }
/* 214:248 */       return '(';
/* 215:    */     }
/* 216:    */     
/* 217:    */     static char theCloseBracketFor(char openBracket)
/* 218:    */     {
/* 219:252 */       if (openBracket == '{') {
/* 220:253 */         return '}';
/* 221:    */       }
/* 222:254 */       if (openBracket == '[') {
/* 223:255 */         return ']';
/* 224:    */       }
/* 225:257 */       return ')';
/* 226:    */     }
/* 227:    */   }
/* 228:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.common.TemplateAwareExpressionParser
 * JD-Core Version:    0.7.0.1
 */