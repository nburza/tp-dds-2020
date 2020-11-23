package controllers;

import egreso.Egreso;
import persistencia.EntidadPersistente;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.Usuario;

import java.util.*;
import java.util.stream.Collectors;

public class MensajesController extends ControllerGenerico {
    public ModelAndView showMensajes(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();

            Usuario usuarioLogueado = this.getUsuarioLogueado(request);
            this.cargarDatosGeneralesA(viewModel,request,"Mensajes");
            viewModel.put("egresos", egresosPorUsuario(usuarioLogueado));
            if (this.getUsuarioLogueado(request).esAdmin()) {
                viewModel.put("esAdmin", true);
            }

            return new ModelAndView(viewModel, "mensajes.hbs");
        });
    }

    public List<Egreso> egresosPorUsuario(Usuario usuario){
        List<Egreso> egresosList = new ArrayList<>();
        for (Enumeration<Egreso> egresos = usuario.consultarBandeja().keys();egresos.hasMoreElements();){
            egresosList.add(egresos.nextElement());
        }
        return ordenarPorID(egresosList);
    }

    public List<Egreso> ordenarPorID(List<Egreso> egresos){
        return egresos.stream()
                .sorted(Comparator.comparing(EntidadPersistente::getId).reversed())
                .collect(Collectors.toList());
    }

}


