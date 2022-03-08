package rendering;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

	public static void render(ShaderHandler.ShaderType shaderType, Renderable renderable, Uniform uniform) {
		if(renderable == null) return;

		glBindVertexArray(renderable.getVAO());
		glUseProgram(shaderType.get());

		uniform.load();

		glDrawElements(GL_TRIANGLES, 3 * renderable.getFaceCount(), GL_UNSIGNED_INT, 0);

		glBindVertexArray(0);
		glUseProgram(0);
	}

	public static void renderArrays(ShaderHandler.ShaderType shaderType, Renderable renderable, Uniform uniform) {
		if(renderable == null) return;

		glBindVertexArray(renderable.getVAO());
		glUseProgram(shaderType.get());

		uniform.load();

		glDrawArrays(GL_TRIANGLES, 0, 3 * renderable.getFaceCount());

		glBindVertexArray(0);
		glUseProgram(0);
	}


}
