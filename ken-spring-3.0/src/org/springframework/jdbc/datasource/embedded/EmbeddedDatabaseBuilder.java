/*   1:    */ package org.springframework.jdbc.datasource.embedded;
/*   2:    */ 
/*   3:    */ import org.springframework.core.io.DefaultResourceLoader;
/*   4:    */ import org.springframework.core.io.ResourceLoader;
/*   5:    */ import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
/*   6:    */ 
/*   7:    */ public class EmbeddedDatabaseBuilder
/*   8:    */ {
/*   9:    */   private final EmbeddedDatabaseFactory databaseFactory;
/*  10:    */   private final ResourceDatabasePopulator databasePopulator;
/*  11:    */   private final ResourceLoader resourceLoader;
/*  12:    */   
/*  13:    */   public EmbeddedDatabaseBuilder()
/*  14:    */   {
/*  15: 50 */     this(new DefaultResourceLoader());
/*  16:    */   }
/*  17:    */   
/*  18:    */   public EmbeddedDatabaseBuilder(ResourceLoader resourceLoader)
/*  19:    */   {
/*  20: 58 */     this.databaseFactory = new EmbeddedDatabaseFactory();
/*  21: 59 */     this.databasePopulator = new ResourceDatabasePopulator();
/*  22: 60 */     this.databaseFactory.setDatabasePopulator(this.databasePopulator);
/*  23: 61 */     this.resourceLoader = resourceLoader;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public EmbeddedDatabaseBuilder setName(String databaseName)
/*  27:    */   {
/*  28: 71 */     this.databaseFactory.setDatabaseName(databaseName);
/*  29: 72 */     return this;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public EmbeddedDatabaseBuilder setType(EmbeddedDatabaseType databaseType)
/*  33:    */   {
/*  34: 82 */     this.databaseFactory.setDatabaseType(databaseType);
/*  35: 83 */     return this;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public EmbeddedDatabaseBuilder addScript(String sqlResource)
/*  39:    */   {
/*  40: 92 */     this.databasePopulator.addScript(this.resourceLoader.getResource(sqlResource));
/*  41: 93 */     return this;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public EmbeddedDatabaseBuilder addDefaultScripts()
/*  45:    */   {
/*  46:102 */     addScript("schema.sql");
/*  47:103 */     addScript("data.sql");
/*  48:104 */     return this;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public EmbeddedDatabase build()
/*  52:    */   {
/*  53:112 */     return this.databaseFactory.getDatabase();
/*  54:    */   }
/*  55:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
 * JD-Core Version:    0.7.0.1
 */