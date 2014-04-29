/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ 
/*   5:    */ abstract class AbstractNameValueExpression<T>
/*   6:    */   implements NameValueExpression<T>
/*   7:    */ {
/*   8:    */   protected final String name;
/*   9:    */   protected final T value;
/*  10:    */   protected final boolean isNegated;
/*  11:    */   
/*  12:    */   AbstractNameValueExpression(String expression)
/*  13:    */   {
/*  14: 39 */     int separator = expression.indexOf('=');
/*  15: 40 */     if (separator == -1)
/*  16:    */     {
/*  17: 41 */       this.isNegated = expression.startsWith("!");
/*  18: 42 */       this.name = (this.isNegated ? expression.substring(1) : expression);
/*  19: 43 */       this.value = null;
/*  20:    */     }
/*  21:    */     else
/*  22:    */     {
/*  23: 46 */       this.isNegated = ((separator > 0) && (expression.charAt(separator - 1) == '!'));
/*  24: 47 */       this.name = (this.isNegated ? expression.substring(0, separator - 1) : expression.substring(0, separator));
/*  25: 48 */       this.value = parseValue(expression.substring(separator + 1));
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String getName()
/*  30:    */   {
/*  31: 53 */     return this.name;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public T getValue()
/*  35:    */   {
/*  36: 57 */     return this.value;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public boolean isNegated()
/*  40:    */   {
/*  41: 61 */     return this.isNegated;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected abstract T parseValue(String paramString);
/*  45:    */   
/*  46:    */   public final boolean match(HttpServletRequest request)
/*  47:    */   {
/*  48:    */     boolean isMatch;
/*  49:    */     boolean isMatch;
/*  50: 68 */     if (this.value != null) {
/*  51: 69 */       isMatch = matchValue(request);
/*  52:    */     } else {
/*  53: 72 */       isMatch = matchName(request);
/*  54:    */     }
/*  55: 74 */     return this.isNegated ? true : isMatch ? false : isMatch;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected abstract boolean matchName(HttpServletRequest paramHttpServletRequest);
/*  59:    */   
/*  60:    */   protected abstract boolean matchValue(HttpServletRequest paramHttpServletRequest);
/*  61:    */   
/*  62:    */   public boolean equals(Object obj)
/*  63:    */   {
/*  64: 83 */     if (this == obj) {
/*  65: 84 */       return true;
/*  66:    */     }
/*  67: 86 */     if ((obj != null) && ((obj instanceof AbstractNameValueExpression)))
/*  68:    */     {
/*  69: 87 */       AbstractNameValueExpression<?> other = (AbstractNameValueExpression)obj;
/*  70:    */       
/*  71:    */ 
/*  72: 90 */       return (this.name.equalsIgnoreCase(other.name)) && (this.value != null ? this.value.equals(other.value) : other.value == null) && (this.isNegated == other.isNegated);
/*  73:    */     }
/*  74: 92 */     return false;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int hashCode()
/*  78:    */   {
/*  79: 97 */     int result = this.name.hashCode();
/*  80: 98 */     result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
/*  81: 99 */     result = 31 * result + (this.isNegated ? 1 : 0);
/*  82:100 */     return result;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String toString()
/*  86:    */   {
/*  87:105 */     StringBuilder builder = new StringBuilder();
/*  88:106 */     if (this.value != null)
/*  89:    */     {
/*  90:107 */       builder.append(this.name);
/*  91:108 */       if (this.isNegated) {
/*  92:109 */         builder.append('!');
/*  93:    */       }
/*  94:111 */       builder.append('=');
/*  95:112 */       builder.append(this.value);
/*  96:    */     }
/*  97:    */     else
/*  98:    */     {
/*  99:115 */       if (this.isNegated) {
/* 100:116 */         builder.append('!');
/* 101:    */       }
/* 102:118 */       builder.append(this.name);
/* 103:    */     }
/* 104:120 */     return builder.toString();
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.AbstractNameValueExpression
 * JD-Core Version:    0.7.0.1
 */