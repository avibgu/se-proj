package movaProj.agent;

import type.MessageType;

/**
 * This class represents message that used to notify the client on events 
 * in the system.
 *
 */
public class MovaMessage {
	private MessageType messageType;
	private Object data;
	
	public MovaMessage(MessageType messageType, Object data) {
		super();
		this.messageType = messageType;
		this.data = data;
	}
	
	/**
	 * The type of the message.
	 * @return the message type.
	 */
	public MessageType getMessageType() {
		return messageType;
	}
	
	/**
	 * Set the message type.
	 * @param messageType Enum with the available messages.
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	/**
	 * Retrieve the event data object.
	 * @return the event data.
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * Set the event data object.
	 * @param data the data object of the event.
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
}
