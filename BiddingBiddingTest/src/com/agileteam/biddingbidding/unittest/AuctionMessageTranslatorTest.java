package com.agileteam.biddingbidding.unittest;

import junit.framework.TestCase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;

import com.agileteam.biddingbidding.AuctionEventListener;
import com.agileteam.biddingbidding.AuctionEventListener.PriceSource;
import com.agileteam.biddingbidding.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest extends TestCase {
	public static final Chat UNUSED_CHAT = null;
	private static final String BIDDER_ID = "sniper";
	private Mockery context = new Mockery();
	private final AuctionEventListener listener = context
			.mock(AuctionEventListener.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(
			BIDDER_ID, listener);

	public void testNotifiesAuctionClosedWhenCloseMessageReceived() {

		context.checking(new Expectations() {
			{
				oneOf(listener).auctionClosed();
			}
		});

		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		translator.processMessage(UNUSED_CHAT, message);

		context.assertIsSatisfied();
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
		context.checking(new Expectations() {
			{
				exactly(1).of(listener).currentPrice(192, 7,
						PriceSource.FromOtherBidder);
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		translator.processMessage(UNUSED_CHAT, message);
		context.assertIsSatisfied();
	}

	public void testNotifiesBidDetailsWhenCurrentPriceMessageReceived() {
		context.checking(new Expectations() {
			{
				exactly(1).of(listener).currentPrice(234, 5,
						PriceSource.FromSelf);
			}
		});
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: "
				+ BIDDER_ID + ";");
		translator.processMessage(UNUSED_CHAT, message);

		context.assertIsSatisfied();
	}
}
