package com.nuo.bean.notebook;

import com.lidroid.xutils.db.annotation.Column;
import com.nuo.bean.EntityBase;

import java.util.Date;

/**
 * Created by zxl on 2015/2/10.
 * 标签
 */
public class NoteBookLabel extends EntityBase {
    @Column(column = "name")
    private String name;
    @Column(column = "create_time")
    private Date create_time;

    private boolean hasTag;

    public boolean isHasTag() {
        return hasTag;
    }

    public void setHasTag(boolean hasTag) {
        this.hasTag = hasTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteBookLabel)) return false;
        NoteBookLabel that = (NoteBookLabel) o;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
