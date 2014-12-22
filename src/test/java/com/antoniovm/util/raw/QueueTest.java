/**
 * 
 */
package com.antoniovm.util.raw;

import org.junit.Assert;
import org.junit.Test;

import com.antoniovm.util.event.DataListener;


/**
 * 
 * @author Antonio Vicente Martin
 *
 */
public class QueueTest {

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testNewQueueArrayIndexOutOfBoundsException() {
		byte[] test = { 1, 2, 0, 0, 0 };
		new Queue(test, 6);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPushIllegalArgumentException() {
		byte[] test = { 1, 2, 0, 0, 0 };
		Queue qTest = new Queue(test, test.length);
		qTest.push(test, 4, 2);
	}

	@Test
	public void testPushDisplacement() {
		byte[] test = { 1, 2, 3, 4, 5 };
		byte[] newData = { 6, 7 };
		byte[] expected = { 3, 4, 5, 6, 7 };

		Queue.push(newData, test, test.length);

		Assert.assertArrayEquals(expected, test);
	}

	@Test
	public void testPushNoDisplacement() {
		byte[] test = { 1, 2, 0, 0, 0 };
		byte[] newData = { 3, 4 };
		byte[] expected = { 1, 2, 3, 4, 0 };

		Queue.push(newData, test, 2);

		Assert.assertArrayEquals(expected, test);
	}

	@Test
	public void testPushNoTruncate() {
		byte[] test = { 1, 2, 3, 4, 5 };
		Queue qTest = new Queue(test, test.length, true);
		byte[] newData = { 6, 7 };
		byte[] expected = { 3, 4, 5, 6, 7 };

		qTest.push(newData);

		Assert.assertArrayEquals(expected, qTest.getRawData());
	}

	@Test
	public void testPushTruncate() {
		byte[] test = { 1, 2, 3 };
		byte[] newData = { 6, 7, 8, 9 };
		byte[] expected = { 7, 8, 9 };

		Queue.push(newData, test, test.length);

		Assert.assertArrayEquals(expected, test);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPushBadIndexes() {
		byte[] test = { 1, 2, 3 };
		byte[] newData = {};

		Queue.push(newData, test, test.length);

	}

	@Test
	public void testPeek() {
		byte[] test = { 1, 2 };
		byte[] peekedData = new byte[3];
		byte[] expected = { 1, 2, 0 };

		Queue qTest = new Queue(test, test.length);
		qTest.peek(peekedData);
		qTest.pop(peekedData);

		Assert.assertArrayEquals(expected, peekedData);
	}

	@Test
	public void testPop() {
		byte[] test = { 1, 2, 3, 4, 5, 6 };
		byte[] removedData = new byte[2];
		byte[] expected = { 5, 6, 0, 0, 0, 0 };

		Queue qTest = new Queue(test, test.length);
		qTest.pop(removedData);
		qTest.pop(removedData);

		Assert.assertArrayEquals(expected, qTest.getRawData());
		Assert.assertEquals(2, qTest.getSize());
	}

	@Test
	public void testOnFull() {

		final boolean[] onFullCalled = { false };

		Queue qTest = new Queue(5);
		qTest.addDataListener(new DataListener() {

			@Override
			public void onFull() {
				onFullCalled[0] = true;
			}

			@Override
			public void onEmpty() {
			}

		});
		byte[] test = { 1, 2, 3, 4, 5, 6 };

		qTest.push(test);

		Assert.assertEquals(true, onFullCalled[0]);
	}

	@Test
	public void testOnEmpty() {

		final boolean[] onEmptyCalled = { false };
		byte[] removedData = new byte[2];
		byte[] test = { 1, 2, 3 };
		Queue qTest = new Queue(test, test.length);
		qTest.addDataListener(new DataListener() {

			@Override
			public void onFull() {
			}

			@Override
			public void onEmpty() {
				onEmptyCalled[0] = true;
			}

		});


		qTest.pop(removedData);
		qTest.pop(removedData);

		Assert.assertEquals(true, onEmptyCalled[0]);
	}


}
