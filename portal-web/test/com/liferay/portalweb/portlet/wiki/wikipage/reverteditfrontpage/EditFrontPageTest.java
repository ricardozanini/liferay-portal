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

package com.liferay.portalweb.portlet.wiki.wikipage.reverteditfrontpage;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="EditFrontPageTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 */
public class EditFrontPageTest extends BaseTestCase {
	public void testEditFrontPage() throws Exception {
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
		assertEquals(RuntimeVariables.replace("Test Wiki Article"),
			selenium.getText("//div[@class='wiki-body']/h2"));
		assertEquals(RuntimeVariables.replace("this is italics"),
			selenium.getText("//i"));
		assertEquals(RuntimeVariables.replace("bold"), selenium.getText("//b"));
		assertTrue(selenium.isElementPresent("link=Link to website"));
		assertEquals(RuntimeVariables.replace(
				"this is a list item this is a sub list item"),
			selenium.getText("//div[@class='wiki-body']/ul/li"));
		selenium.clickAt("link=Edit", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		selenium.type("_36_content",
			RuntimeVariables.replace(
				"==Test Wiki Article Edited==\n\n//this is italics. edited.//\n\n**bold edited**\n\n[[http://www.liferay.com|Link to website. Edited.]]\n\n*this is a list item. edited.\n**this is a sub list item. edited."));

		for (int second = 0;; second++) {
			if (second >= 60) {
				fail("timeout");
			}

			try {
				if (selenium.isVisible(
							"//span[@class='aui-icon-search aui-icon']")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("//input[@value='Save']", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Test Wiki Article Edited"),
			selenium.getText("//div[@class='wiki-body']/h2"));
		assertEquals(RuntimeVariables.replace("this is italics. edited."),
			selenium.getText("//i"));
		assertEquals(RuntimeVariables.replace("bold edited"),
			selenium.getText("//b"));
		assertTrue(selenium.isElementPresent("link=Link to website. Edited."));
		assertEquals(RuntimeVariables.replace(
				"this is a list item. edited. this is a sub list item. edited."),
			selenium.getText("//div[@class='wiki-body']/ul/li"));
		assertNotEquals(RuntimeVariables.replace("Test Wiki Article"),
			selenium.getText("//div[@class='wiki-body']/h2"));
		assertNotEquals(RuntimeVariables.replace("this is italics"),
			selenium.getText("//i"));
		assertNotEquals(RuntimeVariables.replace("bold"),
			selenium.getText("//b"));
		assertFalse(selenium.isElementPresent("link=Link to website"));
		assertNotEquals(RuntimeVariables.replace(
				"this is a list item this is a sub list item"),
			selenium.getText("//div[@class='wiki-body']/ul/li"));
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
		selenium.clickAt("link=Details", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=History", RuntimeVariables.replace(""));
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("link=1.2"));
		assertTrue(selenium.isElementPresent("link=1.1"));
		assertTrue(selenium.isElementPresent("link=1.0 (Minor Edit)"));
	}
}