/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchPluginSettingException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.PluginSetting;
import com.liferay.portal.model.impl.PluginSettingImpl;
import com.liferay.portal.model.impl.PluginSettingModelImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="PluginSettingPersistenceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PluginSettingPersistenceImpl extends BasePersistenceImpl
	implements PluginSettingPersistence {
	public PluginSetting create(long pluginSettingId) {
		PluginSetting pluginSetting = new PluginSettingImpl();

		pluginSetting.setNew(true);
		pluginSetting.setPrimaryKey(pluginSettingId);

		return pluginSetting;
	}

	public PluginSetting remove(long pluginSettingId)
		throws NoSuchPluginSettingException, SystemException {
		Session session = null;

		try {
			session = openSession();

			PluginSetting pluginSetting = (PluginSetting)session.get(PluginSettingImpl.class,
					new Long(pluginSettingId));

			if (pluginSetting == null) {
				if (_log.isWarnEnabled()) {
					_log.warn("No PluginSetting exists with the primary key " +
						pluginSettingId);
				}

				throw new NoSuchPluginSettingException(
					"No PluginSetting exists with the primary key " +
					pluginSettingId);
			}

			return remove(pluginSetting);
		}
		catch (NoSuchPluginSettingException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public PluginSetting remove(PluginSetting pluginSetting)
		throws SystemException {
		if (_listeners.length > 0) {
			for (ModelListener listener : _listeners) {
				listener.onBeforeRemove(pluginSetting);
			}
		}

		pluginSetting = removeImpl(pluginSetting);

		if (_listeners.length > 0) {
			for (ModelListener listener : _listeners) {
				listener.onAfterRemove(pluginSetting);
			}
		}

		return pluginSetting;
	}

	protected PluginSetting removeImpl(PluginSetting pluginSetting)
		throws SystemException {
		Session session = null;

		try {
			session = openSession();

			if (BatchSessionUtil.isEnabled()) {
				Object staleObject = session.get(PluginSettingImpl.class,
						pluginSetting.getPrimaryKeyObj());

				if (staleObject != null) {
					session.evict(staleObject);
				}
			}

			session.delete(pluginSetting);

			session.flush();

			return pluginSetting;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);

			FinderCacheUtil.clearCache(PluginSetting.class.getName());
		}
	}

	/**
	 * @deprecated Use <code>update(PluginSetting pluginSetting, boolean merge)</code>.
	 */
	public PluginSetting update(PluginSetting pluginSetting)
		throws SystemException {
		if (_log.isWarnEnabled()) {
			_log.warn(
				"Using the deprecated update(PluginSetting pluginSetting) method. Use update(PluginSetting pluginSetting, boolean merge) instead.");
		}

		return update(pluginSetting, false);
	}

	/**
	 * Add, update, or merge, the entity. This method also calls the model
	 * listeners to trigger the proper events associated with adding, deleting,
	 * or updating an entity.
	 *
	 * @param        pluginSetting the entity to add, update, or merge
	 * @param        merge boolean value for whether to merge the entity. The
	 *                default value is false. Setting merge to true is more
	 *                expensive and should only be true when pluginSetting is
	 *                transient. See LEP-5473 for a detailed discussion of this
	 *                method.
	 * @return        true if the portlet can be displayed via Ajax
	 */
	public PluginSetting update(PluginSetting pluginSetting, boolean merge)
		throws SystemException {
		boolean isNew = pluginSetting.isNew();

		if (_listeners.length > 0) {
			for (ModelListener listener : _listeners) {
				if (isNew) {
					listener.onBeforeCreate(pluginSetting);
				}
				else {
					listener.onBeforeUpdate(pluginSetting);
				}
			}
		}

		pluginSetting = updateImpl(pluginSetting, merge);

		if (_listeners.length > 0) {
			for (ModelListener listener : _listeners) {
				if (isNew) {
					listener.onAfterCreate(pluginSetting);
				}
				else {
					listener.onAfterUpdate(pluginSetting);
				}
			}
		}

		return pluginSetting;
	}

	public PluginSetting updateImpl(
		com.liferay.portal.model.PluginSetting pluginSetting, boolean merge)
		throws SystemException {
		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, pluginSetting, merge);

			pluginSetting.setNew(false);

			return pluginSetting;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);

			FinderCacheUtil.clearCache(PluginSetting.class.getName());
		}
	}

	public PluginSetting findByPrimaryKey(long pluginSettingId)
		throws NoSuchPluginSettingException, SystemException {
		PluginSetting pluginSetting = fetchByPrimaryKey(pluginSettingId);

		if (pluginSetting == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("No PluginSetting exists with the primary key " +
					pluginSettingId);
			}

			throw new NoSuchPluginSettingException(
				"No PluginSetting exists with the primary key " +
				pluginSettingId);
		}

		return pluginSetting;
	}

	public PluginSetting fetchByPrimaryKey(long pluginSettingId)
		throws SystemException {
		Session session = null;

		try {
			session = openSession();

			return (PluginSetting)session.get(PluginSettingImpl.class,
				new Long(pluginSettingId));
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<PluginSetting> findByCompanyId(long companyId)
		throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "findByCompanyId";
		String[] finderParams = new String[] { Long.class.getName() };
		Object[] finderArgs = new Object[] { new Long(companyId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append(
					"FROM com.liferay.portal.model.PluginSetting WHERE ");

				query.append("companyId = ?");

				query.append(" ");

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				List<PluginSetting> list = q.list();

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List<PluginSetting>)result;
		}
	}

	public List<PluginSetting> findByCompanyId(long companyId, int start,
		int end) throws SystemException {
		return findByCompanyId(companyId, start, end, null);
	}

	public List<PluginSetting> findByCompanyId(long companyId, int start,
		int end, OrderByComparator obc) throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "findByCompanyId";
		String[] finderParams = new String[] {
				Long.class.getName(),
				
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				new Long(companyId),
				
				String.valueOf(start), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append(
					"FROM com.liferay.portal.model.PluginSetting WHERE ");

				query.append("companyId = ?");

				query.append(" ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				List<PluginSetting> list = (List<PluginSetting>)QueryUtil.list(q,
						getDialect(), start, end);

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List<PluginSetting>)result;
		}
	}

	public PluginSetting findByCompanyId_First(long companyId,
		OrderByComparator obc)
		throws NoSuchPluginSettingException, SystemException {
		List<PluginSetting> list = findByCompanyId(companyId, 0, 1, obc);

		if (list.size() == 0) {
			StringBuilder msg = new StringBuilder();

			msg.append("No PluginSetting exists with the key {");

			msg.append("companyId=" + companyId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchPluginSettingException(msg.toString());
		}
		else {
			return list.get(0);
		}
	}

	public PluginSetting findByCompanyId_Last(long companyId,
		OrderByComparator obc)
		throws NoSuchPluginSettingException, SystemException {
		int count = countByCompanyId(companyId);

		List<PluginSetting> list = findByCompanyId(companyId, count - 1, count,
				obc);

		if (list.size() == 0) {
			StringBuilder msg = new StringBuilder();

			msg.append("No PluginSetting exists with the key {");

			msg.append("companyId=" + companyId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			throw new NoSuchPluginSettingException(msg.toString());
		}
		else {
			return list.get(0);
		}
	}

	public PluginSetting[] findByCompanyId_PrevAndNext(long pluginSettingId,
		long companyId, OrderByComparator obc)
		throws NoSuchPluginSettingException, SystemException {
		PluginSetting pluginSetting = findByPrimaryKey(pluginSettingId);

		int count = countByCompanyId(companyId);

		Session session = null;

		try {
			session = openSession();

			StringBuilder query = new StringBuilder();

			query.append("FROM com.liferay.portal.model.PluginSetting WHERE ");

			query.append("companyId = ?");

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY ");
				query.append(obc.getOrderBy());
			}

			Query q = session.createQuery(query.toString());

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(companyId);

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
					pluginSetting);

			PluginSetting[] array = new PluginSettingImpl[3];

			array[0] = (PluginSetting)objArray[0];
			array[1] = (PluginSetting)objArray[1];
			array[2] = (PluginSetting)objArray[2];

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public PluginSetting findByC_I_T(long companyId, String pluginId,
		String pluginType) throws NoSuchPluginSettingException, SystemException {
		PluginSetting pluginSetting = fetchByC_I_T(companyId, pluginId,
				pluginType);

		if (pluginSetting == null) {
			StringBuilder msg = new StringBuilder();

			msg.append("No PluginSetting exists with the key {");

			msg.append("companyId=" + companyId);

			msg.append(", ");
			msg.append("pluginId=" + pluginId);

			msg.append(", ");
			msg.append("pluginType=" + pluginType);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchPluginSettingException(msg.toString());
		}

		return pluginSetting;
	}

	public PluginSetting fetchByC_I_T(long companyId, String pluginId,
		String pluginType) throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "fetchByC_I_T";
		String[] finderParams = new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			};
		Object[] finderArgs = new Object[] {
				new Long(companyId),
				
				pluginId,
				
				pluginType
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append(
					"FROM com.liferay.portal.model.PluginSetting WHERE ");

				query.append("companyId = ?");

				query.append(" AND ");

				if (pluginId == null) {
					query.append("pluginId IS NULL");
				}
				else {
					query.append("pluginId = ?");
				}

				query.append(" AND ");

				if (pluginType == null) {
					query.append("pluginType IS NULL");
				}
				else {
					query.append("pluginType = ?");
				}

				query.append(" ");

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (pluginId != null) {
					qPos.add(pluginId);
				}

				if (pluginType != null) {
					qPos.add(pluginType);
				}

				List<PluginSetting> list = q.list();

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				if (list.size() == 0) {
					return null;
				}
				else {
					return list.get(0);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			List<PluginSetting> list = (List<PluginSetting>)result;

			if (list.size() == 0) {
				return null;
			}
			else {
				return list.get(0);
			}
		}
	}

	public List<Object> findWithDynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		Session session = null;

		try {
			session = openSession();

			dynamicQuery.compile(session);

			return dynamicQuery.list();
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<Object> findWithDynamicQuery(DynamicQuery dynamicQuery,
		int start, int end) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			dynamicQuery.setLimit(start, end);

			dynamicQuery.compile(session);

			return dynamicQuery.list();
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<PluginSetting> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	public List<PluginSetting> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	public List<PluginSetting> findAll(int start, int end, OrderByComparator obc)
		throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "findAll";
		String[] finderParams = new String[] {
				"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};
		Object[] finderArgs = new Object[] {
				String.valueOf(start), String.valueOf(end), String.valueOf(obc)
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append("FROM com.liferay.portal.model.PluginSetting ");

				if (obc != null) {
					query.append("ORDER BY ");
					query.append(obc.getOrderBy());
				}

				Query q = session.createQuery(query.toString());

				List<PluginSetting> list = null;

				if (obc == null) {
					list = (List<PluginSetting>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<PluginSetting>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, list);

				return list;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return (List<PluginSetting>)result;
		}
	}

	public void removeByCompanyId(long companyId) throws SystemException {
		for (PluginSetting pluginSetting : findByCompanyId(companyId)) {
			remove(pluginSetting);
		}
	}

	public void removeByC_I_T(long companyId, String pluginId, String pluginType)
		throws NoSuchPluginSettingException, SystemException {
		PluginSetting pluginSetting = findByC_I_T(companyId, pluginId,
				pluginType);

		remove(pluginSetting);
	}

	public void removeAll() throws SystemException {
		for (PluginSetting pluginSetting : findAll()) {
			remove(pluginSetting);
		}
	}

	public int countByCompanyId(long companyId) throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "countByCompanyId";
		String[] finderParams = new String[] { Long.class.getName() };
		Object[] finderArgs = new Object[] { new Long(companyId) };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portal.model.PluginSetting WHERE ");

				query.append("companyId = ?");

				query.append(" ");

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				Long count = null;

				Iterator<Long> itr = q.list().iterator();

				if (itr.hasNext()) {
					count = itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countByC_I_T(long companyId, String pluginId, String pluginType)
		throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "countByC_I_T";
		String[] finderParams = new String[] {
				Long.class.getName(), String.class.getName(),
				String.class.getName()
			};
		Object[] finderArgs = new Object[] {
				new Long(companyId),
				
				pluginId,
				
				pluginType
			};

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				StringBuilder query = new StringBuilder();

				query.append("SELECT COUNT(*) ");
				query.append(
					"FROM com.liferay.portal.model.PluginSetting WHERE ");

				query.append("companyId = ?");

				query.append(" AND ");

				if (pluginId == null) {
					query.append("pluginId IS NULL");
				}
				else {
					query.append("pluginId = ?");
				}

				query.append(" AND ");

				if (pluginType == null) {
					query.append("pluginType IS NULL");
				}
				else {
					query.append("pluginType = ?");
				}

				query.append(" ");

				Query q = session.createQuery(query.toString());

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (pluginId != null) {
					qPos.add(pluginId);
				}

				if (pluginType != null) {
					qPos.add(pluginType);
				}

				Long count = null;

				Iterator<Long> itr = q.list().iterator();

				if (itr.hasNext()) {
					count = itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public int countAll() throws SystemException {
		boolean finderClassNameCacheEnabled = PluginSettingModelImpl.CACHE_ENABLED;
		String finderClassName = PluginSetting.class.getName();
		String finderMethodName = "countAll";
		String[] finderParams = new String[] {  };
		Object[] finderArgs = new Object[] {  };

		Object result = null;

		if (finderClassNameCacheEnabled) {
			result = FinderCacheUtil.getResult(finderClassName,
					finderMethodName, finderParams, finderArgs, this);
		}

		if (result == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(
						"SELECT COUNT(*) FROM com.liferay.portal.model.PluginSetting");

				Long count = null;

				Iterator<Long> itr = q.list().iterator();

				if (itr.hasNext()) {
					count = itr.next();
				}

				if (count == null) {
					count = new Long(0);
				}

				FinderCacheUtil.putResult(finderClassNameCacheEnabled,
					finderClassName, finderMethodName, finderParams,
					finderArgs, count);

				return count.intValue();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}
		else {
			return ((Long)result).intValue();
		}
	}

	public void registerListener(ModelListener listener) {
		List<ModelListener> listeners = ListUtil.fromArray(_listeners);

		listeners.add(listener);

		_listeners = listeners.toArray(new ModelListener[listeners.size()]);
	}

	public void unregisterListener(ModelListener listener) {
		List<ModelListener> listeners = ListUtil.fromArray(_listeners);

		listeners.remove(listener);

		_listeners = listeners.toArray(new ModelListener[listeners.size()]);
	}

	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.portal.util.PropsUtil.get(
						"value.object.listener.com.liferay.portal.model.PluginSetting")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener> listeners = new ArrayList<ModelListener>();

				for (String listenerClassName : listenerClassNames) {
					listeners.add((ModelListener)Class.forName(
							listenerClassName).newInstance());
				}

				_listeners = listeners.toArray(new ModelListener[listeners.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	private static Log _log = LogFactory.getLog(PluginSettingPersistenceImpl.class);
	private ModelListener[] _listeners = new ModelListener[0];
}