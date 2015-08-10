package com.tambo.btc;

import org.bitcoinj.core.Address;

public class GetReceiveAddress extends WalletAction {

	public GetReceiveAddress(String walletPath) throws Exception {
		super(walletPath);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new GetReceiveAddress(parseCmdParams(args)).getReceiveAddress());
	}

	public String getReceiveAddress() {
		Address currentReceiveAddress = getWallet().currentReceiveAddress();
		return currentReceiveAddress.toString();

	}

}
