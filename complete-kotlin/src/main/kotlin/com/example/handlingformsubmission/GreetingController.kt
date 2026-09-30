package com.example.handlingformsubmission

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class GreetingController {

    @GetMapping("/greeting")
    fun greetingForm(model: Model): String {
        model.addAttribute("greeting", Greeting())
        return "greeting"
    }

    @PostMapping("/greeting")
    fun greetingSubmit(@ModelAttribute greeting: Greeting, model: Model): String {
        model.addAttribute("greeting", greeting)
        return "result"
    }
}
