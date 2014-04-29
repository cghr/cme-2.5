package org.springframework.expression;

public abstract interface ExpressionParser
{
  public abstract Expression parseExpression(String paramString)
    throws ParseException;
  
  public abstract Expression parseExpression(String paramString, ParserContext paramParserContext)
    throws ParseException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.ExpressionParser
 * JD-Core Version:    0.7.0.1
 */