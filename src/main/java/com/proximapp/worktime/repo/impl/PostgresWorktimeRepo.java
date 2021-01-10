package com.proximapp.worktime.repo.impl;

import com.proximapp.worktime.entity.Worktime;
import com.proximapp.worktime.repo.IWorktimeRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

public class PostgresWorktimeRepo implements IWorktimeRepo {

	private static final String queryAddWorktime = "INSERT INTO public.worktime(companyId, userId, dateFrom, dateTo) VALUES (?, ?, ?, ?);";
	private static final String queryUpdateWorktime = "UPDATE worktime SET dateto = ? WHERE worktimeid =?";
	private static final String findWorktimeById = "SELECT worktime.worktimeid, worktime.companyid, worktime.userid, worktime.datefrom, worktime.dateto " +
			"FROM public.worktime " +
			"WHERE worktimeid =?";
	private static final String findLastEntry = "SELECT worktime.worktimeid, worktime.companyid, worktime.userid, worktime.datefrom, worktime.dateto " +
			"FROM public.worktime " +
			"WHERE companyid =? AND userid =? AND dateto IS NULL";
	private static final String deleteWorktime = "DELETE FROM public.worktime WHERE worktime.worktimeid=?";

	private final Connection connection;

	public PostgresWorktimeRepo(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Worktime createWorktime(Worktime worktime) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(queryAddWorktime);
			preparedStatement.setLong(1, worktime.getCompanyId());
			preparedStatement.setLong(2, worktime.getUserId());
			preparedStatement.setTimestamp(3, new Timestamp(worktime.getDateFrom().getTime()));
			if (worktime.getDateTo() != null)
				preparedStatement.setTimestamp(4, new Timestamp(worktime.getDateTo().getTime()));
			else
				preparedStatement.setTimestamp(4, null);
			preparedStatement.executeUpdate();
			return findWorktimeById(preparedStatement.getGeneratedKeys().getLong(1));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Worktime findWorktimeById(long worktimeId) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(findWorktimeById);
			preparedStatement.setLong(1, worktimeId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next())
				return null;
			Worktime worktime = new Worktime();
			worktime.setId(resultSet.getLong(1));
			worktime.setCompanyId(resultSet.getLong(2));
			worktime.setUserId(resultSet.getLong(3));
			worktime.setDateFrom(new Date(resultSet.getTimestamp(4).getTime()));
			worktime.setDateTo(new Date(resultSet.getTimestamp(5).getTime()));
			return worktime;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Worktime> findWorktimesByQuery(long companyId, Set<Long> userIds, Date dateFrom, Date dateTo) {
		try {
			String findWorktimesByQuery = "SELECT worktime.worktimeid, worktime.companyid, worktime.userid, worktime.datefrom, worktime.dateto " +
					"FROM public.worktime " +
					"WHERE companyid =? AND datefrom >= ? AND dateto <= ? ";
			if (userIds.size() > 0) {
				findWorktimesByQuery += "AND userid IN(?";
				for (int i = 1; i < userIds.size(); i++) {
					findWorktimesByQuery += ",?";
				}
				findWorktimesByQuery += ")";
			}
			List<Worktime> worktimes = new LinkedList<>();
			PreparedStatement preparedStatement = connection.prepareStatement(findWorktimesByQuery);
			preparedStatement.setLong(1, companyId);
			preparedStatement.setTimestamp(2, new Timestamp(dateFrom.getTime()));
			if (dateTo == null) {
				dateTo = new Date(System.currentTimeMillis());
			}
			preparedStatement.setTimestamp(3, new Timestamp(dateTo.getTime()));
			int userIdsParameterIndex = 3;
			Iterator<Long> it = userIds.iterator();
			while (it.hasNext()) {
				userIdsParameterIndex++;
				preparedStatement.setLong(userIdsParameterIndex, it.next());
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Worktime worktime = new Worktime();
				worktime.setId(resultSet.getLong(1));
				worktime.setCompanyId(resultSet.getLong(2));
				worktime.setUserId(resultSet.getLong(3));
				worktime.setDateFrom(new Date(resultSet.getTimestamp(4).getTime()));
				worktime.setDateTo(new Date(resultSet.getTimestamp(5).getTime()));
				worktimes.add(worktime);
			}
			return worktimes;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Worktime findLastUnclosedWorktimeByUser(long companyID, long userId) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(findLastEntry);
			preparedStatement.setLong(1, companyID);
			preparedStatement.setLong(2, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			Worktime worktime = new Worktime();
			worktime.setId(resultSet.getLong(1));
			worktime.setCompanyId(resultSet.getLong(2));
			worktime.setUserId(resultSet.getLong(3));
			worktime.setDateFrom(new Date(resultSet.getTimestamp(4).getTime()));
			return worktime;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean deleteWorktime(long worktimeId) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(deleteWorktime);
			preparedStatement.setLong(1, worktimeId);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Worktime updateWorktime(Worktime worktime) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(queryUpdateWorktime);
			preparedStatement.setTimestamp(1, new Timestamp(worktime.getDateTo().getTime()));
			preparedStatement.setLong(2, worktime.getId());
			preparedStatement.executeUpdate();
			return worktime;
		} catch (Exception e) {
			return null;
		}
	}

}
