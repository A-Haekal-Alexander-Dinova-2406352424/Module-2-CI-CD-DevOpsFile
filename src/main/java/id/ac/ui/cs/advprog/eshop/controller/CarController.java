package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.logging.IdLogger;
import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService service;
    private final IdLogger idLogger;

    @Autowired
    public CarController(CarService service, IdLogger idLogger) {
        this.service = service;
        this.idLogger = idLogger;
    }

    @GetMapping("/createCar")
    public String createCarPage(Model model) {
        Car car = new Car();
        model.addAttribute("car", car);
        return "createCar";
    }

    @PostMapping("/createCar")
    public String createCarPost(@ModelAttribute Car car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createCar";
        }

        service.create(car);
        return "redirect:listCar";
    }

    @GetMapping("/listCar")
    public String carListPage(Model model) {
        List<Car> allCars = service.findAll();
        model.addAttribute("cars", allCars);
        return "carList";
    }

    @GetMapping("/editCar/{id}")
    public String editCarPage(@PathVariable("id") String id, Model model) {
        Car car = service.findById(id);
        if (car == null) {
            return "redirect:listCar";
        }

        model.addAttribute("car", car);
        return "editCar";
    }

    @PostMapping("/editCar")
    public String editCarPost(@ModelAttribute Car car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editCar";
        }

        idLogger.log(car.getCarId());
        service.update(car);
        return "redirect:listCar";
    }

    @PostMapping("/deleteCar/{id}")
    public String deleteCarPost(@PathVariable("id") String id) {
        idLogger.log(id);
        service.delete(id);
        return "redirect:listCar";
    }
}

