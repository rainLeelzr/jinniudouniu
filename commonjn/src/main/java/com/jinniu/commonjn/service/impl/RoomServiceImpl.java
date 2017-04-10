package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.RoomDao;
import com.jinniu.commonjn.dao.RoomMemberDao;
import com.jinniu.commonjn.dao.UserDao;
import com.jinniu.commonjn.dao.VoteDao;
import com.jinniu.commonjn.model.*;
import com.jinniu.commonjn.redis.RoomRedis;
import com.jinniu.commonjn.service.RoomService;
import com.jinniu.commonjn.util.CommonError;
import com.jinniu.commonjn.util.CommonUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomServiceImpl extends BaseServiceImpl<Integer, Room> implements RoomService {

    @Autowired
    private GameService gameService;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoomMemberDao roomMemberDao;
    @Autowired
    private VoteDao voteDao;


    @Autowired
    private RoomRedis roomRedis;

    /**
     * 创建房间逻辑
     * 根据有无房间号来判断是创建好友房还是创建金币房
     * 若玩家还在房间中就不能再创建房间
     * 创建好友房前要判断用户是否有足够的钻石支付开房费
     *
     * @param data
     * @return
     */
    public Map<String, Object> createRoom(JSONObject data) {

        Map<String, Object> result = new HashMap<String, Object>(3);
        String times = (String) data.get("times");
        String uId = (String) data.get("uId");
        Integer multiple = (Integer) data.get("multiple");
        Integer payType = (Integer) data.get("payType");
        Integer type = (Integer) data.get("type");
        Integer diamond = (Integer) data.get("diamond");

        Entity.UserCriteria userCriteria = new Entity.UserCriteria();
        userCriteria.setUId(Entity.Value.eq(uId));
        User user = userDao.selectOne(userCriteria);

        if (user != null) {
            RoomMember roomMember = checkInRoom(user.getId());

            if (roomMember == null) {//玩家没有在房间中

                Room room = new Room();
                room.setCreatedUserId(user.getId());
                room.setType(type);
                room.setPlayers(Room.playerLimit);
                room.setCreatedTime(new Date());
                room.setStart(Room.start.UNSTART.getCode());
                room.setState(Room.state.wait.getCode());
                room.setMultiple(multiple);
                Integer roomCode = CommonUtil.createRoomCode();
                roomCode = checkRoomCode(roomCode);
                room.setRoomCode(roomCode);

                if (multiple >= Room.multiple.COINS_WITH_2000.getCode()) {//创建金币房
                    result = createCoinRoom(room, result);

                } else {//创建好友房
                    if (user.getDiamond() >= (payType == Room.payType.PAY_BY_ONE.getCode() ? diamond : diamond / Room.playerLimit)) {
                        result = createFriendRoom(times, diamond, payType, room, result);
                    } else {
                        throw CommonError.USER_LACK_DIAMONDS.newException();
                    }
                }
                result.put("user", user);
                return result;
            } else {
                throw CommonError.ROOM_USER_IN_ROOM.newException();
            }
        } else {
            throw CommonError.USER_NOT_EXIST.newException();
        }
    }

    /**
     * 防止房间号码相同
     *
     * @param roomCode
     * @return
     */
    private Integer checkRoomCode(Integer roomCode) {
        Entity.RoomCriteria roomCriteria = new Entity.RoomCriteria();
        roomCriteria.setState(Entity.Value.ne(Room.state.DISMISS.getCode()));
        roomCriteria.setRoomCode(Entity.Value.eq(roomCode));
        long count = roomDao.selectCount(roomCriteria);
        if (count > 0) {
            roomCode = CommonUtil.createRoomCode();//假如创建房间的号码与未解散的房间相同,需要再次创建,确保不同
            roomCode = checkRoomCode(roomCode);
        }
        return roomCode;
    }

    private Map<String, Object> createCoinRoom(Room room, Map<String, Object> result) {
        roomDao.save(room);
        result.put("room", room);
        RoomMember roomMember = createRoomMember(room, room.getCreatedUserId());
        result.put("roomMember", roomMember);
        roomRedis.createRoom(room, roomMember);
        return result;
    }

    private Map<String, Object> createFriendRoom(String times, Integer diamond, Integer payType, Room room, Map<String, Object> result) {
        room.setDiamond(diamond);
        room.setPayType(payType);
        room.setTimes(times);

        roomDao.save(room);
        result.put("room", room);
        RoomMember roomMember = createRoomMember(room, room.getCreatedUserId());
        result.put("roomMember", roomMember);
        roomRedis.createRoom(room, roomMember);

        return result;
    }

    private RoomMember createRoomMember(Room room, Integer userId) {
        RoomMember roomMember = new RoomMember();
        roomMember.setState(RoomMember.state.UNREADY.getCode());
        roomMember.setJoinTime(new Date());
        roomMember.setRoomId(room.getId());
        roomMember.setSeat(1);
        roomMember.setUserId(userId);
        roomMemberDao.save(roomMember);
        return roomMember;

    }

    private Map<String, Object> joinRoomMember(Room room, Long count, User user, Map<String, Object> result) {
        RoomMember roomMember = createRoomMember(room, user.getId());
        roomMember.setSeat((int) (count + 1));
        roomMemberDao.update(roomMember);
        result.put("room", room);
        roomRedis.joinRoom(roomMember);
        //查出房间中所有玩家
        Set<RoomMember> roomMembers = roomRedis.getRoomMembers(roomMember.getRoomId().toString());
        result.put("roomMembers", roomMembers);
        return result;

    }

    /**
     * 加入房间逻辑
     * 根据有无房号来就加入好友房和金币房
     * 玩家在房间中的时候不能再加入房间
     * 其中加入好友房是需要判断房间的支付方式是一人支付还是四人支付
     * 四人支付的话判断玩家是否有足够钻石支付房费
     * 而加入金币房判断是快速加入还是进入指定金币场
     * 都要判断玩家的金币是否达到加入房间的条件
     * 快速加入能根据玩家的金币数进入对相应的金币场
     * 所有金币房间都没有空位时,系统自动创建房间
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> joinRoom(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<>(2);

        Integer roomCode = (Integer) data.get("roomCode");
        Integer multiple = (Integer) data.get("multiple");

        if (user != null) {
            RoomMember roomMember = checkInRoom(user.getId());

            if (roomCode != null) {//进入好友场
                if (roomMember == null) {//玩家没有在房间中
                    result = joinFriendRoom(user, roomCode, result);
                } else {
                    throw CommonError.ROOM_USER_IN_ROOM.newException();
                }
            } else {//进入金币场
                if (roomMember != null) {//玩家已在房间中,即为换桌加入(加入到原来的等级的房间)
                    Room room = getRoomByRoomId(roomMember.getRoomId());
                    data.put("roomCode", room.getRoomCode());
                    outRoom(data, user);
                }
                if (multiple != null) {//进入指定金币场
                    if (user.getCoin() >= multiple) {
                        result = joinCoinRoom(multiple, user, result);
                        if (result == null) {//所有房间都已经满人,系统自动创建金币房间
                            data.put("uId", user.getUId().toString());
                            data.put("type", Room.type.COINS_ROOM.getCode());
                            result = createRoom(data);
                        }
                    } else {
                        throw CommonError.USER_LACK_COINS.newException();
                    }
                } else {//快速加入

                    if (user.getCoin() >= Room.multiple.COINS_WITH_2000.getCode() && user.getCoin() < Room.multiple.COINS_WITH_20000.getCode()) {
                        multiple = Room.multiple.COINS_WITH_2000.getCode();
                    } else if (user.getCoin() >= Room.multiple.COINS_WITH_20000.getCode()) {
                        multiple = Room.multiple.COINS_WITH_20000.getCode();
                    } else {
                        throw CommonError.USER_LACK_COINS.newException();
                    }
                    result = joinCoinRoom(multiple, user, result);
                    if (result == null) {//所有房间都已经满人或没有对应的金币场房间,系统自动创建金币房间
                        data.put("multiple", multiple);
                        data.put("uId", user.getUId().toString());
                        data.put("type", Room.type.COINS_ROOM.getCode());
                        result = createRoom(data);
                    }
                }
            }

            List<User> users = new ArrayList<>();
            Set<RoomMember> roomMembers = (Set<RoomMember>) result.get("roomMembers");
            if (roomMembers != null) {
                for (RoomMember member : roomMembers) {
                    User u = getUserByUserId(member.getUserId());
                    users.add(u);
                }
                result.put("users", users);
                result.put("userId", user.getId());
            }
            return result;
        } else {
            throw CommonError.USER_NOT_EXIST.newException();
        }


    }


    private Map<String, Object> joinCoinRoom(Integer multiple, User user, Map<String, Object> result) {
        Entity.RoomCriteria roomCriteria = new Entity.RoomCriteria();
        roomCriteria.setMultiple(Entity.Value.eq(multiple));
        roomCriteria.setState(Entity.Value.ne(Room.state.DISMISS.getCode()));
        List<Room> rooms = roomDao.selectList(roomCriteria);
        if (rooms.size() > 0) {
            for (Room room : rooms) {
                Long count = roomRedis.getRoomMemberCount(room.getId().toString());
                if (count < Room.playerLimit) {
                    return joinRoomMember(room, count, user, result);
                }
            }
        }
        return null;
    }

    private Map<String, Object> joinFriendRoom(User user, Integer roomCode, Map<String, Object> result) {
        Room room = getRoomByRoomCode(roomCode);
        if (room != null && room.getType() == Room.type.FRIENDS_ROOM.getCode()) {
            Long count = roomRedis.getRoomMemberCount(room.getId().toString());
            if (count < Room.playerLimit) {

                return joinRoomMember(room, count, user, result);
            } else {
                throw CommonError.ROOM_FULL.newException();
            }
        } else {
            throw CommonError.ROOM_NOT_EXIST.newException();
        }
    }

    /**
     * 退出房间
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> outRoom(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<>(2);

        Integer roomCode = (Integer) data.get("roomCode");


        if (user != null) {
            RoomMember roomMember = checkInRoom(user.getId());
            if (roomMember != null) {//需要在房间中才能退出房间
                Room room = getRoomByRoomCode(roomCode);

                if (room != null) {
                    Long count = roomRedis.getRoomMemberCount(room.getId().toString());
                    if (count > 1) {//房间人数大于1人时,退出房间后房间状态为待开始状态
                        room.setState(Room.state.wait.getCode());
                    } else {//房间只剩下1个人时,退出房间后房间状态为解散状态
                        room.setState(Room.state.DISMISS.getCode());
                        room.setLastLoginTime(new Date());
                        roomRedis.dismissRoom(room);
                    }
                    roomDao.update(room);
                } else {
                    throw CommonError.ROOM_NOT_EXIST.newException();
                }
                roomRedis.editRoom(roomMember);//移除redis中的房间成员

                //座位顺序比退出玩家位置大的玩家都需要退后一位
                Set<RoomMember> roomMembers = roomRedis.getRoomMembers(room.getId().toString());
                for (RoomMember member : roomMembers) {
                    if (member.getSeat() > roomMember.getSeat()) {
                        roomRedis.editRoom(member);
                        member.setSeat(member.getSeat() - 1);
                        roomMemberDao.update(member);
                        roomRedis.joinRoom(member);//更新redis中的房间用户信息
                    }

                }

                roomMember.setState(RoomMember.state.OUT_ROOM.getCode());
                roomMember.setSeat(-1);
                roomMember.setLeaveTime(new Date());
                roomMemberDao.update(roomMember);

                result.put("roomId", room.getId());
                result.put("result", true);
                return result;
            } else {
                throw CommonError.ROOM_USER_NOT_IN_ROOM.newException();
            }
        } else {
            throw CommonError.USER_NOT_EXIST.newException();
        }

    }

    /**
     * 解散房间
     */
    private void dismissRoom(RoomMember roomMember) {
        User user = getUserByUserId(roomMember.getUserId());

        Room room = getRoomByRoomId(roomMember.getRoomId());

        JSONObject data = new JSONObject();
        data.put("roomCode", room.getRoomCode());
        outRoom(data, user);
    }

    /**
     * 申请解散房间
     * 未发牌前解散房间是可以直接解散
     * 发牌后解散需要发起解散房间申请,需其他玩家同意
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> dismissRoom(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<>(3);
        boolean r;
        Integer roomCode = (Integer) data.get("roomCode");

        Room room = getRoomByRoomCode(roomCode);

        if (room != null) {

            Set<RoomMember> roomMembers = roomRedis.getRoomMembers(room.getId().toString());
            if (room.getStart() == Room.start.UNSTART.getCode()) {//未开始游戏,可以直接解散房间
                for (RoomMember roomMember : roomMembers) {
                    dismissRoom(roomMember);
                }
                r = true;
            } else {//已开始游戏,需要申请解散,其他玩家同意才可以解散
                r = false;

                for (RoomMember roomMember : roomMembers) {
                    Vote vote = new Vote();
                    vote.setRoomId(room.getId());
                    vote.setType(Vote.type.DISMISS_VOTE.getCode());
                    vote.setState(Vote.state.UN_VOTE.getCode());
                    vote.setOrganizerUserId(user.getId());
                    vote.setVoterUserId(roomMember.getUserId());
                    vote.setStatus(Vote.status.PROCESSING.getCode());
                    if (roomMember.getUserId().equals(user.getId())) {//假如是发起人,则默认投票为同意
                        vote.setState(Vote.state.AGREE.getCode());
                        vote.setStatus(Vote.status.FINISH.getCode());
                    }
                    voteDao.save(vote);
                }

                //2分钟计时无响应默认同意

            }
            result.put("roomId", room.getId());
            result.put("uId", user.getUId());
            result.put("nickName", user.getNickName());
            result.put("result", r);
            return result;
        } else {
            throw CommonError.ROOM_NOT_EXIST.newException();
        }

    }

    /**
     * 是否同意解散房间
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> agreeDismiss(JSONObject data, User user) {
        Map<String, Object> result = new HashMap<>(2);
        boolean isAgree = (boolean) data.get("isAgree");
        Integer roomCode = (Integer) data.get("roomCode");

        if (user != null) {
            RoomMember roomMember = checkInRoom(user.getId());
            if (roomMember != null) {
                Room room = getRoomByRoomCode(roomCode);
                if (room != null) {
                    Entity.VoteCriteria voteCriteria = new Entity.VoteCriteria();
                    voteCriteria.setVoterUserId(Entity.Value.eq(user.getId()));
                    voteCriteria.setRoomId(Entity.Value.eq(room.getId()));
                    voteCriteria.setType(Entity.Value.eq(Vote.type.DISMISS_VOTE.getCode()));
                    voteCriteria.setStatus(Entity.Value.eq(Vote.status.PROCESSING.getCode()));
                    Vote vote = voteDao.selectOne(voteCriteria);

                    if (isAgree) {//有玩家同意解散
                        vote.setState(Vote.state.AGREE.getCode());
                        voteDao.update(vote);

                        Entity.VoteCriteria vc = new Entity.VoteCriteria();
                        vc.setRoomId(Entity.Value.eq(room.getId()));
                        vc.setState(Entity.Value.eq(Vote.state.UN_VOTE.getCode()));
                        long count = voteDao.selectCount(vc);

                        if (count == 0) {//全部人投票同意解散房间
                            Set<RoomMember> roomMembers = roomRedis.getRoomMembers(room.getId().toString());
                            for (RoomMember member : roomMembers) {
                                dismissRoom(member);
                            }
                        }

                    } else {//有玩家不同意解散,即游戏继续
                        vote.setState(Vote.state.DISAGREE.getCode());
                    }

                    vote.setStatus(Vote.status.FINISH.getCode());
                    voteDao.update(vote);
                    result.put("result", isAgree);
                    result.put("roomId", room.getId());
                    result.put("uId", user.getUId());
                    result.put("nickName", user.getNickName());
                    return result;
                } else {
                    throw CommonError.ROOM_NOT_EXIST.newException();
                }
            } else {
                throw CommonError.ROOM_USER_NOT_IN_ROOM.newException();
            }
        } else {
            throw CommonError.USER_NOT_EXIST.newException();
        }

    }

    /**
     * 获取的金币房在玩人数
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> numberOfPlayers(JSONObject data) {
        Map<String, Object> result = new HashMap<>(2);
        long primary = roomDao.countForPlayers(Room.multiple.COINS_WITH_2000.getCode());
        long senior = roomDao.countForPlayers(Room.multiple.COINS_WITH_20000.getCode());
        result.put("primary", primary);
        result.put("senior", senior);
        return result;
    }


    /**
     * 用户准备
     * 需要在房间中并且之前的状态为待准备的玩家才能准备
     * 当四个玩家都已经准备的时候可以开始游戏
     *
     * @return
     */
    @Override
    public Map<String, Object> ready(User user) throws IllegalAccessException, InstantiationException {
        Map<String, Object> result = new HashMap<>(4);
        Integer type;

        if (user != null) {
            RoomMember roomMember = new RoomMember();
            roomMember.setUserId(user.getId());
            roomMember.setState(RoomMember.state.UNREADY.getCode());
            roomMember = roomMemberDao.selectByUserIdForReady(roomMember);
            if (roomMember != null) {//需要在房间中并且之前的状态为待准备的玩家才能准备
                roomMember.setState(RoomMember.state.READY.getCode());
                roomMemberDao.update(roomMember);

                //判断房间用户是否已经全部准备
                List<RoomMember> roomMembers = roomMemberDao.selectForStart(roomMember);
                if (roomMembers != null && roomMembers.size() == Room.playerLimit) {//所有玩家都已经准备,可以发牌
                    //调用开始发牌接口
                    Room room = roomDao.selectOne(roomMember.getRoomId());
                    room.setStart(Room.start.STARTED.getCode());
                    room.setState(Room.state.PLAYING.getCode());
                    roomDao.update(room);
                    Map<String, Object> mahjongGameData = gameService.firstPutOutCard(room, roomMembers);
                    result.putAll(mahjongGameData);
                    type = 2;
                } else {
                    type = 1;
                }
                roomRedis.editRoom(roomMember);
                roomRedis.joinRoom(roomMember);
                result.put("type", type);
                result.put("roomId", roomMember.getRoomId());
                result.put("uId", user.getUId());
                result.put("state", roomMember.getState());
                return result;
            } else {
                throw CommonError.ROOM_READY_ERROR.newException();
            }
        } else {
            throw CommonError.USER_NOT_EXIST.newException();
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

    /**
     * 根据房间号查询房间
     *
     * @param roomCode
     * @return
     */
    private Room getRoomByRoomCode(Integer roomCode) {
        Entity.RoomCriteria roomCriteria = new Entity.RoomCriteria();
        roomCriteria.setRoomCode(Entity.Value.eq(roomCode));
        roomCriteria.setState(Entity.Value.ne(Room.state.DISMISS.getCode()));
        Room room = roomDao.selectOne(roomCriteria);
        return room;
    }

    /**
     * 根据房间id查询房间
     *
     * @param roomId
     * @return
     */
    private Room getRoomByRoomId(Integer roomId) {
        Entity.RoomCriteria roomCriteria = new Entity.RoomCriteria();
        roomCriteria.setId(Entity.Value.eq(roomId));
        Room room = roomDao.selectOne(roomCriteria);
        return room;
    }

    /**
     * 判断玩家是否在房间中
     *
     * @param userId
     * @return
     */
    private RoomMember checkInRoom(Integer userId) {
        RoomMember roomMember = new RoomMember();
        roomMember.setUserId(userId);
        roomMember = roomMemberDao.selectByUserIdForCheck(roomMember);
        return roomMember;
    }


}