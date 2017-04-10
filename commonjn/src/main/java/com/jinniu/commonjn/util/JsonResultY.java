package com.jinniu.commonjn.util;

public class JsonResultY {
    // 内容
    private Object data;

    // 错误码
    private int code;

    // 接口协议号
    private int pid;

    public Object getData() {
        return null == data ? new Object() : data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return JsonUtil.toJson(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public static class Builder {
        private JsonResultY jsonResultY = new JsonResultY();

        public Builder setPid(PidValue pid) {
            jsonResultY.setPid(pid.getPid());
            return this;
        }

        public Builder setPid(int pid) {
            jsonResultY.setPid(pid);
            return this;
        }

        public Builder setError(CommonError error) {
            jsonResultY.setCode(error.getCode());
            jsonResultY.setData(error.getMsg());
            return this;
        }

        public Builder setCode(int code) {
            jsonResultY.setCode(code);
            return this;
        }

        public Builder setData(Object data) {
            jsonResultY.setData(data);
            return this;
        }

        public JsonResultY build() {
            return jsonResultY;
        }
    }
}
