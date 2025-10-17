package com.example.customers_data.controllers;

import com.example.customers_data.models.Customer;
import com.example.customers_data.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    private int customerIndex = 0;

    @RequestMapping("/home")
    public ModelAndView home() {
        List<Customer> customersList = customerService.findAll();
        ModelAndView view = new ModelAndView("index");
        Customer current;
        if (customersList.isEmpty())
            current = new Customer();
        else
            current = customersList.get(customerIndex);
        view.addObject("current", current);
        return view;
    }
}
