package com.klef.fsd.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsd.model.EmailDetails;
import com.klef.fsd.model.Order;
import com.klef.fsd.model.Farmer;
import com.klef.fsd.repository.OrderRepository;
import com.klef.fsd.repository.ProductRepository;
import com.klef.fsd.repository.FarmerRepository;

@Service
public class FarmerServiceImpl implements FarmerService {

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public String farmerRegistration(Farmer farmer) {
		farmer.setStatus("Pending");
		farmerRepository.save(farmer);
		return "Farmer Registered Successfully!";
	}

	@Override
	public Farmer checkFarmerLogin(String username, String password) {
		Farmer farmer = farmerRepository.findByUsernameAndPassword(username, password);
		if (farmer != null && "Approved".equalsIgnoreCase(farmer.getStatus())) {
			return farmer;
		}
		return null;
	}

	@Override
	public List<Farmer> viewPendingFarmers() {
		return farmerRepository.findByStatus("Pending");
	}

	@Override
	public String approveFarmer(int farmerId) {
		Optional<Farmer> optionalFarmer = farmerRepository.findById(farmerId);
		if (optionalFarmer.isPresent()) {
			Farmer farmer = optionalFarmer.get();
			farmer.setStatus("Approved");
			farmerRepository.save(farmer);
			return "Farmer approved successfully.";
		} else {
			return "Farmer not found.";
		}
	}

	@Override
	public String rejectFarmer(int id) {
		Optional<Farmer> optionalFarmer = farmerRepository.findById(id);
		if (optionalFarmer.isPresent()) {
			Farmer farmer = optionalFarmer.get();
			farmer.setStatus("Rejected");
			farmerRepository.save(farmer);
			return "Farmer rejected successfully";
		} else {
			return "Farmer not found";
		}
	}

	@Override
	public String deleteFarmer(int id) {
		Optional<Farmer> optionalFarmer = farmerRepository.findById(id);
		if (optionalFarmer.isPresent()) {
			farmerRepository.deleteById(id);
			return "Farmer deleted successfully";
		} else {
			return "Farmer not found";
		}
	}

	@Override
	public Farmer getFarmerById(int fid) {

		return farmerRepository.findById(fid).get();
	}

	@Override
	public String updateFarmerProfile(Farmer farmer) {
		Optional<Farmer> optionalFarmer = farmerRepository.findById(farmer.getId());
		if (optionalFarmer.isPresent()) {

			Farmer f = optionalFarmer.get();
			f.setLocation(farmer.getLocation());
			f.setMobileno(farmer.getMobileno());
			f.setNationalidno(farmer.getNationalidno());
			f.setUsername(f.getUsername());
			f.setEmail(farmer.getEmail());
			farmerRepository.save(f);
			return "Farmer Updated Successfully";
		} else {
			return "Farmer not found";
		}

	}

	@Override
	public List<Farmer> viewAllFarmers() {

		return farmerRepository.findAll();
	}

	@Override
	public String generateResetToken(String email) {
		Optional<Farmer> farmerOpt = farmerRepository.findByEmail(email);
		if (farmerOpt.isEmpty()) {
			return "Farmer not found!";
		}

		Farmer farmer = farmerOpt.get();
		String resetToken = UUID.randomUUID().toString();
		farmer.setResetToken(resetToken);
		farmerRepository.save(farmer);

		String resetLink = "https://sdpfrontend-rr9e.onrender.com/sreset-password?token=" + resetToken;

		EmailDetails mail = new EmailDetails();
		mail.setRecipient(email);
		mail.setSubject("🔐 Reset Your Password - LL-Cart");

		String htmlContent = "<h3>Hello from <span style='color:#2563EB;'>LL-Cart</span> 👋</h3>"
				+ "<p>We received a request to reset your password.</p>" + "<p><a href=\"" + resetLink + "\" "
				+ "style='padding:10px 20px; background-color:#2563EB; color:white; text-decoration:none; border-radius:5px;'>"
				+ "Click here to reset your password</a></p>"
				+ "<p>If you didn’t request this, please ignore this email.</p>"
				+ "<br><p>Regards,<br><b>LL-Cart Support Team</b></p>";

		mail.setMsgBody(htmlContent);
		emailService.sendHtmlMail(mail); // ✅ Use HTML method

		return "Reset link sent to your email";
	}

	@Override
	public String resetPassword(String token, String newPassword) {
		Farmer farmer = farmerRepository.findByResetToken(token);
		if (farmer == null) {
			return "Invalid token!";
		}

		farmer.setPassword(newPassword); // add encoder if needed
		farmer.setResetToken(null);
		farmerRepository.save(farmer);
		return "Password updated successfully!";
	}

	public long getTotalProductsByFarmer(int farmerId) {
		Farmer farmer = farmerRepository.findById(farmerId).orElse(null);
		if (farmer == null) {
			throw new IllegalArgumentException("Farmer not found");
		}
		return productRepository.findByFarmer(farmer).size();
	}

	@Override
	public long getTotalOrdersByFarmer(int farmerId) {
		return orderRepository.findByFarmerId(farmerId).size();
	}

	@Override
	public double getTotalRevenueByFarmer(int farmerId) {
		return orderRepository.findByFarmerId(farmerId).stream()
				.filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
				.mapToDouble(order -> order.getAmount()).sum();
	}

	@Override
	public List<Map<String, Object>> getSalesDataByFarmer(int farmerId, String period) {
		List<Map<String, Object>> salesData = new ArrayList<>();
		List<Order> orders = orderRepository.findByFarmerId(farmerId);

		if ("daily".equalsIgnoreCase(period)) {
			// Last 7 days
			LocalDate today = LocalDate.now();
			for (int i = 6; i >= 0; i--) {
				LocalDate date = today.minusDays(i);
				Map<String, Object> data = new HashMap<>();
				data.put("date", date.format(DateTimeFormatter.ISO_LOCAL_DATE));

				long orderCount = orders.stream().filter(order -> order.getOrderDate().toLocalDate().equals(date))
						.count();
				double revenue = orders.stream().filter(order -> order.getOrderDate().toLocalDate().equals(date))
						.filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
						.mapToDouble(order -> order.getAmount()).sum();

				data.put("orderCount", orderCount);
				data.put("revenue", revenue);
				salesData.add(data);
			}
		} else if ("monthly".equalsIgnoreCase(period)) {
			// Last 12 months
			LocalDate today = LocalDate.now();
			for (int i = 11; i >= 0; i--) {
				LocalDate monthStart = today.minusMonths(i).withDayOfMonth(1);
				String month = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM"));
				Map<String, Object> data = new HashMap<>();
				data.put("month", month);

				long orderCount = orders.stream()
						.filter(order -> order.getOrderDate().toLocalDate().getYear() == monthStart.getYear())
						.filter(order -> order.getOrderDate().toLocalDate().getMonth() == monthStart.getMonth())
						.count();
				double revenue = orders.stream()
						.filter(order -> order.getOrderDate().toLocalDate().getYear() == monthStart.getYear())
						.filter(order -> order.getOrderDate().toLocalDate().getMonth() == monthStart.getMonth())
						.filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
						.mapToDouble(order -> order.getAmount()).sum();

				data.put("orderCount", orderCount);
				data.put("revenue", revenue);
				salesData.add(data);
			}
		}

		return salesData;
	}
}
