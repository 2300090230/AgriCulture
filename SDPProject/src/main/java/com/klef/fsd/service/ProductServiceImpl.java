package com.klef.fsd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsd.model.Product;
import com.klef.fsd.model.Farmer;
import com.klef.fsd.repository.ProductRepository;
import com.klef.fsd.repository.FarmerRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FarmerRepository farmerRepository;

	@Override
	public String addProduct(Product product) {
		productRepository.save(product);
		return "Product Added Successfully";
	}

	@Override
	public List<Product> viewallProducts() {

		return productRepository.findAll();
	}

	@Override
	public List<Product> viewProductsByCategory(String category) {

		return productRepository.findByCategory(category);
	}

	@Override
	public List<Product> viewProductsByFarmer(int fid) {

		Farmer farmer = farmerRepository.findById(fid).orElse(null);
		return productRepository.findByFarmer(farmer);
	}

	@Override
	public String deleteProduct(int pid) {
		Optional<Product> product = productRepository.findById(pid);
		String msg = null;
		if (product.isPresent()) {
			Product p = product.get();
			productRepository.delete(p);
			msg = "Product Deleted Successfully";
		} else {
			msg = "Product Not found";
		}

		return msg;
	}

	public Product viewProductById(int sid) {
		return productRepository.findById(sid).orElse(null);
	}

	@Override
	public String updateProduct(Product product) {

		productRepository.save(product);
		return "Product Updated Successfully";
	}

	public Product getProductById(int id) {
	    return productRepository.findById(id)
	            .orElse(null); // no throw
	}


}
