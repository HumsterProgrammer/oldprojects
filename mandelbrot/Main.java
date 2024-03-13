
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.RepaintManager;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class Main extends Component{
	private static int width = 700;
	private static int height = 700;
	protected static double min_z = -2;
	protected static double max_z = 2;
	
	protected static double centerx = -0.5;
	protected static double centery = 0;
	
	protected static int depth = 200;
	protected static boolean mustRepaint = false;
	
	private BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	
	private int getC(double cx, double cy){
		double x = 0;
		double y = 0;
		for(int n = 0; n< depth; n++){
			double t = x;
			x = x*x - y*y + cx;
			y = 2*t*y + cy;
			if(x*x + y*y >4){
				return (int)((double)n/depth * 0xffffff);
			}
		}
		return 0;
	}
	
	private void render(){
		double mnogx = (max_z - min_z)/width;
		double mnogy = (max_z - min_z)/height;
		
		double c = (max_z-min_z)/2;
		
		for(int y = 0; y< height; y++){
			double cy = y*mnogy-c + centery;
			for(int x = 0; x<width; x++){
				double cx = x*mnogx-c + centerx;
				img.setRGB(x,y, getC(cx, cy));
			}
		}
	}
	
	@Override
	public void paint(Graphics g){
		render();
		g.drawImage(img, 0,0,null);
	}
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.setSize(width, height);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Main m = new Main();
		m.setBounds(0,0,width, height);
		
		MouseListener l = new ML();
		m.addMouseListener(l);
		f.add(m);
		
		f.setVisible(true);
		
		RepaintManager rm = new RepaintManager();
		while(true){
			f.repaint(0,0,width, height);
			rm.paintDirtyRegions();
			try{
				Thread.currentThread().sleep(100);
			}catch(Exception e){}
		}
	}
	
	static class ML implements MouseListener{
		public void mouseClicked(MouseEvent e){
			double mnogx = (max_z - min_z)/width;
			double mnogy = (max_z - min_z)/height;
			double c = (max_z-min_z)/2;
			centery += e.getY()*mnogy-c;
			centerx += e.getX()*mnogx-c;
			
			min_z /= 2;
			max_z /= 2;
			depth += 2;
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
	}
}
