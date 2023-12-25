package io.github.wiqer.bug.level;

import lombok.Getter;

@Getter
public enum LevelTypeEnum {
    BRANCH("b",2,"BRANCH","树枝"),
    LEAF("l",3,"LEAF","树叶"),
    TRUNK("t",1,"TRUNK","树干"),
    BUG("bug",4,"BUG","小虫"),
    NOTHING("n",0,"NOTHING","无")
    ,;
    private String typeShortName;
    private int typeId;
    private String typeName;
    private String typeDetails;

    LevelTypeEnum(String typeShortName, int typeId, String typeName, String typeDetails) {
        this.typeShortName = typeShortName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeDetails = typeDetails;
        if(!this.name().equals(this.typeName)){
            System.out.println(this.name());
            System.out.println(this.typeName);
            throw new RuntimeException("com.aixuexi.io.github.wiqer.bug.level.LevelTypeEnum: typeName 必须与name一致");
        }
    }

    public static LevelTypeEnum getById(Integer id){
        for (LevelTypeEnum contentTypeEnum : LevelTypeEnum.values()){
            if(contentTypeEnum.getTypeId() == id){
                return contentTypeEnum;
            }
        }
        return NOTHING;
    }

    public static LevelTypeEnum getByTypeName(String typeName){
        for (LevelTypeEnum contentTypeEnum : LevelTypeEnum.values()){
            if(contentTypeEnum.getTypeName().equals(typeName)){
                return contentTypeEnum;
            }
        }
        return NOTHING;
    }
}
