/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.List;
/*   6:    */ import joptsimple.OptionSet;
/*   7:    */ 
/*   8:    */ public class JOptCommandLinePropertySource
/*   9:    */   extends CommandLinePropertySource<OptionSet>
/*  10:    */ {
/*  11:    */   public JOptCommandLinePropertySource(OptionSet options)
/*  12:    */   {
/*  13: 64 */     super(options);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public JOptCommandLinePropertySource(String name, OptionSet options)
/*  17:    */   {
/*  18: 72 */     super(name, options);
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected boolean containsOption(String key)
/*  22:    */   {
/*  23: 77 */     return ((OptionSet)this.source).has(key);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public List<String> getOptionValues(String key)
/*  27:    */   {
/*  28: 82 */     List<?> argValues = ((OptionSet)this.source).valuesOf(key);
/*  29: 83 */     List<String> stringArgValues = new ArrayList();
/*  30: 84 */     for (Object argValue : argValues)
/*  31:    */     {
/*  32: 85 */       if (!(argValue instanceof String)) {
/*  33: 86 */         throw new IllegalArgumentException("argument values must be of type String");
/*  34:    */       }
/*  35: 88 */       stringArgValues.add((String)argValue);
/*  36:    */     }
/*  37: 90 */     if (stringArgValues.size() == 0)
/*  38:    */     {
/*  39: 91 */       if (((OptionSet)this.source).has(key)) {
/*  40: 92 */         return Collections.emptyList();
/*  41:    */       }
/*  42: 95 */       return null;
/*  43:    */     }
/*  44: 98 */     return Collections.unmodifiableList(stringArgValues);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected List<String> getNonOptionArgs()
/*  48:    */   {
/*  49:103 */     return ((OptionSet)this.source).nonOptionArguments();
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.JOptCommandLinePropertySource
 * JD-Core Version:    0.7.0.1
 */