package com.tambo.btc;

import java.util.List;

import org.bitcoinj.core.AbstractBlockChain.NewBlockType;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.BlockChainListener;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;

public class BlockChainTest {

	public static void main(String[] args) throws BlockStoreException, InterruptedException {
		NetworkParameters networkParameters = MainNetParams.get();
		BlockChain chain = new BlockChain(networkParameters, new MemoryBlockStore(networkParameters));

		chain.addListener(new BlockChainListener() {

			public void reorganize(StoredBlock splitPoint, List<StoredBlock> oldBlocks, List<StoredBlock> newBlocks)
					throws VerificationException {
				System.out.println("reorganize");

			}

			public void receiveFromBlock(Transaction tx, StoredBlock block, NewBlockType blockType,
					int relativityOffset) throws VerificationException {
				System.out.println("receiveFromBlock");

			}

			public boolean notifyTransactionIsInBlock(Sha256Hash txHash, StoredBlock block, NewBlockType blockType,
					int relativityOffset) throws VerificationException {
				System.out.println("notifyTxIsInBlock");
				return false;
			}

			public void notifyNewBestBlock(StoredBlock block) throws VerificationException {
				System.out.println("notifyNewBestBlock, blockheight=" + block.getHeight());

			}

			public boolean isTransactionRelevant(Transaction tx) throws ScriptException {
				if (tx.getMemo() != null) {
					System.out.println("isTxRelevant, memo=" + tx.getMemo());
				}
				return false;
			}
		});

		PeerGroup peerGroup = new PeerGroup(networkParameters, chain);
		peerGroup.setUserAgent("sample app", "0.1");
		peerGroup.addPeerDiscovery(new DnsDiscovery(networkParameters));
		peerGroup.start();

		while (peerGroup.numConnectedPeers() < 3) {
			System.out.println("waiting for more peers. current peer count=" + peerGroup.numConnectedPeers());
			Thread.sleep(500);
		}
		peerGroup.downloadBlockChain();
		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
