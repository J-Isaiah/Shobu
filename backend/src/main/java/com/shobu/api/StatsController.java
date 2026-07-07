package com.shobu.api;

import com.shobu.api.dto.response.GetStatsResponse;
import com.shobu.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final DataService dataService;

    public StatsController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public GetStatsResponse getStats() {
        return dataService.getStats();
    }
}