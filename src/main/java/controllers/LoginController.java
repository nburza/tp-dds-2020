package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LoginController {


    public static ModelAndView show(Request req, Response res){
        return new ModelAndView(null, "login.hbs");
    }

    public static ModelAndView login(Request req, Response res) {
        res.redirect("/");
        return null;
    }

}
