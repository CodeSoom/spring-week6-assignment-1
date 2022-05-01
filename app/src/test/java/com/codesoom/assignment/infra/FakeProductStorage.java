package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FakeProductStorage implements ProductRepository {
    private Map<Long, Product> productStorage = new ConcurrentHashMap<Long, Product>();


    @Override
    public List<Product> findAll() {
        return new ArrayList<Product>(productStorage.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productStorage.get(id));
    }

    @Override
    public Product save(Product product) {
        Long productId = createProductId();
        product = product.initProductId(productId);
        this.productStorage.put(productId, product);
        return product;
    }

    @Override
    public void delete(Product product) {
        this.productStorage.remove(product.getId());
    }

    private Long createProductId() {
        List<Long> sortedStorageIds = sortedStorageIds();

        if (sortedStorageIds.size() == 0) {
            return 0L;
        }
        return sortedStorageIds.get(Math.decrementExact(sortedStorageIds.size()));
    }

    private List<Long> sortedStorageIds() {
        return productStorage.keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
