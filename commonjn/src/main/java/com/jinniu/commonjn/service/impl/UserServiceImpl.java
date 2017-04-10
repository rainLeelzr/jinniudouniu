package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.RoomMemberDao;
import com.jinniu.commonjn.dao.ScoreDao;
import com.jinniu.commonjn.dao.TranRecordDao;
import com.jinniu.commonjn.dao.UserDao;
import com.jinniu.commonjn.model.*;
import com.jinniu.commonjn.service.RoomService;
import com.jinniu.commonjn.service.UserService;
import com.jinniu.commonjn.util.CommonError;
import com.jinniu.commonjn.util.CommonUtil;
import com.jinniu.commonjn.util.vo.ScoreVo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.*;

@Service
public class UserServiceImpl extends BaseServiceImpl<Integer, User> implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomMemberDao roomMemberDao;
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private TranRecordDao tranRecordDao;

    @Autowired
    private RoomService roomService;

    /**
     * 用户登录
     *
     * @param data
     * @param ip
     * @return
     * @throws Exception
     */
    public Map<String, Object> login(JSONObject data, String ip) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>(2);
        Integer loginType;//1正常登陆,2游戏中断线重连,3结算后未显示结算页面

        String openId = (String) data.get("openId");
        String nickName = (String) data.get("nickName");
        String image = (String) data.get("image");
        Integer sex = data.getInt("sex");

        Entity.UserCriteria userCriteria = new Entity.UserCriteria();
        userCriteria.setOpenId(Entity.Value.eq(openId));
        User user = userDao.selectOne(userCriteria);


        if (user == null) {//新用户登录
            user = new User();
            user.setImage(image);
            user.setIp(ip);
            user.setLastLoginTime(new Date());
            user.setNickName(nickName);
            user.setSex(sex);
            user.setOpenId(openId);
            user.setCoin(30000);
            // DEBUGING 玩家钻石
            user.setDiamond(1000);
            user.setHorn(0);
            Integer uId = CommonUtil.createUserCode();
            uId = checkUId(uId);
            user.setUId(uId);
            userDao.save(user);
            loginType = 1;
        } else {
            if (!image.equals(user.getImage())) {//头像发生变化
                user.setImage(image);
            } else if (!nickName.equals(user.getNickName())) {//昵称发生变化
                user.setNickName(nickName);
            }
            user.setLastLoginTime(new Date());
            userDao.update(user);

            RoomMember roomMember = new RoomMember();
            roomMember.setUserId(user.getId());
            roomMember = roomMemberDao.selectByUserIdForCheck(roomMember);
            if (roomMember != null) {
                loginType = 2;
                Room room = roomService.selectOne(roomMember.getRoomId());
                result.put("room", room);
            } else {
                loginType = 1;
            }
        }
        result.put("user", user);
        result.put("login_type", loginType);
        return result;
    }

    /**
     * 防止uId重复
     *
     * @param uId
     * @return
     */
    private Integer checkUId(Integer uId) {
        Entity.UserCriteria uc = new Entity.UserCriteria();
        uc.setUId(Entity.Value.eq(uId));
        long count = userDao.selectCount(uc);
        if (count > 0) {
            uId = CommonUtil.createUserCode();
            uId = checkUId(uId);
        }
        return uId;
    }

    public TextMessage TestConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 登出
     *
     * @param data
     * @return
     */
    @Override
    public User logout(JSONObject data) {
        String uId = (String) data.get("uId");
        Entity.UserCriteria userCriteria = new Entity.UserCriteria();
        userCriteria.setUId(Entity.Value.eq(uId));
        User user = userDao.selectOne(userCriteria);
        if (user != null) {
            return user;
        } else {
            throw CommonError.USER_NOT_EXIST.newException();
        }

    }

    /**
     * 获取用户信息
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> getUser(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        if (user != null) {
            result.put("user", user);
            Entity.ScoreCriteria scoreCriteria = new Entity.ScoreCriteria();
            scoreCriteria.setUserId(Entity.Value.eq(user.getId()));
            long count = scoreDao.selectCount(scoreCriteria);//总局数
            scoreCriteria.setWinType(Entity.Value.ne(0));
            long win_count = scoreDao.selectCount(scoreCriteria);//胜利局数
            result.put("win", win_count);
            result.put("lose", count - win_count);
            return result;

        } else {
            throw CommonError.USER_NOT_EXIST.newException();
        }

    }

    /**
     * 抽奖
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> prizeDraw(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        Integer way = TranRecord.way.DRAW_BY_FREE.getCode();
        boolean judge = data.getBoolean("judge");

        TranRecord tr = new TranRecord();
        tr.setWay(way);
        tr.setUserId(user.getId());
        Long count = tranRecordDao.countForPrizeDraw(tr);

        if (judge) {
            if (count < 1) {
                result.put("free", true);
            } else {
                result.put("free", false);
            }
        } else {
            if (count < 1) {//说明当日还没有进行免费抽奖
                result = PrizeRandom(user, result, way);
            } else {
                way = TranRecord.way.DRAW_BY_COINS.getCode();
                tr.setWay(way);
                count = tranRecordDao.countForPrizeDraw(tr);
                if (count < 3) {//说明当日还可以用金币进行抽奖
                    if (user.getCoin() >= 10000) {
                        user.setCoin(user.getCoin() - 10000);
                        result = PrizeRandom(user, result, way);

                    } else {
                        throw CommonError.USER_LACK_COINS.newException();
                    }
                } else {
                    throw CommonError.ALREADY_DRAW_COINS.newException();
                }
            }
            result.put("way", way);
        }

        return result;


    }


    /**
     * 抽奖实现
     *
     * @param user
     * @param result
     * @param way
     * @return
     */
    private Map<String, Object> PrizeRandom(User user, Map<String, Object> result, Integer way) {
        int random = -1;
        List<Prize> prizes = packPrize();

        try {
            //计算总权重
            double sumWeight = 0;
            for (Prize p : prizes) {
                sumWeight += p.getPrize_weight();
            }

            //产生随机数
            double randomNumber;
            randomNumber = Math.random();

            //根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for (int i = 0; i < prizes.size(); i++) {
                d2 += prizes.get(i).getPrize_weight() / sumWeight;
                if (i == 0) {
                    d1 = 0;
                } else {
                    d1 += prizes.get(i - 1).getPrize_weight() / sumWeight;
                }
                if (randomNumber >= d1 && randomNumber <= d2) {
                    random = i;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("生成抽奖随机数出错，出错原因：" + e.getMessage());
        }

        //抽完奖创建一条交易记录
        String prizeName = prizes.get(random).getPrize_name();
        switch (prizeName) {
            case "100钻石":
                user.setDiamond(user.getDiamond() + 100);
                createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 100);

                break;
            case "50钻石":
                user.setDiamond(user.getDiamond() + 50);
                createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 50);

                break;
            case "10钻石":
                user.setDiamond(user.getDiamond() + 10);
                createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 10);

                break;
            case "5钻石":
                user.setDiamond(user.getDiamond() + 5);
                createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 5);

                break;
            case "50000金币":
                user.setCoin(user.getCoin() + 50000);
                createRecord(user, way, TranRecord.itemType.COIN.getCode(), 50000);

                break;
            case "2钻石":
                user.setDiamond(user.getDiamond() + 2);
                createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 2);
                break;
            case "2000金币":
                user.setCoin(user.getCoin() + 2000);
                createRecord(user, way, TranRecord.itemType.COIN.getCode(), 2000);

                break;
        }
        userDao.update(user);

        result.put("user", user);
        result.put("prize", prizeName);
        return result;
    }

    /**
     * 封装奖品
     *
     * @return
     */
    private List<Prize> packPrize() {
        List<Prize> prizes = new ArrayList<Prize>();
        Prize p1 = new Prize("100钻石", 0.01);
        Prize p2 = new Prize("50钻石", 0.1);
        Prize p3 = new Prize("10钻石", 1.0);
        Prize p4 = new Prize("5钻石", 5.0);
        Prize p5 = new Prize("50000金币", 10.0);
        Prize p6 = new Prize("2钻石", 10.0);
        Prize p7 = new Prize("2000金币", 73.89);
        prizes.add(p1);
        prizes.add(p2);
        prizes.add(p3);
        prizes.add(p4);
        prizes.add(p5);
        prizes.add(p6);
        prizes.add(p7);
        return prizes;
    }

    /**
     * 创建一条交易记录
     *
     * @return
     */
    private void createRecord(User user, Integer way, Integer itemType, Integer quantity) {
        TranRecord tranRecord = new TranRecord();
        tranRecord.setUserId(user.getId());
        tranRecord.setWay(way);
        tranRecord.setTranTimes(new Date());
        tranRecord.setQuantity(quantity);
        tranRecord.setItemType(itemType);
        tranRecordDao.save(tranRecord);

    }

    /**
     * 免费领取金币
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> freeCoins(User user) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        Integer way = TranRecord.way.FREE_COIN.getCode();

        TranRecord tr = new TranRecord();
        tr.setWay(way);
        tr.setUserId(user.getId());
        Long count = tranRecordDao.countForPrizeDraw(tr);
        //每两小时领取一次,需要计时

        if (count < 5) {//满足领取金币的资格
            if (count < 4) {
                user.setCoin(user.getCoin() + 2000);

            } else {//当天第5次
                user.setCoin(user.getCoin() + 2000);
                user.setDiamond(user.getDiamond() + 1);
                createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 1);

            }
            userDao.update(user);
            //交易记录
            createRecord(user, way, TranRecord.itemType.COIN.getCode(), 2000);
            result.put("user", user);
            result.put("count", count + 1);
            return result;
        } else {//不满足领取金币的资格
            throw CommonError.ALREADY_GET_COINS.newException();
        }
    }

    /**
     * 购买金币/钻石/道具
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> buy(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<String, Object>(2);

        Integer coin = (Integer) data.get("coin");
        Integer diamond = (Integer) data.get("diamond");
        Integer horn = (Integer) data.get("horn");

        //支付接口对接
        if (coin != null) {
            user.setCoin(user.getCoin() + coin);
        } else if (diamond != null) {
            user.setDiamond(user.getDiamond() + diamond);
        } else if (horn != null) {
            user.setHorn(user.getHorn() + horn);
        }
        userDao.update(user);
        result.put("user", user);
        return result;
    }

    /**
     * 绑定手机
     *
     * @param data
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> bindPhone(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        String phone = (String) data.get("phone");
        Integer check = (Integer) data.get("check");

        Integer way = TranRecord.way.BIND_PHONE.getCode();
        Entity.TranRecordCriteria tranRecordCriteria = new Entity.TranRecordCriteria();
        tranRecordCriteria.setUserId(Entity.Value.eq(user.getId()));
        tranRecordCriteria.setWay(Entity.Value.eq(way));
        long count = tranRecordDao.selectCount(tranRecordCriteria);

        if (check != null) {//检查用户是否已经领取绑定手机钻石
            if (count < 1) {
                result.put("receive", false);
            } else {
                result.put("receive", true);
            }
        } else {
            if (phone != null) {//绑定手机
                if (user.getMobilePhone() == null) {//还没有绑定过手机
                    user.setMobilePhone(phone);
                } else {
                    throw CommonError.ALREADY_BIND_PHONE.newException();
                }
            } else {//领取钻石
                if (user.getMobilePhone() != null) {//已经绑定手机

                    if (count < 1) {//没有领取过绑定手机钻石
                        user.setDiamond(user.getDiamond() + 2);
                        //生成交易记录
                        createRecord(user, way, TranRecord.itemType.DIAMOND.getCode(), 2);
                    } else {
                        throw CommonError.ALREADY_GET_DIAMOND.newException();
                    }
                } else {
                    throw CommonError.UN_BIND_PHONE.newException();
                }
            }
            userDao.update(user);
            result.put("user", user);
        }
        return result;

    }

    /**
     * 每日任务胜利十局可以领取2000金币
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> tenWins(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        String query = (String) data.get("query");

        TranRecord tr = new TranRecord();
        tr.setWay(TranRecord.way.WIN.getCode());
        tr.setUserId(user.getId());
        Long count = tranRecordDao.countForPrizeDraw(tr);

        if (query != null) {//查询当前胜利局数
            result.put("winNum", count);
        } else {
            if (count >= 10) {//当天胜利局数大于十局

                tr.setWay(TranRecord.way.TEN_WINS.getCode());
                count = tranRecordDao.countForPrizeDraw(tr);

                if (count < 0) {//当天还没有领取胜利任务金币
                    user.setCoin(user.getCoin() + 2000);
                    userDao.update(user);
                    createRecord(user, TranRecord.way.TEN_WINS.getCode(), TranRecord.itemType.COIN.getCode(), 2000);
                    result.put("user", user);
                } else {
                    throw CommonError.ALREADY_GET_COINS.newException();
                }
            } else {
                throw CommonError.NOT_ENOUGH_GAMES.newException();
            }
        }
        return result;
    }

    /**
     * 获取房间战绩
     *
     * @param room
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> getStanding(Room room, User user) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        Map<String, Integer> players = new HashMap<String, Integer>(4);
        List stands = new ArrayList();

        if (room != null) {

            Entity.ScoreCriteria scoreCriteria = new Entity.ScoreCriteria();
            scoreCriteria.setUserId(Entity.Value.eq(user.getId()));
            scoreCriteria.setRoomId(Entity.Value.eq(room.getId()));
            List<Score> scores = scoreDao.selectList(scoreCriteria);
            if (scores.size() > 0) {

                for (Score score : scores) {//个人在这个房间的所有战绩
                    ScoreVo sv = new ScoreVo();
                    sv.setCreatedTime(score.getCreatedTime());
                    sv.setRoomCode(room.getRoomCode());

                    if (score.getWinType() != 0) {
                        sv.setState(ScoreVo.WIN);
                    } else {
                        sv.setState(ScoreVo.LOSE);
                    }

                    Entity.ScoreCriteria sc = new Entity.ScoreCriteria();
                    sc.setTimes(Entity.Value.eq(score.getTimes()));
                    sc.setRoomId(Entity.Value.eq(room.getId()));
                    List<Score> scs = scoreDao.selectList(sc);
                    for (Score se : scs) {//其中每局四个玩家的战绩
                        User u = getUserByUserId(se.getUserId());
                        if (se.getWinType() != 0) {
                            players.put(u.getUId().toString(), se.getScore() * room.getMultiple());
                        } else {
                            players.put(u.getUId().toString(), -(se.getScore() * room.getMultiple()));
                        }
                    }
                    sv.setPlayers(players);
                    stands.add(sv);
                }
                result.put("stands", stands);
                return result;
            } else {
                throw CommonError.NOT_STANDS.newException();
            }
        } else {
            throw CommonError.ROOM_USER_NOT_IN_ROOM.newException();
        }
    }

    /**
     * 根据userId查询用户
     *
     * @param userId
     * @return
     */
    private User getUserByUserId(Integer userId) {
        Entity.UserCriteria userCriteria = new Entity.UserCriteria();
        userCriteria.setId(Entity.Value.eq(userId));
        User user = userDao.selectOne(userCriteria);
        return user;
    }

//	public TextMessage TestConnection() {
//		JsonResult jsonResult=new JsonResult();
//		jsonResult.setError(StatusUtil.SYS_SUCCESS);
//		jsonResult.setPid(PidValue.CHECK_CLIENT);
//		TextMessage textMessage = new TextMessage(jsonResult.toString());
//		return textMessage;
//	}


}