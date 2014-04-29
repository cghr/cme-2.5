package org.springframework.context.annotation;

public abstract interface ImportSelector
{
  public abstract String[] selectImports(ImportSelectorContext paramImportSelectorContext);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ImportSelector
 * JD-Core Version:    0.7.0.1
 */