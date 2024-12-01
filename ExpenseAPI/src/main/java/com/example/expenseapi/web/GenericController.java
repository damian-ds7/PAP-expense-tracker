package com.example.expenseapi.web;

import com.example.expenseapi.pojo.Expense;
import com.example.expenseapi.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<T, ID> {
    protected final GenericService<T, ID> service;

    protected GenericController(GenericService<T, ID> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve object with given id")
    @ApiResponse(responseCode = "200", description = "Object with given id")
    public ResponseEntity<T> get(@PathVariable ID id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Add object to database")
    @ApiResponse(responseCode = "201", description = "Newly created object in database")
    public ResponseEntity<T> save(@RequestBody T entity) {
        return new ResponseEntity<>(service.save(entity), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete object with given id")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    @Operation(summary = "Delete all objects from the table")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAll() {
        service.deleteAllData();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update object with given id")
    @ApiResponse(responseCode = "200", description = "Updated object")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        return new ResponseEntity<>(service.update(id, entity), HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieves all objects")
    @ApiResponse(responseCode = "200", description = "List of all objects")
    public ResponseEntity<List<T>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
}