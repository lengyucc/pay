package com.antbean.pay;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.logging.log4j.core.util.UuidUtil;

import com.antbean.pay.ali.Alipay;
import com.antbean.pay.ali.model.AppPayModel;
import com.antbean.pay.ali.model.RefundModel;
import com.antbean.pay.ali.processor.RefundStatusProcessor;
import com.antbean.pay.ali.processor.TradeStatusProcessor;

public class AlipayDemo {
	
	public static void main(String[] args) {
		final String APP_ID = "2018092061439655";
		final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCsvq7EV4oyD4c940im0CnSsGcEMHdZT2/M0dOqKeooe8W9i8u2Cz2rJtnOgW6pWDOjyRZ57/+/zflyG+D9kc8vfYHMqT6QcCzF/bs7SW9UpAFTVnSntsLi+bv6UG1mAsQzg+1wq3eUQG6RAeIET5iD2LF8ETKKgwGr7WJ0Hz/EvPmiSHXg2EVY2cwbeYUYa63v1OaW3AWwbHG/JYJhOD3IKNQjgBPJuqwAiR5QVALHeaQofk9KSq40msRtNdlLLQLthMFWNMScrY07f9MuJFODpuCbAbRmhGoBLRFLcct4+f/OKhgn5FBrnlyt8sXv37PEBuBrIChasmS+0/UF2j6DAgMBAAECggEBAKkNUutC4DNtjuS58PeO798IU0h+Rc5ZNwgyr/nvWwCTYEjG7m0RQyCjvLOjZzDC6IZ43E4o2RhT8rIBJoYAk8neAZDQcDHaFXcNgJXcn98tQT7VdQRMjxyhWZz16JnUSz4+9bc1lqi3BX1TYmTloFQnVEoeal+zJYejoJ4Tlsxoo0MUoGA2kKzMXgR9ebCV6PQCIrBd7x0YFdMPi0aNr9gBtNdW83j2OcyvJ5FidGtcYdxjMmjaYXL+3I7V3LrPFPzRb+cIxFPj1PrvEYTPIUbvgUk7z3ZL1aAzhr5Fy9sSzfsisWXpI0idIntCfCutL5CoXOfunHTBtB3wQM1vh0ECgYEA9V1UqpsjNlF3QTy9YCDZr1ua5Ys1Eb2Y6AnwXkqqztnu+8rIrPP0TgxhmSxldHColYRZPe9QS4nJ4Zt39W7H36i5qADY+dVYXJufov+6q0enRZxvQFqccIpX2PtCooT+v5Z9FZw9a/Q/jDjPAk9tWhnY563Z/29/2x38aVnhqe0CgYEAtDuIZHahrB8fUGYdAHnzacyrl4gfBRWCAOXh9uE40c073pGzPUJ/PjvGPOAyGexaBnanOl885BEX56Z2y353qMLufixa4nQmHlUTEf1tHQzEh7RXaHJc/Xi2WIaL6hM/lNiMGguP3VajZGXK3gq+Tctr1XwurBUhtLTIOSLqvC8CgYEAoHiv3PylpOrW4fXFrU8Oz99LiQDobnffvzJw4FpZJ/vkjwzq/iJ3D2yNkE429FaSC2eJrjYL/7tYK5Mwe3Y+hoqlEbmsLolXxwmI+GPkRA0S2zYalK4uTnRMS/7Yb84nhkn3+OA8U5Rk02J8EPi9fgYVlEBhqOvVZQtRf91m92kCgYEAktUteSts610vlcCbQBgaRseHxBwRqYKsNy8RuKAkOkiJzfnFnNLf90ObbVz5dEUp0ro340ZTxTpnz0G+5mpR8htdUU2xfWxC4dNNhgqBKnmMY0J5M454U8hURirXMgwccJc7DLz+p8Q9uT102RDHmXlwkKxrGabdnurpdCMOOeECgYAREBtFqQXCD1r489ZxSkhtCNpUF0t2RoQ8TmsQpJ7+uh0rvcKT8U7uu9d8Q5Nfa3z0aaSk8EAif8m2TG2nsyColYQicBB3IqqwcstADWG1XwYHPsuaJH+uf054wsIdDiIOqNuQGjAffHk5t71kj4BCIZamVbO10NEpal17cKRokA==";
		final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhG+sd8+3yTQFIpPKbcJ5Sm/hRvSpZDUTNy6aelH1maPSEiE4W/obrRwcNeXYUGk0KluqKmd8hCtGQUK93AbNS1UjPYVfqntSng2rRXiYDO8goOQkEGY6qbvyXM5Oc4VkRHLHXF4oEfdYP3V7KsOmGABrzqTrYOYa+VI6Aygf0/AagqVWya6nB6805TLm7C2zSHJxiAes+4MgJbsaaCwjbBU78xR2q0szbhrSQ7dO4dKKS516zMYp4MHMxYWRyr4HDrpKX4j1WVTUVJ5yryiPqo08lGsbI4YXdC2i/yMYbLh4ZFGsAuOLeljJ4+8YLhyU01lEsl3JDX907IV0mtC2rQIDAQAB";
		final String NOTIFY_URL = "http://api.devot.guaguaclub.com/alipay/notify.do";
		final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

		final Alipay alipay = new Alipay(APP_ID, APP_PRIVATE_KEY, ALIPAY_PUBLIC_KEY, NOTIFY_URL, SERVER_URL);

		String[] ss = {
				"162-303-1",
				"163-303-2",
				"194-303-23",
				"194-303-24",
				"191-303-25",
				"181-303-26",
				"195-303-27",
				"196-303-28",
				"196-303-29",
				"195-303-30",
				"197-303-31",
				"197-303-32",
				"193-303-33",
				"190-303-34",
				"189-303-35",
				"198-303-36",
				"198-303-37",
				"199-303-38",
				"199-303-39",
				"178-303-40",
				"177-303-41",
				"176-303-42",
				"175-303-43"
		};
		
		for (int i = 0; i < ss.length; i++) {
			String s = ss[i];
			
			try {
				alipay.createRefund(new RefundModel(s, UuidUtil.getTimeBasedUuid().toString(), "0.01"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	public static void main2(String[] args) {
		final String APP_ID = "2018092061439655";
		final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCsvq7EV4oyD4c940im0CnSsGcEMHdZT2/M0dOqKeooe8W9i8u2Cz2rJtnOgW6pWDOjyRZ57/+/zflyG+D9kc8vfYHMqT6QcCzF/bs7SW9UpAFTVnSntsLi+bv6UG1mAsQzg+1wq3eUQG6RAeIET5iD2LF8ETKKgwGr7WJ0Hz/EvPmiSHXg2EVY2cwbeYUYa63v1OaW3AWwbHG/JYJhOD3IKNQjgBPJuqwAiR5QVALHeaQofk9KSq40msRtNdlLLQLthMFWNMScrY07f9MuJFODpuCbAbRmhGoBLRFLcct4+f/OKhgn5FBrnlyt8sXv37PEBuBrIChasmS+0/UF2j6DAgMBAAECggEBAKkNUutC4DNtjuS58PeO798IU0h+Rc5ZNwgyr/nvWwCTYEjG7m0RQyCjvLOjZzDC6IZ43E4o2RhT8rIBJoYAk8neAZDQcDHaFXcNgJXcn98tQT7VdQRMjxyhWZz16JnUSz4+9bc1lqi3BX1TYmTloFQnVEoeal+zJYejoJ4Tlsxoo0MUoGA2kKzMXgR9ebCV6PQCIrBd7x0YFdMPi0aNr9gBtNdW83j2OcyvJ5FidGtcYdxjMmjaYXL+3I7V3LrPFPzRb+cIxFPj1PrvEYTPIUbvgUk7z3ZL1aAzhr5Fy9sSzfsisWXpI0idIntCfCutL5CoXOfunHTBtB3wQM1vh0ECgYEA9V1UqpsjNlF3QTy9YCDZr1ua5Ys1Eb2Y6AnwXkqqztnu+8rIrPP0TgxhmSxldHColYRZPe9QS4nJ4Zt39W7H36i5qADY+dVYXJufov+6q0enRZxvQFqccIpX2PtCooT+v5Z9FZw9a/Q/jDjPAk9tWhnY563Z/29/2x38aVnhqe0CgYEAtDuIZHahrB8fUGYdAHnzacyrl4gfBRWCAOXh9uE40c073pGzPUJ/PjvGPOAyGexaBnanOl885BEX56Z2y353qMLufixa4nQmHlUTEf1tHQzEh7RXaHJc/Xi2WIaL6hM/lNiMGguP3VajZGXK3gq+Tctr1XwurBUhtLTIOSLqvC8CgYEAoHiv3PylpOrW4fXFrU8Oz99LiQDobnffvzJw4FpZJ/vkjwzq/iJ3D2yNkE429FaSC2eJrjYL/7tYK5Mwe3Y+hoqlEbmsLolXxwmI+GPkRA0S2zYalK4uTnRMS/7Yb84nhkn3+OA8U5Rk02J8EPi9fgYVlEBhqOvVZQtRf91m92kCgYEAktUteSts610vlcCbQBgaRseHxBwRqYKsNy8RuKAkOkiJzfnFnNLf90ObbVz5dEUp0ro340ZTxTpnz0G+5mpR8htdUU2xfWxC4dNNhgqBKnmMY0J5M454U8hURirXMgwccJc7DLz+p8Q9uT102RDHmXlwkKxrGabdnurpdCMOOeECgYAREBtFqQXCD1r489ZxSkhtCNpUF0t2RoQ8TmsQpJ7+uh0rvcKT8U7uu9d8Q5Nfa3z0aaSk8EAif8m2TG2nsyColYQicBB3IqqwcstADWG1XwYHPsuaJH+uf054wsIdDiIOqNuQGjAffHk5t71kj4BCIZamVbO10NEpal17cKRokA==";
		final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhG+sd8+3yTQFIpPKbcJ5Sm/hRvSpZDUTNy6aelH1maPSEiE4W/obrRwcNeXYUGk0KluqKmd8hCtGQUK93AbNS1UjPYVfqntSng2rRXiYDO8goOQkEGY6qbvyXM5Oc4VkRHLHXF4oEfdYP3V7KsOmGABrzqTrYOYa+VI6Aygf0/AagqVWya6nB6805TLm7C2zSHJxiAes+4MgJbsaaCwjbBU78xR2q0szbhrSQ7dO4dKKS516zMYp4MHMxYWRyr4HDrpKX4j1WVTUVJ5yryiPqo08lGsbI4YXdC2i/yMYbLh4ZFGsAuOLeljJ4+8YLhyU01lEsl3JDX907IV0mtC2rQIDAQAB";
		final String NOTIFY_URL = "http://api.devot.guaguaclub.com/alipay/notify.do";
		final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

		final Alipay alipay = new Alipay(APP_ID, APP_PRIVATE_KEY, ALIPAY_PUBLIC_KEY, NOTIFY_URL, SERVER_URL);

		String outTradeNo = UUID.randomUUID().toString();

		outTradeNo = "165-303-4";
		BigDecimal payAmount = new BigDecimal("0.01");
		System.out.println(payAmount.toString());
		TradeStatusProcessor processor = new TradeStatusProcessor() {

			@Override
			public long onWaitBuyerPay(String outTradeNo) {
				System.out.println("等待支付");
				return 10000;
			}

			@Override
			public long onTradeSuccess(String outTradeNo) {
				System.out.println("交易成功");

				RefundModel _model = new RefundModel(outTradeNo, "123456789213123123", payAmount.toString());
				alipay.createRefund(_model);
				alipay.createRefundStatusQueryTask(_model.getOutTradeNo(), _model.getOutRequestNo(),
						new RefundStatusProcessor() {

							@Override
							public long onRefundSuccess(String outTradeNo, String outRequestNo) {
								System.out.println("退款成功");
								return 0;
							}

							@Override
							public long onNull(String outTradeNo, String outRequestNo) {
								System.out.println("等待退款");
								return 3000;
							}
						}, 10000);

				return 0;
			}

			@Override
			public long onTradeFinished(String outTradeNo) {
				System.out.println("交易结束");
				return 0;
			}

			@Override
			public long onTradeClosed(String outTradeNo) {
				System.out.println("交易关闭");
				return 0;
			}

			@Override
			public long onNull(String outTradeNo) {
				System.out.println("查询出错");
				return 15000;
			}
		};

		AppPayModel _model = new AppPayModel(outTradeNo, payAmount.toString(), "测试商品");

		String orderString = alipay.createAppPayTrade(_model);
		System.out.println("######### " + orderString);

		//
		alipay.createTradeStatusQueryTask(_model.getOutTradeNo(), processor, 30000);
	}
}
