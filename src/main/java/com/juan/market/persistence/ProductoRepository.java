package com.juan.market.persistence;

import com.juan.market.domain.Product;
import com.juan.market.domain.repository.ProductRepository;
import com.juan.market.persistence.crud.ProductoCrudRepository;
import com.juan.market.persistence.entity.Producto;
import com.juan.market.persistence.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository implements ProductRepository {

    private ProductoCrudRepository productoCrudRepository;
    private ProductMapper mapper;

    public ProductoRepository(ProductoCrudRepository productoCrudRepository, ProductMapper mapper) {
        this.productoCrudRepository = productoCrudRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Product> getAll(){
        return mapper.toProducts((List<Producto>) productoCrudRepository.findAll());
    }

    @Override
    public Optional<List<Product>> getByCategory(int categoryId) {
        return Optional.of(mapper.toProducts(productoCrudRepository.findByIdCategoriaOrderByNombreAsc(categoryId)));
    }

    @Override
    public Optional<List<Product>> getScarseProducts(int quantity) {
        Optional<List<Producto>> productos =
                productoCrudRepository.findByCantidadStockLessThanAndEstado(quantity, true);
        return productos.map( prds -> mapper.toProducts(prds));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.findById(productId)
                .map(prod -> mapper.toProduct(prod));
    }

    @Override
    public Product save(Product product) {
        return mapper.toProduct(productoCrudRepository.save(mapper.toProducto(product)));
    }

    @Override
    public void delete(int productId) {
        productoCrudRepository.deleteById(productId);
    }
}