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
import com.footbits.oglwrapper.Color;
import com.footbits.oglwrapper.GlslProgram;
import com.texample2.GLText;


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


	private RenderedAxis leftAxis;
	private RenderedAxis upAxis;

	private RenderedObject crosshairs;

	private int width;
	private int height;

	//This flag is set by other classes when the axes need to be redrawn.
	//TODO: Find a better way.
	private boolean updateAxes;

	public AngleGLRenderer(Context context) {
		this.updateAxes = false;


		simpleGlslProgram = new GlslProgram(
				TextResourceReader.readTextFileFromResource(context, R.raw.simple_vert),
				TextResourceReader.readTextFileFromResource(context, R.raw.simple_frag));


		Mesh ch_mesh = UiElementDrawer.drawCrosshair(100, 3, 1000);
		crosshairs = new RenderedObject(ch_mesh, simpleGlslProgram);
		this.context = context;

		this.strings = new ArrayList<>();
		this.renderedObjects = new ArrayList<>();

		this.camera = new Camera(context, Camera.Projection.Orthographic);
		this.projectionViewMatrix = new float[16];

		this.leftAxis = new RenderedAxis(new AngleExtent(100), simpleGlslProgram);
		this.upAxis = new RenderedAxis(new AngleExtent(100), simpleGlslProgram);

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

		simpleGlslProgram.compile();

		loadRenderableObjects();
		initGLText();

		crosshairs.setColor(new Color(1.0f, 0.760784f, 0.0f, 1.0f));
		crosshairs.createBuffers();
		renderedObjects.add(crosshairs);
	}

	public void onDrawFrame(GL10 unused) {
		if (updateAxes) {
			leftAxis.updateMarks(height - 400, 30);
			upAxis.updateMarks(width - 300, 30);
			setAxisPositions(width, height);
			updateAxes = false;
		}

		// Redraw background color
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		camera.getProjectionViewnMatrix(projectionViewMatrix);

		renderObjects(renderedObjects);

		renderObjects(leftAxis.getMarks());
		renderObjects(upAxis.getMarks());

		renderStrings();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		this.width = width;
		this.height = height;

		// Set the OpenGL viewport to fill the entire surface.
		glViewport(0, 0, width, height);
		camera.setScreenDimensions(width, height);

		leftAxis.updateMarks(height - 400, 30);
		upAxis.updateMarks(width - 300, 30);

		setAxisPositions(width, height);
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

		for (RenderedString str : strings) renderString(str);

		for (RenderedString str : leftAxis.getRenderedStrings()) {
			renderString(str);
		}

		for(RenderedString str : upAxis.getRenderedStrings()) {
			renderString(str);
		}

		glText.end();
	}

	private void renderString(RenderedString str) {
		float[] mat = str.getTransform().getGlobalMatrix();
		Transform.setNoRotation(mat);

		glText.draw(str.getText(), mat);
	}

	private void loadRenderableObjects() {
		for (RenderedObject ro : renderedObjects) {
			// Make sure glsl program is built
			if (!ro.getProgram().isBuilt()) {
				ro.getProgram().compile();
			}

			//if still unbuilt, error building
			if (!(ro.getProgram().isBuilt())) {
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

	private void renderObjects(ArrayList<RenderedObject> objects) {
		float[] roMVP = new float[16];
		float[] temp = new float[16];

		for (RenderedObject ro : objects) {
			ro.getProgram().bind();

			int dataOffset = 0;
			int stride = 0;

			Matrix.multiplyMM(temp, 0, camera.getViewMatrix(), 0, ro.getTransform().getGlobalMatrix(), 0);
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
					ro.getColor().getRgbaArray(),
					0
			);


			ro.getGlIndexBuffer().bind();
			glDrawElements(GL_TRIANGLES, ro.getMesh().getIndices().length, GL_UNSIGNED_INT, 0);
			ro.getGlIndexBuffer().unBind();
		}
	}

	private void setAxisPositions(int width, int height) {
		float[] leftMat = leftAxis.getAxisTransform().getLocalMatrix();
		Matrix.setIdentityM(leftMat, 0);
		Matrix.translateM(leftMat, 0, -width / 2, 0.0f, 0.0f);

		float[] upMat = upAxis.getAxisTransform().getLocalMatrix();
		Matrix.setIdentityM(upMat, 0);

		Matrix.translateM(upMat, 0, 0, height / 2.0f, 0.0f);
		Matrix.rotateM(upMat, 0, -90, 0, 0, 1);
	}

	public RenderedObject getCrosshairs() {
		return crosshairs;
	}

	public RenderedAxis getLeftAxis() {
		return leftAxis;
	}

	public RenderedAxis getUpAxis() {
		return upAxis;
	}

	public void setUpdateAxes(boolean updateAxes) {
		this.updateAxes = updateAxes;
	}
}
