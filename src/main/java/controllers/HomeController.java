package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends ControllerGenerico{

    public ModelAndView show(Request req, Response res){
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            this.cargarDatosGeneralesA(viewModel,request,"Home");

            return new ModelAndView(viewModel, "home.hbs");
        });
    }

    public Void index(Request request, Response response){
        if(!this.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            response.redirect("/home");
        }
        return null;
    }
}
