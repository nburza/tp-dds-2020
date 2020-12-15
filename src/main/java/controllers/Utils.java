package controllers;

import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

public class Utils {

    public Utils() {}

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

    Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }

}
