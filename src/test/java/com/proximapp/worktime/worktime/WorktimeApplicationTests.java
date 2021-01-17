package com.proximapp.worktime.worktime;

import com.proximapp.worktime.entity.Worktime;
import com.proximapp.worktime.workingtime.WorktimeWS;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
class WorktimeApplicationTests {

	@Test
	void contextLoads() throws ParseException {
		long companyId = 1;
		WorktimeWS worktimeWS = new WorktimeWS();
		Worktime worktimeTest, worktime1, worktime2, worktime3, worktime4;
		worktime1 = new Worktime();
		worktime2 = new Worktime();
		worktime3 = new Worktime();
		assert worktimeWS.notifyWorktime(companyId, 1, "2021-01-17 08:30:00", true) != null;
		assert (worktime1 = worktimeWS.notifyWorktime(companyId, 1, "2021-01-17 13:30:00", false)) != null;
		assert worktimeWS.notifyWorktime(companyId, 1, "2021-01-17 14:30:00", true) != null;
		assert (worktime2 = worktimeWS.notifyWorktime(companyId, 1, "2021-01-17 17:30:00", false)) != null;
		assert worktimeWS.notifyWorktime(companyId, 2, "", true) != null;
		assert (worktime3 = worktimeWS.notifyWorktime(companyId, 2, "", false)) != null;

		worktime4 = new Worktime();
		try {
			worktimeWS.notifyWorktime(companyId, 1, "2021-01-18 08:30:00", true);
		} catch (IllegalStateException e) {
			assert false;
		}
		try {
			worktime4 = worktimeWS.notifyWorktime(companyId, 1, "2021-01-18 18:30:00", false);
		} catch (IllegalStateException e) {
			assert false;
		}
		try {
			worktimeWS.notifyWorktime(companyId, 1, "2021-01-18 08:30:00", false);
		} catch (IllegalStateException e) {
			assert true;
		}

		worktimeTest = new Worktime();
		worktimeTest.setId(worktime2.getId());
		worktimeTest.setCompanyId(companyId);
		worktimeTest.setUserId(1);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		worktimeTest.setDateFrom(format.parse("2021-01-17 14:30:00"));
		worktimeTest.setDateTo(format.parse("2021-01-17 17:30:00"));
		assert (worktime2.getCompanyId() == worktimeTest.getCompanyId()
				&& worktime2.getCompanyId() == worktimeTest.getCompanyId()
				&& worktime2.getUserId() == worktimeTest.getUserId()
				&& worktime2.getDateFrom().equals(worktimeTest.getDateFrom())
				&& worktime2.getDateTo().equals(worktimeTest.getDateTo()));

		assert worktimeWS.findWorktimesByQuery(companyId, "1,2", "", "").size() == 4;
		assert worktimeWS.findWorktimesByQuery(companyId, "2", "", "").size() == 1;
		assert worktimeWS.findWorktimesByQuery(companyId, "1", "2021-01-09 16:28:22", "").size() == 3;
		assert worktimeWS.findWorktimesByQuery(companyId, "1", "2021-01-17 00:00:00", "2021-01-17 23:59:59").size() == 2;

		assert worktimeWS.deleteWorktime(worktime1.getId(), companyId) == true;
		assert worktimeWS.deleteWorktime(worktime2.getId(), companyId) == true;
		assert worktimeWS.deleteWorktime(worktime3.getId(), companyId) == true;
		assert worktimeWS.deleteWorktime(worktime4.getId(), companyId) == true;
		assert worktimeWS.deleteWorktime(worktime2.getId(), companyId) == false;
		assert worktimeWS.findWorktimesByQuery(companyId, "", "", "").size() == 0;
	}
}
