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


    public ModelAndView show(Request request, Response response){
        if(!estaLogueado(request, response)){
            response.redirect("/login");
        }

        Map<String, Object> viewModel = new HashMap<String, Object>();
        viewModel.put("anio", LocalDate.now().getYear());
        viewModel.put("titulo", "Home");
        //viewModel.put("nombreUsuario", )
        return new ModelAndView(viewModel, "home.hbs");
    }

    private boolean estaLogueado(Request request, Response response) {
        Usuario usuario = getUsuarioLogueado(request);

        return usuario != null;
    }

    private Usuario getUsuarioLogueado(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");

        Usuario usuario = null;

        if(idUsuario != null){
            usuario = RepositorioDeUsuarios.getInstance().getPorId(idUsuario).get();
        }

        return usuario;
    }

}
