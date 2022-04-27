package com.codesoom.assignment.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepositoryDouble {
    List<Map<Long, Product>> products = new ArrayList<Map<Long, Product>>();

    private final Long newId = 0L;
}
