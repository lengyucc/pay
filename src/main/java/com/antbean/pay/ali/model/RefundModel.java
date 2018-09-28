package com.antbean.pay.ali.model;

/**
 * 退款model
 * 
 * @author liminghui
 */
public class RefundModel extends Model {
	private String outTradeNo;
	private String outRequestNo;
	private String refundAmount;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRequestNo() {
		return outRequestNo;
	}

	public void setOutRequestNo(String outRequestNo) {
		this.outRequestNo = outRequestNo;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Override
	public String toString() {
		return "RefundModel [outTradeNo=" + outTradeNo + ", outRequestNo=" + outRequestNo + ", refundAmount="
				+ refundAmount + "]";
	}

	public RefundModel() {
		super();
	}

	public RefundModel(String outTradeNo, String outRequestNo, String refundAmount) {
		super();
		this.outTradeNo = outTradeNo;
		this.outRequestNo = outRequestNo;
		this.refundAmount = refundAmount;
	}
}