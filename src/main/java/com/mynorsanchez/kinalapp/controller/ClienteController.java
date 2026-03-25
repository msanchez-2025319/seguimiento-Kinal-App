package com.mynorsanchez.kinalapp.controller;


import com.mynorsanchez.kinalapp.entity.Cliente;
import com.mynorsanchez.kinalapp.service.ClienteService;
import com.mynorsanchez.kinalapp.service.IClientesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody

@RequestMapping("/clientes")
//Todas las rutas en este controlador deben empezar con /clientes

public class ClienteController {


    private final IClientesService clienteService;

    public ClienteController(IClientesService clienteService) {
        this.clienteService = clienteService;
    }

    //Este responde peticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clientes = clienteService.listarTodos();
        //delegamos el servicio
        return ResponseEntity.ok(clientes);
    }

    //{dpi} es una variable de ruta, es un valor a buscar
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi){
        //@PathVariable toma el valor de la URL y lo asigna al DPI
        //Si optional tiene valor, devuelve 200 ok con el cliente
        //Pero si hay un error esta vacio y devuelve error 404 NOT FOUND
        return clienteService.buscarPorDPI(dpi).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Post crea un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente ){
        //@RequestBody toma el JSon del cuerpo y lo convierte a un objeto del tipo cliente
        //<?> significa tipo generico q puede ser un cliente o un String
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //intentamos guardar el cliente
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREATED (mas especifico que el 200)
        } catch (IllegalArgumentException e) {
            //si hay error en la validacion
            //retorna un 400 bad request con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Elimina a un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void> No devuelve cuerpo en la respuesta
        try {
            if (!clienteService.existePorDPI(dpi)){
                return ResponseEntity.notFound().build();
                //404 not found si no existe
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 sin contenido, pero se ejecuto correctamente y no devuelve cuerpo
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try {
            if (!clienteService.existePorDPI(dpi)){
                //verificar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
            }
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalArgumentException e){
            //error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return  ResponseEntity.notFound().build();
        }
    }

    //Este responde peticiones GET
    @GetMapping("/activos")
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Cliente>> listarActivos(){
        List<Cliente> clientesA = clienteService.listarActivos();
        //delegamos el servicio
        return ResponseEntity.ok(clientesA);
    }

}
