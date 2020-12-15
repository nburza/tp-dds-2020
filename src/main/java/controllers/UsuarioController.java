package controllers;

import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.ContraseniaDebilException;
import usuario.NombreDeUsuarioRepetidoException;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class UsuarioController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    private Utils utils = new Utils();

    public ModelAndView showAgregarUsuario(Request req, Response res){
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();

            if (utils.getUsuarioLogueado(req).esAdmin()) {
                viewModel.cargarDatosGenerales(request,"Cargar Usuario");
                viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            } else {
                res.redirect("/home");
            }
            return new ModelAndView(viewModel.getViewModel(), "nuevoUsuario.hbs");
        });
    }

    public ModelAndView agregarUsuario(Request req, Response res){
        ViewModelTuneado viewModel = new ViewModelTuneado();
        String username = req.queryParams("usuario");
        String password = req.queryParams("password");
        String organizacionId = req.queryParams("organizaciones");
        Usuario usuarioNuevo = null;
        try {
            usuarioNuevo = new Usuario(username,password);
        } catch (ContraseniaDebilException | ClassNotFoundException e) {
            viewModel.cargarDatosGenerales(req,"Cargar Usuario");
            viewModel.agregarMensajeDeError("La contraseña ingresado no es válida. " + e.getMessage());
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            viewModel.put("nombreValue", username);
            return new ModelAndView(viewModel.getViewModel(), "nuevoUsuario.hbs");
        }
        Organizacion organizacion = RepositorioDeOrganizaciones.getInstance().getPorId(Long.parseLong(organizacionId)).get();

        Usuario finalUsuarioNuevo = usuarioNuevo;
        try {
            withTransaction(() ->
            {
                RepositorioDeUsuarios.getInstance().agregar(finalUsuarioNuevo);
                organizacion.agregarUsuario(finalUsuarioNuevo);
            });
        } catch (NombreDeUsuarioRepetidoException e) {
            viewModel.cargarDatosGenerales(req,"Cargar usuario");
            viewModel.agregarMensajeDeError("El usuario ingresado ya existe. Ingrese nuevamente un usuario.");
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            return new ModelAndView(viewModel.getViewModel(), "nuevoUsuario.hbs");
        }
        viewModel.cargarDatosGenerales(req,"Home");
        viewModel.agregarMensajeDeExito("El usuario " + username + " fue ingresado con éxito.");
        return new ModelAndView(viewModel.getViewModel(), "home.hbs");
    }
}