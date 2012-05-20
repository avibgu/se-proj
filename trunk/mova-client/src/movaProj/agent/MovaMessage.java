package movaProj.agent;

import type.MessageType;


public class MovaMessage {
	private MessageType messageType;
	private Object data;
	
	public MovaMessage(MessageType messageType, Object data) {
		super();
		this.messageType = messageType;
		this.data = data;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
