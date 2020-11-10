package controllers;

import entidadOrganizativa.CategoriaEntidad;
import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadesController {

    public ModelAndView getFormularioNuevaEntidad(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        viewModel.put("anio", LocalDate.now().getYear());
        viewModel.put("titulo", "Home");
        //viewModel.put("nombreUsuario", )
        viewModel.put("categorias", getCategorias(request, response));
        return new ModelAndView(viewModel, "nuevaEntidad.hbs");
    }

    private List<CategoriaEntidad> getCategorias(Request request, Response response) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get().getCategorias();
    }
}
