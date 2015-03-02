package com.nuo.bean.notebook;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.nuo.bean.EntityBase;

/**
 * 笔记本类型
 * 手写，图片，录音
 */
@Table(name = "note_book_type")
public class NoteBookType extends EntityBase{

    public  static  final  Integer TYPE_WRITE= 1;
    public static final Integer TYPE_PIC = 2;
    /**
     * 索引, 在笔记中的位置
     */
    @Column(column = "position")
    private Integer position;
    /**
     * 类型
     */
    @Column(column = "type")
    private Integer type;
    /**
     * 路径
     */
    @Column(column = "path")
    private String path;

    @Column(column = "note_book_id")
    private Integer noteBookId;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNoteBookId(Integer noteBookId) {
        this.noteBookId = noteBookId;
    }

    public Integer getNoteBookId() {
        return noteBookId;
    }
}
