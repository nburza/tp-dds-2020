package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends ControllerGenerico{

    private Utils utils = new Utils();

    public ModelAndView show(Request req, Response res){
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            viewModel.cargarDatosGenerales(request,"Home");

            return new ModelAndView(viewModel.getViewModel(), "home.hbs");
        });
    }

    public Void index(Request request, Response response){
        if(!utils.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            response.redirect("/home");
        }
        return null;
    }
}
