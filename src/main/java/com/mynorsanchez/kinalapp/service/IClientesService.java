package com.mynorsanchez.kinalapp.service;


import com.mynorsanchez.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClientesService {
    /*
     * Interfaz: Es un contrato que dice QUÉ métodos debe tenes
     *  cualquier servicio de Clientes, No tiene
     * Implementacion, solo la definicion de los métodos
     */

    //Metodo que devuelve una lista de todos los Clientes
    List<Cliente> listarTodos();

    List<Cliente> listarActivos();
    /*
     *List<Cliente> lo que hace es devolver una lista
     *  de objetos de la entidad Clientes
     */

    //Metodo que guarda un Cliente en la BD
    Cliente guardar (Cliente cliente);
    //Parametros: Recibe un objeto Cliente con los datos a
    //guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);


    //Metodo que actualiza un Cliente
    Cliente actualizar(String dpi, Cliente cliente);
    /*
     * Parametros - dpi: DPI del cliente a actualizar
     * Cliente cliente: Objeto con los datos nuevos
     *  Retorna un objeto de tipo Cliente ya aztualizado
     */

    /*
     * Metodo de tipo void para eliminar a un CLiente
     * void: no retorna ningun valor o dato
     * Eliminar un Cliente por su DPI
     */
    void eliminar(String dpi);

    //boolean - Retorna true si existe y false si no existe
    boolean existePorDPI (String dpi);





}

