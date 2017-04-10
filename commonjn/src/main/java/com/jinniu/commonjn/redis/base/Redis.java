package com.jinniu.commonjn.redis.base;

import com.jinniu.commonjn.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 封装Redis的5种数据类型String、Hash、List、Set、SortedSet的常用操作
 * 所有redis的操作都统一调用这个类的方法
 */
@Component
public class Redis {

    public KeyValue keyValue = new KeyValue();
    public Hash hash = new Hash();
    public List list = new List();
    public Set set = new Set();
    public SortedSet sortedSet = new SortedSet();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 反序列化，将obj转为type类型的对象
     * obj可能是基本数据类型、普通字符串、json
     */
    private static Object deserialize(Object obj, Class type) {
        return deserialize(obj, type, null);
    }

    /**
     * 反序列化，将obj转为type类型的对象
     * obj可能是基本数据类型、普通字符串、json
     */
    private static Object deserialize(Object obj, Class type, Map<String,
            Class> classMap) {
        if (obj == null) {
            return null;
        }

        if (type == String.class) {
            return obj;
        }

        String tempStr = (String) obj;
        if (type == Short.class || type == short.class) {
            return Short.parseShort(tempStr);
        }

        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(tempStr);
        }

        if (type == Long.class || type == long.class) {
            return Long.parseLong(tempStr);
        }

        if (type == Float.class || type == float.class) {
            return Float.parseFloat(tempStr);
        }

        if (type == Double.class || type == double.class) {
            return Double.parseDouble(tempStr);
        }

