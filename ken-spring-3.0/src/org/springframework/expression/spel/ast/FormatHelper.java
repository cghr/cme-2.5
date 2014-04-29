/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.core.convert.TypeDescriptor;
/*  5:   */ 
/*  6:   */ public class FormatHelper
/*  7:   */ {
/*  8:   */   public static String formatMethodForMessage(String name, List<TypeDescriptor> argumentTypes)
/*  9:   */   {
/* 10:37 */     StringBuilder sb = new StringBuilder();
/* 11:38 */     sb.append(name);
/* 12:39 */     sb.append("(");
/* 13:40 */     for (int i = 0; i < argumentTypes.size(); i++)
/* 14:   */     {
/* 15:41 */       if (i > 0) {
/* 16:42 */         sb.append(",");
/* 17:   */       }
/* 18:44 */       TypeDescriptor typeDescriptor = (TypeDescriptor)argumentTypes.get(i);
/* 19:45 */       if (typeDescriptor != null) {
/* 20:46 */         sb.append(formatClassNameForMessage(typeDescriptor.getType()));
/* 21:   */       } else {
/* 22:49 */         sb.append(formatClassNameForMessage(null));
/* 23:   */       }
/* 24:   */     }
/* 25:52 */     sb.append(")");
/* 26:53 */     return sb.toString();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public static String formatClassNameForMessage(Class<?> clazz)
/* 30:   */   {
/* 31:63 */     if (clazz == null) {
/* 32:64 */       return "null";
/* 33:   */     }
/* 34:66 */     StringBuilder fmtd = new StringBuilder();
/* 35:67 */     if (clazz.isArray())
/* 36:   */     {
/* 37:68 */       int dims = 1;
/* 38:69 */       Class baseClass = clazz.getComponentType();
/* 39:70 */       while (baseClass.isArray())
/* 40:   */       {
/* 41:71 */         baseClass = baseClass.getComponentType();
/* 42:72 */         dims++;
/* 43:   */       }
/* 44:74 */       fmtd.append(baseClass.getName());
/* 45:75 */       for (int i = 0; i < dims; i++) {
/* 46:76 */         fmtd.append("[]");
/* 47:   */       }
/* 48:   */     }
/* 49:   */     else
/* 50:   */     {
/* 51:79 */       fmtd.append(clazz.getName());
/* 52:   */     }
/* 53:81 */     return fmtd.toString();
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.FormatHelper
 * JD-Core Version:    0.7.0.1
 */