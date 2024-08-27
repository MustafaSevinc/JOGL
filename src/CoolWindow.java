import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.JFrame;

public class CoolWindow {
    private final String title;
    private final int width;
    private final int height;
    private SuperUselessRendererBaseClass renderer;

    public CoolWindow(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void run() {
    	
    	//OpenGL ve JFrame arasındaki iletişim GLCanvas üzerinden sağlanıyor. GLFW  IO ve işletim sist. pencereleri ile openGL iletişimi için kütüphane.
    	
        GLProfile profile = GLProfile.get(GLProfile.GL3); // OpenGL profili set edildi
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities); // Capabilities tanımlandı. (Duble buffer)
        
        
        
        renderer = new EpicRendererWithMovement();
        
        
        
        canvas.addGLEventListener(renderer); // Yazdığımız renderer'i glCanvas listener olarak ekledik. (display, render, init metodları ovırride ettik)

        JFrame frame = new JFrame(title);
        frame.getContentPane().add(canvas);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start(); // displayi cagirmaya baslar (60 fps set edildi bi üstte)
    }
    
    /*
     * JFrame oluşturuluyor.
     * JFrame, işletim sistemi üzerinden bir pencere talep ediyor.
     * İşletim sistemi, pencereyi oluşturuyor ve kontrolünü Java uygulamasına veriyor.
     * Bir GLCanvas oluşturuluyor ve JFrame içerisine ekleniyor.
     * GLCanvas, OpenGL komutları ile render ediliyor.
     * OpenGL ile yapılan çizimler, ekran kartı tarafından işleniyor ve sonrasında GLCanvas alanında görüntüleniyor.
     */
}