/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.core.io.Resource;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class Location
/*  7:   */ {
/*  8:   */   private final Resource resource;
/*  9:   */   private final Object source;
/* 10:   */   
/* 11:   */   public Location(Resource resource)
/* 12:   */   {
/* 13:47 */     this(resource, null);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Location(Resource resource, Object source)
/* 17:   */   {
/* 18:57 */     Assert.notNull(resource, "Resource must not be null");
/* 19:58 */     this.resource = resource;
/* 20:59 */     this.source = source;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Resource getResource()
/* 24:   */   {
/* 25:67 */     return this.resource;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Object getSource()
/* 29:   */   {
/* 30:77 */     return this.source;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.Location
 * JD-Core Version:    0.7.0.1
 */