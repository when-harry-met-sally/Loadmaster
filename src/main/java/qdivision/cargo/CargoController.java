package qdivision.cargo;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cargo")
public class CargoController {

    private final CargoRepository cargoRepository;

    public CargoController(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @GetMapping
    public List<Cargo> getCargo(){
        return cargoRepository.findAll()
                .stream()
                .map(cargoEntity -> toCargo(cargoEntity))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Cargo createCargo(@RequestBody Cargo cargo) {

        return toCargo(cargoRepository.saveAndFlush(toCargoEntity(cargo)));
    }

    @PutMapping
    @RequestMapping("{id}")
    public Cargo updateCargo(@PathVariable("id") Long id, @RequestBody Cargo cargo) {

        CargoEntity updatedCargo = cargoRepository.findById(id)
                .map(toUpdate -> toUpdate.toBuilder()
                        .name(cargo.getName())
                        .build())
                .orElseThrow(() -> new RuntimeException("Cargo not found to update"));

        return toCargo(cargoRepository.saveAndFlush(updatedCargo));
    }

    private CargoEntity toCargoEntity(Cargo cargo) {
        return CargoEntity.builder()
                .id(cargo.getId())
                .name(cargo.getName())
                .build();
    }

    private Cargo toCargo(CargoEntity cargoEntity) {
        return Cargo.builder()
                .id(cargoEntity.getId())
                .name(cargoEntity.getName())
                .build();
    }
}