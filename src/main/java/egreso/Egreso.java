package egreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import mediosDePago.MedioDePago;
import presupuesto.Presupuesto;
import proveedor.ValidadorDeMoneda;
import usuario.Usuario;

public class Egreso
{
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private MedioDePago medioDePago;
    private List<Item> items = new ArrayList<>();
    private LocalDate fecha;
    private List<Presupuesto> presupuestos = new ArrayList<>();
    private boolean requierePresupuesto = true;
    private List<Usuario> revisores = new ArrayList<>();
    private String moneda;
    private CriterioCompra criterioDeSeleccion = CriterioMenorValor.getInstance();
    private EstadoValidacion estado = EstadoValidacion.PENDIENTE;
    private List<Etiqueta> etiquetas = new ArrayList<>();

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, List<Item> unosItems, LocalDate unaFecha, String moneda)
    {
        ValidadorDeMoneda.getInstance().validarMoneda(moneda);
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.moneda = moneda;
        RepositorioDeEgresos.getInstance().agregarEgresos(this);
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, List<Item> unosItems, LocalDate unaFecha, boolean requierePresupuesto, String moneda)
    {
        ValidadorDeMoneda.getInstance().validarMoneda(moneda);
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.requierePresupuesto = requierePresupuesto;
        this.moneda = moneda;
        RepositorioDeEgresos.getInstance().agregarEgresos(this);
    }

    public EstadoValidacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoValidacion estado) {
        this.estado = estado;
    }

    private static final int presupuestosRequeridos = 3;

    public BigDecimal totalEgreso()
    {
        BigDecimal total = new BigDecimal("0");
        BigDecimal totalFinal = items.stream()
                .map(x -> total.add(x.precioTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalFinal;
    }

    public void agregarPresupuesto(Presupuesto presupuesto) throws Exception {
        if(requierePresupuesto){
            presupuestos.add(presupuesto);
        }else throw new Exception("El egreso no requiere presupuesto");
    }

    public void agregarEtiqueta(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }

    public void quitarEtiqueta(Etiqueta etiqueta){
        etiquetas.remove(etiqueta);
    }

    public void validar(){
        if (this.esValido()){
            this.setEstado(EstadoValidacion.VALIDO);
        }
    }

    public boolean esValido()
    {
        return (!requierePresupuesto||this.cumpleCondicionDeValidacion());

    }
    public boolean cumpleCondicionDeValidacion (){
        return this.cumplePresupuestosMinimos() &&
                this.estaBasadoEnUnPresupuesto() &&
            this.cumpleCriterioSeleccion();

}

    public boolean cumplePresupuestosMinimos()
    {

            return presupuestos.size() >= presupuestosRequeridos;

    }

    public boolean estaBasadoEnUnPresupuesto()
    {

            return presupuestos.stream().anyMatch(x -> x.getDetalle().equals(items));

    }

    public boolean cumpleCriterioSeleccion()
    {

            return presupuestos.stream().anyMatch(x -> this.getCriterioDeSeleccion().presupuestosQueCumplen(presupuestos).contains(x));

    }

    public List<Usuario> getRevisores()
    {
        return revisores;
    }

    public void agregarRevisor(Usuario usuario)
    {
        revisores.add(usuario);
    }

    public List<Item> getItems() {
        return items;
    }

    public CriterioCompra getCriterioDeSeleccion() { return criterioDeSeleccion; }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public LocalDate getFecha(){
        return fecha;
    }

    public boolean tieneLaEtiqueta(Etiqueta etiqueta){
        return etiquetas.contains(etiqueta);
    }

    public boolean estaEnElUltimoMes() {
        if(fecha == null){
            fecha = LocalDate.now();
        }
        return fecha.compareTo(LocalDate.now().minusMonths(1))==1;
    }

    public boolean esRevisor(Usuario unUsuario){
        return revisores.contains(unUsuario);
    }

}