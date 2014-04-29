/*  1:   */ package org.springframework.web.servlet.mvc.condition;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Iterator;
/*  5:   */ 
/*  6:   */ public abstract class AbstractRequestCondition<T extends AbstractRequestCondition<T>>
/*  7:   */   implements RequestCondition<T>
/*  8:   */ {
/*  9:   */   protected abstract Collection<?> getContent();
/* 10:   */   
/* 11:   */   public boolean equals(Object o)
/* 12:   */   {
/* 13:40 */     if (this == o) {
/* 14:41 */       return true;
/* 15:   */     }
/* 16:43 */     if ((o != null) && (getClass().equals(o.getClass())))
/* 17:   */     {
/* 18:44 */       AbstractRequestCondition<?> other = (AbstractRequestCondition)o;
/* 19:45 */       return getContent().equals(other.getContent());
/* 20:   */     }
/* 21:47 */     return false;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int hashCode()
/* 25:   */   {
/* 26:52 */     return getContent().hashCode();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:57 */     StringBuilder builder = new StringBuilder("[");
/* 32:58 */     for (Iterator<?> iterator = getContent().iterator(); iterator.hasNext();)
/* 33:   */     {
/* 34:59 */       Object expression = iterator.next();
/* 35:60 */       builder.append(expression.toString());
/* 36:61 */       if (iterator.hasNext()) {
/* 37:62 */         builder.append(getToStringInfix());
/* 38:   */       }
/* 39:   */     }
/* 40:65 */     builder.append("]");
/* 41:66 */     return builder.toString();
/* 42:   */   }
/* 43:   */   
/* 44:   */   protected abstract String getToStringInfix();
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
 * JD-Core Version:    0.7.0.1
 */