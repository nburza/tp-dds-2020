package controllers;

import diccionarioDeInputs.DatosFaltantesException;
import diccionarioDeInputs.DiccionarioDeInputs;
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

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putSimpleObligatorio("username", req.queryParams("username"));
        inputs.putSimpleObligatorio("password", req.queryParams("password"));
        inputs.putSimpleObligatorio("organizacionId", req.queryParams("organizaciones"));
        try {
            inputs.chequearDatosFaltantes();
        }
        catch(DatosFaltantesException e) {
            viewModel.cargarDatosGenerales(req,"Cargar Usuario");
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            return new ModelAndView(viewModel.getViewModel(),"nuevoUsuario.hbs");
        }

        Usuario usuarioNuevo = null;
        try {
            usuarioNuevo = new Usuario(inputs.getSimple("username"), inputs.getSimple("password"));
        } catch (ContraseniaDebilException | ClassNotFoundException e) {
            viewModel.cargarDatosGenerales(req,"Cargar Usuario");
            viewModel.agregarMensajeDeError("La contraseña ingresado no es válida. " + e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("organizacion", RepositorioDeOrganizaciones.getInstance().getAllInstances());
            return new ModelAndView(viewModel.getViewModel(), "nuevoUsuario.hbs");
        }
        Organizacion organizacion = RepositorioDeOrganizaciones.getInstance().getPorId(Long.parseLong(inputs.getSimple("organizacionId"))).get();

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
        viewModel.agregarMensajeDeExito("El usuario " + inputs.getSimple("username") + " fue ingresado con éxito.");
        return new ModelAndView(viewModel.getViewModel(), "home.hbs");
    }
}