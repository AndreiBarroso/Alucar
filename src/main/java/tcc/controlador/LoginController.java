//package tcc.controlador;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class LoginController {
//
//    @GetMapping(path = "/login")
//    public String login() {
//        return "login";
//    }
//
//    @ExceptionHandler(Exception.class)
//
//    public String handleException(Exception error) throws Exception {
//        return "redirect:/login?error=Login incorreto";
//
//    }
//
//}
