/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Collections;
/*  5:   */ import java.util.HashMap;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map;
/*  8:   */ import java.util.Set;
/*  9:   */ 
/* 10:   */ class CommandLineArgs
/* 11:   */ {
/* 12:36 */   private final Map<String, List<String>> optionArgs = new HashMap();
/* 13:37 */   private final List<String> nonOptionArgs = new ArrayList();
/* 14:   */   
/* 15:   */   public void addOptionArg(String optionName, String optionValue)
/* 16:   */   {
/* 17:46 */     if (!this.optionArgs.containsKey(optionName)) {
/* 18:47 */       this.optionArgs.put(optionName, new ArrayList());
/* 19:   */     }
/* 20:49 */     if (optionValue != null) {
/* 21:50 */       ((List)this.optionArgs.get(optionName)).add(optionValue);
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Set<String> getOptionNames()
/* 26:   */   {
/* 27:58 */     return Collections.unmodifiableSet(this.optionArgs.keySet());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean containsOption(String optionName)
/* 31:   */   {
/* 32:65 */     return this.optionArgs.containsKey(optionName);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public List<String> getOptionValues(String optionName)
/* 36:   */   {
/* 37:74 */     return (List)this.optionArgs.get(optionName);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void addNonOptionArg(String value)
/* 41:   */   {
/* 42:81 */     this.nonOptionArgs.add(value);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public List<String> getNonOptionArgs()
/* 46:   */   {
/* 47:88 */     return Collections.unmodifiableList(this.nonOptionArgs);
/* 48:   */   }
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.CommandLineArgs
 * JD-Core Version:    0.7.0.1
 */