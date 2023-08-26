CREATE SCHEMA IF NOT EXISTS cafe;

CREATE TABLE IF NOT EXISTS cafe.item_types(
	id SERIAL PRIMARY KEY,
	value VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS cafe.items(
	id SERIAL PRIMARY KEY,
	name VARCHAR (50) UNIQUE NOT NULL,
	type_id INT NOT NULL,
	serving VARCHAR(50) NOT NULL,
	quantity DOUBLE PRECISION NOT NULL,
	price double precision NOT NULL,
		CONSTRAINT fk_type
			FOREIGN KEY(type_id) 
			REFERENCES cafe.item_types(id)
);

CREATE TABLE IF NOT EXISTS cafe.payments (
	id SERIAL PRIMARY KEY,
	amount_recorded REAL,
	settled boolean,
	create_date timestamptz NOT NULL,
	update_date timestamptz NOT NULL
);

CREATE TABLE IF NOT EXISTS cafe.orders(
	id SERIAL PRIMARY KEY,
	payment_id INT NOT NULL,
	create_date timestamptz NOT NULL,
	update_date timestamptz NOT NULL,
		CONSTRAINT fk_payment
			FOREIGN KEY(payment_id)
			REFERENCES cafe.payments(id)
);

CREATE TABLE IF NOT EXISTS cafe.selected_items(
	id SERIAL PRIMARY KEY,
	order_id INT NOT NULL,
	item_id INT NOT NULL,
		CONSTRAINT fk_orders
			FOREIGN KEY(order_id)
			REFERENCES cafe.orders(id),
		CONSTRAINT fk_item
			FOREIGN KEY(item_id)
			REFERENCES cafe.items(id)
);

INSERT INTO cafe.item_types(value)
VALUES
	('COFFEE')
ON CONFLICT ON CONSTRAINT item_types_value_key
DO NOTHING;

INSERT INTO cafe.items(name, type_id, serving, quantity, price)
VALUES
	('Coffee that makes you shit your pants real quick', 1, 'LARGE', 500, 90),
	('why would you want to punish yourself?', 1, 'MEDIUM', 250, 50),
	('nice coffee', 1, 'SMALL', 100, 20)
ON CONFLICT ON CONSTRAINT items_name_key
DO NOTHING;
