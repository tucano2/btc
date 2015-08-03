package com.tambo.btc;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.UnreadableWalletException;
import org.bitcoinj.wallet.KeyChain.KeyPurpose;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Unit test for simple App.
 */
public class AppTest {

	String hashOfFirstTestnetBlock = "000000000933ea01ad0ee984209779baaec3ced90fa3f408719526f8d77f4943";

	@Test
	public void testGetBlock() {
		NetworkParameters networkParameters = TestNet3Params.get();

		BlockStore blockStore = new MemoryBlockStore(networkParameters);

		BlockChain chain;

		try {

			chain = new BlockChain(networkParameters, blockStore);
			Peer peer = new Peer(networkParameters, chain, new PeerAddress(InetAddress.getLocalHost()), "testApp1",
					"0.0.1");
			ListenableFuture<Peer> connectionOpenFuture = peer.getConnectionOpenFuture();

			connectionOpenFuture.addListener(new Thread() {
				@Override
				public void run() {
					System.out.println("got a listener...");
				}
			}, Executors.newCachedThreadPool());
			Peer peer2 = connectionOpenFuture.get();

		} catch (BlockStoreException bse) {
			bse.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testWallet() throws IOException {
		NetworkParameters networkParameters = TestNet3Params.get();
		Wallet wallet = null;
		File walletFile = new File("test.wallet");

		if (walletFile.exists()) {
			System.out.println("wallet already exists");

			wallet = new Wallet(networkParameters);

			for (int i = 0; i < 5; i++) {
				wallet.freshKey(KeyPurpose.AUTHENTICATION);
			}
			wallet.saveToFile(walletFile);
		} else {
			try {
				wallet.loadFromFile(walletFile, null);
			} catch (UnreadableWalletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(wallet);

		DeterministicKey authKey = wallet.getActiveKeychain().getKey(KeyPurpose.AUTHENTICATION);

		System.out.println(authKey);

		System.out.println("is it mine " + wallet.isPubKeyHashMine(authKey.getPubKeyHash()));

	}

	@Test
	@Ignore
	public void testAddress() {
		String net = "test";

		ECKey key = new ECKey();

		System.out.println("key is " + key);
		NetworkParameters netParams = TestNet3Params.get();

		// get valid btc address from pub key
		Address addressFromKey = key.toAddress(netParams);

		System.out.println("on the " + net + " net, we can use this address: " + addressFromKey);
	}
}
