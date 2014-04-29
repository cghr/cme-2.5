/*  1:   */ package org.springframework.dao.support;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.dao.DataAccessException;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public class ChainedPersistenceExceptionTranslator
/*  9:   */   implements PersistenceExceptionTranslator
/* 10:   */ {
/* 11:37 */   private final List<PersistenceExceptionTranslator> delegates = new ArrayList(4);
/* 12:   */   
/* 13:   */   public final void addDelegate(PersistenceExceptionTranslator pet)
/* 14:   */   {
/* 15:44 */     Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
/* 16:45 */     this.delegates.add(pet);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final PersistenceExceptionTranslator[] getDelegates()
/* 20:   */   {
/* 21:52 */     return (PersistenceExceptionTranslator[])this.delegates.toArray(new PersistenceExceptionTranslator[this.delegates.size()]);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public DataAccessException translateExceptionIfPossible(RuntimeException ex)
/* 25:   */   {
/* 26:57 */     for (PersistenceExceptionTranslator pet : this.delegates)
/* 27:   */     {
/* 28:58 */       DataAccessException translatedDex = pet.translateExceptionIfPossible(ex);
/* 29:59 */       if (translatedDex != null) {
/* 30:60 */         return translatedDex;
/* 31:   */       }
/* 32:   */     }
/* 33:63 */     return null;
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.support.ChainedPersistenceExceptionTranslator
 * JD-Core Version:    0.7.0.1
 */