package com.tambo.btc;

public class CheckBalance extends WalletAction {

	public CheckBalance(String walletPath) throws Exception {
		super(walletPath);
	}

	public static void main(String[] args) throws Exception {
		CheckBalance checkBalance = new CheckBalance(WalletAction.parseCmdParams(args));
		System.out.println(checkBalance.getBalance());
	}

	private String getBalance() {
		return getWallet().getBalance().toFriendlyString();
	}

}
