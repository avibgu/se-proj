package actor;

import java.util.UUID;

public class Entity {
	protected String id;
	
	public Entity(){
		id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
