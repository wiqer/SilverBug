package com.aixuexi.io.github.wiqer.bug.manage;

import lombok.Getter;

@Getter
public enum InstanceSourceEnum {
    SPRING("s",2,"SPRING","SPRING 代理"),
    JAVA("j",1,"JAVA","JAVA 反射"),
    NOTHING("n",0,"NOTHING","无")
    ,;
    private String typeShortName;
    private int typeId;
    private String typeName;
    private String typeDetails;

    InstanceSourceEnum(String typeShortName, int typeId, String typeName, String typeDetails) {
        this.typeShortName = typeShortName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeDetails = typeDetails;
        if(!this.name().equals(this.typeName)){
            System.out.println(this.name());
            System.out.println(this.typeName);
            throw new RuntimeException("com.aixuexi.io.github.wiqer.bug.manage.InstanceSourceEnum: typeName 必须与name一致");
        }
    }

    public static InstanceSourceEnum getById(Integer id){
        for (InstanceSourceEnum contentTypeEnum : InstanceSourceEnum.values()){
            if(contentTypeEnum.getTypeId() == id){
                return contentTypeEnum;
            }
        }
        return NOTHING;
    }

    public static InstanceSourceEnum getByTypeName(String typeName){
        for (InstanceSourceEnum contentTypeEnum : InstanceSourceEnum.values()){
            if(contentTypeEnum.getTypeName().equals(typeName)){
                return contentTypeEnum;
            }
        }
        return NOTHING;
    }
}
