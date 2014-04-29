package org.springframework.ui.context;

public abstract interface HierarchicalThemeSource
  extends ThemeSource
{
  public abstract void setParentThemeSource(ThemeSource paramThemeSource);
  
  public abstract ThemeSource getParentThemeSource();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.context.HierarchicalThemeSource
 * JD-Core Version:    0.7.0.1
 */