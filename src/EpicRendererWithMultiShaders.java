import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EpicRendererWithMultiShaders  extends SuperUselessRendererBaseClass {
    private int[] programs;
    private int vao;
    private int vbo;
    private int[] vertexShaders;
    private int[] fragmentShaders;
    

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        vertexShaders = new int[5];
        fragmentShaders = new int[5];
        programs = new int[5];
        
        //GLSL KODLAR//
        String vertexShaderSource =
                "#version 330 core\n" +
                "layout(location = 0) in vec3 position;\n" +
                "void main() {\n" +
                "  gl_Position = vec4(position, 1.0);\n" +
                "}\n";
        String fragmentShaderSource =
                "#version 330 core\n" +
                "out vec4 color;\n" +
                "void main() {\n" +
                "  color = vec4(1.0, 0.6, 0.2, 1.0);\n" +
                "}\n";
        
        vertexShaders[0] = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        gl.glShaderSource(vertexShaders[0], 1, new String[]{vertexShaderSource}, null);
        gl.glCompileShader(vertexShaders[0]);
        
        fragmentShaders[0] = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentShaders[0], 1, new String[]{fragmentShaderSource}, null);
        gl.glCompileShader(fragmentShaders[0]);
        //=================================================================================================================/
       
        //GLSL KODLAR//
        vertexShaderSource =
                "#version 330 core\n" +
                "layout(location = 0) in vec3 position;\n" +
                "void main() {\n" +
                "  gl_Position = vec4(position, 1.0);\n" +
                "}\n";
        fragmentShaderSource =
                "#version 330 core\n" +
                "out vec4 color;\n" +
                "void main() {\n" +
                "  color = vec4(1.0, 1.0, 1.0, 1.0);\n" +
                "}\n";
        
        vertexShaders[1] = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        gl.glShaderSource(vertexShaders[1], 1, new String[]{vertexShaderSource}, null);
        gl.glCompileShader(vertexShaders[1]);
        
        fragmentShaders[1] = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentShaders[1], 1, new String[]{fragmentShaderSource}, null);
        gl.glCompileShader(fragmentShaders[1]);
        //=================================================================================================================/
        
        
        // Shader Program
        for(int i = 0; i<5;i++) {
        	programs[i] = gl.glCreateProgram();
            gl.glAttachShader(programs[i], vertexShaders[i]);
            gl.glAttachShader(programs[i], fragmentShaders[i]);
            gl.glLinkProgram(programs[i]);
          }
        //=================================================================================================================/    
        
        

        // Delete shaders after linking
        for(int i = 0; i<5;i++) {
    	  gl.glDeleteShader(vertexShaders[i]);
          gl.glDeleteShader(fragmentShaders[i]);	
        }
        //=================================================================================================================/

        // Vertex Data
        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                 0.5f, -0.5f, 0.0f,
                 0.0f,  0.5f, 0.0f
        };
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * Float.BYTES)
                                             .order(ByteOrder.nativeOrder())
                                             .asFloatBuffer()
                                             .put(vertices);
        vertexBuffer.flip();
        //=================================================================================================================/
        
        // Vertex Array Object
        int[] vaos = new int[1];
        gl.glGenVertexArrays(1, vaos, 0);
        vao = vaos[0];
        gl.glBindVertexArray(vao);

        // Vertex Buffer Object
        int[] vbos = new int[1];
        gl.glGenBuffers(1, vbos, 0);
        vbo = vbos[0];
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexBuffer.limit() * Float.BYTES, vertexBuffer, GL3.GL_STATIC_DRAW);
        //=================================================================================================================/
        // Vertex Attributes
        gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 3 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        // Unbind VBO and VAO
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
    }


    
    int displayCallCounter = 0;
    int chosenShader = 0;
    @Override
    public void display(GLAutoDrawable drawable) {
    	displayCallCounter++;
    	if(displayCallCounter>60) {
    		chosenShader=0;
    		displayCallCounter = 0;
    	}if(displayCallCounter >30) {
    		chosenShader = 1;
    	}
    	
    	
    	
    	GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(programs[chosenShader]);
        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, 3);
        gl.glBindVertexArray(0);
        gl.glUseProgram(0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        for(int i = 0; i<5 ; i++)
        	gl.glDeleteProgram(programs[i]);
        gl.glDeleteBuffers(1, new int[]{vbo}, 0);
        gl.glDeleteVertexArrays(1, new int[]{vao}, 0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(0, 0, width, height);
    }
}