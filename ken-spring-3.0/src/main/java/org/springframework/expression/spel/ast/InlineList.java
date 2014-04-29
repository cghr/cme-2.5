/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.expression.EvaluationException;
/*   7:    */ import org.springframework.expression.TypedValue;
/*   8:    */ import org.springframework.expression.spel.ExpressionState;
/*   9:    */ import org.springframework.expression.spel.SpelNode;
/*  10:    */ 
/*  11:    */ public class InlineList
/*  12:    */   extends SpelNodeImpl
/*  13:    */ {
/*  14: 36 */   TypedValue constant = null;
/*  15:    */   
/*  16:    */   public InlineList(int pos, SpelNodeImpl... args)
/*  17:    */   {
/*  18: 39 */     super(pos, args);
/*  19: 40 */     checkIfConstant();
/*  20:    */   }
/*  21:    */   
/*  22:    */   private void checkIfConstant()
/*  23:    */   {
/*  24: 49 */     boolean isConstant = true;
/*  25: 50 */     int c = 0;
/*  26: 50 */     for (int max = getChildCount(); c < max; c++)
/*  27:    */     {
/*  28: 51 */       SpelNode child = getChild(c);
/*  29: 52 */       if (!(child instanceof Literal)) {
/*  30: 53 */         if ((child instanceof InlineList))
/*  31:    */         {
/*  32: 54 */           InlineList inlineList = (InlineList)child;
/*  33: 55 */           if (!inlineList.isConstant()) {
/*  34: 56 */             isConstant = false;
/*  35:    */           }
/*  36:    */         }
/*  37:    */         else
/*  38:    */         {
/*  39: 59 */           isConstant = false;
/*  40:    */         }
/*  41:    */       }
/*  42:    */     }
/*  43: 63 */     if (isConstant)
/*  44:    */     {
/*  45: 64 */       List<Object> constantList = new ArrayList();
/*  46: 65 */       int childcount = getChildCount();
/*  47: 66 */       for (int c = 0; c < childcount; c++)
/*  48:    */       {
/*  49: 67 */         SpelNode child = getChild(c);
/*  50: 68 */         if ((child instanceof Literal)) {
/*  51: 69 */           constantList.add(((Literal)child).getLiteralValue().getValue());
/*  52: 70 */         } else if ((child instanceof InlineList)) {
/*  53: 71 */           constantList.add(((InlineList)child).getConstantValue());
/*  54:    */         }
/*  55:    */       }
/*  56: 74 */       this.constant = new TypedValue(Collections.unmodifiableList(constantList));
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public TypedValue getValueInternal(ExpressionState expressionState)
/*  61:    */     throws EvaluationException
/*  62:    */   {
/*  63: 80 */     if (this.constant != null) {
/*  64: 81 */       return this.constant;
/*  65:    */     }
/*  66: 83 */     List<Object> returnValue = new ArrayList();
/*  67: 84 */     int childcount = getChildCount();
/*  68: 85 */     for (int c = 0; c < childcount; c++) {
/*  69: 86 */       returnValue.add(getChild(c).getValue(expressionState));
/*  70:    */     }
/*  71: 88 */     return new TypedValue(returnValue);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String toStringAST()
/*  75:    */   {
/*  76: 94 */     StringBuilder s = new StringBuilder();
/*  77:    */     
/*  78: 96 */     s.append('{');
/*  79: 97 */     int count = getChildCount();
/*  80: 98 */     for (int c = 0; c < count; c++)
/*  81:    */     {
/*  82: 99 */       if (c > 0) {
/*  83:100 */         s.append(',');
/*  84:    */       }
/*  85:102 */       s.append(getChild(c).toStringAST());
/*  86:    */     }
/*  87:104 */     s.append('}');
/*  88:105 */     return s.toString();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean isConstant()
/*  92:    */   {
/*  93:112 */     return this.constant != null;
/*  94:    */   }
/*  95:    */   
/*  96:    */   private List<Object> getConstantValue()
/*  97:    */   {
/*  98:117 */     return (List)this.constant.getValue();
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.InlineList
 * JD-Core Version:    0.7.0.1
 */