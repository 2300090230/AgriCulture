package com.klef.fsd.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import com.klef.fsd.model.Farmer;
import com.klef.fsd.service.FarmerService;

@RestController
@RequestMapping("/farmer")
@CrossOrigin("*")
public class FarmerController {

	@Autowired
	private FarmerService farmerService;

	// Existing endpoints (unchanged)
	@PostMapping("/registration")
	public ResponseEntity<?> farmerRegistration(@RequestBody Farmer farmer) {
		try {
			String output = farmerService.farmerRegistration(farmer);
			return ResponseEntity.ok(output);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Farmer registration failed.");
		}
	}

	@PostMapping("/checkfarmerlogin")
	public ResponseEntity<?> checkFarmerLogin(@RequestBody Farmer farmer) {
		Farmer f = farmerService.checkFarmerLogin(farmer.getUsername(), farmer.getPassword());
		if (f != null) {
			return ResponseEntity.ok(f);
		} else {
			return ResponseEntity.status(401).body("Invalid credentials or not approved.");
		}
	}

	
  @GetMapping("/pending")
  public ResponseEntity<List<Farmer>> viewPendingFarmers() {
    List<Farmer> list = farmerService.viewPendingFarmers();
    return ResponseEntity.ok(list);
  }
//
//  @PutMapping("/approve/{id}")
//  public ResponseEntity<String> approveSeller(@PathVariable("id") int id) {
//    String result = sellerService.approveSeller(id);
//    return ResponseEntity.ok(result);
//  }

  @PutMapping("/reject/{id}")
  public ResponseEntity<String> rejectFarmer(@PathVariable("id") int id) {
    String result = farmerService.rejectFarmer(id);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteFarmer(@RequestParam("id") int id) {
    String result = farmerService.deleteFarmer(id);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/updatefarmer")
  public ResponseEntity<String> farmerupdateprofile(@RequestBody Farmer farmer) {
    try {
      System.out.println(farmer.toString());
      String output = farmerService.updateFarmerProfile(farmer);
      return ResponseEntity.ok(output);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.status(500).body("Failed to Update Farmer ... !!");
    }
  }

  @GetMapping("/viewallfarmers")
  public List<Farmer> viewAllFarmers() {

    return farmerService.viewAllFarmers();

  }
	@GetMapping("/{farmerId}/products/count")
	public ResponseEntity<Map<?, ?>> getTotalProducts(@PathVariable("farmerId") int farmerId) {
		try {
			long count = farmerService.getTotalProductsByFarmer(farmerId);
			return ResponseEntity.ok(Map.of("farmerId", (long) farmerId, "totalProducts", count));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch product count: " + e.getMessage()));
		}
	}

	@GetMapping("/{farmerId}/orders/count")
	public ResponseEntity<Map<?, ?>> getTotalOrders(@PathVariable("farmerId") int farmerId) {
		try {
			long count = farmerService.getTotalOrdersByFarmer(farmerId);
			return ResponseEntity.ok(Map.of("farmerId", (long) farmerId, "totalOrders", count));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch order count: " + e.getMessage()));
		}
	}

	@GetMapping("/{farmerId}/revenue")
	public ResponseEntity<Map<?, ?>> getTotalRevenue(@PathVariable("farmerId") int farmerId) {
		try {
			double revenue = farmerService.getTotalRevenueByFarmer(farmerId);
			return ResponseEntity.ok(Map.of("farmerId", (long) farmerId, "totalRevenue", revenue));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch revenue: " + e.getMessage()));
		}
	}

	@GetMapping("/{farmerId}/sales-data")
	public ResponseEntity<List<Map<String, Object>>> getSalesData(@PathVariable("farmerId") int farmerId,
			@RequestParam(value = "period", defaultValue = "daily") String period) {
		try {
			List<Map<String, Object>> salesData = farmerService.getSalesDataByFarmer(farmerId, period);
			return ResponseEntity.ok(salesData);
		} catch (Exception e) {
			return ResponseEntity.status(500)
					.body(List.of(Map.of("error", "Failed to fetch sales data: " + e.getMessage())));
		}
	}
@PostMapping("/fforgot-password")
  public ResponseEntity<String> forgotPassword(@RequestParam String email) {
    String result = farmerService.generateResetToken(email);
    if (result.equals("Farmer not found!")) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
    return ResponseEntity.ok(result);
  }

  @PostMapping("/freset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
    String result = farmerService.resetPassword(token, newPassword);
    if (result.equals("Invalid token!")) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    return ResponseEntity.ok(result);
  }
}
