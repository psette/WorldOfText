package messages;

import resource.Factory;

public class FactoryMessage implements Message {
	private static final long serialVersionUID = 1L;
	private Factory factory;

	public FactoryMessage(Factory factory) {
		this.factory = factory;
	}

	public Factory getFactory() {
		return this.factory;
	};
}
