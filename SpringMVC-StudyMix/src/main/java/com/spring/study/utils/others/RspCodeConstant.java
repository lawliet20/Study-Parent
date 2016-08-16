package com.spring.study.utils.others;
/** 
 * @author  Z
 * @date 2015-6-28 下午12:10:10 
 * 
 */
public class RspCodeConstant {
	public final static String SUCCESS="0000"; 	//操作成功
	public final static String FAILURE="9999"; 	//操作失败
	public final static String WAIT_FOR_PAY="5000"; 	//等待支付
	public final static String ERR_TERMINAL_NOT_EXSIT="9997"; 	//终端号不存在
	public final static String ERR_SYSTEM_EXCEPTION="9996"; 	//系统异常
	public final static String ERR_MAC_CHECK="9998"; 	//Mac校验错
	public final static String ERR_CHECK_VALUE="9995"; 	//校验码不正确
	public final static String ERR_MERCHANT_STAT_EXP="9994"; 	//终端号不存在
	public final static String ERR_ARGS_NOT_COMPLETE="1005"; 	//参数不齐全
	public final static String ERR_MEMBER_NOT_EXSIT="1001"; 	//会员不存在
	public final static String ERR_MEMBER_EXSIT="1002"; 	//会员已存在
	public final static String ERR_MEMBER_STAT_EXP="1003"; 	//会员状态不正确
	public final static String ERR_BIND_CARD="1006"; 	//绑卡失败
	public final static String ERR_CARD_IS_BINDED="1007"; 	//卡已被绑
	public final static String ERR_CARD_NOT_BINDED="1008"; 	//卡未绑定
	public final static String ERR_PWD_NOT_INIT="1009"; 	//密码未初始化
	public final static String ERR_PWD_NOT_CORRECT="1010"; 	//密码不正确
	public final static String ERR_EDOU_NOT_FULL="1011"; 	//e豆余额不足
	public final static String ERR_MSG_NOT_PATCHED="1012"; 	//会员跟券不匹配
	public final static String ERR_COUPON_IS_VALID="1013"; 	//券码失效
	public final static String ERR_COUPON_IS_USED="1014"; 	//券码已使用
	public final static String ERR_COUPON_UNSATISFY="1015"; 	//券码使用条件不满足
	public final static String ERR_MEMBER_TYPE_WRONG="1016"; 	//会员类别不正确
	public final static String ERR_MEMBER_BINDCARD_EMPTY="1017"; 	//绑卡记录为空
	public final static String ERR_MERCHANT_INFO_EMPTY="1018"; 	//商户信息为空
	public final static String ERR_MERCHANT_ACT_EMPTY="1019"; 	//商户活动信息为空
	public final static String ERR_WX_IS_BINDED="1020"; 	//微信已被绑
	public final static String ERR_MEMBER_BINDWX_EMPTY="1021"; 	//绑定微信信息为空
	public final static String ERR_MEMBER_CARD_EMPTY="1022"; 	//会员卡信息为空
	public final static String ERR_MEMBER_CARD_STATUS="1023"; 	//会员卡状态不正常
	public final static String ERR_MEMBER_CARD_EXPIRE="1024"; 	//会员卡过期
	public final static String ERR_BANK_CARD_INVALID="1025"; 	//银行卡无效
	public final static String ERR_BIND_CARD_OVER="1026"; 	//绑卡数量超过限制
	public final static String ERR_ORIG_TRANS_NOT_FOUND="1027"; 	//原始交易不存在
	public final static String ERR_ORIG_TRANSAMT_NOT_PATCH="1028"; 	//交易金额不匹配
	public final static String ERR_PRE_ORDER_STAT_EXP="1029"; 	//订单状态不正确
	public final static String ERR_MERCHANT_CATEGORY="1030"; 	//商户分类不存在
	public final static String ERR_MEMBER_CONSUME_LOG_EMPTY="1031"; 	//会员消费记录为空
	public final static String ERR_WEIXIN_BARCODE_EXPIREDT="1032"; 	//条码过期
	public final static String ERR_WEIXIN_NOT_ENOUFGH="1033"; 	//微信余额不足
	public final static String ERR_MEMBER_ACCT_DAY_TOTAL_EMPTY="1034"; 	//会员账户汇总信息为空
	public final static String ERR_REPEAT_TRANSATION="1035"; 	//重复交易
	public final static String ERR_ACTIVITY_STATUS="1036"; 	//活动状态不正常
	public final static String ERR_ACTIVITY_TYPE="1037"; 	//活动状态类型错误
	public final static String ERR_REPEAT_GET_MTASK="1038"; 	//重复领取佣金任务
	public final static String ERR_REPEAT_GET_COUPON="1039"; 	//重复领取优惠券
	public final static String ERR_MERCHANT_NOTHAVA_COUPON="1040"; 	//重复领取优惠券
	public final static String ERR_COUPON_NUM_ZERO="1041"; 	//优惠券领取完毕
	public final static String ERR_RECMDMEMBERID_NULL="1042"; 	//会员推荐人为空
	public final static String ERR_COUPONNO_EMPTY="1043"; 	//优惠券号为空
	public final static String ERR_OPENID_EMPTY="1044"; 	//会员微信openid为空
	public final static String ERR_QUERY_COUPON_LIST_EMPTY="1045"; 	//优惠券列表为空
	public final static String ERR_PW_LOCK="1046"; 	//密码错误锁定
	public final static String ERR_RE_PAY="1047"; 	//重新支付没有刷新错误
	public final static String ERR_DISCOUNTRATE="1048"; 	//签约费率异常
}
