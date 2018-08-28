package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting_attribute", new GreetingClass());
        return "greeting_template";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute GreetingClass greetingClass) {
        return "result";
    }
}
