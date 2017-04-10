package com.jinniu.commonjn.util;

import com.jinniu.commonjn.dao.RoomDao;
import com.jinniu.commonjn.model.Entity;
import com.jinniu.commonjn.model.Room;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.fanlychie.mybatis.TemplateGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.*;


public class CommonUtil {

    /***
     *
     * @Title: CommonUtil.java
     * @Package commons.lander.util
     * @Description: 创建6位数字的用户唯一标识
     * @author chenwenhao
     * @date 2016-12-1 上午9:01:01
     */
    public static Integer createUserCode() {
        int i = RandomUtils.nextInt(1000000);
        if (i < 100000) {
            i += 100000;
        }
        return i;
    }
    /***
     *
     * @Title: CommonUtil.java
     * @Package commons.lander.util
     * @Description: 创建4位数字的房间号码
     * @author chenwenhao
     * @date 2016-12-1 上午9:01:01
     */
    public static Integer createRoomCode() {

        int i = RandomUtils.nextInt(10000);
        if (i < 1000) {
            i += 1000;
        }
        return i;
    }

    /**
     * 获取一个?位数字的随机码
     */
    public static String createRandomNumeric(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    /***
     *
     * @Title: CommonUtil.java
     * @Package commons.lander.util
     * @Description: 把List转为Map
     * @author chenwenhao
     * @date 2016-12-10 上午9:48:54
     */
    public static Map<Serializable, Entity> transformToMap(List<? extends Entity> list) {
        Map<Serializable, Entity> map = null;
        if (list != null && !list.isEmpty()) {
            map = new HashMap<Serializable, Entity>();
            for (Entity ent : list) {
                //map.put(ent.getId(), ent);
            }
        }
        return map;
    }

    /**
     * @Title: CommonUtil.java
     * @Package commons.lander.util
     * @Description: 创建用户token值
     * token = userCode+当前时间戳+uuid随机码
     * @author chenwenhao
     * @date 2016-12-1 上午9:39:21
     */
    public static String createToken(Integer userCode) {
        StringBuilder bulider = new StringBuilder("");
        bulider.append(userCode);
        bulider.append(";");
        bulider.append(System.currentTimeMillis());
        bulider.append(";");
        bulider.append(UUID.randomUUID().toString());
        return RSAUtils.encrypt(bulider.toString(), true);
    }


    public static void main(String[] args) {
        try {
            TemplateGenerator.generate();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //TemplateGenerator
//		System.out.println(createRandomNumeric(6));
//		String s = UUID.randomUUID().toString();
//		System.out.println(s);
//		s = UUID.randomUUID().toString();
//		System.out.println(s);
//		
//		System.out.println(createToken(18436399));
    }

    /**
     * 生成订单号  当前年月日 + 随机9位数
     *
     * @return
     */
    public static String createOrderCode() {
        return DateFuncs.toString(new Date(), "yyyyMMdd") + createRandomNumeric(9);
    }

    /**
     * int转成字节数组
     */
    public static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[0] = (byte) (value & 0xff);  // 低位(右边)的8个bit位
        des[1] = (byte) ((value >> 8) & 0xff); //第二个8 bit位
        des[2] = (byte) ((value >> 16) & 0xff); //第三个 8 bit位
        /**
         * (byte)((value >> 24) & 0xFF);
         * value向右移动24位, 然后和0xFF也就是(11111111)进行与运算
         * 在内存中生成一个与 value 同类型的值
         * 然后把这个值强制转换成byte类型, 再赋值给一个byte类型的变量 des[3]
         */
        des[3] = (byte) ((value >> 24) & 0xff); //第4个 8 bit位
        return des;
    }

    /**
     * 通讯的数据的加前四字节表示长度
     *
     * @param jsonResult
     * @return
     */
    public static String jsonResultAddFour(JsonResult jsonResult) {
        String json = JsonHelper.toJson(jsonResult);
        byte[] last = json.getBytes();
        byte[] prefix = CommonUtil.intToBytes(json.length());
        byte[] des = new byte[prefix.length + last.length];
        System.arraycopy(prefix, 0, des, 0, 4);
        System.arraycopy(last, 0, des, 4, last.length);
        String returnMessage = new String(des);
        return returnMessage;
    }
}
