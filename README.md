# Electrical Store Management System

> Overview description of this project: We are building the Java back-end of a web-based electronics store’s checkout
> system

# Technologies are used

- Use Java version 17
- Spring framework version 3
- Database System MySQL

# Architecture System:

- **Relationship database:**
  <picture><img src="https://github.com/letung999/PPN/blob/master/images/DB_system.png?raw=true">
- **Schema System:**
  <picture><img src="https://github.com/letung999/PPN/blob/master/ElectronicApplicationManagement.png?raw=true">

# Main function

- Admin User Operations:
    * Create a new product
    * Remove a product
    * Admin endpoints should support pagination.
    * Add discount deals for products (Example: Buy 1 get 50% off the second)
    * Admin can add an expiration date/time to deals.
      <picture>
          <source media="(prefers-color-scheme: dark)" srcset="https://github.com/letung999/PPN/blob/ad1b4b491f56089a798491e2de8d182ad180fa50/images/admin_operation.png?raw=true">
          <source media="(prefers-color-scheme: light)" srcset="https://github.com/letung999/PPN/blob/ad1b4b491f56089a798491e2de8d182ad180fa50/images/admin_operation.png?raw=true">
          <img alt="The follow of system" src="https://github.com/letung999/PPN/blob/ad1b4b491f56089a798491e2de8d182ad180fa50/images/admin_operation.png?raw=true">
      </picture>

- Customer Operations:
    * Add and remove products to and from a basket
    * Calculate a receipt of items, including all purchases, deals applied and total
      price
    * Products can be filtered by category, price range, or availability.
    * Customer endpoints should support pagination.
    * Products have limited stock.
    * When a customer adds a product to the basket, decrement stock accordingly.
    * If stock is insufficient, the operation should fail gracefully.
      <picture>
          <source media="(prefers-color-scheme: dark)" srcset="https://raw.githubusercontent.com/letung999/PPN/1d47a54690ac1c072e17632e50853f49237093eb/images/Screenshot%202025-08-24%20at%2023.27.32.png">
          <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/letung999/PPN/1d47a54690ac1c072e17632e50853f49237093eb/images/Screenshot%202025-08-24%20at%2023.27.32.png">
          <img alt="The follow of system" src="https://raw.githubusercontent.com/letung999/PPN/1d47a54690ac1c072e17632e50853f49237093eb/images/Screenshot%202025-08-24%20at%2023.27.32.png">
      </picture>

# Sequence Diagram

* removeProductById(Long id)
  <picture><img src="https://github.com/letung999/PPN/blob/master/images/sequence_remove_Product_ID.png?raw=true">
* addDeal(List<AddDealRequest> requests)
  <picture><img src="https://github.com/letung999/PPN/blob/master/images/sequences_add_deal.png?raw=true">
* calculateReceipt(Long customerId)
  <picture><img src="https://github.com/letung999/PPN/blob/master/images/sequence_about_reciptCalculate.png?raw=true">


# To run Application

- swagger host: `http://localhost:8082/swagger-ui/index.html#/`
- clone sources from repository `git clone https://github.com/letung999/Electronic_Application_Management.git`
- load dependencies with maven `mvn clean install`
- run main application class `ElectronicApplicationManagementApplication`