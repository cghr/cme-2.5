/*   1:    */ package org.springframework.core.env;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.util.StringUtils;
/*   6:    */ 
/*   7:    */ public abstract class CommandLinePropertySource<T>
/*   8:    */   extends PropertySource<T>
/*   9:    */ {
/*  10:    */   public static final String COMMAND_LINE_PROPERTY_SOURCE_NAME = "commandLineArgs";
/*  11:    */   public static final String DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME = "nonOptionArgs";
/*  12:196 */   private String nonOptionArgsPropertyName = "nonOptionArgs";
/*  13:    */   
/*  14:    */   public CommandLinePropertySource(T source)
/*  15:    */   {
/*  16:203 */     super("commandLineArgs", source);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public CommandLinePropertySource(String name, T source)
/*  20:    */   {
/*  21:211 */     super(name, source);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setNonOptionArgsPropertyName(String nonOptionArgsPropertyName)
/*  25:    */   {
/*  26:219 */     this.nonOptionArgsPropertyName = nonOptionArgsPropertyName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public final boolean containsProperty(String key)
/*  30:    */   {
/*  31:232 */     if (this.nonOptionArgsPropertyName.equals(key)) {
/*  32:233 */       return !getNonOptionArgs().isEmpty();
/*  33:    */     }
/*  34:235 */     return containsOption(key);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public final String getProperty(String key)
/*  38:    */   {
/*  39:250 */     if (this.nonOptionArgsPropertyName.equals(key))
/*  40:    */     {
/*  41:251 */       Collection<String> nonOptionArguments = getNonOptionArgs();
/*  42:252 */       if (nonOptionArguments.isEmpty()) {
/*  43:253 */         return null;
/*  44:    */       }
/*  45:256 */       return StringUtils.collectionToCommaDelimitedString(nonOptionArguments);
/*  46:    */     }
/*  47:259 */     Collection<String> optionValues = getOptionValues(key);
/*  48:260 */     if (optionValues == null) {
/*  49:261 */       return null;
/*  50:    */     }
/*  51:264 */     return StringUtils.collectionToCommaDelimitedString(optionValues);
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected abstract boolean containsOption(String paramString);
/*  55:    */   
/*  56:    */   protected abstract List<String> getOptionValues(String paramString);
/*  57:    */   
/*  58:    */   protected abstract List<String> getNonOptionArgs();
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.CommandLinePropertySource
 * JD-Core Version:    0.7.0.1
 */