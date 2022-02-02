package com.juan.market.persistence;

import com.juan.market.domain.Purchase;
import com.juan.market.domain.repository.PurchaseRepository;
import com.juan.market.persistence.crud.CompraCrudRepository;
import com.juan.market.persistence.entity.Compra;
import com.juan.market.persistence.mapper.PurchaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository implements PurchaseRepository {

    private PurchaseMapper mapper;
    private CompraCrudRepository compraCrudRepository;

    public CompraRepository(PurchaseMapper mapper, CompraCrudRepository compraCrudRepository) {
        this.mapper = mapper;
        this.compraCrudRepository = compraCrudRepository;
    }

    @Override
    public List<Purchase> getAll() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<List<Purchase>> getByCliente(String clienteId) {
        return compraCrudRepository.findByIdCliente(clienteId)
                .map(compras -> mapper.toPurchases(compras));
    }

    @Override
    public Purchase save(Purchase purchase) {
        Compra compra = mapper.toCompra(purchase);
        compra.getProductos().forEach(producto -> producto.setCompra(compra));
        return mapper.toPurchase(compraCrudRepository.save(compra));
    }

}
