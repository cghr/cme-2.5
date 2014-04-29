/*   1:    */ package org.springframework.beans.factory.parsing;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ 
/*   5:    */ public final class ParseState
/*   6:    */ {
/*   7:    */   private static final char TAB = '\t';
/*   8:    */   private final Stack state;
/*   9:    */   
/*  10:    */   public ParseState()
/*  11:    */   {
/*  12: 50 */     this.state = new Stack();
/*  13:    */   }
/*  14:    */   
/*  15:    */   private ParseState(ParseState other)
/*  16:    */   {
/*  17: 58 */     this.state = ((Stack)other.state.clone());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void push(Entry entry)
/*  21:    */   {
/*  22: 66 */     this.state.push(entry);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void pop()
/*  26:    */   {
/*  27: 73 */     this.state.pop();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Entry peek()
/*  31:    */   {
/*  32: 81 */     return (Entry)(this.state.empty() ? null : this.state.peek());
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ParseState snapshot()
/*  36:    */   {
/*  37: 89 */     return new ParseState(this);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String toString()
/*  41:    */   {
/*  42: 98 */     StringBuilder sb = new StringBuilder();
/*  43: 99 */     for (int x = 0; x < this.state.size(); x++)
/*  44:    */     {
/*  45:100 */       if (x > 0)
/*  46:    */       {
/*  47:101 */         sb.append('\n');
/*  48:102 */         for (int y = 0; y < x; y++) {
/*  49:103 */           sb.append('\t');
/*  50:    */         }
/*  51:105 */         sb.append("-> ");
/*  52:    */       }
/*  53:107 */       sb.append(this.state.get(x));
/*  54:    */     }
/*  55:109 */     return sb.toString();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static abstract interface Entry {}
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ParseState
 * JD-Core Version:    0.7.0.1
 */