package com.klef.fsd.repository;
import com.klef.fsd.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByBuyerId(int buyerId);
    List<Order> findByFarmerId(int farmerId);
    Optional<Order> findByRazorpayPaymentId(String razorpayPaymentId);
    
    // Using JPQL functions that are database-agnostic
    @Query("SELECT FUNCTION('date_format', o.orderDate, '%Y-%m-%d') as date, COUNT(o) as orderCount, SUM(o.amount) as revenue " +
           "FROM Order o WHERE o.farmer.id = :farmerId AND o.orderDate >= :startDate " +
           "GROUP BY FUNCTION('date_format', o.orderDate, '%Y-%m-%d')")
    List<Object[]> getDailySalesData(@Param("farmerId") int farmerId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT FUNCTION('date_format', o.orderDate, '%Y-%m') as month, COUNT(o) as orderCount, SUM(o.amount) as revenue " +
           "FROM Order o WHERE o.farmer.id = :farmerId AND o.orderDate >= :startDate " +
           "GROUP BY FUNCTION('date_format', o.orderDate, '%Y-%m')")
    List<Object[]> getMonthlySalesData(@Param("farmerId") int farmerId, @Param("startDate") LocalDateTime startDate);
    
    // Admin queries for platform-wide sales data - modified to be more database-agnostic
    @Query("SELECT FUNCTION('date_format', o.orderDate, '%Y-%m-%d') as date, COUNT(o) as orderCount, SUM(o.amount) as revenue " +
           "FROM Order o WHERE o.orderDate >= :startDate " +
           "GROUP BY FUNCTION('date_format', o.orderDate, '%Y-%m-%d')")
    List<Object[]> getAdminDailySalesData(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT FUNCTION('date_format', o.orderDate, '%Y-%m') as month, COUNT(o) as orderCount, SUM(o.amount) as revenue " +
           "FROM Order o WHERE o.orderDate >= :startDate " +
           "GROUP BY FUNCTION('date_format', o.orderDate, '%Y-%m')")
    List<Object[]> getAdminMonthlySalesData(@Param("startDate") LocalDateTime startDate);
}