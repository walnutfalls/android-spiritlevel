package com.footbits.sava.oglspiritleveldisplay;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_TRIANGLES;

import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glViewport;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.concubicycle.util.TextResourceReader;
import com.footbits.oglwrapper.Camera;
import com.footbits.oglwrapper.GlslProgram;
import com.texample2.GLText;
import com.texample2.RenderedString;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class AngleGLRenderer implements GLSurfaceView.Renderer {
	private static final String TAG = "AngleGLRenderer";

	private Context context;
	private ArrayList<RenderedString> strings;
	private ArrayList<RenderedObject> renderedObjects;
	private GLText glText;
	private Camera camera;

	private GlslProgram simpleGlslProgram;

    private float[] projectionViewMatrix;
	private int currentGlslProgramId;

	public AngleGLRenderer(Context context) {
		this.context = context;

		this.strings = new ArrayList<>();
		this.renderedObjects = new ArrayList<>();

		this.camera = new Camera(context, Camera.Projection.Orthographic);
        this.projectionViewMatrix = new float[16];

		// camera is looking down negative z axis
		camera.lookAt(0, 0, 3, //position
				0, 0, 0, //look at point
				0, 1, 0); //up
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.2f, 0.0f, 0.3f, 1.0f);

		// enable texture + alpha blending
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		simpleGlslProgram = new GlslProgram(
				TextResourceReader.readTextFileFromResource(context, R.raw.simple_vert),
				TextResourceReader.readTextFileFromResource(context, R.raw.simple_frag));

		simpleGlslProgram.compile();



		/// TEMP
		addSampleSquare();
		////



		loadRenderableObjects();
		initGLText();
	}

	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		for(RenderedObject ro : renderedObjects) {
			ro.getProgram().bind();

			ro.getMesh().getGlVertexBuffer().setVertexAttribPointer(
					0,
					ro.getProgram().attributeLocation("a_Position"),
					4,
					0);

			ro.getMesh().getGlIndexBuffer().bind();
			glDrawElements(GL_TRIANGLES, ro.getMesh().getIndices().length, GL_UNSIGNED_SHORT, 0);
			ro.getMesh().getGlIndexBuffer().unBind();
		}

        renderStrings();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);
		camera.setScreenDimensions(width, height);
	}

	public ArrayList<RenderedString> getStrings() {
		return strings;
	}


	private void initGLText() {
		glText = new GLText(context.getAssets());

		// Load the font from file (set size + padding), creates the texture
		// NOTE: after a successful call to this the font is ready for rendering!
		glText.load("Roboto-Regular.ttf", 40, 1, 1);  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)
	}

    private void renderStrings() {
        camera.getProjectionViewnMatrix(projectionViewMatrix);
        glText.begin(projectionViewMatrix);
        for (RenderedString str : strings) {
            glText.draw(str.getText(), str.getX(), str.getY());
        }
        glText.end();
    }

	private void loadRenderableObjects() {
		for(RenderedObject ro : renderedObjects)
		{
			// Make sure glsl program is built
			if(!ro.getProgram().isBuilt())
			{
				ro.getProgram().compile();
			}

			//if still unbuilt, error building
			if(!(ro.getProgram().isBuilt()))
			{
				Log.e(TAG,
					"Couldn't build shader program.\n" +
					"Vertex Shader: " + "\n" +
					"---------------" + "\n" +
					ro.getProgram().getVertexShader().getSource() + "\n" +
					"Fragment Shader: " + "\n" +
					"---------------" + "\n" +
					ro.getProgram().getFragmentShader().getSource() + "\n\n");
			}

			// Create VBO's and such
			ro.getMesh().createBuffers();
		}
	}

	private void addSampleSquare() {
		ShapeBuilder builder = new ShapeBuilder(true);

		builder.addQuad(
				1.0f, -1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
				-1.0f, -1.0f, 0.0f);

		Mesh mesh = builder.toMesh();

		this.renderedObjects.add(new RenderedObject(mesh, simpleGlslProgram));
	}

}
