package com.kentropy.web;

import javax.servlet.http.HttpServletRequest;
import net.xoetrope.xui.data.XModel;

public abstract interface RequestTransformer
{
  public abstract void transform(HttpServletRequest paramHttpServletRequest, XModel paramXModel);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.web.RequestTransformer
 * JD-Core Version:    0.7.0.1
 */