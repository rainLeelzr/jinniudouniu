package com.jinniu.commonjn.util;

import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class JsonUtil {
    private static JsonConfig config;

    static {
        config = new JsonConfig();
        config.registerJsonValueProcessor(java.util.Date.class,
                new JsonDateValueProcessor());
    }

    public static String toJson(List list) {
        JSONArray arr = JSONArray.fromObject(list, config);
        return arr.toString();
    }

    public static String toJson(Object obj) {
        JSONObject jsonObject = JSONObject.fromObject(obj, config);
        return jsonObject.toString();
    }

    public static <T> Object toBean(String json, Class<T> type) {
        return toBean(json, type, null);
    }

    public static <T> Object toBean(String json, Class<T> type, Map<String,
            Class> classMap) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        Object o = JSONObject.toBean(jsonObject, type, classMap);
        if (o instanceof RoomMember) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            RoomMember rm = (RoomMember) o;
            String joinTime = (String) jsonObject.get("joinTime");
            try {
                rm.setJoinTime(sf.parse(joinTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (o instanceof CanDoOperate) {
            CanDoOperate canDoOperate = (CanDoOperate) o;
            if (canDoOperate.getOperates() != null && canDoOperate.getOperates().size() != 0) {
                Iterator<Operate> iterator = canDoOperate.getOperates().iterator();
                boolean isString = false;
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (next instanceof String) {
                        isString = true;
                    }
                    break;
                }
                if (isString) {
                    Set<Operate> newOperates = new TreeSet<>();
                    Iterator it = canDoOperate.getOperates().iterator();
                    while (it.hasNext()) {
                        Object next = it.next();
                        newOperates.add(Operate.valueOf(next.toString()));
                    }
                    canDoOperate.setOperates(newOperates);
                }
            }
        } else if (o instanceof MahjongGameData) {
            MahjongGameData mahjongGameData = (MahjongGameData) o;
            List baoMahjongs = mahjongGameData.getBaoMahjongs();

            // 修正baoMahjongs成员变量
            if (baoMahjongs.size() > 0 && baoMahjongs.get(0) instanceof String) {
                List<Mahjong> baoMs = new ArrayList<>(baoMahjongs.size());
                for (int i = 0; i < baoMahjongs.size(); i++) {
                    baoMs.add(Mahjong.valueOf(baoMahjongs.get(i).toString()));
                }
                mahjongGameData.setBaoMahjongs(baoMs);
            }

            // 修正baoMahjongMakeUpMahjongs成员变量
            List baoMahjongMakeUpMahjongs = mahjongGameData.getBaoMahjongMakeUpMahjongs();
            if (baoMahjongMakeUpMahjongs.size() > 0 && baoMahjongMakeUpMahjongs.get(0) instanceof String) {
                List<Mahjong> realMahjong = new ArrayList<>(baoMahjongMakeUpMahjongs.size());
                for (int i = 0; i < baoMahjongMakeUpMahjongs.size(); i++) {
                    realMahjong.add(Mahjong.valueOf(baoMahjongMakeUpMahjongs.get(i).toString()));
                }
                mahjongGameData.setBaoMahjongMakeUpMahjongs(realMahjong);
            }
        }
        return o;
    }

    public static int getInt(JSONObject data, String key) {
        try {
            return data.getInt(key);
        } catch (JSONException e) {
            throw new RuntimeException(String.format("缺少参数：%s", key));
        }
    }

    public static List<Integer> getIntegerList(JSONObject data, String key) {
        try {
            JSONArray jsonArray = data.getJSONArray(key);
            List<Integer> ints = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                if (o instanceof Integer) {
                    ints.add((Integer) o);
                } else {
                    ints.add(Integer.valueOf((int) o));
                }
            }
            return ints;
        } catch (JSONException e) {
            throw new RuntimeException(String.format("缺少参数：%s", key));
        }
    }


    public static long getLong(JSONObject data, String key) {
        try {
            return data.getLong(key);
        } catch (JSONException e) {
            throw new RuntimeException(String.format("缺少参数：%s", key));
        }
    }

    //test
    public static void main(String[] args) {
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(java.util.Date.class,
                new JsonDateValueProcessor());
//		Admin s = new Admin();
//		  // long currentTime=System.currentTimeMillis(); 
//	   s.setCreationtime(String.valueOf(System.currentTimeMillis()));
//		JSONObject jsonObject = JSONObject.fromObject(s, config);
//		System.out.println(jsonObject.toString());
//		
//		
//		List list = new ArrayList();
//		list.add(s);
//		System.out.println(JsonUtil.toJson(s));
//		System.out.println(JsonUtil.toJson(list));
//		
//		Map m = new HashMap();
//		m.put("obj", s);
//		m.put("list", list);
//		System.out.println(JsonUtil.toJson(m));
    }

    /**
     * 将json转化为实体POJO
     *
     * @param jsonStr
     * @param obj
     * @return
     */
    public static <T> Object JSONToObj(String jsonStr, Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
