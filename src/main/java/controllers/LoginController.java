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


    public static ModelAndView show(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();
        viewModel.put("anio", LocalDate.now().getYear());
        return new ModelAndView(viewModel, "login.hbs");
    }

    public static ModelAndView login(Request req, Response res) {
        String username = req.queryParams("usuario");
        String password = req.queryParams("password");
        Optional<Usuario> usuario = RepositorioDeUsuarios.getInstance().getPorNombreDeUsuario(username);

        RepositorioDeUsuarios.usuariosLogueados.add(usuario.get());

        req.session().attribute("idUsuario", usuario.get().getId());

        res.redirect("/home");
        return null;
    }

    public static ModelAndView logout(Request req, Response res) {
        String username = req.queryParams("usuario");
        String password = req.queryParams("password");
        Optional<Usuario> usuario = RepositorioDeUsuarios.getInstance().getPorNombreDeUsuario(username);

        RepositorioDeUsuarios.usuariosLogueados.remove(usuario.get());

        req.session().removeAttribute("idUsuario");

        res.redirect("/login");
        return null;
    }
}
