# Selective Print Testing Guide

## 🧪 **How to Test the Selective Print Functionality**

### **Prerequisites**
1. Start the application: `mvn spring-boot:run`
2. Navigate to: `http://localhost:8080/transactions`
3. Ensure transactions data is loaded in the table

## 🔍 **Test Scenarios**

### **Test 1: Basic Selection**
1. **Action**: Click checkbox next to any transaction
2. **Expected Results**:
   - ✅ Row gets light gray background
   - ✅ Selection info panel appears
   - ✅ "Print Selected" button becomes enabled
   - ✅ Counter shows "1 transaction selected"

### **Test 2: Select All Functionality**
1. **Action**: Click the checkbox in table header
2. **Expected Results**:
   - ✅ All transaction rows get selected
   - ✅ All rows have gray background
   - ✅ Counter shows "X transactions selected" (where X = total)
   - ✅ "Print Selected" button is enabled

### **Test 3: Clear Selection**
1. **Action**: After selecting some items, click "Clear Selection"
2. **Expected Results**:
   - ✅ All checkboxes become unchecked
   - ✅ Gray backgrounds disappear
   - ✅ Selection info panel hides
   - ✅ "Print Selected" button becomes disabled

### **Test 4: Print Selected Records**
1. **Setup**: Select 2-3 specific transactions
2. **Action**: Click "Print Selected" button
3. **Expected Results**:
   - ✅ Print dialog opens
   - ✅ Print preview shows only selected transactions
   - ✅ Header shows "Selected Transactions Report (X items)"
   - ✅ Statistics reflect only selected items
   - ✅ Non-selected rows are hidden

### **Test 5: Print All vs Print Selected**
1. **Setup**: Select some transactions (not all)
2. **Action**: Compare "Print All" vs "Print Selected"
3. **Expected Results**:
   - ✅ "Print All" shows complete table
   - ✅ "Print Selected" shows only selected rows
   - ✅ Headers are different
   - ✅ Statistics are calculated differently

### **Test 6: Export Selected CSV**
1. **Setup**: Select specific transactions
2. **Action**: Click "Export" → "Export Selected CSV"
3. **Expected Results**:
   - ✅ CSV file downloads automatically
   - ✅ Filename includes "selected_transactions"
   - ✅ File contains only selected records
   - ✅ Headers are included (no checkbox column)

### **Test 7: Button State Management**
1. **Test Disabled States**:
   - ✅ "Print Selected" disabled when no selection
   - ✅ "Export Selected" options grayed out when no selection
   - ✅ Quick actions in selection panel disabled appropriately

2. **Test Enabled States**:
   - ✅ All selection-dependent buttons enable when items selected
   - ✅ Buttons remain functional during selection changes

### **Test 8: Selection Persistence**
1. **Action**: Select items, apply filters, search, etc.
2. **Expected Results**:
   - ✅ Selection clears when new data loads (expected behavior)
   - ✅ Selection state maintained during UI interactions
   - ✅ No memory leaks or performance issues

### **Test 9: Responsive Design**
1. **Desktop**: Test on full-width browser
2. **Tablet**: Resize to tablet width (~768px)
3. **Mobile**: Resize to mobile width (~375px)
4. **Expected Results**:
   - ✅ Checkboxes remain accessible on all sizes
   - ✅ Buttons stack appropriately on smaller screens
   - ✅ Selection info panel remains readable
   - ✅ Print functionality works on all devices

### **Test 10: Edge Cases**
1. **Empty Selection Print**: Try to print with no selection
   - ✅ Shows alert: "Please select at least one transaction to print."

2. **Empty Selection Export**: Try to export with no selection
   - ✅ Shows alert: "Please select at least one transaction to export."

3. **Single Item Selection**: Select only one item
   - ✅ Counter shows "1 transaction selected" (singular)
   - ✅ Print works correctly for single item

4. **All Items Deselected**: Uncheck "Select All" after checking it
   - ✅ All items become unselected
   - ✅ UI returns to no-selection state

## 📊 **Expected Print Output Comparison**

### **Print All Output**
```
GlobalVen - Transaction Report
Generated on: [Date/Time]
Total Records: 10

Summary:
Total Transfers: 10    Pending: 3    Approved: 4    Processed: 3

[All 10 transactions displayed]
```

### **Print Selected Output (3 selected)**
```
GlobalVen - Selected Transactions Report (3 items)
Generated on: [Date/Time]
Total Records: 3

Summary:
Total Transfers: 3    Pending: 1    Approved: 1    Processed: 1

[Only 3 selected transactions displayed]
```

## 🎯 **Visual Verification Points**

### **Selection Visual Cues**
- [ ] Selected rows have light gray background (#f8f9fa)
- [ ] Checkboxes are properly sized (1.2x scale)
- [ ] Selection info panel has blue border and background
- [ ] Button states change color (enabled vs disabled)

### **Print Preview Verification**
- [ ] Only selected rows visible in print preview
- [ ] Checkbox column hidden in print
- [ ] Action buttons column hidden in print
- [ ] Print header shows correct title and count
- [ ] Print summary shows correct statistics

### **Export File Verification**
- [ ] CSV file contains correct number of rows
- [ ] CSV headers exclude checkbox and actions columns
- [ ] CSV filename indicates "selected" vs "all"
- [ ] CSV data matches selected records exactly

## 🚨 **Common Issues & Solutions**

### **Issue**: Checkboxes not working
**Solution**: 
- Check browser JavaScript console for errors
- Verify `toggleTransactionSelection` function is defined
- Refresh page and try again

### **Issue**: Selection not clearing
**Solution**:
- Check `selectedTransactions.clear()` is called
- Verify `updateSelectionInfo()` is working
- Try manual clear using "Clear Selection" button

### **Issue**: Print shows all records instead of selected
**Solution**:
- Verify `updatePrintData('selected')` is called
- Check row hiding logic in print function
- Ensure selected transaction IDs are in Set

### **Issue**: Export includes wrong records
**Solution**:
- Check CSV filtering logic
- Verify checkbox value attribute matches transaction ID
- Test with different selection combinations

## ✅ **Test Completion Checklist**

### **Basic Functionality**
- [ ] Individual selection works
- [ ] Select All/Clear All works
- [ ] Visual feedback (row highlighting) works
- [ ] Selection counter updates correctly

### **Print Functionality**
- [ ] Print All works (original functionality)
- [ ] Print Selected works (new functionality)
- [ ] Print preview shows correct records
- [ ] Print headers differentiate between modes

### **Export Functionality**
- [ ] Export All CSV works
- [ ] Export Selected CSV works
- [ ] Export filenames are correct
- [ ] Export content matches selection

### **User Interface**
- [ ] Button states change appropriately
- [ ] Selection info panel shows/hides correctly
- [ ] Responsive design works on all screen sizes
- [ ] Error messages show for invalid operations

### **Edge Cases**
- [ ] Empty selection handling works
- [ ] Single item selection works
- [ ] Large selection (all items) works
- [ ] Selection persistence during page interactions

---

**Testing Time**: ~30 minutes for complete test suite  
**Priority**: High (core functionality)  
**Browser Testing**: Test on Chrome, Firefox, Safari, Edge

*Complete this testing checklist before deploying the selective print functionality to production.*
