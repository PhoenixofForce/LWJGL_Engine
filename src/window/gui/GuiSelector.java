package window.gui;

import window.font.DynamicText;
import window.font.TextureAtlasFont;

import java.util.List;

public class GuiSelector extends GuiElement {

	private List<String> options;

	private int selectedOption;
	private int optionCount;

	private GuiButton left;
	private GuiButton right;
	private GuiText selectionDisplay;

	public GuiSelector(GuiConfig config, List<String> options) {
		super(config, false);
		this.options = options;
		initComponent();
	}

	@Override
	protected void initComponent() {
		left = new GuiButton(new GuiConfig(Anchor.BOTTOM_LEFT, 0, 0, 0.1f, 1f));
		right = new GuiButton(new GuiConfig(Anchor.BOTTOM_RIGHT, 1f, 0, 0.1f, 1f));
		selectionDisplay = new GuiText(new GuiConfig(Anchor.CENTERCENTER, 0.5f, 0.5f, 0, 0), new TextureAtlasFont("Font"), 16f);

		optionCount = options.size();
		setOption(0);

		selectionDisplay.setText(new DynamicText() {
			@Override
			public String getText() {
				return options == null? "": options.get(selectedOption);
			}
		});

		left.setClickListener(() -> setOption(selectedOption - 1));
		right.setClickListener(() -> setOption(selectedOption + 1));

		this.addElement(left);
		this.addElement(right);
		this.addElement(selectionDisplay);
	}

	@Override
	public void renderComponent() {}

	@Override
	public void cleanUpComponent() { }

	private void setOption(int option) {
		this.selectedOption = option % optionCount;
		while(selectedOption < 0) selectedOption += optionCount;
	}
}
