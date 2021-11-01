package com.example.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class OrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersApplication.class, args);
	}
}

@Controller
class OrderRSocketController {

	private final Map<Integer, Profile> db = new ConcurrentHashMap<>();

	OrderRSocketController() {
		var ctr = 0;
		for (var customerId = 1; customerId <= 8; customerId++)	{
			ctr += 1;
			this.db.put(customerId, new Profile(ctr, new Date()));
		}
	}

	@MessageMapping("profiles.{customerId}")
	Mono<Profile> getProfileForCustomer(@DestinationVariable Integer customerId) {
		var customer = this.db.get(customerId);
		return Mono.just(customer);
	}
}

record Profile(Integer id, Date registered) {
}
