package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.function.BiFunction;

public abstract class ControllerGenerico {

    private Utils utils = new Utils();

    public ModelAndView ejecutarConControlDeLogin(Request request, Response response, BiFunction<Request,Response,ModelAndView> miFuncion) {

        if(!utils.estaLogueado(request, response)){
            response.redirect("/login");
        }
        return miFuncion.apply(request,response);
    }
}
