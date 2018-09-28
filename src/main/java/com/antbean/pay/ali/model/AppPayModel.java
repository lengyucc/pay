package com.antbean.pay.ali.model;

import com.antbean.pay.ali.enums.PayChannel;

/**
 * App支付model
 * 
 * @author liminghui
 */
public class AppPayModel extends Model {
	private String outTradeNo;
	private String totalAmount;
	private String subject;
	private PayChannel[] enablePayChannels;
	private PayChannel[] disablePayChannels;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public PayChannel[] getEnablePayChannels() {
		return enablePayChannels;
	}

	public void setEnablePayChannels(PayChannel[] enablePayChannels) {
		this.enablePayChannels = enablePayChannels;
	}

	public PayChannel[] getDisablePayChannels() {
		return disablePayChannels;
	}

	public void setDisablePayChannels(PayChannel[] disablePayChannels) {
		this.disablePayChannels = disablePayChannels;
	}

	public AppPayModel(String outTradeNo, String totalAmount, String subject) {
		super();
		this.outTradeNo = outTradeNo;
		this.totalAmount = totalAmount;
		this.subject = subject;
	}

	public AppPayModel() {
		super();
	}

}