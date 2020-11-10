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

public class LoginController {

    public ModelAndView show(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();

        if(RepositorioDeUsuarios.estaLogueado(req,res))
        {
            res.redirect("/home");
        }
        else
        {
            viewModel.put("anio", LocalDate.now().getYear());
        }
        return new ModelAndView(viewModel, "login.hbs");
    }

    public Void login(Request req, Response res) {
        String username = req.queryParams("usuario");
        String password = req.queryParams("password");
        Optional<Usuario> usuario = RepositorioDeUsuarios.getInstance().getPorNombreDeUsuario(username);

        if(usuario.isPresent() && usuario.get().autenticar(username,password)) {
            req.session().attribute("idUsuario", usuario.get().getId());
            res.redirect("/home");
        } else {
            res.redirect("/login");
        }

        return null;
    }

    public Void logout(Request req, Response res) {

        req.session().removeAttribute("idUsuario");

        res.redirect("/login");
        return null;
    }


}
