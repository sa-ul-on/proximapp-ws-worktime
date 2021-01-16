package com.proximapp.worktime.repo.impl;

import com.proximapp.worktime.entity.Worktime;
import com.proximapp.worktime.repo.IWorktimeRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

public class PostgresWorktimeRepo implements IWorktimeRepo {

	private final Connection connection;

	public PostgresWorktimeRepo(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Worktime createWorktime(Worktime worktime) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO worktime(companyId, " +
					"userId, dateFrom, dateTo) VALUES (?, ?, ?, ?) RETURNING worktimeid;");
			preparedStatement.setLong(1, worktime.getCompanyId());
			preparedStatement.setLong(2, worktime.getUserId());
			preparedStatement.setTimestamp(3, new Timestamp(worktime.getDateFrom().getTime()));
			if (worktime.getDateTo() != null)
				preparedStatement.setTimestamp(4, new Timestamp(worktime.getDateTo().getTime()));
			else
				preparedStatement.setTimestamp(4, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			long lastInsertedId = resultSet.getLong(1);
			return findWorktimeById(lastInsertedId);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Worktime findWorktimeById(long worktimeId) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT worktimeid, companyid, " +
					"userid, datefrom, dateto FROM worktime WHERE worktimeid = ?");
			preparedStatement.setLong(1, worktimeId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next())
				return null;
			Worktime worktime = new Worktime();
			worktime.setId(resultSet.getLong(1));
			worktime.setCompanyId(resultSet.getLong(2));
			worktime.setUserId(resultSet.getLong(3));
			worktime.setDateFrom(new Date(resultSet.getTimestamp(4).getTime()));
			if (resultSet.getTimestamp(5) != null)
				worktime.setDateTo(new Date(resultSet.getTimestamp(5).getTime()));
			return worktime;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Worktime> findWorktimesByQuery(long companyId, Set<Long> userIds, Date dateFrom, Date dateTo) {
		try {
			String findWorktimesByQuery = "SELECT worktime.worktimeid, worktime.companyid, worktime.userid, " +
					"worktime.datefrom, worktime.dateto FROM worktime WHERE companyid = ?";
			if (dateFrom != null)
				findWorktimesByQuery += " AND datefrom >= ?";
			if (dateTo != null)
				findWorktimesByQuery += " AND dateto <= ?";
			if (userIds != null && userIds.size() > 0)
				findWorktimesByQuery += " AND userid IN (?" + ", ?".repeat(userIds.size() - 1) + ")";
			PreparedStatement preparedStatement = connection.prepareStatement(findWorktimesByQuery);
			int i = 1;
			preparedStatement.setLong(i++, companyId);
			if (dateFrom != null)
				preparedStatement.setTimestamp(i++, new Timestamp(dateFrom.getTime()));
			if (dateTo != null)
				preparedStatement.setTimestamp(i++, new Timestamp(dateTo.getTime()));
			if (userIds != null && userIds.size() > 0)
				for (long userId : userIds)
					preparedStatement.setLong(i++, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Worktime> worktimes = new LinkedList<>();
			while (resultSet.next()) {
				Worktime worktime = new Worktime();
				worktime.setId(resultSet.getLong(1));
				worktime.setCompanyId(resultSet.getLong(2));
				worktime.setUserId(resultSet.getLong(3));
				worktime.setDateFrom(new Date(resultSet.getTimestamp(4).getTime()));
				if (resultSet.getTimestamp(5) != null)
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
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT worktimeid, companyid, " +
					"userid, datefrom, dateto FROM worktime WHERE companyid = ? AND userid = ? AND dateto IS NULL");
			preparedStatement.setLong(1, companyID);
			preparedStatement.setLong(2, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next())
				return null;
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
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM worktime WHERE " +
					"worktimeid = ?");
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
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE worktime SET dateto = ? " +
					"WHERE worktimeid = ?");
			preparedStatement.setTimestamp(1, new Timestamp(worktime.getDateTo().getTime()));
			preparedStatement.setLong(2, worktime.getId());
			preparedStatement.executeUpdate();
			return worktime;
		} catch (Exception e) {
			return null;
		}
	}

}
