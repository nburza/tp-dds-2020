package controllers;

import egreso.Egreso;
import egreso.RepositorioDeEgresos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MensajesController {
    public ModelAndView showMensajes(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }else {
            viewModel.put("egresos", egresosPorUsuarioLogueado(request));
        }
        return new ModelAndView(viewModel, "mensajes.hbs");
    }

    public List<Egreso> egresosPorUsuarioLogueado(Request request){
        Usuario usuarioLogueado = RepositorioDeUsuarios.getUsuarioLogueado(request);
        List<Egreso> egresosList = new ArrayList<>();
        for (Enumeration<Egreso> egresos = usuarioLogueado.consultarBandeja().keys();egresos.hasMoreElements();){
            egresosList.add(egresos.nextElement());
        }
        return egresosList;
    }
}


