package com.example.handlingformsubmission

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(GreetingController::class)
@TestPropertySource(properties = ["logging.level.org.springframework.web=DEBUG"])
class HandlingFormSubmissionApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun rendersForm() {
        mockMvc.get("/greeting")
            .andExpect { content { string(containsString("Form")) } }
    }

    @Test
    fun submitsForm() {
        mockMvc.post("/greeting") {
            param("id", "12345")
            param("content", "Hello")
        }.andExpect { content { string(containsString("Result")) } }
            .andExpect { content { string(containsString("id: 12345")) } }
    }
}