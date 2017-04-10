package com.jinniu.commonjn.redis;

import com.jinniu.commonjn.redis.base.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VersionRedis {

    private static String VERSION_FIELD = "version";

    @Autowired
    private Redis redis;

    /**
     * 获取现在的版本号
     */
    public Long nowVersion(Integer roomId) {
        return (Long) redis.hash.get(
                String.format(RoomRedis.ROOM_KEY, roomId.toString()),
                VERSION_FIELD,
                Long.class);
    }

    /**
     * 获取下一个版本号
     */
    public Long nextVersion(Integer roomId) {
        return redis.hash.increment(
                String.format(RoomRedis.ROOM_KEY, roomId.toString()),
                VERSION_FIELD);
    }
}
