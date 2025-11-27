
package com.klef.fsd.service;

import java.util.List;
import java.util.Map;

import com.klef.fsd.model.Admin;
import com.klef.fsd.model.Buyer;
import com.klef.fsd.model.Farmer;

public interface AdminService 
{

  public Admin checkadminlogin(String username,String password);
  
  public String addFarmer(Farmer farmer);
  
  public List<Farmer> viewFarmers();
  
  public List<Buyer> viewBuyers();
  
  public String deleteFarmer(int id);
  
  public String deleteBuyer(int id);
  
  public List<Farmer> viewPendingFarmers();
  public String approveFarmer(int farmerId);

  long getTotalFarmers();
  long getTotalBuyers();
  long getTotalProducts();
  long getTotalOrders();
  double getTotalRevenue();
  List<Map<String, Object>> getSalesData(String period);
  
  
}
