package com.jinniu.commonjn.model;

public class User implements Entity {

    private static final long serialVersionUID = 1L;

    /**  */
    protected Integer id;

    /**  */
    protected Integer coin;

    /**  */
    protected Integer diamond;

    /**  */
    protected Integer horn;

    /**  */
    protected String idNum;

    /**  */
    protected String image;

    /**  */
    protected String ip;

    /**  */
    protected java.util.Date lastLoginTime;

    /**  */
    protected String mobilePhone;

    /**  */
    protected String name;

    /**  */
    protected String nickName;

    /**  */
    protected String openId;

    /**  */
    protected Integer sex;

    /**  */
    protected Integer uId;

    public User(Integer id) {
        this.id = id;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getHorn() {
        return horn;
    }

    public void setHorn(Integer horn) {
        this.horn = horn;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public java.util.Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(java.util.Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getUId() {
        return uId;
    }

    public void setUId(Integer uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id = ").append(id).append(", ");
        builder.append("coin = ").append(coin).append(", ");
        builder.append("diamond = ").append(diamond).append(", ");
        builder.append("horn = ").append(horn).append(", ");
        builder.append("idNum = ").append(idNum).append(", ");
        builder.append("image = ").append(image).append(", ");
        builder.append("ip = ").append(ip).append(", ");
        builder.append("lastLoginTime = ").append(lastLoginTime).append(", ");
        builder.append("mobilePhone = ").append(mobilePhone).append(", ");
        builder.append("name = ").append(name).append(", ");
        builder.append("nickName = ").append(nickName).append(", ");
        builder.append("openId = ").append(openId).append(", ");
        builder.append("sex = ").append(sex).append(", ");
        builder.append("uId = ").append(uId);
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}