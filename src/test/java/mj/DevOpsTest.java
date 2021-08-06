package mj;

import org.junit.Assert;
import org.junit.Test;

public class DevOpsTest {

	/**
	 * 既不是3也不是5的倍数，输出自己
	 */
	@Test
	public void testReturnSelf() {
		Assert.assertEquals("1",DevOps.printOne(1));
		Assert.assertEquals("2",DevOps.printOne(2));
		Assert.assertEquals("4",DevOps.printOne(4));
		Assert.assertEquals("46",DevOps.printOne(46));
	}
	/**
	 * 只是3的倍数，不是5的倍数，输出Dev
	 */
	@Test
	public void testReturnDev() {
		Assert.assertEquals("Dev",DevOps.printOne(3));
		Assert.assertEquals("Dev",DevOps.printOne(6));
		Assert.assertEquals("Dev",DevOps.printOne(99));
	}
	/**
	 * 只是5的倍数，不是3的倍数，输出Ops
	 */
	@Test
	public void testReturnOps() {
		Assert.assertEquals("Ops",DevOps.printOne(5));
		Assert.assertEquals("Ops",DevOps.printOne(25));
	}
	/**
	 * 既是3的倍数，也是5的倍数，输出DevOps
	 */
	@Test
	public void testReturnDevOps() {
		Assert.assertEquals("DevOps",DevOps.printOne(15));
		Assert.assertEquals("DevOps",DevOps.printOne(45));
	}
}
