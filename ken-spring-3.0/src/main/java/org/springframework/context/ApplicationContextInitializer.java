package org.springframework.context;

public abstract interface ApplicationContextInitializer<C extends ConfigurableApplicationContext>
{
  public abstract void initialize(C paramC);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.ApplicationContextInitializer
 * JD-Core Version:    0.7.0.1
 */