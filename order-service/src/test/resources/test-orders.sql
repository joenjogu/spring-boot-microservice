truncate table orders cascade;
alter sequence order_id_seq restart with 100;
alter sequence order_item_id_seq restart with 100;

insert into orders(id, order_number, username, customer_name, customer_email, customer_phone, delivery_address_line1,
                   delivery_address_line2, delivery_address_city, delivery_address_state, delivery_address_zip_code,
                   delivery_address_country, status, comments)
values 
(1, 'ORD-10001', 'user', 'John Doe', 'john.doe@example.com', '+1-555-123-4567', '123 Main St', 'Apt 4B', 'New York', 'NY', '10001', 'India', 'NEW', 'Please deliver before 6pm'),
(2, 'ORD-10002', 'user', 'Jane Doe', 'jane.doe@example.com', '+1-555-987-6543', '456 Oak Ave', NULL, 'Los Angeles', 'CA', '90001', 'Kenya', 'IN_PROCESS', NULL),
(3, 'ORD-10003', 'user', 'Bob Smith', 'bob.smith@example.com', '+1-555-456-7890', '789 Pine Rd', 'Suite 101', 'Chicago', 'IL', '60601', 'Germany', 'DELIVERED', 'Left with neighbor'),
(4, 'ORD-10004', 'alicejones', 'Alice Jones', 'alice.jones@example.com', '+1-555-789-0123', '321 Elm St', NULL, 'Houston', 'TX', '77001', 'India', 'NEW', NULL),
(5, 'ORD-10005', 'mikebrown', 'Mike Brown', 'mike.brown@example.com', '+1-555-234-5678', '654 Maple Dr', 'Unit 7', 'Phoenix', 'AZ', '85001', 'Kenya', 'CANCELLED', 'Customer requested cancellation'),
(6, 'ORD-10006', 'sarahlee', 'Sarah Lee', 'sarah.lee@example.com', '+1-555-345-6789', '987 Cedar Ln', NULL, 'Philadelphia', 'PA', '19101', 'Germany', 'ERROR', 'Payment processing error'),
(7, 'ORD-10007', 'davidwilson', 'David Wilson', 'david.wilson@example.com', '+1-555-456-7890', '159 Birch Ave', 'Apt 12C', 'San Antonio', 'TX', '78201', 'India', 'IN_PROCESS', 'Priority shipping'),
(8, 'ORD-10008', 'emilytaylor', 'Emily Taylor', 'emily.taylor@example.com', '+1-555-567-8901', '753 Spruce Blvd', NULL, 'San Diego', 'CA', '92101', 'Kenya', 'DELIVERED', 'Signature required'),
(9, 'ORD-10009', 'jamesjohnson', 'James Johnson', 'james.johnson@example.com', '+1-555-678-9012', '246 Walnut St', 'Floor 3', 'Dallas', 'TX', '75201', 'Germany', 'NEW', NULL),
(10, 'ORD-10010', 'oliviamartin', 'Olivia Martin', 'olivia.martin@example.com', '+1-555-789-0123', '864 Pineapple Way', 'Suite 5', 'San Jose', 'CA', '95101', 'India', 'IN_PROCESS', 'Gift wrap requested');


insert into order_items(order_id, code, name, price, quantity)
values
(1, 'P100', 'Hunger Games', 34.0, 2),
(1, 'P101', 'To kill a mockingbird', 78.0, 5),
(2, 'P101', 'The chronicles of narnia', 50.0, 10);
