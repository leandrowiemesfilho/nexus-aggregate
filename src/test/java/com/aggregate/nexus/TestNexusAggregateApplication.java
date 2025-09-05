package com.aggregate.nexus;

import org.springframework.boot.SpringApplication;

public class TestNexusAggregateApplication {

	public static void main(String[] args) {
		SpringApplication.from(NexusAggregateApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
