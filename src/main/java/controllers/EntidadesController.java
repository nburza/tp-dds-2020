package controllers;

import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.ServiceLocator;
import entidadOrganizativa.CategoriaEntidad;
import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadesController {

    public ModelAndView showEntidades(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Entidades");
            viewModel.put("idOrganizacion", getOrganizacion(request).getId());
            viewModel.put("entidades", getOrganizacion(request).getEntidades());
        }
        return new ModelAndView(viewModel, "entidades.hbs");
    }

    public ModelAndView showFormularioNuevaEntidad(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Crear entidad");
            viewModel.put("categorias", getOrganizacion(request).getCategorias());
        }
        return new ModelAndView(viewModel, "nuevaEntidad.hbs");
    }

    private Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }
}
