package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car create(Car car) {
        if (car.getCarId() == null || car.getCarId().isBlank()) {
            car.setCarId(UUID.randomUUID().toString());
        }
        carRepository.create(car);
        return car;
    }

    @Override
    public List<Car> findAll() {
        Iterator<Car> carIterator = carRepository.findAll();
        List<Car> allCar = new ArrayList<>();
        carIterator.forEachRemaining(allCar::add);
        return allCar;
    }

    @Override
    public Car findById(String id) {
        return carRepository.findById(id);
    }

    @Override
    public Car update(Car car) {
        return carRepository.update(car);
    }

    @Override
    public boolean delete(String id) {
        return carRepository.delete(id);
    }
}
