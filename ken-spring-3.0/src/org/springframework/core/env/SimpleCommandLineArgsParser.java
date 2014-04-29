/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ class SimpleCommandLineArgsParser
/*  4:   */ {
/*  5:   */   public CommandLineArgs parse(String... args)
/*  6:   */   {
/*  7:61 */     CommandLineArgs commandLineArgs = new CommandLineArgs();
/*  8:62 */     for (String arg : args) {
/*  9:63 */       if (arg.startsWith("--"))
/* 10:   */       {
/* 11:64 */         String optionText = arg.substring(2, arg.length());
/* 12:   */         
/* 13:66 */         String optionValue = null;
/* 14:   */         String optionName;
/* 15:67 */         if (optionText.contains("="))
/* 16:   */         {
/* 17:68 */           String optionName = optionText.substring(0, optionText.indexOf("="));
/* 18:69 */           optionValue = optionText.substring(optionText.indexOf("=") + 1, optionText.length());
/* 19:   */         }
/* 20:   */         else
/* 21:   */         {
/* 22:72 */           optionName = optionText;
/* 23:   */         }
/* 24:74 */         if ((optionName.isEmpty()) || ((optionValue != null) && (optionValue.isEmpty()))) {
/* 25:75 */           throw new IllegalArgumentException("Invalid argument syntax: " + arg);
/* 26:   */         }
/* 27:77 */         commandLineArgs.addOptionArg(optionName, optionValue);
/* 28:   */       }
/* 29:   */       else
/* 30:   */       {
/* 31:80 */         commandLineArgs.addNonOptionArg(arg);
/* 32:   */       }
/* 33:   */     }
/* 34:83 */     return commandLineArgs;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.SimpleCommandLineArgsParser
 * JD-Core Version:    0.7.0.1
 */