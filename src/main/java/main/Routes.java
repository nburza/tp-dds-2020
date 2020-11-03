package main;

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

        HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

        Spark.get("/",(request, response) -> {return "Hola";});
    }
}
