/*   1:    */ package org.springframework.expression.spel.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.expression.EvaluationException;
/*   7:    */ import org.springframework.expression.TypeLocator;
/*   8:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*   9:    */ import org.springframework.expression.spel.SpelMessage;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ 
/*  12:    */ public class StandardTypeLocator
/*  13:    */   implements TypeLocator
/*  14:    */ {
/*  15:    */   private ClassLoader loader;
/*  16: 41 */   private final List<String> knownPackagePrefixes = new ArrayList();
/*  17:    */   
/*  18:    */   public StandardTypeLocator()
/*  19:    */   {
/*  20: 45 */     this(ClassUtils.getDefaultClassLoader());
/*  21:    */   }
/*  22:    */   
/*  23:    */   public StandardTypeLocator(ClassLoader loader)
/*  24:    */   {
/*  25: 49 */     this.loader = loader;
/*  26:    */     
/*  27: 51 */     registerImport("java.lang");
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Class<?> findType(String typename)
/*  31:    */     throws EvaluationException
/*  32:    */   {
/*  33: 63 */     String nameToLookup = typename;
/*  34:    */     try
/*  35:    */     {
/*  36: 65 */       return this.loader.loadClass(nameToLookup);
/*  37:    */     }
/*  38:    */     catch (ClassNotFoundException localClassNotFoundException1)
/*  39:    */     {
/*  40: 70 */       for (String prefix : this.knownPackagePrefixes) {
/*  41:    */         try
/*  42:    */         {
/*  43: 72 */           nameToLookup = prefix + "." + typename;
/*  44: 73 */           return this.loader.loadClass(nameToLookup);
/*  45:    */         }
/*  46:    */         catch (ClassNotFoundException localClassNotFoundException2) {}
/*  47:    */       }
/*  48: 79 */       throw new SpelEvaluationException(SpelMessage.TYPE_NOT_FOUND, new Object[] { typename });
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void registerImport(String prefix)
/*  53:    */   {
/*  54: 88 */     this.knownPackagePrefixes.add(prefix);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public List<String> getImportPrefixes()
/*  58:    */   {
/*  59: 96 */     return Collections.unmodifiableList(this.knownPackagePrefixes);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void removeImport(String prefix)
/*  63:    */   {
/*  64:100 */     this.knownPackagePrefixes.remove(prefix);
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.StandardTypeLocator
 * JD-Core Version:    0.7.0.1
 */