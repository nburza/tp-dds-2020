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
            List<Egreso> egresosPorUsuario = ordenarPorID(egresosPorUsuario(usuarioLogueado));
            this.cargarDatosGeneralesA(viewModel,request,"Mensajes");
            int numPagina = Integer.parseInt(request.params(":numPag"));
            Paginador paginador = new Paginador(numPagina);
            List<Egreso> egresosPorPagina = paginador.paginar(egresosPorUsuario);
            viewModel.put("pagSig", paginador.getPagSig());
            viewModel.put("pagAnterior", paginador.getPagAnterior());
            viewModel.put("pagActual", paginador.getPagActual());
            viewModel.put("tienePagSig",paginador.getTienePagSig());
            viewModel.put("tienePagAnt",paginador.getTienePagAnt());
            if(!egresosPorPagina.isEmpty())
            {
                viewModel.put("egresos", egresosPorPagina);
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


