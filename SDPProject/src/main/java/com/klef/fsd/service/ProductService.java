package com.klef.fsd.service;

import java.util.List;

import com.klef.fsd.model.Product;

public interface ProductService {

	public String addProduct(Product product);
	
	public String updateProduct(Product product);
	
	public String deleteProduct(int pid);

	public List<Product> viewallProducts();

	public List<Product> viewProductsByFarmer(int fid);

	public List<Product> viewProductsByCategory(String category);
	
	public Product viewProductById(int fid);
	
	public Product getProductById(int pid);

	
	
	

}
