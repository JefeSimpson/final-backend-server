package com.github.jefesimpson.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class Blog {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String head;
    @DatabaseField
    private String text;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate createdDate;
    @DatabaseField
    private boolean vipAccess;

    public Blog() {
    }

    public Blog(int id, String head, String text, LocalDate createdDate, boolean vipAccess) {
        this.id = id;
        this.head = head;
        this.text = text;
        this.createdDate = createdDate;
        this.vipAccess = vipAccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return id == blog.id &&
                vipAccess == blog.vipAccess &&
                Objects.equals(head, blog.head) &&
                Objects.equals(text, blog.text) &&
                Objects.equals(createdDate, blog.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, head, text, createdDate, vipAccess);
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", head='" + head + '\'' +
                ", text='" + text + '\'' +
                ", createdDate=" + createdDate +
                ", vipAccess=" + vipAccess +
                '}';
    }

    public boolean isVipAccess() {
        return vipAccess;
    }

    public void setVipAccess(boolean vipAccess) {
        this.vipAccess = vipAccess;
    }

    public final static String COLUMN_ID = "id";
    public final static String COLUMN_HEAD = "head";
    public final static String COLUMN_TEXT = "text";
    public final static String COLUMN_CREATED_DATE = "createdDate";
    public final static String COLUMN_VIP_ACCESS = "vipAccess";

}
