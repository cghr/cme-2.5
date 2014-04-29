package org.springframework.asm.commons;

class SerialVersionUIDAdder$Item
  implements Comparable
{
  String name;
  int access;
  String desc;
  
  SerialVersionUIDAdder$Item(String paramString1, int paramInt, String paramString2)
  {
    this.name = paramString1;
    this.access = paramInt;
    this.desc = paramString2;
  }
  
  public int compareTo(Object paramObject)
  {
    Item localItem = (Item)paramObject;
    int i = this.name.compareTo(localItem.name);
    if (i == 0) {
      i = this.desc.compareTo(localItem.desc);
    }
    return i;
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.SerialVersionUIDAdder.Item
 * JD-Core Version:    0.7.0.1
 */