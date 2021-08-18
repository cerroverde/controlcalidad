package com.cupagroup.controlcalidad.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de datos estático para alimentar la aplicación
 */
public class Campos {
    private final String titulo;
    private final String etiqueta;


    public Campos(String titulo, String etiqueta) {
        this.titulo = titulo;
        this.etiqueta = etiqueta;
    }

    public static final List<Campos> MATERIALS = new ArrayList<>();

    static {
        MATERIALS.add(new Campos("REF. COMERCIAL", "Referencias comerciales"));
        MATERIALS.add(new Campos("FORMATOS", "Medidas de la pieza"));
        MATERIALS.add(new Campos("FORMA", "Seleccione la forma de la pieza"));
        MATERIALS.add(new Campos("CALIDAD", "Seleccione el tipo de calidad"));
        MATERIALS.add(new Campos("ESPESOR", "Especifique el espesor correspondiente"));
        MATERIALS.add(new Campos("CHAFLAN", "Corte y rebaje en la arista de las piezas"));
        MATERIALS.add(new Campos("ROTURAS", "Cantidad de roturas identificadas"));
        MATERIALS.add(new Campos("FINA", "Preguntar posible descripción"));
        MATERIALS.add(new Campos("GRUESA", "Preguntar posible descripción"));
        MATERIALS.add(new Campos("REFOLLO", "Preguntar posible descripción"));
        MATERIALS.add(new Campos("FALSA ESCUADRA", "Preguntar posible descripción"));
        MATERIALS.add(new Campos("TORCIDA","Numero de torceduras"));
        MATERIALS.add(new Campos("NUDOS","Cantidad de nudos visibles"));
        MATERIALS.add(new Campos("PIRITAS OXIDABLES","Preguntar posible descripción"));
        MATERIALS.add(new Campos("BREGADAS","Preguntar posible descripción"));
        MATERIALS.add(new Campos("FLOR","Preguntar posible descripción"));
        MATERIALS.add(new Campos("CORTES Y CUARZOS","Preguntar posible descripción"));
        MATERIALS.add(new Campos("RUCIOS", "Cantidad de cortes erróneos"));
    }

    public String getValor() {
        return titulo;
    }
    public String getEtiqueta() { return etiqueta; }

}