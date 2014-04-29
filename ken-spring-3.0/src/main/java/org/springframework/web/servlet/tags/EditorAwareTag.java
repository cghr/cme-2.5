package org.springframework.web.servlet.tags;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspException;

public abstract interface EditorAwareTag
{
  public abstract PropertyEditor getEditor()
    throws JspException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.EditorAwareTag
 * JD-Core Version:    0.7.0.1
 */