package com.example.demo.entity.enums;

/**
 * @author zhangzongbo
 * @date 19-2-21 下午6:43
 */
public enum UserStatusEnum {

    /**
     * 使用中
     */
    USING(1),
    /**
     * 已删除
     */
    DELETED(-1),
    /**
     * 已锁定
     */
    LOCKED(0);

    UserStatusEnum(int value){
        this.value = value;
    }

    private int value;

    public int getValue(){
        return value;
    }

    public String getStringValue(){
        return String.valueOf(value);
    }

    public static UserStatusEnum getByValue(int value){
        for (UserStatusEnum statu : UserStatusEnum.values()){
            if (statu.getValue() == value){
                return statu;
            }
        }
        return USING;
    }


}
