package com.budgetplatform.common.web;

import com.budgetplatform.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    ApiResponse<HealthStatus> health() {
        return ApiResponse.success(new HealthStatus("UP", Instant.now()));
    }

    record HealthStatus(String status, Instant checkedAt) {
    }
}
