package com.antbean.pay.ali.processor;

/**
 * 交易状态处理
 * 
 * @author liminghui
 */
public interface TradeStatusProcessor {

	long onWaitBuyerPay(String outTradeNo);

	long onTradeClosed(String outTradeNo);

	long onTradeSuccess(String outTradeNo);

	long onTradeFinished(String outTradeNo);

	long onNull(String outTradeNo);
}