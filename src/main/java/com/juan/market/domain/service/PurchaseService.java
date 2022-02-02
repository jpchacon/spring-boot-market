package com.juan.market.domain.service;

import com.juan.market.domain.Purchase;
import com.juan.market.persistence.CompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    private CompraRepository repository;

    public PurchaseService(CompraRepository repository) {
        this.repository = repository;
    }

    public List<Purchase> getAll(){
        return repository.getAll();
    }

    public Optional<List<Purchase>> getByCliente(String clienteId){
        return repository.getByCliente(clienteId);
    }

    public Purchase save(Purchase purchase) {
        return repository.save(purchase);
    }
}