        if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(tempStr);
        }

        if (type == BigDecimal.class) {
            return new BigDecimal(tempStr);
        }

        return JsonUtil.toBean(tempStr, type, classMap);
    }

    /**
     * 序列化value
     */
    private static String serialize(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return (String) value;
        }

        if (value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long
                || value instanceof Float
                || value instanceof Double
                || value instanceof Boolean
                || value instanceof BigDecimal
                ) {
            return value.toString();
        }

        return JsonUtil.toJson(value);
    }

    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    public void del(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /**
     * 为给定 key 设置生存时间(秒)，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * 设置成功返回 true 。
     * 当 key 不存在或者不能为 key 设置生存时间时，返回 false 。
     */
    public Boolean expire(String key, long timeout) {
        return stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置key生存时间。key在date这个时间点过期（自动被删除）
     */
    public Boolean expireAt(String key, Date date) {
        return stringRedisTemplate.expireAt(key, date);
    }

    public void flushAll() {
        stringRedisTemplate.execute(new RedisCallback<Boolean>() {


            public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                conn.flushAll();
                return true;
            }
        });
    }

    public void flushDb() {
        stringRedisTemplate.execute(new RedisCallback<Boolean>() {


            public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                conn.flushDb();
                return true;
            }
        });
    }

    /**
     * Redis的String数据类型
     */
    public class KeyValue {

        /**
         * 设置value
         */
        public void set(String key, Object value) {
            String str = serialize(value);
            stringRedisTemplate.boundValueOps(key).set(str);
        }

        /**
         * 将字符串值 value 关联到 key 。如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
         * 有效期为timeout秒（即timeout秒后被自动删除）
         */
        public void set(String key, String value, long timeout) {
            String str = serialize(value);
            stringRedisTemplate.boundValueOps(key).set(str, timeout, TimeUnit.SECONDS);
        }

        /**
         * 获取value
         */
        public Object get(String key, Class type) {
            return get(key, type, null);
        }

        public Object get(String key, Class type, Map<String, Class> classMap) {
            Object obj = stringRedisTemplate.opsForValue().get(key);
            return deserialize(obj, type, classMap);
        }

        /**
         * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
         * 返回给定 key 的旧值。
         * 当 key 没有旧值时，也即是， key 不存在时，返回 null 。
         */
        public Object getAndSet(String key, String value, Class type) {
            Object obj = stringRedisTemplate.boundValueOps(key).getAndSet(value);
            return deserialize(obj, type);
        }

        /**
         * 删除key
         */
        public void delete(String key) {
            del(key);
        }

        /**
         * value加1.如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 increment 命令。
         */
        public Long increment(String key) {
            return stringRedisTemplate.boundValueOps(key).increment(1);
        }

        /**
         * value加n.如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 increment 命令。
         */
        public Long increment(String key, long step) {
            return stringRedisTemplate.boundValueOps(key).increment(step);
        }

    }

    /**
     * Redis的Hash数据类型
     */
    public class Hash {

        public Object get(String key, String field, Class type) {
            return get(key, field, type, null);
        }

        public Object get(String key, String field, Class type,
                          Map<String, Class> classMap) {
            Object obj = stringRedisTemplate.boundHashOps(key).get(field);
            return deserialize(obj, type, classMap);
        }

        public void put(String key, String field, Object value) {
            stringRedisTemplate.boundHashOps(key).put(field, serialize(value));
        }

        /**
         * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
         * 此命令会覆盖哈希表中已存在的域。
         * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
         */
        public void put(String key, Map<String, Object> values) {
            Map<String, String> temps = new HashMap<String, String>((int) Math.
                    ceil(values.size() / 0.75)
            );
            for (String s : values.keySet()) {
                temps.put(s, serialize(values.get(s)));
            }
            stringRedisTemplate.boundHashOps(key).putAll(temps);
        }

        /**
         * 删除key
         */
        public void delete(String key) {
            del(key);
        }

        /**
         * 删除map里的field
         */
        public void delete(String key, String field) {
            stringRedisTemplate.boundHashOps(key).delete(field);
        }

        /**
         * @Description: 查看哈希表 key 中，给定域 field 是否存在。
         * 如果哈希表含有给定域，返回 true 。
         * 如果哈希表不含有给定域，或 key 不存在，返回 false。
         */
        public Boolean hasField(String key, String field) {
            return stringRedisTemplate.boundHashOps(key).hasKey(field);
        }

        /**
         * 为哈希表 key 中的域 field 的值加上增量 1 。
         * 如果 key 不存在，一个新的哈希表被创建并执行 increment 命令。
         * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
         */
        public Long increment(String key, String field) {
            return stringRedisTemplate.boundHashOps(key).increment(field, 1);
        }

        /**
         * 为哈希表 key 中的域 field 的值加上增量 step 。
         * 增量也可以为负数，相当于对给定域进行减法操作。
         * 如果 key 不存在，一个新的哈希表被创建并执行 increment 命令。
         * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
         */
        public Long increment(String key, String field, long step) {
            return stringRedisTemplate.boundHashOps(key).increment(field, step);
        }
    }

    /**
     * Redis的List数据类型
     */
    public class List {

        /**
         * 获取列表的元素个数，若列表不存在，返回0
         */
        public Long size(String key) {
            return stringRedisTemplate.boundListOps(key).size();
        }

        public java.util.List<Object> get(String key, Class type) {
            if (!hasKey(key)) {
                return null;
            }

            Long size = size(key);
            if (size == 0) {
                return Collections.emptyList();
            }

            java.util.List<String> objs = stringRedisTemplate.boundListOps(key).range(0, size - 1);
            if (objs == null) {
                return null;
            }

            java.util.List<Object> results = new ArrayList<Object>(objs.size());
            for (String obj : objs) {
                results.add(deserialize(obj, type));
            }
            return results;
        }

        /**
         * 在列表中添加一个元素，若列表不存在，会自动创建。
         * 返回新添加的元素所有列表的第几行，从1开始
         */
        public Long add(String key, Object value) {
            String str = serialize(value);
            return stringRedisTemplate.opsForList().rightPush(key, str);
        }

        /**
         * 删除key
         */
        public void delete(String key) {
            del(key);
        }

    }

    /**
     * Redis的Set数据类型
     */
    public class Set {

        /**
         * 获取列表的元素个数，若列表不存在，返回0
         */
        public Long size(String key) {
            return stringRedisTemplate.boundSetOps(key).size();
        }

        public java.util.Set<Object> get(String key, Class type) {
            if (!hasKey(key)) {
                return null;
            }

            Long size = size(key);
            if (size == 0) {
                return Collections.emptySet();
            }

            java.util.Set<String> objs = stringRedisTemplate.boundSetOps(key)
                    .members();
            if (objs == null) {
                return null;
            }

            java.util.Set<Object> results = new HashSet<Object>(objs.size());
            for (String obj : objs) {
                results.add(deserialize(obj, type));
            }
            return results;
        }

        /**
         * 在列表中添加一个元素，若列表不存在，会自动创建。
         */
        public Boolean add(String key, Object value) {
            String str = serialize(value);
            return stringRedisTemplate.opsForSet().add(key, str);
        }

        /**
         * 删除key
         */
        public void delete(String key) {
            del(key);
        }


        /**
         * 移除集合 key 中的一个 value 元素，不存在的 value 元素会被忽略。
         * 移除成功返回true，失败返回false
         */
        public Boolean delete(String key, Object value) {
            String str = serialize(value);
            return stringRedisTemplate.boundSetOps(key).remove(str);
        }

        /**
         * 判断 value 元素是否集合 key 的成员。
         * 如果 value 元素是集合的成员，返回 true 。
         * 如果 value 元素不是集合的成员，或 key 不存在，返回 false 。
         */
        public Boolean isMember(String key, Object value) {
            String str = serialize(value);
            return stringRedisTemplate.boundSetOps(key).isMember(str);
        }

    }

    /**
     * Redis的SortedSet数据类型
     */
    public class SortedSet {

        /**
         * 获取列表的元素个数，若列表不存在，返回0
         */
        public Long size(String key) {
            return stringRedisTemplate.boundZSetOps(key).size();
        }

        /**
         * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
         */
        public Long count(String key, double min, double max) {
            return stringRedisTemplate.boundZSetOps(key).count(min, max);
        }

        public java.util.Set<Object> get(String key, Class type) {
            Long size = size(key);
            if (size == 0) {
                return Collections.emptySet();
            }

            java.util.Set<String> objs = stringRedisTemplate.boundZSetOps
                    (key).range(0, size - 1);

            //考虑到其他线程可能此时删除了key
            if (objs == null || objs.size() == 0) {
                return Collections.emptySet();
            }

            java.util.Set<Object> results = new HashSet<Object>(objs.size());
            for (String obj : objs) {
                results.add(deserialize(obj, type));
            }
            return results;
        }

        /**
         * 获取对应下标区间内的成员（member）组合
         * 返回有序集 key 中，指定区间内的成员。
         * 其中成员的位置按 score 值递减(从大到小)来排列。
         * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
         */
        public java.util.Set<Object> getReverse(String key, Class type) {
            Long size = size(key);
            if (size == 0) {
                return Collections.emptySet();
            }

            java.util.Set<String> objs = stringRedisTemplate.boundZSetOps
                    (key).reverseRange(0, size - 1);

            //考虑到其他线程可能此时删除了key
            if (objs == null || objs.size() == 0) {
                return Collections.emptySet();
            }

            java.util.Set<Object> results = new HashSet<Object>(objs.size());
            for (String obj : objs) {
                results.add(deserialize(obj, type));
            }
            return results;
        }

        /**
         * 注意点：返回的是成员。即member的集合值
         * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
         * 有序集成员按 score 值递增(从小到大)次序排列。
         */
        public java.util.Set<Object> get(String key, double min, double max,
                                         Class type) {
            java.util.Set<String> strs = stringRedisTemplate.boundZSetOps(key)
                    .rangeByScore(min, max);
            if (strs == null || strs.size() == 0) {
                return Collections.emptySet();
            }

            java.util.Set<Object> results = new HashSet<Object>(strs.size());
            for (String obj : strs) {
                results.add(deserialize(obj, type));
            }
            return results;
        }

        /**
         * 获取score最小的元素
         *
         * @param key
         * @param type
         * @return
         */
        public Object getByMinScore(String key, Class type) {
            Long size = size(key);
            if (size == 0) {
                return null;
            }

            java.util.Set<ZSetOperations.TypedTuple<String>> objs = stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, 0);

            //考虑到其他线程可能此时删除了key
            if (objs == null || objs.size() == 0) {
                return Collections.emptySet();
            }

            Object result = null;
            for (ZSetOperations.TypedTuple<String> obj : objs) {
                String str = obj.getValue();
                result = deserialize(str, type);
                break;
            }

            return result;
        }

        /**
         * 在列表中添加一个元素，若列表不存在，会自动创建。
         * 如果某个 value 已经是有序集的成员，那么更新这个 value 的 score 值，
         */
        public Boolean add(String key, Object value, double score) {
            String str = serialize(value);
            return stringRedisTemplate.opsForZSet().add(key, str, score);
        }

        /**
         * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
         * 可以通过传递一个负数值 increment ，让 score 减去相应的值，
         * 比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
         * 当 key 不存在，或 member 不是 key 的成员时，
         * ZINCRBY key increment member 等同于 add key increment member 。
         * score 值可以是整数值或双精度浮点数。
         * 返回值：value 成员的新 score 值
         */
        public Double increment(String key, Object value, double score) {
            String str = serialize(value);
            return stringRedisTemplate.boundZSetOps(key).incrementScore(str, score);
        }

        /**
         * 返回有序集 key 中，成员 value 的 score 值。
         * 如果 value 元素不是有序集 key 的成员，或 key 不存在，返回 null 。
         */
        public Double score(String key, Object value) {
            String str = serialize(value);
            return stringRedisTemplate.boundZSetOps(key).score(str);
        }

        /**
         * 删除key
         */
        public void delete(String key) {
            del(key);
        }

        /**
         * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略
         * 成功返回true,失败返回false
         */
        public Boolean delete(String key, Object value) {
            String str = serialize(value);
            return stringRedisTemplate.boundZSetOps(key).remove(str);
        }

        /**
         * 移除有序集 key 中的score范围在score1~score2的记录，不存在的成员将被忽略
         */
        public void deleteByScore(String key, double score1, double score2) {
            stringRedisTemplate.boundZSetOps(key).removeRangeByScore(score1, score2);
        }

        /**
         * 移除有序集 key 中中的range(row)范围在range1~range2的记录，不存在的成员将被忽略
         */
        public void deleteByRange(String key, long range1, long range2) {
            stringRedisTemplate.boundZSetOps(key).removeRange(range1, range2);
        }

    }
}
