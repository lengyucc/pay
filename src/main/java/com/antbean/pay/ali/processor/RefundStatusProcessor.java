package com.antbean.pay.ali.processor;

/**
 * 退款状态处理
 * 
 * @author liminghui
 */
public interface RefundStatusProcessor {
	long onRefundSuccess(String outTradeNo, String outRequestNo);

	long onNull(String outTradeNo, String outRequestNo);
}