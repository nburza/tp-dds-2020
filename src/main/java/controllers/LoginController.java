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

public class LoginController extends ControllerGenerico{

    public ModelAndView show(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();

        if(this.estaLogueado(req,res))
        {
            res.redirect("/");
        }
        else
        {
            viewModel.put("anio", LocalDate.now().getYear());
        }
        return new ModelAndView(viewModel, "login.hbs");
    }

    public ModelAndView login(Request req, Response res) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        String username = req.queryParams("usuario");
        String password = req.queryParams("password");
        Optional<Usuario> usuario = RepositorioDeUsuarios.getInstance().getPorNombreDeUsuario(username);
        if(usuario.isPresent() && usuario.get().autenticar(username,password)) {
            req.session().attribute("idUsuario", usuario.get().getId());
            res.redirect("/mensajes/pag/1");
            return null;
        } else {
            this.agregarMensajeDeErrorA(viewModel,"El usuario o la contraseña son incorrectos. Por favor Ingrese nuevamente.");
            viewModel.put("anio", LocalDate.now().getYear());
        }

        return new ModelAndView(viewModel, "login.hbs");
    }

    public Void logout(Request req, Response res) {

        req.session().removeAttribute("idUsuario");

        res.redirect("/login");
        return null;
    }


}
