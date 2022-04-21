package vn.elca.training.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.elca.training.service.GroupTableService;

/**
 * @author phau  
*/ 

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupTableService groupTableService;

    @GetMapping(value = "/list")
    public Set<String> listAllById() {
        return groupTableService.listAllGroupLeaders();
    }
}
