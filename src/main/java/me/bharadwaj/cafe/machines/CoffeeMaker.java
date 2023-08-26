package me.bharadwaj.cafe.machines;

import lombok.Data;

@Data
public class CoffeeMaker {
	
	private CoffeeInventory inventory;
	
	public CoffeeMaker(CoffeeInventory inventory) {
		this.inventory = inventory;
	}
	
	public void makeFor(double quantity, String name) {
		var neededWater = quantity * 1.0 / 3.0;
		quantity = quantity - neededWater;

		var neededMilk = quantity * 1.0 / 4.0;
		quantity = quantity - neededMilk;

		var neededBeans = quantity;

		var neededInventory = new CoffeeInventory(neededWater, neededMilk, neededBeans);

		if (!isSufficient(neededInventory)) {
			System.out.println("sorry, refil your inventory and try again!");
			return;
		}

		System.out.println(String.format("Your coffee '%s' is ready, it took: %.2fml Water, %.2fml milk & %.2fgm beans",
				name, neededWater, neededMilk, neededBeans));

	}

	private boolean isSufficient(CoffeeInventory coffeeInventory) {
		var comparison = this.inventory.compareTo(coffeeInventory);
		return comparison > 0 ? true : (comparison == 0 ? true : false);
	}

}
