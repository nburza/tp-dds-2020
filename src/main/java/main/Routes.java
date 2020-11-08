package main;

import controllers.HomeController;
import controllers.LoginController;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Routes {
    public static void main(String[] args) {
        Spark.port(8088);
        Spark.staticFileLocation("/public");

        new Bootstrap().run();

        HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
        LoginController loginController = new LoginController();
        HomeController homeController = new HomeController();

        Spark.get("/home",(request, response) ->  homeController.show(request, response), engine);
        Spark.get("/login", (request, response) -> loginController.show(request, response), engine);
        Spark.post("/login", (request, response) -> loginController.login(request, response), engine);
        Spark.get("/logout", (request, response) -> loginController.logout(request, response), engine);
    }
}
