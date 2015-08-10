package com.tambo.btc;

import java.io.File;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.MemoryBlockStore;

public abstract class WalletAction {

	private final Wallet wallet;

	public WalletAction(String walletPath) throws Exception {
		NetworkParameters networkParameters = TestNet3Params.get();
		wallet = new Wallet(networkParameters);

		File walletFile = new File(walletPath);
		if (walletFile.exists()) {
			System.out.println("loading wallet");
			wallet.loadFromFile(walletFile, null);
		} else {
			throw new IllegalStateException("wallet not found");
		}

		BlockChain chain = new BlockChain(networkParameters, wallet, new MemoryBlockStore(networkParameters));

		PeerGroup peerGroup = new PeerGroup(networkParameters, chain);
		peerGroup.setUserAgent("sample app", "0.1");
		peerGroup.addPeerDiscovery(new DnsDiscovery(networkParameters));
		peerGroup.addWallet(wallet);
		peerGroup.start();

		while (peerGroup.numConnectedPeers() < 3) {
			System.out.println("waiting for more peers. current peer count=" + peerGroup.numConnectedPeers());
			Thread.sleep(500);
		}
		System.out.println("got this many peers=" + peerGroup.numConnectedPeers());
	}

	static String parseCmdParams(String[] args) {
		if (args == null || args.length < 1) {
			throw new IllegalStateException("Usage: <full wallet file path>");
		}
		return args[0];
	}

	Wallet getWallet() {
		return wallet;
	}

}
