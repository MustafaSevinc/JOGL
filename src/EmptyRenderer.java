import com.jogamp.opengl.GLAutoDrawable;

public class EmptyRenderer extends SuperUselessRendererBaseClass {

	@Override
	public void display(GLAutoDrawable arg0) {
		System.out.println("DISPLAY");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		System.out.println("DISPOSE");
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		System.out.println("INIT");
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		System.out.println("RESHAPE");
		
	}

}
