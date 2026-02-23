package com.example.handlingformsubmission

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@WebMvcTest(GreetingController::class)
@TestPropertySource(properties = ["logging.level.org.springframework.web=DEBUG"])
class HandlingFormSubmissionApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun rendersForm() {
        mockMvc.perform(get("/greeting"))
            .andExpect(content().string(containsString("Form")))
    }

    @Test
    fun submitsForm() {
        mockMvc.perform(post("/greeting").param("id", "12345").param("content", "Hello"))
            .andExpect(content().string(containsString("Result")))
            .andExpect(content().string(containsString("id: 12345")))
    }
}
