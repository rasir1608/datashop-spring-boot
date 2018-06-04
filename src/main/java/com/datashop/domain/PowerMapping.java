package com.datashop.domain;

public class PowerMapping {
    private Integer id;

    private DUser user;

    private DProject project;

    private Integer power;

    private Long createTime;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DUser getUser() {
        return user;
    }

    public void setUser(DUser user) {
        this.user = user;
    }

    public DProject getProject() {
        return project;
    }

    public void setProject(DProject project) {
        this.project = project;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PowerMapping{" +
                "id=" + id +
                ", user=" + user +
                ", project=" + project +
                ", power=" + power +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
