/*  1:   */ package com.kentropy.flow;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class NumberValue
/*  6:   */ {
/*  7: 7 */   int value = 0;
/*  8: 8 */   public String type = "String";
/*  9: 9 */   Age ageValue = null;
/* 10:   */   
/* 11:   */   public NumberValue(Object age)
/* 12:   */   {
/* 13:13 */     this.type = "age";
/* 14:14 */     if ((age instanceof Age))
/* 15:   */     {
/* 16:16 */       this.type = "Age";
/* 17:17 */       this.ageValue = ((Age)age);
/* 18:   */     }
/* 19:   */     else
/* 20:   */     {
/* 21:21 */       this.type = "String";
/* 22:22 */       this.value = Integer.parseInt(age.toString());
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void add(NumberValue nv)
/* 27:   */     throws Exception
/* 28:   */   {
/* 29:28 */     if (!nv.type.equals(this.type)) {
/* 30:30 */       throw new Exception(" Not the same type");
/* 31:   */     }
/* 32:33 */     if (this.type.equals("String")) {
/* 33:35 */       this.value += nv.value;
/* 34:   */     } else {
/* 35:38 */       this.ageValue.add(nv.ageValue);
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Comparable getValue()
/* 40:   */   {
/* 41:43 */     if (this.type.equals("Age")) {
/* 42:44 */       return this.ageValue;
/* 43:   */     }
/* 44:46 */     return Integer.valueOf(this.value);
/* 45:   */   }
/* 46:   */   
/* 47:   */   public static void main(String[] args)
/* 48:   */     throws Exception
/* 49:   */   {
/* 50:51 */     Age age = new Age("15,M");
/* 51:52 */     age.add(new Age("14,M"));
/* 52:53 */     System.out.println("Age is " + age.ageValue);
/* 53:54 */     NumberValue nv = new NumberValue(new Age("15,M"));
/* 54:55 */     NumberValue nv1 = new NumberValue(new Age("14,M"));
/* 55:56 */     nv.add(nv1);
/* 56:57 */     System.out.println(((Age)nv.getValue()).ageValue);
/* 57:58 */     NumberValue nv2 = new NumberValue("-15");
/* 58:59 */     nv2.add(new NumberValue("14"));
/* 59:60 */     System.out.println(nv2.getValue());
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.flow.NumberValue
 * JD-Core Version:    0.7.0.1
 */