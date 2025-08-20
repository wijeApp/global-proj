package com.tas.global.globalven.controller;

import com.tas.global.globalven.TransfersMain;
import com.tas.global.globalven.Employee;
import com.tas.global.globalven.Rates;
import com.tas.global.globalven.TransactionType;
import com.tas.global.globalven.dto.CreateTransferRequest;
import com.tas.global.globalven.service.TransfersMainService;
import com.tas.global.globalven.service.EmployeeService;
import com.tas.global.globalven.service.RatesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class TransfersMainController {
      @Autowired
    private TransfersMainService transfersMainService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private RatesService ratesService;
    
    // CREATE - Add new transfer
    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestBody CreateTransferRequest request) {
        try {
            // Log the received request data
            System.out.println("Received transfer request: employeeId=" + request.getEmployeeId() + 
                             ", rateId=" + request.getRateId() + ", amount=" + request.getAmount());
            
            // Validate required fields
            if (request.getEmployeeId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Employee ID is required"));
            }
            
            if (request.getAmount() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Amount is required"));
            }
            
            // Fetch employee
            Optional<Employee> employeeOpt = employeeService.getEmployeeById(request.getEmployeeId());
            if (!employeeOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Employee not found with ID: " + request.getEmployeeId()));
            }
            
            // For now, let's get the first available rate if no rateId provided
            Rates rate = null;
            if (request.getRateId() != null) {
                Optional<Rates> rateOpt = ratesService.getRateById(request.getRateId());
                if (!rateOpt.isPresent()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Rate not found with ID: " + request.getRateId()));
                }
                rate = rateOpt.get();
            } else {
                // Get first available rate as fallback
                List<Rates> availableRates = ratesService.getAllRates();
                if (!availableRates.isEmpty()) {
                    rate = availableRates.get(0);
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "No rates available in the system"));
                }
            }
            
            // Create TransfersMain object
            TransfersMain transfer = new TransfersMain();
            transfer.setEmployee(employeeOpt.get());
            transfer.setRate(rate);
            transfer.setTransactionType(request.getTransactionType() != null ? request.getTransactionType() : TransactionType.OTHER);
            transfer.setAmount(request.getAmount());
            transfer.setHoursWorked(request.getHoursWorked());
            transfer.setDescription(request.getDescription());
            transfer.setCurrency(request.getCurrency() != null ? request.getCurrency() : "USD");
            transfer.setNotes(request.getNotes());
            transfer.setGlrefcode(request.getGlrefcode());
            transfer.setTransactionDate(LocalDateTime.now());
            transfer.setStatus("PENDING");
            transfer.setIsActive(true);
            
            TransfersMain savedTransfer = transfersMainService.saveTransfer(transfer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTransfer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create transfer: " + e.getMessage()));
        }
    }
    
    // READ - Get all transfers
    @GetMapping
    public ResponseEntity<List<TransfersMain>> getAllTransfers() {
        try {
            List<TransfersMain> transfers = transfersMainService.getAllTransfers();
            if (transfers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfer by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransfersMain> getTransferById(@PathVariable Long id) {
        try {
            Optional<TransfersMain> transfer = transfersMainService.getTransferById(id);
            if (transfer.isPresent()) {
                return ResponseEntity.ok(transfer.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfers by employee ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TransfersMain>> getTransfersByEmployee(@PathVariable Long employeeId) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            if (employee.isPresent()) {
                List<TransfersMain> transfers = transfersMainService.getTransfersByEmployee(employee.get());
                return ResponseEntity.ok(transfers);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }    }
    
    // READ - Get transfers by transaction type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransfersMain>> getTransfersByType(@PathVariable String type) {
        try {
            TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
            List<TransfersMain> transfers = transfersMainService.getTransfersByType(transactionType);
            return ResponseEntity.ok(transfers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfers by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransfersMain>> getTransfersByStatus(@PathVariable String status) {
        try {
            List<TransfersMain> transfers = transfersMainService.getTransfersByStatus(status.toUpperCase());
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfers by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<TransfersMain>> getTransfersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
            LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);
            
            List<TransfersMain> transfers = transfersMainService.getTransfersByDateRange(start, end);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfers by amount range
    @GetMapping("/amount-range")
    public ResponseEntity<List<TransfersMain>> getTransfersByAmountRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        try {
            List<TransfersMain> transfers = transfersMainService.getTransfersByAmountRange(minAmount, maxAmount);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfers by currency
    @GetMapping("/currency/{currency}")
    public ResponseEntity<List<TransfersMain>> getTransfersByCurrency(@PathVariable String currency) {
        try {
            List<TransfersMain> transfers = transfersMainService.getTransfersByCurrency(currency);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // SEARCH - Search transfers
    @GetMapping("/search")
    public ResponseEntity<List<TransfersMain>> searchTransfers(@RequestParam String keyword) {
        try {
            List<TransfersMain> transfers = transfersMainService.searchTransactions(keyword);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get pending transfers
    @GetMapping("/pending")
    public ResponseEntity<List<TransfersMain>> getPendingTransfers() {
        try {
            List<TransfersMain> transfers = transfersMainService.getAllPendingTransactions();
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Update transfer by ID
    @PutMapping("/{id}")
    public ResponseEntity<TransfersMain> updateTransfer(@PathVariable Long id, @RequestBody TransfersMain transferDetails) {
        try {
            TransfersMain updatedTransfer = transfersMainService.updateTransfer(id, transferDetails);
            return ResponseEntity.ok(updatedTransfer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Approve transfer
    @PatchMapping("/{id}/approve")
    public ResponseEntity<TransfersMain> approveTransfer(@PathVariable Long id, @RequestParam String approver) {
        try {
            TransfersMain approvedTransfer = transfersMainService.approveTransfer(id, approver);
            return ResponseEntity.ok(approvedTransfer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Reject transfer
    @PatchMapping("/{id}/reject")
    public ResponseEntity<TransfersMain> rejectTransfer(@PathVariable Long id, @RequestParam String rejector) {
        try {
            TransfersMain rejectedTransfer = transfersMainService.rejectTransfer(id, rejector);
            return ResponseEntity.ok(rejectedTransfer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UPDATE - Process transfer
    @PatchMapping("/{id}/process")
    public ResponseEntity<TransfersMain> processTransfer(@PathVariable Long id, @RequestParam String processor) {
        try {
            TransfersMain processedTransfer = transfersMainService.processTransfer(id, processor);
            return ResponseEntity.ok(processedTransfer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DELETE - Delete transfer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        try {
            boolean deleted = transfersMainService.deleteTransfer(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // STATISTICS - Get transfer statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getTransferStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Count by status
            stats.put("totalTransfers", transfersMainService.getAllTransfers().size());
            stats.put("pendingCount", transfersMainService.getCountByStatus("PENDING"));
            stats.put("approvedCount", transfersMainService.getCountByStatus("APPROVED"));
            stats.put("rejectedCount", transfersMainService.getCountByStatus("REJECTED"));
            stats.put("processedCount", transfersMainService.getCountByStatus("PROCESSED"));
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }    }
      // SEARCH - Search transfers by description
    @GetMapping("/search-description")
    public ResponseEntity<List<TransfersMain>> searchTransfersByDescription(@RequestParam String description) {
        try {
            List<TransfersMain> transfers = transfersMainService.searchTransactionsByDescription(description);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // SEARCH - Search transfers by reference number
    @GetMapping("/search-reference")
    public ResponseEntity<List<TransfersMain>> searchTransfersByReferenceNumber(@RequestParam String referenceNumber) {
        try {
            List<TransfersMain> transfers = transfersMainService.searchTransactionsByReferenceNumber(referenceNumber);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // READ - Get transfers by glrefcode
    @GetMapping("/glrefcode/{glrefcode}")
    public ResponseEntity<List<TransfersMain>> getTransfersByGlrefcode(@PathVariable String glrefcode) {
        try {
            List<TransfersMain> transfers = transfersMainService.getTransfersByGlrefcode(glrefcode);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // SEARCH - Search transfers by glrefcode
    @GetMapping("/search-glrefcode")
    public ResponseEntity<List<TransfersMain>> searchTransfersByGlrefcode(@RequestParam String glrefcode) {
        try {
            List<TransfersMain> transfers = transfersMainService.searchTransactionsByGlrefcode(glrefcode);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // UTILITY - Get transaction types
    @GetMapping("/transaction-types")
    public ResponseEntity<Map<String, String>> getTransactionTypes() {
        try {
            Map<String, String> types = new HashMap<>();
            for (TransactionType type : TransactionType.values()) {
                types.put(type.name(), type.getDisplayName());
            }
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
