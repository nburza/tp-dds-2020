package controllers;

import egreso.Egreso;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MensajesController {
    public ModelAndView showMensajes(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }else {
            Usuario usuarioLogueado = RepositorioDeUsuarios.getUsuarioLogueado(request);
            viewModel.put("egresos", usuarioLogueado.consultarBandeja());
        }
        return new ModelAndView(viewModel, "mensajes.hbs");
    }
}
