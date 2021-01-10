package com.proximapp.worktime.worktime;

import com.proximapp.worktime.entity.Worktime;
import com.proximapp.worktime.workingtime.WorktimeWS;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class WorktimeApplicationTests {

	@Test
	void contextLoads() throws ParseException {
		WorktimeWS worktimeWS = new WorktimeWS();
		Worktime worktime1, worktime2, worktimeTest;
		worktimeWS.notifyWorktime(1, 1, "2021-01-09 16:28:22", true);
		worktime1 = worktimeWS.notifyWorktime(1, 1, "2021-01-09 16:30:22", false);
		worktimeWS.notifyWorktime(1, 1, "", true);
		worktime2 = worktimeWS.notifyWorktime(1, 1, "", false);
		worktimeTest = new Worktime();
		worktimeTest.setId(worktime1.getId());
		worktimeTest.setCompanyId(1);
		worktimeTest.setUserId(1);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		worktimeTest.setDateFrom(format.parse("2021-01-09 16:28:22"));
		worktimeTest.setDateTo(format.parse("2021-01-09 16:30:22"));
		assert (worktime1.getId() == worktimeTest.getId()
				&& worktime1.getCompanyId() == worktimeTest.getCompanyId()
				&& worktime1.getUserId() == worktimeTest.getUserId()
				&& worktime1.getDateFrom().equals(worktimeTest.getDateFrom())
				&& worktime1.getDateTo().equals(worktimeTest.getDateTo()));
		assert worktimeWS.notifyWorktime(1, 1, "", false) == null;
		assert worktimeWS.findWorktimesByQuery(1,"1", "2021-01-08 16:28:22","").stream().filter(worktime -> {
			if(worktime.getId() == worktime1.getId() &&
					worktime.getCompanyId() == worktime1.getCompanyId() &&
					worktime.getUserId() == worktime1.getUserId() &&
					worktime.getDateFrom().equals(worktime1.getDateFrom()) &&
					worktime.getDateTo().equals(worktime1.getDateTo())) {
				return true;
			}else if(worktime.getId() == worktime2.getId() &&
					worktime.getCompanyId() == worktime2.getCompanyId() &&
					worktime.getUserId() == worktime2.getUserId() &&
					worktime.getDateFrom().equals(worktime2.getDateFrom()) &&
					worktime.getDateTo().equals(worktime2.getDateTo())){
				return true;
			}else{
				return false;
			}
		}).count() == 2;
		assert worktimeWS.deleteWorktime(worktime1.getId(),worktime1.getCompanyId()) == true;
		assert worktimeWS.deleteWorktime(worktime2.getId(),worktime2.getCompanyId()) == true;
		assert worktimeWS.deleteWorktime(worktime2.getId(),2) == false;
		assert worktimeWS.findWorktimesByQuery(1,"1","2021-01-08 16:28:22","").size() == 0;
	}
}
