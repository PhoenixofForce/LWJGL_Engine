package window.views;

import assets.audio.AudioPlayer;
import assets.audio.AudioType;
import gameobjects.entities.Camera;
import gameobjects.particles.ParticleSpawner;
import gameobjects.particles.ParticleType;
import utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import utils.Options;
import window.Window;
import window.font.*;
import window.gui.*;

import java.util.List;
import java.util.Optional;

public class TestView implements View {

	private Camera cam;
	private GuiText text;
	private GuiElement healthBar, staminaBar, manaBar, currentMana, crosshair, button, checkbox, selector;
	private GuiSlider slider;
	private GuiTextField field;

	private int particle;

	public TestView() {
		cam = new Camera();
		this.particle = ParticleSpawner.getNewSpawner(new Vector3f(0, 5, 0),
				new ParticleType().setDirection(MathUtils.randomVectorAround(new Vector3f(1, 0, 0), 360), 0));

		healthBar = new BasicColorGuiElement(new GuiConfig(Anchor.BEGIN, Anchor.BEGIN, 20, 20, 200, 20));
		staminaBar = new BasicColorGuiElement(new GuiConfig(Anchor.BOTTOM_RIGHT, -20, 20, 200, 20));
		manaBar = new BasicColorGuiElement(new GuiConfig(Anchor.BOTTOM_CENTER, 0.5f, 20, 200, 20));
		currentMana = new BasicColorGuiElement(new GuiConfig(Anchor.TOP_LEFT, 0, 1, 0.3f, 20));

		manaBar.addElement(currentMana);

		crosshair = new BasicColorGuiElement(new GuiConfig(0.5f, 0.5f, 10, 10));

		slider = new GuiSlider(new GuiConfig(Anchor.BOTTOM_LEFT, 50, 200, 200, 20));
		slider.setValue(Options.musicVolume);
		slider.setChangeListener(v -> Options.musicVolume = v);

		button = new GuiButton(new GuiConfig(Anchor.CENTERCENTER, 150, 275, 200, 50));
		checkbox = new GuiCheckbox(new GuiConfig(Anchor.BOTTOM_LEFT, 50, 320, 20, 20), false);
		selector = new GuiSelector(new GuiConfig(Anchor.BOTTOM_LEFT, 50, 400, 200, 50),
				List.of("Option 1", "Option 2", "Option 3", "Option 4")
		);

		Font font1 = new GeneralFont("WhitePeaberryOutline", 2);
		Font font2 = new TextureAtlasFont("Font");

		text = new GuiText(new GuiConfig(Anchor.TOP_LEFT,  20, -20f, 0.2f, 0.075f), font1, 24f, 50)
			.setText(new DynamicText() {
				@Override
				public String getText() {
					long runTime = Window.INSTANCE.getRuntime();
					return "[color=#ff0000]Ticks: " + runTime + "\r\nLorem ipsum dol";
				}
			});

		field = new GuiTextField(new GuiConfig(Anchor.BOTTOM_RIGHT, -50, 320, 200, 50), "Enter name...", Optional.of(new TextConfirmer.WordConfirmer()));

		Window.INSTANCE.setMouseClickListener((e, b) -> {
			if(e != 2) AudioType.EFFECT.play();
		});
	}

	@Override
	public void init(Window window) {
		window.addElement(healthBar);
		window.addElement(staminaBar);
		window.addElement(manaBar);
		window.addElement(crosshair);
		window.addElement(slider);
		window.addElement(button);
		window.addElement(checkbox);
		window.addElement(selector);
		window.addElement(text);
		window.addElement(field);

		ParticleSpawner.unFreeze(particle);
		ParticleSpawner.startSpawning(particle);
		AudioPlayer.playMusic(AudioType.MUSIC);
	}

	@Override
	public void update(long dt) {
		cam.update(dt);
	}

	@Override
	public void render() {
		Matrix4f projection_matrix = new Matrix4f().perspective((float)Math.PI/3, Window.INSTANCE.getWidth() / Window.INSTANCE.getHeight(),0.001f, 1250f);
		Matrix4f view_matrix = new Matrix4f().lookAt(cam.getPosition(), new Vector3f(cam.getLookingDirection()).add(cam.getPosition()), cam.getUp());

		ParticleSpawner.renderAll(projection_matrix, view_matrix);
	}

	@Override
	public void remove() {
		ParticleSpawner.freeze(particle);
	}

	@Override
	public void cleanUp() {
		ParticleSpawner.cleanUp(particle);
	}
}
