package com.jinniu.commonjn.util;


public class ErrorCode {
	private int code;
	private String msg;

	public ErrorCode() {

	}

	public ErrorCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	//房间创建失败
	/**房间号不存在*/
	public static final ErrorCode ROOM_NUMBER_ERROR  = new ErrorCode(200001, "房间号不存在");
	/**房间已满*/
	public static final ErrorCode ROOM_FULL_ERROR  = new ErrorCode(200002, "房间已满");
	/**准备失败*/
	public static final ErrorCode READY_FAILED  = new ErrorCode(200002, "准备失败");
	/**数据错误*/
	public static final ErrorCode ERROR_IN_DATA  = new ErrorCode(200002, "数据错误");
	
	
	
	
	
	//系统编码       1000xx
	/**成功 */
	public static final ErrorCode SYS_SUSSES = new ErrorCode(100001,"成功");
	/**失败 */
	public static final ErrorCode SYS_ERR = new ErrorCode(100002,"失败");
	/**参数不完整 */
	public static final ErrorCode SYS_PARAM_VALUE_ERROR = new ErrorCode(100003,"参数不完整");
	/**请求方式错误 */
	public static final ErrorCode SYS_REQUEST_METHOD_ERROR  = new ErrorCode(100004, "请求方式错误");
	/**连接超时 */
	public static final ErrorCode SYS_CONNECTION_TIMED_OUT= new ErrorCode(100005, "连接超时");
	/**请求频繁 */
	public static final ErrorCode SYS_REQUEST_OFTEN= new ErrorCode(100006, "请求频繁");
	/**测试提现，每次只能提1元 */
	public static final ErrorCode SYS_TEST_WITHDRAW= new ErrorCode(100007, "测试提现，每次只能提现1元");
	
	
	//用户模块编码 2000xx
	/**手机号码已注册 */
	public static final ErrorCode CUSTOM_PHONE_EXITS  = new ErrorCode(200001, "手机号码已注册");
	/**无效的邀请码 */
	public static final ErrorCode CUSTOM_INVALID_INVITE_CODE = new ErrorCode(200002, "无效的邀请码");
	/**验证码不正确 */
	public static final ErrorCode CUSTOM_YANGZHENGMA_ERROR = new ErrorCode(200003, "验证码不正确");
	/**验证码已过期 */
	public static final ErrorCode CUSTOM_YANGZHENGMA_EXPIRED  = new ErrorCode(200004, "验证码已过期");
	/**账号或密码错误 */
	public static final ErrorCode CUSTOM_USER_PWD_ERROR  = new ErrorCode(200005, "账号或密码错误");
	/**用户已存在 */
	public static final ErrorCode CUSTOM_EXIST = new ErrorCode(200006, "用户已存在");
	/**无效的token */
	public static final ErrorCode CUSTOM_NOT_EXIST = new ErrorCode(200007, "用户不存在");
	/**成功 */
	public static final ErrorCode CUSTOM_TOKEN_INVALID  = new ErrorCode(200008, "无效的token");
	/**验证码发送失败 */
	public static final ErrorCode CUSTOM_YANGZHENGMA_SEND_ERROR  = new ErrorCode(200009, "验证码发送失败");
	/**非法的用户 */
	public static final ErrorCode CUSTOM_TOKEN_USER_INVALID  = new ErrorCode(200010, "非法的用户");
	/**签名错误 */
	public static final ErrorCode CUSTOM_SIGN_ERROR  = new ErrorCode(200011, "签名错误");
	/**验证码已发送 */
	public static final ErrorCode CUSTOM_YANGZHENGMA_SEND_ALREADY  = new ErrorCode(200012, "验证码已发送");
	/**账号已停用 */
	public static final ErrorCode CUSTOM_STATUS_STOP  = new ErrorCode(200013, "账号已停用");
	/**已绑定第三方平台账号 */
	public static final ErrorCode CUSTOM_THIRDACCOUNT_BIND  = new ErrorCode(200014, "已绑定第三方平台账号");
	/**你暂时不是代理商，请联系客服 */
	public static final ErrorCode CUSTOM_NOT_AGENT  = new ErrorCode(200015, "你暂时不是代理商，请联系客服");
	/**你暂时不是代理商，请联系客服 */
	public static final ErrorCode CUSTOM_AGENT_TIMEOUT  = new ErrorCode(200016, "你的代理商已到期，请联系客服");
	/**钱包余额不足*/
	public static final ErrorCode CUSTOM_BALANCE_NOT_ENOUGH  = new ErrorCode(200017, "钱包余额不足");
	/**未绑定第三方平台账号*/
	public static final ErrorCode CUSTOM_THIRDACCOUNT_NOT_BIND  = new ErrorCode(200018, "未绑定第三方平台账号");
	/**支付密码错误*/
	public static final ErrorCode CUSTOM_PAYPWD_ERROR  = new ErrorCode(200019, "支付密码错误");
	/**手机号码未注册*/
	public static final ErrorCode CUSTOM_PHONE_NOT_REGISTER  = new ErrorCode(200020, "手机号码未注册");
	
	//后台模块编码 3000xx
	/**角色名已存在，请勿重复添加 */
	public static final ErrorCode ROLENAME_EXITS  = new ErrorCode(300001, "角色名已存在，请勿重复添加");
	/**请至少选择一个角色 */
	public static final ErrorCode ROLE_NOT_NULL  = new ErrorCode(300002, "请至少选择一个角色");
	/**该用户已经配有角色，如需修改，请使用修改功能 */
	public static final ErrorCode USER_AGENT_EXITS  = new ErrorCode(300003, "该用户已经配有角色，如需修改，请使用修改功能");
	
	//任务/技能模块4000xx
	/**任务不存在 */
	public static final ErrorCode TASK_NOT_EXIST  = new ErrorCode(400001, "任务不存在");
	/**任务已失效 */
	public static final ErrorCode TASK_NOT_INVALID  = new ErrorCode(400002, "任务已失效");
	/**任务已被申请完 */
	public static final ErrorCode TASK_ALREADY_APPLY_ALL  = new ErrorCode(400003, "任务已被申请完");
	/**不能申请自己发布的任务/技能 */
	public static final ErrorCode TASK_NOT_APPLY_ONESELF  = new ErrorCode(400004, "不能申请自己发布的任务/技能");
	
	//IM xx
	/**发送失败 */
	public static final ErrorCode IM_SENX_FAILED  = new ErrorCode(500001, "消息发送失败");
}
