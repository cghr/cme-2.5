/*  1:   */ package org.springframework.util;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.LinkedList;
/*  5:   */ import java.util.List;
/*  6:   */ import java.util.NoSuchElementException;
/*  7:   */ 
/*  8:   */ public class CompositeIterator<E>
/*  9:   */   implements Iterator<E>
/* 10:   */ {
/* 11:30 */   private List<Iterator<E>> iterators = new LinkedList();
/* 12:32 */   private boolean inUse = false;
/* 13:   */   
/* 14:   */   public void add(Iterator<E> iterator)
/* 15:   */   {
/* 16:44 */     Assert.state(!this.inUse, "You can no longer add iterator to a composite iterator that's already in use");
/* 17:45 */     if (this.iterators.contains(iterator)) {
/* 18:46 */       throw new IllegalArgumentException("You cannot add the same iterator twice");
/* 19:   */     }
/* 20:48 */     this.iterators.add(iterator);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean hasNext()
/* 24:   */   {
/* 25:52 */     this.inUse = true;
/* 26:53 */     for (Iterator<Iterator<E>> it = this.iterators.iterator(); it.hasNext();) {
/* 27:54 */       if (((Iterator)it.next()).hasNext()) {
/* 28:55 */         return true;
/* 29:   */       }
/* 30:   */     }
/* 31:58 */     return false;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public E next()
/* 35:   */   {
/* 36:62 */     this.inUse = true;
/* 37:63 */     for (Iterator<Iterator<E>> it = this.iterators.iterator(); it.hasNext();)
/* 38:   */     {
/* 39:64 */       Iterator<E> iterator = (Iterator)it.next();
/* 40:65 */       if (iterator.hasNext()) {
/* 41:66 */         return iterator.next();
/* 42:   */       }
/* 43:   */     }
/* 44:69 */     throw new NoSuchElementException("Exhaused all iterators");
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void remove()
/* 48:   */   {
/* 49:73 */     throw new UnsupportedOperationException("Remove is not supported");
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.CompositeIterator
 * JD-Core Version:    0.7.0.1
 */