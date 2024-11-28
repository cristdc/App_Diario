package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;

import java.sql.SQLException;

public interface EstadoDeAnimoDAO {
    EstadoDeAnimo findById(int id);
    int insert(EstadoDeAnimo estadoDeAnimo) throws SQLException;
    void update(EstadoDeAnimo estadoDeAnimo);
    void delete(int id);
}
