package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;

public interface EstadoDeAnimoDAO {
    EstadoDeAnimo findById(int id);
    void insert(EstadoDeAnimo estadoDeAnimo);
    void update(EstadoDeAnimo estadoDeAnimo);
    void delete(int id);
}
