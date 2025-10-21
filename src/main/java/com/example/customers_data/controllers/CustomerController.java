package com.example.customers_data.controllers;

import com.example.customers_data.models.Customer;
import com.example.customers_data.services.CustomerService;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {
    private int currentIdx = 0;
    private List<Customer> fetchedList;
    private boolean fetchOn = false;
    private String message = null;
    private int number;

    private final CustomerService customerService;
    private List<Customer> customerList;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        this.customerList = customerService.findAll();
    }

    @RequestMapping("/home")
    public ModelAndView home() {
        ModelAndView view = new ModelAndView("index");
        Customer current;
        if (!fetchOn) {
            number = customerList.size();
            if(customerList.isEmpty())
                current = new Customer();
            else {
                currentIdx = (currentIdx + customerList.size()) % customerList.size();
                current = customerList.get(currentIdx);
            }
        }
        else {
            number = fetchedList.size();
            if(fetchedList.isEmpty()) {
                current = new Customer();
                fetchOn = false;
                message = "The fetched list is now empty. Exited fetching mode.";
            }
            else {
                currentIdx = (currentIdx + fetchedList.size()) % fetchedList.size();
                current = fetchedList.get(currentIdx);
            }
        }
        view.addObject("current", current);
        view.addObject("message", message);
        view.addObject("number", number);
        return view;
    }

    @PostMapping("/submit")
    public String action(Customer customer, @RequestParam String action) {
        if (!fetchOn)
            message = null;
        else
            message = "Click FETCH again to escape fetching mode.";
        switch (action) {
            case "add":
                customer.setId(null);
                customerService.save(customer);
                customerList.add(customer);
                currentIdx = customerList.size() - 1;
                break;
            case "edit":
                Customer updatedCustomer = customerService.save(customer);
                customerList = customerService.findAll();
                if (fetchOn)
                    updateLocalList(fetchedList, updatedCustomer);
                break;
            case "fetch":
                fetchOn = !fetchOn;
                if (fetchOn) {
                    fetchedList = customerService.findByDynamicFields(customer);
                    if (fetchedList.isEmpty()) {
                        fetchOn = false;
                        message = "Sorry, no customer found with the given details.";
                    }
                    else {
                        currentIdx = 0;
                        message = "Click FETCH again to escape fetching mode.";
                        return "redirect:/home";
                    }
                }
                else {
                    message = null;
                }
                currentIdx = 0;
                break;
            case "delete":
                if (customerService.existsById(Long.valueOf(customer.getId()))) {
                    customerService.delete(customer);
                    customerList = customerService.findAll();
                    fetchedList.removeIf(c -> c.getId() == customer.getId());
                }
                else {
                    message = "Choose the customer to delete using fetch, or any of the methods above.";
                }
                break;
            case "first":
                currentIdx = 0;
                break;
            case "previous":
                    currentIdx = currentIdx - 1;
                break;
            case "next":
                currentIdx = currentIdx + 1;
                break;
            case "last":
                if (fetchOn)
                    currentIdx = fetchedList.size() - 1;
                else
                    currentIdx = customerList.size() - 1;
                break;
        }
        return "redirect:/home";
    }

    private void updateLocalList(List<Customer> list, Customer updatedCustomer) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                // Compare IDs to find the customer to replace
                if (list.get(i).getId() != null && list.get(i).getId().equals(updatedCustomer.getId())) {
                    list.set(i, updatedCustomer); // Replace the old object with the updated one
                    return; // Customer found and updated
                }
            }
        }
    }
}
