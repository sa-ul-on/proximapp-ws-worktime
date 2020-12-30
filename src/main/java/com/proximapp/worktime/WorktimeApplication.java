package com.proximapp.worktime;
import com.proximapp.worktime.entity.Movimento;
import com.proximapp.worktime.repo.impl.AccessRepo;
import com.proximapp.worktime.workingtime.WorktimeWS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.Date;

@SpringBootApplication
public class WorktimeApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(WorktimeApplication.class, args);
        System.out.println("Main Running....");

    }


}
