import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.lang.Math;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;

public class EpicRendererRadar extends SuperUselessRendererBaseClass {
	
    private int program;
    private int vao;
    private int vbo;
    
    private static final int NUM_SEGMENTS = 4096;
    private static final float RADIUS = 0.5f;
    private static final float START_ANGLE = 0.0f;
    private static final float END_ANGLE = (float)Math.PI*2;

    private int currentAzimuth = 0;
    private int azimuthRange= 100;
    
	@Override
	public void init(GLAutoDrawable drawable) {
		 GL3 gl = drawable.getGL().getGL3();

	        //GLSL KODLAR//
	        String vertexShaderSource =
	                "#version 330 core\n" +
	                "layout(location = 0) in vec3 position;\n" +
	                "void main() {\n" +
	                "  gl_Position = vec4(position, 1.0);\n" +
	                "}\n";
	        String fragmentShaderSource =
	                "#version 330 core\n" +
	                "void main() {\n" +
	                "  gl_FragColor = vec4(0.0,1.0,0.0,1.0);\n" +
	                "}\n";
	        //==========//

	        // Compile and link shaders
	        program = createShaderProgram(gl, vertexShaderSource, fragmentShaderSource);

	        // Generate circle segment vertices
	        float[] vertices = generateCircleSegmentVertices(RADIUS, START_ANGLE, END_ANGLE, NUM_SEGMENTS);

	        // Setup VAO and VBO
	        int[] vaoId = new int[1];
	        int[] vboId = new int[1];
	        gl.glGenVertexArrays(1, vaoId, 0);
	        gl.glGenBuffers(1, vboId, 0);
	        vao = vaoId[0];
	        vbo = vboId[0];

	        gl.glBindVertexArray(vao);
	        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo);
	        gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, FloatBuffer.wrap(vertices), GL3.GL_STATIC_DRAW);
	        
	        // Define the layout of the vertex data
	        gl.glEnableVertexAttribArray(0);
	        gl.glVertexAttribPointer(0, 2, GL3.GL_FLOAT, false, 2 * Float.BYTES, 0);

	        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
	        gl.glBindVertexArray(0);
	        startRadarTurningThread();
	}
    
	@Override
	public void display(GLAutoDrawable drawable) {
	    final GL3 gl = drawable.getGL().getGL3();
	    gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
	    
	    gl.glUseProgram(program);
	    gl.glBindVertexArray(vao);
	    gl.glDrawArrays(GL3.GL_TRIANGLE_FAN, currentAzimuth, 100); 
	    gl.glBindVertexArray(0);
	    gl.glUseProgram(0);

	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(0, 0, width, height);
    }
	
	
	
	
	
	
	
	
	
	
    private float[] generateCircleSegmentVertices(float radius, float startAngle, float endAngle, int numSegments) {
        ArrayList<Float> verticesList = new ArrayList<>();
        float angleStep = (endAngle - startAngle) / numSegments;

        for (int i = 0; i <= numSegments; ++i) {
            float angle = startAngle + i * angleStep;
            if(i%azimuthRange == 0) {
            	verticesList.add(0f);
            	verticesList.add(0f);
            }
            verticesList.add(radius * (float)Math.cos(angle));
            verticesList.add(radius * (float)Math.sin(angle));
            if(i%azimuthRange == 0) {
            	verticesList.add(0f);
            	verticesList.add(0f);
            }
        }

        float[] vertices = new float[verticesList.size()];
        for (int i = 0; i < verticesList.size(); i++) {
            vertices[i] = verticesList.get(i);
        }
        return vertices;
    }
	
	  private int compileShader(GL3 gl, int type, String src) {
	        int shader = gl.glCreateShader(type);
	        gl.glShaderSource(shader, 1, new String[]{src}, null);
	        gl.glCompileShader(shader);
	        
	        int[] compileStatus = new int[1];
	        gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, compileStatus, 0);
	        if (compileStatus[0] == GL3.GL_FALSE) {
	            // Compilation failed, get log
	            int[] logLength = new int[1];
	            gl.glGetShaderiv(shader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);
	            byte[] log = new byte[logLength[0]];
	            gl.glGetShaderInfoLog(shader, logLength[0], (int[]) null, 0, log, 0);
	            System.err.println("Shader compilation failed: " + new String(log));
	            System.exit(1);
	        }
	        
	        return shader;
	    }
	
	
	    private int createShaderProgram(GL3 gl, String vertexSrc, String fragmentSrc) {
	        int vertexShader = compileShader(gl, GL3.GL_VERTEX_SHADER, vertexSrc);
	        int fragmentShader = compileShader(gl, GL3.GL_FRAGMENT_SHADER, fragmentSrc);
	        
	        int prog = gl.glCreateProgram();
	        gl.glAttachShader(prog, vertexShader);
	        gl.glAttachShader(prog, fragmentShader);
	        gl.glLinkProgram(prog);
	        
	        int[] linkStatus = new int[1];
	        gl.glGetProgramiv(prog, GL3.GL_LINK_STATUS, linkStatus, 0);

	        gl.glDeleteShader(vertexShader);
	        gl.glDeleteShader(fragmentShader);
	        
	        return prog;
	    }
	    
	    
	    
	    
	    
	    private void startRadarTurningThread() {
	    	new Thread(()->{while(true) {
	    		currentAzimuth += (azimuthRange+2);
	    		if(currentAzimuth>4096)
	    			currentAzimuth = 0;
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	}}).start();
	    }
	  
	  
}
