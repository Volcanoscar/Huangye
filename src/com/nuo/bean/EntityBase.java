package com.nuo.bean;

import java.io.Serializable;

/**
 * Author: wyouflf
 * Date: 13-8-13
 * Time: 上午11:15
 */
public abstract class EntityBase implements Serializable{


    //@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    //@NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
    // @Transient 使这个列被忽略，不存入数据库,,,, 静态字段也不会存入数据库
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}