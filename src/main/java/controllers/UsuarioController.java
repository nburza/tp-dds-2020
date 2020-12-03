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
    public ModelAndView showAgregarUsuario(Request req, Response res){
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();

            if (this.getUsuarioLogueado(req).esAdmin()) {
                this.cargarDatosGeneralesA(viewModel,request,"Cargar Usuario");
                viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            } else {
                res.redirect("/home");
            }
            return new ModelAndView(viewModel, "nuevoUsuario.hbs");
        });
    }

    public ModelAndView agregarUsuario(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();
        String username = req.queryParams("usuario");
        String password = req.queryParams("password");
        String organizacionId = req.queryParams("organizaciones");
        Usuario usuarioNuevo = null;
        try {
            usuarioNuevo = new Usuario(username,password);
        } catch (ContraseniaDebilException | ClassNotFoundException e) {
            this.cargarDatosGeneralesA(viewModel,req,"Cargar Usuario");
            this.agregarMensajeDeErrorA(viewModel,"La contraseña ingresado no es válida. " + e.getMessage());
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            viewModel.put("nombreValue", username);
            return new ModelAndView(viewModel, "nuevoUsuario.hbs");
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
            this.cargarDatosGeneralesA(viewModel,req,"Cargar usuario");
            this.agregarMensajeDeErrorA(viewModel,"El usuario ingresado ya existe. Ingrese nuevamente un usuario.");
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            return new ModelAndView(viewModel, "nuevoUsuario.hbs");
        }
        this.cargarDatosGeneralesA(viewModel,req,"Home");
        this.agregarMensajeDeExitoA(viewModel,"El usuario " + username + " fue ingresado con éxito.");
        return new ModelAndView(viewModel, "home.hbs");
    }
}