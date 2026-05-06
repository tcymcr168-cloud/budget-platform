package com.budgetplatform.common.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTests {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new FailingController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void returnsStructuredErrorResponse() throws Exception {
        mockMvc.perform(get("/test/fail").header("X-Request-Id", "trace-001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.error.message").value("Invalid request."))
                .andExpect(jsonPath("$.error.traceId").value("trace-001"));
    }

    @RestController
    static class FailingController {

        @GetMapping("/test/fail")
        void fail() {
            throw new ApplicationException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }
}
