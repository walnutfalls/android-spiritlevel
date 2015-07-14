package com.footbits.sava.oglspiritleveldisplay;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_UNSIGNED_INT;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glViewport;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.concubicycle.util.TextResourceReader;
import com.footbits.oglwrapper.AttributeInfo;
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
		camera.lookAt(0, 0, 5, //position
				0, 0, 0, //look at point
				0, 1, 0); //up
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		// Set the background frame color
		glClearColor(0.2f, 0.0f, 0.3f, 1.0f);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		glDepthFunc(GLES20.GL_LEQUAL);

		// enable texture + alpha blending
		glEnable(GLES20.GL_BLEND);
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		camera.getProjectionViewnMatrix(projectionViewMatrix);

		float[] roMVP = new float[16];
		float[] temp  = new float[16];

		for (RenderedObject ro : renderedObjects) {
			ro.getProgram().bind();

			int dataOffset = 0;
			int stride = 0;

			Matrix.multiplyMM(temp, 0, camera.getViewMatrix(), 0, ro.getTransform().modelMatrix, 0);
			Matrix.multiplyMM(roMVP, 0, camera.getProjectionMatrix(), 0, temp, 0);

			AttributeInfo posAttrInfo = ro.getProgram().getAttributeNameToInfo().get("a_Position");


			ro.getGlVertexBuffer().setVertexAttribPointer(
					dataOffset,
					posAttrInfo.location,
					4,
					stride,
					posAttrInfo.glslType);


			glUniformMatrix4fv(
					ro.getProgram().uniformLocation("u_Matrix"),
					1,
					false,
					roMVP,
					0);

			glUniform4fv(
					ro.getProgram().uniformLocation("u_Color"),
					1,
					new float[]{0.3f, 0.75f, 0.5f, 1},
					0
			);


			ro.getGlIndexBuffer().bind();
			glDrawElements(GL_TRIANGLES, ro.getMesh().getIndices().length, GL_UNSIGNED_INT, 0);
			ro.getGlIndexBuffer().unBind();
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

			ro.createBuffers();
		}
	}

	private void addSampleSquare() {
		ShapeBuilder builder = new ShapeBuilder(true);

		builder.addQuad(
				100.0f, 0.0f, -1.0f,
				100.0f, 100.0f, -1.0f,
				0.0f, 100.0f, -1.0f,
				0.0f, 0.0f, -1.0f);

		builder.addTriangle(
				0.0f, 0.0f, -1.0f,
				-300.0f, 0.0f, -1.0f,
				0.0f, -200.0f, -1.0f);



		Mesh mesh = builder.toMesh();
		this.renderedObjects.add(new RenderedObject(mesh, simpleGlslProgram));
	}
}
