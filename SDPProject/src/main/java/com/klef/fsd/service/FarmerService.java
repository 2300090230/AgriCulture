package com.klef.fsd.service;

import com.klef.fsd.model.Farmer;

import java.util.List;
import java.util.Map;

public interface FarmerService {
    // Existing methods
    String farmerRegistration(Farmer farmer);
    Farmer checkFarmerLogin(String username, String password);
    List<Farmer> viewPendingFarmers();
    String approveFarmer(int farmerId);
    String rejectFarmer(int id);
    String deleteFarmer(int id);
    Farmer getFarmerById(int fid);
    String updateFarmerProfile(Farmer farmer);
    List<Farmer> viewAllFarmers();
    String generateResetToken(String email);
    String resetPassword(String token, String newPassword);

    // New methods for dashboard
    long getTotalProductsByFarmer(int farmerId);
    long getTotalOrdersByFarmer(int farmerId);
    double getTotalRevenueByFarmer(int farmerId);
    List<Map<String, Object>> getSalesDataByFarmer(int farmerId, String period);
}