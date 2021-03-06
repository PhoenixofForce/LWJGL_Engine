package window.gui;

import maths.SmoothFloat;
import org.lwjgl.glfw.GLFW;
import window.listener.ButtonClickListener;

import java.awt.*;

public class GuiButton extends BasicColorGuiElement {

	//TODO: Button Text

	private SmoothFloat size;
	private ButtonClickListener listener;

	public GuiButton(GuiConfig config) {
		super(config);
	}

	@Override
	protected void initComponent() {
		super.initComponent();
		setColors(new Color(85, 112, 138), new Color(47, 98, 161));

		this.size = new SmoothFloat(1);
	}

	@Override
	public void onClick(int event, int button) {
		super.onClick(event, button);
		if(event == GLFW.GLFW_RELEASE) {
			size.setSmooth(1, 50);
			if(listener != null) listener.onClick();
		} else {
			size.set(0.8f);
		}
	}

	@Override
	public void updateComponent(long dt) {
		size.update();
		if(size.getDisplayValue() >= 1) {
			if(isMouseEntered()) {
				size.set(1.05f);
			} else {
				size.set(1);
			}
		}
		this.setScale(size.getDisplayValue(), size.getDisplayValue());
	}

	public void setClickListener(ButtonClickListener listener) {
		this.listener = listener;
	}
}
