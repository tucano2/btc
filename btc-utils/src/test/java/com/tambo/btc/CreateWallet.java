package com.tambo.btc;

import java.io.File;
import java.io.IOException;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.UnreadableWalletException;

public class CreateWallet {

	public static void main(String[] args) throws BlockStoreException, UnreadableWalletException, IOException {

		String walletPath = args[0];

		NetworkParameters networkParameters = TestNet3Params.get();
		Wallet wallet = new Wallet(networkParameters);

		File walletFile = new File(walletPath);
		if (walletFile.exists()) {
			System.out.println("loading wallet");
			wallet.loadFromFile(walletFile, null);
		} else {
			System.out.println("saving wallet");
			wallet.saveToFile(walletFile);
		}
		
		System.out.println(wallet.currentReceiveAddress());

		BlockChain chain = new BlockChain(networkParameters, wallet, new MemoryBlockStore(networkParameters));

		PeerGroup peerGroup = new PeerGroup(networkParameters, chain);
		peerGroup.addWallet(wallet);
		peerGroup.start();

	}

}
