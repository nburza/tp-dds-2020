package controllers;

import diccionarioDeInputs.DiccionarioDeInputs;
import spark.Request;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ViewModelTuneado {

    private Map<String, Object> viewModel = new HashMap<String, Object>();
    private Utils utils = new Utils();

    public ViewModelTuneado() {
    }

    public Map<String, Object> getViewModel() {
        return viewModel;
    }

    public void put(String clave, Object valor) {
        viewModel.put(clave, valor);
    }

    public Object get(String clave) {
        return viewModel.get(clave);
    }

    public void cargarDatosGenerales(Request request, String titulo) {
        Usuario usuario = utils.getUsuarioLogueado(request);
        viewModel.put("titulo", titulo);
        viewModel.put("nombreUsuario", usuario.getNombreUsuario());
        viewModel.put("anio", LocalDate.now().getYear());
        if(usuario.esAdmin()){
            viewModel.put("esAdmin", true);
        }
        else{
            viewModel.put("nombreOrganizacion", "(" + utils.getOrganizacion(request).getNombre() + ")");
        }
    }

    public void agregarMensaje(String tipo, String titulo, String texto) {
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", tipo);
        viewModel.put("tituloMensaje", titulo);
        viewModel.put("textoMensaje", texto);
    }

    public void agregarMensajeDeError(String textoMensaje) {
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", "danger");
        viewModel.put("tituloMensaje", "Error!");
        viewModel.put("textoMensaje", textoMensaje);
    }

    public void agregarMensajeDeExito(String textoMensaje) {
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", "success");
        viewModel.put("tituloMensaje", "Perfecto!");
        viewModel.put("textoMensaje", textoMensaje);
    }

    public void rellenarDatosAnteError(DiccionarioDeInputs diccionarioDeInputs) {
        diccionarioDeInputs.getInputs().forEach((key, value) -> viewModel.put(key+"Value", value.getValorSimple()));
    }
}
