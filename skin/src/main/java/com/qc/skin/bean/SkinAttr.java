package com.qc.skin.bean;

/**
 * author : Don
 * date : 2020/11/24 10:10
 * description :
 */
public class SkinAttr {
  /***
   * 对应View的属性
   */
  public String attrName;

  /***
   * 属性对应的R文件的id
   */
  public int attrRefId;

  /***
   * 属性值refrence id对应的名称，如R.color.XX，则此值为"XX"
   */
  public String attrValueRefName;

  public int note = -1; // 目前就IconTabTextView类使用了这个字段

  /***
   * 属性类型 drawable color
   */
  public String attrType;

  public SkinAttr(String attrName, int attrValueRefId, String attrValueRefName,
      String attrValueTypeName) {
    this.attrName = attrName;
    this.attrRefId = attrValueRefId;
    this.attrValueRefName = attrValueRefName;
    this.attrType = attrValueTypeName;
  }

  public SkinAttr(String attrName, int attrValueRefId, String attrValueRefName,
      String attrValueTypeName, int note) {

    this.attrName = attrName;
    this.attrRefId = attrValueRefId;
    this.attrValueRefName = attrValueRefName;
    this.attrType = attrValueTypeName;
    this.note = note;
  }

  @Override
  public String toString() {
    return "SkinAttr \n[\nattrName=" + attrName + ", \n"
        + "attrValueRefId=" + attrRefId + ", \n"
        + "attrValueRefName=" + attrValueRefName + ", \n"
        + "attrValueTypeName=" + attrType
        + "\n]";
  }

}
