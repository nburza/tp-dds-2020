package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HomeController {


    public static ModelAndView show(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();
        viewModel.put("anio", LocalDate.now().getYear());
        viewModel.put("titulo", "Home");
        //viewModel.put("nombreUsuario", )
        return new ModelAndView(viewModel, "home.hbs");
    }

}
