package vn.elca.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import vn.elca.training.util.ApplicationMapper;

public abstract class AbstractApplicationController {
    @Autowired
    ApplicationMapper mapper;
}
