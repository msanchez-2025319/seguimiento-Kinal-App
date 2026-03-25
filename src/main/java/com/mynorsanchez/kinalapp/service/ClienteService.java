package com.mynorsanchez.kinalapp.service;

import com.mynorsanchez.kinalapp.entity.Cliente;
import com.mynorsanchez.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 *Anotacion que registra un bean como un bean de SPRING
 * Que la clase contiene la logica del negocio
 */
@Service
/*
 * Por defecto todos los metodos de esta clase seram tramsaccionales
 * Una transaccion es que puede ocurrir o no algo
 */
@Transactional
public class ClienteService implements IClientesService {
    /*
     * private: solo es accesible dentro de la misma clase
     * final: No puede cambiar, porque es constante
     * Cliente repository: El repositorio para acceder a la BD
     * Inyeccion de Dependencias ya que Spring nos da el repositorio
     */
    private final ClienteRepository clienteRepository;
    /*
     * Constructor: este se ejecuta al crear un objeto
     * Spring: pasa el repositorio automaticamente (Inyeccion de Dependencia)
     */

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        //Asignar el repositorio a nuestra variable de clase
    }

    //Indica que esta implementado un metodo de la interfaz
    @Override
    // Optimizar la consulta, solo lectura, para que no bloquee la BD
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado(1);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        /*
         * Metodo de guardar, crea un cliente
         * Aca es donde colocamos la  logica del negocio
         * antes de guardar pero primero
         * validamos el dato
         */
        validarCliente(cliente);
        //if (cliente.getEstado() == 0)
            //cliente.setEstado(1);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Busca un cliente por DPI
        return clienteRepository.findById(dpi);
        //Optional nos evita el NullPointerException
    }



    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Metodo para actualizar un cliente existente
        if(!clienteRepository.existsById((dpi))){
            throw new RuntimeException("El cliente no se encontro con el DPI"+dpi);
            //si no existe se lanza una excepcion (error controlado)
        }
        cliente.setDPICliente(dpi);
        //Aseguramos que el dpi del objeto coincida con el de la URL
        //Por seguridad usamos el DPI de la URL y no el que viene en el JSON
        validarCliente(cliente);
        return clienteRepository.save(cliente);
        /*
         * save() este no solo sirve para guardar sino para actualizar tambien
         * si el dato existe (dpi) entonces hace UPDATE pero si no existe hace un
         * INSERT pero antes verificamos si existe o no el registro
         */

    }

    @Override
    public void eliminar(String dpi) {
        //Eliminar un cliente
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro con el DPI"+ dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    public boolean existePorDPI(String dpi) {
        //Verifica si existe un cliente
        return clienteRepository.existsById(dpi);
    }

    //Metodo privado( Solo se puede usar dentro de la clase)
    private void validarCliente (Cliente cliente){
        /*
         *   Validaciones del negocio: este metodo se usara
         * es algo interno del servicio
         */
        if(cliente.getDPICliente()== null || cliente.getDPICliente().trim().isEmpty()){
            //si el DPI es null o vacio despues de quitar espacios
            //lanza una excepcion con un mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }

        if(cliente.getNombreCliente()== null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }

        if(cliente.getApellidoCliente()== null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }
    }






}
