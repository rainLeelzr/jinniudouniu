package com.jinniu.commonjn.model;

public class Room implements Entity {

    public static final int playerLimit = 4;

    private static final long serialVersionUID = 1L;

    public static enum payType {
        PAY_BY_ONE(1, "一人支付"),
        PAY_BY_FOUR(2, "四人支付");


        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        payType(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    public static enum type {
        COINS_ROOM(1, "金币场"),
        FRIENDS_ROOM(2, "好友场");


        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        type(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    public static enum multiple {
        COINS_WITH_2000(2000, "2000金币场"),
        COINS_WITH_20000(20000, "20000金币场"),
        FRIENDS_WITH_2(2, "底分为2"),
        FRIENDS_WITH_5(5, "底分为5"),
        FRIENDS_WITH_10(10, "底分为10");


        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        multiple(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }
    public static enum start {
        UNSTART(1, "未开始游戏"),
        STARTED(2, "已开始游戏");

        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        start(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**  */
    protected Integer id;

    /**  */
    protected java.util.Date createdTime;

    /**  */
    protected Integer createdUserId;

    /**  */
    protected Integer diamond;

    /**  */
    protected java.util.Date lastLoginTime;

    /**  */
    protected Integer multiple;

    /**  */
    protected Integer payType;

    /**  */
    protected Integer roomCode;

    /**  */
    protected Integer start;
    /**  */
    protected Integer state;

    /**  */
    protected String times;

    /**  */
    protected Integer players;

    /**  */
    protected Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.util.Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.util.Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public java.util.Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(java.util.Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(Integer roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id = ").append(id).append(", ");
        builder.append("createdTime = ").append(createdTime).append(", ");
        builder.append("createdUserId = ").append(createdUserId).append(", ");
        builder.append("diamond = ").append(diamond).append(", ");
        builder.append("lastLoginTime = ").append(lastLoginTime).append(", ");
        builder.append("multiple = ").append(multiple).append(", ");
        builder.append("payType = ").append(payType).append(", ");
        builder.append("players = ").append(players).append(", ");
        builder.append("roomCode = ").append(roomCode).append(", ");
        builder.append("state = ").append(state).append(", ");
        builder.append("times = ").append(times).append(", ");
        builder.append("type = ").append(type);
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
        Room other = (Room) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * 房间状态
     */
    public static enum state {
        wait(1, "待开始"),
        PLAYING(2, "游戏中"),
        DISMISS(3, "已解散");

        private int code;

        private String name;

        state(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}