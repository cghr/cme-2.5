/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ 
/*   5:    */ public class SimpleCommandLinePropertySource
/*   6:    */   extends CommandLinePropertySource<CommandLineArgs>
/*   7:    */ {
/*   8:    */   public SimpleCommandLinePropertySource(String... args)
/*   9:    */   {
/*  10: 87 */     super(new SimpleCommandLineArgsParser().parse(args));
/*  11:    */   }
/*  12:    */   
/*  13:    */   public SimpleCommandLinePropertySource(String name, String[] args)
/*  14:    */   {
/*  15: 95 */     super(name, new SimpleCommandLineArgsParser().parse(args));
/*  16:    */   }
/*  17:    */   
/*  18:    */   protected boolean containsOption(String key)
/*  19:    */   {
/*  20:100 */     return ((CommandLineArgs)this.source).containsOption(key);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected List<String> getOptionValues(String key)
/*  24:    */   {
/*  25:105 */     return ((CommandLineArgs)this.source).getOptionValues(key);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected List<String> getNonOptionArgs()
/*  29:    */   {
/*  30:110 */     return ((CommandLineArgs)this.source).getNonOptionArgs();
/*  31:    */   }
/*  32:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.SimpleCommandLinePropertySource
 * JD-Core Version:    0.7.0.1
 */