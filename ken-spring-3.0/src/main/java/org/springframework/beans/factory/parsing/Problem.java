/*   1:    */ package org.springframework.beans.factory.parsing;
/*   2:    */ 
/*   3:    */ import org.springframework.core.io.Resource;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ 
/*   6:    */ public class Problem
/*   7:    */ {
/*   8:    */   private final String message;
/*   9:    */   private final Location location;
/*  10:    */   private final ParseState parseState;
/*  11:    */   private final Throwable rootCause;
/*  12:    */   
/*  13:    */   public Problem(String message, Location location)
/*  14:    */   {
/*  15: 49 */     this(message, location, null, null);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public Problem(String message, Location location, ParseState parseState)
/*  19:    */   {
/*  20: 59 */     this(message, location, parseState, null);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Problem(String message, Location location, ParseState parseState, Throwable rootCause)
/*  24:    */   {
/*  25: 70 */     Assert.notNull(message, "Message must not be null");
/*  26: 71 */     Assert.notNull(location, "Location must not be null");
/*  27: 72 */     this.message = message;
/*  28: 73 */     this.location = location;
/*  29: 74 */     this.parseState = parseState;
/*  30: 75 */     this.rootCause = rootCause;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getMessage()
/*  34:    */   {
/*  35: 83 */     return this.message;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Location getLocation()
/*  39:    */   {
/*  40: 90 */     return this.location;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getResourceDescription()
/*  44:    */   {
/*  45: 99 */     return getLocation().getResource().getDescription();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public ParseState getParseState()
/*  49:    */   {
/*  50:106 */     return this.parseState;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Throwable getRootCause()
/*  54:    */   {
/*  55:113 */     return this.rootCause;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String toString()
/*  59:    */   {
/*  60:119 */     StringBuilder sb = new StringBuilder();
/*  61:120 */     sb.append("Configuration problem: ");
/*  62:121 */     sb.append(getMessage());
/*  63:122 */     sb.append("\nOffending resource: ").append(getResourceDescription());
/*  64:123 */     if (getParseState() != null) {
/*  65:124 */       sb.append('\n').append(getParseState());
/*  66:    */     }
/*  67:126 */     return sb.toString();
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.Problem
 * JD-Core Version:    0.7.0.1
 */