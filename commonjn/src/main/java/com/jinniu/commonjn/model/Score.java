package com.jinniu.commonjn.model;

public class Score implements Entity {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	protected Integer id;
	
	/**  */
	protected Integer anGangTimes;
	
	/**  */
	protected Integer coin;
	
	/**  */
	protected Integer dianPaoTimes;
	
	/**  */
	protected Integer isZiMo;
	
	/**  */
	protected Integer jiePaoTimes;
	
	/**  */
	protected Integer mingGangTimes;
	
	/**  */
	protected Integer roomId;
	
	/**  */
	protected Integer score;
	
	/**  */
	protected Integer times;
	
	/**  */
	protected Integer type;
	
	/**  */
	protected Integer userId;
	
	/**  */
	protected Integer winType;

    protected java.util.Date createdTime;

    public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getAnGangTimes() {
		return anGangTimes;
	}
	
	public void setAnGangTimes(Integer anGangTimes) {
		this.anGangTimes = anGangTimes;
	}
	
	public Integer getCoin() {
		return coin;
	}
	
	public void setCoin(Integer coin) {
		this.coin = coin;
	}
	
	public Integer getDianPaoTimes() {
		return dianPaoTimes;
	}
	
	public void setDianPaoTimes(Integer dianPaoTimes) {
		this.dianPaoTimes = dianPaoTimes;
	}
	
	public Integer getIsZiMo() {
		return isZiMo;
	}
	
	public void setIsZiMo(Integer isZiMo) {
		this.isZiMo = isZiMo;
	}
	
	public Integer getJiePaoTimes() {
		return jiePaoTimes;
	}
	
	public void setJiePaoTimes(Integer jiePaoTimes) {
		this.jiePaoTimes = jiePaoTimes;
	}
	
	public Integer getMingGangTimes() {
		return mingGangTimes;
	}
	
	public void setMingGangTimes(Integer mingGangTimes) {
		this.mingGangTimes = mingGangTimes;
	}
	
	public Integer getRoomId() {
		return roomId;
	}
	
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getTimes() {
		return times;
	}
	
	public void setTimes(Integer times) {
		this.times = times;
	}
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getWinType() {
		return winType;
	}
	
	public void setWinType(Integer winType) {
		this.winType = winType;
	}

    public java.util.Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.util.Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id = ").append(id).append(", ");
		builder.append("anGangTimes = ").append(anGangTimes).append(", ");
		builder.append("coin = ").append(coin).append(", ");
		builder.append("dianPaoTimes = ").append(dianPaoTimes).append(", ");
		builder.append("isZiMo = ").append(isZiMo).append(", ");
		builder.append("jiePaoTimes = ").append(jiePaoTimes).append(", ");
		builder.append("mingGangTimes = ").append(mingGangTimes).append(", ");
		builder.append("roomId = ").append(roomId).append(", ");
		builder.append("score = ").append(score).append(", ");
		builder.append("times = ").append(times).append(", ");
		builder.append("type = ").append(type).append(", ");
		builder.append("userId = ").append(userId).append(", ");
		builder.append("winType = ").append(winType);
        builder.append("createdTime = ").append(createdTime).append(", ");
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
		Score other = (Score) obj;
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