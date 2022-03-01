package com.sapient.wellington.oneviewassignment;

import com.sapient.wellington.oneviewassignment.service.OneViewService;
import com.sapient.wellington.oneviewassignment.service.OneViewServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;


@SpringBootApplication
public class OneviewAssignmentApplication {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = SpringApplication.run(OneviewAssignmentApplication.class, args);
		OneViewService service = context.getBean(OneViewServiceImpl.class);

		service.processAverageIncome();
	}

}
