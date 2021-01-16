package com.proximapp.worktime.workingtime;

import com.proximapp.worktime.entity.Worktime;
import com.proximapp.worktime.repo.IWorktimeRepo;
import com.proximapp.worktime.repo.impl.ConnectionPool;
import com.proximapp.worktime.repo.impl.PostgresWorktimeRepo;
import com.proximapp.worktime.util.DatetimeManager;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WorktimeWS {

	private IWorktimeRepo worktimeRepo;

	@CrossOrigin
	@PostMapping("/worktimes/{companyId}")
	public Worktime notifyWorktime(@PathVariable("companyId") long companyId,
	                               @RequestParam(value = "user_id") long userId,
	                               @RequestParam(value = "date", defaultValue = "") String strDate,
	                               @RequestParam(value = "in_or_out") boolean inOrOut) {
		initRepos();
		Date date;
		if (strDate.isEmpty()) {
			date = new Date(System.currentTimeMillis());
		} else {
			try {
				date = DatetimeManager.parse(strDate);
			} catch (ParseException e) {
				throw new IllegalStateException("Invalid datetime from");
			}
		}
		if (inOrOut) {
			// IN
			Worktime lastWorktime = worktimeRepo.findLastUnclosedWorktimeByUser(companyId, userId);
			if (lastWorktime != null) {
				throw new IllegalStateException("Can't create new worktime if the last is still opened");
			}
			Worktime worktime = new Worktime();
			worktime.setCompanyId(companyId);
			worktime.setUserId(userId);
			worktime.setDateFrom(date);
			worktime.setDateTo(null);
			worktime = worktimeRepo.createWorktime(worktime);
			return worktime;
		} else {
			// OUT
			Worktime lastWorktime = worktimeRepo.findLastUnclosedWorktimeByUser(companyId, userId);
			if (lastWorktime == null) {
				throw new IllegalStateException("Last worktime already closed");
			}
			lastWorktime.setDateTo(date);
			lastWorktime = worktimeRepo.updateWorktime(lastWorktime);
			return lastWorktime;
		}
	}

	@CrossOrigin
	@GetMapping("/worktimes/{companyId}/query")
	public List<Worktime> findWorktimesByQuery(@PathVariable("companyId") long companyId,
	                                           @RequestParam(value = "user_ids", defaultValue = "") String strUserIds,
	                                           @RequestParam(value = "date_from", defaultValue = "") String strDateFrom,
	                                           @RequestParam(value = "date_to", defaultValue = "") String strDateTo) {
		initRepos();
		final String ID_LIST_REG_EXP = "((\\d+,)*\\d+|)";
		if (!strUserIds.matches(ID_LIST_REG_EXP))
			return null;
		Set<Long> userIds = strUserIds.isEmpty() ? new HashSet<>() :
				Arrays.stream(strUserIds.split(","))
						.map(Long::parseLong)
						.filter(possibleWorktimeId -> worktimeRepo.findWorktimeById(possibleWorktimeId) != null)
						.collect(Collectors.toSet());
		Date dateFrom = null;
		Date dateTo = null;
		if (!strDateFrom.isEmpty()) {
			try {
				dateFrom = DatetimeManager.parse(strDateFrom);
			} catch (ParseException e) {
				throw new IllegalStateException("Invalid dateFrom");
			}
		}
		if (!strDateTo.isEmpty()) {
			try {
				dateTo = DatetimeManager.parse(strDateTo);
			} catch (ParseException e) {
				throw new IllegalStateException("Invalid dateTo");
			}
		}
		return worktimeRepo.findWorktimesByQuery(companyId, userIds, dateFrom, dateTo);
	}

	@CrossOrigin
	@DeleteMapping("/worktimes/{companyId}/{worktimeId}")
	public boolean deleteWorktime(@PathVariable("worktimeId") long worktimeId,
	                              @PathVariable("companyId") long companyId) {
		initRepos();
		Worktime worktime = worktimeRepo.findWorktimeById(worktimeId);
		if (worktime == null || worktime.getCompanyId() != companyId) {
			return false;
		}
		return worktimeRepo.deleteWorktime(worktime.getId());
	}

	private void initRepos() {
		if (worktimeRepo == null) {
			Connection connection = ConnectionPool.getConnection();
			if (connection == null)
				throw new IllegalStateException();
			worktimeRepo = new PostgresWorktimeRepo(connection);
		}
	}

}
