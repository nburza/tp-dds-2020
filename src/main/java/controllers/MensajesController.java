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
            List<Egreso> egresosPorUsuario = egresosPorUsuario(usuarioLogueado);
            this.cargarDatosGeneralesA(viewModel,request,"Mensajes");
            viewModel.put("egresos", ordenarPorID(egresosPorUsuario));
            if(!egresosPorUsuario.isEmpty())
            {
                viewModel.put("hayResultados", true);
            }
            return new ModelAndView(viewModel, "mensajes.hbs");
        });
    }

    public List<Egreso> egresosPorUsuario(Usuario usuario){
        return new ArrayList<>(usuario.consultarBandeja().keySet());
    }

    public List<Egreso> ordenarPorID(List<Egreso> egresos){
        return egresos.stream()
                .sorted(Comparator.comparing(EntidadPersistente::getId).reversed())
                .collect(Collectors.toList());
    }

}


