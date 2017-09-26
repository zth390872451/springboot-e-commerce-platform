package com.svlada.common.request;


/**
 * REST response status code
 *
 * @creator JT
 * @modifier JT
 * @date：2017-05-19
 */
public enum CustomResponseStatus {
	_0(0, "成功"),//成功
	_1(1, "失败"),//失败
	_20001(20001, "Target has been existed."),//已经存在
	_20002(20002, "设备已被激活(或被绑定)"),//设备是绑定状态
	_20003(20003, "当前设备不在线"),//设备不在线
	_20004(20004, "设备已被停用"),//设备已被停用
	_20005(20005, "添加数量已达到上限"),//添加数量已达到上限
	_20006(20006, "设备已被关注"),//已关注
	_20007(20007, "邀请码不存在或已失效"),//已失效
	_20008(20008, "管理员不能取消关注自己"),//管理员不可以取消关注，只可解绑
	_20009(20009, "SOS电话最多添加5个"),//SOS电话最多添加5个
	_20010(20010, "设备闹钟最多添加5个"),//设备闹钟最多添加5个
	_20011(20011, "管理员不能被移出家庭成员"),
	_20012(20012, "不能删除已在使用闹钟铃声"),
	_20101(20101, "其它种类"),
	_40000(40000, "缺少参数或无效的参数"),//缺少参数、无效的参数
	_40100(40100, "未经过授权"),//未授权
	_40101(40101, "错误的访问凭证"),//错误的访问凭证,用于授权
	_40102(40102, "无效的Token"),//无效的Token，用于访问资源
	_40103(40103, "Token已过期"),//Token已过期，用于访问资源
	_40300(40300, "操作被禁止"),//操作被禁止
	_40301(40301, "API访问超出调用频率,限制访问"),//API访问超出调用频率,限制访问
	_40302(40302, "非法操作用户"),//非法操作用户
	_40400(40400, "Content Not Found"),//内容不存在

	_40401(40401, "Target is not found."),//目标不存在
	_40402(40402, "Target has been existed."),//已经存在


	_40403(40403, "Forbid period with ID %s is not found."),//上课禁用时段不存在
	_40404(40404, "Follow with userId %s is not found."),//管理员或关注者不存在
	_40405(40405, "Frequency Location Period is not found."),//定位频率不存在
	_40406(40406, "Geofence  with ID %s is not found."),//安全区域不存在
	_40407(40407, "Wifi with ID %s is not found."),//Wifi不存在
	_40408(40408, "Ring with ID %s is not found."),//铃声不存在
	_40409(40409, "Device With bind code %s is not found."),//绑定码不存在
	_40410(40410, "Family member With ID %s is not found."),//家庭成员不存在
	_40500(40500, "请求方法不支持"),//请求方法不支持
	_41500(41500, "不支持的媒体类型"),//不支持的媒体类型
	_50000(50000, "服务端内部发生错误");//服务端内部发生错误


	private final int code;
	private final String reasonPhrase;
	
	private CustomResponseStatus(int code, String reasonPhrase) {
		this.code = code;
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase() {
		/*  this.message = ApplicationSupport.getMessageByEnv(key);
        if (this.message == null) {
            this.message = key;
        }*/
		return reasonPhrase;
	}

	public Integer getCode() {
		return code;
	}
	
	/**
	 * Return the enum constant of this type with the specified numeric value.
	 *
	 * @param code
	 *            the numeric value of the enum to be returned
	 * @return the enum constant with the specified numeric value
	 * @throws IllegalArgumentException
	 *             if this enum has no constant for the specified numeric value
	 */
	public static CustomResponseStatus valueOf(int code) {
		for (CustomResponseStatus status : values()) {
			if (status.code == code) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + code + "]");
	}
	
	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return Integer.toString(code);
	}
	
}
