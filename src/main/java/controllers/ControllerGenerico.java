package controllers;

import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.util.function.BiFunction;

public abstract class ControllerGenerico {

    public ModelAndView funcionConControlDeLogueo(Request request, Response response, BiFunction<Request,Response,ModelAndView> miFuncion) {

        if(!this.estaLogueado(request, response)){
            response.redirect("/login");
        }
        return miFuncion.apply(request,response);
    }

    public boolean estaLogueado(Request request, Response response) {
        Usuario usuario = getUsuarioLogueado(request);

        return usuario != null;
    }

    public Usuario getUsuarioLogueado(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");

        Usuario usuario = null;

        if(idUsuario != null){
            usuario = RepositorioDeUsuarios.getInstance().getPorId(idUsuario).get();
        }

        return usuario;
    }

    private Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }
}
