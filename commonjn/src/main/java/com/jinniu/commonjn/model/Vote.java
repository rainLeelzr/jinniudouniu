package com.jinniu.commonjn.model;

public class Vote implements Entity {

    private static final long serialVersionUID = 1L;

    public static enum type {
        DISMISS_VOTE(1, "解散房间投票"),
        CONTINUE_VOTE(2, "在此房间继续游戏投票");

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

    public static enum state {
        UN_VOTE(1, "未投票"),
        AGREE(2, "同意"),
        DISAGREE(3, "反对");


        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        state(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }
    public static enum status {
        PROCESSING(1,"进行中"),
        FINISH(2,"已完成");


        private int code;

        private String name;

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        status(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**  */
    protected Integer id;

    /**
     * 发起人id
     */
    protected Integer organizerUserId;

    /**  */
    protected Integer roomId;

    /**
     * 投票情况
     */
    protected Integer state;

    /**
     * 投票状态
     */
    protected Integer status;

    /**  */
    protected Integer type;

    /**
     * 投票人id
     */
    protected Integer voterUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrganizerUserId() {
        return organizerUserId;
    }

    public void setOrganizerUserId(Integer organizerUserId) {
        this.organizerUserId = organizerUserId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVoterUserId() {
        return voterUserId;
    }

    public void setVoterUserId(Integer voterUserId) {
        this.voterUserId = voterUserId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id = ").append(id).append(", ");
        builder.append("organizerUserId = ").append(organizerUserId).append(", ");
        builder.append("roomId = ").append(roomId).append(", ");
        builder.append("state = ").append(state).append(", ");
        builder.append("status = ").append(status).append(", ");
        builder.append("type = ").append(type).append(", ");
        builder.append("voterUserId = ").append(voterUserId);
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
        Vote other = (Vote) obj;
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