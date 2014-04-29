/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class ManagedArray
/*  6:   */   extends ManagedList<Object>
/*  7:   */ {
/*  8:   */   volatile Class resolvedElementType;
/*  9:   */   
/* 10:   */   public ManagedArray(String elementTypeName, int size)
/* 11:   */   {
/* 12:40 */     super(size);
/* 13:41 */     Assert.notNull(elementTypeName, "elementTypeName must not be null");
/* 14:42 */     setElementTypeName(elementTypeName);
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ManagedArray
 * JD-Core Version:    0.7.0.1
 */