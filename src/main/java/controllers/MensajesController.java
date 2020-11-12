package controllers;

import egreso.Egreso;
import egreso.RepositorioDeEgresos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MensajesController {
    public ModelAndView showMensajes(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }else {
            Usuario usuarioLogueado = RepositorioDeUsuarios.getUsuarioLogueado(request);
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("nombreUsuario",usuarioLogueado.getNombreUsuario());
            viewModel.put("egresos", egresosPorUsuario(usuarioLogueado));
        }
        return new ModelAndView(viewModel, "mensajes.hbs");
    }

    public List<Egreso> egresosPorUsuario(Usuario usuario){
        List<Egreso> egresosList = new ArrayList<>();
        for (Enumeration<Egreso> egresos = usuario.consultarBandeja().keys();egresos.hasMoreElements();){
            egresosList.add(egresos.nextElement());
        }
        return egresosList;
    }

}


