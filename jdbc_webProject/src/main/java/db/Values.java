//$Id$
package db;

import java.util.List;
import java.util.ArrayList;

public class Values {
	
	private int index;
	private List<Object> objects;
	
	public Values() {
		objects = new ArrayList<>();
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void addObject(Object object) {
		this.objects.add(object);
	}
	
	public int getSize() {
		return objects.size();
	}
	
	public int getIndex() {
		return index;
	}
	
	public List<Object> getObjectList() {
		return objects;
	}
	
	public Object getObjectAtIndex(int index) {
		return objects.get(index);
	}
	
}
