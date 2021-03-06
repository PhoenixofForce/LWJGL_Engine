package window.gui;

import assets.models.TextModel;
import rendering.Renderer;
import rendering.ShaderHandler;
import rendering.uniform.MassUniform;
import window.Window;
import window.font.Font;
import window.font.StaticText;
import window.font.Text;

import java.util.Optional;

public class GuiText extends GuiElement {

	private Font font;
	private float fontSize;

	private TextModel model;
	private Text text;

	private long writerDuration = 0;
	private long displayTime = 0;

	private long clearAfterMS;
	private boolean shouldRebuild;

	private boolean fixedWidth;
	private boolean fixedHeight;

	/*
        There are two types of texts
        With the first type you specify the Text

        Lorem Ipsum
        Dolor sit

        And the width and height get set automatically

        +-----------+
        |Lorem Ipsum|
        |Dolor sit  |
        +-----------+

        And with the second type you specify the width of the box (5)
        +-----+
        +-----+

        And the Text gets matched by that bounds
        +-----+
        |Lorem|
        |Ipsum|
        |Dolor|
        |sit  |
        +-----+
	 */

	public GuiText(GuiConfig config, Font font, float fontSize) {
		this(config, font, fontSize, 0);
	}

	public GuiText(GuiConfig config, Font font, float fontSize, long writerDuration) {
		super(config);

		this.text = new StaticText();

		this.font = font;
		this.fontSize = fontSize;

		fixedWidth =  width != 0;
		fixedHeight = height != 0;
		clear(writerDuration, -1);
	}

	@Override
	protected void initComponent() {
		//super.initComponent();
	}

	@Override
	public void updateGui(long dt) {
		super.updateGui(dt);

		if(shouldRebuild || text.hasChanged()) {
			build();
			shouldRebuild = false;
		}

		displayTime += dt;

		if(clearAfterMS >= 0 && displayTime >= clearAfterMS + writerDuration) {
			this.setText(" ", 0, -1);
		}
	}

	@Override
	public void renderComponent() {
		if(model != null) {
			//super.renderComponent();

			//(translationX, translationY) needs to be the center of the first char
			//getCenterX points to center of the whole text
			//-getWidth/2 to the left side of the first letter
			//+fontSize (aka width)/2 to the middle of the first letter
			float width = font.getWidth(text.getFirstChar() + "", fontSize);
			float translationX = toScreenSpace(getCenterX() - getWidth() / 2 + (fontSize / width) / 2, Window.INSTANCE.getWidth());
			float translationY = toScreenSpace(getCenterY() + getHeight() / 2 - (fontSize / 2), Window.INSTANCE.getHeight());

			ShaderHandler.ShaderType type = ShaderHandler.ShaderType.TEXT;
			MassUniform u = new MassUniform();
			u.setTextures(font.getAtlas());
			u.setFloats(translationX, translationY,
					getCenterX(), getCenterY(), getWidth(), getHeight(),
					Window.INSTANCE.getWidth(), Window.INSTANCE.getHeight(),
					Window.INSTANCE.getRuntime(), model.charCount(), (float) displayTime / writerDuration);

			Renderer.renderArraysInstanced(type, model, u, model.charCount());
		}
	}

	@Override
	public boolean resizeComponent() {
		boolean out = super.resizeComponent();

		if(out) {
			shouldRebuild = true;
		}

		return out;
	}

	@Override
	public void cleanUpComponent() {
		if(model != null) {
			model.cleanUp();
		}
	}

	//>--| BUILDER |--<\\

	public GuiText setFont(Optional<Font> font, float fontSize) {
		this.font = font.orElse(this.font);
		this.fontSize = fontSize;
		build();
		return this;
	}

	public GuiText setText(Text text, long writerDuration, long clearAfterMS) {
		this.clear(writerDuration, clearAfterMS);
		this.text = text;
		build();
		return this;
	}

	public GuiText setText(Text text) {
		return this.setText(text, 0, -1L);
	}

	public GuiText setText(String s, long writerDuration, long clearAfterMS) {
		this.clear(writerDuration, clearAfterMS);
		this.text = new StaticText().addText(s);
		build();
		return this;
	}

	public GuiText setText(String s) {
		return this.setText(s, 0, -1);
	}

	private void clear(long writerDurationPerChar, long clearAfterMS) {
		this.writerDuration = writerDurationPerChar;
		this.clearAfterMS = clearAfterMS;
		this.text = new StaticText();
	}

	private void build() {
		if(model == null) {
			model = new TextModel(getWidth(), getHeight());
		}

		model.clear(getWidth(), getHeight());
		model.updateInstance(font, fontSize, getWidth(), text);

		//writer duration is set per char, so we have to multiply it with the amount of characters
		displayTime = -writerDuration;
		writerDuration *= model.charCount();

		if(!fixedWidth) this.setRawWidth(model.getWidth());
		if(!fixedHeight) this.setRawHeight(model.getHeight());
	}
}