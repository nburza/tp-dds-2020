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
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UsuarioController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
    public ModelAndView showAgregarUsuario(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();

        if(!RepositorioDeUsuarios.estaLogueado(req,res))
        {
            res.redirect("/login");
        }
        else
        {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Cargar Usuario");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(req).getNombreUsuario());
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            if(RepositorioDeUsuarios.getUsuarioLogueado(req).esAdmin())
            {
                viewModel.put("esAdmin",true);
            }
        }
        return new ModelAndView(viewModel, "nuevoUsuario.hbs");
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
            viewModel.put("mensaje", true);
            viewModel.put("tipoMensaje", "danger");
            viewModel.put("tituloMensaje", "Error!");
            viewModel.put("textoMensaje", "la contraseña ingresado no es válida. " + e.getMessage());
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Cargar Usuario");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(req).getNombreUsuario());
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            if(RepositorioDeUsuarios.getUsuarioLogueado(req).esAdmin())
            {
                viewModel.put("esAdmin",true);
            }
            return new ModelAndView(viewModel, "nuevoUsuario.hbs");
        }
        Organizacion organizacion = RepositorioDeOrganizaciones.getInstance().getPorId(Long.valueOf(organizacionId)).get();

        if(!RepositorioDeUsuarios.getInstance().getAllInstances().stream().anyMatch(x -> x.getNombreUsuario().equals(username))) {
            Usuario finalUsuarioNuevo = usuarioNuevo;
            withTransaction(() ->
            {
                RepositorioDeUsuarios.getInstance().agregar(finalUsuarioNuevo);
                organizacion.agregarUsuario(finalUsuarioNuevo);
            });
            viewModel.put("mensaje", true);
            viewModel.put("tipoMensaje", "success");
            viewModel.put("tituloMensaje", "Exito!");
            viewModel.put("textoMensaje", "El usuario " + username + " fue ingresado con éxito.");
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Home");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(req).getNombreUsuario());
            if(RepositorioDeUsuarios.getUsuarioLogueado(req).esAdmin())
            {
                viewModel.put("esAdmin",true);
            }
        } else {
            viewModel.put("mensaje", true);
            viewModel.put("tipoMensaje", "danger");
            viewModel.put("tituloMensaje", "Error!");
            viewModel.put("textoMensaje", "El usuario ingresado ya existe. Ingrese nuevamente un usuario.");
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Cargar Usuario");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(req).getNombreUsuario());
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            if(RepositorioDeUsuarios.getUsuarioLogueado(req).esAdmin())
            {
                viewModel.put("esAdmin",true);
            }
            return new ModelAndView(viewModel, "nuevoUsuario.hbs");
        }

        return new ModelAndView(viewModel, "home.hbs");
    }
}
