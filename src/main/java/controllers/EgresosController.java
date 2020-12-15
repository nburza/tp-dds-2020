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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EgresosController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps
{
    ServicioAPIMercadoLibre servicioAPIMercadoLibre = new ServicioAPIMercadoLibre();
    private Utils utils = new Utils();

    public ModelAndView showEgresos(Request request, Response response){
        return ejecutarConControlDeLogin(request, response, (req, res) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();

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
            //viewModel.put("entidades", getOrganizacion(req).getEntidadesConSubentidades());
            viewModel.put("entidades", utils.getOrganizacion(req).getEntidades());

            return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
        });
    }

    public ModelAndView altaEgresos(Request req, Response res)
    {
        ViewModelTuneado viewModel = new ViewModelTuneado();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putObligatorio("tipoDocumento", req.queryParams("tipoDocumento"));
        inputs.put("identificadorDocumento", req.queryParams("identificadorDocumento"));
        inputs.putObligatorio("medioDePago", req.queryParams("medioDePago"));
        inputs.putObligatorio("moneda", req.queryParams("moneda"));
        inputs.put("fecha", req.queryParams("fecha"));
        inputs.put("requierePresu", req.queryMap("requierePresu").value());
        inputs.put("criterio", req.queryParams("criterio"));
        inputs.putObligatorio("entidad", req.queryParams("entidad"));

        List<String> items = Arrays.asList(req.queryMap("items").values());
        List<String> usuarios = Arrays.asList(req.queryMap("usuarios").values());
        List<String> etiquetas = Arrays.asList(req.queryMap("etiquetas").values());

        try {
            inputs.chequearDatosFaltantes();
        }
        catch(DatosFaltantesException e) {
            List<String> monedas = new ArrayList<String>();
            List<MonedaDTO> monedasValidas = servicioAPIMercadoLibre.getMonedas();

            for (MonedaDTO moneda : monedasValidas) {
                monedas.add(moneda.getSymbol() + " " + moneda.getId() + " (" + moneda.getDescription() + ")");
            }
            viewModel.cargarDatosGenerales(req,"Cargar Egresos");
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("documentos", TipoDocComercial.values());
            viewModel.put("usuarios", RepositorioDeUsuarios.getInstance().getAllInstances());
            viewModel.put("items", RepositorioDeItems.getInstance().getAllInstances());
            viewModel.put("medios", RepositorioDeMediosDePago.getInstance().getAllInstances());
            viewModel.put("monedas", monedasValidas);
            viewModel.put("etiquetas", RepositorioDeEtiquetas.getInstance().getAllInstances());
            viewModel.put("entidades", utils.getOrganizacion(req).getEntidades());
            return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
        }

        DocComercial docComercial = new DocComercial(Integer.parseInt(inputs.get("identificadorDocumento")),
                                                     TipoDocComercial.valueOf(inputs.get("tipoDocumento")));
        List<DocComercial> documentos = new ArrayList<DocComercial>();
        documentos.add(docComercial);

        MedioDePago medio = RepositorioDeMediosDePago.getInstance().getPorId(Long.parseLong(inputs.get("medioDePago"))).get();

        List<Item> itemsMapeados = items.stream().map(item -> RepositorioDeItems.getInstance().getPorId(Long.parseLong(item)).get()).collect(Collectors.toList());
        List<Usuario> usuariosMapeados = usuarios.stream().map(usuario -> RepositorioDeUsuarios.getInstance().getPorId(Long.parseLong(usuario)).get()).collect(Collectors.toList());
        List<Etiqueta> etiquetasMapeadas = etiquetas.stream().map(etiqueta -> RepositorioDeEtiquetas.getInstance().getPorId(Long.parseLong(etiqueta)).get()).collect(Collectors.toList());
        LocalDate fechaMapeada = LocalDate.parse(inputs.get("fecha"), formatter);

        //Entidad entidadMapeada = getOrganizacion(req).getEntidadesConSubentidades().stream().filter(e -> e.getId().equals(Long.parseLong(entidad))).findFirst().get();
        Entidad entidadMapeada = utils.getOrganizacion(req).getEntidades().stream().filter(e -> e.getId().equals(Long.parseLong(inputs.get("entidad")))).findFirst().get();

        Egreso egreso = new Egreso(documentos, medio, itemsMapeados, fechaMapeada, inputs.get("moneda"));

        if(inputs.get("requierePresu") == null)
            egreso.setRequierePresupuesto(false);
        else
            egreso.setRequierePresupuesto(true);

        if(inputs.get("criterio") != null)
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
            List<String> monedas = new ArrayList<String>();
            List<MonedaDTO> monedasValidas = servicioAPIMercadoLibre.getMonedas();

            for (MonedaDTO moneda : monedasValidas) {
                monedas.add(moneda.getSymbol() + " " + moneda.getId() + " (" + moneda.getDescription() + ")");
            }
            viewModel.cargarDatosGenerales(req,"Cargar Egresos");
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("documentos", TipoDocComercial.values());
            viewModel.put("usuarios", RepositorioDeUsuarios.getInstance().getAllInstances());
            viewModel.put("items", RepositorioDeItems.getInstance().getAllInstances());
            viewModel.put("medios", RepositorioDeMediosDePago.getInstance().getAllInstances());
            viewModel.put("monedas", monedasValidas);
            viewModel.put("etiquetas", RepositorioDeEtiquetas.getInstance().getAllInstances());
            viewModel.put("entidades", utils.getOrganizacion(req).getEntidades());
            return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
        }

        viewModel.cargarDatosGenerales(req,"Cargar Usuario");
        viewModel.agregarMensajeDeExito("El egreso fue ingresado con éxito.");

        return new ModelAndView(viewModel.getViewModel(), "altaEgresos.hbs");
    }
}
