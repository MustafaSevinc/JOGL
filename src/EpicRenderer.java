import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EpicRenderer  extends SuperUselessRendererBaseClass {
    private int program;
    private int vao;
    private int vbo;

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
                "out vec4 color;\n" +
                "void main() {\n" +
                "  color = vec4(1.0, 0.6, 0.2, 1.0);\n" +
                "}\n";
        //==========//
        
        int vertexShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        gl.glShaderSource(vertexShader, 1, new String[]{vertexShaderSource}, null);
        gl.glCompileShader(vertexShader);
        
        int fragmentShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentShader, 1, new String[]{fragmentShaderSource}, null);
        gl.glCompileShader(fragmentShader);

        // Shader Program
        program = gl.glCreateProgram();
        gl.glAttachShader(program, vertexShader);
        gl.glAttachShader(program, fragmentShader);
        gl.glLinkProgram(program);

        // Delete shaders after linking
        gl.glDeleteShader(vertexShader);
        gl.glDeleteShader(fragmentShader);//GPU memorisinden temizler

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

        // Vertex Attributes
        gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 3 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        // Unbind VBO and VAO
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        
        gl.glUseProgram(program);
        gl.glBindVertexArray(vao);
        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, 3);
        gl.glBindVertexArray(0);
        gl.glUseProgram(0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glDeleteProgram(program);
        gl.glDeleteBuffers(1, new int[]{vbo}, 0);
        gl.glDeleteVertexArrays(1, new int[]{vao}, 0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(0, 0, width, height);
    }
}