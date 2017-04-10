package com.jinniu.commonjn.util;

import com.jinniu.commonjn.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum CommonError {


    /**
     * 系统
     */
    SYS_SUSSES(100001, "成功", null),
    SYS_ERR(100002, "失败", null),
    SYS_PARAM_ERROR(100003, "参数错误", IllegalArgumentException.class),

    /**
     * 用户
     */
    USER_NOT_EXIST(200001, "用户不存在", UserNotExistException.class),
    USER_UnLogin(200002, "用户未登陆", UserUnLoginException.class),
    USER_LACK_COINS(200003, "金币不足", UserLackCoinsException.class),
    USER_LACK_DIAMONDS(200004, "钻石不足", UserLackDiamondsException.class),
    ALREADY_DRAW_COINS(200005, "今日金币抽奖次数已用完", AlreadyDrawCoinsException.class),
    ALREADY_GET_COINS(200006, "今日领取金币次数已用完", AlreadyGetCoinsException.class),
    ALREADY_BIND_PHONE(200007, "已经绑定过手机", AlreadyBindPhoneException.class),
    ALREADY_GET_DIAMOND(200008, "已经领取过钻石了", AlreadyGetDiamondException.class),
    UN_BIND_PHONE(200009, "未绑定手机", UnBindPhoneException.class),
    NOT_ENOUGH_GAMES(200010, "未完成足够的局数", NotEnoughGamesException.class),
    NOT_STANDS(200011, "玩家在此房间还没有战绩", NotStandsException.class),

    /**
     * 房间
     */
    ROOM_NOT_EXIST(300001, "房间不存在", RoomNotExistException.class),
    ROOM_FULL(300002, "房间已满", RoomMemberFullException.class),
    ROOM_USER_IN_ROOM(300003, "玩家已经在房间中,不能再加入房间", UserInRoomException.class),
    ROOM_USER_NOT_IN_ROOM(300004, "玩家没有在房间中", UserNotInRoomException.class),
    ROOM_READY_ERROR(300005, "玩家准备动作失败", ReadyErrorException.class),
    ROOM_UNREADY(300006, "开始游戏失败,还有玩家没有准备", UnReadyException.class),

    /**
     * 游戏过程
     */
    REDIS_GAME_DATA_ERROR(400001, "redis中的游戏数据异常",
            RedisGameDataException.class),

    USER_NOT_HAVE_SPECIFIED_CARD(400002, "玩家没有拥有指定的牌",
            UesrNotHaveCardException.class),
    NOT_YOUR_TURN(400003, "没有轮到该用户操作",
            NotYourTurnException.class);

    public static Map<String, CommonError> exceptions = new HashMap<String, CommonError>();
    private static Logger log = LoggerFactory.getLogger(CommonError.class);

    static {
        for (CommonError commonError : CommonError.values()) {
            if (commonError.exception != null) {
                exceptions.put(commonError.exception.getSimpleName(), commonError);
            }
        }
    }

    private int code;
    private String msg;
    private Class exception;


    CommonError(int code, String msg, Class exception) {
        this.code = code;
        this.msg = msg;
        this.exception = exception;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public RuntimeException newException() {
        try {
            return (RuntimeException) exception.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return new RuntimeException("创建Error对应的异常对象失败");
    }

////房间创建失败
    ///**房间号不存在*/
    //public static final ErrorCode ROOM_NUMBER_ERROR  = new ErrorCode(200001, "房间号不存在");
    ///**房间已满*/
    //public static final ErrorCode ROOM_FULL_ERROR  = new ErrorCode(200002, "房间已满");
    ///**准备失败*/
    //public static final ErrorCode READY_FAILED  = new ErrorCode(200002, "准备失败");
    ///**数据错误*/
    //public static final ErrorCode ERROR_IN_DATA  = new ErrorCode(200002, "数据错误");
    //
    //
    //
    //
    //
    ////系统编码       1000xx
    ///**成功 */
    //public static final ErrorCode SYS_SUSSES = new ErrorCode(100001,"成功");
    ///**失败 */
    //public static final ErrorCode SYS_ERR = new ErrorCode(100002,"失败");
    ///**参数不完整 */
    //public static final ErrorCode SYS_PARAM_VALUE_ERROR = new ErrorCode(100003,"参数不完整");
    ///**请求方式错误 */
    //public static final ErrorCode SYS_REQUEST_METHOD_ERROR  = new ErrorCode(100004, "请求方式错误");
    ///**连接超时 */
    //public static final ErrorCode SYS_CONNECTION_TIMED_OUT= new ErrorCode(100005, "连接超时");
    ///**请求频繁 */
    //public static final ErrorCode SYS_REQUEST_OFTEN= new ErrorCode(100006, "请求频繁");
    ///**测试提现，每次只能提1元 */
    //public static final ErrorCode SYS_TEST_WITHDRAW= new ErrorCode(100007, "测试提现，每次只能提现1元");
    //
    //
    ////用户模块编码 2000xx
    ///**手机号码已注册 */
    //public static final ErrorCode CUSTOM_PHONE_EXITS  = new ErrorCode(200001, "手机号码已注册");
    ///**无效的邀请码 */
    //public static final ErrorCode CUSTOM_INVALID_INVITE_CODE = new ErrorCode(200002, "无效的邀请码");
    ///**验证码不正确 */
    //public static final ErrorCode CUSTOM_YANGZHENGMA_ERROR = new ErrorCode(200003, "验证码不正确");
    ///**验证码已过期 */
    //public static final ErrorCode CUSTOM_YANGZHENGMA_EXPIRED  = new ErrorCode(200004, "验证码已过期");
    ///**账号或密码错误 */
    //public static final ErrorCode CUSTOM_USER_PWD_ERROR  = new ErrorCode(200005, "账号或密码错误");
    ///**用户已存在 */
    //public static final ErrorCode CUSTOM_EXIST = new ErrorCode(200006, "用户已存在");
    ///**无效的token */
    //public static final ErrorCode CUSTOM_NOT_EXIST = new ErrorCode(200007, "用户不存在");
    ///**成功 */
    //public static final ErrorCode CUSTOM_TOKEN_INVALID  = new ErrorCode(200008, "无效的token");
    ///**验证码发送失败 */
    //public static final ErrorCode CUSTOM_YANGZHENGMA_SEND_ERROR  = new ErrorCode(200009, "验证码发送失败");
    ///**非法的用户 */
    //public static final ErrorCode CUSTOM_TOKEN_USER_INVALID  = new ErrorCode(200010, "非法的用户");
    ///**签名错误 */
    //public static final ErrorCode CUSTOM_SIGN_ERROR  = new ErrorCode(200011, "签名错误");
    ///**验证码已发送 */
    //public static final ErrorCode CUSTOM_YANGZHENGMA_SEND_ALREADY  = new ErrorCode(200012, "验证码已发送");
    ///**账号已停用 */
    //public static final ErrorCode CUSTOM_STATUS_STOP  = new ErrorCode(200013, "账号已停用");
    ///**已绑定第三方平台账号 */
    //public static final ErrorCode CUSTOM_THIRDACCOUNT_BIND  = new ErrorCode(200014, "已绑定第三方平台账号");
    ///**你暂时不是代理商，请联系客服 */
    //public static final ErrorCode CUSTOM_NOT_AGENT  = new ErrorCode(200015, "你暂时不是代理商，请联系客服");
    ///**你暂时不是代理商，请联系客服 */
    //public static final ErrorCode CUSTOM_AGENT_TIMEOUT  = new ErrorCode(200016, "你的代理商已到期，请联系客服");
    ///**钱包余额不足*/
    //public static final ErrorCode CUSTOM_BALANCE_NOT_ENOUGH  = new ErrorCode(200017, "钱包余额不足");
    ///**未绑定第三方平台账号*/
    //public static final ErrorCode CUSTOM_THIRDACCOUNT_NOT_BIND  = new ErrorCode(200018, "未绑定第三方平台账号");
    ///**支付密码错误*/
    //public static final ErrorCode CUSTOM_PAYPWD_ERROR  = new ErrorCode(200019, "支付密码错误");
    ///**手机号码未注册*/
    //public static final ErrorCode CUSTOM_PHONE_NOT_REGISTER  = new ErrorCode(200020, "手机号码未注册");
    //
    ////后台模块编码 3000xx
    ///**角色名已存在，请勿重复添加 */
    //public static final ErrorCode ROLENAME_EXITS  = new ErrorCode(300001, "角色名已存在，请勿重复添加");
    ///**请至少选择一个角色 */
    //public static final ErrorCode ROLE_NOT_NULL  = new ErrorCode(300002, "请至少选择一个角色");
    ///**该用户已经配有角色，如需修改，请使用修改功能 */
    //public static final ErrorCode USER_AGENT_EXITS  = new ErrorCode(300003, "该用户已经配有角色，如需修改，请使用修改功能");
    //
    ////任务/技能模块4000xx
    ///**任务不存在 */
    //public static final ErrorCode TASK_NOT_EXIST  = new ErrorCode(400001, "任务不存在");
    ///**任务已失效 */
    //public static final ErrorCode TASK_NOT_INVALID  = new ErrorCode(400002, "任务已失效");
    ///**任务已被申请完 */
    //public static final ErrorCode TASK_ALREADY_APPLY_ALL  = new ErrorCode(400003, "任务已被申请完");
    ///**不能申请自己发布的任务/技能 */
    //public static final ErrorCode TASK_NOT_APPLY_ONESELF  = new ErrorCode(400004, "不能申请自己发布的任务/技能");
    //
    ////IM xx
    ///**发送失败 */
    //public static final ErrorCode IM_SENX_FAILED  = new ErrorCode(500001, "消息发送失败");
}
