package controllers;

import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class ControllerGenerico {

    public ModelAndView ejecutarConControlDeLogin(Request request, Response response, BiFunction<Request,Response,ModelAndView> miFuncion) {

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

    Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }

    public void cargarDatosGeneralesA(Map<String, Object> viewModel, Request request, String titulo) {
        viewModel.put("titulo", titulo);
        viewModel.put("nombreUsuario", this.getUsuarioLogueado(request).getNombreUsuario());
        viewModel.put("anio", LocalDate.now().getYear());
    }

    public void agregarMensajeA(Map<String, Object> viewModel, String tipo, String titulo, String texto) {
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", tipo);
        viewModel.put("tituloMensaje", titulo);
        viewModel.put("textoMensaje", texto);
    }

    public void agregarMensajeDeErrorA(Map<String, Object> viewModel, String textoMensaje) {
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", "danger");
        viewModel.put("tituloMensaje", "Error!");
        viewModel.put("textoMensaje", textoMensaje);
    }

    public void agregarMensajeDeExitoA(Map<String, Object> viewModel, String textoMensaje) {
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", "success");
        viewModel.put("tituloMensaje", "Exito!");
        viewModel.put("textoMensaje", textoMensaje);
    }

}
