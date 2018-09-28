package com.antbean.pay.ali.enums;

/**
 * 交易状态
 * 
 * @author liminghui
 *
 */
public enum TradeStatus {
	WAIT_BUYER_PAY("WAIT_BUYER_PAY", "交易创建，等待买家付款")//
	, TRADE_CLOSED("TRADE_CLOSED", "未付款交易超时关闭，或支付完成后全额退款")//
	, TRADE_SUCCESS("TRADE_SUCCESS", "交易支付成功")//
	, TRADE_FINISHED("TRADE_FINISHED", "交易结束，不可退款")//
	;

	private String code;
	private String desc;

	private TradeStatus(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static TradeStatus parse(String code) {
		for (TradeStatus item : TradeStatus.values()) {
			if (item.code.equals(code)) {
				return item;
			}
		}
		return null;
	}
}