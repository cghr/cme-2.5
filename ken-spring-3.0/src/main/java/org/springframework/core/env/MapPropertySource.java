/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.Set;
/*  5:   */ 
/*  6:   */ public class MapPropertySource
/*  7:   */   extends EnumerablePropertySource<Map<String, Object>>
/*  8:   */ {
/*  9:   */   public MapPropertySource(String name, Map<String, Object> source)
/* 10:   */   {
/* 11:31 */     super(name, source);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public Object getProperty(String key)
/* 15:   */   {
/* 16:36 */     return ((Map)this.source).get(key);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String[] getPropertyNames()
/* 20:   */   {
/* 21:41 */     return (String[])((Map)this.source).keySet().toArray(EMPTY_NAMES_ARRAY);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.MapPropertySource
 * JD-Core Version:    0.7.0.1
 */