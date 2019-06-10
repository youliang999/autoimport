package com.youliang.autoimport.upload;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "AUTH_GROUP")
public class GroupInfoDTO extends BaseDO {
    /** 父级id */
    private Long   parentId;

    /** 租户ID， 用于扩展多租户的情况 */
    private Long   tid;

    /** 名称 */
    private String name;

    /** 保留字段 */
    private String no;

    /** 描述 */
    private String description;

    /** 扩展字段 */
    private String extData;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }
}
