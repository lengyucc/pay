package com.antbean.pay.ali.enums;

/**
 * 支付渠道
 * 
 * @author liminghui
 */
public enum PayChannel {
	balance("balance", "余额")//
	, moneyFund("moneyFund", "余额宝")//
	, coupon("coupon", "红包")//
	, pcredit("pcredit", "花呗")//
	, pcreditpayInstallment("pcreditpayInstallment", "花呗分期")//
	, creditCard("creditCard", "信用卡")//
	, creditCardExpress("creditCardExpress", "信用卡快捷")//
	, creditCardCartoon("creditCardCartoon", "信用卡卡通")//
	, credit_group("credit_group", "信用支付类型（包含信用卡卡通、信用卡快捷、花呗、花呗分期）")//
	, debitCardExpress("debitCardExpress", "借记卡快捷")//
	, mcard("mcard", "商户预存卡")//
	, pcard("pcard", "个人预存卡")//
	, promotion("promotion", "优惠（包含实时优惠+商户优惠）")//
	, voucher("voucher", "营销券")//
	, point("point", "积分")//
	, mdiscount("mdiscount", "商户优惠")//
	, bankPay("bankPay", "网银")//
	;

	private String code;
	private String desc;

	private PayChannel(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static PayChannel parse(String code) {
		for (PayChannel item : PayChannel.values()) {
			if (item.code.equals(code)) {
				return item;
			}
		}
		return null;
	}
}