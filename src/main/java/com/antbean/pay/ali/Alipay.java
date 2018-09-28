package com.antbean.pay.ali;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.antbean.pay.SystemHolder;
import com.antbean.pay.ali.enums.PayChannel;
import com.antbean.pay.ali.enums.RefundStatus;
import com.antbean.pay.ali.enums.TradeStatus;
import com.antbean.pay.ali.model.AppPayModel;
import com.antbean.pay.ali.model.RefundModel;
import com.antbean.pay.ali.processor.RefundStatusProcessor;
import com.antbean.pay.ali.processor.TradeStatusProcessor;

public class Alipay {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unused")
	private String appId;

	@SuppressWarnings("unused")
	private String appPrivateKey;

	private String alipayPublicKey;

	@SuppressWarnings("unused")
	private String serverUrl;

	private String notifyUrl;

	private static final String SIGN_TYPE = "RSA2";

	private static final String FORMAT = "json";

	private static final String CHARSET = "utf-8";

	private final AlipayClient alipayClient;

	public Alipay(String appId, String appPrivateKey, String alipayPublicKey, String notifyUrl, String serverUrl) {
		super();
		this.notifyUrl = notifyUrl;
		this.appId = appId;
		this.appPrivateKey = appPrivateKey;
		this.alipayPublicKey = alipayPublicKey;
		this.notifyUrl = notifyUrl;
		this.serverUrl = serverUrl;

		this.alipayClient = new DefaultAlipayClient(serverUrl, appId, appPrivateKey, FORMAT, CHARSET, alipayPublicKey,
				SIGN_TYPE);
	}

	public String createAppPayTrade(AppPayModel _model, TradeStatusProcessor processor, long delay) {
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setSubject(_model.getSubject());
		model.setOutTradeNo(_model.getOutTradeNo());
		model.setTotalAmount(_model.getTotalAmount());

		if (_model.hasOptionField()) {
			try {
				BeanUtils.populate(model, _model.getOptionFields());
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		if (null != _model.getEnablePayChannels() && 0 < _model.getEnablePayChannels().length) {
			StringBuilder enablePayChannels = new StringBuilder();

			for (PayChannel payChannel : _model.getEnablePayChannels()) {
				enablePayChannels.append(",");
				enablePayChannels.append(payChannel.getCode());
			}

			model.setEnablePayChannels(enablePayChannels.substring(1));
		}
		if (null != _model.getDisablePayChannels() && 0 < _model.getDisablePayChannels().length) {
			StringBuilder disablePayChannels = new StringBuilder();
			for (PayChannel payChannel : _model.getEnablePayChannels()) {
				disablePayChannels.append(",");
				disablePayChannels.append(payChannel.getCode());
			}
			model.setDisablePayChannels(disablePayChannels.substring(1));
		}

		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);

		String orderString = null;
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			orderString = response.getBody();// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
			throw new RuntimeException(e);
		}

		if (processor != null && delay > 0) {
			String outTradeNo = _model.getOutTradeNo();
			SystemHolder.getSchedulerPool().schedule(new Runnable() {
				@Override
				public void run() {
					TradeStatus tradeStatus = queryTradeStatus(_model.getOutTradeNo());
					long nextDelay = process(outTradeNo, processor, tradeStatus);
					if (nextDelay > 0) {
						SystemHolder.getSchedulerPool().schedule(this, nextDelay, TimeUnit.MILLISECONDS);
					}

				}
			}, delay, TimeUnit.MILLISECONDS);
		}
		return orderString;
	}

	private long process(String outTradeNo, TradeStatusProcessor processor, TradeStatus tradeStatus) {
		long nextDelay = 0;
		if (tradeStatus == TradeStatus.WAIT_BUYER_PAY) {
			nextDelay = processor.onWaitBuyerPay(outTradeNo);
		}
		if (tradeStatus == TradeStatus.TRADE_CLOSED) {
			nextDelay = processor.onTradeClosed(outTradeNo);
		}
		if (tradeStatus == TradeStatus.TRADE_SUCCESS) {
			nextDelay = processor.onTradeSuccess(outTradeNo);
		}
		if (tradeStatus == TradeStatus.TRADE_FINISHED) {
			nextDelay = processor.onTradeFinished(outTradeNo);
		}
		if (tradeStatus == null) {
			nextDelay = processor.onNull(outTradeNo);
		}
		return nextDelay;
	}

