package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MemoryProductRepository implements ProductRepository {
    private final ConcurrentMap<Long, Product> store = new ConcurrentHashMap<>();


    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> product = Optional.of(store.get(id));
        return product;
    }

    @Override
    public Product save(Product product) {
        return store.put(product.getId(), product);
    }

    @Override
    public void delete(Product product) {
        store.remove(product.getId());
    }
}
