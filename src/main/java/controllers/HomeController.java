package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HomeController {

    public ModelAndView show(Request request, Response response){
        Map<String, Object> viewModel = new HashMap<String, Object>();

        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else
        {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Home");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
        }
        return new ModelAndView(viewModel, "home.hbs");
    }
}
