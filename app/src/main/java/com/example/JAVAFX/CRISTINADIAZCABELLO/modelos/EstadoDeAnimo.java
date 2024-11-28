/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JAVAFX.CRISTINADIAZCABELLO.modelos;

/**
 *
 * @author crist
 */
public class EstadoDeAnimo {
    private String emoji;
    private int paciencia;
    private int fuerzaSentimiento;
    private int gradoProductividad;

    public EstadoDeAnimo(String emoji, int paciencia, int fuerzaSentimiento, int gradoProductividad) {
        this.emoji = emoji;
        this.paciencia = paciencia;
        this.fuerzaSentimiento = fuerzaSentimiento;
        this.gradoProductividad = gradoProductividad;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public int getPaciencia() {
        return paciencia;
    }

    public void setPaciencia(int paciencia) {
        this.paciencia = paciencia;
    }

    public int getFuerzaSentimiento() {
        return fuerzaSentimiento;
    }

    public void setFuerzaSentimiento(int fuerzaSentimiento) {
        this.fuerzaSentimiento = fuerzaSentimiento;
    }

    public int getGradoProductividad() {
        return gradoProductividad;
    }

    public void setGradoProductividad(int gradoProductividad) {
        this.gradoProductividad = gradoProductividad;
    }

    @Override
    public String toString() {
        return "EstadoDeAnimo{" +
                ", emoji='" + emoji + '\'' +
                ", paciencia=" + paciencia +
                ", fuerzaSentimiento=" + fuerzaSentimiento +
                ", gradoProductividad=" + gradoProductividad +
                '}';
    }
}