	public void createRefund(RefundModel _model, RefundStatusProcessor processor, long delay) {
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		model.setOutTradeNo(_model.getOutTradeNo());
		model.setOutRequestNo(_model.getOutRequestNo());
		model.setRefundAmount(_model.getRefundAmount());

		if (_model.hasOptionField()) {
			try {
				BeanUtils.populate(model, _model.getOptionFields());
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);

		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			if (!response.isSuccess()) {
				logger.debug("Response[code={}, msg={}, sub_code={}, sub_msg={}]", response.getCode(),
						response.getMsg(), response.getSubCode(), response.getSubMsg());
				throw new RuntimeException("Create refund error");
			}
		} catch (AlipayApiException e) {
			logger.warn("Create refund error", e);
			throw new RuntimeException(e);
		}

		if (processor != null && delay > 0) {
			String outTradeNo = _model.getOutTradeNo();
			String outRequestNo = _model.getOutRequestNo();
			SystemHolder.getSchedulerPool().schedule(new Runnable() {
				@Override
				public void run() {
					RefundStatus refundStatus = queryRefundStatus(outTradeNo, outRequestNo);

					long nextDelay = 0;
					if (refundStatus == RefundStatus.REFUND_SUCCESS) {
						nextDelay = processor.onRefundSuccess(outTradeNo, outRequestNo);
					}
					if (refundStatus == null) {
						nextDelay = processor.onNull(outTradeNo, outRequestNo);
					}
					if (nextDelay > 0) {
						SystemHolder.getSchedulerPool().schedule(this, nextDelay, TimeUnit.MILLISECONDS);
					}

				}
			}, delay, TimeUnit.MILLISECONDS);
		}

	}

	public boolean verifyNotifyData(Map<String, String[]> notifyData) {
		Map<String, String> params = new HashMap<String, String>();
		Iterator<String> iterator = notifyData.keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			String[] values = notifyData.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		try {
			return AlipaySignature.rsaCheckV1(params, this.alipayPublicKey, CHARSET, SIGN_TYPE);
		} catch (AlipayApiException e) {
			logger.debug("Verify notify data error", e);
			return false;
		}
	}

	public void processNotifyData(Map<String, String[]> notifyData, TradeStatusProcessor processor) {
		if (!verifyNotifyData(notifyData)) {
			throw new RuntimeException("Verify notify data error");
		}
		SystemHolder.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				TradeStatus tradeStatus = TradeStatus.parse(notifyData.get("trade_status")[0]);
				String outTradeNo = notifyData.get("out_trade_no")[0];
				process(outTradeNo, processor, tradeStatus);
			}
		});
	}

	public TradeStatus queryTradeStatus(String outTradeNo) {
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(outTradeNo);

		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);

		try {
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if (!response.isSuccess()) {
				logger.debug("Response[code={}, msg={}, sub_code={}, sub_msg={}]", response.getCode(),
						response.getMsg(), response.getSubCode(), response.getSubMsg());
			}
			return TradeStatus.parse(response.getTradeStatus());
		} catch (AlipayApiException e) {
			logger.warn("Query trade status error", e);
			return null;
		}
	}

	public RefundStatus queryRefundStatus(String outTradeNo, String outRequestNo) {
		AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
		model.setOutTradeNo(outTradeNo);
		model.setOutRequestNo(outRequestNo);

		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);

		try {
			AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
			logger.debug("Response[code={}, msg={}, sub_code={}, sub_msg={}, body={}]", response.getCode(),
					response.getMsg(), response.getSubCode(), response.getSubMsg(), response.getBody());
			if (!response.isSuccess()) {
				logger.debug("Response[code={}, msg={}, sub_code={}, sub_msg={}]", response.getCode(),
						response.getMsg(), response.getSubCode(), response.getSubMsg());
				return RefundStatus.REFUNDING;
			}
			if (null != response.getBody()) {
				return RefundStatus.REFUND_SUCCESS;
			}
			return RefundStatus.REFUNDING;
		} catch (AlipayApiException e) {
			logger.warn("Query trade status error", e);
			return null;
		}
	}
}
