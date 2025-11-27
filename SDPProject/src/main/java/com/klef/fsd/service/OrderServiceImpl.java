package com.klef.fsd.service;

import com.klef.fsd.dto.OrderDTO;
import com.klef.fsd.dto.ProductDTO;
import com.klef.fsd.model.Buyer;
import com.klef.fsd.model.Order;
import com.klef.fsd.repository.BuyerRepository;
import com.klef.fsd.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    public Order createOrder(Order order) {
        // Basic null check only - validation is already done in PaymentController
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        // Simply save the order - all validations are done before calling this method
        return orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getOrdersByBuyerId(int buyerId) {
        Optional<Buyer> buyerOpt = buyerRepository.findById(buyerId);
        if (!buyerOpt.isPresent()) {
            throw new IllegalArgumentException("Buyer does not exist");
        }

        List<Order> orders = orderRepository.findByBuyerId(buyerId);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setQuantity(order.getQuantity());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setAddress(order.getAddress());

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(order.getProduct().getId());
            productDTO.setName(order.getProduct().getName());
            productDTO.setCategory(order.getProduct().getCategory());
            productDTO.setDescription(order.getProduct().getDescription());
            productDTO.setCost(order.getProduct().getCost());
            productDTO.setFarmer_id(order.getFarmer().getId());

            orderDTO.setProduct(productDTO);
            orderDTOs.add(orderDTO);
        }

        return orderDTOs;
    }

    @Override
    public List<OrderDTO> getOrdersByFarmerId(int farmerId) {
        List<Order> orders = orderRepository.findByFarmerId(farmerId);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setQuantity(order.getQuantity());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setBuyerName(order.getBuyer().getName());
            orderDTO.setBuyerEmail(order.getBuyer().getEmail());
            orderDTO.setAddress(order.getAddress());

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(order.getProduct().getId());
            productDTO.setName(order.getProduct().getName());
            productDTO.setCategory(order.getProduct().getCategory());
            productDTO.setDescription(order.getProduct().getDescription());
            productDTO.setCost(order.getProduct().getCost());
            productDTO.setFarmer_id(order.getFarmer().getId());

            orderDTO.setProduct(productDTO);
            orderDTOs.add(orderDTO);
        }

        return orderDTOs;
    }
}