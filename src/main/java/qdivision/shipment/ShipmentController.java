package qdivision.shipment;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("shipment")
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;

    public ShipmentController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping
    public List<Shipment> getShipments(){
        return shipmentRepository.findAll()
                .stream()
                .map(shipmentEntity -> toShipment(shipmentEntity))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Shipment createShipment(@RequestBody Shipment shipment) {

        return toShipment(shipmentRepository.saveAndFlush(toShipmentEntity(shipment)));
    }

    @PutMapping
    @RequestMapping("{id}")
    public Shipment updateShipment(@PathVariable("id") Long id, @RequestBody Shipment shipment) {

        ShipmentEntity updatedShipment = shipmentRepository.findById(id)
                .map(toUpdate -> toUpdate.toBuilder()
                        .name(shipment.getName())
                        .build())
                .orElseThrow(() -> new RuntimeException("Shipment not found to update"));

        return toShipment(shipmentRepository.saveAndFlush(updatedShipment));
    }

    private ShipmentEntity toShipmentEntity(Shipment shipment) {
        return ShipmentEntity.builder()
                .id(shipment.getId())
                .name(shipment.getName())
                .build();
    }

    private Shipment toShipment(ShipmentEntity shipmentEntity) {
        return Shipment.builder()
                .id(shipmentEntity.getId())
                .name(shipmentEntity.getName())
                .build();
    }
}