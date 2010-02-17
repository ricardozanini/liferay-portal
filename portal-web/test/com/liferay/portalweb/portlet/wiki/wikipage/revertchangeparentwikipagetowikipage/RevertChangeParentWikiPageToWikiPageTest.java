/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
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

package com.liferay.portalweb.portlet.wiki.wikipage.revertchangeparentwikipagetowikipage;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="RevertChangeParentWikiPageToWikiPageTest.java.html"><b><i>View
 * Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 */
public class RevertChangeParentWikiPageToWikiPageTest extends BaseTestCase {
	public void testRevertChangeParentWikiPageToWikiPage()
		throws Exception {
		selenium.open("/web/guest/home/");

		for (int second = 0;; second++) {
			if (second >= 60) {
				fail("timeout");
			}

			try {
				if (selenium.isElementPresent("link=Wiki Test Page")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("link=Wiki Test Page", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		selenium.click(RuntimeVariables.replace("link=All Pages"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=Wiki2 Page2 Test2", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("link=Wiki1 Page1 Test1"));
		selenium.clickAt("link=Wiki1 Page1 Test1", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Wiki2 Page2 Test2"),
			selenium.getText(
				"//ul[@class='breadcrumbs lfr-component']/li[3]/span/a"));
		assertEquals(RuntimeVariables.replace("Wiki1 Page1 Test1"),
			selenium.getText(
				"//ul[@class='breadcrumbs lfr-component']/li[4]/span/a"));
		assertEquals(RuntimeVariables.replace("This is a wiki1 page1 test1."),
			selenium.getText("//div[@class='wiki-body']"));
		selenium.clickAt("link=Details", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=History", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Changed parent from \".\""),
			selenium.getText(
				"//tr[@class='portlet-section-body results-row']/td[7]"));
		selenium.clickAt("link=Revert", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Reverted to 1.0"),
			selenium.getText(
				"//tr[@class='portlet-section-body results-row']/td[7]"));
		selenium.open("/web/guest/home/");

		for (int second = 0;; second++) {
			if (second >= 60) {
				fail("timeout");
			}

			try {
				if (selenium.isElementPresent("link=Wiki Test Page")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("link=Wiki Test Page", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		selenium.click(RuntimeVariables.replace("link=All Pages"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=Wiki2 Page2 Test2", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertFalse(selenium.isElementPresent("link=Wiki1 Page1 Test1"));
		selenium.open("/web/guest/home/");

		for (int second = 0;; second++) {
			if (second >= 60) {
				fail("timeout");
			}

			try {
				if (selenium.isElementPresent("link=Wiki Test Page")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("link=Wiki Test Page", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		selenium.click(RuntimeVariables.replace("link=All Pages"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=Wiki1 Page1 Test1", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Wiki1 Page1 Test1"),
			selenium.getText(
				"//ul[@class='breadcrumbs lfr-component']/li[3]/span/a"));
		assertEquals(RuntimeVariables.replace("This is a wiki1 page1 test1."),
			selenium.getText("//div[@class='wiki-body']"));
	}
}