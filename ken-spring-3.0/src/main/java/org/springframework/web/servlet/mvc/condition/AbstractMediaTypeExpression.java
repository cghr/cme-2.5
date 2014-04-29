/*  1:   */ package org.springframework.web.servlet.mvc.condition;
/*  2:   */ 
/*  3:   */ import java.util.Comparator;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import org.springframework.http.MediaType;
/*  6:   */ 
/*  7:   */ abstract class AbstractMediaTypeExpression
/*  8:   */   implements Comparable<AbstractMediaTypeExpression>, MediaTypeExpression
/*  9:   */ {
/* 10:   */   private final MediaType mediaType;
/* 11:   */   private final boolean isNegated;
/* 12:   */   
/* 13:   */   AbstractMediaTypeExpression(String expression)
/* 14:   */   {
/* 15:39 */     if (expression.startsWith("!"))
/* 16:   */     {
/* 17:40 */       this.isNegated = true;
/* 18:41 */       expression = expression.substring(1);
/* 19:   */     }
/* 20:   */     else
/* 21:   */     {
/* 22:44 */       this.isNegated = false;
/* 23:   */     }
/* 24:46 */     this.mediaType = MediaType.parseMediaType(expression);
/* 25:   */   }
/* 26:   */   
/* 27:   */   AbstractMediaTypeExpression(MediaType mediaType, boolean negated)
/* 28:   */   {
/* 29:50 */     this.mediaType = mediaType;
/* 30:51 */     this.isNegated = negated;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public MediaType getMediaType()
/* 34:   */   {
/* 35:55 */     return this.mediaType;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public boolean isNegated()
/* 39:   */   {
/* 40:59 */     return this.isNegated;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public final boolean match(HttpServletRequest request)
/* 44:   */   {
/* 45:63 */     boolean match = matchMediaType(request);
/* 46:64 */     return match ? false : !this.isNegated ? match : true;
/* 47:   */   }
/* 48:   */   
/* 49:   */   protected abstract boolean matchMediaType(HttpServletRequest paramHttpServletRequest);
/* 50:   */   
/* 51:   */   public int compareTo(AbstractMediaTypeExpression other)
/* 52:   */   {
/* 53:70 */     return MediaType.SPECIFICITY_COMPARATOR.compare(getMediaType(), other.getMediaType());
/* 54:   */   }
/* 55:   */   
/* 56:   */   public boolean equals(Object obj)
/* 57:   */   {
/* 58:75 */     if (this == obj) {
/* 59:76 */       return true;
/* 60:   */     }
/* 61:78 */     if ((obj != null) && (getClass().equals(obj.getClass())))
/* 62:   */     {
/* 63:79 */       AbstractMediaTypeExpression other = (AbstractMediaTypeExpression)obj;
/* 64:80 */       return (this.mediaType.equals(other.mediaType)) && (this.isNegated == other.isNegated);
/* 65:   */     }
/* 66:82 */     return false;
/* 67:   */   }
/* 68:   */   
/* 69:   */   public int hashCode()
/* 70:   */   {
/* 71:87 */     return this.mediaType.hashCode();
/* 72:   */   }
/* 73:   */   
/* 74:   */   public String toString()
/* 75:   */   {
/* 76:92 */     StringBuilder builder = new StringBuilder();
/* 77:93 */     if (this.isNegated) {
/* 78:94 */       builder.append('!');
/* 79:   */     }
/* 80:96 */     builder.append(this.mediaType.toString());
/* 81:97 */     return builder.toString();
/* 82:   */   }
/* 83:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.AbstractMediaTypeExpression
 * JD-Core Version:    0.7.0.1
 */