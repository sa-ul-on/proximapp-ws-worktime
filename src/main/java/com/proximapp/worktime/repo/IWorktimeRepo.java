package com.proximapp.worktime.repo;

import com.proximapp.worktime.entity.Worktime;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IWorktimeRepo {

	Worktime createWorktime(Worktime worktime);

	Worktime findWorktimeById(long worktimeId);

	List<Worktime> findWorktimesByQuery(long companyID, Set<Long> userIds, Date dateFrom, Date dateTo);

	Worktime findLastUnclosedWorktimeByUser(long companyID, long userId);

	Worktime updateWorktime(Worktime worktime);

	boolean deleteWorktime(long worktimeId);

}
