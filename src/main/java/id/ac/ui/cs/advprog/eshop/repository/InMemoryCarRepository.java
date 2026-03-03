package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryCarRepository implements CarRepository {
    private final List<Car> carData = new ArrayList<>();

    @Override
    public Car create(Car car) {
        carData.add(car);
        return car;
    }

    @Override
    public Iterator<Car> findAll() {
        return carData.iterator();
    }

    @Override
    public Car findById(String id) {
        for (Car car : carData) {
            if (Objects.equals(car.getCarId(), id)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Car update(Car updatedCar) {
        if (updatedCar == null || updatedCar.getCarId() == null) {
            return null;
        }

        Car car = findById(updatedCar.getCarId());
        if (car == null) {
            return null;
        }

        car.setCarName(updatedCar.getCarName());
        car.setCarColor(updatedCar.getCarColor());
        car.setCarQuantity(updatedCar.getCarQuantity());
        return car;
    }

    @Override
    public boolean delete(String id) {
        Iterator<Car> iterator = carData.iterator();
        while (iterator.hasNext()) {
            if (Objects.equals(iterator.next().getCarId(), id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}

