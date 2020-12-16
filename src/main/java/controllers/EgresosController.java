package controllers;

import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.ServicioAPIMercadoLibre;
import diccionarioDeInputs.DatosFaltantesException;
import diccionarioDeInputs.DiccionarioDeInputs;
import egreso.*;
import entidadOrganizativa.Entidad;
import entidadOrganizativa.exceptions.MontoSuperadoException;
import mediosDePago.MedioDePago;
import mediosDePago.RepositorioDeMediosDePago;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EgresosController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps
{
    ServicioAPIMercadoLibre servicioAPIMercadoLibre = new ServicioAPIMercadoLibre();
    private Utils utils = new Utils();

    public ModelAndView showEgresos(Request request, Response response){
        return ejecutarConControlDeLogin(request, response, (req, res) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();

            cargarPantallaDeEgresos(req, viewModel);

            return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
        });
    }

    public ModelAndView altaEgresos(Request req, Response res)
    {
        ViewModelTuneado viewModel = new ViewModelTuneado();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putSimpleObligatorio("tipoDocumento", req.queryParams("tipoDocumento"));
        inputs.putSimple("identificadorDocumento", req.queryParams("identificadorDocumento"));
        inputs.putSimpleObligatorio("medioDePago", req.queryParams("medioDePago"));
        inputs.putSimpleObligatorio("moneda", req.queryParams("moneda"));
        inputs.putMultiple("items",utils.getValuesComoLista(req,"items"));
        inputs.putSimple("fecha", req.queryParams("fecha"));
        inputs.putSimple("requierePresu", req.queryMap("requierePresu").value());
        inputs.putMultiple("usuarios", utils.getValuesComoLista(req, "usuarios"));
        inputs.putSimple("criterio", req.queryParams("criterio"));
        inputs.putMultiple("etiquetas", utils.getValuesComoLista(req, "etiquetas"));
        inputs.putSimpleObligatorio("entidad", req.queryParams("entidad"));

        try {
            inputs.chequearDatosFaltantes();
        }
        catch(DatosFaltantesException e) {
            cargarPantallaDeEgresos(req, viewModel);
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
        }

        DocComercial docComercial = new DocComercial(Integer.parseInt(inputs.getSimple("identificadorDocumento")),
                                                     TipoDocComercial.valueOf(inputs.getSimple("tipoDocumento")));
        List<DocComercial> documentos = new ArrayList<DocComercial>();
        documentos.add(docComercial);

        MedioDePago medio = RepositorioDeMediosDePago.getInstance().getPorId(Long.parseLong(inputs.getSimple("medioDePago"))).get();

        List<Item> itemsMapeados = RepositorioDeItems.getInstance().getPorListaDeIds(utils.parsearIds(inputs.getMultiple("items")));
        List<Usuario> usuariosMapeados = RepositorioDeUsuarios.getInstance().getPorListaDeIds(utils.parsearIds(inputs.getMultiple("usuarios")));
        List<Etiqueta> etiquetasMapeadas = RepositorioDeEtiquetas.getInstance().getPorListaDeIds(utils.parsearIds(inputs.getMultiple("etiquetas")));

        LocalDate fechaMapeada = LocalDate.parse(inputs.getSimple("fecha"), formatter);

        Entidad entidadMapeada = utils.getOrganizacion(req).getEntidadPorId(Long.parseLong(inputs.getSimple("entidad")));

        Egreso egreso = new Egreso(documentos, medio, itemsMapeados, fechaMapeada, inputs.getSimple("moneda"));

        if(inputs.getSimple("requierePresu") == null)
            egreso.setRequierePresupuesto(false);
        else
            egreso.setRequierePresupuesto(true);

        if(inputs.getSimple("criterio") != null)
            egreso.setCriterioDeSeleccion(CriterioCompra.CRITERIO_MENOR_VALOR);

        egreso.setRevisores(usuariosMapeados);
        egreso.setEtiquetas(etiquetasMapeadas);

        List<Egreso> egresos = new ArrayList<Egreso>();
        egresos.add(egreso);

        try {
            withTransaction(() -> {
                entidadMapeada.setEgresos(egresos);
            });
        }
        catch(MontoSuperadoException e) {
            cargarPantallaDeEgresos(req,viewModel);
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
        }

        cargarPantallaDeEgresos(req, viewModel);
        viewModel.agregarMensajeDeExito("El egreso fue ingresado con éxito.");

        return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
    }

    private void cargarPantallaDeEgresos(Request req, ViewModelTuneado viewModel) {
        List<String> monedas = new ArrayList<String>();
        List<MonedaDTO> monedasValidas = servicioAPIMercadoLibre.getMonedas();

        for (MonedaDTO moneda : monedasValidas) {
            monedas.add(moneda.getSymbol() + " " + moneda.getId() + " (" + moneda.getDescription() + ")");
        }
        viewModel.cargarDatosGenerales(req,"Cargar Egresos");
        viewModel.put("documentos", TipoDocComercial.values());
        viewModel.put("usuarios", RepositorioDeUsuarios.getInstance().getAllInstances());
        viewModel.put("items", RepositorioDeItems.getInstance().getAllInstances());
        viewModel.put("medios", RepositorioDeMediosDePago.getInstance().getAllInstances());
        viewModel.put("monedas", monedasValidas);
        viewModel.put("etiquetas", RepositorioDeEtiquetas.getInstance().getAllInstances());
        viewModel.put("entidades", utils.getOrganizacion(req).getEntidades());
    }
}
