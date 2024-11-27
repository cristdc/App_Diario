/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JAVAFX.CRISTINADIAZCABELLO.modelos;

import java.util.Date;

public class Dia {
    private Date fecha;
    private int calidadSueno;
    private String clima;
    private boolean siesta;
    private String retos;

    public Dia(Date fecha, int calidadSueno, String clima, boolean siesta, String retos) {
        this.fecha = fecha;
        this.calidadSueno = calidadSueno;
        this.clima = clima;
        this.siesta = siesta;
        this.retos = retos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCalidadSueno() {
        return calidadSueno;
    }

    public void setCalidadSueno(int calidadSueno) {
        this.calidadSueno = calidadSueno;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public boolean isSiesta() {
        return siesta;
    }

    public void setSiesta(boolean siesta) {
        this.siesta = siesta;
    }

    public String getRetos() {
        return retos;
    }

    public void setRetos(String retos) {
        this.retos = retos;
    }

    @Override
    public String toString() {
        return "Dia{" +
                "fecha=" + fecha +
                ", calidadSueno=" + calidadSueno +
                ", clima='" + clima + '\'' +
                ", siesta=" + siesta +
                ", retos='" + retos + '\'' +
                '}';
    }
}