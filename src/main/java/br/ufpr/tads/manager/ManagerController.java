package br.ufpr.tads.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/manager")
public class ManagerController {
    
    @Autowired
    ManagerService _service;

    @GetMapping("/list")
    public ResponseEntity<List<ManagerResult>> getAllManagers()
    {
        try{
            var managers = _service.getAllManagers();
            return ResponseEntity.ok(managers);
        }catch(Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
