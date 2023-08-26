package me.bharadwaj.cafe.machines;

import java.util.LinkedList;

import lombok.Data;

@Data
public class CoffeeInventory implements Comparable<CoffeeInventory> {
	private double water;
	private double milk;
	private double beans;

	public CoffeeInventory(double water, double milk, double beans) {
		this.water = water;
		this.milk = milk;
		this.beans = beans;
	}

	@Override
	public int compareTo(CoffeeInventory o) {

		var list = new LinkedList<Integer>();

		list.add(Double.compare(this.water, o.getWater()));
		list.add(Double.compare(this.milk, o.getMilk()));
		list.add(Double.compare(this.beans, o.getBeans()));

		if (list.contains(Integer.valueOf(-1)))
			return -1;
		if (list.contains(Integer.valueOf(1)))
			return 1;

		return 0;

	}
}
