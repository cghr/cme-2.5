/*  1:   */ package com.kentropy.agents.cme;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ 
/*  6:   */ public class TestInvoke
/*  7:   */ {
/*  8:   */   public void test1(String tt)
/*  9:   */   {
/* 10:10 */     System.out.println("Test1" + tt);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public void test(String tt)
/* 14:   */   {
/* 15:15 */     System.out.println("Test" + tt);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public static void main(String[] args)
/* 19:   */     throws Exception, NoSuchMethodException
/* 20:   */   {
/* 21:20 */     TestInvoke tt = new TestInvoke();
/* 22:21 */     Class[] tt0 = { String.class };
/* 23:22 */     Method mt = TestInvoke.class.getMethod("test1", tt0);
/* 24:23 */     Object[] tt1 = { "tt-tt" };
/* 25:24 */     mt.invoke(tt, tt1);
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.TestInvoke
 * JD-Core Version:    0.7.0.1
 */