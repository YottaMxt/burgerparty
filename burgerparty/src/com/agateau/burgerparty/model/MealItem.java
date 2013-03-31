package com.agateau.burgerparty.model;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.XmlReader;

public class MealItem {
	public enum Type {
		SIDE_ORDER,
		DRINK,
		BURGER
	}
	public static MealItem get(String name) {
		if (sMap.size == 0) {
			initMap();
		}
		return sMap.get(name);
	}

	public Type getType() {
		return mType;
	}

	public String getName() {
		return mName;
	}

	protected MealItem(XmlReader.Element element) {
		mName = element.getAttribute("name");
	}

	private static void initMap() {
		FileHandle handle = Gdx.files.internal("mealitems.xml");
		XmlReader.Element root = null;
		try {
			XmlReader reader = new XmlReader();
			root = reader.parse(handle);
		} catch (IOException e) {
			Gdx.app.error("MealItem.initMap", "Failed to load items definition from " + handle.path() + ". Exception: " + e.toString());
			return;
		}

		for(int idx = 0; idx < root.getChildCount(); ++idx) {
			XmlReader.Element element = root.getChild(idx);
			String type = element.getAttribute("type");
			MealItem item = null;
			if (type.equals("burger")) {
				item = new BurgerItem(element);
				item.mType = Type.BURGER;
			} else if (type.equals("drink")) {
				item = new MealItem(element);
				item.mType = Type.DRINK;
			} else if (type.equals("side-order")) {
				item = new MealItem(element);
				item.mType = Type.SIDE_ORDER;
			}
			assert(item != null);
			sMap.put(item.mName, item);
		}
	}

	private Type mType;
	private String mName;

	private static OrderedMap<String, MealItem> sMap = new OrderedMap<String, MealItem>();
}
