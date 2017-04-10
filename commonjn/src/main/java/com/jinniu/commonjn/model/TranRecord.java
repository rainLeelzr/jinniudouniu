package com.jinniu.commonjn.model;

public class TranRecord implements Entity {
	
	private static final long serialVersionUID = 1L;

	public static enum way {
		DRAW_BY_FREE(1, "免费抽奖"),
		DRAW_BY_COINS(2, "金币抽奖"),
		FREE_COIN(3, "每日免费领取金币"),
		BIND_PHONE(4, "绑定手机领取钻石"),
		TEN_WINS(5, "胜利十局领取金币"),
		WIN(6, "对局胜利获取金币");


		private int code;

		private String name;

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		way(int code, String name) {
			this.code = code;
			this.name = name;
		}
	}
	public static enum itemType {
		COIN(1, "金币"),
		DIAMOND(2, "钻石"),
		HORN(3, "喇叭");


		private int code;

		private String name;

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		itemType(int code, String name) {
			this.code = code;
			this.name = name;
		}
	}
	/**  */
	protected Integer id;
	
	/**  */
	protected Integer itemType;
	
	/**  */
	protected Integer quantity;
	
	/**  */
	protected java.util.Date tranTimes;
	
	/**  */
	protected Integer userId;
	
	/**  */
	protected Integer way;
	
 	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getItemType() {
		return itemType;
	}
	
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public java.util.Date getTranTimes() {
		return tranTimes;
	}
	
	public void setTranTimes(java.util.Date tranTimes) {
		this.tranTimes = tranTimes;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getWay() {
		return way;
	}
	
	public void setWay(Integer way) {
		this.way = way;
	}
	
 	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id = ").append(id).append(", ");
		builder.append("itemType = ").append(itemType).append(", ");
		builder.append("quantity = ").append(quantity).append(", ");
		builder.append("tranTimes = ").append(tranTimes).append(", ");
		builder.append("userId = ").append(userId).append(", ");
		builder.append("way = ").append(way);
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
		TranRecord other = (TranRecord) obj;
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