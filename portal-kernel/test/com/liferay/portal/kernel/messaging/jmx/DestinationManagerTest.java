/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.kernel.messaging.jmx;

import com.liferay.portal.kernel.messaging.Destination;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import junit.framework.TestCase;

import org.jmock.integration.junit4.JUnit4Mockery;

/**
 * <a href="DestinationManagerTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael C. Han
 *
 */
public class DestinationManagerTest extends TestCase {

	public void testRegisterMBean() throws Exception {
		_mBeanServer.registerMBean(
			new DestinationManager(_mockery.mock(Destination.class)),
			DestinationManager.createObjectName("test"));

		assertTrue(
			_mBeanServer.isRegistered(
				DestinationManager.createObjectName("test")));
	}

	protected void setUp() {
		_mockery = new JUnit4Mockery();
		_mBeanServer = ManagementFactory.getPlatformMBeanServer();
	}

	private JUnit4Mockery _mockery;
	private MBeanServer _mBeanServer;

}