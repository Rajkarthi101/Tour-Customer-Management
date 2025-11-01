package com.tms.customer.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.tms.customer.models.TourCustomer;

@FeignClient(name = "data-pull-service", url = "https://my-json-server.typicode.com/Rajkarthi101/Tour-Customer-Management")
public interface DataPullProxy {

    @GetMapping("/customers")
    List<TourCustomer> fetchDataFromExternalAPI();
}
