/*  1:   */ package org.springframework.context.expression;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.expression.AccessException;
/*  5:   */ import org.springframework.expression.EvaluationContext;
/*  6:   */ import org.springframework.expression.PropertyAccessor;
/*  7:   */ import org.springframework.expression.TypedValue;
/*  8:   */ 
/*  9:   */ public class MapAccessor
/* 10:   */   implements PropertyAccessor
/* 11:   */ {
/* 12:   */   public boolean canRead(EvaluationContext context, Object target, String name)
/* 13:   */     throws AccessException
/* 14:   */   {
/* 15:37 */     Map map = (Map)target;
/* 16:38 */     return map.containsKey(name);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public TypedValue read(EvaluationContext context, Object target, String name)
/* 20:   */     throws AccessException
/* 21:   */   {
/* 22:42 */     Map map = (Map)target;
/* 23:43 */     Object value = map.get(name);
/* 24:44 */     if ((value == null) && (!map.containsKey(name))) {
/* 25:45 */       throw new MapAccessException(name);
/* 26:   */     }
/* 27:47 */     return new TypedValue(value);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean canWrite(EvaluationContext context, Object target, String name)
/* 31:   */     throws AccessException
/* 32:   */   {
/* 33:51 */     return true;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void write(EvaluationContext context, Object target, String name, Object newValue)
/* 37:   */     throws AccessException
/* 38:   */   {
/* 39:56 */     Map map = (Map)target;
/* 40:57 */     map.put(name, newValue);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public Class[] getSpecificTargetClasses()
/* 44:   */   {
/* 45:61 */     return new Class[] { Map.class };
/* 46:   */   }
/* 47:   */   
/* 48:   */   private static class MapAccessException
/* 49:   */     extends AccessException
/* 50:   */   {
/* 51:   */     private final String key;
/* 52:   */     
/* 53:   */     public MapAccessException(String key)
/* 54:   */     {
/* 55:74 */       super();
/* 56:75 */       this.key = key;
/* 57:   */     }
/* 58:   */     
/* 59:   */     public String getMessage()
/* 60:   */     {
/* 61:80 */       return "Map does not contain a value for key '" + this.key + "'";
/* 62:   */     }
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.expression.MapAccessor
 * JD-Core Version:    0.7.0.1
 */